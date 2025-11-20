package uoc.ds.pr.util;

import org.apache.commons.csv.CSVParser;

import java.io.IOException;
import org.apache.commons.csv.*;
import uoc.ds.pr.ComputerProjects;
import uoc.ds.pr.exceptions.DSException;
import uoc.ds.pr.exceptions.ProjectNotFoundException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.function.BiConsumer;


public class CSVUtil {

    public static CSVParser getCVSParser(String filePath) {

        CSVParser csvParser = null;
        try {
//            System.out.println(filePath);
            InputStream inputStream = CSVUtil.class.getResourceAsStream(filePath);
            if (inputStream == null) {
                throw new FileNotFoundException("Resource not found: " + filePath);
            }

            Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            csvParser = new CSVParser(reader, CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).build());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return csvParser;
    }

    public static void processRows(String csvFile, String[] fields,
                                    ComputerProjects computerProjects,
                                    BiConsumer<CSVRecord, ComputerProjects> addFunction) throws DSException {
        CSVParser csvParser = getCVSParser(csvFile);
        for (CSVRecord record : csvParser) {
            addFunction.accept(record, computerProjects);
        }
    }

    public static void addOperators(ComputerProjects computerProjects) throws DSException {
        processRows("/operators.csv",
                new String[]{"operatorId", "name", "surname", "address"},
                computerProjects,
                (record, cp) -> cp.addOperator(
                        record.get("operatorId"),
                        record.get("name"),
                        record.get("surname"),
                        record.get("address")
                )
        );
    }

    public static void addCoordinators(ComputerProjects computerProjects) throws DSException {
        processRows("/coordinators.csv",
                new String[]{"coordinatorId", "name", "surname", "address"},
                computerProjects,
                (record, cp) -> cp.addCoordinator(
                        record.get("coordinatorId"),
                        record.get("name"),
                        record.get("surname"),
                        record.get("address")
                )
        );
    }

    public static void addProjects(ComputerProjects computerProjects)  throws DSException {
        processRows("/projects.csv",
                new String[]{"projectId", "name", "surname", "address"},
                computerProjects,
                (record, cp) -> cp.addProject(
                        record.get("projectId"),
                        record.get("name"),
                        record.get("coordinatorId")));
    }

    public static void addTasks(ComputerProjects computerProjects)  throws DSException {
        processRows("/tasks.csv",
                new String[]{"taskId", "projectId", "description"},
                computerProjects,
                (record, cp) -> {
                    try {
                        cp.registerTaskInProject(
                                record.get("taskId"),
                                record.get("projectId"),
                                record.get("description"));
                    } catch (ProjectNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

}

