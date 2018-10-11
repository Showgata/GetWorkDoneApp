package com.jcoder.kanuma.todolist;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


/*
    This activity is the splash screen of the app
    which contains a basic delay function.
 */

public class IntroActivity extends AppCompatActivity {


    private final int DELAY_TIME = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent t = new Intent(IntroActivity.this,NoteListActivity.class);
                startActivity(t);
                overridePendingTransition(R.anim.anim_move_down,R.anim.anim_move_up);
                finish();
            }
        },DELAY_TIME);
    }
}
