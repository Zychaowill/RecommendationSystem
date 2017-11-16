package com.jangz.recommendation.config

import org.apache.commons.configuration.PropertiesConfiguration
import org.apache.commons.configuration.ConfigurationException

object Config {

  /**
   * mysql
   */
  var driverClass: String = null
  var url: String = null
  var username: String = null
  var password: String = null
  var checkout_timeout: Int = 0

  /**
   * static parameters config
   */
  var day: String = null
  var output_path: String = "output/time"
  var newline: String = "\n"
  var backslash: String = "/"
  var default_ses_time: Long = 1800
  var reduce_result_filename: String = "part-r-0000"

  /**
   * Kafka parameter
   */
  var topic: String = null
  var zkHosts: String = null
  var brokerList: String = null

  def setDay(dayStr: String): Unit = {
    this.day = dayStr
  }

  def input_path: String = {
    assert(day != null, "should set day before run")
    "data/logs/recommendation" + day.replace("-", "") + ".log"
  }

  private def loadConfig(config: PropertiesConfiguration) {
    if (config.containsKey("driverClass")) {
      driverClass = config.getString("driverClass")
    }

    if (config.containsKey("url")) {
      url = config.getString("url")
    }

    if (config.containsKey("username")) {
      username = config.getString("username")
    }

    if (config.containsKey("password")) {
      password = config.getString("password")
    }

    if (config.containsKey("checkout_timeout")) {
      checkout_timeout = config.getInt("checkout_timeout")
    }

    if (config.containsKey("topic")) {
      topic = config.getString("topic")
    }

    if (config.containsKey("zkHosts")) {
      zkHosts = config.getString("zkHosts")
    }

    if (config.containsKey("brokerList")) {
      brokerList = config.getString("brokerList")
    }
  }

  def main(args: Array[String]): Unit = {
//    setDay("201720162131321")
    println(Config.day)
    println(Config.input_path)
    println(Config.driverClass)
    println(Config.url)
    println(Config.username)
    println(Config.password)
    println(Config.checkout_timeout)
    println(Config.topic)
    println(Config.zkHosts)
    println(Config.brokerList)
  }
  
  var config: PropertiesConfiguration = null
  try {
    config = new PropertiesConfiguration("conf.properties")
  } catch {
    case e: ConfigurationException => {
      e.printStackTrace()
    }
  }
  loadConfig(config)
}