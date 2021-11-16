package vn.vme.common;

public class JConstants {

	public static final String DOMAIN = "gaka.vn";
	public static final String APP_NAME = "gaka";
	public static final String TEST_VERIFY = "123123";
	public static final String TEST_PASSWORD = "Pass!234";
	public static final Long ADMIN_ID = 95L;
	public static final int TID = 1000;
	public static final int VERIFY_KEY_EXPIRATION = 3;//minutes
	public static final int VERIFY_EXCEED = 5;
	// For test case JUNIT
	public static final String SCHEMA = "_schema";
	
	public static final String GET_EVENT = "GET";
	
	public static final String CREATE_EVENT = "CREATE";
	public static final String UPDATE_EVENT = "UPDATE";
	public static final String DELETE_EVENT = "DELETE";
	
	public static final String VERIFY_EMAIL_EVENT = "VERIFY_EMAIL";
	public static final String VERIFY_PHONE_EVENT = "VERIFY_PHONE";
	public static final String CHANGE_BALANCE_EVENT = "CHANGE_BALANCE";
	public static final String DEPOSIT_EVENT = "DEPOSIT";
	public static final String COMING_EVENT = "COMING";
	public static final String LIVE_EVENT = "LIVE";
	public static final String COMPLETE_EVENT = "COMPLETE";
	public static final String CONFIRM_EVENT = "CONFIRM_OTP";
	
	public static final String USER_API = "user-api";
	public static final String SIM_API = "game-api";
	public static final String NOTIFY_API = "notify-api";
	public static final String PAYMENT_API = "payment-api";
	public static final String FINANCE_API = "finance-api";
	public static final String LENDING_API = "lending-api";
	public static final String AUCTION_API = "auction-api";
	
	public static final String PLAN_FREE_ID = "FREE";
	
	public static final String MD5 = "MD5withRSA";
	public static final String SHA1 = "MD5withRSA";
	public static final String BID_DELAY = "bid-delay_";
	
	public static final String STREAM_ID_PRE = "VIETLINK";
	
	public enum Social {
		FACEBOOK, GOOGLE
	}
	
	public enum TourStatus{
		ACTIVE("ĐANG HOẠT ĐỘNG"),
		COMING("SẮP DIỄN RA"),// doing email, marketing, mời member tham gia
		FINISHED("KẾT THÚC");
		public final String value;
		TourStatus(String value) { this.value = value; }
	}
	public enum TourType{
		POINT("Tích điểm"),
		GROUP("Đấu bảng");
		public final String value;
		TourType(String value) { this.value = value; }
	}
	
	public enum EventStatus{
		ACTIVE("ĐANG HOẠT ĐỘNG"),
		COMING("SẮP DIỄN RA"),// doing email, marketing, mời member tham gia
		LIVE("SẮP ĐẤU GIÁ"),// khởi động trước bidding. user có thể dự đoán, trước 10 phút
		FINISHED("KẾT THÚC"), 
		COMPLETED("HOÀN THÀNH"), 
		DELAY("TẠM HOÃN"),
		INACTIVE("CHƯA XÁC NHẬN"),
		CANCEL("HỦY BỎ");

		public final String value;
		EventStatus(String value) { this.value = value; }
	}
	
	public enum GameType{
		NEW ("GAME MỚI"),
		HOT ("GAME HOT"),
		APP ("GAME APP"),
		PLAY ("GAME CHƠI NGAY");
		public final String value;
		GameType(String value) { this.value = value; }
	}
	public enum Scope {
		EVENT, GAME
	}
	public enum PoolType	{
		ALL ("Chung"),
		SINGLE ("Riêng"),
		MULTI ("Nhiều lần");
		public final String value;
		PoolType(String value) { this.value = value; }
	}
	
	
	 public enum PeriodType {
		DAILY(""), 
		WEEKLY(""), 
		MONTHLY(""), 
		QUARTER(""), 
		YEARLY("");
	    public final String value;
	    PeriodType(String value) {
	       this.value = value;
	    }
	 }
	public static enum NotifyStatus {
        INIT("INIT"),
        SEND("Đã SEND"),
        READ("Đã đọc"),
        FAIL("Gửi lỗi"),
        RESEND("Gửi lại");
        public final String value;

        NotifyStatus(String value) {
            this.value = value;
        }
    }
    public static enum NewsStatus {
        INIT("INIT"),
        ACTIVE("Active"),
        PUBLISH("Publish"),
        INACTIVE("INACTIVE");
        public final String value;

