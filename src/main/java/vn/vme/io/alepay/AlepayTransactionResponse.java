package vn.vme.io.alepay;

import lombok.Data;

@Data
public class AlepayTransactionResponse {
    private String code;
    private String message;
    private String transactionCode;
    private String orderCode;
    private double amount;
    private String currency;
    private String buyerEmail;
    private String buyerPhone;
    private String cardNumber;
    private String buyerName;
    private String status;
    private String reason;
    private String description;
    private boolean installment;
    private boolean is3D;
    private Integer month;
    private String bankCode;
    private String bankName;
    private String method;
    private Long transactionTime;
    private Long successTime;
    private String bankHotline;
    private double merchantFee;
    private double payerFee;
    private String signature;

}
