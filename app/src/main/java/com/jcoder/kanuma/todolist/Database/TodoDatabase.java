package com.jcoder.kanuma.todolist.Database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;

import java.util.Date;

/*
* This class creates the database using the entities and dao object
* */

@Database(entities = {Todos.class}, version = 1)
public abstract class TodoDatabase extends RoomDatabase {

    private static TodoDatabase instance;
    public abstract TodoDao todoDao();

    //creates a database
    public static synchronized TodoDatabase getInstance(Context c)
    {
        if(instance == null)
        {
            instance = Room.databaseBuilder(c.getApplicationContext(),
                    TodoDatabase.class,"todo_database")
                    .fallbackToDestructiveMigration()
                    //.addCallback(roomCallback)
                    .build();
        }

        return instance;
    }


//    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback()
//    {
//        @Override
//        public void onCreate(@NonNull SupportSQLiteDatabase db) {
//            super.onCreate(db);
//
//            new PopulateDB(instance).execute();
//        }
//    };


//    private static class PopulateDB extends AsyncTask<Void,Void,Void>
//    {
//        private TodoDao todoDao;
//
//        private PopulateDB(TodoDatabase todoDatabase)
//        {
//            todoDao =todoDatabase.todoDao();
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//
//            todoDao.insert(new Todos("Morning Walk","Jump around and jogging",new Date()));
//            todoDao.insert(new Todos("Eat Healthy","Eat Something Good",new Date()));
//            todoDao.insert(new Todos("Concentrate on Study Yes See How it goes How are you I am fine ","Concentrate on Study 2",new Date()));
//
//
//            return null;
//        }
//    }
}
