package com.example.glimmerheaven.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.glimmerheaven.R;
import com.example.glimmerheaven.utils.Notifications.OrderNotification;

public class NotificationManage extends AppCompatActivity {

    private ImageView back;
    private CardView card_orderNotificationSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notification_manage);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        back = findViewById(R.id.img_back_availableshops);
        card_orderNotificationSetting = findViewById(R.id.order_notification_setting);

        back.setOnClickListener(view -> {
            onBackPressed();
        });
        card_orderNotificationSetting.setOnClickListener(view -> {
            openOrderNotificationChannel();
        });
    }

    private void openOrderNotificationChannel(){
        Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
        intent.putExtra(Settings.EXTRA_CHANNEL_ID, new OrderNotification().getCHANNEL_ID());
        startActivity(intent);
    }
}