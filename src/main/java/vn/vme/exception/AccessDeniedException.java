package vn.vme.exception;

public class AccessDeniedException extends RuntimeException {

    private String userName;
    private String status;

    public AccessDeniedException(String message) {
        super(message);
    }
    public AccessDeniedException(String tenantKey, String status,String message) {
        super(message);
        this.userName=tenantKey;
        this.status=status;
    }

    public String getTenantKey() {
        return userName;
    }

    public String getStatus() {
        return status;
    }
}
