package com.jcoder.kanuma.todolist.Database;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.jcoder.kanuma.todolist.Extras.TimeStampInfo;

import java.util.Date;

@Entity(tableName = "todos_table")
public class Todos {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String todoTitle;
    private String todoDescription;

    private boolean todoCompleted=false;

    @TypeConverters({TimeStampInfo.class})
    private Date dateCreated;

    private String dateRemainder;




}
