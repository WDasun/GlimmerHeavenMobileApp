package com.example.glimmerheaven.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.glimmerheaven.data.model.Customer;
import com.example.glimmerheaven.data.repository.CustomerRepository;
import com.example.glimmerheaven.data.repository.FirebaseAuthRepository;
import com.example.glimmerheaven.utils.callBacks.CustomerCallBack;
import com.example.glimmerheaven.utils.eventCanceller.ValueEventListnerHolder;

import java.util.Map;

public class ProfileViewModel extends ViewModel {

    public ProfileViewModel() {
    }

}
