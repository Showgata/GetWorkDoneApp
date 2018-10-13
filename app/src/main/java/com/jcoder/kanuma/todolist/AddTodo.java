package com.jcoder.kanuma.todolist;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.jcoder.kanuma.todolist.Database.TodoViewModel;
import com.jcoder.kanuma.todolist.Database.Todos;
import com.jcoder.kanuma.todolist.Extras.DatePickerFragment;
import com.jcoder.kanuma.todolist.Extras.TimePickerFragment;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class AddTodo extends AppCompatDialogFragment implements TimePickerDialog.OnTimeSetListener,DatePickerDialog.OnDateSetListener {

    private EditText todoTitle;
    private EditText todoDesc;
    private Button saveBtn;
    private Button cancelBtn;
    private Button timePicker;
    private CheckBox notifyMeDailyCb;
    private TextView timePickerView;
    private Calendar c=null;
    private static AddTodo fragment;
    private TodoViewModel todoViewModel;
    private static final String TAG = "AddTodo";
    public static final String NOTIFICATION_TITLE="notification title";
    public static final String NOTIFICATION_DESC="notification desc";
    public static final String BUNDLE_NAME="content bundle";
    private boolean ringDaily=false;
    /*ringDaily = false means reminder rings only once
        ringDaily =true means reminder rings daily at that time
     */

    public static AddTodo newInstance() {

        if(fragment==null) {
            fragment = new AddTodo();

        }
        return fragment;
    }

    public void setTodoViewModel(TodoViewModel todoViewModel)
    {
        this.todoViewModel=todoViewModel;
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
        notifyMeDailyCb=view.findViewById(R.id.daily_cb);


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndSave();
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
                openDatePicker();
            }
        });


        notifyMeDailyCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                ringDaily = isChecked;
            }
        });


        return builder.create();
    }

    private void validateAndSave()
    {
        String title = todoTitle.getText().toString().trim();
        String desc= todoDesc.getText().toString().trim();
        Todos todos;

        if(TextUtils.isEmpty(title)){
            todoTitle.setError("This field cannot be empty");
            return;
        }else {
                Calendar cal = Calendar.getInstance();
                todos =new Todos(title,desc,cal.getTime());
            }


        if(c!=null){
            final int random = new Random().nextInt(10000000)+100000;
            todos.setDateRemainder(c.getTime());
            todos.setRingDaily(ringDaily);
            todos.setAlarmUniqueId(random);
            startRemainder(c,title,random,ringDaily,getActivity());
            Toast.makeText(getActivity(),"Alarm Set For :"+c.getTime(),Toast.LENGTH_SHORT).show();
            c=null;
        }

        todoViewModel.insert(todos);
        dismiss();
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

    public void openDatePicker()
    {
        DialogFragment tp =new DatePickerFragment();
        tp.show(getActivity().getSupportFragmentManager(),"date picker");
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {



        c.set(Calendar.HOUR_OF_DAY,hourOfDay);
        c.set(Calendar.MINUTE,minute);
        c.set(Calendar.SECOND,0);

        String s = checkDate(c,0,2);
        Log.i(TAG, "onTimeSet: "+c.getTime());
        timePickerView.setText(s);
    }

    public static void startRemainder(Calendar c,String todo,int rc,boolean ringDaily,Context context)
    {
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i =new Intent(context,AlarmReceiver.class);
        i.putExtra(NOTIFICATION_TITLE,todo);
        PendingIntent pdi = PendingIntent.getBroadcast(context,rc,i,0);

        if(!ringDaily) {
            manager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pdi);
        }else
            {
                manager.setRepeating(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pdi);
            }

    }


    public static void cancelRemainder(Context c,int rc)
    {

        AlarmManager manager = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
        Intent i =new Intent(c,AlarmReceiver.class);
        PendingIntent pdi = PendingIntent.getBroadcast(c,rc,i,0);

        manager.cancel(pdi);

        Toast.makeText(c,"Alarm with id "+rc+" is cancelled",Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        c= Calendar.getInstance();
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);

        view.setMinDate(System.currentTimeMillis()-1000);

        Log.i(TAG, "onDateSet: "+c.getTime());

        openTimePicker();

    }

    public static String getProperTag(int i)
    {
        String[] s={"Remind Me At :\n","Remind Me Daily \n At","Remind Me On \n","Remind Me On ","Remind Me At "};
        return s[i];
    }

    public static String checkDate(Calendar c,int i1,int i2)
    {
        String s="";
        if(c.get(Calendar.DAY_OF_MONTH)==Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
        {
            s = getProperTag(i1) + DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());
        }else
        {
            s = getProperTag(i2)+ DateFormat.getDateInstance(DateFormat.MEDIUM).format(c.getTime());
        }

        return s;
    }
}
