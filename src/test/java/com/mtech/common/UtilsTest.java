package com.mtech.common;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
@RunWith(SpringRunner.class)
public class UtilsTest {

	
	
	@Test
	public void testGenTenantKey() throws Exception {
		String tenantKey = RandomStringUtils.randomAlphanumeric(20);
	    System.out.println(tenantKey);
	}
	
	@Test
	public void testLocaleCurrencyFormat() throws Exception {
		Long number = 15000000000L;
		   NumberFormat format = NumberFormat.getCurrencyInstance(Locale.KOREA);
	        String currency = format.format(number).replaceFirst("€", "đ");
	        System.out.println("Currency in JAPAN : " + currency);
	        DecimalFormat df = new DecimalFormat("###,###"); // or pattern "###,###.##$"
	        System.out.println(df.format(number));
	}
	
	
	@Test
	public void testGenKey() throws Exception {
		System.out.println(RandomStringUtils.randomAlphanumeric(12));
		System.out.println(RandomStringUtils.randomAlphanumeric(20));
	}
	@Test
	public void testHash() {
		//System.out.println(Utils.getHash("Pass!234"));
		System.out.println(new BCryptPasswordEncoder().encode("Pass!234"));
		
	}
}
