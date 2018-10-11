package com.jcoder.kanuma.todolist;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.jcoder.kanuma.todolist.Database.Todos;

import java.util.List;

public class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.MyViewHolder> {

    private List<Todos> listOfTodos;
//
//    public ToDoListAdapter(List<Todos> listOfTodos) {
//        this.listOfTodos = listOfTodos;
//    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.custom_checker, parent, false);

            return new MyViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 20;
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
}
