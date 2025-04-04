package com.example.glimmerheaven.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.glimmerheaven.data.model.Product;
import com.example.glimmerheaven.data.repository.ProductRepository;
import com.example.glimmerheaven.utils.callBacks.ProductCallBack;
import com.example.glimmerheaven.utils.callBacks.ProductsCallBack;

import java.util.Map;

public class ProductViewViewModel extends ViewModel {
    private ProductRepository productRepository;
    private MutableLiveData<Product> productLiveData = new MutableLiveData<>();
    public ProductViewViewModel() {
        productRepository = new ProductRepository();
    }

    public LiveData<Product> getProduct(String productId){
        if(productLiveData.getValue() == null){
            loadProduct(productId);
        }
        return productLiveData;
    }

    private void loadProduct(String productId) {
        productRepository.searchProduct(productId, new ProductCallBack() {
            @Override
            public void onDataComplete(Product product) {
                productLiveData.setValue(product);
            }
        });
    }
}
