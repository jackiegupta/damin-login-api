package vn.vme.exception;

public class ExistedPhoneException extends RuntimeException {

    private String userId;
    private String status;

    public ExistedPhoneException(String message) {
        super(message);
    }
    public ExistedPhoneException(String userId, String status,String message) {
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
