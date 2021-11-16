package com.mtech.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;

import vn.vme.common.DateUtils;

public class Test {

	public static void main(String[] args) {
		LocalDate now = new LocalDate();
		LocalDate monday = now.withDayOfWeek(DateTimeConstants.MONDAY);
		System.out.println(monday);
		System.out.println(now);
		System.out.println(now.getDayOfMonth());
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date d1 = sdf.parse(monday.toString());
			Date d2 = sdf.parse(now.toString());
			long daysBetween = DateUtils.getDifferenceDays(d1, d2);
			System.out.println("Days: " + daysBetween);
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

}
