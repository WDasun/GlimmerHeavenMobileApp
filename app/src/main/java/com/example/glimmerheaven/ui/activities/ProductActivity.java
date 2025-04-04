package com.example.glimmerheaven.ui.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;

import com.example.glimmerheaven.ui.CustomFragmentFactory.ProductListFactory;
import com.example.glimmerheaven.R;

public class ProductActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private String categoryId, catName, catImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getSupportFragmentManager();
        categoryId = getIntent().getStringExtra("categoryId");
        catName = getIntent().getStringExtra("categoryName");
        catImage = getIntent().getStringExtra("catgoryImage");
        fragmentManager.setFragmentFactory(new ProductListFactory(categoryId,catName,catImage));
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}