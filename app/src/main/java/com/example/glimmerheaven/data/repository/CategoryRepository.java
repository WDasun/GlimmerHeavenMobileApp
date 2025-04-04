package com.example.glimmerheaven.data.repository;

import androidx.annotation.NonNull;

import com.example.glimmerheaven.data.model.Category;
import com.example.glimmerheaven.data.model.User;
import com.example.glimmerheaven.utils.callBacks.CategoriesCallBack;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CategoryRepository {
private DatabaseReference databaseReference;

    public CategoryRepository() {
        databaseReference = FirebaseDatabase.getInstance().getReference("gm").child("category");
    }

    public void searchAllCategories(CategoriesCallBack categoriesCallBack){

        Map<String, Category> categoryMap = new HashMap<>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds : snapshot.getChildren()){
                    categoryMap.put(ds.getKey(), ds.getValue(Category.class));
                }

                categoriesCallBack.onDataComplete(categoryMap);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
