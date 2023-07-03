package com.pennant.prodmtr.Dao.Impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.pennant.prodmtr.Dao.Interface.SubtaskDao;
import com.pennant.prodmtr.model.Entity.Subtask;
import com.pennant.prodmtr.model.Input.SubtaskInput;

@Repository
public class SubtaskDaoImpl implements SubtaskDao {

	@PersistenceContext
	private EntityManager entityManager;
	private Object subtaskId;

	public Subtask save(Subtask subtask) {
		System.out.println(subtask);
		entityManager.persist(subtask);
		return subtask;
	}

	public void saveSubtask(Subtask subtask) {
		entityManager.persist(subtask);
	}

	public void setNewSubTask(Subtask subtask) {
		entityManager.persist(subtask);
	}

	public Object getSubtaskId() {
		return subtaskId;
	}

	public void setSubtaskId(Object subtaskId) {
		this.subtaskId = subtaskId;
	}

	@Override
	public Subtask findSubtask(int taskId, int subtaskId) {
		TypedQuery<Subtask> query = entityManager.createQuery(
				"SELECT st FROM Subtask st where st.primaryKey.taskId = :taskId and st.primaryKey.subtaskId = :subtaskId",
				Subtask.class);
		query.setParameter("taskId", taskId);
		query.setParameter("subtaskId", subtaskId);
		Subtask subtask = query.getSingleResult();
		return subtask;
	}

	@Override
	public List<Subtask> findSubtaskByTaskId(int taskId) {

		TypedQuery<Subtask> query = entityManager
				.createQuery("SELECT st FROM Subtask st where st.primaryKey.taskId = :taskId", Subtask.class);
		query.setParameter("taskId", taskId);
		return query.getResultList();
	}

	@Override
	public List<Subtask> getUnapprovedSubtasks(int userId) {
		TypedQuery<Subtask> query = entityManager.createQuery(
				"SELECT st FROM Subtask st where st.apprStatus = 'NA' and st.primaryKey.taskId in (select t from Task t where t.taskSupervisor.userId = :userId ) ",
				Subtask.class);
		query.setParameter("userId", userId);
		return query.getResultList();
	}

	@Override
	public void updateSubtaskApproval(SubtaskInput subtaskInput) {
		// TODO Auto-generated method stub
		Query query = entityManager.createQuery(
				"update Subtask st set st.apprStatus = :status where st.primaryKey.taskId = :taskId and st.primaryKey.subtaskId  = :subtaskId");
		query.setParameter("status", subtaskInput.getApprStatus());
		query.setParameter("taskId", subtaskInput.getTaskId());
		query.setParameter("subtaskId", subtaskInput.getSubtaskId());
		query.executeUpdate();
	}
}
