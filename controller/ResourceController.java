package com.pennant.prodmtr.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.pennant.prodmtr.model.Dto.ProjectDto;
import com.pennant.prodmtr.model.Dto.ResTaskFilter;
import com.pennant.prodmtr.model.Dto.ResourceFilter;
import com.pennant.prodmtr.model.Dto.TaskDto;
import com.pennant.prodmtr.model.Dto.UserDto;
import com.pennant.prodmtr.model.Entity.Role;
import com.pennant.prodmtr.model.Entity.User;
import com.pennant.prodmtr.model.Input.UserInput;
import com.pennant.prodmtr.model.view.TaskCountview;
import com.pennant.prodmtr.service.Interface.ProjectService;
import com.pennant.prodmtr.service.Interface.ResourceService;
import com.pennant.prodmtr.service.Interface.RoleService;
import com.pennant.prodmtr.service.Interface.TaskService;

@Controller
public class ResourceController {
	private final ResourceService resourceService;
	private final ProjectService projectService;
	private final RoleService roleService;
	private final TaskService taskService;

	@Autowired
	private User user;

	@Autowired
	private Role role;

	@Autowired
	public ResourceController(ResourceService resourceService, ProjectService projectService, RoleService roleService,
			TaskService taskService) {
		this.resourceService = resourceService;
		this.projectService = projectService;
		this.roleService = roleService;
		this.taskService = taskService;
	}

	@RequestMapping(value = "/resources", method = RequestMethod.GET)
	public String getAllResources(Model model) {
		// Set the loading attribute to true

		// Fetch all resources asynchronously
		CompletableFuture<List<UserDto>> resourcesFuture = CompletableFuture
				.supplyAsync(() -> resourceService.getAllResources());
		CompletableFuture<List<ProjectDto>> projectsFuture = CompletableFuture
				.supplyAsync(() -> projectService.getAllProjects());
		CompletableFuture<List<Role>> rolesFuture = CompletableFuture.supplyAsync(() -> roleService.getAllRoles());

		// Wait for all futures to complete
		CompletableFuture.allOf(resourcesFuture, projectsFuture, rolesFuture).join();

		// Get the results
		List<UserDto> resources = resourcesFuture.join();
		List<ProjectDto> projects = projectsFuture.join();
		List<Role> roles = rolesFuture.join();

		// Perform calculations asynchronously for each resource
		List<CompletableFuture<Void>> calculationFutures = resources.stream()
				.map(resource -> CompletableFuture.runAsync(() -> {
					int completedTasks = taskService.getCompletedTasksByUserId(resource.getUserId());
					int totalTasks = taskService.getTotalTasksByUserId(resource.getUserId());
					double performanceScore = taskService.calculatePerformanceScore(completedTasks, totalTasks);
					double hoursWorked = taskService.getHoursWorkedByUserId(resource.getUserId());

					resource.setPerformanceScore(performanceScore);
					resource.setHoursWorked(hoursWorked);
					resource.setTasksCompleted(completedTasks);
				})).collect(Collectors.toList());

		// Wait for all calculation futures to complete
		CompletableFuture.allOf(calculationFutures.toArray(new CompletableFuture[0])).join();

		// Add resources, projects, and roles to the model
		model.addAttribute("resources", resources);
		model.addAttribute("projects", projects);
		model.addAttribute("roles", roles);

		// Return the view name
		return "ResourceHome";
	}

	@RequestMapping(value = "/resources/filter", method = RequestMethod.GET)
	@ResponseBody
	public String filterResources(@Validated ResourceFilter resourceFilter, BindingResult bindingResult) {
		// Validate the resource filter
		if (bindingResult.hasErrors()) {
			// Handle validation errors
			// Return appropriate error response
			return "Validation Error";
		}

		// Retrieve filtered resources based on the filter criteria
		List<UserDto> filteredResources = resourceService.filterResources(resourceFilter);

		// Convert the filtered resources to JSON using Gson
		Gson gson = new Gson();
		String json = gson.toJson(filteredResources);

		// Return the JSON response
		return json;
	}

	@RequestMapping(value = "/resources/details", method = RequestMethod.GET)
	public String getResourceDetails(@RequestParam(name = "displayName") String displayName, Model model) {

		System.out.println(displayName);
		User resource = resourceService.getResourceByDisplayName(displayName);
		model.addAttribute("resource", resource);

		System.out.println(resource.userEmployeeId);
		return "user_details";
	}

