package com.jcoder.kanuma.todolist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jcoder.kanuma.todolist.Database.Todos;
import com.jcoder.kanuma.todolist.Extras.DateTimeInfo;

import java.util.List;

public class NoteListActivity extends AppCompatActivity {

    private RecyclerView rvListOfTodos;
    private ImageButton addBtn;
    private List<Todos> listOfTodos=null;
    private TextView showText;

    private TextView dateTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);

        rvListOfTodos =findViewById(R.id.listOftodos);
        rvListOfTodos.setAdapter(new ToDoListAdapter());
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
    }


    @Override
    protected void onStart() {
        super.onStart();

        showNoneTextIfListisEmpty();
    }

    public void showNoneTextIfListisEmpty()
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

        addTodo.show(getSupportFragmentManager(),"Add Todo !");
    }


}
