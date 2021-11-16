package vn.vme.common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class DateUtils {

    private static final Logger logger = LoggerFactory.getLogger(DateUtils.class);

    public DateUtils() {
    }
    
    public static String toDateString(String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        try {
            return dateFormat.format(new Date());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.print(e.getMessage());
            return "";
        }
    }

    public static String toString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(JConstants.DD_MM_YYYY);
        try {
            return dateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.print(e.getMessage());
            return "";
        }
    }

    public static String toStringHHmm(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(JConstants.HHmm);
        try {
            return dateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.print(e.getMessage());
            return "";
        }
    }

   
    /**
     * date to String By Format
     *
     * @param date
     * @param format
     * @return
     */
    public static String toString(Date date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        try {
            return dateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.print(e.getMessage());
            return "";
        }
    }
    public static String toDateDDMM(Date date) {
        if (date == null) {
            return "";
        }
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(JConstants.DDMM);
            return dateFormat.format(toLocalDate(date));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.print(e.getMessage());
            return null;
        }
    }

    public static Date toLocalDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, JConstants.HOUR_LOCAL);
        return calendar.getTime();
    }

    public static String toDateYYMMDD(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(JConstants.yyMMdd);
        try {
            return dateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.print(e.getMessage());
            return null;
        }
    }

    public static String toDateYYYYMMDD(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(JConstants.YYYYMMDD);
        try {
            return dateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.print(e.getMessage());
            return null;
        }
    }

    public static String toDateYYYY_MM(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(JConstants.YYYY_MM);
        try {
            return dateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.print(e.getMessage());
            return null;
        }
    }
    
    public static String toDateMM_YYYY(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(JConstants.MM_YYYY);
        try {
            return dateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.print(e.getMessage());
            return null;
        }
    }
    
    public static String toDateYYYYMMDDSSS(Date date) {

        SimpleDateFormat dateFormat = new SimpleDateFormat(JConstants.HHmmssSSSddMMyyyy);
        try {
            return dateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.print(e.getMessage());
            return null;
        }
    }
    
    public static String toDateYYYY_MM_DD(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(JConstants.YYYY_MM_DD);
        try {
            return dateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.print(e.getMessage());
            return null;
        }
    }

    public static Date toDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(JConstants.DD_MM_YYYY);
        java.util.Date date;
        try {
            date = dateFormat.parse(dateString);
            return date;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.print(e.getMessage());
            return null;
        }
    }

    public static Date toDateYYYYMMDD(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(JConstants.YYYY_MM_DD);
        java.util.Date date;
        try {
            date = dateFormat.parse(dateString);
            return date;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.print(e.getMessage());
            return null;
        }
    }

    /**
     * Convert DateString by format 20210120T092414Z
     *
     * @param dateString
     * @param format
     * @return Date
     */
    public static Date toDate(String dateString, String format) {
        // Remove offset
        if (dateString.contains(".") && dateString.endsWith("Z")) {
            dateString = dateString.substring(0, dateString.indexOf(".")) + "Z";
        }
        //2013-11-23T13:59
        if (dateString.length() == 15) {
            dateString = dateString + ":00Z";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        Date date;
        try {
            date = dateFormat.parse(dateString);
            return date;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.print(e.getMessage());
            return null;
        }
    }

    public static Date toDateGMT(String dateString, String format) {
        if (dateString.contains(".") && dateString.endsWith("Z")) {
            dateString = dateString.substring(0, dateString.indexOf(".")) + "Z";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date;
        try {
            date = dateFormat.parse(dateString);
            return date;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.print(e.getMessage());
            return null;
        }
    }

    public static String livingTime(Date fromDate, Date toDate) {
        long diff = Math.abs(toDate.getTime() - fromDate.getTime());

        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;
        // long diffDays = diff / (24 * 60 * 60 * 1000);
        if (diffHours == 0) {
            return diffMinutes + ":" + diffSeconds;
        } else {
            return diffHours + ":" + diffMinutes + ":" + diffSeconds;
        }
    }

    public static String duringTime(Date fromDate, Date toDate) {
        long diff = Math.abs(toDate.getTime() - fromDate.getTime());

        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;
        // long diffDays = diff / (24 * 60 * 60 * 1000);
        if (diffHours == 0) {
            return diffMinutes + " phút " + diffSeconds + " giây";
        } else {
            return diffHours + " giờ " + diffMinutes + " phút ";
        }
    }
    public static int yearAgo(Date birthDate) {
        if ((birthDate != null)) {
        	long currentTime = System.currentTimeMillis();
            Calendar now = Calendar.getInstance();
            now.setTimeInMillis(currentTime);
       
            Calendar birthDay = Calendar.getInstance();
            birthDay.setTimeInMillis(birthDate.getTime());
            
            //Get difference between years
            return now.get(Calendar.YEAR) - birthDay.get(Calendar.YEAR);
        } else {
            return 0;
        }
    }
    public static long getDifferenceDays(Date d1, Date d2) {
	    long diff = d2.getTime() - d1.getTime();
	    return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
	}
    public static String getTimeAgo(Date fromDate) {
        long diff = Math.abs(new Date().getTime() - fromDate.getTime());

        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;
        // long diffDays = diff / (24 * 60 * 60 * 1000);
        if (diffHours == 0) {
            if (diffMinutes == 0) {
                return diffSeconds + " giây trước";
            } else {
                return diffMinutes + " phút trước";
            }
        } else {
            return diffHours + " giờ trước";
        }
    }

    //yyyy-mm-dd
    public static String addStartTime(String from) {
        if (from != null && from.trim().length() >= 10) {
            from = from.substring(0, 10) + " 00:00:00";
        }
        return from;
    }

    //yyyy-mm-dd plus 1 day 
    public static String addEndTime(String to) {
    	Date toDate = toDate(to,JConstants.YYYY_MM_DD);
    	Date addDate = addDate(toDate,1);
    	to = formatDate(addDate, JConstants.YYYY_MM_DD);
        if (to != null && to.trim().length() >= 10) {
            to = to.substring(0, 10) + " 00:00:00";
        }
        return to;
    }

    /**
     * GMT format ago day
     *
     * @param ago
     * @return Date
     */
    public static Date getDateGMT(int ago) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, ago);
        return calendar.getTime();

    }

    /**
     * Get GMT now
     *
     * @return
     */
    public static Date getNowGMT() {
        try {
            SimpleDateFormat dateFormatGmt = new SimpleDateFormat(JConstants.TIME_ZONE);
            dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));// -06:00 and +7
            // Local time zone
            SimpleDateFormat dateFormatLocal = new SimpleDateFormat(JConstants.TIME_ZONE);
            // Time in GMT
            return dateFormatLocal.parse(dateFormatGmt.format(new Date()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get Local system now
     *
     * @return
     */
    public static Date getNow() {
        try {
            Calendar calendar = Calendar.getInstance();
            return calendar.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Compare Date is today
     *
     * @param dateTime
     * @return
     */
    public static boolean isToday(Date dateTime) {
        return toString(getNowGMT()).substring(0, 10).equals(toString(dateTime).substring(0, 10));
    }

    /**
     * addDate to now
     *
     * @param day
     * @return
     */
    public static Date addDate(int day) {
        return addDate(new Date(), day);
    }
    
   
    public static Date addMonth(Date date, int month) {
    	 Calendar calendar = Calendar.getInstance();
         calendar.setTime(date);
         calendar.add(Calendar.MONTH, month);
         return calendar.getTime();
    }
    public static String toMonth(Date date, int month) {
   	 Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, month);
        return toString(calendar.getTime());
   }
    public static int getMonth(Date date) {
      	 Calendar calendar = Calendar.getInstance();
           return calendar.get(Calendar.MONTH);
      }
    public static Date addYear(Date date, int year) {
   	 Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, year);
        return calendar.getTime();
   }

    public static Date addDate(Date date, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, day);
        return calendar.getTime();

    }

    /**
     * Get add minutes from specified date
     *
     * @param date
     * @param minutes
     * @return
     */
    public static Date addMinutes(Date date, int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minutes);
        return calendar.getTime();
    }

    public static Date addMinutes(int minutes) {
        return addMinutes(new Date(), minutes);
    }

    public static Date addSeconds(int seconds) {
        return addSeconds(new Date(), seconds);
    }

    public static Date addSeconds(Date date, int seconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, seconds);
        return calendar.getTime();
    }

    public static String toNow() {
        return toString(new Date(), JConstants.yyyyMMddHHmmss);
    }

    public static Long getTimeNow() {
        return new Date().getTime();
    }

    public static List<String> getListDate(String fromDate, String toDate) {

        List<String> lst = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = df.parse(fromDate);
            calendar.setTime(date);
            while (df.format(calendar.getTime()).compareTo(toDate) <= 0) {
                lst.add(df.format(calendar.getTime()));
                calendar.add(Calendar.DAY_OF_MONTH, 1);

            }
        } catch (ParseException e) {
            logger.error("error get list date from_date =" + fromDate + " to_date=" + toDate, e);
        }
        return lst;
    }

