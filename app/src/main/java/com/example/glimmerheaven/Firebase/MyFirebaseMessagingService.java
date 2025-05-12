package com.example.glimmerheaven.Firebase;

import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.glimmerheaven.data.model.Customer;
import com.example.glimmerheaven.data.repository.CustomerRepository;
import com.example.glimmerheaven.data.repository.FirebaseAuthRepository;
import com.example.glimmerheaven.utils.Notifications.OrderNotification;
import com.example.glimmerheaven.utils.singleton.UserManage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public MyFirebaseMessagingService() {
    }


    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);

        String title = message.getData().get("title");
        String body = message.getData().get("body");
        String orderId = message.getData().get("orderId");
        String uid = message.getData().get("uid");

        new OrderNotification(this).createNotification(title,body,orderId,uid);

    }

    public void retrieveCurrentToken(){
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {

                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        Log.v("ats3","Token : "+token);
                        saveNewTokenForCustomer(token);

                    }
                });
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        saveNewTokenForCustomer(token);
    }

    private void saveNewTokenForCustomer(String token){
        try{
            Customer customer = UserManage.getInstance().getCurrentUser().getValue();
            customer.setFCMToken(token);
            String UID = new FirebaseAuthRepository().getCurrentUser().getUid();

            new CustomerRepository().saveCustomer(UID,customer,null);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
