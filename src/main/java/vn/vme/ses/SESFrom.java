package vn.vme.ses;

public enum SESFrom {

    PAYTECH("admin1@paytech.vn", "Công ty cổ phần PayTech"),
    NO_REPLY("no-reply@paytech.vn", "Mail tự động Paytech"),
    RECONCILE("doisoat@paytech.vn", "Paytech đối soát");

    private final String email;
    private final String name;

    private SESFrom(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public static SESFrom getFromByEmail(String email) {
        for (SESFrom sesFrom : values()) {
            if (sesFrom.getEmail().equals(email)) {
                return sesFrom;
            }
        }
        return null;
    }
}