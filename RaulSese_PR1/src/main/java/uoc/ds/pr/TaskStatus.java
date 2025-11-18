package uoc.ds.pr;

public enum TaskStatus {
    REGISTERED(0),
    ASSIGNED(1),
    ACCEPTED(2),
    COMPLETED(3),
    REJECTED(4);
    private final int value;

    TaskStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
