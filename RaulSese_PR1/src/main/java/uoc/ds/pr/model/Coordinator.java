package uoc.ds.pr.model;

import edu.uoc.ds.adt.helpers.Position;
import edu.uoc.ds.adt.sequential.LinkedList;
import edu.uoc.ds.adt.sequential.List;
import edu.uoc.ds.traversal.Traversal;

public class Coordinator extends Operator {

    // Lista de proyectos que coordina
    private List<Project> coordinatedProjects;

    public Coordinator(String coordinatorId, String name, String surname, String address) {
        // Llama al constructor de la clase padre (Operator)
        super(coordinatorId, name, surname, address);
        this.coordinatedProjects = new LinkedList<>();
    }

    public void addProject(Project project) {
        this.coordinatedProjects.insertEnd(project);
    }

    public void removeProject(Project project) {
        // Borramos el proyecto de la lista (O(N), pero N es pequeño)

        // 1. Usamos un Traversal para obtener posiciones
        Traversal<Project> traversal = this.coordinatedProjects.positions();

        Position<Project> currentPosition = null;
        Project currentProject = null;
        boolean found = false;

        // 2. Recorremos las posiciones
        while (traversal.hasNext() && !found) {
            // 3. Obtenemos la posición actual
            currentPosition = traversal.next();
            // 4. Obtenemos el elemento (project) de esa posición
            currentProject = currentPosition.getElem();

            // 5. Comparamos el proyecto
            if (currentProject.equals(project)) {
                found = true;
            }
        }

        if (found) {
            // 6. Llamamos a delete con la posición
            this.coordinatedProjects.delete(currentPosition);
        }
    }

    public int numProjects() {
        return this.coordinatedProjects.size();
    }
}