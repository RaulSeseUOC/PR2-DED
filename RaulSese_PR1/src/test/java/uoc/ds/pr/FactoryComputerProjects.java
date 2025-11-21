package uoc.ds.pr;


import uoc.ds.pr.exceptions.DSException;
import static uoc.ds.pr.util.CSVUtil.*;

public class FactoryComputerProjects {

    public static ComputerProjects getComputerProjects() throws DSException {
        ComputerProjects computerProjects;
        computerProjects = new ComputerProjectsPR2Impl();

        addOperators(computerProjects);
        addCoordinators(computerProjects);

        return computerProjects;
    }



}