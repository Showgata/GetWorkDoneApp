package com.jcoder.kanuma.todolist.Extras;

import android.annotation.TargetApi;
import android.arch.persistence.room.TypeConverter;
import android.os.Build;

import java.text.DateFormat;
import java.text.ParseException;

import java.util.Calendar;
import java.util.Date;

/*Converts Long value into date object and vice versa.
* used by room */
public class TimeStampInfo {

    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }



}
