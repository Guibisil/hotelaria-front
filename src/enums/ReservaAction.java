package enums;

public enum ReservaAction {
    CHECKIN("checkin"),
    CHECKOUT("checkout");

    private final String value;

    ReservaAction(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
