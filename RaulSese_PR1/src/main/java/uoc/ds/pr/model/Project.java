package uoc.ds.pr.model;

import edu.uoc.ds.adt.sequential.LinkedList;
import edu.uoc.ds.adt.sequential.List;

public class Project {

    private String projectId;
    private String name;
    private Coordinator coordinator;

    // Lista de tareas del proyecto
    private List<Task> tasks;

    public Project(String projectId, String name, Coordinator coordinator) {
        this.projectId = projectId;
        this.name = name;
        this.coordinator = coordinator;
        this.tasks = new LinkedList<>();
    }

    public void addTask(Task task) {
        this.tasks.insertEnd(task);
    }

    public int numTasks() {
        return this.tasks.size();
    }

    // Getters y Setters necesarios
    public String getProjectId() {
        return projectId;
    }

    public String getName() {
        return name;
    }

    public Coordinator getCoordinator() {
        return coordinator;
    }

    public void setCoordinator(Coordinator coordinator) {
        this.coordinator = coordinator;
    }

    public void setName(String name) {
        this.name = name;
    }
}