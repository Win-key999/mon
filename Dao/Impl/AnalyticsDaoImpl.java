package com.pennant.prodmtr.Dao.Impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pennant.prodmtr.Dao.Interface.AnalyticsDao;
import com.pennant.prodmtr.model.Dto.ProjectDto;
import com.pennant.prodmtr.model.Entity.Module;
import com.pennant.prodmtr.model.Entity.Project;
import com.pennant.prodmtr.model.Entity.ProjectTask;
import com.pennant.prodmtr.model.Entity.Subtask;
import com.pennant.prodmtr.model.Entity.Task;
import com.pennant.prodmtr.model.view.ProjectSummary;
import com.pennant.prodmtr.service.Interface.TaskService;

@Component
public class AnalyticsDaoImpl implements AnalyticsDao {

	@PersistenceContext
	EntityManager entityManager;
	TaskService taskService;

	@Autowired
	public AnalyticsDaoImpl(TaskService taskService) {
		this.taskService = taskService;
	}

	@Override
	public ProjectSummary getProjectSummaryByUserId(int userId) {
		String hql = "SELECT new com.pennant.prodmtr.model.dto.ProjectSummary(p.projId, p.projName, ABS(SUM(EXTRACT(EPOCH FROM (t.taskCmpDatetime - t.taskCdatetime))) / 3600)) "
				+ "FROM Project p JOIN p.tasks t " + "WHERE t.assignedUser.userId = :userId "
				+ "GROUP BY p.projId, p.projName";

		Query query = entityManager.createQuery(hql);
		query.setParameter("userId", userId);

		return (ProjectSummary) query.getSingleResult();
	}

	@Override
	public List<ProjectDto> getAllProjects() {
		TypedQuery<Project> query = entityManager.createQuery("SELECT pt FROM Project pt", Project.class);
		List<Project> projects = query.getResultList();
		List<ProjectDto> convertedProjects = projects.stream().map(ProjectDto::fromEntity).collect(Collectors.toList());
		return convertedProjects;
	}

	@Override
	public List<ProjectTask> getAllProjectsWorkingHoursByUid(int userId) {
		TypedQuery<ProjectTask> query = entityManager.createQuery(
				"select distinct pt from ProjectTask pt  where assignedUser.userId = :userId and taskStatus = 'COMP'",
				ProjectTask.class);
		query.setParameter("userId", userId);
		List<ProjectTask> projects = query.getResultList();
		return projects;
	}

	@Override
	public List<Task> getAllProjectsIndvTasksWorkingHoursByUid(int userId) {

		TypedQuery<Task> query = entityManager.createQuery(
				"select distinct t from Task t where taskSupervisor.userId = :userId and taskStatus = 'COMP'",
				Task.class);
		query.setParameter("userId", userId);
		List<Task> tasks = query.getResultList();
		return tasks;
	}

	@Override
	public List<Subtask> getAllSubTasksByUid(int userId) {
		TypedQuery<Subtask> query = entityManager.createQuery(
				"select st from Subtask st where st.primaryKey.taskId in (select t.taskId from Task t where t.taskSupervisor.userId = :userId)",
				Subtask.class);
		query.setParameter("userId", userId);
		List<Subtask> subtasks = query.getResultList();

		return subtasks;
	}

	@Override
	public Task getTaskById(Short taskId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Module getModlbyId(Short moduleId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getProjectNameById(short projectId) {
		// TODO Auto-generated method stub
		return null;
	}

}
