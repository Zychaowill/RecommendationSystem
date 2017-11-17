package com.jangz.recommendation.spark.mllib

import org.apache.spark.mllib.classification.SVMModel
import org.apache.spark.mllib.feature.HashingTF

import com.jangz.recommendation.config.Config
import com.jangz.recommendation.entity.Training
import com.jangz.recommendation.util.FileUtil
import com.jangz.recommendation.util.SparkUtil
import com.jangz.recommendation.dao.TrainingDao

object GenderAnalysis {
  
  def runAnalysis(): Unit = {
    val sc = SparkUtil.getSparkContext(this.getClass)
    
    val lines = sc.textFile(Config.input_path)
    val filter = SparkUtil.getFilterLog(lines).cache()
    
    val model = SVMModel.load(sc, BaseModelUtil.modelPath("svm"))
    val tf = new HashingTF(numFeatures = 10000)
    
    /**
     * calculate gender information(map, reduce)
     */
    val map = filter.map(log => {
      val training: Training = new Training(pv = 1, uv = 1, ip = 1)
      training.genderId = model.predict(tf.transform(FileUtil.getTrainingString(log.getClearTitle()))).toInt
      training.uvs += log.Uuid
      training.ips += log.Ip
      (training.genderId, training)
    }).cache()
    
    /**
     * pv & uv & ip by general ReduceByKey
     */
    val reduce = map.reduceByKey((m, n) => {
      m.pv += n.pv
      m.uvs ++= n.uvs
      m.ips ++= n.ips
      m.uv = m.uvs.size
      m.ip = m.ips.size
      (m)
    })
    
    val list: List[Training] = reduce.values.collect().toList
    list.foreach(item => {
      item.day = Config.day
    })
    sc.stop
    
    /**
     * save into DB
     */
    TrainingDao.saveChannelList(list)
  }
  
  def main(args: Array[String]): Unit = {
    val dayStr = if (args.length > 0) args(0) else "2016-12-01"
    Config.setDay(dayStr)
    runAnalysis()    
  }
}