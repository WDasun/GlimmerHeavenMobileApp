package com.example.glimmerheaven.data.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.glimmerheaven.data.model.CommentAndRating;
import com.example.glimmerheaven.data.model.Customer;
import com.example.glimmerheaven.utils.callBacks.MessageCallBack;
import com.example.glimmerheaven.utils.callBacks.ResultCallBack;
import com.example.glimmerheaven.utils.callBacks.ResultMapCallBack;
import com.example.glimmerheaven.utils.eventCanceller.ValueEventListnerHolder;
import com.example.glimmerheaven.utils.singleton.UserManage;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CommentAndRatingRepository {
    private DatabaseReference databaseReference;

    public CommentAndRatingRepository() {
        databaseReference = FirebaseDatabase.getInstance().getReference("gm").child("productCommentAndRating");
    }

    public ValueEventListnerHolder getProductRelatedCommentsAndRatings(String productId, ResultMapCallBack callBack){
        DatabaseReference newRef = databaseReference.child(productId);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String, CommentAndRating> commentsAndRatingMap = new HashMap<>();
                for(DataSnapshot ds : snapshot.getChildren()){
                    commentsAndRatingMap.put(ds.getKey(), ds.getValue(CommentAndRating.class));
                }
                callBack.onCompleted(commentsAndRatingMap, true, "success");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callBack.onCompleted(null, false, error.getMessage());
            }
        };

        return new ValueEventListnerHolder(newRef, newRef.addValueEventListener(eventListener));
    }

    public void saveCommentAndRating(String productId, String uid, CommentAndRating car, MessageCallBack callBack){
        Map<String,Object> saves = new HashMap<>();
        saves.put("gm/productCommentAndRating/"+productId+"/"+uid,car);
        saves.put("gm/customer/"+uid+"/ratedProducts/"+productId, car);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.updateChildren(saves, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if(callBack != null){
                    if(error == null){
                        callBack.onComplete(true, "success");
                    }else{
                        callBack.onComplete(false, error.getMessage());
                    }
                }
            }
        });
    }
}
