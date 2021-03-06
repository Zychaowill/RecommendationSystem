package com.jangz.recommendation.spark.mllib

import com.jangz.recommendation.util.SparkUtil
import org.apache.hadoop.fs.Path
import org.apache.hadoop.fs.FileSystem
import scala.collection.mutable.ListBuffer
import com.jangz.recommendation.util.FileUtil
import com.jangz.recommendation.entity.Training
import org.apache.spark.mllib.feature.HashingTF
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.classification.NaiveBayes

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
    
    val list: ListBuffer[Training] = FileUtil.getTrainingList("data/ml/channel.txt")
    val arr = FileUtil.getTrainingArrayBuffer(list)
    
    val data = sc.parallelize(arr)
    val tf = new HashingTF(numFeatures = 10000)
    var parsedData = data.map(line => {
      var parts = line.split(',')
      LabeledPoint(parts(0).toDouble, tf.transform(parts(1).split(" ")))
    }).cache()
    
    val model = NaiveBayes.train(parsedData, lambda = 1.0, modelType = "multinomial")
    
    // save type
    model.save(sc, BaseModelUtil.modelPath("bayes"))
    
    sc.stop
  }
}