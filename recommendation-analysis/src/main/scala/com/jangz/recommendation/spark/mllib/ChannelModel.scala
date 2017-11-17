package com.jangz.recommendation.spark.mllib

import com.jangz.recommendation.util.SparkUtil
import org.apache.hadoop.fs.Path
import org.apache.hadoop.fs.FileSystem
import scala.collection.mutable.ListBuffer
import com.jangz.recommendation.util.FileUtil
import com.jangz.recommendation.entity.Training

/**
 * Bias classification algorithm
 */
object ChannelModel {
  
  def main(args: Array[String]): Unit = {
    val sc = SparkUtil.getSparkContext(this.getClass)
    
    val modelPath = new Path(BaseModelUtil.modelPath("bayes"))
    val fs: FileSystem = FileSystem.get(modelPath.toUri, sc.hadoopConfiguration)
    if (fs.exists(modelPath)) {
      fs.delete(modelPath, true)
    }
    
    val list: ListBuffer[Training] = FileUtil.getTrainingList("")
  }
}