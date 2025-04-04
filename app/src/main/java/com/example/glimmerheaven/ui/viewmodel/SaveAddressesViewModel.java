package com.example.glimmerheaven.ui.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.glimmerheaven.data.model.Address;
import com.example.glimmerheaven.data.repository.CustomerRepository;
import com.example.glimmerheaven.data.repository.FirebaseAuthRepository;
import com.example.glimmerheaven.utils.callBacks.MessageCallBack;

import java.util.ArrayList;

public class SaveAddressesViewModel extends ViewModel {

    public SaveAddressesViewModel() {
    }

    public void saveNewAddress(ArrayList<Address> addressList, MessageCallBack callBack){
        if(addressList != null && callBack != null){
            String UID = new FirebaseAuthRepository().getCurrentUser().getUid();
            new CustomerRepository().saveAddressForCustomer(UID, addressList, callBack);
        }
    }

}
