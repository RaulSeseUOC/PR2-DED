package uoc.ds.pr.model;

import edu.uoc.ds.adt.sequential.LinkedList;
import edu.uoc.ds.adt.sequential.List;
import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.TaskStatus;

public class Task {

    private String taskId;
    private String description;
    private TaskStatus status;
    private Project project;
    private Operator operator;

    // Lista para la funcionalidad de operadores que rechazan
    private List<Operator> operatorsWhoRejected;

    public Task(String taskId, String description, Project project) {
        this.taskId = taskId;
        this.description = description;
        this.project = project;
        this.status = TaskStatus.REGISTERED; // Estado inicial
        this.operatorsWhoRejected = new LinkedList<>();
    }

    // Getters
    public String getTaskId() {
        return taskId;
    }

    public String getDescription() {
        return description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public Project getProject() {
        return project;
    }

    public Operator getOperator() {
        return operator;
    }

    // Setters (para cuando el estado cambia)
    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    // Métodos para gestionar rechazos
    public void addRejectingOperator(Operator op) {
        this.operatorsWhoRejected.insertEnd(op);
    }

    public int numOperatorsWhoRejected() {
        return this.operatorsWhoRejected.size();
    }
}