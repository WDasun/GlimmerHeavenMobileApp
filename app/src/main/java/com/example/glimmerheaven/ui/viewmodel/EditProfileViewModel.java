package com.example.glimmerheaven.ui.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.glimmerheaven.data.model.Customer;
import com.example.glimmerheaven.data.repository.CustomerRepository;
import com.example.glimmerheaven.data.repository.FirebaseAuthRepository;
import com.example.glimmerheaven.utils.callBacks.MessageCallBack;
import com.example.glimmerheaven.utils.singleton.UserManage;

public class EditProfileViewModel extends ViewModel {
    public EditProfileViewModel() {
    }

    public Customer getCurrentCustomer(){
        Customer customer = UserManage.getInstance().getCurrentUser().getValue();
        return customer;
    }

    public void updateCustomer(Customer customer,MessageCallBack callBack){
        String uid = new FirebaseAuthRepository().getCurrentUser().getUid();
        new CustomerRepository().saveCustomer(uid, customer, callBack);
    }
}
