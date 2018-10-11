package com.jcoder.kanuma.todolist.Extras;

import java.text.DateFormat;
import java.util.Calendar;

public class DateTimeInfo {

    public static String getDateTime()
    {
        Calendar c = Calendar.getInstance();
        return DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
    }


}
