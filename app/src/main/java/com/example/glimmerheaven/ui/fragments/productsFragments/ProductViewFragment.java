package com.example.glimmerheaven.ui.fragments.productsFragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.glimmerheaven.R;
import com.example.glimmerheaven.data.model.Product;
import com.example.glimmerheaven.ui.viewmodel.ProductViewViewModel;

public class ProductViewFragment extends Fragment {
    private String productId;
    private ProductViewViewModel productViewViewModel;
    private TextView txt_productName,txt_price,txt_stockStatus, txt_description;
    private ImageView img_product;
    private int colorInStock, colorOutOfStock;

    public ProductViewFragment() {
        super(R.layout.fragment_product_view);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        productId = getArguments().getString("productId");
        txt_productName = view.findViewById(R.id.txt_product_name_productview);
        txt_price = view.findViewById(R.id.txt_price_productview);
        txt_stockStatus = view.findViewById(R.id.txt_stock_status_productview);
        txt_description = view.findViewById(R.id.txt_description_productview);
        img_product = view.findViewById(R.id.img_product_image_productview);
        colorInStock = ContextCompat.getColor(view.getContext(), R.color.InStock);
        colorOutOfStock = ContextCompat.getColor(view.getContext(), R.color.OutOfStock);

        Log.v("ats", "Product Id : "+productId);

        productViewViewModel = new ViewModelProvider(this).get(ProductViewViewModel.class);

        productViewViewModel.getProduct(productId).observe(getViewLifecycleOwner(), new Observer<Product>() {
            @Override
            public void onChanged(Product product) {
                txt_productName.setText(product.getName());
                txt_price.setText(String.valueOf(product.getPrice()));
                txt_description.setText(product.getDescription());
                int qty = product.getQty();
                if(qty < 1){
                    txt_stockStatus.setText("Out of stock");
                    txt_stockStatus.setTextColor(colorOutOfStock);
                }else{
                    txt_stockStatus.setText("Instock");
                    txt_stockStatus.setTextColor(colorInStock);
                }
                Glide.with(view)
                        .load(product.getImage())
                        .into(img_product);

            }
        });

        LinearLayout LinearLayoutComment = view.findViewById(R.id.LinearLayout_comment);
        LayoutInflater inflater = getLayoutInflater();

        int i = 0;
        while (i < 5){
            LinearLayoutComment.addView(inflater.inflate(R.layout.single_comment_layout, LinearLayoutComment, false));
            i++;
        }

    }
}