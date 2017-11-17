package com.jangz.recommendation.spark.streaming

import com.jangz.recommendation.config.Config
import java.util.Properties
import kafka.producer.ProducerConfig
import kafka.producer.Producer
import com.jangz.recommendation.util.FileUtil
import kafka.producer.KeyedMessage

object KafkaLogsProducer {
  
  def main(args: Array[String]): Unit = {
    val topic = Config.topic
    val brokers = Config.brokerList
    val props = new Properties()
    props.put("metadata.broker.list", brokers)
    props.put("serializer.class", "kafka.serializer.StringEncoder")
    
    val kafkaConfig = new ProducerConfig(props)
    val producer = new Producer[String, String](kafkaConfig)
    
    (1 to 7).map(i => s"data/logs/recommendation206120$i.log").foreach(path => {
      println(s"read log from $path and replay to kafka $topic")
      FileUtil.readFileAsLines(path).foreach(line => {
        producer.send(new KeyedMessage[String, String](topic, line))
      })
    })
  }
}