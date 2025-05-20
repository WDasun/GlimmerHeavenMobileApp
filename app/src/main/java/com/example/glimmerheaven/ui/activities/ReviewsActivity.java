package com.example.glimmerheaven.ui.activities;

import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.glimmerheaven.R;
import com.example.glimmerheaven.data.model.Product;
import com.example.glimmerheaven.data.repository.ProductRepository;
import com.example.glimmerheaven.ui.adapters.ReviewsAdapters.MyReviewsAdapter;
import com.example.glimmerheaven.utils.singleton.UserManage;

import java.util.ArrayList;

public class ReviewsActivity extends AppCompatActivity {

    private ImageView img_back;
    private RecyclerView rcv_reviews;
    private MyReviewsAdapter myReviewsAdapter;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reviews);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        context = this;

        img_back = findViewById(R.id.img_back_reviews);

        UserManage.getInstance().getCurrentUser().observe(this,customer -> {
            new ProductRepository().searchProductsOnce(
                    new ArrayList<>(customer.getRatedProducts().keySet()),
                    (keys, values, status, message) -> {
                        if(status){
                            rcv_reviews = findViewById(R.id.rcv_myreviews);
                            myReviewsAdapter = new MyReviewsAdapter(
                                    context,
                                    new ArrayList<>(customer.getRatedProducts().keySet()),
                                    new ArrayList<>(customer.getRatedProducts().values()),
                                    (ArrayList<Product>) values
                            );
                            rcv_reviews.setLayoutManager(new LinearLayoutManager(context));
                            rcv_reviews.setAdapter(myReviewsAdapter);
                        }else{
                            Toast.makeText(context,"Fetching data from the database failed!",Toast.LENGTH_LONG);
                        }
                    }
            );

        });

        img_back.setOnClickListener(view -> {
            onBackPressed();
        });
    }
}