//    public List<Long> getListHourLong(String fromDate, String toDate) {
//        List<Long> lst = new ArrayList<>();
//        try {
//            Long fromDateL = getStartTime(fromDate);
//            long toDateL = getEndTime(toDate);
//            for (long i = fromDateL; i <= toDateL; i += 3600000) {
//                lst.add(i);
//            }
//        } catch (ParseException e) {
//            logger.error("error get list date from_date =" + fromDate + " to_date=" + toDate, e);
//        }
//        return lst;
//    }

//    public List<Long> getListDateLong(String fromDate, String toDate) {
//        List<Long> lst = new ArrayList<>();
//        try {
//            Long fromDateL = getStartTime(fromDate);
//            long toDateL = getEndTime(toDate);
//            for (long i = fromDateL; i <= toDateL; i += 86400000) {
//                lst.add(i);
//            }
//        } catch (ParseException e) {
//            logger.error("error get list date from_date =" + fromDate + " to_date=" + toDate, e);
//        }
//        return lst;
//    }

    public static List<String> getListDateByHourRange(String fromDate, String toDate, Integer timeDelta) {

        List<String> lst = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH");
        try {
            Date date = df.parse(fromDate);
            calendar.setTime(date);
            while (df.format(calendar.getTime()).compareTo(toDate) <= 0) {
                lst.add(df.format(calendar.getTime()));
                calendar.add(Calendar.HOUR_OF_DAY, timeDelta);
            }
        } catch (ParseException e) {
            logger.error("error get list date from_date =" + fromDate + " to_date=" + toDate, e);
        }
        return lst;
    }

    public static String convertTimeLong(Long time, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(new Date(time));
    }

    public static List<String> getListHourly(Long startTime, int range) {
        String format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat df = new SimpleDateFormat(format);
        String hourString = convertTimeLong(startTime, format);
        List<String> lst = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();

        try {
            calendar.setTime(df.parse(hourString));
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            for (int i = 0; i < range; i++) {
                lst.add(df.format(calendar.getTime()));
                calendar.add(Calendar.HOUR_OF_DAY, 1);
            }
        } catch (ParseException e) {
            logger.error("error get list hour hour =" + hourString + " delta=" + range, e);
        }
        return lst;
    }

    public static Long timeLongToHour(Long timeLong) {
        long tmp = timeLong % (60 * 60 * 1000);
        return timeLong - tmp;
    }

    public static String convertHourLong(Long time) {
        Date date = new Date(time);
        DateFormat formatter = new SimpleDateFormat("HH");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        return formatter.format(date);
    }


    public static String formatDate(Date date, String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }
    
    public static Date getFirstDayOfLastMonth() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);
        c.set(Calendar.DAY_OF_MONTH, 1);
        return c.getTime();
    }
    
    public static Date getLastDayOfLastMonth() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        return c.getTime();
    }
    
    
}
