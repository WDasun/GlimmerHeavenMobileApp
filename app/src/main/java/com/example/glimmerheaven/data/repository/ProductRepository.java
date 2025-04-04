package com.example.glimmerheaven.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.glimmerheaven.data.model.Product;
import com.example.glimmerheaven.utils.callBacks.CategoriesCallBack;
import com.example.glimmerheaven.utils.callBacks.MessageCallBack;
import com.example.glimmerheaven.utils.callBacks.ProductCallBack;
import com.example.glimmerheaven.utils.callBacks.ProductsCallBack;
import com.example.glimmerheaven.utils.callBacks.ResultCallBack;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ProductRepository {
    private DatabaseReference databaseReference;

    public ProductRepository() {
        databaseReference = FirebaseDatabase.getInstance().getReference("gm").child("product");
    }

    public void searchProduct(String productId, ProductCallBack callBack){
        Log.v("ats3", "searchProduct Called !");
        databaseReference.child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                callBack.onDataComplete(snapshot.getValue(Product.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void searchProductsOnce(ArrayList<String> productIds, ResultCallBack callBack){

        ArrayList<String> fetchedProductIds = new ArrayList<>();
        ArrayList<Product> fetchedProducts = new ArrayList<>();

        int productIdsSize = productIds.size();
        AtomicInteger completedCount = new AtomicInteger(0);

        for(String pid : productIds){

            databaseReference.child(pid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()) {

                        DataSnapshot ds = task.getResult();
                        String pid = ds.getKey();
                        Product product = ds.getValue(Product.class);

                            synchronized (this){
                                fetchedProductIds.add(pid);
                                fetchedProducts.add(product);
                            }
                    }else{
                        Log.v("ats2", "Fetch data failed PID : "+pid);
                    }

                    if(productIdsSize == completedCount.incrementAndGet()){
                        callBack.onDataComplete(fetchedProductIds,fetchedProducts, true, "Completed");
                    }
                }
            });
        }
    }

    public void searchAllProductsInCategory(String categoryId,ProductsCallBack productsCallBack){

        Map<String, Product> productMap = new HashMap<>();

        databaseReference.orderByChild("categoryId").equalTo(categoryId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    productMap.put(ds.getKey(), ds.getValue(Product.class));
                }
                productsCallBack.onDataComplete(productMap);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void updateQtyInProducts(Map<String, Integer> productIdsAndQtys, MessageCallBack callBack){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        ArrayList<String> pids = new ArrayList<>(productIdsAndQtys.keySet());
        Map<String, Object> updates = new HashMap<>();
        for (String pid : pids) {
            updates.put("gm/product/" + pid + "/qty", productIdsAndQtys.get(pid));
        }
        databaseReference.updateChildren(updates, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError de, DatabaseReference dr) {
                if (de != null) {
                    if(callBack != null){
                        callBack.onComplete(false, de.getMessage());
                    }
                } else {
                   if(callBack != null){
                       callBack.onComplete(true, "Success!");
                   }
                }
            }
        });
    }
}
