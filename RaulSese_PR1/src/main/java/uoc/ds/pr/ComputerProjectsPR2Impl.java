package uoc.ds.pr;

import edu.uoc.ds.adt.nonlinear.Dictionary;
import edu.uoc.ds.adt.nonlinear.HashTable;
import edu.uoc.ds.adt.sequential.DictionaryArrayImpl;
import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.exceptions.*;
import uoc.ds.pr.model.Coordinator;
import uoc.ds.pr.model.Operator;
import uoc.ds.pr.model.Project;
import uoc.ds.pr.model.Task;

public class ComputerProjectsPR2Impl implements ComputerProjects, ComputerProjectsHelper {

    // Contenedores acotados para Operadores y Coordinadores (acceso por ID)
    private Dictionary<String, Operator> operators;
    private Dictionary<String, Coordinator> coordinators;
    // Contenedores no acotados para Proyectos y Tareas (acceso por ID)
    private Dictionary<String, Project> projects;
    private Dictionary<String, Task> tasks;

    public ComputerProjectsPR2Impl() {
        // Inicializamos las colecciones acotadas con su tamaño máximo
        this.operators = new DictionaryArrayImpl<>(MAX_NUM_OPERATORS);
        this.coordinators = new DictionaryArrayImpl<>(MAX_NUM_COORDINATORS);
        // Inicializamos las colecciones no acotadas (Tablas Hash)
        this.projects = new HashTable<>();
        this.tasks = new HashTable<>();
    }

    @Override
    public ComputerProjectsHelper getComputerProjectsHelper() {
        // Los tests esperan que la propia clase sea el helper
        return this;
    }

    @Override
    public void addOperator(String operatorId, String name, String surname, String address) {
        Operator op = this.operators.get(operatorId);
        if (op != null) {
            // Si ya existe, actualizamos sus datos
            op.setName(name);
            op.setSurname(surname);
            op.setAddress(address);
        } else {
            // Si no existe, creamos uno nuevo y lo añadimos
            op = new Operator(operatorId, name, surname, address);
            this.operators.put(operatorId, op);
        }
    }

    @Override
    public void addCoordinator(String coordinatorId, String name, String surname, String address) {
        Coordinator coord = this.coordinators.get(coordinatorId);
        if (coord != null) {
            // Si ya existe, actualizamos sus datos
            coord.setName(name);
            coord.setSurname(surname);
            coord.setAddress(address);
        } else {
            // Si no existe, creamos uno nuevo y lo añadimos
            coord = new Coordinator(coordinatorId, name, surname, address);
            this.coordinators.put(coordinatorId, coord);
        }
    }

    @Override
    public void addProject(String projectId, String coordinatorId, String name) {
        // 1. Buscamos al coordinador (si no existe, el get devuelve null)
        Coordinator coord = this.coordinators.get(coordinatorId);
        if (coord == null) {
            return;
        }

        // 2. Buscamos el proyecto
        Project project = this.projects.get(projectId);

        if (project != null) {
            // 3. Si el proyecto existe, actualizamos
            project.setName(name);

            // Verificamos si ha cambiado de coordinador
            if (!project.getCoordinator().getId().equals(coordinatorId)) {
                // Lo quitamos de la lista del coordinador antiguo
                ((Coordinator) project.getCoordinator()).removeProject(project);
                // Lo añadimos al nuevo
                coord.addProject(project);
                // Actualizamos la referencia en el proyecto
                project.setCoordinator(coord);
            }
        } else {
            // 4. Si el proyecto no existe, lo creamos
            project = new Project(projectId, name, coord);
            this.projects.put(projectId, project);
            // Y lo añadimos a la lista del coordinador
            coord.addProject(project);
        }
    }

    @Override
    public void registerTaskInProject(String taskId, String projectId, String description) throws ProjectNotFoundException {
        // 1. Buscamos el proyecto
        Project project = this.projects.get(projectId);

        // 2. Si no existe, lanzamos excepción
        if (project == null) {
            throw new ProjectNotFoundException("Project with ID " + projectId + " not found.");
        }

        // 3. Verificamos si la tarea ya existe (para no duplicarla)
        Task task = this.tasks.get(taskId);

        if (task == null) {
            // 4. Si no existe, la creamos
            task = new Task(taskId, description, project);
            // La añadimos al gestor global de tareas
            this.tasks.put(taskId, task);
            // Y la añadimos a la lista de tareas del proyecto
            project.addTask(task);
        }
        // Si la tarea ya existía, no hacemos nada (como se establece en los tests)
    }

    @Override
    public Operator getOperator(String id) {
        return this.operators.get(id);
    }

    @Override
    public int numOperators() {
        return this.operators.size();
    }

    @Override
    public Coordinator getCoordinator(String id) {
        return this.coordinators.get(id);
    }

