package uoc.ds.pr.exceptions;

public class TaskNotAssignedToOperatorException extends DSException {
    private static final long serialVersionUID = 4L;

    public TaskNotAssignedToOperatorException() { super(); }

    public TaskNotAssignedToOperatorException(String msg) { super(msg); }
}