package com.jangz.recommendation.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jangz.recommendation.basic.BasicServiceSupportImpl;
import com.jangz.recommendation.dao.DimensionDao;
import com.jangz.recommendation.model.Dimension;

@Service("dimensionService")
public class DimensionService extends BasicServiceSupportImpl {

	@Resource
	protected DimensionDao dimensionDao;

	/**
	 * Spark Streaming 地区分布
	 * 
	 * @param dimension
	 * @return List<Dimension>
	 */
	@SuppressWarnings("unchecked")
	public List<Dimension> getStreamProvinceList(Dimension dimension) {
		return (List<Dimension>) dimensionDao.selectList("common.dimension.getStreamProvinceList", dimension);
	}

	/**
	 * Spark Core 流量统计
	 * 
	 * @param dimension
	 * @return List<Dimension>
	 */
	@SuppressWarnings("unchecked")
	public List<Dimension> getMemoryList(Dimension dimension) {
		return (List<Dimension>) dimensionDao.selectList("common.dimension.getMemoryList", dimension);
	}

	/**
	 * Spark Core 维度信息
	 * 
	 * @param dimension
	 * @return List<Dimension>
	 */
	@SuppressWarnings("unchecked")
	public List<Dimension> getMemoryDimensionList(Dimension dimension) {
		return (List<Dimension>) dimensionDao.selectList("common.dimension.getMemoryDimensionList", dimension);
	}
}