	@RequestMapping(value = "/resources/update", method = RequestMethod.GET)
	public String updateResource(@RequestParam("userId") int userId, Model model) {
		try {
			// Log the information
			// logger.info("Entered the updateResource method");

			// Retrieve the resource by user ID
			User resource = resourceService.getResourceByUserId(userId);
			List<Role> roles = roleService.getAllRoles();

			// Print the resource
			// logger.info("Resource: {}", resource);

			// Add the resource and roles to the model
			model.addAttribute("resource", resource);
			model.addAttribute("roles", roles);

			// Return the view name
			return "update_resource";
		} catch (Exception e) {
			// Log the exception
			// logger.error("An error occurred while updating resource", e);

			// Redirect to an error page
			return "errorPage";
		}
	}

	@RequestMapping(value = "/resources/updateSuccess", method = RequestMethod.POST)
	public String updateResourceSuccess(@Validated UserInput userInput) {
		try {
			// Log the information
			// logger.info("Entered the updateResourceSuccess method");

			// Retrieve the existing resource from the database using the original user ID
			User resource = resourceService.getResourceByUserId(userInput.getUserId());

			// Create a new Role object and set the user's role ID
			Role role = new Role();
			role.setRoleId(userInput.getUserRole());

			// Update the resource with the new role and status
			resource.setUserRole(role);
			resource.setUserStatus(userInput.getUserStatus());

			// Save the updated resource
			resourceService.save(resource);

			// Redirect to the resources page
			return "redirect:/resources";
		} catch (Exception e) {
			// Log the exception
			// logger.error("An error occurred while updating resource", e);
			//
			// Redirect to an error page
			return "errorPage";
		}
	}

	@RequestMapping(value = "/resources/AddResource", method = RequestMethod.GET)
	public String addResource(Model model) {
		// Add necessary logic
		List<Role> roles = (List<Role>) roleService.getAllRoles();
		model.addAttribute("roles", roles);
		return "AddResource";
	}

	@RequestMapping(value = "/resources/addSuccess", method = RequestMethod.POST)
	public String addResource(@Validated UserInput userinput, Model model) {
		System.out.println(userinput.getUserRole());
		userinput.setUserCreationDate(new Date()); // Set current date as the creation date
		userinput.setUserLastUpdatedDate(new Date());

		// Set last updated date as null
		// user.setUserRole(roleid);
		System.out.println(userinput);
		// System.out.println("role is " + roleid);
		resourceService.addUser(userinput);
		return "redirect:/resources";
	}

	@RequestMapping(value = "/resources/tasks", method = RequestMethod.GET)
	public String viewTasksForUser(@RequestParam("userId") int userId, Model model) {
		// Assuming you have a method in your service layer to retrieve tasks by user ID
		List<TaskDto> tasks = taskService.getTasksByUserId(userId);
		List<ProjectDto> projects = projectService.getAllProjects();

		// Group tasks by project ID and count the number of tasks per project
		Map<Integer, Integer> projectTaskCount = new HashMap<>();
		for (TaskDto task : tasks) {
			Integer projectId = task.getProjectId();
			int count = projectTaskCount.getOrDefault(projectId, 0);
			projectTaskCount.put(projectId, count + 1);
		}

		List<TaskCountview> taskCountList = new ArrayList<>();
		for (Map.Entry<Integer, Integer> entry : projectTaskCount.entrySet()) {
			int projectId = entry.getKey();
			int taskCount = entry.getValue();
			TaskCountview taskCountDto = new TaskCountview(projectId, taskCount);
			taskCountList.add(taskCountDto);
		}

		model.addAttribute("userId", userId);
		model.addAttribute("tasks", tasks);
		model.addAttribute("projects", projects);
		model.addAttribute("taskCountList", taskCountList);
		System.out.println("data " + taskCountList);

		return "TasksByName"; // Replace with the name of your JSP page to display tasks
	}

	@RequestMapping(value = "resources/tasks/filter", method = RequestMethod.GET)
	@ResponseBody
	public String filterTasks(@Validated ResTaskFilter ResTaskFilter, BindingResult bindingResult) {
		System.out.println("/tasks/filter is called ");
		System.out.println(ResTaskFilter.getStatus());
		System.out.println(ResTaskFilter.getProjectId());
		if (bindingResult.hasErrors()) {
			// Handle validation errors
			// Return appropriate error response
			return "Validation Error";
		}
		List<TaskDto> task = taskService.filterTasks(ResTaskFilter);
		Gson gson = new Gson();
		String json = gson.toJson(task);
		return json;
	}

	@RequestMapping(value = "/user_details", method = RequestMethod.GET)
	public String getResourceDetailsById(@RequestParam(name = "userId") int userId, Model model) {
		System.out.println(userId);
		User resource = resourceService.getResourceById(userId);
		model.addAttribute("resource", resource);
		return "user_details";
	}

}