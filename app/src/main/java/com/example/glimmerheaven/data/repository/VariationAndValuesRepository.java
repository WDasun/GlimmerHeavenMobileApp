package com.example.glimmerheaven.data.repository;

import androidx.annotation.NonNull;

import com.example.glimmerheaven.utils.callBacks.VariationsAndValuesCallBack;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VariationAndValuesRepository {
    private DatabaseReference databaseReference;

    public VariationAndValuesRepository() {
        databaseReference = FirebaseDatabase.getInstance().getReference("gm").child("variation_values");
    }


    public void searchAllVariationsAndValues(String categoryId, VariationsAndValuesCallBack callBack) {
        Map<String, ArrayList<String>> variationsAndValuesMap = new HashMap<>();

        databaseReference.child(categoryId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    ArrayList<String> values = new ArrayList<>();
                    for(DataSnapshot d : ds.getChildren()){
                        values.add(d.getKey());
                    }
                    variationsAndValuesMap.put(ds.getKey(), values);
                }
                callBack.onDataComplete(variationsAndValuesMap);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
