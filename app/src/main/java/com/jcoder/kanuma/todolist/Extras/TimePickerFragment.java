package com.jcoder.kanuma.todolist.Extras;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;

import com.jcoder.kanuma.todolist.AddTodo;

import java.util.Calendar;

/*Simple Time Picker Fragment*/
public class TimePickerFragment extends DialogFragment {



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Calendar c = Calendar.getInstance();
        int hour =c.get(Calendar.HOUR_OF_DAY);
        int min =c.get(Calendar.MINUTE);


        return new TimePickerDialog(getActivity(), (TimePickerDialog.OnTimeSetListener) AddTodo.newInstance()
                ,hour,min, DateFormat.is24HourFormat(getActivity()));
    }
}
