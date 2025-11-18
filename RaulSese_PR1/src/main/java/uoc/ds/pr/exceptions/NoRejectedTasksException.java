package uoc.ds.pr.exceptions;

public class NoRejectedTasksException extends DSException {
    private static final long serialVersionUID = 8L;

    public NoRejectedTasksException() { super(); }

    public NoRejectedTasksException(String msg) { super(msg); }
}