package uoc.ds.pr;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uoc.ds.pr.exceptions.*;
import uoc.ds.pr.model.Operator;
import uoc.ds.pr.model.Task;

public class ComputerProjectsPR2ExtendedTest {

    private ComputerProjects computerProjects;
    private ComputerProjectsHelper helper;

    @Before
    public void setUp() throws Exception {
        this.computerProjects = FactoryComputerProjects.getComputerProjects();
        this.helper = this.computerProjects.getComputerProjectsHelper();
    }

    @After
    public void tearDown() {
        this.computerProjects = null;
    }

    /**
     * Test para verificar que al añadir un operador con un ID existente se
     * actualiza la información
     * en lugar de crear un duplicado o lanzar un error.
     */
    @Test
    public void updateOperatorTest() {
        // OP3 existe (del CSV)
        Operator op3 = helper.getOperator("OP3");
        Assert.assertNotNull(op3);
        String originalName = op3.getName();

        // Actualizamos OP3
        computerProjects.addOperator("OP3", "NewName", "NewSurname", "NewAddress");

        Operator updatedOp3 = helper.getOperator("OP3");
        Assert.assertEquals("NewName", updatedOp3.getName());
        Assert.assertEquals("NewSurname", updatedOp3.getSurname());
        Assert.assertEquals("NewAddress", updatedOp3.getAddress());

        // Verificamos que la cuenta no ha cambiado.
        // operators.csv tiene 28 entradas (OP3 a OP30).
        Assert.assertEquals(28, helper.numOperators());
    }

    /**
     * Test para verificar que al cambiar el coordinador de un proyecto se
     * actualizan las listas correctamente.
     */
    @Test
    public void updateProjectCoordinatorTest() {
        // Usamos COORD1 y COORD2 que existen en el CSV
        computerProjects.addProject("PR_NEW", "New Project", "COORD1");

        // Verificamos que se añadió a COORD1
        Assert.assertEquals(1, helper.numProjectsByCoordinator("COORD1"));
        // COORD2 debería tener 0 (asumiendo que no tiene proyectos previos en el CSV o
        // setup)
        // Nota: Si el CSV carga proyectos, esto podría fallar. Mejor verificar
        // incremento.
        int initialCoord2Projects = helper.numProjectsByCoordinator("COORD2");

        // Cambiamos el coordinador a COORD2
        computerProjects.addProject("PR_NEW", "New Project Updated", "COORD2");

        Assert.assertEquals(0, helper.numProjectsByCoordinator("COORD1"));
        Assert.assertEquals(initialCoord2Projects + 1, helper.numProjectsByCoordinator("COORD2"));

        // Verificamos que los detalles del proyecto se han actualizado
        Assert.assertEquals("New Project Updated", helper.getProject("PR_NEW").getName());
    }

    /**
     * Test del ciclo de vida completo: Asignar -> Rechazar -> Reasignar -> Aceptar
     * -> Completar
     */
    @Test
    public void fullTaskLifecycleTest() throws DSException {
        // Configuración
        String taskId = "TASK_LIFECYCLE";
        String op1Id = "OP3"; // Usamos OP3
        String op2Id = "OP4"; // Usamos OP4

        // Usamos COORD1 que existe
        computerProjects.addProject("PR_LIFECYCLE", "Lifecycle Project", "COORD1");
        computerProjects.registerTaskInProject(taskId, "PR_LIFECYCLE", "Description");

        // 1. Asignar a OP3
        computerProjects.assignTaskToOperatorTest(op1Id, taskId);
        Assert.assertEquals(1, helper.numAssignedTasksByOperator(op1Id));

        // 2. OP3 Rechaza
        computerProjects.rejectTaskByOperator(op1Id, taskId);
        Assert.assertEquals(0, helper.numAssignedTasksByOperator(op1Id));
        Assert.assertEquals(1, helper.numRejectedTasksByOperator(op1Id));
        Assert.assertEquals(1, helper.numRejectingOperatorsByTask(taskId));

        // 3. Reasignar a OP4
        computerProjects.assignTaskToOperatorTest(op2Id, taskId);
        Assert.assertEquals(1, helper.numAssignedTasksByOperator(op2Id));

        // 4. OP4 Acepta
        computerProjects.acceptTaskByOperator(op2Id, taskId);
        Assert.assertEquals(0, helper.numAssignedTasksByOperator(op2Id));
        Assert.assertEquals(1, helper.numAcceptedTasksByOperator(op2Id));

        // 5. OP4 Completa
        computerProjects.doTaskByOperator(op2Id);
        Assert.assertEquals(0, helper.numAcceptedTasksByOperator(op2Id));
        Assert.assertEquals(1, helper.numCompletedTasksByOperator(op2Id));

        // Verificar estado final
        Task task = helper.getTask(taskId);
        Assert.assertNotNull(task);
    }

