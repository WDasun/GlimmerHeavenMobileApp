package com.example.glimmerheaven.ui.activities;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;

import com.example.glimmerheaven.R;
import com.example.glimmerheaven.ui.fragments.cartFragments.CartAddAddressFragment;
import com.example.glimmerheaven.ui.fragments.cartFragments.CartPaymentFragment;

public class BuyProductNowActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private FragmentContainerView fc_main;
    private String pid;
    private int qty,fragmentContainerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_buy_product_now);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        fragmentManager = getSupportFragmentManager();
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            pid = bundle.getString("pid");
            qty = bundle.getInt("qty");
            fragmentContainerId = bundle.getInt("fcid");
        }

        fc_main = findViewById(R.id.fc_main_bpna);

        fragmentManager.beginTransaction()
                .replace(R.id.fc_main_bpna, CartAddAddressFragment.class, bundle,"buyNowAddress")
                .commit();


    }
}