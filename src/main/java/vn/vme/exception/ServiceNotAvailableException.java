package vn.vme.exception;

public class ServiceNotAvailableException extends RuntimeException {

    private String tenantKey;
    private String status;

    public ServiceNotAvailableException(String message) {
        super(message);
    }
    public ServiceNotAvailableException(String tenantKey, String status,String message) {
        super(message);
        this.tenantKey=tenantKey;
        this.status=status;
    }

    public String getTenantKey() {
        return tenantKey;
    }

    public String getStatus() {
        return status;
    }
}
