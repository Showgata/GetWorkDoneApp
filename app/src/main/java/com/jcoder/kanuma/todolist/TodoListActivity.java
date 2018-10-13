package com.jcoder.kanuma.todolist;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.jcoder.kanuma.todolist.Database.TodoViewModel;
import com.jcoder.kanuma.todolist.Database.Todos;
import com.jcoder.kanuma.todolist.Extras.DateTimeInfo;

import java.util.ArrayList;
import java.util.List;

public class TodoListActivity extends AppCompatActivity {

    private RecyclerView rvListOfTodos;
    private ImageButton addBtn;
    private ImageButton deleteAll;
    private TextView showText;
    private TodoViewModel todoViewModel;
    private TextView dateTv;
    private ToDoListAdapter adapter;
    private List<Todos> todos =new ArrayList<>();

    private NotificationManagerCompat managerCompat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

        managerCompat = NotificationManagerCompat.from(this);

        rvListOfTodos =findViewById(R.id.listOftodos);
        adapter=new ToDoListAdapter();
        rvListOfTodos.setAdapter(adapter);
        rvListOfTodos.setLayoutManager(new LinearLayoutManager(this));
        rvListOfTodos.hasFixedSize();

        showText=findViewById(R.id.if_list_empty);
        dateTv=findViewById(R.id.dateTv);
        dateTv.setText(DateTimeInfo.getDateTime());

        addBtn=findViewById(R.id.add_todo_btn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog();
            }
        });

        deleteAll=findViewById(R.id.delete_all_todo_btn);
        deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                todoViewModel.deleteAll();
                cancelAllRemainder(todos);
            }
        });

        todoViewModel= ViewModelProviders.of(this).get(TodoViewModel.class);
        todoViewModel.getAllTodos().observe(this, new Observer<List<Todos>>() {
            @Override
            public void onChanged(@Nullable List<Todos> todos) {
                showNoneTextIfListisEmpty(todos);
                adapter.setTodoList(todos);
                TodoListActivity.this.todos=todos;
            }
        });


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                Todos t =adapter.getTodoAtPosition(viewHolder.getAdapterPosition());


                if(t.getAlarmUniqueId()!=0)
                    AddTodo.cancelRemainder(TodoListActivity.this,t.getAlarmUniqueId());

                todoViewModel.delete(t);
                Toast.makeText(TodoListActivity.this,"Todo deleted Successfully",Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(rvListOfTodos);
    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    public void showNoneTextIfListisEmpty(List<Todos> listOfTodos)
    {
        if(listOfTodos==null || listOfTodos.isEmpty())
        {
            showText.setVisibility(View.VISIBLE);
            rvListOfTodos.setVisibility(View.GONE);
        }else
            {
                showText.setVisibility(View.GONE);
                rvListOfTodos.setVisibility(View.VISIBLE);
            }
    }

    public void createDialog()
    {
        AddTodo addTodo = AddTodo.newInstance();
        addTodo.setTodoViewModel(todoViewModel);
        addTodo.show(getSupportFragmentManager(),"Add Todo !");

    }

    public void cancelAllRemainder(List<Todos> todos)
    {
        int j=0;
        AlarmManager manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent i =new Intent(this,AlarmReceiver.class);

        while(j<todos.size()) {

            int rc = todos.get(j).getAlarmUniqueId();

            if(rc!=0) {
                PendingIntent pdi = PendingIntent.getBroadcast(this, rc, i, 0);

                manager.cancel(pdi);

                Toast.makeText(this, "Alarm with id " + rc + " is cancelled", Toast.LENGTH_SHORT).show();
            }
            j++;
        }
    }


}
