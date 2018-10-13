package com.jcoder.kanuma.todolist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.jcoder.kanuma.todolist.Database.Todos;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.MyViewHolder> {

    private List<Todos> listOfTodos=new ArrayList<>();
    private boolean flag =false;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.custom_checker, parent, false);

            return new MyViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        final Todos todo =listOfTodos.get(position);
        holder.getTodo().setText(todo.getTodoTitle());
        holder.getIsDone().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                float alpha =1f;

                if(isChecked) {
                    alpha = 0.5f;
                    AddTodo.cancelRemainder(buttonView.getContext(),todo.getAlarmUniqueId());
                    flag=true;
                }else
                    {   if(flag) {
                            restartRemainder(todo, buttonView.getContext());
                            flag=false;
                        }
                    }

                holder.getTodo().setAlpha(alpha);
                holder.getReminderDate().setAlpha(alpha);
                holder.getIsDone().setAlpha(alpha);
            }
        });

        if(todo.getDateRemainder()==null)
        {
            holder.reminderDate.setVisibility(View.GONE);
        }
        else {

            Calendar c =Calendar.getInstance();
            c.setTime(todo.getDateRemainder());
            String df = AddTodo.checkDate(c,4,3);
            holder.reminderDate.setText(df);
            holder.reminderDate.setVisibility(View.VISIBLE);

            }


        holder.isDone.setChecked(todo.isTodoCompleted());

    }



    private void restartRemainder(Todos todos, Context context)
    {

        Calendar c =Calendar.getInstance();
        c.setTime(todos.getDateRemainder());

        if(!c.before(Calendar.getInstance())) {
            AddTodo.startRemainder(c, todos.getTodoTitle(), todos.getAlarmUniqueId(), todos.isRingDaily(), context);
            Toast.makeText(context, "" + c.getTime(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return listOfTodos.size();
    }

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

    public Todos getTodoAtPosition(int pos)
    {   return listOfTodos.get(pos);}
}
