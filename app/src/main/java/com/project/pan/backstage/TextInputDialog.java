package com.project.pan.backstage;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class TextInputDialog {

    public interface Listener {
        boolean onOkClicked(String s);
    }
    private AlertDialog alertDialog;
    private EditText editText;
    private Listener listener;

    public TextInputDialog(Context context, String title, boolean numberOnly) {
        editText = new EditText(context);
        if(numberOnly) {
            editText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        }
        alertDialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setView(editText)
                .setPositiveButton("Ok", null)
                .create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialogInterface) {
                Button button = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(listener.onOkClicked(editText.getText().toString())) {
                            dialogInterface.dismiss();
                        }
                    }
                });
            }
        });
    }

    public void show() {
        alertDialog.show();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }
}
