package vn.vme.controller;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import vn.vme.common.PaymentUtils;
import vn.vme.entity.User;
import vn.vme.io.alepay.*;
import vn.vme.io.rest360.User360;
import vn.vme.io.user.ListPackageRequest;
import vn.vme.repository.UserRepository;
import vn.vme.service.Rest360Service;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Slf4j
@RestController
@RequestMapping("/api")
public class AlepayPaymentController extends BaseController {

    @Autowired
    private Rest360Service rest360Service;

    @Autowired
    private UserRepository userRepository;

    @PostMapping(value = "/alepay/sendOrder")
    public ResponseEntity sendAlepayOrder(@RequestBody OrderRequest orderRequest, Authentication authentication) throws NoSuchAlgorithmException, InvalidKeyException {
        String username = authentication.getName();

        log.info("----------Send Alepay Order process----- User :" + username);

        User user = userRepository.findByUserName(username);
        String tokenKey = env.getProperty("alepay.tokenKey");
        String orderCode = PaymentUtils.randomOrderCode();
        String customMerchantId = env.getProperty("alepay.customMerchantId");
        int amount = Integer.valueOf(orderRequest.getAmount());
        String currency = env.getProperty("alepay.currency");
        String orderDescription = env.getProperty("alepay.orderDescription");
        Integer totalItem = 1;
//        Integer checkoutType = 0;
//        boolean installment = false;
//        Integer month = 0;
//        String bankCode = "";
//        String paymentMethod = "";
        String returnUrl = orderRequest.getReturnUrl();
        String cancelUrl = orderRequest.getCancelUrl();
        String buyerName = env.getProperty("alepay.default");
        String buyerEmail = null;
        if (user.getEmail() == null) {
            buyerEmail = null;
        } else {
            buyerEmail = user.getEmail();
        }
        String buyerPhone = "0368980597";
        String buyerAddress = "Hà Nội";
        String buyerCity = "Hà Nội";
        String buyerCountry = "Việt Nam";
//        String paymentHours = "";
//        String promotionCode = "";
//        boolean allowDomestic = false;
        String language = "vi";
        String endpoint = env.getProperty("alepay.baseUrl") + "/request-payment";

        StringBuilder data = new StringBuilder();
        data.append("amount=" + amount);
        data.append("&buyerAddress=" + buyerAddress);
        data.append("&buyerCity=" + buyerCity);
        data.append("&buyerCountry=" + buyerCountry);
        data.append("&buyerEmail=" + buyerEmail);
        data.append("&buyerName=" + buyerName);
        data.append("&buyerPhone=" + buyerPhone);
        data.append("&cancelUrl=" + orderRequest.getCancelUrl());
        data.append("&currency=" + currency);
        data.append("&customMerchantId=" + customMerchantId);
        data.append("&language=" + language);
        data.append("&orderCode=" + orderCode);
        data.append("&orderDescription=" + orderDescription);
        data.append("&returnUrl=" + orderRequest.getReturnUrl());
        data.append("&tokenKey=" + tokenKey);
        data.append("&totalItem=" + totalItem);

        System.out.println(data.toString());

        byte[] checksumKey = env.getProperty("alepay.checksumKey").getBytes();

        String signature = PaymentUtils.generateHmac256(data.toString(), checksumKey);

        AlepayOrderDataResponse dataResponse = new AlepayOrderDataResponse();
        dataResponse.setTokenKey(tokenKey);
        dataResponse.setOrderCode(orderCode);
        dataResponse.setCustomMerchantId(customMerchantId);
        dataResponse.setAmount(amount);
        dataResponse.setCurrency(currency);
        dataResponse.setOrderDescription(orderDescription);
        dataResponse.setTotalItem(totalItem);
//        dataResponse.setCheckoutType(checkoutType);
//        dataResponse.setInstallment(installment);
//        dataResponse.setMonth(month);
//        dataResponse.setBankCode(bankCode);
//        dataResponse.setPaymentMethod(paymentMethod);
        dataResponse.setReturnUrl(returnUrl);
        dataResponse.setCancelUrl(cancelUrl);
        dataResponse.setBuyerName(buyerName);
        dataResponse.setBuyerEmail(buyerEmail);
        dataResponse.setBuyerPhone(buyerPhone);
        dataResponse.setBuyerAddress(buyerAddress);
        dataResponse.setBuyerCity(buyerCity);
        dataResponse.setBuyerCountry(buyerCountry);
//        dataResponse.setPaymentHours(paymentHours);
//        dataResponse.setPromotionCode(promotionCode);
//        dataResponse.setAllowDomestic(allowDomestic);
        dataResponse.setLanguage(language);
        dataResponse.setSignature(signature);

        AlepayOrderResponse response = new AlepayOrderResponse(endpoint, dataResponse);

        return response(response);

    }

