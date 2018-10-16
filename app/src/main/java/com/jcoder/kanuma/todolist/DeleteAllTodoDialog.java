package com.jcoder.kanuma.todolist;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.View;
import android.widget.Button;

import com.jcoder.kanuma.todolist.Database.TodoViewModel;


/*
* Dialog Fragment With Custom View
* Contains Two Buttons - Ok and Cancel with their basic implementation
* */

public class DeleteAllTodoDialog extends AppCompatDialogFragment {

    private static DeleteAllTodoDialog fragment;

    private Button okay;
    private Button cancel;
    private TodoViewModel todoViewModel;

    public static DeleteAllTodoDialog newInstance() {

        if(fragment==null) {
            fragment = new DeleteAllTodoDialog();

        }
        return fragment;
    }

    public void setTodoViewModel(TodoViewModel todoViewModel)
    {
        this.todoViewModel=todoViewModel;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final AlertDialog.Builder builder =new AlertDialog.Builder(getActivity());

        View view = getActivity().getLayoutInflater()
                .inflate(R.layout.fragment_delete_all_todo,null);

        okay=view.findViewById(R.id.sOkay);
        cancel=view.findViewById(R.id.sCancel);

        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                todoViewModel.deleteAll();
                DeleteAllTodoDialog.this.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteAllTodoDialog.this.dismiss();
            }
        });



        builder.setView(view);

        return builder.create();
    }


}
