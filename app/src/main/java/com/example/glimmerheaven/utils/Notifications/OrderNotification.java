package com.example.glimmerheaven.utils.Notifications;

import static androidx.core.content.ContextCompat.getSystemService;
import static androidx.core.content.PermissionChecker.checkSelfPermission;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.PermissionChecker;

import com.example.glimmerheaven.R;
import com.example.glimmerheaven.ui.activities.ViewOrderFromNotification;

public class OrderNotification {
    private final String CHANNEL_NAME = "Order Status Notification";
    private final String CHANNEL_ID = "ORDER_STATUS_NOTIFY";
    private final String CHANNEL_DESC = "Notify when order status changed";
    private Context context;

    public OrderNotification() {
    }

    public OrderNotification(Context context) {
        this.context = context;
    }

    public void craeteNotificationChanel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESC);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void createNotification(String title, String body, String orderId, String uid){

        Intent intent = new Intent(context, ViewOrderFromNotification.class);
        intent.putExtra("orderId", orderId);
        intent.putExtra("uid",uid);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,CHANNEL_ID)
                .setSmallIcon(R.drawable.orders)
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                checkSelfPermission(context,android.Manifest.permission.POST_NOTIFICATIONS) == PermissionChecker.PERMISSION_GRANTED) {

            notificationManager.notify(1001, builder.build());
        }
    }

    public String getCHANNEL_NAME() {
        return CHANNEL_NAME;
    }

    public String getCHANNEL_ID() {
        return CHANNEL_ID;
    }

    public String getCHANNEL_DESC() {
        return CHANNEL_DESC;
    }
}
