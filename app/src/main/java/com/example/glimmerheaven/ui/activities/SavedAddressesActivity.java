package com.example.glimmerheaven.ui.activities;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.glimmerheaven.R;
import com.example.glimmerheaven.ui.adapters.addressAdapters.SavedAddressesAdapter;
import com.example.glimmerheaven.ui.viewmodel.SaveAddressesViewModel;
import com.example.glimmerheaven.utils.dialogs.AddNewAddressFragmentDialog;
import com.example.glimmerheaven.utils.dialogs.UpdateAddressFragmentDialog;
import com.example.glimmerheaven.utils.singleton.UserManage;
import com.google.android.material.card.MaterialCardView;

public class SavedAddressesActivity extends AppCompatActivity {

    private MaterialCardView card_addNewAddress;
    private ImageView img_back;
    private RecyclerView recyclerView;
    private SaveAddressesViewModel saveAddressesViewModel;
    private SavedAddressesAdapter savedAddressesAdapter;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_saved_addresses);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        saveAddressesViewModel = new ViewModelProvider(this).get(SaveAddressesViewModel.class);
        context = this;

        img_back = findViewById(R.id.img_back_savedaddresses);
        card_addNewAddress = findViewById(R.id.card_newaddress_savedaddresses);

        UserManage.getInstance().getCurrentUser().observe(this, customer -> {
            if(customer != null && !customer.getAddress().isEmpty()){
                recyclerView = findViewById(R.id.ryv_address_savedaddress);
                savedAddressesAdapter = new SavedAddressesAdapter(customer.getAddress(),context, getSupportFragmentManager());
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.setAdapter(savedAddressesAdapter);
            }
        });

        img_back.setOnClickListener(view -> {
            onBackPressed();
        });

        card_addNewAddress.setOnClickListener(view -> {
            new AddNewAddressFragmentDialog()
                    .show(getSupportFragmentManager(), "");
        });
    }
}