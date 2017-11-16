package com.jangz.recommendation.util.db

import com.jangz.recommendation.config.Config
import java.sql.Connection
import java.sql.DriverManager

object JDBCHelper {

  def getConnection(): Connection = {
    Class.forName(Config.driverClass)
    DriverManager.getConnection(Config.url, Config.username, Config.password)
  }
  
  def main(args: Array[String]): Unit = {
    JDBCHelper.getConnection()
  }
}