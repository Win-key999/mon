package com.pennant.prodmtr.service.Interface;

import com.pennant.prodmtr.model.Dto.AnalyticsDto;

public interface AnalyticService {
	public AnalyticsDto getSummariesByUserId(int userId);

	public AnalyticsDto getUserProjDetails(int user_id);

	/* public List<Past30CompletionSummary> getPast30CompletionSummary(); */

}