    /**
     * Test de casos límite:
     * - Aceptar una tarea no asignada
     * - Completar una tarea no aceptada
     */
    @Test
    public void taskLifecycleEdgeCasesTest() throws DSException {
        String taskId = "TASK_EDGE";
        String opId = "OP5"; // Usamos OP5

        computerProjects.addProject("PR_EDGE", "Edge Project", "COORD1");
        computerProjects.registerTaskInProject(taskId, "PR_EDGE", "Desc");

        // Intentar aceptar sin asignación previa
        try {
            computerProjects.acceptTaskByOperator(opId, taskId);
            Assert.fail("Debería haber lanzado TaskNotAssignedToOperatorException");
        } catch (TaskNotAssignedToOperatorException e) {
            // Esperado
        }

        // Asignar y luego intentar completar sin aceptar
        computerProjects.assignTaskToOperatorTest(opId, taskId);

        // doTaskByOperator coge de la cola de aceptadas. Si está vacía, lanza
        // NoAcceptedTasksException
        try {
            computerProjects.doTaskByOperator(opId);
            Assert.fail("Debería haber lanzado NoAcceptedTasksException");
        } catch (NoAcceptedTasksException e) {
            // Esperado
        }
    }

    /**
     * Test de desempate en estadísticas para 'Quien más trabaja'.
     * La implementación usa desigualdad estricta (>), por lo que debería quedarse
     * con el primero encontrado.
     */
    @Test
    public void statsTieBreakingTest() throws DSException {
        // Crear dos operadores nuevos para aislar este test
        String opA = "OP_A";
        String opB = "OP_B";
        computerProjects.addOperator(opA, "A", "A", "A");
        computerProjects.addOperator(opB, "B", "B", "B");

        // Crear proyecto y tareas
        computerProjects.addProject("PR_STATS", "Stats", "COORD1");
        String t1 = "T1";
        String t2 = "T2";
        computerProjects.registerTaskInProject(t1, "PR_STATS", "D");
        computerProjects.registerTaskInProject(t2, "PR_STATS", "D");

        // OP_A completa T1
        computerProjects.assignTaskToOperatorTest(opA, t1);
        computerProjects.acceptTaskByOperator(opA, t1);
        computerProjects.doTaskByOperator(opA);

        // OP_B completa T2
        computerProjects.assignTaskToOperatorTest(opB, t2);
        computerProjects.acceptTaskByOperator(opB, t2);
        computerProjects.doTaskByOperator(opB);

        // Ambos tienen 1 tarea completada.
        // Como OP_A se añadió primero (o tiene índice menor), debería ser devuelto si
        // la lógica es 'primero encontrado'.
        // Si la lógica fuera 'último encontrado' (>=), sería OP_B.
        // Si la lógica fuera 'desempate por ID', sería OP_A (A < B).

        Operator bestOp = computerProjects.getOperatorWorksTheMost();
        // Basado en la implementación actual: if (current > max) -> update.
        // 1 > -1 (OP_A) -> max=1, best=OP_A
        // 1 > 1 (OP_B) -> false.
        // Por tanto debería ser OP_A.

        Assert.assertEquals(opA, bestOp.getId());
    }
}
