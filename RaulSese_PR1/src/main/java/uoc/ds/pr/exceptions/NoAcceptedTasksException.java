package uoc.ds.pr.exceptions;

public class NoAcceptedTasksException extends DSException {
    private static final long serialVersionUID = 5L;

    public NoAcceptedTasksException() { super(); }

    public NoAcceptedTasksException(String msg) { super(msg); }
}