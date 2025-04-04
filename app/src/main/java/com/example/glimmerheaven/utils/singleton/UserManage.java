package com.example.glimmerheaven.utils.singleton;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.glimmerheaven.data.model.Customer;
import com.example.glimmerheaven.data.repository.CustomerRepository;
import com.example.glimmerheaven.utils.callBacks.CustomerCallBack;
import com.example.glimmerheaven.utils.callBacks.MessageCallBack;

public class UserManage {
    // Not need to fetch customer details again and again. You can get and update customer details here
    static UserManage instance = new UserManage();
    MutableLiveData<Customer> currentUserLiveData = new MutableLiveData<>();

    private UserManage(){}

    public static UserManage getInstance(){
        return instance;
    }

    public LiveData<Customer> getCurrentUser() {
        return currentUserLiveData;
    }

    public void setCurrentUser(String customerUid, MessageCallBack callBack){
        new CustomerRepository().getCustomer(customerUid, new CustomerCallBack() {
            @Override
            public void onCompleted(Customer customer) {
                if(customer != null){
                    currentUserLiveData.setValue(customer);
                    if(callBack != null){
                        callBack.onComplete(true, "Completed !");
                    }
                }
            }
        });
    }
}
