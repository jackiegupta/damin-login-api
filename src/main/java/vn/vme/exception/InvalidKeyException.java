package vn.vme.exception;

public class InvalidKeyException extends RuntimeException {


    public InvalidKeyException(String message) {
        super(message);
    }
    public InvalidKeyException(String tenantKey, String status,String message) {
        super(message);
    }
}
