package uoc.ds.pr.model;

import edu.uoc.ds.adt.helpers.Position;
import edu.uoc.ds.adt.sequential.LinkedList;
import edu.uoc.ds.adt.sequential.List;
import edu.uoc.ds.adt.sequential.Queue;
import edu.uoc.ds.traversal.Iterator;
import edu.uoc.ds.traversal.Traversal;
import uoc.ds.pr.exceptions.TaskNotAssignedToOperatorException;
import uoc.ds.pr.util.QueueLinkedList;

public class Operator {

    private String operatorId;
    private String name;
    private String surname;
    private String address;

    // Colecciones de tareas del operador
    private List<Task> assignedTasks;
    private Queue<Task> acceptedTasks;
    private List<Task> completedTasks;
    private List<Task> rejectedTasks;

    public Operator(String operatorId, String name, String surname, String address) {
        this.operatorId = operatorId;
        this.name = name;
        this.surname = surname;
        this.address = address;

        // Inicializamos las colecciones
        this.assignedTasks = new LinkedList<>();
        this.acceptedTasks = new QueueLinkedList<>(); // Usamos nuestra implementación
        this.completedTasks = new LinkedList<>();
        this.rejectedTasks = new LinkedList<>();
    }

    // Métodos de gestión de tareas
    public void assignTask(Task task) {
        this.assignedTasks.insertEnd(task);
        task.setOperator(this);
    }

    public void acceptTask(Task task) throws TaskNotAssignedToOperatorException {
        // Para aceptar, primero debe estar asignada. La buscamos y la borramos.
        Task taskToRemove = removeTaskFromList(this.assignedTasks, task.getTaskId());
        if (taskToRemove == null) {
            throw new TaskNotAssignedToOperatorException();
        }
        this.acceptedTasks.add(task);
    }

    public Task completeTask() {
        // Sacamos la siguiente tarea de la cola FIFO
        Task task = this.acceptedTasks.poll();
        this.completedTasks.insertEnd(task);
        return task;
    }

    public void rejectTask(Task task) throws TaskNotAssignedToOperatorException {
        // Para rechazar, debe estar asignada. La buscamos y la borramos.
        Task taskToRemove = removeTaskFromList(this.assignedTasks, task.getTaskId());
        if (taskToRemove == null) {
            throw new TaskNotAssignedToOperatorException();
        }
        this.rejectedTasks.insertEnd(task);
        task.addRejectingOperator(this);
    }

    // Metodo auxiliar privado para buscar y borrar una tarea de una lista
    private Task removeTaskFromList(List<Task> list, String taskId) {

        // 1. Usamos un Traversal para obtener posiciones
        Traversal<Task> traversal = list.positions();

        Position<Task> currentPosition = null;
        Task task = null;
        boolean found = false;

        // 2. Recorremos las posiciones
        while (traversal.hasNext() && !found) {
            // 3. Obtenemos la posición actual
            currentPosition = traversal.next();
            // 4. Obtenemos el elemento (task) de esa posición
            task = currentPosition.getElem();

            // 5. Comparamos el ID
            if (task.getTaskId().equals(taskId)) {
                found = true;
            }
        }

        if (found) {
            // 6. Llamamos a delete con la posición
            list.delete(currentPosition);
            return task;
        }
        return null;
    }

    // Getters para datos
    public String getId() {
        return operatorId;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getAddress() {
        return address;
    }

    // Setters para actualizar datos
    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // Getters para iteradores (para los métodos get...Tasks)
    public Iterator<Task> getAssignedTasks() {
        return this.assignedTasks.values();
    }

    public Iterator<Task> getAcceptedTasks() {
        return this.acceptedTasks.values();
    }

    public Iterator<Task> getCompletedTasks() {
        return this.completedTasks.values();
    }

    public Iterator<Task> getRejectedTasks() {
        return this.rejectedTasks.values();
    }

    // Getters para tamaños (para los métodos num...Tasks y getOperator...Most)
    public int numAssignedTasks() {
        return this.assignedTasks.size();
    }

    public int numAcceptedTasks() {
        return this.acceptedTasks.size();
    }

    public int numCompletedTasks() {
        return this.completedTasks.size();
    }

    public int numRejectedTasks() {
        return this.rejectedTasks.size();
    }
}