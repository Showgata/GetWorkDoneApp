package com.jcoder.kanuma.todolist.Database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class TodoViewModel extends AndroidViewModel {

    private TodoRepository repository;
    private LiveData<List<Todos>> allTodos;

    public TodoViewModel(@NonNull Application application) {
        super(application);

        repository =new TodoRepository(application);
        allTodos =repository.getAllTodos();
    }

    public void insert(Todos todos)
    {
        repository.insert(todos);
    }

    public void update(Todos todos)
    {
        repository.update(todos);
    }

    public void delete(Todos todos)
    {
        repository.delete(todos);
    }

    public void deleteAll()
    {
        repository.deleteAll();
    }

    public LiveData<List<Todos>> getAllTodos() {
        return allTodos;
    }
}
