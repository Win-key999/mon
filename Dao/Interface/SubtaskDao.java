package com.pennant.prodmtr.Dao.Interface;

import java.util.List;

import com.pennant.prodmtr.model.Entity.Subtask;
import com.pennant.prodmtr.model.Input.SubtaskInput;

public interface SubtaskDao {
	Subtask save(Subtask subtask);

	void saveSubtask(Subtask subtask);

	void setNewSubTask(Subtask subtask);

	List<Subtask> findSubtaskByTaskId(int taskId);

	Subtask findSubtask(int taskId, int subtaskId);

	List<Subtask> getUnapprovedSubtasks(int userId);

	void updateSubtaskApproval(SubtaskInput subtaskInput);

}
