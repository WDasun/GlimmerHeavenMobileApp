package com.example.glimmerheaven.utils.callBacks;

import com.example.glimmerheaven.data.model.Category;
import java.util.Map;

public interface CategoriesCallBack {
    void onDataComplete(Map<String, Category> categoryMap);
}
