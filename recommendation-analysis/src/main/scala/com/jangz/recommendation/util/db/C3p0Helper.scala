package com.jangz.recommendation.util.db

import com.mchange.v2.c3p0.ComboPooledDataSource
import com.jangz.recommendation.config.Config
import java.sql.Connection

object C3p0Helper {
  private var cpds: ComboPooledDataSource = _

  def getConnection(): Connection = {
    if (cpds == null) {
      synchronized {
        if (cpds == null) {
          Class.forName(Config.driverClass)
          cpds = initCPDS(Config.url)
        }
      }
    }
    cpds.getConnection
  }

  private def initCPDS(url: String): ComboPooledDataSource = {
    // 批量提交
    var jdbcUrl = ""
    if (url.indexOf("?") < 0)
      jdbcUrl = url + "?rewriteBatchedStatements=true"
    else
      jdbcUrl = url + "&rewriteBatchedStatements=true"
    val thecpds: ComboPooledDataSource = new ComboPooledDataSource
    thecpds.setJdbcUrl(jdbcUrl)
    thecpds.setUser(Config.username)
    thecpds.setPassword(Config.password)
    thecpds.setCheckoutTimeout(Config.checkout_timeout)
    // the settings below are optional -- c3p0 can work with defaults
    thecpds.setMinPoolSize(1)
    thecpds.setInitialPoolSize(1)
    thecpds.setAcquireIncrement(1)
    thecpds.setMaxPoolSize(5)
    thecpds.setMaxIdleTime(1800)
    thecpds.setMaxStatements(0) // disable Statements cache to avoid deadlock
    thecpds.setPreferredTestQuery("select 1")
    thecpds.setTestConnectionOnCheckout(true)
    thecpds
  }
}