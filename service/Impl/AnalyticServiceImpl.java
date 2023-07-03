package com.pennant.prodmtr.service.Impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.pennant.prodmtr.Dao.Interface.AnalyticsDao;
import com.pennant.prodmtr.model.Dto.AnalyticsDto;
import com.pennant.prodmtr.model.Entity.Module;
import com.pennant.prodmtr.model.Entity.ProjectTask;
import com.pennant.prodmtr.model.Entity.Subtask;
import com.pennant.prodmtr.model.Entity.Task;
import com.pennant.prodmtr.model.view.IndvTaskSummary;
import com.pennant.prodmtr.model.view.ModuleSummary;
import com.pennant.prodmtr.model.view.ProjectSummary;
import com.pennant.prodmtr.model.view.SubtaskSummary;
import com.pennant.prodmtr.model.view.TaskSummary;
import com.pennant.prodmtr.service.Interface.AnalyticService;

public class AnalyticServiceImpl implements AnalyticService {

	@Autowired
	private AnalyticsDao analyticsDao;

	// Constructor
	public AnalyticsDto getSummariesByUserId(int userId) {
		AnalyticsDto summaryOutput = new AnalyticsDto();

		/*
		 * List<ProjectSummary> projectSummaries = analyticsDao.getProjectSummariesByUserId(userId); List<ModuleSummary>
		 * moduleSummaries = analyticsDao.getModuleSummariesByUserId(userId); List<TaskSummary> taskSummaries =
		 * analyticsDao.getTaskSummariesByUserId(userId); List<SubtaskSummary> subtaskSummaries =
		 * analyticsDao.getSubtaskSummariesByUserId(userId); List<Past30CompletionSummary> past30CompletionSummaries =
		 * analyticsDao.getPast30CompletionSummaries(); summaryOutput.setProjectSummaries(projectSummaries);
		 * summaryOutput.setModuleSummaries(moduleSummaries); summaryOutput.setTaskSummaries(taskSummaries);
		 * summaryOutput.setSubtaskSummaries(subtaskSummaries);
		 */
		return summaryOutput;
	}

	@Override
	public AnalyticsDto getUserProjDetails(int userId) {
		AnalyticsDto a = new AnalyticsDto();
		// Create a HashMap to store project-wise hours
		Map<Short, Long> projectHoursMap = new HashMap<>();
		// Create a HashMap to store module-wise hours
		Map<Short, Long> moduleHoursMap = new HashMap<>();
		// Create a HashMap to store module-wise hours
		Map<Short, Long> taskHoursMap = new HashMap<>();
		List<ProjectTask> projectTask = analyticsDao.getAllProjectsWorkingHoursByUid(userId);
		List<Task> tasks = analyticsDao.getAllProjectsIndvTasksWorkingHoursByUid(userId);
		List<Subtask> subtasks = analyticsDao.getAllSubTasksByUid(userId);

		List<ProjectSummary> projectSummaries = new ArrayList<>();

		List<ModuleSummary> moduleWiseSummary = new ArrayList<>();
		// indvtaskssummary represents the data from table pm_tasks
		List<IndvTaskSummary> IndvTaskWiseSummary = new ArrayList<>();
		// taskssummary represents the data from table pm_projecttasks
		List<TaskSummary> taskWiseSummary = new ArrayList<>();
		List<SubtaskSummary> subtaskWiseSummary = new ArrayList<>();
		System.out.println(tasks);
		setProjectWiseHoursForProjectTasks(projectTask, projectHoursMap);
		setProjectWiseHoursForIndvTasks(tasks, projectHoursMap);
		setProjectNames(projectHoursMap, projectSummaries);
		setmoduleWiseHoursForTask(tasks, moduleWiseSummary, moduleHoursMap);
		setTaskWiseHours(tasks, taskWiseSummary, taskHoursMap);
		System.out.println("projectSummaries " + projectSummaries);
		System.out.println("moduleWiseSummary" + moduleWiseSummary);
		System.out.println("taskWiseSummary" + taskWiseSummary);
		a.setProjectSummaries(projectSummaries);
		a.setModuleSummaries(moduleWiseSummary);
		a.setTaskSummaries(taskWiseSummary);
		return a;
	}

