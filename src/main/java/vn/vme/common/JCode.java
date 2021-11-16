package vn.vme.common;

public class JCode {

	public static String SUCCESS ="000";
   //000 --> thành công, 0xx --> Error
	public static enum CommonCode {
		ACCESS_DENY("001"),
		REQUEST_INVALID("002"),
		NOT_FOUND("003"),
		CONFLICT("004"),
		PROCESSING("005"),
		SIGN_INVALID("006"),
		CONTACT_CUSTOMER_SUPPORT("008"),
        TIMEOUT("098"),
		SYSTEM_ERROR("099"), 
		;
        public final String code;
		CommonCode(String code) {
			this.code = code;
		}
    }
	
	//1xx khac 000
	public static enum UserCode {
		PHONE_EXISTED("101"),
		EMAIL_EXISTED("102"), 
		USER_NAME_EXISTED("103"), 
		IDENTITY_INVALID("104"), 
		NOT_ACTIVE("105"), 
		NOT_LINK_BANK("106"),
		RECEIVER_NOT_FOUND("107"),
		PASSWORD_INVALID("108"),
		USER_NAME_INVALID("109"),
		PIN_FAIL("110"),// LOGIN
		PIN_FAIL_WARN("111"),// LOGIN WARN
		PIN_FAIL_LOCK("112"),// LOGIN LOCK
		OTP_FAIL("113"),
		OTP_FAIL_WARN("114"),
		OTP_FAIL_LOCK("115"),
		USER_BLOCK("116"),
		BACK_TO_CREATE_TRANSACTION("199"),
		NOT_ENOUGHT_BALANCE("201"),
		NOT_MATCH("202"), 
		CODE_USED("203"), 
		CODE_EXISTED("204"), 
		;
		public final String code;

		UserCode(String code) {
			this.code = code;
		}
	}
   
}
