package vn.vme.exception;

public class InvalidDataException extends RuntimeException {


    public InvalidDataException(String message) {
        super(message);
    }
    public InvalidDataException(String tenantKey, String status,String message) {
        super(message);
    }
}
