package vn.vme.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.InvalidPropertyException;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;

import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import sun.security.provider.MD5;
import vn.vme.common.JConstants.UserLevel;
import vn.vme.common.JConstants.Vip;
import vn.vme.model.Paging;

public class Utils {
    protected static Logger log = LoggerFactory.getLogger(Utils.class.getName());

    public static String genRefId() {
        Long random = Long.valueOf(Utils.uniqueNumeric(4));
        return DateUtils.toDateString(JConstants.YYMMDDHHmm) + random;
    }
    public static String genReferCode(int n) {
    	String AlphaNumericString = "ABCDEFGHJKLMNPQRSTUVWXYZ" + "123456789";
    	StringBuilder sb = new StringBuilder(n);
		for (int i = 0; i < n; i++) {
			int index = (int) (AlphaNumericString.length() * Math.random());
			// add Character one by one in end of sb
			sb.append(AlphaNumericString.charAt(index));
		}
		return sb.toString();
    }
 // function to generate a random string of length n
    public static String genCode(int n) {
		String AlphaNumericString = "ABCDEFGHJKLMNPQRSTUVWXYZ" + "123456789";// + "abcdefghjklmnpqrstuvxyz";
		StringBuilder sb = new StringBuilder(n);
		for (int i = 0; i < n; i++) {
			int index = (int) (AlphaNumericString.length() * Math.random());
			// add Character one by one in end of sb
			sb.append(AlphaNumericString.charAt(index));
		}
		return sb.toString();
	}
    
    
	public static boolean isDigit(String str) {
		// Traverse the string from
		// start to end
		if(isEmpty(str)) {
			return false;
		}
		for (int i = 0; i < str.length(); ++i) {

			// Check if character is
			// digit from 0-9
			// then return true
			// else false
			if (str.charAt(i) >= '0' && str.charAt(i) <= '9') {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}
  
    
    /**
     * buildFormParam query
     *
     * @param requestMap
     * @return: pair key = parameter
     */
    public static List<NameValuePair> buildFormParam(Map<String, Object> requestMap) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        for (Map.Entry<String, Object> entry : requestMap.entrySet()) {
            Object value = entry.getValue();
            if (isNotEmpty(value)) {
                params.add(new BasicNameValuePair(entry.getKey(), String.valueOf(value)));
            }
        }
        return params;
    }

    public static Map<String, Object> buildFormParamURLEncoder(Map<String, Object> requestMap) throws UnsupportedEncodingException {
        Map<String, Object> params = new HashMap<String, Object>();
        for (Map.Entry<String, Object> entry : requestMap.entrySet()) {
            Object value = entry.getValue();
            if (isNotEmpty(value)) {
                params.put(entry.getKey(),
                        URLEncoder.encode(String.valueOf(String.valueOf(value)), "UTF-8"));
            }
        }
        return params;
    }

    public static  Map<String, String> buildMap(String propery, Object object, Map<String, String> param) {
    	if(Utils.isEmpty(object)) {
    		return param;
    	}
    		
    	  Map<String, Object> requestMap = new ObjectMapper().convertValue(object, Map.class);
			for (Map.Entry<String, Object> entry : requestMap.entrySet()) {
				Object value = entry.getValue();
				if (isNotEmpty(value) && !(value instanceof LinkedHashMap)) {
					param.put(propery + "." + entry.getKey(), String.valueOf(String.valueOf(value)));
				}
			}
          return param;
		
	}
    /**
     * buildFormParam query
     *
     * @throws UnsupportedEncodingException
     * @return: pair key = parameter
     */
    public static String buildQueryObject(Object object) throws UnsupportedEncodingException {
        Map<String, Object> requestMap = new ObjectMapper().convertValue(object, Map.class);
        return buildQueryParam(requestMap);

    }

    public static Map<String, Object> responseMap(List<?> responseList, Page result, Long total) {
        Map<String, Object> map = new HashMap<String, Object>();
		if (result.getSize() != 1) {
			map.put(JConstants.DATA_LIST, responseList);
		}
		map.put(JConstants.DATA_TOTAL, total);
		
        Paging paging = new Paging(result.getNumber() + 1, result.getSize());
        paging.setTotalPages(result.getTotalPages());
        paging.setTotalRows(result.getTotalElements());
        map.put(Paging.class.getSimpleName().toLowerCase(), paging);
        return map;
    }
    
    public static Map<String, Object> responseMap(List<?> responseList, Page result) {
        Map<String, Object> map = new HashMap<String, Object>();
		if (result.getSize() != 1) {
			map.put(JConstants.DATA_LIST, responseList);
		}
        Paging paging = new Paging(result.getNumber() + 1, result.getSize());
        paging.setTotalPages(result.getTotalPages());
        paging.setTotalRows(result.getTotalElements());
        map.put(Paging.class.getSimpleName().toLowerCase(), paging);
        return map;
    }

    public static String buildQueryParam(Map<String, Object> requestMap) throws UnsupportedEncodingException {
        List<NameValuePair> params = buildFormParam(requestMap);
        StringBuilder postData = new StringBuilder();
        for (NameValuePair nameValuePair : params) {
            if (postData.length() != 0) postData.append('&');
            postData.append(nameValuePair.getName());
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(nameValuePair.getValue()), "UTF-8"));
        }
        return postData.toString();
    }

    public static String getNextLevel(String level) {
    	UserLevel userLevel = UserLevel.valueOf(level);
    	switch (userLevel) {
		case BRONZE:
			return UserLevel.SILVER.name();
		case SILVER:
			return UserLevel.GOLD.name();
		case GOLD:
			return UserLevel.GOLD.name();
		default:
			return "";
		}
        
    }


    public static String getNextVip(String vip) {
    	Vip userVip = Vip.valueOf(vip);
    	switch (userVip) {
		case VIP1:
			return Vip.VIP2.name();
		case VIP2:
			return Vip.VIP3.name();
		case VIP3:
			return Vip.VIP3.name();
		default:
			return "";
		}
        
    }
   
   

    public static int random(int pow) {
        int limit = (int) Math.pow(10, pow - 1);
        SecureRandom secureRandom;
        try {
            secureRandom = SecureRandom.getInstance("SHA1PRNG");
            int secureNumber = secureRandom.nextInt(9 * limit) + limit;
            return secureNumber;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return 999;
        }
    }
    
    final static String Refund = "RF";
    public static String getRefundId(String id) {
        return Refund + id;
    }

    public static String genVerifyCode() {
        return String.valueOf(random(6));
    }

    public static String genVerifyingCode() {
        return String.valueOf(random(6));
    }

    public static String uniqueNumeric(int pow) {
        int limit = (int) Math.pow(10, pow - 1);
        SecureRandom secureRandom;
        try {
            secureRandom = SecureRandom.getInstance("SHA1PRNG");
            int secureNumber = secureRandom.nextInt(9 * limit) + limit;
            return String.valueOf(secureNumber);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String mask(String phone) {
    	if(Utils.isEmpty(phone)) {
    		return "";
    	}
        try {
            return mask(phone, 3, phone.length() - 3);
        } catch (Exception e) {
            return phone;
        }
    }

    public static String maskEmail(String email) {
    	if(isEmpty(email)){
    		return "";
    	}
    	int pos = email.indexOf("@");
    	if(pos <0) {
    		return "";
    	}
    	String sub1 = email.substring(0, pos);
    	String sub2 = email.substring(pos+1);
    	int mid1 = sub1.length()/2;
    	int mid2 = sub2.length()/2;
    	try {
			sub1 = mask(sub1,mid1,sub1.length());
			sub2 = mask(sub2,0,mid2);
		} catch (Exception e) {
			return email;
		}
        return sub1 + "@" + sub2;
    }
    
    public static String maskCard(String card) {
        try {
            return mask(card, 4, card.length() - 4);
        } catch (Exception e) {
            return card;
        }
    }

    public static String maskBank(String accountNumber) {
        try {
            return mask(accountNumber, 3, accountNumber.length() - 3);
        } catch (Exception e) {
            return accountNumber;
        }
    }

    public static String mask(String strText, int start, int end) throws Exception {

        if (strText == null || strText.equals(""))
            return "";

        if (start < 0)
            start = 0;

        if (end > strText.length())
            end = strText.length();

        if (start > end)
            throw new Exception("End index cannot be greater than start index");

        int maskLength = end - start;

        if (maskLength == 0)
            return strText;

        StringBuilder sbMaskString = new StringBuilder(maskLength);

        for (int i = 0; i < maskLength; i++) {
            sbMaskString.append("*");
        }

        return strText.substring(0, start) + sbMaskString.toString() + strText.substring(start + maskLength);
    }

    public static <T> String doPost(String url, Map<String, String> headers, T body) {
        CloseableHttpClient httpClient = null;
        HttpPost httpPost = null;
        CloseableHttpResponse response = null;
        StringBuffer responseString = new StringBuffer();

        try {

            httpClient = HttpClients.createDefault();
            httpPost = new HttpPost(url);

            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            if (headers != null) {
                headers.forEach((k, v) -> {
                    nvps.add(new BasicNameValuePair(k, v));
                });
            }
            if (body != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                String bodyAsString = objectMapper.writeValueAsString(body);

                StringEntity input = new StringEntity(bodyAsString);
                input.setContentType("application/json");
                httpPost.setEntity(input);
            }

            for (NameValuePair h : nvps) {
                httpPost.addHeader(h.getName(), h.getValue());
            }

            response = httpClient.execute(httpPost);
            BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));

            String output;
            while ((output = br.readLine()) != null) {
                responseString.append(output);
            }

            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException(
                        url + " Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return responseString.toString();
    }

    public static void downloadContentToFile(String url, String fileName) {
        try {

            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(url);

            request.setHeader("User-Agent", JConstants.USER_AGENT);
            HttpResponse response = client.execute(request);

            response.getEntity();
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            StringBuffer content = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                content.append(line);
            }
            BufferedWriter bw = null;
            FileWriter fw = null;

            try {

                fw = new FileWriter(fileName);
                bw = new BufferedWriter(fw);
                bw.write(content.toString());

            } catch (IOException e) {

                e.printStackTrace();

            } finally {

                bw.close();

                fw.close();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String getPhoneASCII(String env) {
        String digit = "";
        for (char ch : env.toCharArray()) {
            int ascii = (int) ch;
            digit += String.valueOf(ascii);
        }
        String phone = "05" + digit.substring(0, 9);
        return phone;
    }

    /**
     * @param rate:   5
     * @param amount: 10
     * @return 10#0.50
     */
    public static String getAmountFee(BigDecimal rate, BigDecimal amount) {
        BigDecimal getAmount = rate.multiply(amount);
        BigDecimal rateAmount = getAmount.setScale(2, BigDecimal.ROUND_HALF_DOWN);
        return amount + "#" + rateAmount;
    }

    /**
     * Return VND: 15,000,000 đ
     *
     * @param number
     * @return
     */
    public static String VND(Long number) {
        return vnd(number) + " đ";
    }
    public static String vnd(Long number) {
        DecimalFormat df = new DecimalFormat("###,###,###");
        return df.format(number);
    }
    public static boolean isLocal(String env) {
        return env.contains(JConstants.Env.local.name());
    }

    public static boolean isDev(String env) {
        return env.contains(JConstants.Env.dev.name());

    }

    public static boolean isProduct(String env) {
        return env.contains(JConstants.Env.prod.name());

    }

    public static boolean isEmpty(Object str) {
        return !isNotEmpty(str);
    }

    public static boolean isNotEmpty(Object str) {
        if (str == null || str.toString().trim().length() == 0 || str.toString().trim().equalsIgnoreCase("null") ) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Get format database name postgres from domain name
     *
     * @return
     */
    public static String getDbName(String domainName) {
        return domainName.replaceAll("\\.", "_");
    }

    public static Map<String, Object> buildPageSize(int page, int size) {
        Map<String, Object> map = new HashMap<String, Object>();
        Paging paging = new Paging(page, size);
        map.put(Paging.class.getSimpleName(), paging);
        return map;
    }

    public static String sortByKeyData(Map<String, Object> map) {

        LinkedHashMap<String, Object> sortedMap = new LinkedHashMap<>();
        map.entrySet().stream().sorted(Map.Entry.comparingByKey())
                .forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));
        String data = "";

        for (Map.Entry<String, Object> entry : sortedMap.entrySet()) {
            Object value = entry.getValue();
            if (isNotEmpty(value)) {
                data = data + value;
            }
        }

        return data;
    }


    /**
     * Copies properties from one object to another
     *
     * @param source
     * @return
     * @destination
     */
    public static void copyNonNullProperties(Object source, Object destination) {
        // getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();
        Set<String> emptyNames = new HashSet<String>();
        for (java.beans.PropertyDescriptor pd : pds) {
            // check if value of this property is null add fullName
            String name = pd.getName();
            if (name.equals("request") || name.equals("response")) {
                continue;
            }
            try {
                Object srcValue = src.getPropertyValue(name);
                if (Utils.isEmpty(srcValue) || srcValue.toString().contains("***")) {
                    emptyNames.add(name);
                }
            } catch (InvalidPropertyException e) {
                emptyNames.add(name);
                continue;
            }
        }
        String[] result = new String[emptyNames.size()];
        String[] ignoreProperties = emptyNames.toArray(result);
        BeanUtils.copyProperties(source, destination, ignoreProperties);
    }

    public static String phoneVN(String phoneNumber) {
        if (!phoneNumber.startsWith("+84") && phoneNumber.startsWith("0") && phoneNumber.startsWith("o")) {
            return phoneNumber.replaceFirst("0", "+84").replaceFirst("o", "+84");
        }
        return phoneNumber;
    }

    public static boolean isTest(Environment env) {
        return isLocal(env);
    }

    public static boolean isLocal(Environment env) {
        return isLocal(env.getActiveProfiles()[0]);
    }

    public static boolean isProduct(Environment env) {
        return isProduct(env.getActiveProfiles()[0]);
    }

    public static boolean isNotProduct(Environment env) {
        return !isProduct(env.getActiveProfiles()[0]);
    }

    public static boolean isDev(Environment env) {
        return isDev(env.getActiveProfiles()[0]);
    }

    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
    /**
     * Generate transaction ID by member Name
     *
     * @param userName
     * @return: mid_YY-MM-DD_HHMMSSSEQ
     */
    public static String generateId(String userName) {
        SimpleDateFormat dateFomrat = new SimpleDateFormat("yyMMddHHmmss");
        String floor = new DecimalFormat("#0000").format(Math.floor(Math.random() * 1000));
        return dateFomrat.format(new Date()) + floor;
    }

    public static String getTid(String transactionId) {
        return transactionId.replace(JConstants.APP_NAME, "");
    }

    public static String genTid() {
        Long random = Long.valueOf(Utils.uniqueNumeric(4));
        return DateUtils.toDateString(JConstants.yyMMdd) + random;
    }
    
      
    public static String genRefNumber(int length) {
        Calendar calendar = Calendar.getInstance();
        String day = String.format("%03d", calendar.get(Calendar.DAY_OF_YEAR));
        Long random = Long.valueOf(Utils.uniqueNumeric(length));
        int year = Calendar.getInstance().get(Calendar.YEAR) % 10;

        return year + day + random;
    }

    public static String appendTid() {
        return JConstants.APP_NAME + genTid();
    }

    public static String appendTid(String tid) {
        return JConstants.APP_NAME + tid;
    }

    public static boolean isValidISO(String input) {
        byte[] bytes = input.getBytes(StandardCharsets.ISO_8859_1);
        String output = new String(bytes, StandardCharsets.ISO_8859_1);
        return input.equals(output);
    }

    public static String readAsText(String resourceFile) {
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            BufferedReader reader = new BufferedReader(new InputStreamReader(classLoader.getResourceAsStream(resourceFile)));
            StringBuffer fileContentBuffer = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null) {
                fileContentBuffer.append(line);
                fileContentBuffer.append('\n');
            }
            reader.close();
            return fileContentBuffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    public static boolean checkCode(Environment env, String verifyCode) {
        return isTest(env) && verifyCode.equals(JConstants.TEST_VERIFY);
    }


    /**
     * 4,500,000 VND == 4,5
     *
     * @param currentPrice
     * @return
     */
    public static Long getRoundPriceM(Long currentPrice) {
        Long priceM = currentPrice / 1000000;
        if (currentPrice % 1000000 != 0) {
            priceM = priceM + 1;
        }
        return priceM;
    }

    public static String getIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (Utils.isEmpty(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        if (Utils.isEmpty(ipAddress)) {
            ipAddress = "0.0.0.0";
        }
        return ipAddress;
    }

    public static String gen6DigistsOtp(byte[] secret, long counter) throws NoSuchAlgorithmException, InvalidKeyException {
        // Signing Key
        SecretKeySpec signKey = new SecretKeySpec(secret, "HmacSHA1");

        // Preparing the byte array to sign
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(counter);

        // Signing the byte array
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(signKey);
        byte[] hash = mac.doFinal(buffer.array());

        // Determines the offset of recovering the last byte (20-th) and then performing a bitwise and with
        // 00000000 00000000 00000000 00001111
        int offset = hash[19] & 0xF;
        // Requpero the first element starting from the offset and performing a bitwise and with
        // 00000000 00000000 00000000 00000000 00000000 00000000 00000000 01111111
        long truncatedHash = hash[offset] & 0x7F;

        for (int the = 1; the < 4; the++) {
            // Perform the shift left 8 bits
            truncatedHash <<= 8;
            // Get the next element of the sequence, and performing a bitwise and with
            // 00000000 00000000 00000000 00000000 00000000 00000000 00000000 11111111
            truncatedHash |= hash[offset + the] & 0xFF;
        }

        //validate 6 digits
        long otpNumb = truncatedHash % 1000000;
        if ((otpNumb + "").length() != 6) {
            int length = (otpNumb + "").length();
            int add = 6 - length;
            if (add > 0) {
                for (int i = 0; i < add; i++) {
                    otpNumb = otpNumb * 10;
                }
            } else {
                for (int i = 0; i < add; i++) {
                    otpNumb = otpNumb / 10;
                }
            }
        }

        // Return the 6 digit least significant
        return otpNumb + "";
    }

    public static String hmacSha1(String data, String key) throws NoSuchAlgorithmException, InvalidKeyException {
        SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA1");

        Mac mac = Mac.getInstance("HmacSHA1");

        mac.init(signingKey);

        byte[] digest = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));

        return bytesToHex(digest);
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(Integer.toHexString((b & 0xFF) | 0x100), 1, 3);
        }
        return sb.toString();
    }
    /**
     * Nguyen 
     * @param fullName
     * @return
     */
    public static String getFirstName(String fullName) {
		if(Utils.isNotEmpty(fullName) && fullName.contains(" ")) {
			int pos = fullName.indexOf(" ");
			return fullName.substring(0,pos);
		}else {
			return fullName;
		}
	}
    /**
     * Van A
     * @param fullName
     * @return
     */
	public static String getLastName(String fullName) {
		return fullName.replace(getFirstName(fullName),"");
	}

	
	public static Map<String, Object> convertParammeter2Map(HttpServletRequest request){
		log.info("request.getQueryString()" + request.getQueryString());
		 Enumeration<String> paramNames = request.getParameterNames();
		 Map<String, Object> data  = new HashMap<String, Object>();
	      while(paramNames.hasMoreElements()) {
	         String key = (String)paramNames.nextElement();
	         String value = request.getParameter(key);
	         log.info("Param [" + key+ " = "+ value +"]");
	         data.put(key, value);
	      }
	      return data;
	}
	
	public static Map<String, Object> convertString2Map(String data){
		
	      try {
			return new ObjectMapper().readValue(data, Map.class);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new HashMap<String, Object>();
		}
	}

		
	public static String deAccent(String str) {
        String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD); 
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("").replace('\u0111', 'd').replace('\u0110', 'D');
    }
	
	public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

	
	public static boolean isPhoneNumber(String code) {
		return NumberUtils.isDigits(code) && code.startsWith("0") && code.length() == 10;
	}
	
	public static String genUuid() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
		
	}
	
	public static boolean checkContainSpecialCharacters(String input) {
		Pattern pattern = Pattern.compile("[^a-zA-Z0-9]");
		Matcher matcher = pattern.matcher(input);
		boolean isStringContainsSpecialCharacter = matcher.find();
		if(isStringContainsSpecialCharacter) {
			return true;
		}
		return false;
	}
	
	public static boolean isFullname(String input) {
		Pattern pattern = Pattern.compile("[^a-zA-Z]");
		Matcher matcher = pattern.matcher(input.replaceAll(" ", ""));
		boolean isStringContainsSpecialCharacter = matcher.find();
		if(isStringContainsSpecialCharacter) {
			return true;
		}
		return false;
	}

	public static boolean isTestPhoto(String fileName) {
		return isNotEmpty(fileName) && (fileName.contains("avatar.png") || fileName.contains("1.jpg"));
	}
	
	public static String convertObjectToJSon(Object object) {
		String json = "";
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
			// convert java object to JSON
			json = mapper.writeValueAsString(object);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return json;
	}
	
}