package com.example.glimmerheaven.ui.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.glimmerheaven.data.model.Order;
import com.example.glimmerheaven.data.model.OrderItem;
import com.example.glimmerheaven.data.repository.OrderRepository;
import com.example.glimmerheaven.data.repository.ProductRepository;
import com.example.glimmerheaven.utils.callBacks.ResultCallBack;
import com.example.glimmerheaven.utils.eventCanceller.ValueEventListnerHolder;

import java.util.ArrayList;

public class ViewOrderFromNotificationModel extends ViewModel {

    private MutableLiveData<Order> orderMutableLiveData = new MutableLiveData<>();
    private String loadedOrderId = null;
    private ValueEventListnerHolder orderVEHolder = null;
    public ViewOrderFromNotificationModel() {
    }

    public LiveData<Order> getSelectedOrder(String orderId) {
        Log.v("ats5","Here vof .............1");
        if(orderMutableLiveData.getValue() == null){
            Log.v("ats5","Here vof .............2");
            loadSelectedOrder(orderId);
        }
        return orderMutableLiveData;
    }

    private void loadSelectedOrder(String orderId) {
        orderVEHolder = new OrderRepository().searchOrderById(orderId, new ResultCallBack() {
            @Override
            public void onDataComplete(Object keys, Object values, boolean status, String message) {
                if(status){
                    loadedOrderId = (String) keys;
                    orderMutableLiveData.setValue((Order) values);
                }
            }
        });
    }
    public void getRelatedProducts(ResultCallBack callBack){
        ArrayList<String> productIds = new ArrayList<>();
        for(OrderItem oi : orderMutableLiveData.getValue().getOrderItemList()){
            productIds.add(oi.getProductId());
        }
        new ProductRepository().searchProductsOnce(productIds, callBack);
    }
    public String getLoadedOrderId() {
        return loadedOrderId;
    }

    public ValueEventListnerHolder getOrderVEHolder() {
        return orderVEHolder;
    }
}
