package uoc.ds.pr.exceptions;

public class TaskNotFoundException extends DSException {
    private static final long serialVersionUID = 2L;

    public TaskNotFoundException() { super(); }

    public TaskNotFoundException(String msg) { super(msg); }
}