package baseObjects;

import additionalObjects.Specialization;
import additionalObjects.TaskState;

import java.time.LocalDateTime;

public class Task {
    private String _id;
    private String projectId;
    private LocalDateTime createdAt;
    private String createdBy;
    private TaskState taskState;
    private String name;
    private int estimation;
    private Specialization specialization;
    private String assignedTo;

    public Task(String _id, String projectId, LocalDateTime createdAt, String createdBy, String name, int estimation, Specialization specialization) {
        this._id = _id;
        this.projectId = projectId;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.taskState = TaskState.DEFAULT;
        this.name = name;
        this.estimation = estimation;
        this.specialization = specialization;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public TaskState getTaskState() {
        return taskState;
    }

    public void setTaskState(TaskState taskState) {
        this.taskState = taskState;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getEstimation() {
        return estimation;
    }

    public void setEstimation(int estimation) {
        this.estimation = estimation;
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }
}
