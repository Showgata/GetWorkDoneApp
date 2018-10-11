package com.jcoder.kanuma.todolist.Anim;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.jcoder.kanuma.todolist.R;

public class MyAnimationSet {

    public void moveUpAnimation(Context context)
    {
        Animation anim = AnimationUtils.loadAnimation(context, R.anim.anim_move_up);
        anim.start();
    }
}
