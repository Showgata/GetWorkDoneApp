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

/*
* A Simple Fragment to add new todo_ in the recycler view
* */


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
    public static final String NOTIFICATION_ID="notification ID";
    public static final String BUNDLE_NAME="content bundle";
    private boolean ringDaily=false;
    private String s;
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


        //Adding a custom view to the dialog fragment
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

        //opens the default date and time picker fragment for selecting date and time
        timePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker();
            }
        });

        //listens if the checkbox is selected or not
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
        Calendar cal;
        String title = todoTitle.getText().toString().trim();
        String desc= todoDesc.getText().toString().trim();
        Todos todos;

        //checks if the title field is empty or not
        if(TextUtils.isEmpty(title)){
            todoTitle.setError("This field cannot be empty");
            return;
        }else {
                cal = Calendar.getInstance();
                todos =new Todos(title,desc,cal.getTime());
            }


        //if the date and time is picked,then c will not be null
        if(c!=null && !c.equals(cal)){
            //generate a random number as alarm id
            final int random = new Random().nextInt(10000000)+100000;
            todos.setDateRemainder(c.getTime());
            todos.setRingDaily(ringDaily);
            todos.setAlarmUniqueId(random);
            startRemainder(c,title,random,ringDaily,getActivity());
            Toast.makeText(getActivity(),"Alarm Set For :"+c.getTime(),Toast.LENGTH_SHORT).show();
            c=null;
        }

        //insert the todo_ details in the database
        todoViewModel.insert(todos);
        dismiss();
    }


    @Override
    public void onStart() {
        super.onStart();

        //remove the default background of the dialog fragment
        Window window = getDialog().getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
    }


    public void openTimePicker()
    {
        //opens the default time picker fragment
        DialogFragment tp =new TimePickerFragment();
        tp.show(getActivity().getSupportFragmentManager(),"time picker");
    }

    public void openDatePicker()
    {
        //opens the default date picker fragment
        DialogFragment tp =new DatePickerFragment();
        tp.show(getActivity().getSupportFragmentManager(),"date picker");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        //create new calender instance and set the date to it
        c= Calendar.getInstance();
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);


        Log.i(TAG, "onDateSet: "+c.getTime());

        openTimePicker();

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        //set time to the existing calender object
        c.set(Calendar.HOUR_OF_DAY,hourOfDay);
        c.set(Calendar.MINUTE,minute);
        c.set(Calendar.SECOND,0);

        if(c.before(Calendar.getInstance()))
        {
            c.set(Calendar.DAY_OF_MONTH,c.get(Calendar.DAY_OF_MONTH)+1);
        }


        s = checkDate(c,0,2,false);
        Log.i(TAG, "onTimeSet: "+c.getTime());

        //update the textview with the string in s
        timePickerView.setText(s);
    }


    //creates a new alarm to ring on a particular date and time
    public static void startRemainder(Calendar c,String todo,int rc,boolean ringDaily,Context context)
    {
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i =new Intent(context,AlarmReceiver.class);
        i.putExtra(NOTIFICATION_TITLE,todo);
        i.putExtra(NOTIFICATION_ID,rc);
        PendingIntent pdi = PendingIntent.getBroadcast(context,rc,i,0);

        if(!ringDaily) {
            manager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pdi);
        }else
            {
                manager.setRepeating(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pdi);
                Toast.makeText(context,"Alarm with ring daily from "+c.getTime(),Toast.LENGTH_LONG).show();
            }

    }


    //cancels the alarm
    public static void cancelRemainder(Context c,int rc)
    {

        AlarmManager manager = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
        Intent i =new Intent(c,AlarmReceiver.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pdi = PendingIntent.getBroadcast(c,rc,i,0);

        manager.cancel(pdi);

        //Toast.makeText(c,"Alarm with id "+rc+" is cancelled",Toast.LENGTH_SHORT).show();

    }




    public static String getProperTag(int i)
    {
        String[] s={"Remind Me At :\n","Remind Me Daily At ","Remind Me On \n","Remind Me On ","Remind Me At "};
        return s[i];
    }

    /*
     * checkDate checks the date and shows various strings accordingly
     * */
    public static String checkDate(Calendar c,int i1,int i2,boolean ringDaily)
    {
        String s="";
        if(c.get(Calendar.DAY_OF_MONTH)==Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
        {
            s = getProperTag(i1) + DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());
        }else
            {
                s = getProperTag(i2)+ DateFormat.getDateInstance(DateFormat.MEDIUM).format(c.getTime());
            }

        if(ringDaily)
        {
            s = getProperTag(1)+ DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());
        }

        return s;
    }
}
