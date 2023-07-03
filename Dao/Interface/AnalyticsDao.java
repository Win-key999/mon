package com.pennant.prodmtr.Dao.Interface;

import java.util.List;

import com.pennant.prodmtr.model.Dto.ProjectDto;
import com.pennant.prodmtr.model.Entity.Module;
import com.pennant.prodmtr.model.Entity.ProjectTask;
import com.pennant.prodmtr.model.Entity.Subtask;
import com.pennant.prodmtr.model.Entity.Task;
import com.pennant.prodmtr.model.view.ProjectSummary;

public interface AnalyticsDao {

	ProjectSummary getProjectSummaryByUserId(int userId);

	List<ProjectDto> getAllProjects();

	List<ProjectTask> getAllProjectsWorkingHoursByUid(int userId);

	List<Task> getAllProjectsIndvTasksWorkingHoursByUid(int userId);

	List<Subtask> getAllSubTasksByUid(int userId);

	Task getTaskById(Short taskId);

	Module getModlbyId(Short moduleId);

	String getProjectNameById(short projectId);

	/* List<Past30CompletionSummary> getPast30CompletionSummaries(); */

}
