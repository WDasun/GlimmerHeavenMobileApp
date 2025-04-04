package com.example.glimmerheaven.data.repository;

import androidx.annotation.NonNull;

import com.example.glimmerheaven.data.model.Order;
import com.example.glimmerheaven.utils.callBacks.MessageCallBack;
import com.example.glimmerheaven.utils.callBacks.ResultMapCallBack;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class OrderRepository {
    private DatabaseReference databaseReference;

    public OrderRepository() {
        databaseReference = FirebaseDatabase.getInstance().getReference("gm").child("order");
    }

    public void getSelectedOrdersOnce(ArrayList<String> orderIds, ResultMapCallBack callBack){
        Map<String, Order> orders = new HashMap<>();
        int orderIdsSize = orderIds.size();
        AtomicInteger count = new AtomicInteger(0);

        for(String orderId : orderIds){
            databaseReference.child(orderId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    DataSnapshot ds = task.getResult();
                    String Oid = ds.getKey();
                    Order order = ds.getValue(Order.class);
                    synchronized (this){
                        orders.put(Oid,order);
                    }
                    if(orderIdsSize == count.incrementAndGet()){
                        callBack.onCompleted(orders,true,null);
                    }
                }
            });
        }
    }

    public void addOrder(Order order,ArrayList<String> currentOrderIdList, String customerId, MessageCallBack callBack) {
        String key = databaseReference.push().getKey();
        String orderId = "ORDER"+key;
        currentOrderIdList.add(orderId);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        Map<String, Object> saves = new HashMap<>();
        saves.put("gm/order/" + orderId, order);
        saves.put("gm/customer/" + customerId + "/orders", currentOrderIdList);

        databaseReference.updateChildren(saves, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError de, DatabaseReference dr) {
                if (de != null) {
                    callBack.onComplete(false, de.getMessage());
                } else {
                    callBack.onComplete(true, "Success!");
                }
            }
        });
    }
}