        NewsStatus(String value) {
            this.value = value;
        }
    }
    public static enum NewsType {
        PRIVATE("PRIVATE"),
        PUBLIC("PUBLIC");
        public final String value;
        NewsType(String value) {
            this.value = value;
        }
    }
    public static enum NotifyType {
        PRIVATE("Một user"),
        PUBLIC("Tất cả");
        public final String value;

        NotifyType(String value) {
            this.value = value;
        }
    }
	public enum Env{
		local, 
		dev,
		stg,
		prod,
		
	}
	
	public static enum Roles{
		NA("NA"),// +1
		ROOT("ROOT"),
		ADMIN("ADMIN"),
		GM("GM"),
		SUPPORT("SUPPORT"),
		USER("USER");
		public final String value;

		Roles(String value) {
            this.value = value;
        }
	}
	 public enum Telco {
	        Viettel, Vinaphone, Mobifone, Vietnamobile
	    }

	public enum UserLevel{
		//bronze, silver, gold, platinum
		BRONZE("Đồng"),
		SILVER("Bạc"),
		GOLD("Vàng"),
		PLATIN("Bạch kim");
		public final String value;
	    UserLevel(String value) { this.value = value; }
	}
	public enum Vip{
		//bronze, silver, gold, platinum
		VIP1("VIP1"),
		VIP2("VIP2"),
		VIP3("VIP3"),
		VIP4("VIP4"),
		VIP5("VIP5"),
		VIP6("VIP6"),
		VIP7("VIP7");
		public final String value;
		Vip(String value) { this.value = value; }
	}
	public enum CurrencyType{
		MONEY("Tiền"),
		EXP("Điểm kinh nghiệm"),
		RICE("Gạo"),
		SEED("Thóc"),
		POINT("Điểm");
	   public final String value;
	   CurrencyType(String value) {this.value = value;}
	}
	public enum UserType{
		PERSONAL("PERSONAL"), 
		ADMIN("ADMIN");
		public final String value;
		UserType(String value) {
			this.value = value;
		}
	}
	
	public static enum Status{
		INIT("INIT"), 
		ACTIVE("ĐANG HOẠT ĐỘNG"), 
		INACTIVE("CHƯA KÍCH HOẠT"), 
		VERIFIED("ĐÃ XÁC THỰC"),
		UNVERIFY("CHƯA XÁC THỰC"),
		BLOCK("KHÓA"), 
		SUCCESS("THÀNH CÔNG"),
		ERROR("LỖI") ; 
		public final String value;
		Status(String value) {this.value = value;}
	}
	public static enum UserStatus{
		ACTIVE("ĐANG HOẠT ĐỘNG"), 
		INACTIVE("CHƯA KÍCH HOẠT"), 
		UNVERIFY("CHƯA XÁC THỰC");
		public final String value;
		UserStatus(String value) {this.value = value;}
	}
	public static enum MemberStatus{
		UNVERIFY("CHƯA XÁC THỰC"),
		VERIFIED("ĐÃ XÁC THỰC"),
		TRUSTED("TIN TƯỞNG");
		 public final String value;
		 MemberStatus(String value) {this.value = value;}
	}
	
	public static enum BankInfo{
		TCB("Cong ty co phan VietLink Jsc, Số tài khoản 135792468123, Ngân hàng TechcomBank");
		public final String value;
		BankInfo(String value) {this.value = value;}
	}
	
	public static enum TransactionType{
		TRANSFER("TRANSFER"), 
		WITHDRAW("WITHDRAW"), 
		DEPOSIT("DEPOSIT"),
		BUYGAME("Phí mua game"),
		BUYITEM("Phí mua vật phẩm"),
		ATTEND("Điểm danh"),
		BONUS("Thưởng"),
		GIFTCODE("Điểm Giftcode"),
		EXCHANGERICE("Đổi gạo lấy thóc"),
		FEE("FEE"),
		TASK("Nhiệm vụ");
		public final String value;
		TransactionType(String value) {this.value = value;}
	}
	public static enum TypeChange{
		PLUS("PLUS"), 
		MINUS("MINUS");
		public final String value;
		TypeChange(String value) {this.value = value;}
	}
	
