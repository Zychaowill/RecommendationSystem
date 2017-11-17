package com.jangz.recommendation.basic

import java.sql.{ Connection, PreparedStatement, ResultSet, ResultSetMetaData }
import scala.collection.mutable.ListBuffer
import com.jangz.recommendation.util.db.DBHelper
import org.apache.commons.lang3.StringUtils
import java.lang.reflect.Method

object BasicDao {

  def getSqlObject(sql: String, entity: Any, conn: Connection): Any = {
    val list: ListBuffer[Any] = getSqlList(sql, entity, conn)
    if (list != null && list.nonEmpty) {
      list.head
    } else {
      null
    }
  }

  def getSqlList(sql: String, entity: Any, conn: Connection): ListBuffer[Any] = {
    val cls: Class[_] = entity.getClass
    var pstmt: PreparedStatement = null
    var rs: ResultSet = null

    try {
      pstmt = conn.prepareStatement(getRealSql(sql))
      setPreparedSql(sql, pstmt, entity)
      rs = pstmt.executeQuery()
      getRsListFromMetaData(rs, cls)
    } finally {
      DBHelper.close(rs)
      DBHelper.close(pstmt)
    }
  }

  def getSqlList(sql: String, cls: Class[_], conn: Connection): ListBuffer[Any] = {
    var pstmt: PreparedStatement = null
    var rs: ResultSet = null
    try {
      pstmt = conn.prepareStatement(sql)
      rs = pstmt.executeQuery()
      getRsListFromMetaData(rs, cls)
    } finally {
      DBHelper.close(rs)
      DBHelper.close(pstmt)
    }
  }

  def saveObject(sql: String, entity: Any, conn: Connection): Int = {
    executeSql(sql, entity, conn)
  }

  def saveList(sql: String, entities: List[Any], conn: Connection): Int = {
    entities.map(saveObject(sql, _, conn)).sum
  }

  def saveListBatch(sql: String, entities: List[Any], conn: Connection): Int = {
    DBHelper.setAutoCommit(conn, false)
    var pstmt: PreparedStatement = null
    try {
      pstmt = conn.prepareStatement(getRealSql(sql))
      var count: Int = 0
      for (entity: Any <- entities) {
        setPreparedSql(sql, pstmt, entity)
        pstmt.addBatch()
        count += 1
        DBHelper.executeBatch(conn, pstmt, count)
      }
      pstmt.executeBatch()
      DBHelper.commit(conn)
      count
    } finally {
      DBHelper.close(pstmt)
      DBHelper.setAutoCommit(conn, true)
    }
  }

  def deleteObject(sql: String, entity: Any, conn: Connection): Int = {
    executeSql(sql, entity, conn)
  }

  def updateObject(sql: String, entity: Any, conn: Connection): Int = {
    executeSql(sql, entity, conn)
  }

  def updateList(sql: String, entities: List[Any], conn: Connection): Int = {
    entities.map(updateObject(sql, _, conn)).sum
  }

  def getRsListFromMetaData(rs: ResultSet, cls: Class[_]): ListBuffer[Any] = {
    val list: ListBuffer[Any] = ListBuffer[Any]()
    // MetaData
    val data: ResultSetMetaData = rs.getMetaData()
    val columnCount: Int = data.getColumnCount()

    while (rs.next()) {
      val clsInstance: Any = cls.newInstance()
      for (i <- 1 to columnCount) {
        // MetaData columnName
        val columnName: String = data.getColumnLabel(i) // data.getColumnName(i)
        // GetColumn return type
        val typeCls: Class[_] = cls.getMethod("get" + toFirstUpperCase(columnName)).getReturnType()
        // SetColumn
        val method: Method = cls.getMethod("set" + toFirstUpperCase(columnName), typeCls)
        // Integer String Float
        var simpleName: String = toFirstUpperCase(typeCls.getSimpleName())
        // 对Integer类型的特殊处理
        if (simpleName.equalsIgnoreCase("Integer")) {
          simpleName = "Int"
        }
        // Rs getString getInt getFloat
        val rsMethod: Method = rs.getClass().getMethod("get" + simpleName, classOf[String])
        // Rs getString getInt getFloat invoke
        val value: Object = rsMethod.invoke(rs, columnName)
        // SetColumn invoke
        method.invoke(clsInstance, value)
      }
      list.append(clsInstance)
    }
    list
  }

  def setPreparedSql(sql: String, pstmt: PreparedStatement, entity: Any): Unit = {
    if (sql.contains("#")) {
      val cls: Class[_] = entity.getClass()
      val sqlSplit: Array[String] = sql.split("#")
      for (i <- 1 until sqlSplit.length) {
        val split: String = sqlSplit(i)
        val splitbefore: String = StringUtils.substringBeforeLast(split, "}").trim()
        val paramName: String = StringUtils.substringAfterLast(splitbefore, "{").trim()

        // getXxxx
        val paramMethod: Method = cls.getMethod("get" + toFirstUpperCase(paramName))
        // getXxxx invoke
        val paramValue: Any = paramMethod.invoke(entity)
        pstmt.setString(i, paramValue.toString())
      }
    }
  }

  def getRealSql(sql: String): String = {
    sql.replaceAll("\\#\\{ *[a-z,A-Z,0-9,_]+ *\\}", "\\?")
  }

  def executeSql(sql: String, entity: Any, conn: Connection): Int = {
    var pstmt: PreparedStatement = null
    try {
      pstmt = conn.prepareStatement(getRealSql(sql))
      setPreparedSql(sql, pstmt, entity)
      pstmt.executeUpdate()
    } finally {
      DBHelper.close(pstmt)
    }
  }

  /**
   * CRUD by sql
   * @param sql
   * @param conn
   */
  def executeSql(sql: String, conn: Connection): Int = {
    var pstmt: PreparedStatement = null
    try {
      pstmt = conn.prepareStatement(sql)
      pstmt.executeUpdate()
    } finally {
      DBHelper.close(pstmt)
    }
  }

  def toFirstUpperCase(str: String): String = {
    if (str == null || str.length() < 1) {
      return ""
    }
    val start: String = str.substring(0, 1).toUpperCase()
    val end: String = str.substring(1, str.length())
    start + end
  }
}