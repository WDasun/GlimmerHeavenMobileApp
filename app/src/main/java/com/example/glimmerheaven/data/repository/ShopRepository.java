package com.example.glimmerheaven.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.glimmerheaven.data.model.Order;
import com.example.glimmerheaven.data.model.Product;
import com.example.glimmerheaven.data.model.Shop;
import com.example.glimmerheaven.utils.callBacks.ProductCallBack;
import com.example.glimmerheaven.utils.callBacks.ResultCallBack;
import com.example.glimmerheaven.utils.eventCanceller.ValueEventListnerHolder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShopRepository {
    private final String NODE = "shop";
    private DatabaseReference databaseReference;

    public ShopRepository() {
        databaseReference = FirebaseDatabase.getInstance().getReference("gm").child(NODE);
    }

    public ValueEventListnerHolder getAllShops(ResultCallBack callBack){
        ValueEventListener listner = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot ds) {
                ArrayList<String> keyList = new ArrayList<>();
                ArrayList<Shop> valueList = new ArrayList<>();
                for(DataSnapshot snapshot : ds.getChildren()){
                    keyList.add(snapshot.getKey());
                    valueList.add(snapshot.getValue(Shop.class));
                }
                callBack.onDataComplete(keyList, valueList, true, "success");
            }

            @Override
            public void onCancelled(DatabaseError de) {
                callBack.onDataComplete(null, null, false, "failed");
            }
        };
        return new ValueEventListnerHolder(databaseReference, databaseReference.addValueEventListener(listner));
    }
}
