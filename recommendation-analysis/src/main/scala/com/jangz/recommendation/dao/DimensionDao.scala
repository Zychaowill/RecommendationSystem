package com.jangz.recommendation.dao

import scala.collection.mutable.ListBuffer

import com.jangz.recommendation.basic.BasicSimpleDao
import com.jangz.recommendation.entity.Dimension

object DimensionDao {

  def getDimensionConfig(dimension: Dimension): ListBuffer[Dimension] = {
    val sql: String = "SELECT id dimeId,`value` FROM common_dimension WHERE `type` = #{type}"
    BasicSimpleDao.getSqlList(sql, dimension).asInstanceOf[ListBuffer[Dimension]]
  }

  def getDimensionConfigMap(dimension: Dimension): Map[String, Dimension] = {
    val list: ListBuffer[Dimension] = getDimensionConfig(dimension)
    var map: Map[String, Dimension] = Map[String, Dimension]()
    for (dimension <- list) {
      map += (dimension.value -> dimension)
    }
    map
  }

  def getSearchEngineMap(): Map[String, Dimension] = {
    val dimension: Dimension = new Dimension
    dimension.`type` = "Search-engine"
    DimensionDao.getDimensionConfigMap(dimension)
  }

  def getCountryMap(): Map[String, Dimension] = {
    val dimension: Dimension = new Dimension
    dimension.`type` = "country"
    DimensionDao.getDimensionConfigMap(dimension)
  }

  def getProvinceMap(): Map[String, Dimension] = {
    val dimension: Dimension = new Dimension
    dimension.`type` = "province"
    DimensionDao.getDimensionConfigMap(dimension)
  }

  def saveDimensionData(dimension: Dimension): Int = {
    val sql: String = "insert into sparkcore_dimension_data(dimeid,`day`,pv,uv,ip,time) values (#{dimeId},#{day},#{pv},#{uv},#{ip},#{time}) on duplicate key update pv = values(pv),uv = values(uv),ip = values(ip),time = values(time)"
    BasicSimpleDao.saveObject(sql, dimension)
  }

  def saveDimensionList(list: List[Dimension]): Int = {
    val sql: String = "insert into sparkcore_dimension_data(dimeid,`day`,pv,uv,ip,time) values (#{dimeId},#{day},#{pv},#{uv},#{ip},#{time}) on duplicate key update pv = values(pv),uv = values(uv),ip = values(ip),time = values(time)"
    BasicSimpleDao.saveListBatch(sql, list)
  }

  def saveStreamingDimensionList(list: List[Dimension]): Int = {
    val sql: String = "insert into streaming_dimension_data(dimeid,`second`,pv,uv) values (#{dimeId},#{second},#{pv},#{uv}) on duplicate key update pv = values(pv),uv = values(uv)"
    BasicSimpleDao.saveListBatch(sql, list)
  }

}