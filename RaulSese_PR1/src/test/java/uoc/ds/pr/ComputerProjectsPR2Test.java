package uoc.ds.pr;

import edu.uoc.ds.traversal.Iterator;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uoc.ds.pr.exceptions.*;
import uoc.ds.pr.model.Coordinator;
import uoc.ds.pr.model.Operator;
import uoc.ds.pr.model.Task;

import static uoc.ds.pr.util.CSVUtil.addProjects;
import static uoc.ds.pr.util.CSVUtil.addTasks;

public class ComputerProjectsPR2Test {

    protected ComputerProjects computerProjects;
    protected ComputerProjectsHelper helper;

    @Before
    public void setUp() throws Exception {
        this.computerProjects = FactoryComputerProjects.getComputerProjects();
        this.helper = this.computerProjects.getComputerProjectsHelper();
    }


    @After
    public void tearDown() {
        this.computerProjects = null;
    }


    @Test
    public void addOperatorTest() {
        Assert.assertEquals(28, helper.numOperators());
        computerProjects.addOperator("OP35", "Operator 35", "SURNAME 35", "Address 35");
        Assert.assertEquals(28+1, helper.numOperators());

        computerProjects.addOperator("OP36", "Operator 366", "SURNAME 366", "Address 366");
        Assert.assertEquals(29+1, helper.numOperators());

        Operator op = helper.getOperator("OP36");
        Assert.assertEquals("Operator 366", op.getName());
        Assert.assertEquals("SURNAME 366", op.getSurname());
        Assert.assertEquals("Address 366", op.getAddress());

        computerProjects.addOperator("OP36", "Operator 36", "SURNAME 36", "Address 36");
        Assert.assertEquals(30, helper.numOperators());

        op = helper.getOperator("OP36");
        Assert.assertEquals("Operator 36", op.getName());
        Assert.assertEquals("SURNAME 36", op.getSurname());
        Assert.assertEquals("Address 36", op.getAddress());

    }

    @Test
    public void addCoordinatorTest() {
        Assert.assertEquals(2, helper.numCoordinators());
        computerProjects.addCoordinator("COORD35", "Coordinator 35", "SURNAME 35", "Address 35");
        Assert.assertEquals(2+1, helper.numCoordinators());

        computerProjects.addCoordinator("COORD36", "Coordinator 366", "SURNAME 366", "Address 366");
        Assert.assertEquals(3+1, helper.numCoordinators());

        Coordinator coordinator = helper.getCoordinator("COORD36");
        Assert.assertEquals("Coordinator 366", coordinator.getName());
        Assert.assertEquals("SURNAME 366", coordinator.getSurname());
        Assert.assertEquals("Address 366", coordinator.getAddress());

        computerProjects.addCoordinator("COORD36", "Coordinator 36", "SURNAME 36", "Address 36");
        Assert.assertEquals(4, helper.numCoordinators());

        coordinator = helper.getCoordinator("COORD36");
        Assert.assertEquals("Coordinator 36", coordinator.getName());
        Assert.assertEquals("SURNAME 36", coordinator.getSurname());
        Assert.assertEquals("Address 36", coordinator.getAddress());

    }

    @Test
    public void addProjectTest() throws DSException {
        Assert.assertEquals(2, helper.numCoordinators());
        Assert.assertEquals(28, helper.numOperators());
        Assert.assertEquals(0, helper.numProjects());

        addProjects(computerProjects);

        Assert.assertEquals(7, helper.numProjects());
        Assert.assertEquals(28, helper.numOperators());
        Assert.assertEquals(2, helper.numCoordinators());

        Assert.assertEquals(4, helper.numProjectsByCoordinator("COORD1"));
        Assert.assertEquals(3, helper.numProjectsByCoordinator("COORD2"));
        Assert.assertEquals(0, helper.numProjectsByCoordinator("COORDXXX"));

        computerProjects.addProject("PROJ008", "Stealth Drone Control", "COORD2");
        computerProjects.addProject("PROJ009", "Utility Belt Inventory App", "COORD1");
        computerProjects.addProject("PROJ010", "Utility VVVVVV Monitoring", "COORD2");

        Assert.assertEquals(5, helper.numProjectsByCoordinator("COORD1"));
        Assert.assertEquals(5, helper.numProjectsByCoordinator("COORD2"));


        Assert.assertEquals(10, helper.numProjects());

        computerProjects.addProject("PROJ010", "Utility Vision Monitoring", "COORD2");
        Assert.assertEquals(10, helper.numProjects());
        Assert.assertEquals(5, helper.numProjectsByCoordinator("COORD1"));
        Assert.assertEquals(5, helper.numProjectsByCoordinator("COORD2"));


        computerProjects.addProject("PROJ010", "Utility Vision Monitoring", "COORD1");
        Assert.assertEquals(10, helper.numProjects());
        Assert.assertEquals(5+1, helper.numProjectsByCoordinator("COORD1"));
        Assert.assertEquals(5-1, helper.numProjectsByCoordinator("COORD2"));
    }


