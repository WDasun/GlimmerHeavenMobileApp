package com.example.glimmerheaven.ui.viewmodel;

import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.glimmerheaven.data.model.Product;
import com.example.glimmerheaven.data.repository.ProductRepository;
import com.example.glimmerheaven.data.repository.VariationAndValuesRepository;
import com.example.glimmerheaven.utils.callBacks.ProductsCallBack;
import com.example.glimmerheaven.utils.callBacks.VariationsAndValuesCallBack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductListViewModel extends ViewModel {
    private MutableLiveData<Map<String, Product>> productsLiveData = new MutableLiveData<>();
    private MutableLiveData<Map<String, Product>> filteredProductsLiveData = new MutableLiveData<>();
    private MutableLiveData<Map<String, ArrayList<String>>> variationsLiveData = new MutableLiveData<>();
    private Map<String,String> previouslySelectedVariations = null;
    public ProductListViewModel() {

    }

    public Map<String, String> getPreviouslySelectedVariations() {
        return previouslySelectedVariations;
    }

    public LiveData<Map<String, ArrayList<String>>> getVariationsLiveData(String categoryId){
        if(variationsLiveData.getValue() == null){
            loadVariations(categoryId);
        }

        return variationsLiveData;
    }
    private void loadVariations(String categoryId){
        new VariationAndValuesRepository().searchAllVariationsAndValues(categoryId, variationsAndValues -> {
            if(variationsAndValues != null){
                variationsLiveData.setValue(variationsAndValues);
            }
        });
    }
    public LiveData<Map<String, Product>> getFilteredProductsLiveData(String categoryId, Map<String,String> optionVariations, LifecycleOwner lifecycleOwner){

        if(productsLiveData.getValue() == null){
            loadCategoryRelatedProducts(categoryId);
        }
        productsLiveData.observe(lifecycleOwner, productMap -> {
            previouslySelectedVariations = optionVariations;
            if(optionVariations != null){
                filter(optionVariations);
            }else{
                filteredProductsLiveData.setValue(productsLiveData.getValue());
            }
        });
        return filteredProductsLiveData;
    }

    private void loadCategoryRelatedProducts(String categoryId){
        new ProductRepository().searchAllProductsInCategory(categoryId, new ProductsCallBack() {
            @Override
            public void onDataComplete(Map<String, Product> productsMap) {
                productsLiveData.setValue(productsMap);
            }
        });

    }
    private void filter(Map<String,String> optionVariations){
        if(optionVariations != null){
            Map<String, Product> currentProducts = productsLiveData.getValue();
            Map<String, Product> newFilteredProducts = new HashMap<>();
            for(String pid : currentProducts.keySet()){
                boolean satisfied = false;
                Product product = currentProducts.get(pid);
                for(String optionVariation : optionVariations.keySet()){
                    if(product.getVariationsOptions().containsKey(optionVariation)){
                        String optionVariationValue = optionVariations.get(optionVariation);
                        String productVariationValue = product.getVariationsOptions().get(optionVariation);
                        if(optionVariationValue.equals(productVariationValue)){
                            satisfied = true;
                        }else{
                            satisfied = false;
                            break;
                        }
                    }else{
                        satisfied = false;
                        break;
                    }
                }
                if(satisfied){
                    newFilteredProducts.put(pid,product);
                }
            }
            filteredProductsLiveData.setValue(newFilteredProducts);
        }
    }
}
