package com.jcoder.kanuma.todolist.Database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

public class TodoRepository {

    private TodoDao todoDao;
    private LiveData<List<Todos>> allTodos;

    private static final String TAG = "TodoRepository";

    public TodoRepository(Application application)
    {
        TodoDatabase database = TodoDatabase.getInstance(application);
        todoDao=database.todoDao();
        allTodos=todoDao.getAllTodos();
    }

    //Doesnt automatically executes in the background thread
    public void insert(Todos todo)
    {
        new TodosAsyncTask(todoDao,1).execute(todo);
    }

    public void update(Todos todo)
    {
        new TodosAsyncTask(todoDao,2).execute(todo);
    }

    public void delete(Todos todo)
    {
        new TodosAsyncTask(todoDao,3).execute(todo);
    }

    public void deleteAll()
    {
        new TodosAsyncTask(todoDao,4).execute();
    }

    //Automatically executes in the background thread
    public LiveData<List<Todos>> getAllTodos() {
        return allTodos;
    }


    private static class TodosAsyncTask extends AsyncTask<Todos,Void,Void>
    {
        private TodoDao todoDao;
        private int choice;

        /*
        *   CHOICES
        *   '1' - insert
        *   '2' - update
        *   '3  - delete
        *   '4' - deleteAll
        *   '5' - getAll
        * */


        private TodosAsyncTask(TodoDao todoDao,int choice)
        {
            this.todoDao =todoDao;
            this.choice=choice;
        }

        @Override
        protected Void doInBackground(Todos... todos) {

            switch (choice)
            {
                case 1:
                    todoDao.insert(todos[0]);
                    break;
                case 2:
                    todoDao.update(todos[0]);
                    break;
                case 3:
                    todoDao.delete(todos[0]);
                    break;
                case 4:
                    todoDao.deleteAll();
                    break;
                default:
                    Log.i(TAG,"Invalid Choice");
            }


            return null;
        }
    }

}
