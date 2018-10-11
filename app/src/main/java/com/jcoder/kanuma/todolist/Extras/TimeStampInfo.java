package com.jcoder.kanuma.todolist.Extras;

import android.annotation.TargetApi;
import android.arch.persistence.room.TypeConverter;
import android.os.Build;

import java.text.DateFormat;
import java.text.ParseException;

import java.util.Date;


public class TimeStampInfo {

    static DateFormat df= DateFormat.getDateInstance();

    @TypeConverter
    public static Date getDate(String value) {

        if (value != null) {
            try {
                return df.parse(value);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
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
