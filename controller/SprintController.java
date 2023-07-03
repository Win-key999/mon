package com.pennant.prodmtr.controller;

import java.text.ParseException;
import java.util.ArrayList;
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
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.pennant.prodmtr.model.Dto.FunctionalUnitdto;
import com.pennant.prodmtr.model.Dto.ModuleDTO;
import com.pennant.prodmtr.model.Dto.ProjectDto;
import com.pennant.prodmtr.model.Dto.TaskDto;
import com.pennant.prodmtr.model.Dto.UserDto;
import com.pennant.prodmtr.model.Entity.FunctionalUnit;
import com.pennant.prodmtr.model.Entity.Sprint;
import com.pennant.prodmtr.model.Entity.SprintResource;
import com.pennant.prodmtr.model.Entity.SprintTasks;
import com.pennant.prodmtr.model.Entity.Task;
import com.pennant.prodmtr.model.Input.SprintInput;
import com.pennant.prodmtr.model.Input.SprintResourceInput;
import com.pennant.prodmtr.model.Input.SprintTasksInput;
import com.pennant.prodmtr.model.Input.TaskInput;
import com.pennant.prodmtr.model.view.FunctionalTask;
import com.pennant.prodmtr.service.Interface.ModuleService;
import com.pennant.prodmtr.service.Interface.ProjectService;
import com.pennant.prodmtr.service.Interface.ResourceService;
import com.pennant.prodmtr.service.Interface.SprintService;
import com.pennant.prodmtr.service.Interface.TaskService;

@Controller
public class SprintController {

	SprintService sprintService;
	ProjectService projectService;
	ModuleService moduleService;
	TaskService taskService;
	ResourceService resourceService;
	private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

	@Autowired
	public SprintController(SprintService sprintService, ProjectService projectService, ModuleService moduleService,
			TaskService taskService, ResourceService resourceService) {
		super();
		this.sprintService = sprintService;
		this.projectService = projectService;
		this.moduleService = moduleService;
		this.taskService = taskService;
		this.resourceService = resourceService;

	}

	@RequestMapping(value = "/ShowFunctionalUnits", method = RequestMethod.POST)
	public String createTask(@Validated SprintInput sprintInput,
			@ModelAttribute SprintResourceInput SprintResourceInput, Model model, HttpSession session)
			throws ParseException, IllegalArgumentException, TransactionRequiredException, EntityExistsException,
			PersistenceException {
		// Initialize logger

		// Store the sprint in the database
		Sprint s = sprintService.storeSprint(sprintInput.toEntity());

		// Convert and store the sprint resource in the database
		SprintResource sr = SprintResourceInput.toEntity();
		int sprintid = s.getSprintId();
		session.setAttribute("sprintid", sprintid);
		sr.setSprintId(s.getSprintId());
		sprintService.storeSprintResource(sr);

		// Retrieve functional units for the given module and project
		List<FunctionalUnit> flist = sprintService.getFunctionalUnitsByModId(sprintInput.getModuleId(),
				sprintInput.getProjectId());
		List<FunctionalUnitdto> funlistDto = new ArrayList<>();

		// Log the list of functional units before processing
		logger.info("List of functional units before processing: {}", flist);

		// Process the list and populate the DTO list
		for (FunctionalUnit functionalUnit : flist) {
			FunctionalUnitdto funUnitDto = FunctionalUnitdto.fromEntity(functionalUnit);
			funlistDto.add(funUnitDto);
		}

		// Log the populated DTO list of functional units
		logger.info("List of functional units after processing: {}", funlistDto);

		// Add the DTO list and project ID to the model for rendering in the view
		model.addAttribute("funlist", funlistDto);
		model.addAttribute("pro_id", sprintInput.getProjectId());

		// Return the name of the view to be rendered
		return "ShowFunctionalUnits";
	}

	@RequestMapping(value = "/ShowFunUnits", method = RequestMethod.GET)
	public String getFunctionalUnitIntoSprint(@RequestParam("modlid") int modlid, @RequestParam("projid") int projid,
			Model model) {

		// Log the module ID and project ID
		logger.info("Received request to get functional units for module ID {} and project ID {}", modlid, projid);

		// Retrieve functional units for the given module ID and project ID
		List<FunctionalUnit> flist = sprintService.getFunctionalUnitsByModId(modlid, projid);

		// Log the retrieved functional units
		logger.info("Retrieved functional units: {}", flist);

		// Convert functional units to DTOs
		List<FunctionalUnitdto> funlistDto = new ArrayList<>();
		for (FunctionalUnit functionalUnit : flist) {
			FunctionalUnitdto funUnitDto = FunctionalUnitdto.fromEntity(functionalUnit);
			funlistDto.add(funUnitDto);
		}

		// Add the DTO list to the model for rendering in the view
		model.addAttribute("funlist", funlistDto);

		// Return the name of the view to be rendered
		return "ShowFunctionalUnits";
	}

