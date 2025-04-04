package com.example.glimmerheaven.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.glimmerheaven.data.model.Order;
import com.example.glimmerheaven.data.model.OrderItem;
import com.example.glimmerheaven.data.repository.OrderRepository;
import com.example.glimmerheaven.data.repository.ProductRepository;
import com.example.glimmerheaven.ui.activities.OrdersActivity;
import com.example.glimmerheaven.utils.callBacks.ResultCallBack;
import com.example.glimmerheaven.utils.callBacks.ResultMapCallBack;
import com.example.glimmerheaven.utils.singleton.UserManage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderViewModel extends ViewModel {
    private MutableLiveData<Map<String,Order>> ordersLiveData = new MutableLiveData<>();
    private String selectedOrderId;
    public OrderViewModel() {
    }

    public String getSelectedOrderId() {
        return selectedOrderId;
    }

    public void setSelectedOrderId(String selectedOrderId) {
        this.selectedOrderId = selectedOrderId;
    }

    // Give selected Order
    public Order getSelectedOrderAndId(){
        return ordersLiveData.getValue().get(selectedOrderId);
    }

    // Get Products related to oderItems
    public void getRelatedOrders(ResultCallBack callBack){
        ArrayList<String> productIds = new ArrayList<>();
        for(OrderItem oi : ordersLiveData.getValue().get(selectedOrderId).getOrderItemList()){
            productIds.add(oi.getProductId());
        }
        new ProductRepository().searchProductsOnce(productIds, callBack);
    }

    // Load Orders live data
    public void loadOrdersLiveData(ArrayList<String> orderIds){
       new OrderRepository().getSelectedOrdersOnce(orderIds, new ResultMapCallBack() {
           @Override
           public void onCompleted(Map result, boolean status, String message) {
               if(result != null){
                   ordersLiveData.setValue(result);
               }
           }
       });
    }

    // Get All orders
    public LiveData<Map<String,Order>> getOrdersLiveData(){
        return ordersLiveData;
    }
}