    @Test
    public void registerTaskInProjectTest() throws DSException {

        Assert.assertEquals(0, helper.numTasks());
        Assert.assertEquals(0, helper.numProjects());
        addProjectTest();
        addTasks(computerProjects);
        Assert.assertEquals(10, helper.numProjects());
        Assert.assertEquals(15, helper.numTasks());
        Assert.assertEquals(4, helper.numTasksByProject("PROJ001"));
        Assert.assertEquals(4, helper.numTasksByProject("PROJ002"));
        Assert.assertEquals(3, helper.numTasksByProject("PROJ003"));
        Assert.assertEquals(4, helper.numTasksByProject("PROJ004"));

        Assert.assertThrows(ProjectNotFoundException.class, () -> {
            computerProjects.registerTaskInProject("TASK5000", "PROJXXXX", "");
        });

        computerProjects.registerTaskInProject("TASK5000", "PROJ001", "Add multilingual support for AI assistant");

        Assert.assertEquals(15+1, helper.numTasks());
        Assert.assertEquals(4+1, helper.numTasksByProject("PROJ001"));
        Assert.assertEquals(4, helper.numTasksByProject("PROJ002"));
        Assert.assertEquals(3, helper.numTasksByProject("PROJ003"));
        Assert.assertEquals(4, helper.numTasksByProject("PROJ004"));

        Task task = helper.getTask("TASK5000");
        Assert.assertEquals(TaskStatus.REGISTERED, task.getStatus());
    }


    @Test
    public void assignTaskToOperatorTest() throws DSException{
        registerTaskInProjectTest();


        Assert.assertThrows(OperatorNotFoundException.class, () -> {
            computerProjects.assignTaskToOperatorTest("OPXXXXXX", "TASK5000");
        });

        Assert.assertThrows(TaskNotFoundException.class, () -> {
            computerProjects.assignTaskToOperatorTest("OP15", "TASKXXXXX");
        });

        Assert.assertEquals(0, helper.numAssignedTasksByOperator("OP15"));
        computerProjects.assignTaskToOperatorTest("OP15", "TASK5000");
        Assert.assertEquals(0+1, helper.numAssignedTasksByOperator("OP15"));

        Task task = helper.getTask("TASK5000");
        Assert.assertEquals(TaskStatus.ASSIGNED, task.getStatus());


        computerProjects.assignTaskToOperatorTest("OP15", "TASK001");
        computerProjects.assignTaskToOperatorTest("OP15", "TASK003");
        computerProjects.assignTaskToOperatorTest("OP15", "TASK005");
        computerProjects.assignTaskToOperatorTest("OP15", "TASK007");
        computerProjects.assignTaskToOperatorTest("OP15", "TASK009");
        computerProjects.assignTaskToOperatorTest("OP15", "TASK011");
        computerProjects.assignTaskToOperatorTest("OP15", "TASK013");
        computerProjects.assignTaskToOperatorTest("OP15", "TASK015");

        Assert.assertEquals(9,  helper.numAssignedTasksByOperator("OP15"));

        computerProjects.assignTaskToOperatorTest("OP16", "TASK002");
        computerProjects.assignTaskToOperatorTest("OP16", "TASK004");
        computerProjects.assignTaskToOperatorTest("OP16", "TASK006");
        computerProjects.assignTaskToOperatorTest("OP16", "TASK008");
        computerProjects.assignTaskToOperatorTest("OP16", "TASK010");
        computerProjects.assignTaskToOperatorTest("OP16", "TASK012");

        Assert.assertThrows(TaskAlreadyAssignedException.class, () -> {
            computerProjects.assignTaskToOperatorTest("OP16", "TASK007");
        });

        Assert.assertEquals(6,  helper.numAssignedTasksByOperator("OP16"));
    }


