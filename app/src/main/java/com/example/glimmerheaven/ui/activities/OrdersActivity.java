package com.example.glimmerheaven.ui.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.glimmerheaven.R;
import com.example.glimmerheaven.ui.viewmodel.OrderViewModel;
import com.example.glimmerheaven.utils.singleton.UserManage;

public class OrdersActivity extends AppCompatActivity {

    private OrderViewModel orderViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_orders);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
        UserManage.getInstance().getCurrentUser().observe(this, customer -> {
            if(customer.getOrders() != null){
                orderViewModel.loadOrdersLiveData(customer.getOrders());
            }
        });
    }
}