	public static enum TransactionStatus{
		CREATED("CREATED"), 
		REDIRECT("REDIRECT"), 
		APPROVED("APPROVED"), 
		COMPLETED("COMPLETED"), 
		FAILED("FAILED"),
		AUTHORIZED("AUTHORIZED");
		public final String value;
		TransactionStatus(String value) {this.value = value;}
	}
	public static enum PaymentStatus{
		CREATED("CHỜ CHUYỂN TIỀN"),
		APPROVED("ĐÃ NHẬN TIỀN"), 
		COMPLETED("ĐÃ HOÀN THÀNH"), 
		TIMEOUT("TIMEOUT"),
		FAILED("BỊ LỖI");
		public final String value;
		PaymentStatus(String value) {this.value = value;}
	}
	public static enum RegistrationType{
		Web, 
		Mobile 
	}
	public static enum GameStatus{
		REGISTER("ĐÃ ĐĂNG KÝ"),
		PROCESS("ĐANG XÁC THỰC"),
		VERIFIED("ĐÃ XÁC THỰC"),
		AUCTION("ĐÃ LÊN SÀN ĐẤU GIÁ"),
		SUCCESS("ĐẤU GIÁ THÀNH CÔNG"),
		EXCHANGE("CHUYỂN ĐỔI"),
		FINISH("KẾT THÚC"),
		FAIL("KHÔNG THÀNH CÔNG");
		 public final String value;
		 GameStatus(String value) {this.value = value;}
	}
	public enum ResponseStatus{
		SUCCESS,
		FAILED
	}
	public enum PaymentType{
		DEPOSIT("Nạp tiền"),
		WITHDRAW("Trả sau"),
		CHARGE("Thanh toán");
		public final String value;
		PaymentType(String value) {this.value = value;}
	}
	public enum PayType{
		BEFORE("Trả trước"),
		AFTER("Trả sau"),
		REPAYMENT("Trả góp");
		public final String value;
		PayType(String value) {this.value = value;}
	}
	
	public enum ExchangeMethod{
		ASSIGN("Chủ game ủy quyền cho sàn"),
		SELLER("Chủ game tự chuyển đổi");
		public final String value;
		ExchangeMethod(String value) {this.value = value;}
	}
	
	public enum CityList {
		Hà_Nội("Hà Nội"), TP_Hồ_Chí_Minh("Tp.Hồ Chí Minh"), Can_Tho("Cần Thơ"), Đà_Nẵng("Đà Nẵng"),
		Hải_Phòng("Hải Phòng"), An_Giang("An Giang"), Bà_Rịa_Vũng_Tàu("Bà Rịa - Vũng Tàu"), Bắc_Giang("Bắc Giang"),
		Bắc_Kạn("Bắc Kạn"), Bạc_Liêu("Bạc Liêu"), Bắc_Ninh("Bắc Ninh"), Bến_Tre("Bến Tre"), Bình_Định("Bình Định"),
		Bình_Dương("Bình Dương"), Bình_Phước("Bình Phước"), Bình_Thuận("Bình Thuận"), Cà_Mau("Cà Mau"),
		Cao_Bằng("Cao Bằng"), Đắk_Lắk("Đắk Lắk"), Đắk_Nông("Đắk Nông"), Điện_Biên("Điện Biên"), Đồng_Nai("Đồng Nai"),
		Đồng_Tháp("Đồng Tháp"), Gia_Lai("Gia Lai"), Hà_Giang("Hà Giang"), Hà_Nam("Hà Nam"), Hà_Tĩnh("Hà Tĩnh"),
		Hải_Dương("Hải Dương"), Hậu_Giang("Hậu Giang"), Hòa_Bình("Hòa Bình"), Hưng_Yên("Hưng Yên"),
		Khánh_Hòa("Khánh Hòa"), Kiên_Giang("Kiên Giang"), Kon_Tum("Kon Tum"), Lai_Châu("Lai Châu"),
		Lâm_Đồng("Lâm Đồng"), Lạng_Sơn("Lạng Sơn"), Lào_Cai("Lào Cai"), Long_An("Long An"), Nam_Định("Nam Định"),
		Nghệ_An("Nghệ An"), Ninh_Bình("Ninh Bình"), Ninh_Thuận("Ninh Thuận"), Phú_Thọ("Phú Thọ"),
		Quảng_Bình("Quảng Bình"), Quảng_Ngãi("Quảng Ngãi"), Quảng_Ninh("Quảng Ninh"), Quảng_Trị("Quảng Trị"),
		Sóc_Trăng("Sóc Trăng"), Sơn_La("Sơn La"), Tây_Ninh("Tây Ninh"), Thái_Bình("Thái Bình"),
		Thái_Nguyên("Thái Nguyên"), Thanh_Hóa("Thanh Hóa"), Thừa_Thiên_Huế("Thừa Thiên Huế"), Tiền_Giang("Tiền Giang"),
		Trà_Vinh("Trà Vinh"), Tuyên_Quang("Tuyên Quang"), Vĩnh_Long("Vĩnh Long"), Vĩnh_Phúc("Vĩnh Phúc"),
		Yên_Bái("Yên Bái"), Phú_Yên("Phú Yên");