    @Test
    public void acceptTaskByOperatorTest() throws DSException {
        assignTaskToOperatorTest();

        Assert.assertEquals(9, helper.numAssignedTasksByOperator("OP15"));
        Assert.assertEquals(6, helper.numAssignedTasksByOperator("OP16"));

        Assert.assertEquals(0, helper.numAcceptedTasksByOperator("OP15"));
        Assert.assertEquals(0, helper.numAcceptedTasksByOperator("OP16"));

        Assert.assertThrows(OperatorNotFoundException.class, () -> {
            computerProjects.acceptTaskByOperator("OPXXX", "TASK5000");
        });

        Assert.assertThrows(TaskNotAssignedToOperatorException.class, () -> {
            computerProjects.acceptTaskByOperator("OP15", "TASK002");
        });

        Assert.assertEquals(9, helper.numAssignedTasksByOperator("OP15"));
        computerProjects.acceptTaskByOperator("OP15", "TASK001");
        computerProjects.acceptTaskByOperator("OP15", "TASK003");
        computerProjects.acceptTaskByOperator("OP15", "TASK007");
        Assert.assertEquals(6, helper.numAssignedTasksByOperator("OP15"));
        Assert.assertEquals(6, helper.numAssignedTasksByOperator("OP16"));

        Assert.assertEquals(3, helper.numAcceptedTasksByOperator("OP15"));
        Assert.assertEquals(0, helper.numAcceptedTasksByOperator("OP16"));

        computerProjects.acceptTaskByOperator("OP16", "TASK002");
        computerProjects.acceptTaskByOperator("OP16", "TASK006");
        Assert.assertEquals(3, helper.numAcceptedTasksByOperator("OP15"));
        Assert.assertEquals(2, helper.numAcceptedTasksByOperator("OP16"));

        Task task1 = helper.getTask("TASK001");
        Assert.assertEquals(TaskStatus.ACCEPTED, task1.getStatus());
        Assert.assertEquals("OP15", task1.getOperator().getId());

        Task task3 = helper.getTask("TASK003");
        Assert.assertEquals(TaskStatus.ACCEPTED, task3.getStatus());
        Assert.assertEquals("OP15", task3.getOperator().getId());

        Task task7 = helper.getTask("TASK007");
        Assert.assertEquals(TaskStatus.ACCEPTED, task7.getStatus());
        Assert.assertEquals("OP15", task7.getOperator().getId());

        Task task2 = helper.getTask("TASK002");
        Assert.assertEquals(TaskStatus.ACCEPTED, task2.getStatus());
        Assert.assertEquals("OP16", task2.getOperator().getId());

        Task task6 = helper.getTask("TASK006");
        Assert.assertEquals(TaskStatus.ACCEPTED, task6.getStatus());
        Assert.assertEquals("OP16", task6.getOperator().getId());
    }


    @Test
    public void doTaskByOperatorTest() throws DSException {

        acceptTaskByOperatorTest();

        Assert.assertThrows(OperatorNotFoundException.class, () -> {
            computerProjects.doTaskByOperator("OPXXXX");
        });

        Assert.assertThrows(NoAcceptedTasksException.class, () -> {
            computerProjects.doTaskByOperator("OP3");
        });

        Assert.assertEquals(6, helper.numAssignedTasksByOperator("OP15"));
        Assert.assertEquals(4, helper.numAssignedTasksByOperator("OP16"));
        Assert.assertEquals(3, helper.numAcceptedTasksByOperator("OP15"));
        Assert.assertEquals(2, helper.numAcceptedTasksByOperator("OP16"));
        Assert.assertEquals(0, helper.numCompletedTasksByOperator("OP15"));
        Assert.assertEquals(0, helper.numCompletedTasksByOperator("OP16"));

        computerProjects.doTaskByOperator("OP15");
        computerProjects.doTaskByOperator("OP15");

        computerProjects.doTaskByOperator("OP16");
        computerProjects.doTaskByOperator("OP16");

        Assert.assertEquals(6, helper.numAssignedTasksByOperator("OP15"));
        Assert.assertEquals(4, helper.numAssignedTasksByOperator("OP16"));
        Assert.assertEquals(1, helper.numAcceptedTasksByOperator("OP15"));
        Assert.assertEquals(0, helper.numAcceptedTasksByOperator("OP16"));
        Assert.assertEquals(2, helper.numCompletedTasksByOperator("OP15"));
        Assert.assertEquals(2, helper.numCompletedTasksByOperator("OP16"));

        Assert.assertThrows(NoAcceptedTasksException.class, () -> {
            computerProjects.doTaskByOperator("OP16");
        });

        Task task1 = helper.getTask("TASK001");
        Assert.assertEquals(TaskStatus.COMPLETED, task1.getStatus());

        Task task3 = helper.getTask("TASK003");
        Assert.assertEquals(TaskStatus.COMPLETED, task3.getStatus());

        Task task7 = helper.getTask("TASK007");
        Assert.assertEquals(TaskStatus.ACCEPTED, task7.getStatus());

        Task task2 = helper.getTask("TASK002");
        Assert.assertEquals(TaskStatus.COMPLETED, task2.getStatus());

        Task task6 = helper.getTask("TASK006");
        Assert.assertEquals(TaskStatus.COMPLETED, task6.getStatus());

        Assert.assertEquals(1, helper.numAcceptedTasksByOperator("OP15"));
        Assert.assertEquals(0, helper.numAcceptedTasksByOperator("OP16"));
        Assert.assertEquals(2, helper.numCompletedTasksByOperator("OP15"));
        Assert.assertEquals(2, helper.numCompletedTasksByOperator("OP16"));
    }


