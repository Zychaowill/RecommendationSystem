package com.jangz.recommendation.model;

public class Content {

	private Integer startSecond;
	private Integer endSecond;
	private Long contentId;
	private Integer dimeId;
	private String url;
	private String title;
	private String pv;
	private String uv;

	private Integer value;

	public Integer getStartSecond() {
		return startSecond;
	}

	public void setStartSecond(Integer startSecond) {
		this.startSecond = startSecond;
	}

	public Integer getEndSecond() {
		return endSecond;
	}

	public void setEndSecond(Integer endSecond) {
		this.endSecond = endSecond;
	}

	public Long getContentId() {
		return contentId;
	}

	public void setContentId(Long contentId) {
		this.contentId = contentId;
	}

	public Integer getDimeId() {
		return dimeId;
	}

	public void setDimeId(Integer dimeId) {
		this.dimeId = dimeId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPv() {
		return pv;
	}

	public void setPv(String pv) {
		this.pv = pv;
	}

	public String getUv() {
		return uv;
	}

	public void setUv(String uv) {
		this.uv = uv;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

}
