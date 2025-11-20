package uoc.ds.pr.exceptions;

public class NoCompletedTasksException extends DSException {
    private static final long serialVersionUID = 7L;

    public NoCompletedTasksException() { super(); }

    public NoCompletedTasksException(String msg) { super(msg); }
}