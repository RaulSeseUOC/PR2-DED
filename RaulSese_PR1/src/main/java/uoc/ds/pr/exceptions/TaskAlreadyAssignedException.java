package uoc.ds.pr.exceptions;

public class TaskAlreadyAssignedException extends DSException {
    private static final long serialVersionUID = 3L;

    public TaskAlreadyAssignedException() { super(); }

    public TaskAlreadyAssignedException(String msg) { super(msg); }
}