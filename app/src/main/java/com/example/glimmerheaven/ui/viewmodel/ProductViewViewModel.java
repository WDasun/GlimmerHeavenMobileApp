package com.example.glimmerheaven.ui.viewmodel;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.glimmerheaven.R;
import com.example.glimmerheaven.data.model.CartItem;
import com.example.glimmerheaven.data.model.CommentAndRating;
import com.example.glimmerheaven.data.model.Product;
import com.example.glimmerheaven.data.repository.CommentAndRatingRepository;
import com.example.glimmerheaven.data.repository.CustomerRepository;
import com.example.glimmerheaven.data.repository.FirebaseAuthRepository;
import com.example.glimmerheaven.data.repository.ProductRepository;
import com.example.glimmerheaven.utils.callBacks.MessageCallBack;
import com.example.glimmerheaven.utils.callBacks.ProductCallBack;
import com.example.glimmerheaven.utils.callBacks.ProductsCallBack;
import com.example.glimmerheaven.utils.callBacks.ResultMapCallBack;

import java.util.ArrayList;
import java.util.Map;

public class ProductViewViewModel extends ViewModel {
    private ProductRepository productRepository;
    private MutableLiveData<Product> productLiveData = new MutableLiveData<>();
    private MutableLiveData<Map<String, CommentAndRating>> productRelatedCommentsAndRatingsLiveData = new MutableLiveData<>();
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

    public LiveData<Map<String, CommentAndRating>> getproductRelatedCommentsAndRatingsLiveData(String productId){
        if(productRelatedCommentsAndRatingsLiveData.getValue() == null){
            loadProductRelatedCommentsAndRatings(productId);
        }
        return productRelatedCommentsAndRatingsLiveData;
    }

    private void loadProductRelatedCommentsAndRatings(String productId) {
        new CommentAndRatingRepository().getProductRelatedCommentsAndRatings(productId, new ResultMapCallBack() {
            @Override
            public void onCompleted(Map result, boolean status, String message) {
                if(status){
                    productRelatedCommentsAndRatingsLiveData.setValue(result);
                }
            }
        });
    }

    public void updateWishlist(Context context, ArrayList<String> wishList, ImageView view, boolean addToWishlist){
        new CustomerRepository().updateWishList(new FirebaseAuthRepository().getCurrentUser().getUid(), wishList, new MessageCallBack() {
            @Override
            public void onComplete(boolean status, String message) {
                if(status){
                    if(addToWishlist){
                        view.setImageResource(R.drawable.heart_red);
                        Toast.makeText(context,"Added to the Wishlist!",Toast.LENGTH_SHORT).show();
                    }else{
                        view.setImageResource(R.drawable.heart);
                        Toast.makeText(context,"Removed from the Wishlist!",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(context,"Failed!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void updateCart(ArrayList<CartItem> cart, MessageCallBack callBack){
        if(cart != null){
            String uid = new FirebaseAuthRepository().getCurrentUser().getUid();
            new CustomerRepository().saveCart(uid,cart, callBack);
        }
    }
}
