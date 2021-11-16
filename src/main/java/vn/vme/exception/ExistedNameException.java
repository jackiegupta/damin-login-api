package vn.vme.exception;

public class ExistedNameException extends RuntimeException {

    private String userId;
    private String status;

    public ExistedNameException(String message) {
        super(message);
    }
    public ExistedNameException(String userId, String status,String message) {
        super(message);
        this.userId=userId;
        this.status=status;
    }

    public String getUserId() {
        return userId;
    }

    public String getStatus() {
        return status;
    }
}
