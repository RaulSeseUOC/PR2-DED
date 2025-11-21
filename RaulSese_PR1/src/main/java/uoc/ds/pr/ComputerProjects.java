package uoc.ds.pr;


import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.exceptions.*;
import uoc.ds.pr.model.Operator;
import uoc.ds.pr.model.Task;

public interface ComputerProjects {
    static final int MAX_NUM_OPERATORS = 40;
    static final int MAX_NUM_COORDINATORS = 35;

    void addOperator(String operatorId, String name, String surname, String address);

    void addCoordinator(String coordinatorId, String name, String surname, String address);

    void addProject(String projectId, String coordinatorId, String name);

    void registerTaskInProject(String taskId, String projectId, String description) throws ProjectNotFoundException;

    void assignTaskToOperatorTest(String operatorId, String taskId) throws OperatorNotFoundException, TaskNotFoundException, TaskAlreadyAssignedException;

    void acceptTaskByOperator(String operatorId, String taskId) throws OperatorNotFoundException, TaskNotAssignedToOperatorException;

    void doTaskByOperator(String operatorId) throws OperatorNotFoundException, NoAcceptedTasksException;

    void rejectTaskByOperator(String operatorId, String taskId) throws OperatorNotFoundException, TaskNotAssignedToOperatorException, NoAssignedTasksException ;

    Iterator<Task> getAssignedTasksToOperator(String operatorId) throws OperatorNotFoundException, NoAssignedTasksException;

    Iterator<Task> getAllDoneTasksByOperator(String operatorId) throws OperatorNotFoundException, NoCompletedTasksException;

    Iterator<Task> getRejectedTasksByOperator(String operatorId) throws OperatorNotFoundException, NoRejectedTasksException;

    Operator getOperatorWorksTheMost() throws OperatorNotFoundException;

    Operator getOperatorRejectsTheMost() throws OperatorNotFoundException;

    ComputerProjectsHelper getComputerProjectsHelper();

}


