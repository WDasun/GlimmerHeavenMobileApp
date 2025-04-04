package com.example.glimmerheaven.ui.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.glimmerheaven.data.repository.FirebaseAuthRepository;
import com.example.glimmerheaven.utils.callBacks.ResultCallBack;

public class FogotPasswordViewModel extends ViewModel {
    public FogotPasswordViewModel() {
    }

    public void sendResetPasswordLink(String email, ResultCallBack callBack){
        new FirebaseAuthRepository().sendPasswordResetEmail(email, callBack);
    }
}
