package com.app.narlocks.delivery_service_app.extras;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Extra {

    public static Date toDate(String dateStr, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        try {
            return formatter.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String dateToString(Date date, String format){
        try {
            Format formatter = new SimpleDateFormat(format);
            return formatter.format(date);
        } catch (Exception ex) {
            return "-";
        }
    }

}