    @Override
    public int numCoordinators() {
        return this.coordinators.size();
    }

    @Override
    public Project getProject(String id) {
        return this.projects.get(id);
    }

    @Override
    public int numProjects() {
        return this.projects.size();
    }

    @Override
    public int numProjectsByCoordinator(String coordId) {
        Coordinator coord = this.coordinators.get(coordId);
        if (coord != null) {
            return coord.numProjects();
        }
        return 0; // Si el coordinador no existe, tiene 0 proyectos
    }

    @Override
    public int numTasks() {
        return this.tasks.size();
    }

    @Override
    public int numTasksByProject(String projectId) {
        Project project = this.projects.get(projectId);
        if (project != null) {
            return project.numTasks();
        }
        return 0;
    }

    @Override
    public Task getTask(String taskId) {
        return this.tasks.get(taskId);
    }

    // Dejamos los otros métodos helper para cuando implementemos las listas de tareas
    @Override
    public int numAssignedTasksByOperator(String operatorId) {
        Operator op = this.operators.get(operatorId);
        return (op != null) ? op.numAssignedTasks() : 0;
    }

    @Override
    public int numAcceptedTasksByOperator(String operatorId) {
        Operator op = this.operators.get(operatorId);
        return (op != null) ? op.numAcceptedTasks() : 0;
    }

    @Override
    public int numCompletedTasksByOperator(String operatorId) {
        Operator op = this.operators.get(operatorId);
        return (op != null) ? op.numCompletedTasks() : 0;
    }

    @Override
    public int numRejectedTasksByOperator(String operatorId) {
        Operator op = this.operators.get(operatorId);
        return (op != null) ? op.numRejectedTasks() : 0;
    }

    @Override
    public int numRejectingOperatorsByTask(String taskId) {
        Task task = this.tasks.get(taskId);
        return (task != null) ? task.numOperatorsWhoRejected() : 0;
    }

    @Override
    public void assignTaskToOperatorTest(String operatorId, String taskId) throws OperatorNotFoundException, TaskNotFoundException, TaskAlreadyAssignedException {
        // 1. Buscamos al operador
        Operator operator = this.operators.get(operatorId);
        if (operator == null) {
            throw new OperatorNotFoundException("Operator " + operatorId + " not found.");
        }

        // 2. Buscamos la tarea
        Task task = this.tasks.get(taskId);
        if (task == null) {
            throw new TaskNotFoundException("Task " + taskId + " not found.");
        }

        // 3. Comprobamos que la tarea no esté ya asignada
        if (task.getStatus() != TaskStatus.REGISTERED) {
            throw new TaskAlreadyAssignedException("Task " + taskId + " is already assigned or in progress.");
        }

        // 4. Asignamos la tarea al operador (lógica del modelo)
        operator.assignTask(task);

        // 5. Actualizamos el estado de la tarea
        task.setStatus(TaskStatus.ASSIGNED);
    }

    @Override
    public void acceptTaskByOperator(String operatorId, String taskId) throws OperatorNotFoundException, TaskNotAssignedToOperatorException {
        // 1. Buscamos al operador
        Operator operator = this.operators.get(operatorId);
        if (operator == null) {
            throw new OperatorNotFoundException("Operator " + operatorId + " not found.");
        }

        // 2. Buscamos la tarea
        Task task = this.tasks.get(taskId);
        // La interfaz no pide TaskNotFoundException, así que si es null, el propio
        // metodo operator.acceptTask lanzará TaskNotAssignedToOperatorException

        // 3. El operador acepta la tarea
        // Este metodo ya se encarga de moverla de 'assigned' a 'accepted'
        // y de lanzar la excepción si no la tenía asignada.
        operator.acceptTask(task);

        // 4. Actualizamos el estado de la tarea
        task.setStatus(TaskStatus.ACCEPTED);
    }

    @Override
    public void doTaskByOperator(String operatorId) throws OperatorNotFoundException, NoAcceptedTasksException {
        // 1. Buscamos al operador
        Operator operator = this.operators.get(operatorId);
        if (operator == null) {
            throw new OperatorNotFoundException("Operator " + operatorId + " not found.");
        }

        // 2. Comprobamos si tiene tareas aceptadas
        if (operator.numAcceptedTasks() == 0) {
            throw new NoAcceptedTasksException();
        }

        // 3. El operador completa la tarea
        // Este método saca (poll) de la cola 'accepted' y añade a 'completed'
        Task completedTask = operator.completeTask();

        // 4. Actualizamos el estado de la tarea
        completedTask.setStatus(TaskStatus.COMPLETED);
    }

