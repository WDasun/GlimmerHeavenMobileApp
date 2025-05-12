package com.example.glimmerheaven.utils.authentication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.example.glimmerheaven.Firebase.MyFirebaseMessagingService;
import com.example.glimmerheaven.data.repository.FirebaseAuthRepository;
import com.example.glimmerheaven.ui.activities.SignIn;
import com.example.glimmerheaven.utils.Notifications.OrderNotification;
import com.example.glimmerheaven.utils.SharedPreferences.SPManageUID;
import com.example.glimmerheaven.utils.singleton.UserManage;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseUserAuthentication {
    private Context context;

    public FirebaseUserAuthentication(Context context) {
        this.context = context;
    }

    public void Authenticate(){
        FirebaseUser user = new FirebaseAuthRepository().getCurrentUser();
        if(user == null){
            Intent intent = new Intent(context, SignIn.class);
            context.startActivity(intent);
            if (context instanceof android.app.Activity) {
                ((android.app.Activity) context).finish();
            }

        }else{
            // Setting customer as current user for future use
            UserManage.getInstance().setCurrentUser(user.getUid(), (status, message) -> {
                if(status){
                    // Notification manage
                    new MyFirebaseMessagingService().retrieveCurrentToken();
                    OrderNotification on = new OrderNotification(context);
                    on.craeteNotificationChanel();
                }
            });
            // UID sharedPreferences manage, saving uid
            new SPManageUID(context)
                    .saveUid(user.getUid());
        }
    }


}
