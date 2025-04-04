package com.example.glimmerheaven.data.repository;

import androidx.annotation.NonNull;

import com.example.glimmerheaven.utils.callBacks.FireBaseAuthUserCallBack;
import com.example.glimmerheaven.utils.callBacks.ResultCallBack;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseAuthRepository {
    private FirebaseAuth firebaseAuth;

    public FirebaseAuthRepository() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void signOutCurrentUser(){
        firebaseAuth.signOut();
    }

    public FirebaseUser getCurrentUser(){
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        return currentUser;
    }

    public void createAUser(String email, String password, FireBaseAuthUserCallBack callBack){
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    callBack.onDataComplete(user);
                } else {
                    callBack.onDataComplete(null);
                }
            }
        });
    }

    public void loginUser(String email, String password, FireBaseAuthUserCallBack callBack){
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                    callBack.onDataComplete(currentUser);
                }else{
                    callBack.onDataComplete(null);
                }
            }
        });
    }

    public void sendPasswordResetEmail(String email, ResultCallBack callBack){
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                callBack.onDataComplete(email,null, true, "complete");
            }else{
                callBack.onDataComplete(email, null, false, "failed");
            }
        });
    }

}
