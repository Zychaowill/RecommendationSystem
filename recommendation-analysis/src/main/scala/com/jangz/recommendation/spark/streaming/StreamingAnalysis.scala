package com.jangz.recommendation.spark.streaming

import java.sql.Connection
import java.util.Calendar

import scala.util.control.NonFatal

import org.apache.spark.broadcast.Broadcast
import org.apache.spark.streaming.Seconds
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.kafka.KafkaUtils

import com.alibaba.fastjson.JSON
import com.jangz.recommendation.config.Config
import com.jangz.recommendation.dao.ContentDao
import com.jangz.recommendation.dao.DimensionDao
import com.jangz.recommendation.entity.Content
import com.jangz.recommendation.entity.Dimension
import com.jangz.recommendation.entity.Log
import com.jangz.recommendation.util.SparkUtil
import com.jangz.recommendation.util.StringUtil
import com.jangz.recommendation.util.db.DBHelper

import kafka.serializer.StringDecoder

object StreamingAnalysis {
  
  def dimensionDStream(filter: DStream[Log], bc: Broadcast[Map[String, Dimension]]): DStream[(String, Dimension)] = {
    val map = filter.map(log => {
      val dimension: Dimension = new Dimension(pv = 1, uv = 1, ip = 1)
      val second = getSecond(log)
      dimension.second = second
      val area = log.Area
      if (area != null) {
        if (bc.value.contains(area)) {
          dimension.dimeId = bc.value.get(area).get.dimeId
        }
      }
      (dimension.dimeId + "-" + dimension.second, dimension)
    }).filter(_._1 != 0).cache()
    
    map.reduceByKey((m, n) => {
      m.pv += n.pv
      m.uvs ++= n.uvs
      m.uv = m.uvs.size
      (m)
    })
  }
  
  def dimensionSaveDB(result: DStream[(String, Dimension)]) {
    result.foreachRDD(rdd => {
      val list: List[Dimension] = rdd.values.collect().toList
      if (list != null && list.size > 0) {
        DimensionDao.saveStreamingDimensionList(list)
      }
    })
  }
  
  def contentDStream(filter: DStream[Log]): DStream[(String, Content)] = {
    val map = filter.filter(log => {
      log.getPageType() == '1' && log.getClearTitle() != null && log.getClearTitle().length() > 5
    }).map(log => {
      val content: Content = new Content(pv = 1, uv = 1)
      val second = getSecond(log)
      content.second = second
      content.uvs += log.Uuid
      content.url = log.Url
      content.title = log.getClearTitle()
      (content.contentId + "-" + content.second, content)
    }).cache()
    
    map.reduceByKey((m ,n) => {
      m.pv += n.pv
      m.uvs ++= n.uvs
      m.uv = m.uvs.size
      (m)
    })
  }
  
  def contentSaveDB(result: DStream[(String, Content)]) {
    result.foreachRDD(rdd => {
      val list: List[Content] = rdd.values.collect().toList
      if (list != null && list.size > 1) {
        list.foreach(item => {
          item.url = StringUtil.limitString(item.url, 500, "utf8")
          item.title = StringUtil.limitString(item.title, 500, "utf8")
        })
        val conn: Connection = DBHelper.getConnection
        try {
          ContentDao.saveStreamingContentData(list, conn)
          ContentDao.saveContentDetail(list, conn)
        } finally {
          DBHelper.close(conn)
        }
      }
    })
  }
  
  def getFilterLog(stream: InputDStream[(String, String)]): DStream[Log] = {
    stream.flatMap(line => parseLog(line._2))
  }
  
  def parseLog(line: String): Option[Log] = {
    try {
      val log = JSON.parseObject(line, classOf[Log])
      if (log.isLegal()) Some(log) else None
    } catch {
      case NonFatal(e) => None
    }
  }
  
  private def getSecond(log: Log): Int = {
    val ts: Long = log.getTs
    val millisecond: String = ts + "000"
    val calendar: Calendar = Calendar.getInstance
    calendar.setTimeInMillis(millisecond.toLong)
    val hour: Int = calendar.get(Calendar.HOUR_OF_DAY)
    val minute: Int = calendar.get(Calendar.MINUTE)
    val secondOfHour: Int = calendar.get(Calendar.SECOND)
    val second: Int = hour * 3600 + minute * 60 + secondOfHour
    second
  }
  
  def main(args: Array[String]): Unit = {
    val second = 1
    var brokerList: String = Config.brokerList
    val topic: String = Config.topic
    val conf = SparkUtil.getSparkConf(this.getClass)
    
    val ssc = new StreamingContext(conf, Seconds(second))
    val kafkaParams = Map[String, String]("metadata.broker.list" -> brokerList, "auto.offset.reset" -> "smallest", "group.id" -> "tvStreaming")
    val stream = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, Set(topic))
    
    val provinceMap: Map[String, Dimension] = DimensionDao.getProvinceMap()
    val provinceBroadCast: Broadcast[Map[String, Dimension]] = stream.context.sparkContext.broadcast(provinceMap)
    
    val filter = getFilterLog(stream)
    
    val result: DStream[(String, Dimension)] = dimensionDStream(filter, provinceBroadCast)
    val result2: DStream[(String, Content)] = contentDStream(filter)
    
    dimensionSaveDB(result)
    contentSaveDB(result2)
    
    ssc.start()
    ssc.awaitTermination()
  }
}