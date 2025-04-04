package com.example.glimmerheaven.ui.viewmodel;

import android.util.Log;

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
import java.util.Map;

public class ProductListViewModel extends ViewModel {

    private String categoryId;
    private ProductRepository productRepository;
    private MutableLiveData<Map<String, Product>> productsLiveData = new MutableLiveData<>();
    private MutableLiveData<Map<String, ArrayList<String>>> variationsAndValuesLiveData = new MutableLiveData<>();
    private Map<String, Product> productsBackup;
    public ProductListViewModel() {
        productRepository = new ProductRepository();
    }

    public LiveData<Map<String, ArrayList<String>>> getVariationsAndValuesList(){
        // Use to return fetched Variations and values

        if(variationsAndValuesLiveData.getValue() == null && categoryId != null){
            loadVariationsAndValues(categoryId);
        }
        return variationsAndValuesLiveData;
    }

    private void loadVariationsAndValues(String catId) {
        // fetched Variations and values
        new VariationAndValuesRepository().searchAllVariationsAndValues(catId, new VariationsAndValuesCallBack() {
            @Override
            public void onDataComplete(Map<String, ArrayList<String>> variationsAndValues) {
                Log.v("at2", "v&v fetch called !");
                variationsAndValuesLiveData.setValue(variationsAndValues);
            }
        });
    }

    public void showAll(){
        if(productsBackup != null){
            productsLiveData.setValue(new HashMap<>(productsBackup));
            productsBackup = null;
        }
    }

    public void filterData(ArrayList<String> variationValues){
        // Remove Product from "productsLiveData" that dont have given variation values
        Map<String, Product> products;
        if(productsBackup == null){
            Log.v("at2", "in true");
            products = productsLiveData.getValue();
            productsBackup = new HashMap<>(products);
        }else{
            Log.v("at2", "in false");
            products = new HashMap<>(productsBackup);
        }

        for(String productId : new ArrayList<>(products.keySet())){
            Map<String, String> productVariations = products.get(productId).getVariationsOptions();
            for(String value : variationValues){
                boolean contained = productVariations.containsValue(value);
                if(!contained){
                    products.remove(productId);
                    break;
                }
            }
        }
        productsLiveData.setValue(products);
    }

    //
    public LiveData<Map<String, Product>> getProductList(){
        // Use to return fetched product

        if(productsLiveData.getValue() == null && categoryId != null){
            loadProducts(categoryId);
        }
        return productsLiveData;
    }

    private void loadProducts(String catId) {
        // fetched product

        productRepository.searchAllProductsInCategory(catId, new ProductsCallBack() {
            @Override
            public void onDataComplete(Map<String, Product> productMap) {
                Log.v("at2", "p fetch called !");
                productsLiveData.setValue(productMap);
            }
        });
    }


    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
