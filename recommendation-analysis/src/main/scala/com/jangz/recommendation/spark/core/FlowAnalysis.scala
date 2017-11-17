package com.jangz.recommendation.spark.core

import com.jangz.recommendation.config.Config
import com.jangz.recommendation.util.SparkUtil
import com.jangz.recommendation.entity.Dimension
import com.jangz.recommendation.dao.DimensionDao

object FlowAnalysis {

  def runAnalysis(): Unit = {
    val sc = SparkUtil.getSparkContext(this.getClass)

    val lines = sc.textFile(Config.input_path)
    val filter = SparkUtil.getFilterLog(lines).cache()

    /**
     * pv & uv & ip by MR
     */
    val map = filter.map(log => {
      val dimension: Dimension = new Dimension(pv = 1, uv = 1, ip = 1)
      dimension.Ts = log.Ts
      dimension.uvs += log.Uuid
      dimension.ips += log.Ip
      (dimension)
    }).cache()

    /**
     * page views accumulate directly, user views and ip need to merge
     */
    val reduce = map.reduce((m, n) => {
      m.pv += n.pv
      m.uvs ++= n.uvs
      m.ips ++= n.ips
      m.uv = m.uvs.size
      m.ip = m.ips.size
      (m)
    })

    /**
     * 计算时长(mapTime, reduceTime)
     */
    val mapTime = filter.map(log => (log.Uuid, log.Ts)).cache()

    /**
     * 1. 分组后对每个用户的访问时间排序
     * 2. 遍历每个用户的访问时间，如果前后两次访问的时间差达到半小时（3600秒）就认为访问中断只加10秒
     * 否则就加上两次访问的时间差
     */
    val reduceTime = mapTime.groupByKey().map(m => {
      val list: List[Long] = m._2.toList.sorted
      var time: Long = 10L
      if (list.size > 1) {
        var cachets: Long = 0
        list.foreach(ts => {
          if (cachets != 0) {
            val sub: Long = ts - cachets
            if (sub >= 1800) {
              time += 10
            } else {
              time += sub
            }
          }
          cachets = ts
        })
      }
      (time)
    }).reduce(_ + _)
    
    sc.stop()
    reduce.time = reduceTime
    reduce.day = Config.day
    
    /**
     * save into DB
     */
    DimensionDao.saveDimensionData(reduce)
  }

  def main(args: Array[String]): Unit = {
    val dayStr = if (args.length > 0) args(0) else "2016-12-01"
    Config.setDay(dayStr)
    runAnalysis()
  }
}