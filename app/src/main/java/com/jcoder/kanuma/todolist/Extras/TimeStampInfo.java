package com.jcoder.kanuma.todolist.Extras;

import android.annotation.TargetApi;
import android.arch.persistence.room.TypeConverter;
import android.os.Build;

import java.text.DateFormat;
import java.text.ParseException;

import java.util.Calendar;
import java.util.Date;


public class TimeStampInfo {

    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }


//    public static String getCurrentDate()
//    {
//        Date date =new Date();
//        String dayOfTheWeek = (String) DateFormat.format("EEEE",date);
//        String day =(String) DateFormat.format("dd",date);
//        String month = (String) DateFormat.format("MMM",date);
//
//        return dayOfTheWeek+","+day+" "+month;
//    }


}
