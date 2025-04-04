package com.example.glimmerheaven.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.glimmerheaven.data.model.Category;
import com.example.glimmerheaven.data.repository.CategoryRepository;
import com.example.glimmerheaven.utils.callBacks.CategoriesCallBack;

import java.util.ArrayList;
import java.util.Map;

public class CategoryViewModel extends ViewModel {

    CategoryRepository categoryRepository;
    private MutableLiveData<Map<String, Category>> catListLiveData = new MutableLiveData<>();
    public CategoryViewModel() {
        categoryRepository = new CategoryRepository();
    }

    public LiveData<Map<String, Category>> getCategoryList(){
        if(catListLiveData.getValue() == null){
            loadCategories();
        }
        return catListLiveData;
    }

    private void loadCategories() {
        categoryRepository.searchAllCategories(new CategoriesCallBack() {
            @Override
            public void onDataComplete(Map<String, Category> categoryMap) {
                catListLiveData.setValue(categoryMap);
            }
        });
    }

}
