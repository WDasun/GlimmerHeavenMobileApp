package com.example.glimmerheaven.ui.fragments.productsFragments;

import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.glimmerheaven.R;
import com.example.glimmerheaven.data.model.CartItem;
import com.example.glimmerheaven.data.model.CommentAndRating;
import com.example.glimmerheaven.data.model.Discount;
import com.example.glimmerheaven.data.model.Product;
import com.example.glimmerheaven.data.repository.CommentAndRatingRepository;
import com.example.glimmerheaven.data.repository.FirebaseAuthRepository;
import com.example.glimmerheaven.ui.viewmodel.ProductViewViewModel;
import com.example.glimmerheaven.utils.Ratings.CalculateAverageRate;
import com.example.glimmerheaven.utils.callBacks.ResultMapCallBack;
import com.example.glimmerheaven.utils.dialogs.AddCommentFragmentDialog;
import com.example.glimmerheaven.utils.dialogs.BuyNowProductDialog;
import com.example.glimmerheaven.utils.singleton.UserManage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class ProductViewFragment extends Fragment {
    private String productId;
    private ProductViewViewModel productViewViewModel;
    private LinearLayout lly_other, lly_comment;
    private CardView card_discount;
    private TextView txt_productName,txt_price,txt_stockStatus, txt_description, txt_priceWithDiscount;
    private TextView txt_addToCart, txt_buyNow, txt_review;
    private RatingBar rb_ratingbar;
    private ImageView img_product,img_addToWishList, img_back;
    private int colorInStock, colorOutOfStock;
    private boolean productInCart = false;
    private Product relatedProduct;
    private final String CART_REMOVE_TEXT = "Remove from cart";
    private final String CART_ADD_TEXT = "Add to cart";
    private final String CART_ADD_TOAST_MESSAGE = "Added to the cart!";
    private final String CART_REMOVE_TOAST_MESSAGE = "Removed from the cart!";
    private final String CART_FAILED_TOAST_MESSAGE = "Failed!";
    private final int CART_DEFAULT_ITEM_QTY = 1;

    public ProductViewFragment() {
        super(R.layout.fragment_product_view);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.v("ats6","In ProductViewFragment");
        productId = getArguments().getString("productId");
        txt_productName = view.findViewById(R.id.txt_product_name_productview);
        txt_price = view.findViewById(R.id.txt_price_productview);
        txt_stockStatus = view.findViewById(R.id.txt_stock_status_productview);
        txt_description = view.findViewById(R.id.txt_description_productview);
        txt_priceWithDiscount = view.findViewById(R.id.txt_discountprice_productview);
        txt_buyNow = view.findViewById(R.id.txt_buynow_productview);
        txt_addToCart = view.findViewById(R.id.txt_addtocart_productview);
        txt_review = view.findViewById(R.id.txt_review_productview);
        rb_ratingbar = view.findViewById(R.id.rb_rate_productview);
        lly_other = view.findViewById(R.id.lly_other_productview);
        lly_comment = view.findViewById(R.id.LinearLayout_comment);
        img_product = view.findViewById(R.id.img_product_image_productview);
        img_back = view.findViewById(R.id.img_back_productview);
        img_addToWishList = view.findViewById(R.id.img_addtowishlist_productview);
        card_discount = view.findViewById(R.id.card_discount_productview);
        colorInStock = ContextCompat.getColor(view.getContext(), R.color.InStock);
        colorOutOfStock = ContextCompat.getColor(view.getContext(), R.color.OutOfStock);

        card_discount.setVisibility(View.GONE);

        productViewViewModel = new ViewModelProvider(this).get(ProductViewViewModel.class);

        // Cart pre settings
        ArrayList<CartItem> cartItems = UserManage.getInstance().getCurrentUser().getValue().getCart();
        if(cartItems != null){
            for(CartItem item : cartItems){
                if(item.getProductId().equals(productId)){
                    productInCart = true;
                    break;
                }
            }
        }
        if(productInCart){
            txt_addToCart.setText(CART_REMOVE_TEXT);
        }else{
            txt_addToCart.setText(CART_ADD_TEXT);
        }


        // Wishlist pre settings
        img_addToWishList.setImageResource(R.drawable.heart);
        ArrayList<String> wishList = UserManage.getInstance().getCurrentUser().getValue().getWishList();
        for(String wPid : wishList){
            if(wPid.equals(productId)){
                img_addToWishList.setImageResource(R.drawable.heart_red);
                break;
            }
        }

        // Rating bar manage
        new CommentAndRatingRepository().getProductRelatedCommentsAndRatings(
                productId,
                new ResultMapCallBack() {
                    @Override
                    public void onCompleted(Map result, boolean status, String message) {
                        if(status){
                            Map<String, CommentAndRating> commentsAndRatingMap = result;
                            if(commentsAndRatingMap.size() != 0){
                                float rateCount = commentsAndRatingMap.size();
                                ArrayList<Float> ratings = new ArrayList<>();
                                for(CommentAndRating ct : commentsAndRatingMap.values()){
                                    ratings.add(ct.getRate());
                                }
                                float average = CalculateAverageRate.getRoundedAverage(rateCount,ratings);
                                rb_ratingbar.setRating(average);
                                txt_review.setText("(reviews "+(int)rateCount+")");
                            }else{
                                rb_ratingbar.setRating(0);
                                txt_review.setText("(reviews "+0+")");
                            }
                        }else{
                            rb_ratingbar.setRating(0);
                            txt_review.setText("(reviews "+0+")");
                        }
                    }
                }
        );

        // Observation
        productViewViewModel.getProduct(productId).observe(getViewLifecycleOwner(), new Observer<Product>() {
            @Override
            public void onChanged(Product product) {
                relatedProduct = product;
                txt_productName.setText(product.getName());
                txt_price.setText(String.valueOf(product.getPrice()));
                txt_description.setText(product.getDescription());
                int qty = product.getQty();
                if(qty < 1){
                    txt_stockStatus.setText("Out of stock");
                    txt_stockStatus.setTextColor(colorOutOfStock);
                }else{
                    txt_stockStatus.setText("Instock");
                    txt_stockStatus.setTextColor(colorInStock);
                }
                // Set image
                Glide.with(view)
                        .load(product.getImage())
                        .into(img_product);

                // Discount check
                if(product.getDiscount() != null){
                    Discount discount = new ArrayList<Discount>(product.getDiscount().values()).get(0);
                    long startDate = discount.getStartDate();
                    long endDate = discount.getEndDate();
                    long currentDate = System.currentTimeMillis();
                    if(currentDate > startDate && currentDate < endDate){
                        txt_price.setPaintFlags(txt_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        double newPrice = product.getPrice()-(product.getPrice()*(discount.getRate()/100));
                        txt_priceWithDiscount.setText("LKR "+newPrice+" (5.0% Saving!)");
                        card_discount.setVisibility(View.VISIBLE);
                    }
                }
                // variations adding
                LayoutInflater inflater = getLayoutInflater();
                for(String name : product.getVariationsOptions().keySet()){
                    String value = product.getVariationsOptions().get(name);

                    View singleVariation = inflater.inflate(R.layout.single_variation_productview_layout,lly_other,false);

                    TextView txt_name = singleVariation.findViewById(R.id.variation_name_svpl);
                    TextView txt_value = singleVariation.findViewById(R.id.variation_value_svpl);

                    txt_name.setText(name);
                    txt_value.setText(value);

                    lly_other.addView(singleVariation);
                }

                //Comments and ratings
                productViewViewModel.getproductRelatedCommentsAndRatingsLiveData(productId).observe(getViewLifecycleOwner(), cAndRMap -> {
                    lly_comment.removeAllViews();
                    // First add user related comment
                    String uid = new FirebaseAuthRepository().getCurrentUser().getUid();
                    View userRelatedCAndRLayout = getLayoutInflater().inflate(R.layout.single_user_related_comment_layout, lly_comment, false);
                    Button btn_addComment = userRelatedCAndRLayout.findViewById(R.id.btn_addcommentandrating_surcl);
                    RelativeLayout ry_comment = userRelatedCAndRLayout.findViewById(R.id.rl_comment_surcl);
                    TextView name = userRelatedCAndRLayout.findViewById(R.id.txt_name_surcl);
                    RatingBar ratingBar = userRelatedCAndRLayout.findViewById(R.id.rb_rate_surcl);
                    TextView date = userRelatedCAndRLayout.findViewById(R.id.txt_commented_date_surcl);
                    TextView comment = userRelatedCAndRLayout.findViewById(R.id.txt_comment_surcl);
                    Button editComment = userRelatedCAndRLayout.findViewById(R.id.btn_editcommentandrating_surcl);
                    if(cAndRMap.get(uid) == null){
                        ry_comment.setVisibility(View.GONE);
                        btn_addComment.setVisibility(View.VISIBLE);
                        btn_addComment.setOnClickListener(view1 -> {
                            openAddCommentDialog(false, productId,null);
                        });
                    }else{
                        ry_comment.setVisibility(View.VISIBLE);
                        CommentAndRating car = cAndRMap.get(uid);
                        comment.setText(car.getComment());
                        name.setText(car.getUserName());
                        ratingBar.setRating(car.getRate());
                        date.setText(new SimpleDateFormat("yyyy/MM/dd").format(new Date(car.getChangedDate())));
                        btn_addComment.setVisibility(View.GONE);
                        editComment.setOnClickListener(view1 -> {
                            openAddCommentDialog(true,productId, car);
                        });
                    }
                    lly_comment.addView(userRelatedCAndRLayout);
                    // Secondly add other users comments
                    if(cAndRMap != null){
                        for(String key : cAndRMap.keySet()){
                            if(!key.equals(uid)){
                                View singleCommentLayout = getLayoutInflater().inflate(R.layout.single_comment_layout, lly_comment, false);
                                CommentAndRating car = cAndRMap.get(key);
                                TextView scl_name = singleCommentLayout.findViewById(R.id.txt_name_scl);
                                RatingBar scl_ratingBar = singleCommentLayout.findViewById(R.id.rb_rate_scl);
                                TextView scl_date = singleCommentLayout.findViewById(R.id.txt_commented_date_scl);
                                TextView scl_comment = singleCommentLayout.findViewById(R.id.txt_comment_scl);
                                scl_name.setText(car.getUserName());
                                scl_comment.setText(car.getComment());
                                scl_ratingBar.setRating(car.getRate());
                                scl_date.setText(new SimpleDateFormat("yyyy/MM/dd").format(new Date(car.getChangedDate())));
                                lly_comment.addView(singleCommentLayout);
                            }
                        }
                    }
                });
            }
        });

        // Wishlist
        img_addToWishList.setOnClickListener(view1 -> {
            if(wishList.contains(productId)){
                wishList.remove(productId);
                productViewViewModel.updateWishlist(getContext(),wishList,img_addToWishList,false);
            }else{
                wishList.add(productId);
                productViewViewModel.updateWishlist(getContext(),wishList,img_addToWishList,true);
            }
        });

        // Cart
        txt_addToCart.setOnClickListener(view1 -> {
            if(productInCart){
                //remove form the cart
                ArrayList<CartItem> cits = UserManage.getInstance().getCurrentUser().getValue().getCart();
                for(CartItem cartItem : cits){
                    if(cartItem.getProductId().equals(productId)){
                        cits.remove(cartItem);
                        break;
                    }
                }
                productViewViewModel.updateCart(cits,(status, message) -> {
                    if(status){
                        productInCart = false;
                        txt_addToCart.setText(CART_ADD_TEXT);
                        Toast.makeText(getContext(),CART_REMOVE_TOAST_MESSAGE,Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getContext(),CART_FAILED_TOAST_MESSAGE,Toast.LENGTH_SHORT).show();
                    }
                });
            }else{
                // Add to the cart
                ArrayList<CartItem> cits = UserManage.getInstance().getCurrentUser().getValue().getCart();
                CartItem cartItem = new CartItem(
                        productId,
                        CART_DEFAULT_ITEM_QTY
                );
                cits.add(cartItem);
                productViewViewModel.updateCart(cits,(status, message) -> {
                    if(status){
                        productInCart = true;
                        txt_addToCart.setText(CART_REMOVE_TEXT);
                        Toast.makeText(getContext(),CART_ADD_TOAST_MESSAGE,Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getContext(),CART_FAILED_TOAST_MESSAGE,Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        // Buy Now
        txt_buyNow.setOnClickListener(view1 -> {
            BuyNowProductDialog buyNowProductDiaog = new BuyNowProductDialog();
            Bundle bundle =  new Bundle();
            bundle.putString("pid",productId);
            bundle.putInt("qty", relatedProduct.getQty());
            bundle.putInt("fcid", R.id.fc_main_bpna);
            buyNowProductDiaog.setArguments(bundle);
            buyNowProductDiaog.show(getParentFragmentManager(),"");
        });

        // Back
        img_back.setOnClickListener(view1 -> {
            requireActivity().onBackPressed();
        });
    }

    private void openAddCommentDialog(boolean update, String productId, CommentAndRating car){
        AddCommentFragmentDialog dialog = new AddCommentFragmentDialog();
        Bundle bundle = new Bundle();
        bundle.putBoolean("update",update);
        bundle.putString("productId",productId);
        if(car != null){
            bundle.putString("comment",car.getComment());
            bundle.putString("rating",String.valueOf(car.getRate()));
        }
        dialog.setArguments(bundle);
        dialog.show(getChildFragmentManager(),"");
    }
}