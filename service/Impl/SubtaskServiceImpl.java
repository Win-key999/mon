package com.pennant.prodmtr.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pennant.prodmtr.Dao.Interface.SubtaskDao;
import com.pennant.prodmtr.model.Entity.Subtask;
import com.pennant.prodmtr.model.Entity.SubtaskPrimaryKey;
import com.pennant.prodmtr.model.Input.SubtaskInput;
import com.pennant.prodmtr.service.Interface.SubtaskService;

@Service
@Transactional
public class SubtaskServiceImpl implements SubtaskService {

	@Autowired
	private SubtaskDao subtaskDao;

	public void saveSubtask(Subtask subtask) {
		subtaskDao.saveSubtask(subtask);
	}

	@Override
	public void setNewSubTask(SubtaskInput subtaskInput) {
		SubtaskPrimaryKey spk = new SubtaskPrimaryKey();

		spk.setTaskId(subtaskInput.getTaskId());
		spk.setSubtaskId((subtaskInput.getSubtaskId()));
		Subtask subtask = subtaskInput.toEntity();
		subtask.setPrimaryKey(spk);
		subtask.setApprStatus("NA");
		subtask.getPrimaryKey().getTaskId();

		subtaskDao.setNewSubTask(subtask);

	}

	@Override
	public Subtask findSubtask(String compositeId) {
		String[] ids = compositeId.split(",");
		int taskId = Integer.parseInt(ids[0]);
		int subtaskId = Integer.parseInt(ids[1]);
		// TODO Auto-generated method stub
		return subtaskDao.findSubtask(taskId, subtaskId);
	}

	@Override
	public List<Subtask> getUnapprovedSubtasks(int userId) {
		// TODO Auto-generated method stub
		return subtaskDao.getUnapprovedSubtasks(userId);
	}

	@Override
	public void updateSubtaskApproval(SubtaskInput subtaskInput) {
		subtaskDao.updateSubtaskApproval(subtaskInput);
	}
}
