package com.jangz.recommendation.spark.core

import com.jangz.recommendation.config.Config
import com.jangz.recommendation.util.SparkUtil
import com.jangz.recommendation.dao.DimensionDao
import com.jangz.recommendation.entity.Dimension
import org.apache.spark.broadcast.Broadcast

object SearchAnalysis {
  
  def runAnalysis(): Unit = {
    val sc = SparkUtil.getSparkContext(this.getClass)
    
    val lines = sc.textFile(Config.input_path)
    val filter = SparkUtil.getFilterLog(lines).cache()
    
    val searchMap: Map[String, Dimension] = DimensionDao.getSearchEngineMap()
    var searchBroadCast: Broadcast[Map[String, Dimension]] = sc.broadcast(searchMap)
    
    /**
     * calculate search engine(map, reduce)
     */
    val map = filter.map(log => {
      val dimension: Dimension = new Dimension(pv = 1, uv = 1, ip = 1)
      val search = log.SearchEngine
      dimension.uvs += log.Uuid
      dimension.ips += log.Ip
      if (search != null) {
        if (searchBroadCast.value.contains(search)) {
          dimension.dimeId = searchBroadCast.value.get(search).get.dimeId
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