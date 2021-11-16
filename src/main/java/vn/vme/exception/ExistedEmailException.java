package vn.vme.exception;

public class ExistedEmailException extends RuntimeException {

    private String userId;
    private String status;

    public ExistedEmailException(String message) {
        super(message);
    }
    public ExistedEmailException(String userId, String status,String message) {
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
