package com.example.glimmerheaven.utils.callBacks;

import com.google.firebase.auth.FirebaseUser;

public interface FireBaseAuthUserCallBack {
    void onDataComplete(FirebaseUser currentUser);
}
