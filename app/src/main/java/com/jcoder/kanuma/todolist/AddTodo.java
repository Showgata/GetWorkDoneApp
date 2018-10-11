package com.jcoder.kanuma.todolist;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.jcoder.kanuma.todolist.Extras.TimePickerFragment;

import java.text.DateFormat;
import java.util.Calendar;

public class AddTodo extends AppCompatDialogFragment implements TimePickerDialog.OnTimeSetListener {

    private EditText todoTitle;
    private EditText todoDesc;
    private Button saveBtn;
    private Button cancelBtn;
    private ImageButton timePicker;
    private TextView timePickerView;
    private Calendar c=null;
    private static AddTodo fragment;

    private static final String TAG = "AddTodo";


    public static AddTodo newInstance() {

        if(fragment==null) {
            fragment = new AddTodo();
        }
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final AlertDialog.Builder builder =new AlertDialog.Builder(getActivity());

        View view = getActivity().getLayoutInflater()
                        .inflate(R.layout.snippet_add_todo,null);



        builder.setView(view);

        todoTitle =view.findViewById(R.id.todoTitle);
        todoDesc=view.findViewById(R.id.todoDesc);
        saveBtn=view.findViewById(R.id.saveBtn);
        cancelBtn=view.findViewById(R.id.cancelBtn);
        timePickerView=view.findViewById(R.id.timePicked_textview);
        timePicker=view.findViewById(R.id.timePicker);


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(c!=null){ startRemainder(c);}
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddTodo.this.dismiss();
            }
        });


        timePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimePicker();
            }
        });


        return builder.create();
    }


    @Override
    public void onStart() {
        super.onStart();

        Window window = getDialog().getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
    }


    public void openTimePicker()
    {
        DialogFragment tp =new TimePickerFragment();
        tp.show(getActivity().getSupportFragmentManager(),"time picker");
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {


        c= Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY,hourOfDay);
        c.set(Calendar.MINUTE,minute);
        c.set(Calendar.SECOND,0);

        Log.i(TAG, "onTimeSet: "+c.getTime());

        String s ="Remind Me At :\n"+ DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());
        timePickerView.setText(s);
    }

    public boolean startRemainder(Calendar c)
    {
        AlarmManager manager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent i =new Intent(getActivity(),AlarmReceiver.class);
        PendingIntent pdi = PendingIntent.getBroadcast(getActivity(),1,i,0);

        manager.setExact(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),pdi);
        return true;
    }

    public void cancelRemainder()
    {

        AlarmManager manager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent i =new Intent(getActivity(),AlarmReceiver.class);
        PendingIntent pdi = PendingIntent.getBroadcast(getActivity(),1,i,0);

        manager.cancel(pdi);
        timePickerView.setText("");
    }


}
