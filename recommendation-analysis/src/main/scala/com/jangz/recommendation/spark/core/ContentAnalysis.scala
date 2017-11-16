package com.jangz.recommendation.spark.core

import com.jangz.recommendation.util.SparkUtil
import com.jangz.recommendation.config.Config
import com.jangz.recommendation.entity.Content
import com.jangz.recommendation.util.StringUtil
import java.sql.Connection
import com.jangz.recommendation.util.db.DBHelper
import com.jangz.recommendation.dao.ContentDao

object ContentAnalysis {
  
  def runAnalysis(): Unit = {
    val sc = SparkUtil.getSparkContext(this.getClass)
    
    val lines = sc.textFile(Config.input_path)
    val filter = SparkUtil.getFilterLog(lines)
    
    /**
     * the hot point of manuscript(MR)
     */
     val filtered = filter.filter(log => {
       log.getPageType() == '1' && log.getClearTitle() != null && log.getClearTitle().length() > 5
     })
     val map = filtered.map(log => {
       val content: Content = new Content(pv = 1, uv = 1)
       content.contentId = log.ContentId
       content.uvs += log.Uuid
       content.url = log.Url
       content.title = log.getClearTitle()
       (content.contentId, content)
     })
     
     /**
      * pv & uv of manuscript
      */
     val reduce = map.reduceByKey((m, n) => {
       m.pv += n.pv
       m.uvs ++ n.uvs
       m.uv = m.uvs.size
       (m)
     })
     
     val list: List[Content] = reduce.values.collect().toList
     list.foreach(item => {
       item.day = Config.day
       item.url = StringUtil.limitString(item.url, 500, "utf8")
       item.title = StringUtil.limitString(item.title, 500, "utf8")
     })
     sc.stop()
     
     /**
      * save into DB
      */
     val conn: Connection = DBHelper.getConnection
     
     try {
       ContentDao.saveContentData(list, conn)
       ContentDao.saveContentData(list, conn)
     } finally {
       DBHelper.close(conn)
     }
  }
  
  def main(args: Array[String]): Unit = {
    val dayStr = if (args.length > 0) args(0) else "2016-12-01"
      Config.setDay(dayStr)
      runAnalysis()
  }
}