		public final String value;

		CityList(String value) {
			this.value = value;
		}
	}
	
			
	public static final String PAGE = "1";
	public static final String SIZE = "10";
	public static final String DATA_LIST = "list";
	public static final String DATA_TOTAL = "total";
	public static final String BEARER = "Bearer ";
	
	public static final String DDMM = "dd-MM";
    public static final String HHmm = "HH:mm";
    public static final String yyMMdd = "yyMMdd";
    public static final String YYYYMMDD = "yyyyMMdd";
    public static final String YYMMDDHHmm = "yyMMddHHmm";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YYYY_MM = "yyyy-MM";
    public static final String MM_YYYY = "MM-yyyy";
    public static final String YYYYMMDDHHmmss = "yyyy/MM/dd HH:mm:ss";
    public static final String HHmmYYYYMMDD = "HH:mm yyyy-MM-dd";
    public static final String DD_MM_YYYY = "dd-MM-yyyy";
    public static final String DD_MM_YY = "dd/MM/yy";
    public static final String yyMMddHHmm = "yyMMddHHmm";
    public static final String yyyyMMddHHmmss = "yyyyMMddHHmmss";
    public static final String TIME_ZONE = "yyyy-MM-dd'T'HH:mm:ss";//2017-07-27T06:43:04.541-07:00
    public static final String HHmmssSSSddMMyyyy = "HHmmssSSSddMMyyyy";
    //public static final String DEFAULT_GATEWAY = "https://devservice.vitapay.vn:9955/";
   // public static final String DEFAULT_USER_AVATAR = "https://devweb.vitapay.vn:9980/media/icon/default-avatar.png";
    public static final String DEFAULT_WALLET_TRANS_ICON = "https://devweb.vitapay.vn:9980/media/icon/trans-money.png";
    public static final String DEFAULT_NOTIFY_ICON = "https://devweb.vitapay.vn:9980/media/icon/vitapay-logo.png";
    public static int HOUR_LOCAL = 7;// GMT-06:00 to GMT+07:00

	public static final String USER_AGENT = "Mozilla/5.0";
	
	public static final String AUTH_HEADER = "Authorization";

	public static final Long ROOT = 100L;
	
	public static final String FIREBASE_ACCOUNT = "google/firebaseAccount.json";
	public static final String FIREBASE_URL = "https://vme-dev-default-rtdb.firebaseio.com";
	 public static final String NOTIFICATION_TOPIC_INFORMATION = "information";
	 public static final String NOTIFICATION_TOPIC_UPDATE_APP = "update_app";
	 public static final String NOTIFICATION_TOPIC_UPDATE_CONFIG = "update_config";
	 public static final String NOTIFICATION_TOPIC_BID = "bid";
	 public static final String NOTIFICATION_TOPIC_COMMENT = "comment";
	 public static final String NOTIFICATION_TOPIC_ROOM = "room";
	

	 public static final String NOTIFICATION_SUBSCRIBE_TOPIC_DEBUG = "VMEDebug";
	 public static final String NOTIFICATION_SUBSCRIBE_TOPIC_RELEASE = "VMERelease";
	 public static final String NOTIFICATION_SUBSCRIBE_TOPIC = NOTIFICATION_SUBSCRIBE_TOPIC_DEBUG;

	 public static final String NOTIFICATION_REQ_MESSAGE = "MESSAGE";
	 public static final String NOTIFICATION_REQ_ACTION = "ACTION";
	
	 
	 public static enum NotificationType{
        TEXT("TEXT"),
        HTML("HTML"),
        LINK("LINK"),
        ROOM("ROOM"),
        BUY("BUY"),
        SELL("SELL");
        public final String value;
        NotificationType(String value) {
            this.value = value;
        }
    }
	
}
