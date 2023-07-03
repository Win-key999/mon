package com.pennant.prodmtr.service.Interface;

import java.util.List;

import com.pennant.prodmtr.model.Entity.Subtask;
import com.pennant.prodmtr.model.Input.SubtaskInput;

public interface SubtaskService {

	public void saveSubtask(Subtask subtask);

	public void setNewSubTask(SubtaskInput subtaskInput);

	public List<Subtask> getUnapprovedSubtasks(int userId);

	Subtask findSubtask(String compositeId);

	void updateSubtaskApproval(SubtaskInput subtaskInput);
}
