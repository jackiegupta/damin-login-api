package vn.vme.exception;

public class InvalidStatusTransactionException extends RuntimeException {


    public InvalidStatusTransactionException(String message) {
        super(message);
    }
    public InvalidStatusTransactionException(String tenantKey, String status,String message) {
        super(message);
    }
}
