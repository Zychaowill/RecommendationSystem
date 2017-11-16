package com.jangz.recommendation.util

import com.jangz.recommendation.config.Config

object AllAnalysis {
  
  def main(args: Array[String]): Unit = {
    val days = Array("2016-12-01", "2016-12-02", "2016-12-03", "2016-12-04", "2016-12-05", "2016-12-06", "2016-12-07")
    
    days.foreach(day => {
      Config.setDay(day)
      println(s"Run All Analysis for $day")
      
      
    })
  }
}