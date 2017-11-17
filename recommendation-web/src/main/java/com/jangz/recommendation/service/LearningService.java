package com.jangz.recommendation.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jangz.recommendation.basic.BasicServiceSupportImpl;
import com.jangz.recommendation.dao.LearningDao;
import com.jangz.recommendation.model.Learning;

@Service("learningService")
public class LearningService extends BasicServiceSupportImpl {

	@Resource
	protected LearningDao learningDao;

	/**
	 * Spark MLlib 性别分类
	 * 
	 * @param learning
	 * @return List<Learning>
	 */
	@SuppressWarnings("unchecked")
	public List<Learning> getGenderList(Learning learning) {
		return (List<Learning>) learningDao.selectList("common.learning.getGenderList", learning);
	}

	/**
	 * Spark MLlib 频道分类
	 * 
	 * @param learning
	 * @return List<Learning>
	 */
	@SuppressWarnings("unchecked")
	public List<Learning> getChannelList(Learning learning) {
		return (List<Learning>) learningDao.selectList("common.learning.getChannelList", learning);
	}
}
