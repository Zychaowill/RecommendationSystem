package com.jangz.recommendation.dao

import com.jangz.recommendation.base.BasicDao
import com.jangz.recommendation.entity.Content
import java.sql.Connection

object ContentDao {
  
  def saveContentData(list: List[Content], conn: Connection): Int = {
    val sql: String = "insert into sparkcore_content_data(contentId,`day`,pv,uv) values (#{contentId},#{day},#{pv},#{uv}) on duplicate key update pv = values(pv),uv = values(uv)"
    BasicDao.saveListBatch(sql, list, conn)
  }

  def saveContentDetail(list: List[Content], conn: Connection): Int = {
    val sql: String = "insert into sparkcore_content_detail(contentId,url,title) values (#{contentId},#{url},#{title}) on duplicate key update url = values(url),title = values(title)"
    BasicDao.saveListBatch(sql, list, conn)
  }

  def saveStreamingContentData(list: List[Content], conn: Connection): Int = {
    val sql: String = "insert into streaming_content_data(contentId,`second`,pv,uv) values (#{contentId},#{second},#{pv},#{uv}) on duplicate key update pv = values(pv),uv = values(uv)"
    BasicDao.saveListBatch(sql, list, conn)
  }

  def saveStreamingContentDetail(list: List[Content], conn: Connection): Int = {
    val sql: String = "insert into streaming_content_detail(contentId,url,title) values (#{contentId},#{url},#{title}) on duplicate key update url = values(url),title = values(title)"
    BasicDao.saveListBatch(sql, list, conn)
  }
}