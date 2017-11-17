package com.jangz.recommendation.util

import com.jangz.recommendation.config.Config
import com.jangz.recommendation.spark.core.FlowAnalysis
import com.jangz.recommendation.spark.core.SearchAnalysis
import com.jangz.recommendation.spark.core.ProvinceAnalysis
import com.jangz.recommendation.spark.core.CountryAnalysis
import com.jangz.recommendation.spark.core.ContentAnalysis

object AllAnalysis {
  
  def main(args: Array[String]): Unit = {
    val days = Array("2016-12-01", "2016-12-02", "2016-12-03", "2016-12-04", "2016-12-05", "2016-12-06", "2016-12-07")
    
    days.foreach(day => {
      Config.setDay(day)
      println(s"Run All Analysis for $day")
      
      FlowAnalysis.runAnalysis()
      SearchAnalysis.runAnalysis()
      ProvinceAnalysis.runAnalysis()
      CountryAnalysis.runAnalysis()
      ContentAnalysis.runAnalysis()
      
      // need to train model before run the following analysis
      
      
    })
  }
}