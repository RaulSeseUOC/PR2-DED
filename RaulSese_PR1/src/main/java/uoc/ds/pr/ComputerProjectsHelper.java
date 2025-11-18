package uoc.ds.pr;

import uoc.ds.pr.model.Coordinator;
import uoc.ds.pr.model.Operator;
import uoc.ds.pr.model.Project;
import uoc.ds.pr.model.Task;

public interface ComputerProjectsHelper {
    Operator getOperator(String id);
    int numOperators();

    Coordinator getCoordinator(String id);
    int numCoordinators();

    Project getProject(String id);
    int numProjects();

    int numProjectsByCoordinator(String coordId);

    int numTasks();
    int numTasksByProject(String projectId);

    Task getTask(String taskId);

    int numAssignedTasksByOperator(String operatorId);

    int numAcceptedTasksByOperator(String operatorId);

    int numCompletedTasksByOperator(String operatorId);

    int numRejectedTasksByOperator(String operatorId);

    int numRejectingOperatorsByTask(String taskId);
}