    @Override
    public void rejectTaskByOperator(String operatorId, String taskId) throws OperatorNotFoundException, TaskNotAssignedToOperatorException, NoAssignedTasksException {
        // 1. Buscamos al operador
        Operator operator = this.operators.get(operatorId);
        if (operator == null) {
            throw new OperatorNotFoundException("Operator " + operatorId + " not found.");
        }

        // 2. Comprobamos si tiene tareas asignadas
        if (operator.numAssignedTasks() == 0) {
            throw new NoAssignedTasksException();
        }

        // 3. Buscamos la tarea
        Task task = this.tasks.get(taskId);
        // Si la tarea es null, el metodo rejectTask lanzará la excepción

        // 4. El operador rechaza la tarea
        // Este metodo la quita de 'assigned' y la pone en 'rejected'
        operator.rejectTask(task);

        // 5. Actualizamos el estado de la tarea
        task.setStatus(TaskStatus.REJECTED);
    }

    @Override
    public Iterator<Task> getAssignedTasksToOperator(String operatorId) throws OperatorNotFoundException, NoAssignedTasksException {
        // 1. Buscamos al operador
        Operator op = this.operators.get(operatorId);
        if (op == null) {
            throw new OperatorNotFoundException("Operator " + operatorId + " not found.");
        }

        // 2. Comprobamos si la lista está vacía
        if (op.numAssignedTasks() == 0) {
            throw new NoAssignedTasksException();
        }

        // 3. Devolvemos el iterador de la lista del operador
        return op.getAssignedTasks();
    }

    @Override
    public Iterator<Task> getAllDoneTasksByOperator(String operatorId) throws OperatorNotFoundException, NoCompletedTasksException {
        // 1. Buscamos al operador
        Operator op = this.operators.get(operatorId);
        if (op == null) {
            throw new OperatorNotFoundException("Operator " + operatorId + " not found.");
        }

        // 2. Comprobamos si la lista está vacía
        if (op.numCompletedTasks() == 0) {
            throw new NoCompletedTasksException();
        }

        // 3. Devolvemos el iterador de la lista del operador
        return op.getCompletedTasks();
    }

    @Override
    public Iterator<Task> getRejectedTasksByOperator(String operatorId) throws OperatorNotFoundException, NoRejectedTasksException {
        // 1. Buscamos al operador
        Operator op = this.operators.get(operatorId);
        if (op == null) {
            throw new OperatorNotFoundException("Operator " + operatorId + " not found.");
        }

        // 2. Comprobamos si la lista está vacía
        if (op.numRejectedTasks() == 0) {
            throw new NoRejectedTasksException();
        }

        // 3. Devolvemos el iterador de la lista del operador
        return op.getRejectedTasks();
    }

    @Override
    public Operator getOperatorWorksTheMost() throws OperatorNotFoundException {
        // 1. Comprobamos si hay operadores
        if (this.operators.isEmpty()) {
            throw new OperatorNotFoundException();
        }

        // 2. Obtenemos un iterador de todos los operadores
        Iterator<Operator> it = this.operators.values();

        Operator operatorMax = null; // Para guardar el operador con más tareas
        int maxTasks = -1; // Empezamos en -1 para que el primer operador con 0 tareas ya sea un máximo

        // 3. Recorremos todos los operadores
        while (it.hasNext()) {
            Operator currentOperator = it.next();
            int currentTasks = currentOperator.numCompletedTasks(); // Obtenemos el tamaño de su lista

            // 4. Comparamos
            if (currentTasks > maxTasks) {
                maxTasks = currentTasks;
                operatorMax = currentOperator;
            }
        }

        // 5. Caso de error: si maxTasks sigue en -1 (no debería pasar) o es 0
        // (ningún operador ha completado tareas), lanzamos excepción.
        if (maxTasks <= 0) {
            throw new OperatorNotFoundException("No operators have completed tasks yet.");
        }

        // 6. Devolvemos el operador encontrado
        return operatorMax;
    }

    @Override
    public Operator getOperatorRejectsTheMost() throws OperatorNotFoundException {
        // 1. Comprobamos si hay operadores
        if (this.operators.isEmpty()) {
            throw new OperatorNotFoundException();
        }

        // 2. Obtenemos un iterador de todos los operadores
        Iterator<Operator> it = this.operators.values();

        Operator operatorMax = null; // Para guardar el operador con más rechazos
        int maxRejections = -1; // Empezamos en -1

        // 3. Recorremos todos los operadores
        while (it.hasNext()) {
            Operator currentOperator = it.next();
            int currentRejections = currentOperator.numRejectedTasks(); // Obtenemos el tamaño de su lista

            // 4. Comparamos
            if (currentRejections > maxRejections) {
                maxRejections = currentRejections;
                operatorMax = currentOperator;
            }
        }

        // 5. Caso de error: si ningún operador ha rechazado tareas
        if (maxRejections <= 0) {
            throw new OperatorNotFoundException("No operators have rejected tasks yet.");
        }

        // 6. Devolvemos el operador encontrado
        return operatorMax;
    }
}