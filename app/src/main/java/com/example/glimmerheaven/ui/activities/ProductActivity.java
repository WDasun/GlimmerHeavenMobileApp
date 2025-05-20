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

import com.example.glimmerheaven.ui.CustomFragmentFactory.ProductListFactory;
import com.example.glimmerheaven.R;
import com.example.glimmerheaven.ui.fragments.productsFragments.ProductListFragment;
import com.example.glimmerheaven.ui.fragments.productsFragments.ProductViewFragment;

public class ProductActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private String categoryId, catName, catImage, brand;
    private String specialProductId;
    private FragmentContainerView fc_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        specialProductId = getIntent().getStringExtra("pid");
        fragmentManager = getSupportFragmentManager();
        fc_container = findViewById(R.id.fragment_container_products);
        if(specialProductId == null){
            categoryId = getIntent().getStringExtra("categoryId");
            catName = getIntent().getStringExtra("categoryName");
            catImage = getIntent().getStringExtra("catgoryImage");
            brand = getIntent().getStringExtra("Brand");
            fragmentManager.setFragmentFactory(new ProductListFactory(categoryId,catName,catImage, brand));

            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_products, ProductListFragment.class,null,"ProductListFragment")
                    .commit();

        }else{
            Bundle bundle = new Bundle();
            bundle.putString("productId",specialProductId);
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_products, ProductViewFragment.class,bundle,"ProductViewFragment")
                    .commit();
        }
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


    }
}