package com.jangz.recommendation.spark.mllib

object BaseModelUtil {
  
  private val DEFAULT_MODEL_PATH = "data/model"
  
  def modelPath(typ: String, path: String = DEFAULT_MODEL_PATH) = {
    path + "/" + typ
  }
  
  def main(args: Array[String]): Unit = {
    
  }
}