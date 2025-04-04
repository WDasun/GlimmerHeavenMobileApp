package com.example.glimmerheaven.ui.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.glimmerheaven.data.repository.FirebaseAuthRepository;
import com.google.firebase.auth.FirebaseUser;

public class MainViewModel extends ViewModel {
    private FirebaseAuthRepository authRepository;

    public MainViewModel() {
        authRepository = new FirebaseAuthRepository();
    }

    public FirebaseUser getCurrentUser(){
        return authRepository.getCurrentUser();
    }

}
