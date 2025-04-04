package com.example.glimmerheaven.ui.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.glimmerheaven.data.model.Card;
import com.example.glimmerheaven.data.repository.CustomerRepository;
import com.example.glimmerheaven.data.repository.FirebaseAuthRepository;
import com.example.glimmerheaven.utils.callBacks.MessageCallBack;
import com.example.glimmerheaven.utils.singleton.UserManage;

import java.util.ArrayList;

public class SaveCardsViewModel extends ViewModel {
    public SaveCardsViewModel() {
    }

    public void saveCard(ArrayList<Card> cardList, MessageCallBack callBack){
        String UID = new FirebaseAuthRepository().getCurrentUser().getUid();
        if(cardList != null && callBack != null){
            new CustomerRepository().saveCards(UID, cardList, callBack);
        }
    }
}
