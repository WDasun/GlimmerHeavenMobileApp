package com.example.glimmerheaven.utils.callBacks;

import com.example.glimmerheaven.data.model.Product;

import java.util.Map;

public interface ProductsCallBack {
    void onDataComplete(Map<String, Product> productsMap);
}
