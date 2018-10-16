package com.jcoder.kanuma.todolist;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.jcoder.kanuma.todolist.Database.TodoViewModel;
import com.jcoder.kanuma.todolist.Database.Todos;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/*
* Adapter for the recyclerview
* */
public class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.MyViewHolder> {

    private List<Todos> listOfTodos=new ArrayList<>();
    private boolean flag =false;

    public static final String PREFS_ID="com.jcoder.kanuma.todolist.XYZ";


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.custom_checker, parent, false);

            return new MyViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        final Todos todo =listOfTodos.get(position);
        holder.getTodo().setText(todo.getTodoTitle());
        holder.getIsDone().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            /*
            * The below function cancels the alarm if the checkbox is checked and
            * restarts it if the checkbox is unchecked
            * (iff the alarm time is greater than current time)
            * */

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                todo.setTodoCompleted(isChecked);
                saveCheckBoxState(buttonView.getContext(),todo.getTodoTitle(),isChecked);


                if(isChecked) {
                    AddTodo.cancelRemainder(buttonView.getContext(),todo.getAlarmUniqueId());
                    flag=true;

                }else
                    {   if(flag) {
                            restartRemainder(todo, buttonView.getContext());
                            flag=false;
                        }
                    }

                setAlphaInView(holder,getCheckBoxState(holder.getTodo().getContext(),todo.getTodoTitle()));
            }
        });

        if(todo.getDateRemainder()==null)
        {
            holder.reminderDate.setVisibility(View.GONE);
        }
        else {

            Calendar c =Calendar.getInstance();
            c.setTime(todo.getDateRemainder());
            String df = AddTodo.checkDate(c,4,3,todo.isRingDaily());
            holder.reminderDate.setText(df);
            holder.reminderDate.setVisibility(View.VISIBLE);

            }

        holder.isDone.setChecked(getCheckBoxState(holder.getTodo().getContext(),todo.getTodoTitle()));
        setAlphaInView(holder,getCheckBoxState(holder.getTodo().getContext(),todo.getTodoTitle()));


    }

    /*fade the colors of views if the box is checked*/
    private void setAlphaInView(MyViewHolder holder, boolean check)
    {
        float alpha =1f;
        if(check) {
            alpha=0.5f;

        }

        holder.getTodo().setAlpha(alpha);
        holder.getReminderDate().setAlpha(alpha);
        holder.getIsDone().setAlpha(alpha);

    }




    /*Restarts an existing remainder*/
    private void restartRemainder(Todos todos, Context context)
    {
        if(todos.getDateRemainder()!=null) {
            Calendar c = Calendar.getInstance();
            c.setTime(todos.getDateRemainder());

            if (!c.before(Calendar.getInstance())) {
                AddTodo.startRemainder(c, todos.getTodoTitle(), todos.getAlarmUniqueId(), todos.isRingDaily(), context);
                Toast.makeText(context, "" + c.getTime(), Toast.LENGTH_SHORT).show();

            }
        }
    }

    /* Returns total number of todos_ */
    @Override
    public int getItemCount() {
        return listOfTodos.size();
    }

    /*Viewholder for our todo_ recycler view*/
    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        private TextView todo;
        private TextView reminderDate;
        private CheckBox isDone;

        public MyViewHolder(View v) {
            super(v);

            todo =v.findViewById(R.id.todo);
            reminderDate=v.findViewById(R.id.reminder_date);
            isDone=v.findViewById(R.id.checkBox);
        }

        public TextView getTodo() {
            return todo;
        }

        public TextView getReminderDate() {
            return reminderDate;
        }

        public CheckBox getIsDone() {
            return isDone;
        }
    }

    public void setTodoList(List<Todos> listOfTodos)
    {
        this.listOfTodos = listOfTodos;
        notifyDataSetChanged();
    }


    /*Returns the particular todo_ of the given position*/
    public Todos getTodoAtPosition(int pos)
    {   return listOfTodos.get(pos);}

    /*Stores the checkbox state in a shared preferences*/
    private void saveCheckBoxState(Context context, String key, boolean value)
    {
        SharedPreferences sp = context.getSharedPreferences(PREFS_ID,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key,value);
        editor.commit();
    }

    private boolean getCheckBoxState(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(PREFS_ID, Context.MODE_PRIVATE);
        return key != null && sp.getBoolean(key, false);
    }
}