	private void setTaskWiseHours(List<Task> tasks, List<TaskSummary> taskWiseSummary, Map<Short, Long> taskHoursMap) {
		// Iterate over the taskHoursMap
		for (Map.Entry<Short, Long> entry : taskHoursMap.entrySet()) {
			Short taskId = entry.getKey();
			Long taskHours = entry.getValue();

			// Get the Task object using the taskId
			Task task = analyticsDao.getTaskById(taskId); // Replace `getTaskById` with your actual method to retrieve
															// the Task object

			// Create a TaskSummary object and set the task ID, hours, and other details
			TaskSummary summary = new TaskSummary();
			summary.setTaskId(taskId);
			summary.setTotalWorkingHours(task.getActualHours());
			summary.setTaskName(task.getTaskName());
			// Append the TaskSummary object to the taskWiseSummary list
			taskWiseSummary.add(summary);
		}

	}

	private void setmoduleWiseHoursForTask(List<Task> tasks, List<ModuleSummary> moduleWiseSummary,
			Map<Short, Long> moduleHoursMap) {

		// Iterate over the tasks and calculate the hours spent on each module
		for (Task task : tasks) {
			Short module = task.getModule().getModuleId();
			long taskHours = task.getActualHours();
			// String modl_name = task.getModule().getModuleName();
			// Check if the module already exists in the HashMap
			if (moduleHoursMap.containsKey(module)) {
				// If it exists, add the taskHours to its existing value
				long existingHours = moduleHoursMap.get(module);
				long updatedHours = existingHours + taskHours;
				moduleHoursMap.put(module, updatedHours);
			} else {
				// If it doesn't exist, create a new entry in the HashMap
				moduleHoursMap.put(module, taskHours);
			}
		}

		// Iterate over the moduleHoursMap
		for (Map.Entry<Short, Long> entry : moduleHoursMap.entrySet()) {
			Short moduleId = entry.getKey();
			Long moduleHours = entry.getValue();

			// Get the Module object using the moduleId
			Module module = analyticsDao.getModlbyId(moduleId);
			// Module object

			// Create a ModuleSummary object and set the module ID, hours, and name
			ModuleSummary summary = new ModuleSummary();
			summary.setModlId(moduleId);
			summary.setTotalWorkingHours(moduleHours);
			summary.setModlName(module.getModuleName());

			// Append the ModuleSummary object to the moduleWiseSummary list
			moduleWiseSummary.add(summary);
		}

	}

	private void setProjectNames(Map<Short, Long> projectHoursMap, List<ProjectSummary> projectSummaries) {
		// Populate the ProjectSummary model with project data and names
		for (Map.Entry<Short, Long> entry : projectHoursMap.entrySet()) {
			short projectId = entry.getKey();
			long hours = entry.getValue();

			// Retrieve the project name using the projectId (You need to implement this logic)
			String projectName = getProjectNameById(projectId);

			ProjectSummary projectSummary = new ProjectSummary();
			projectSummary.setProjId(projectId);
			projectSummary.setProjName(projectName);
			projectSummary.setTotalWorkingHours(hours);

			projectSummaries.add(projectSummary);
		}
	}

	private void setProjectWiseHoursForIndvTasks(List<Task> tasks, Map<Short, Long> projectHoursMap) {
		// Iterate over the Tasks and calculate the hours spent on each project
		for (Task t : tasks) {
			short projectId = t.getProject().getProjectId();
			long taskHours = t.getActualHours();
			// Check if projectId already exists in the HashMap
			if (projectHoursMap.containsKey(projectId)) {
				// If it exists, add the taskHours to its existing value
				long existingHours = projectHoursMap.get(projectId);
				long updatedHours = existingHours + taskHours;
				projectHoursMap.put(projectId, updatedHours);
			} else {
				// If it doesn't exist, create a new entry in the HashMap
				projectHoursMap.put(projectId, taskHours);
			}
		}

	}

	private String getProjectNameById(short projectId) {
		String projname = analyticsDao.getProjectNameById(projectId);
		return projname;
	}

	private void setProjectWiseHoursForProjectTasks(List<ProjectTask> projectTask, Map<Short, Long> projectHoursMap) {
		// Iterate over the ProjectTasks and calculate the hours spent on each project
		for (ProjectTask task : projectTask) {
			short projectId = task.getproject().getProjectId();
			long hours = task.getActualHours();
			// Add the hours to the existing total for the project
			long totalHours = projectHoursMap.getOrDefault(projectId, 0L) + hours;
			projectHoursMap.put(projectId, totalHours);
		}
	}
}
