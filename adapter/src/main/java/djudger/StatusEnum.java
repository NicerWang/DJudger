package djudger;

public enum StatusEnum {
    OK("OK"),
    TIMEOUT("TIMEOUT"),
    ERROR("ERROR"),
    PENDING("PENDING");

    private final String describe;

    StatusEnum(String describe) {
        this.describe = describe;
    }

    public String getDescribe() {
        return describe;
    }
}
