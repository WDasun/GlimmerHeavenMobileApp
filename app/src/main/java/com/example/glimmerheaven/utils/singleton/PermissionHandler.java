package com.example.glimmerheaven.utils.singleton;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class PermissionHandler {
    private final String NULL_POINT_ERROR_MSG = "Required context not passed through the setter method !";
    private Context context;

    public final static PermissionHandler PERMISSION_HANDLER = new PermissionHandler();
    private PermissionHandler() {
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void startupPermissionChecks() throws Exception{
        try {
            checkPostNotificationPermission();
            checkCameraPermission();
        } catch (Exception e) {
            throw e;
        }
    }
    public void checkPostNotificationPermission() throws Exception{
        if(checkContext()){
            if(ContextCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED){
                if (context instanceof android.app.Activity) {
                    ((android.app.Activity) context).requestPermissions(
                            new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1005);
                }

            }
        }else{
            throw new NullPointerException(NULL_POINT_ERROR_MSG);
        }
    }

    public void checkCameraPermission(){
        if(checkContext()){
            if(ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                if (context instanceof android.app.Activity) {
                    ((android.app.Activity) context).requestPermissions(
                            new String[]{Manifest.permission.CAMERA}, 1006);
                }

            }
        }else{
            throw new NullPointerException(NULL_POINT_ERROR_MSG);
        }

    }

    private ActivityResultLauncher<String> resultLauncher;
    private AppCompatActivity appCompatActivity;

    private boolean checkContext(){
        if(context != null){
            return true;
        }else{
            return false;
        }
    }
}
