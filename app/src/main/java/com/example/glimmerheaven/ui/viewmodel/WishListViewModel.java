package com.example.glimmerheaven.ui.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.glimmerheaven.data.model.Product;
import com.example.glimmerheaven.data.repository.CustomerRepository;
import com.example.glimmerheaven.data.repository.FirebaseAuthRepository;
import com.example.glimmerheaven.data.repository.ProductRepository;
import com.example.glimmerheaven.utils.callBacks.ResultCallBack;
import com.example.glimmerheaven.utils.callBacks.WishListCallBack;
import com.example.glimmerheaven.utils.eventCanceller.ValueEventListnerHolder;
import com.example.glimmerheaven.utils.singleton.UserManage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WishListViewModel extends ViewModel {
    private MutableLiveData<Map<String, Product>> wishlistProductList = new MutableLiveData<>();
    private ValueEventListnerHolder wishlistVEL;
    public WishListViewModel() {
    }

    public LiveData<Map<String, Product>> getwishListProductListLiveData(){
        if(wishlistProductList.getValue() == null){
            loadWishList();
        }
        return wishlistProductList;
    }
    private void loadWishList(){

        wishlistVEL = new CustomerRepository().getCustomerWishList(new FirebaseAuthRepository().getCurrentUser().getUid(), new WishListCallBack() {
            @Override
            public void onDatacomlete(ArrayList<String> wishlist, boolean status, String message) {
                if(wishlist != null){
                    new ProductRepository().searchProductsOnce(wishlist, new ResultCallBack() {
                        @Override
                        public void onDataComplete(Object keys, Object values, boolean status, String message) {

                            Map<String, Product> productMap = new HashMap<>();

                            ArrayList<String> fetchedPIDs = (ArrayList<String>) keys;
                            ArrayList<Product> fetchedProducts = (ArrayList<Product>) values;

                            for (int i = 0; i < fetchedPIDs.size(); i++){
                                productMap.put(fetchedPIDs.get(i), fetchedProducts.get(i));
                            }

                            wishlistProductList.setValue(productMap);
                        }
                    });
                }else{
                    wishlistProductList.setValue(null);
                }
            }
        });
    }

    public ValueEventListnerHolder getWishlistVEL(){
        return wishlistVEL;
    }
}
