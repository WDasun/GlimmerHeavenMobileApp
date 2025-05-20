package com.example.glimmerheaven.ui.activities;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.glimmerheaven.R;
import com.example.glimmerheaven.data.model.Product;
import com.example.glimmerheaven.ui.adapters.wishlistAdapters.WishlistAdapter;
import com.example.glimmerheaven.ui.viewmodel.WishListViewModel;

import java.util.ArrayList;
import java.util.Map;

public class WishlistActivity extends AppCompatActivity {

    private ImageView img_back;
    private RecyclerView recyclerView;
    private WishListViewModel wishListViewModel;
    private Context context;
    private WishlistAdapter wishlistAdapter;
    private LifecycleOwner lifecycleOwner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_wishlist);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        lifecycleOwner = this;
        wishListViewModel = new ViewModelProvider(this).get(WishListViewModel.class);
        context = this;

        img_back = findViewById(R.id.img_productlist_icon);

        wishListViewModel.getwishListProductListLiveData().observe(this, new Observer<Map<String, Product>>() {
            @Override
            public void onChanged(Map<String, Product> productMap) {

                recyclerView = findViewById(R.id.recyW_wishlist);
                if(productMap != null){
                    wishlistAdapter = new WishlistAdapter(context,lifecycleOwner,new ArrayList<>(productMap.keySet()), new ArrayList<>(productMap.values()));
                }else{
                    wishlistAdapter = new WishlistAdapter(context,lifecycleOwner,new ArrayList<>(), new ArrayList<>());
                }
                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context, 2);

                recyclerView.setAdapter(wishlistAdapter);
                recyclerView.setLayoutManager(layoutManager);
            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wishListViewModel.getWishlistVEL().removeListener();
    }
}