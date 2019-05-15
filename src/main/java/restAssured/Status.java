package restAssured;


/**
 * created by lavpal.bhatia on 30/08/2018
 */
public enum Status {

    SUCCESS("success"),
    ERROR("error");

    private final String name;

    private Status(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
