package impress.weasp.model;



public class PendingAdmin {
    private final String name;
    private final String encodedPassword;

    public PendingAdmin(String name, String encodedPassword) {
        this.name = name;
        this.encodedPassword = encodedPassword;
    }

    public String getName() {
        return name;
    }

    public String getEncodedPassword() {
        return encodedPassword;
    }
}
