package com.jangz.recommendation.util.db

import java.sql.Connection
import java.sql.ResultSet
import java.sql.PreparedStatement

object DBHelper {

  def getConnection(): Connection = {
    C3p0Helper.getConnection
  }

  def commit(conn: Connection): Unit = {
    if (conn != null) {
      conn.commit()
      println("do commit...")
    }
  }

  def setAutoCommit(conn: Connection, autoCommit: Boolean): Unit = {
    if (conn != null) {
      conn.setAutoCommit(autoCommit)
    }
  }

  def executeBatch(conn: Connection, pstmt: PreparedStatement, count: Int): Unit = {
    if (count % 1000 == 0) {
      pstmt.executeBatch()
      DBHelper.commit(conn)
    }
  }

  def close(conn: Connection, pstmt: PreparedStatement, rs: ResultSet): Unit = {
    close(rs)
    close(pstmt)
    close(conn)
  }

  def close(conn: Connection): Unit = {
    if (conn != null) {
      conn.close()
    }
  }

  def close(pstmt: PreparedStatement): Unit = {
    if (pstmt != null) {
      pstmt.close()
    }
  }

  def close(rs: ResultSet): Unit = {
    if (rs != null) {
      rs.close()
    }
  }
}