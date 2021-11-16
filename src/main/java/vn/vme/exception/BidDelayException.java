package vn.vme.exception;

public class BidDelayException extends RuntimeException {

    private String tenantKey;
    private String status;

    public BidDelayException(String message) {
        super(message);
    }
    public BidDelayException(String tenantKey, String status,String message) {
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