	@RequestMapping(value = "/sprint", method = RequestMethod.GET)
	public String sprint(Model model) throws IllegalArgumentException, TransactionRequiredException,
			QueryTimeoutException, PersistenceException, NoResultException, NonUniqueResultException {

		// Log the retrieval of all sprints
		logger.info("Retrieving all sprints");

		// Retrieve all sprints from the service
		List<Sprint> allSprints = sprintService.getAllSprints();

		// Add the list of sprints to the model for rendering in the view
		model.addAttribute("allSprints", allSprints);

		// Log the successful retrieval of all sprints
		logger.info("Retrieved {} sprints", allSprints.size());

		// Return the name of the view to be rendered
		return "sprint_home";
	}

	@RequestMapping(value = "/sprint_details", method = RequestMethod.GET)
	public String getSprintDetails(Model model, @RequestParam int sprintId, HttpSession session)
			throws IllegalArgumentException, TransactionRequiredException, QueryTimeoutException, PersistenceException,
			NoResultException, NonUniqueResultException {

		// Log the retrieval of sprint details for the given sprint ID
		logger.info("Retrieving sprint details for sprint ID: {}", sprintId);

		// Retrieve the sprint details for the given sprint ID
		Sprint sprint = sprintService.getSprintDetails(sprintId);

		// Add the sprint details to the model for rendering in the view
		model.addAttribute("sprint", sprint);

		// Create a new sprint object to retrieve its sprint ID
		Sprint s = new Sprint();
		s.setSprintId(sprintId);
		int sprintid = s.getSprintId();
		session.setAttribute("sprintid", sprintid);

		// Retrieve all tasks by the sprint ID
		List<SprintTasks> tasksByIdSprints = sprintService.getAllTasksBySprintId(s);

		// Add the tasks to the model for rendering in the view
		model.addAttribute("tasksByIdSprints", tasksByIdSprints);

		// Log the successful retrieval of sprint details and tasks
		logger.info("Retrieved sprint details for sprint ID: {}. Retrieved {} tasks.", sprintId,
				tasksByIdSprints.size());

		// Return the name of the view to be rendered
		return "sprint_details";
	}

	@RequestMapping(value = "/add_sprint", method = RequestMethod.GET)
	public String addSprint(Model model) throws PersistenceException {

		// Log the retrieval of all projects
		logger.info("Retrieving all projects");

		// Retrieve all projects from the service
		List<ProjectDto> pl = projectService.getAllProjects();

		// Add the list of projects to the model for rendering in the view
		model.addAttribute("projects", pl);

		// Log the successful retrieval of projects
		logger.info("Retrieved {} projects", pl.size());

		// Log the retrieval of all resources
		logger.info("Retrieving all resources");

		// Retrieve all resources from the service
		List<UserDto> lu = resourceService.getAllResources();

		// Add the list of resources to the model for rendering in the view
		model.addAttribute("users", lu);

		// Log the successful retrieval of resources
		logger.info("Retrieved {} resources", lu.size());

		// Print the list of resources (for debugging purposes)
		System.out.println("lu" + lu);

		// Return the name of the view to be rendered
		return "add_sprint";
	}

	@RequestMapping(value = "/FunctionalUnit", method = RequestMethod.GET)
	public String addSprint() {

		// Log the entry into the addSprint method
		logger.info("Entering addSprint method");

		// Return the name of the view to be rendered
		return "FunctionalUnit";
	}

	@RequestMapping(value = "/SubTaskdetails", method = RequestMethod.GET)
	public String SubtaskDetails() {
		// System.out.println("Subtask Details requested");
		return "SubtaskDetails";
	}

	@RequestMapping(value = "/CreateSubTask", method = RequestMethod.GET)
	public String CreateSubtask() {

		// Log the entry into the CreateSubtask method
		logger.info("Entering CreateSubtask method");

		// Return the name of the view to be rendered
		return "CreateSubtask";
	}

	@RequestMapping(value = "/backlogs", method = RequestMethod.GET)
	public String pastdue(Model model)
			throws IllegalArgumentException, EntityNotFoundException, TransactionRequiredException,
			QueryTimeoutException, PessimisticLockException, LockTimeoutException, PersistenceException {

		// Log the entry into the pastdue method
		logger.info("Entering pastdue method");

		// Retrieve the list of backlogs from the service
		ArrayList<Sprint> SprintList = (ArrayList<Sprint>) sprintService.getBacklogs();

		// Add the list of backlogs to the model for rendering in the view
		model.addAttribute("sprintList", SprintList);

		// Log the successful retrieval of backlogs
		logger.info("Retrieved {} backlogs", SprintList.size());

		// Log an informational message about the completion of the pastdue method
		logger.info("pastdue method execution completed successfully");

		// Return the name of the view to be rendered
		return "backlog";
	}

