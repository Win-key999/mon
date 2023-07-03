package com.pennant.prodmtr.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.pennant.prodmtr.model.Dto.AnalyticsDto;
import com.pennant.prodmtr.model.view.Past30CompletionSummary;
import com.pennant.prodmtr.model.view.SubtaskSummary;
import com.pennant.prodmtr.service.Interface.AnalyticService;

@Controller
public class AnalyticsController {
	@Autowired
	AnalyticService analyticService;

	@RequestMapping(value = "/getDashboardAnalytics", method = RequestMethod.POST)
	public @ResponseBody String getDashboardAnalytics(Model model) throws SQLException {
		// List<Past30CompletionSummary> past30CompletionSummary = analyticService.getPast30CompletionSummary();
		Gson gson = new Gson();
		// String json = gson.toJson(past30CompletionSummary);

		// System.out.println("data" + json);
		return "i";
	}

	@RequestMapping(value = "/getUserAnalytics", method = RequestMethod.POST)
	public @ResponseBody String getProjAnalysisById(@RequestParam("user_id") Integer user_id) throws SQLException {
		// List<ProjectSummary> projectWiseSummary = analyticService.getUserProjDetails(user_id);
		AnalyticsDto a = analyticService.getUserProjDetails(user_id);

		// Create subtask summaries
		List<SubtaskSummary> subtaskSummaries = new ArrayList<>();

		SubtaskSummary subtaskSummary1 = new SubtaskSummary();
		subtaskSummary1.setSubtaskId(1);
		subtaskSummary1.setTotalWorkingHours(2.5);

		SubtaskSummary subtaskSummary2 = new SubtaskSummary();
		subtaskSummary2.setSubtaskId(2);
		subtaskSummary2.setTotalWorkingHours(3.75);

		subtaskSummaries.add(subtaskSummary1);
		subtaskSummaries.add(subtaskSummary2);

		a.setSubtaskSummaries(subtaskSummaries);

		// Create past 30 days completion summaries
		List<Past30CompletionSummary> past30CompletionSummaries = new ArrayList<>();

		Past30CompletionSummary past30CompletionSummary1 = new Past30CompletionSummary();
		past30CompletionSummary1.setCompletedDate("2023-06-01");
		past30CompletionSummary1.setTasksCompleted(10);
		past30CompletionSummary1.setSubtasksCompleted(25);

		Past30CompletionSummary past30CompletionSummary2 = new Past30CompletionSummary();
		past30CompletionSummary2.setCompletedDate("2023-06-02");
		past30CompletionSummary2.setTasksCompleted(15);
		past30CompletionSummary2.setSubtasksCompleted(30);

		past30CompletionSummaries.add(past30CompletionSummary1);
		past30CompletionSummaries.add(past30CompletionSummary2);

		a.setPast30CompletionSummary(past30CompletionSummaries);

		// adding dummy data
		// a = createDummyData();
		// a.setProjectSummaries(projectWiseSummary);
		Gson gson = new Gson();
		String json = gson.toJson(a);
		return json;
	}

}
