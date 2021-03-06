package com.jangz.recommendation.spark.core

import org.apache.spark.broadcast.Broadcast

import com.jangz.recommendation.config.Config
import com.jangz.recommendation.dao.DimensionDao
import com.jangz.recommendation.entity.Dimension
import com.jangz.recommendation.util.SparkUtil

object CountryAnalysis {
  
  def runAnalysis(): Unit = {
    val sc = SparkUtil.getSparkContext(this.getClass)
    
    val lines = sc.textFile(Config.input_path)
    val filter = SparkUtil.getFilterLog(lines).cache()
    
    val countryMap: Map[String, Dimension] = DimensionDao.getCountryMap()
    val countryBroadCast: Broadcast[Map[String, Dimension]] = sc.broadcast(countryMap)
    
    /**
     * calculate country distribution
     */
    val map = filter.map(log => {
      val dimension: Dimension = new Dimension(pv = 1, uv = 1, ip = 1)
      val country = log.Country
      dimension.uvs += log.Uuid
      dimension.ips += log.Ip
      if (country != null) {
        if (countryBroadCast.value.contains(country)) {
          dimension.dimeId = countryBroadCast.value.get(country).get.dimeId
        }
      }
      (dimension.dimeId, dimension)
    }).filter(_._1 != 0).cache()
    
    /**
     * pv & uv & ip by general ReduceByKey
     */
    val reduce = SparkUtil.getReduceByKey(map)
    
    val list: List[Dimension] = reduce.values.collect().toList
    list.foreach(item => {
      item.day = Config.day
    })
    sc.stop()
    
    /**
     * save into DB
     */
    DimensionDao.saveDimensionList(list)
  }
  
  def main(args: Array[String]): Unit = {
    val dayStr = if (args.length > 0) args(0) else "2016-12-01"
    Config.setDay(dayStr)
    runAnalysis()
  }
}