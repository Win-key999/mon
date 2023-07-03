package com.pennant.prodmtr.service.Interface;

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

import com.pennant.prodmtr.model.Dto.ModuleDTO;
import com.pennant.prodmtr.model.Dto.UserDto;
import com.pennant.prodmtr.model.Entity.FunctionalUnit;
import com.pennant.prodmtr.model.Entity.Sprint;
import com.pennant.prodmtr.model.Entity.SprintResource;
import com.pennant.prodmtr.model.Entity.SprintTasks;
import com.pennant.prodmtr.model.Entity.Task;

public interface SprintService {

	// Retrieves all backlogs
	List<Sprint> getBacklogs() throws IllegalArgumentException, EntityNotFoundException, TransactionRequiredException,
			QueryTimeoutException, PessimisticLockException, LockTimeoutException, PersistenceException;

	// Retrieves details of a specific sprint
	Sprint getSprintDetails(int sprintId)
			throws IllegalArgumentException, EntityNotFoundException, TransactionRequiredException,
			QueryTimeoutException, PessimisticLockException, LockTimeoutException, PersistenceException;

	// Retrieves tasks for a specific module
	List<Task> getTasks(int modlId)
			throws IllegalArgumentException, EntityNotFoundException, TransactionRequiredException,
			QueryTimeoutException, PersistenceException, NoResultException, NonUniqueResultException;

	// Retrieves all sprints
	List<Sprint> getAllSprints() throws IllegalArgumentException, TransactionRequiredException, QueryTimeoutException,
			PersistenceException, NoResultException, NonUniqueResultException;

	// Retrieves all tasks for a specific sprint
	List<SprintTasks> getAllTasksBySprintId(Sprint sprintId)
			throws IllegalArgumentException, TransactionRequiredException, QueryTimeoutException, PersistenceException,
			NoResultException, NonUniqueResultException;

	// Stores a new sprint or updates an existing sprint
	Sprint storeSprint(Sprint sprint)
			throws IllegalArgumentException, TransactionRequiredException, EntityExistsException, PersistenceException;

	// Retrieves modules for a specific project and sprint
	List<ModuleDTO> getSprintModulesByProjectId(int projectId)
			throws IllegalArgumentException, QueryTimeoutException, PersistenceException, NoResultException;

	// Retrieves functional units for a specific module and project
	List<FunctionalUnit> getFunctionalUnitsByModId(int modlId, int projid)
			throws IllegalArgumentException, QueryTimeoutException, PersistenceException;

	// Updates the functional status of a functional unit
	public void updateFunctionalstatus(int funit)
			throws IllegalArgumentException, TransactionRequiredException, PersistenceException, QueryTimeoutException,
			LockTimeoutException, PessimisticLockException, OptimisticLockException, NoResultException;

	// Stores a new task or updates an existing task
	public Task storeTask(Task task)
			throws IllegalArgumentException, TransactionRequiredException, PersistenceException;

	// Retrieves all resources
	public List<UserDto> getAllResources() throws PersistenceException;

	// Stores a new sprint resource
	public void storeSprintResource(SprintResource src) throws PersistenceException;

	// Stores a new sprint task
	public void storeSprintTasks(SprintTasks sprintTask) throws IllegalArgumentException, PersistenceException;

	// Retrieves sprints for a specific project
	List<Sprint> getSprintsByProjId(int projectId) throws IllegalArgumentException, PersistenceException;
}
