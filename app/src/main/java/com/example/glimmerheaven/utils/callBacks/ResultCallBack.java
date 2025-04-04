package com.example.glimmerheaven.utils.callBacks;

public interface ResultCallBack <A, B>{
    void onDataComplete(A keys, B values, boolean status, String message);
}
