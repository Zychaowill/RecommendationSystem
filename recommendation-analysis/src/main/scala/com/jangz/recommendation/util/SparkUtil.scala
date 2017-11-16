package com.jangz.recommendation.util

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import com.jangz.recommendation.entity.Log
import com.jangz.recommendation.entity.Dimension
import com.alibaba.fastjson.JSON

object SparkUtil {

  def getSparkConf(cls: Class[_]): SparkConf = {
    new SparkConf()
      .setAppName(cls.getSimpleName)
      .setIfMissing("spark.master", "local[*]")
  }
  
  def getSparkContext(cls: Class[_]): SparkContext = {
    new SparkContext(getSparkConf(cls))
  }
  
  def getFilterLog(lines: RDD[String]): RDD[Log] = {
    lines.map(line => {
      val log: Log = JSON.parseObject(line, classOf[Log])
      if (log.isLegal()) {
        (log)
      } else {
        (null)
      }
    }).filter(_ != null)
  }
  
  def getReduceByKey(map: RDD[(Int, Dimension)]): RDD[(Int, Dimension)] = {
    map.reduceByKey((m, n) => {
      m.pv += n.pv
      m.uvs ++= n.uvs
      m.ips ++= n.ips
      m.uv = n.uvs.size
      m.ip = n.ips.size
      (m)
    })
  }
}