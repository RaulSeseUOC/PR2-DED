package uoc.ds.pr.exceptions;

public class ProjectNotFoundException extends DSException {
    private static final long serialVersionUID = 1L;

    public ProjectNotFoundException() { super(); }

    public ProjectNotFoundException(String msg) { super(msg); }
}