package vn.vme.io.alepay;

import lombok.Data;

@Data
public class TransactionInfoRequest {
    private String tokenKey;
    private String transactionCode;
    private String signature;
}
