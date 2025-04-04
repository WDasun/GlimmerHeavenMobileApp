package com.example.glimmerheaven.data.repository;
import androidx.annotation.NonNull;

import com.example.glimmerheaven.data.model.Customer;
import com.example.glimmerheaven.data.model.Discount;
import com.example.glimmerheaven.utils.callBacks.CustomerCallBack;
import com.example.glimmerheaven.utils.callBacks.ResultCallBack;
import com.example.glimmerheaven.utils.eventCanceller.ValueEventListnerHolder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DiscountRepository {
    private DatabaseReference databaseReference;

    public DiscountRepository() {
        databaseReference = FirebaseDatabase.getInstance().getReference("gm").child("discount");
    }

    public ValueEventListnerHolder getDiscount(String discountId, ResultCallBack callBack){
        DatabaseReference newRef = databaseReference.child(discountId);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Discount discount = snapshot.getValue(Discount.class);
                callBack.onDataComplete(snapshot.getKey(), discount, true, "success");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callBack.onDataComplete(null, null, false, "failed");
            }
        };

        return new ValueEventListnerHolder(newRef, newRef.addValueEventListener(eventListener));
    };
}