    @PostMapping(value = "/alepay/recievePayment")
    public ResponseEntity recievePayment(@RequestBody RecievePaymentRequest recievePaymentRequest) throws NoSuchAlgorithmException, InvalidKeyException {

        String transInfoUrl = env.getProperty("alepay.baseUrl") + "/get-transaction-info";

        StringBuilder rawData = new StringBuilder();
        rawData.append("tokenKey=" + env.getProperty("alepay.tokenKey"));
        rawData.append("&transactionCode=" + recievePaymentRequest.getTransactionCode());

        byte[] checksumKey = env.getProperty("alepay.checksumKey").getBytes();

        String signature = PaymentUtils.generateHmac256(rawData.toString(), checksumKey);

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);

        TransactionInfoRequest transactionInfoRequest = new TransactionInfoRequest();
        transactionInfoRequest.setTokenKey(env.getProperty("alepay.tokenKey"));
        transactionInfoRequest.setTransactionCode(recievePaymentRequest.getTransactionCode());
        transactionInfoRequest.setSignature(signature);

        HttpEntity<String> entity = new HttpEntity<>(new Gson().toJson(transactionInfoRequest), headers);

        AlepayTransactionResponse res = restTemplate.exchange(
                transInfoUrl, HttpMethod.POST, entity, new ParameterizedTypeReference<AlepayTransactionResponse>() {
                }).getBody();

        if (!res.getCode().equals("000")) {
            return new ResponseEntity(res, HttpStatus.FORBIDDEN);
        }

        User360 user = rest360Service.addRice(recievePaymentRequest.getAmount());

        return response(user);
    }

//    @PostMapping("/momo/sendOrder")
//    public ResponseEntity sendMomoOrder(@RequestBody MomoOrderRequest momoOrderRequest) throws NoSuchAlgorithmException, InvalidKeyException {
//        String partnerCode = env.getProperty("momo.partnerCode");
//        String accessKey = env.getProperty("momo.accessKey");
//        String requestId = PaymentUtils.getRequestId("VME");
//        String amount = momoOrderRequest.getAmount();
//        String orderId = PaymentUtils.randomOrderCode();
//        String orderInfo = env.getProperty("momo.orderInfo");
//        String redirectUrl = momoOrderRequest.getRedirectUrl();
//        String ipnUrl = momoOrderRequest.getIpnUrl();
//        String extraData = env.getProperty("momo.extraData");
//        String requestType = env.getProperty("momo.requestType");
//
//        StringBuilder data = new StringBuilder();
//        data.append("partnerCode=" + partnerCode);
//        data.append("&accessKey=" + accessKey);
//        data.append("&requestId=" + requestId);
//        data.append("&amount=" + amount);
//        data.append("&orderId=" + orderId);
//        data.append("&orderInfo=" + orderInfo);
//        data.append("&redirectUrl=" + redirectUrl);
//        data.append("&ipnUrl=" + ipnUrl);
//        data.append("&extraData=" + extraData);
//        data.append("&requestType=" + requestType);
//
//        System.out.println(data.toString());
//
//        byte[] secretKey = env.getProperty("momo.secretKey").getBytes();
//
//        String signature = PaymentUtils.generateHmac256(data.toString(), secretKey);
//
//        String captureWalletUri = "https://test-payment.momo.vn/gw_payment/transactionProcessor";
//
//        CaptureWalletRequest captureWalletRequest = new CaptureWalletRequest();
//        captureWalletRequest.setPartnerCode(partnerCode);
//        captureWalletRequest.setRequestId(requestId);
//        captureWalletRequest.setAmount(Long.valueOf(amount));
//        captureWalletRequest.setOrderId(orderId);
//        captureWalletRequest.setOrderInfo(orderInfo);
//        captureWalletRequest.setRedirectUrl(redirectUrl);
//        captureWalletRequest.setIpnUrl(ipnUrl);
//        captureWalletRequest.setRequestType(requestType);
//        captureWalletRequest.setExtraData(extraData);
//        captureWalletRequest.setLang(env.getProperty("momo.lang"));
//        captureWalletRequest.setSignature(signature);
//
//        System.out.println(new Gson().toJson(captureWalletRequest));
//
//        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
//
//        HttpEntity<String> entity = new HttpEntity<>(new Gson().toJson(captureWalletRequest) ,headers);
//
//        ResponseEntity<String> response = restTemplate.exchange(
//                captureWalletUri, HttpMethod.POST, entity, String.class);
//        return new ResponseEntity<>(response.getBody(), response.getStatusCode());
//    }
}
