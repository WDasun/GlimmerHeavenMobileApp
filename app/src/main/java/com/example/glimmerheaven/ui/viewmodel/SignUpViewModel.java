package com.example.glimmerheaven.ui.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.glimmerheaven.data.model.Customer;
import com.example.glimmerheaven.data.repository.CustomerRepository;
import com.example.glimmerheaven.data.repository.FirebaseAuthRepository;
import com.example.glimmerheaven.utils.callBacks.FireBaseAuthUserCallBack;
import com.example.glimmerheaven.utils.callBacks.MessageCallBack;

public class SignUpViewModel extends ViewModel {
    public SignUpViewModel() {
    }

    public void createNewAccount(String email, String password, FireBaseAuthUserCallBack callBack){
        // Create new account for authentication process
        new FirebaseAuthRepository().createAUser(email, password, callBack);
    }
    public void saveCustomer(String userUID, Customer customer, MessageCallBack callBack){
        // Save a new Customer
        new CustomerRepository().saveCustomer(userUID, customer, callBack);
    }
}
