package vn.vme.exception;

public class InvalidDomainNameException extends RuntimeException {

    private String tenantKey;
    private String status;

    public InvalidDomainNameException(String message) {
        super(message);
    }
    public InvalidDomainNameException(String tenantKey, String status,String message) {
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
