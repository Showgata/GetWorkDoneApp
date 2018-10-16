package com.jcoder.kanuma.todolist.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/*
* DATA ACCESS OBJECT
* This interface contains few CRUD methods
* */

@Dao
public interface TodoDao {

    @Insert
    void insert(Todos todos);

    @Update
    void update(Todos todos);

    @Delete
    void delete(Todos todos);

    @Query("DELETE FROM todos_table")
    void deleteAll();

    @Query("SELECT * FROM todos_table ORDER BY dateCreated DESC")
    LiveData<List<Todos>> getAllTodos();

//    @Query("SELECT * FROM todos_table WHERE dateCreated =?")
//    LiveData<List<Todos>> getSpecificTodos();
}
