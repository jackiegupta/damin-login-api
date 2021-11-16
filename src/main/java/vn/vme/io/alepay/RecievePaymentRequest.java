package vn.vme.io.alepay;

import lombok.Data;

@Data
public class RecievePaymentRequest {
    private String transactionCode;
    private double amount;
}
