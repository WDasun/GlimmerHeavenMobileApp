package com.example.glimmerheaven.utils.callBacks;

import com.example.glimmerheaven.data.model.Order;

import java.util.Map;

public interface ResultMapCallBack <A,B>{
    void onCompleted(Map<A, B> result, boolean status, String message);
}
