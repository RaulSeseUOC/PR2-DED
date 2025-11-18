package uoc.ds.pr.exceptions;

public class OperatorNotFoundException extends DSException {
    private static final long serialVersionUID = -1234567890123456789L; // Puedes poner cualquier número

    public OperatorNotFoundException() {
        super();
    }

    public OperatorNotFoundException(String msg) {
        super(msg);
    }
}