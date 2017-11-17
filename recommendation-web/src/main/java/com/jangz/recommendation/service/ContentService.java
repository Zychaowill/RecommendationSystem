package com.jangz.recommendation.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jangz.recommendation.basic.BasicServiceSupportImpl;
import com.jangz.recommendation.dao.ContentDao;
import com.jangz.recommendation.model.Content;

@Service("contentService")
public class ContentService extends BasicServiceSupportImpl {

	@Resource
	protected ContentDao contentDao;
	
	/**
	 * Spark Streaming area manuscript
	 * @param content
	 * @return List<Content>
	 */
	@SuppressWarnings("unchecked")
	public List<Content> getStreamProvinceContentList(Content content) {
		return (List<Content>)contentDao.selectList("common.content.getStreamProvinceContentList", content);
	}
	
	/**
	 * Spark Core manuscript sort
	 * @param content
	 * @return List<Content> 查询结果
	 */
	@SuppressWarnings("unchecked")
	public List<Content> getMemoryContentList(Content content) {
		return (List<Content>)contentDao.selectList("common.content.getMemoryContentList", content);
	}
}
