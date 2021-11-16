package vn.vme.io.alepay;

import lombok.Data;

import java.math.BigInteger;

@Data
public class OrderRequest {
    private String amount;
    private String bankCode;
    private String cancelUrl;
    private String returnUrl;
}