	@RequestMapping(value = "/BacklogTasks", method = RequestMethod.GET)
	public String getBacklogTasks(Model model, @RequestParam("sprnModlId") int sprnModlId,
			@RequestParam("sprnId") int sprnId)
			throws IllegalArgumentException, EntityNotFoundException, TransactionRequiredException,
			QueryTimeoutException, PersistenceException, NoResultException, NonUniqueResultException {

		// Log the entry into the getBacklogTasks method
		logger.info("Entering getBacklogTasks method");

		// Retrieve the sprint details using the provided sprint ID
		Sprint sprint = sprintService.getSprintDetails(sprnId);

		// Retrieve the list of tasks associated with the specified sprint module ID
		List<Task> taskList = sprintService.getTasks(sprnModlId);

		// Add the sprint and task lists to the model for rendering in the view
		model.addAttribute("sprint", sprint);
		model.addAttribute("taskList", taskList);

		// Log the successful retrieval of sprint details and tasks
		logger.info("Retrieved sprint details for sprint ID: {}", sprnId);
		logger.info("Retrieved {} tasks for sprint module ID: {}", taskList.size(), sprnModlId);

		// Return the name of the view to be rendered
		return "BacklogTasks";
	}

	@ResponseBody
	@RequestMapping(value = "/getModuleById", method = RequestMethod.POST, produces = "application/json")
	public String getModuleById(@RequestParam("projectId") int projectId)
			throws IllegalArgumentException, QueryTimeoutException, PersistenceException, NoResultException {

		// Log the entry into the getModuleById method
		logger.debug("Entering getModuleById method");

		// Retrieve the list of modules for the given project ID
		List<ModuleDTO> moduleList = sprintService.getSprintModulesByProjectId(projectId);

		// Log the first module name in the module list
		logger.debug("First module name in the module list: {}", moduleList.get(0).getModl_name());

		// Convert the module list to JSON using Gson
		Gson gson = new Gson();
		String json = gson.toJson(moduleList);

		// Log the JSON data format
		logger.debug("Data in JSON format: {}", json);

		// Return the JSON response
		return json;
	}

	@RequestMapping(value = "/Task", method = RequestMethod.POST)
	public String createTask(@ModelAttribute FunctionalTask ft, Model model) {

		// Log the entry into the createTask method
		logger.debug("Entering createTask method");

		// Add the FunctionalTask object to the model
		model.addAttribute("funtask", ft);

		// Retrieve all resources and add them to the model
		List<UserDto> lu = resourceService.getAllResources();
		model.addAttribute("users", lu);

		// Retrieve all tasks and add them to the model
		List<TaskDto> tasks = taskService.getAllTasks();
		model.addAttribute("tasks", tasks);

		// Log the successful retrieval of resources and tasks
		logger.debug("Retrieved {} resources", lu.size());
		logger.debug("Retrieved {} tasks", tasks.size());

		// Return the name of the view to be rendered
		return "Task";
	}

	@RequestMapping(value = "/TaskAdded", method = RequestMethod.POST)
	public String TaskAdded(@ModelAttribute TaskInput taskInput, SprintTasksInput sprintTasksInput, Model model,
			HttpSession session)
			throws IllegalArgumentException, TransactionRequiredException, PersistenceException, QueryTimeoutException,
			LockTimeoutException, PessimisticLockException, OptimisticLockException, NoResultException {
		// Log the entry into the TaskAdded method
		logger.debug("Entering TaskAdded method");

		// Print error message and task name (for debugging purposes)
		logger.error("Error in the TaskAdded controller");
		logger.debug("Task name: {}", taskInput.toEntity().getTaskName());

		// Retrieve the sprint ID from the session
		int sprintid = (int) session.getAttribute("sprintid");

		// Store the task entity and retrieve the generated task ID
		Task t = sprintService.storeTask(taskInput.toEntity());

		// Set the sprint ID, task ID, and user ID for sprint tasks input
		sprintTasksInput.setSprintId(sprintid);
		sprintTasksInput.setTaskId(t.getTaskId());
		sprintTasksInput.setUserId(t.getTaskSupervisor().getUserId());

		// Convert sprint tasks input to entity and store it
		SprintTasks st = sprintTasksInput.toEntity();
		sprintService.updateFunctionalstatus(taskInput.getFunid());
		sprintService.storeSprintTasks(st);

		// Return the name of the view to be rendered
		return "TaskAdded";
	}

	@RequestMapping(value = "/sprintDetailsByProjId", method = RequestMethod.GET)
	public String getSprintDetailsByProjId(@RequestParam("projectId") int projectId, Model model)
			throws IllegalArgumentException, PersistenceException {

		// Log the entry into the getSprintDetailsByProjId method
		logger.debug("Entering getSprintDetailsByProjId method");

		// Retrieve the list of sprints by project ID
		List<Sprint> sprintsByProjId = sprintService.getSprintsByProjId(projectId);

		// Add the sprints to the model
		model.addAttribute("sprintsByProjId", sprintsByProjId);

		// Return the name of the view to be rendered
		return "sprintsByProjId";
	}
}
