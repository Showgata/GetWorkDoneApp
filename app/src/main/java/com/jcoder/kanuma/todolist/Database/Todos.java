package com.jcoder.kanuma.todolist.Database;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcelable;

import com.jcoder.kanuma.todolist.Extras.TimeStampInfo;

import java.util.Calendar;
import java.util.Date;

/*
*ENTITIES
This class contains the columns name of our database
* */

@Entity(tableName = "todos_table")
public class Todos  {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String todoTitle;
    private String todoDescription;
    private boolean ringDaily;

    private boolean todoCompleted;

    @TypeConverters({TimeStampInfo.class})
    private Date dateCreated;

    @TypeConverters({TimeStampInfo.class})
    private Date dateRemainder;

    private int alarmUniqueId;


    public Todos(String todoTitle, String todoDescription, Date dateCreated) {
        this.todoTitle = todoTitle;
        this.todoDescription = todoDescription;
        this.todoCompleted = false;
        this.dateCreated = dateCreated;
        this.dateRemainder = null;
        this.alarmUniqueId =0;
        this.ringDaily=false;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTodoTitle() {
        return todoTitle;
    }

    public void setTodoTitle(String todoTitle) {
        this.todoTitle = todoTitle;
    }

    public String getTodoDescription() {
        return todoDescription;
    }

    public void setTodoDescription(String todoDescription) {
        this.todoDescription = todoDescription;
    }

    public int getAlarmUniqueId() {
        return alarmUniqueId;
    }

    public boolean isRingDaily() {
        return ringDaily;
    }

    public void setRingDaily(boolean ringDaily) {
        this.ringDaily = ringDaily;
    }

    public void setAlarmUniqueId(int alarmUniqueId) {
        this.alarmUniqueId = alarmUniqueId;
    }

    public boolean isTodoCompleted() {
        return todoCompleted;
    }

    public void setTodoCompleted(boolean todoCompleted) {
        this.todoCompleted = todoCompleted;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateRemainder() {
        return dateRemainder;
    }

    public void setDateRemainder(Date dateRemainder) {
        this.dateRemainder = dateRemainder;
    }
}
