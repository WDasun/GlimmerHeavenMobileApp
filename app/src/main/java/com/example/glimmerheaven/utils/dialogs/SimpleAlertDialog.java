package com.example.glimmerheaven.utils.dialogs;

import android.app.AlertDialog;
import android.content.Context;

public class SimpleAlertDialog {

    private Context context;
    private String title,message,positiveButtonTitle, negetiveButtonTitle;
    private ButtonBodyCallBack postiveButtonCallBack, negetiveButtonCallBack;

    public SimpleAlertDialog(Context context, String title, String message, String positiveButtonTitle, String negetiveButtonTitle, ButtonBodyCallBack postiveButtonCallBack, ButtonBodyCallBack negetiveButtonCallBack) {
        this.context = context;
        this.title = title;
        this.message = message;
        this.positiveButtonTitle = positiveButtonTitle;
        this.negetiveButtonTitle = negetiveButtonTitle;
        this.postiveButtonCallBack = postiveButtonCallBack;
        this.negetiveButtonCallBack = negetiveButtonCallBack;
    }

    public AlertDialog.Builder createDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if(title != null){
            builder.setTitle(title);
        }
        if(message != null){
            builder.setMessage(message);
        }
        if(positiveButtonTitle != null){
            builder.setPositiveButton(positiveButtonTitle, (dialogInterface, i) -> {
               if(postiveButtonCallBack != null){
                   postiveButtonCallBack.onButtonClicked();
               }
            });
        }
        if(negetiveButtonTitle != null){
            builder.setNegativeButton(negetiveButtonTitle, (dialogInterface, i) -> {
                if(negetiveButtonCallBack != null){
                    negetiveButtonCallBack.onButtonClicked();
                }
            });
        }
        return builder;
    }

    public interface ButtonBodyCallBack{
        void onButtonClicked();
    }
}
