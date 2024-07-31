# ITaskManager

## Description

This smart project task manager allows you to create and manage tasks within projects and facilitates intelligent assignment of tasks to team members based on their previous performance.

## Technologies
The project is built using Java, Spring Boot, Spring Data JPA, Spring Security and PostgreSQL. For testing Spring Boot Test, JUnit and Mockito are used.

## Features
### JWT authentication
jwt authentication is used to authenticate users. The user can register and login to the system. The user can also logout from the system.

### Project Management
The user can create a project, view all projects, view a project by id, update a project, delete a project.

### Task Management
The user can create a task, view all tasks, view a task by id, update a task, delete a task.

### Task Assignment
The user can assign a task to a team member or use the smart assignment, which will automatically assign tasks to team members based on their previous performance.

### Smart Assignment
The algorithm ranks each team member based on the number of tasks completed, the number of tasks failed, and the time taken for task completion. Tasks are then assigned to team members starting with those who have the highest ranks. If there are more tasks than team members, the algorithm prioritizes assigning more complex tasks to those with higher ranks.

## Installation
- Clone the repository
- Create a PostgreSQL database named `db_name`
- Update the `application.properties` file with your database username and password
- Run the project using the command `mvn spring-boot:run`
- Access the project at `http://localhost:8080`

## Ports
- The application runs on port 8080
- The database runs on port 5432








