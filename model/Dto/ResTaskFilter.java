package com.pennant.prodmtr.model.Dto;

public class ResTaskFilter {
	private Short projectId;
	private String status;

	private Integer userId;

	public Short getProjectId() {
		return projectId;
	}

	public void setProjectId(Short projectId) {
		this.projectId = projectId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String statusFilter) {
		this.status = statusFilter;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
}