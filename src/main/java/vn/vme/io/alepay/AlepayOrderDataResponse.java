package vn.vme.io.alepay;

import lombok.Data;

@Data
public class AlepayOrderDataResponse {
    private String tokenKey;
    private String orderCode;
    private String customMerchantId;
    private int amount;
    private String currency;
    private String orderDescription;
    private Integer totalItem;
    //    private Integer checkoutType;
//    private boolean installment;
//    private Integer month;
//    private String bankCode;
//    private String paymentMethod;
    private String returnUrl;
    private String cancelUrl;
    private String buyerName;
    private String buyerEmail;
    private String buyerPhone;
    private String buyerAddress;
    private String buyerCity;
    private String buyerCountry;
    //    private String paymentHours;
//    private String promotionCode;
//    private boolean allowDomestic;
    private String language;
    private String signature;
}
