package com.example.glimmerheaven.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.glimmerheaven.data.model.Address;
import com.example.glimmerheaven.data.model.Card;
import com.example.glimmerheaven.data.model.CartItem;
import com.example.glimmerheaven.data.model.Customer;
import com.example.glimmerheaven.utils.callBacks.CustomerCallBack;
import com.example.glimmerheaven.utils.callBacks.MessageCallBack;
import com.example.glimmerheaven.utils.callBacks.WishListCallBack;
import com.example.glimmerheaven.utils.eventCanceller.ValueEventListnerHolder;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CustomerRepository {
    private DatabaseReference databaseReference;

    public CustomerRepository() {
        databaseReference = FirebaseDatabase.getInstance().getReference("gm").child("customer");
    }

    public void saveCustomer(String userUID, Customer customer, MessageCallBack callBack){
        databaseReference.child(userUID).setValue(customer)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        callBack.onComplete(true,null);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callBack.onComplete(false,null);
                    }
                });
    }

    public ValueEventListnerHolder getCustomer(String customerUid, CustomerCallBack callBack){
        DatabaseReference newRef = databaseReference.child(customerUid);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Customer customer = snapshot.getValue(Customer.class);
                callBack.onCompleted(customer);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        return new ValueEventListnerHolder(newRef, newRef.addValueEventListener(eventListener));
    };

    public ValueEventListnerHolder getCustomerWishList(String customerUid, WishListCallBack callBack){

        DatabaseReference newRef = databaseReference.child(customerUid).child("wishList");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               ArrayList<String> wishlist = new ArrayList<>();
               for(DataSnapshot ds : snapshot.getChildren()){
                   wishlist.add(ds.getValue(String.class));
               }
                callBack.onDatacomlete(wishlist, true, null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callBack.onDatacomlete(null, false, null);
            }
        };
        return new ValueEventListnerHolder(newRef, newRef.addValueEventListener(eventListener));
    }

    // Wishlist
    public void updateWishList(String customerUid, ArrayList<String> wishList){
        DatabaseReference newRef = databaseReference.child(customerUid).child("wishList");
        newRef.setValue(wishList);
    }

    // Address
    public void saveAddressForCustomer(String customerUid, ArrayList<Address> addressList, MessageCallBack callBack){
        databaseReference.child(customerUid).child("address").setValue(addressList)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        callBack.onComplete(true,null);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callBack.onComplete(false,null);
                    }
                });
    }

    // Card
    public void saveCards(String customerUid, ArrayList<Card> cardList, MessageCallBack callBack){
        databaseReference.child(customerUid).child("card").setValue(cardList)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        callBack.onComplete(true,null);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callBack.onComplete(false,null);
                    }
                });
    }

    // Cart
    public void saveCart(String customerUid, ArrayList<CartItem> cart, MessageCallBack callBack){
        databaseReference.child(customerUid).child("cart").setValue(cart)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        if(callBack != null){
                            callBack.onComplete(true,null);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if(callBack != null){
                            callBack.onComplete(false,null);
                        }
                    }
                });
    }
    public void saveCartItemQty(String customerUID, String cartItemId, int qty, MessageCallBack callBack){
        databaseReference.child(customerUID).child("cart").child(cartItemId).child("qty").setValue(qty)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.v("ats3", "Saved!");
                        callBack.onComplete(true,null);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.v("ats3", "Failed!"+e.getMessage());
                        callBack.onComplete(false,null);
                    }
                });
    }
}