    @Test
    public void rejectTaskByOperatorTest() throws DSException {
        acceptTaskByOperatorTest();

        Assert.assertThrows(OperatorNotFoundException.class, () -> {
            computerProjects.rejectTaskByOperator("OPXXXX",  "TASK005");
        });

        Assert.assertThrows(NoAssignedTasksException.class, () -> {
            computerProjects.rejectTaskByOperator("OP3",  "TASK005");
        });

        Assert.assertThrows(TaskNotAssignedToOperatorException.class, () -> {
            computerProjects.rejectTaskByOperator("OP15",  "TASK008");
        });

        Assert.assertEquals(6, helper.numAssignedTasksByOperator("OP15"));
        Assert.assertEquals(4, helper.numAssignedTasksByOperator("OP16"));
        Assert.assertEquals(3, helper.numAcceptedTasksByOperator("OP15"));
        Assert.assertEquals(2, helper.numAcceptedTasksByOperator("OP16"));
        Assert.assertEquals(0, helper.numCompletedTasksByOperator("OP15"));
        Assert.assertEquals(0, helper.numCompletedTasksByOperator("OP16"));
        Assert.assertEquals(0, helper.numRejectedTasksByOperator("OP15"));
        Assert.assertEquals(0, helper.numRejectedTasksByOperator("OP16"));

        computerProjects.rejectTaskByOperator("OP15",  "TASK005");

        Assert.assertEquals(5, helper.numAssignedTasksByOperator("OP15"));
        Assert.assertEquals(4, helper.numAssignedTasksByOperator("OP16"));
        Assert.assertEquals(1, helper.numRejectedTasksByOperator("OP15"));
        Assert.assertEquals(0, helper.numRejectedTasksByOperator("OP16"));
        Assert.assertEquals(1, helper.numRejectingOperatorsByTask("TASK005"));

        Task task5 = helper.getTask("TASK005");
        Assert.assertEquals(TaskStatus.REJECTED, task5.getStatus());
        Assert.assertEquals(5, helper.numAssignedTasksByOperator("OP15"));
        Assert.assertEquals(4, helper.numAssignedTasksByOperator("OP16"));

        computerProjects.assignTaskToOperatorTest("OP3", "TASK005");
        Assert.assertEquals(1, helper.numAssignedTasksByOperator("OP3"));

        computerProjects.rejectTaskByOperator("OP3",  "TASK005");

        Assert.assertEquals(1, helper.numRejectedTasksByOperator("OP3"));
        Assert.assertEquals(1, helper.numRejectedTasksByOperator("OP15"));
        Assert.assertEquals(0, helper.numRejectedTasksByOperator("OP16"));

        Assert.assertEquals(2, helper.numRejectingOperatorsByTask("TASK005"));

    }

    @Test
    public void getAssignedTasksToOperatorTest() throws DSException {

        assignTaskToOperatorTest();

        Assert.assertThrows(NoAssignedTasksException.class, () -> {
            computerProjects.getAssignedTasksToOperator("OP4");
        });

        Assert.assertThrows(OperatorNotFoundException.class, () -> {
            computerProjects.getAssignedTasksToOperator("OPZZZZ");
        });

        Iterator<Task> it = computerProjects.getAssignedTasksToOperator("OP15");

        Assert.assertTrue(it.hasNext());
        Task task5000 = it.next();
        Assert.assertEquals("TASK5000", task5000.getTaskId());

        Assert.assertTrue(it.hasNext());
        Task task1 = it.next();
        Assert.assertEquals("TASK001", task1.getTaskId());

        Assert.assertTrue(it.hasNext());
        Task task3 = it.next();
        Assert.assertEquals("TASK003", task3.getTaskId());

        Assert.assertTrue(it.hasNext());
        Task task5 = it.next();
        Assert.assertEquals("TASK005", task5.getTaskId());

        Assert.assertTrue(it.hasNext());
        Task task7 = it.next();
        Assert.assertEquals("TASK007", task7.getTaskId());

        Assert.assertTrue(it.hasNext());
        Task task9 = it.next();
        Assert.assertEquals("TASK009", task9.getTaskId());

        Assert.assertTrue(it.hasNext());
        Task task11 = it.next();
        Assert.assertEquals("TASK011", task11.getTaskId());

        Assert.assertTrue(it.hasNext());
        Task task13 = it.next();
        Assert.assertEquals("TASK013", task13.getTaskId());

        Assert.assertTrue(it.hasNext());
        Task task15 = it.next();
        Assert.assertEquals("TASK015", task15.getTaskId());

        Assert.assertFalse(it.hasNext());
    }

