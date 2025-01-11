package com.nextrad.vietphucstore.utils;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class IdUtil {

    public String genId(double totalPrice, Date date) {
        String price = String.valueOf(totalPrice);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yy-MM-dd");
        String[] dateSplit = dateFormat.format(date).split("-");
        String dateStr = dateSplit[0] + dateSplit[1] + dateSplit[2];

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String[] timeSplit = timeFormat.format(date).split(":");
        String timeStr = timeSplit[0] + timeSplit[1] + timeSplit[2];

        return "DV" + price.substring(0, price.length() - 3) + dateStr + timeStr;
    }

    public String genCheckId() {
        return "CH" + genDateTimeId();
    }

    private String genDateTimeId() {
        SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm:ss.SSS");
        String[] dateTime = sdf.format(new Date()).split(" ");
        String[] date = dateTime[0].split("-");
        String[] time = dateTime[1].split(":");
        String[] millisecond = time[2].split("\\.");
        return date[0] + date[1] + date[2] + time[0] + time[1] + millisecond[0] + millisecond[1];
    }
}
