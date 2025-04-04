package com.example.glimmerheaven.ui.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.glimmerheaven.data.model.Customer;
import com.example.glimmerheaven.data.repository.FirebaseAuthRepository;
import com.example.glimmerheaven.utils.callBacks.FireBaseAuthUserCallBack;

public class SignInViewModel extends ViewModel {
    public SignInViewModel() {
    }

    public void login(String email, String password, FireBaseAuthUserCallBack callBack) {
        new FirebaseAuthRepository().loginUser(email, password, callBack);
    }
}