    @Test
    public void getAllDoneTasksByOperatorTest() throws DSException {
        doTaskByOperatorTest();

        Assert.assertThrows(NoCompletedTasksException.class, () -> {
            computerProjects.getAllDoneTasksByOperator("OP4");
        });

        Assert.assertThrows(OperatorNotFoundException.class, () -> {
            computerProjects.getAllDoneTasksByOperator("OPZZZZ");
        });

        Iterator<Task> it = computerProjects.getAllDoneTasksByOperator("OP15");

        Assert.assertTrue(it.hasNext());
        Task task1 = it.next();
        Assert.assertEquals("TASK001", task1.getTaskId());

        Assert.assertTrue(it.hasNext());
        Task task3 = it.next();
        Assert.assertEquals("TASK003", task3.getTaskId());

        Assert.assertFalse(it.hasNext());
    }

    @Test
    public void getAllRejectedTasksByOperatorTest() throws DSException {
        rejectTaskByOperatorTest();

        Assert.assertThrows(NoRejectedTasksException.class, () -> {
            computerProjects.getRejectedTasksByOperator("OP4");
        });

        Assert.assertThrows(OperatorNotFoundException.class, () -> {
            computerProjects.getRejectedTasksByOperator("OPZZZZ");
        });

        Iterator<Task> it = computerProjects.getRejectedTasksByOperator("OP15");

        Assert.assertTrue(it.hasNext());
        Task task1 = it.next();
        Assert.assertEquals("TASK005", task1.getTaskId());

        Assert.assertFalse(it.hasNext());
    }

    @Test
    public void getOperatorWorksTheMostTest()  throws DSException {
        Assert.assertThrows(OperatorNotFoundException.class, () -> {
            computerProjects.getOperatorWorksTheMost();
        });

        doTaskByOperatorTest();

        Operator operator = computerProjects.getOperatorWorksTheMost();
        Assert.assertEquals("OP15", operator.getId());

        Assert.assertEquals(2, helper.numCompletedTasksByOperator("OP15"));
        Assert.assertEquals(2, helper.numCompletedTasksByOperator("OP16"));

        computerProjects.acceptTaskByOperator("OP16", "TASK008");
        computerProjects.acceptTaskByOperator("OP16", "TASK010");
        computerProjects.doTaskByOperator("OP16");
        computerProjects.doTaskByOperator("OP16");
        Assert.assertEquals(2, helper.numCompletedTasksByOperator("OP15"));
        Assert.assertEquals(4, helper.numCompletedTasksByOperator("OP16"));

        operator = computerProjects.getOperatorWorksTheMost();
        Assert.assertEquals("OP16", operator.getId());

        Assert.assertEquals(2, helper.numCompletedTasksByOperator("OP15"));
        Assert.assertEquals(4, helper.numCompletedTasksByOperator("OP16"));

    }

    @Test
    public void getOperatorRejectsTheMostTest()  throws DSException {
        Assert.assertThrows(OperatorNotFoundException.class, () -> {
            computerProjects.getOperatorRejectsTheMost();
        });

        rejectTaskByOperatorTest();

        Operator operator = computerProjects.getOperatorRejectsTheMost();

        Assert.assertEquals("OP15", operator.getId());
        Assert.assertEquals(1, helper.numRejectedTasksByOperator("OP15"));
        Assert.assertEquals(0, helper.numRejectedTasksByOperator("OP16"));

        computerProjects.rejectTaskByOperator("OP16", "TASK008");
        computerProjects.rejectTaskByOperator("OP16", "TASK010");

        Assert.assertEquals(1, helper.numRejectedTasksByOperator("OP15"));
        Assert.assertEquals(2, helper.numRejectedTasksByOperator("OP16"));

         operator = computerProjects.getOperatorRejectsTheMost();
        Assert.assertEquals("OP16", operator.getId());
    }

}