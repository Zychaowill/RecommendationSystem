package com.jangz.recommendation.dao

import com.jangz.recommendation.basic.BasicSimpleDao
import com.jangz.recommendation.entity.Training

object TrainingDao {

  def saveGenderList(list: List[Training]): Int = {
    val sql: String = "insert into mllib_gender_data(genderid,`day`,pv,uv,ip) values (#{genderId},#{day},#{pv},#{uv},#{ip}) on duplicate key update pv = values(pv),uv = values(uv),ip = values(ip)"
    BasicSimpleDao.saveListBatch(sql, list)
  }

  def saveChannelList(list: List[Training]): Int = {
    val sql: String = "insert into mllib_channel_data(channelid,`day`,pv,uv,ip) values (#{channelId},#{day},#{pv},#{uv},#{ip}) on duplicate key update pv = values(pv),uv = values(uv),ip = values(ip)"
    BasicSimpleDao.saveListBatch(sql, list)
  }
}