package com.pennant.prodmtr.service.Impl;

import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.persistence.LockTimeoutException;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceException;
import javax.persistence.PessimisticLockException;
import javax.persistence.QueryTimeoutException;
import javax.persistence.TransactionRequiredException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pennant.prodmtr.Dao.Interface.SprintDao;
import com.pennant.prodmtr.model.Dto.ModuleDTO;
import com.pennant.prodmtr.model.Dto.UserDto;
import com.pennant.prodmtr.model.Entity.FunctionalUnit;
import com.pennant.prodmtr.model.Entity.Sprint;
import com.pennant.prodmtr.model.Entity.SprintResource;
import com.pennant.prodmtr.model.Entity.SprintTasks;
import com.pennant.prodmtr.model.Entity.Task;
import com.pennant.prodmtr.service.Interface.SprintService;

@Component
@Transactional
@Service
public class SprintServiceImpl implements SprintService {
	private final SprintDao sprintDao;
	private static final Logger LOGGER = LoggerFactory.getLogger(SprintServiceImpl.class);

	@Autowired
	public SprintServiceImpl(SprintDao sprintDao) {
		this.sprintDao = sprintDao;
	}

	// Retrieves the backlogs.
	@Override
	public List<Sprint> getBacklogs()
			throws IllegalArgumentException, EntityNotFoundException, TransactionRequiredException,
			QueryTimeoutException, PessimisticLockException, LockTimeoutException, PersistenceException {
		LOGGER.info("Fetching backlogs");
		return sprintDao.getBaskLogs();
	}

	// Retrieves the sprint details for a given sprintId.
	@Override
	public Sprint getSprintDetails(int sprintId)
			throws IllegalArgumentException, EntityNotFoundException, TransactionRequiredException,
			QueryTimeoutException, PessimisticLockException, LockTimeoutException, PersistenceException {
		LOGGER.info("Fetching sprint details for sprintId: {}", sprintId);
		return sprintDao.getSprintDetails(sprintId);
	}

	// Retrieves the tasks for a given modlId.
	@Override
	public List<Task> getTasks(int modlId)
			throws IllegalArgumentException, EntityNotFoundException, TransactionRequiredException,
			QueryTimeoutException, PersistenceException, NoResultException, NonUniqueResultException {
		LOGGER.info("Fetching tasks for modlId: {}", modlId);
		return sprintDao.getTasks(modlId);
	}

	// Retrieves all sprints.
	@Override
	public List<Sprint> getAllSprints() throws IllegalArgumentException, TransactionRequiredException,
			QueryTimeoutException, PersistenceException, NoResultException, NonUniqueResultException {
		LOGGER.info("Fetching all sprints");
		return sprintDao.getAllSprints();
	}

	// Retrieves all tasks for a given sprintId.
	@Override
	public List<SprintTasks> getAllTasksBySprintId(Sprint sprintId)
			throws IllegalArgumentException, TransactionRequiredException, QueryTimeoutException, PersistenceException,
			NoResultException, NonUniqueResultException {
		LOGGER.info("Fetching all tasks for sprintId: {}", sprintId);
		return sprintDao.getAllTasksBySprintId(sprintId);
	}

	// Stores a sprint.
	@Override
	public Sprint storeSprint(Sprint sprint)
			throws IllegalArgumentException, TransactionRequiredException, EntityExistsException, PersistenceException {
		LOGGER.info("Storing sprint: {}", sprint);
		return sprintDao.storeSprint(sprint);
	}

	// Retrieves the modules for a given projectId.
	@Override
	public List<ModuleDTO> getSprintModulesByProjectId(int projectId)
			throws IllegalArgumentException, QueryTimeoutException, PersistenceException, NoResultException {
		LOGGER.info("Fetching modules for projectId: {}", projectId);
		return sprintDao.getSprintModulesByProjectId(projectId);
	}

	// Retrieves the functional units for a given modlId and projid.
	@Override
	public List<FunctionalUnit> getFunctionalUnitsByModId(int modlId, int projid)
			throws IllegalArgumentException, QueryTimeoutException, PersistenceException {
		LOGGER.info("Fetching functional units for modlId: {} and projid: {}", modlId, projid);
		return sprintDao.getFunctionalUnitsByModId(modlId, projid);
	}

	// Stores a sprint resource.
	public void storeSprintResource(SprintResource src) throws PersistenceException {
		LOGGER.info("Storing sprint resource: {}", src);
		sprintDao.storeSprintResource(src);
	}

	// Stores a sprint task.
	public void storeSprintTasks(SprintTasks sprintTask) throws IllegalArgumentException, PersistenceException {
		LOGGER.info("Storing sprint task: {}", sprintTask);
		sprintDao.storeSprintTasks(sprintTask);
	}

	// Stores a task.
	@Override
	public Task storeTask(Task task)
			throws IllegalArgumentException, TransactionRequiredException, PersistenceException {
		LOGGER.info("Storing task: {}", task);
		return sprintDao.storeTask(task);
	}

	// Retrieves all resources.
	@Override
	public List<UserDto> getAllResources() throws PersistenceException {
		LOGGER.info("Fetching all resources");
		return sprintDao.getAllResources();
	}

	// Retrieves the sprints for a given projId.
	@Override
	public List<Sprint> getSprintsByProjId(int projId) throws IllegalArgumentException, PersistenceException {
		LOGGER.info("Fetching sprints for projId: {}", projId);
		return sprintDao.getSprintByProjId(projId);
	}

	// Updates the functional status for a given funit.
	public void updateFunctionalstatus(int funit)
			throws IllegalArgumentException, TransactionRequiredException, PersistenceException, QueryTimeoutException,
			LockTimeoutException, PessimisticLockException, OptimisticLockException, NoResultException {
		LOGGER.info("Updating functional status for funit: {}", funit);
		sprintDao.updateFunctionalstatus(funit);
	}
}
