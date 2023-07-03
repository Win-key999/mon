package com.pennant.prodmtr.model.Input;

import com.pennant.prodmtr.model.Entity.Sprint;
import com.pennant.prodmtr.model.Entity.SprintTasks;
import com.pennant.prodmtr.model.Entity.SprintTasksId;
import com.pennant.prodmtr.model.Entity.Task;
import com.pennant.prodmtr.model.Entity.User;

public class SprintTasksInput {
	@Override
	public String toString() {
		return "SprintTasksInput [sprintId=" + sprintId + ", taskId=" + taskId + ", userId=" + userId + "]";
	}

	private Integer sprintId;
	private Integer taskId;
	private Integer userId;

	public Integer getSprintId() {
		return sprintId;
	}

	public void setSprintId(int sprintId) {
		this.sprintId = sprintId;
	}

	public Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(int user) {
		this.userId = user;
	}

	public SprintTasks toEntity() {
		SprintTasks st = new SprintTasks();
		SprintTasksId sti = new SprintTasksId();
		Sprint s = new Sprint();
		s.setSprintId(sprintId);
		Task t = new Task();
		t.setTaskId(taskId);
		sti.setTaskId(t);
		sti.setSprnId(s);
		st.setId(sti);
		User u = new User();
		u.setUserId(this.userId);
		st.setUser(u);
		System.out.println(u + "-----in Entity");
		return st;
	}
	// Additional properties, constructors, and other methods
}