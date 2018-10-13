package com.jcoder.kanuma.todolist.Extras;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.jcoder.kanuma.todolist.AddTodo;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment {

    private Calendar c;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        c=Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month =c.get(Calendar.MONTH);
        int day=c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener) AddTodo.newInstance(),year,month,day);
    }
}
