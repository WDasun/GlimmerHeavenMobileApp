package com.example.glimmerheaven.ui.adapters.productsAdapters;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.glimmerheaven.data.model.CartItem;
import com.example.glimmerheaven.data.model.CommentAndRating;
import com.example.glimmerheaven.data.model.Discount;
import com.example.glimmerheaven.data.model.Product;
import com.example.glimmerheaven.R;
import com.example.glimmerheaven.data.repository.CommentAndRatingRepository;
import com.example.glimmerheaven.data.repository.CustomerRepository;
import com.example.glimmerheaven.data.repository.FirebaseAuthRepository;
import com.example.glimmerheaven.utils.Ratings.CalculateAverageRate;
import com.example.glimmerheaven.utils.callBacks.MessageCallBack;
import com.example.glimmerheaven.utils.callBacks.ResultMapCallBack;
import com.example.glimmerheaven.utils.singleton.UserManage;

import java.util.ArrayList;
import java.util.Map;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {
    ArrayList<Product> productList;
    ArrayList<String> productId;
    ArrayList<String> currentUserWishList = UserManage.getInstance().getCurrentUser().getValue().getWishList();
    ArrayList<CartItem> currentUserCart = UserManage.getInstance().getCurrentUser().getValue().getCart();
    OnProductItemClickListner onProductItemClickListner;

    public ProductListAdapter(Map<String, Product> productMap, OnProductItemClickListner onProductItemClickListner) {
        this.onProductItemClickListner = onProductItemClickListner;
        this.productList = new ArrayList<>(productMap.values());
        this.productId = new ArrayList<>(productMap.keySet());
    }

    public interface OnProductItemClickListner{
        void onPoductItemClicked(String productItemId);
    }

    @NonNull
    @Override
    public ProductListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull ProductListAdapter.ViewHolder holder, int position) {
        String id = productId.get(position);
        Product product = productList.get(position);

        holder.getTxt_productName().setText(product.getName());
        holder.getTxt_price().setText(String.valueOf(product.getPrice())+" LKR");

        // Discount process
        if(product.getDiscount() != null){
            Discount discount = new ArrayList<Discount>(product.getDiscount().values()).get(0);
            long startDate = discount.getStartDate();
            long endDate = discount.getEndDate();
            long currentDate = System.currentTimeMillis();
            if(currentDate > startDate && currentDate < endDate){
                holder.txt_price.setPaintFlags(holder.txt_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                double newPrice = product.getPrice()-(product.getPrice()*(discount.getRate()/100));
                holder.txt_priceWithDiscount.setText(newPrice+"");
                holder.txt_priceWithDiscount.setVisibility(View.VISIBLE);
            }
        }
        // Rating manage
        new CommentAndRatingRepository().getProductRelatedCommentsAndRatings(
                id,
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
                                holder.ratingbar.setRating(average);
                                holder.txt_ratingCount.setText("(reviews "+(int)rateCount+")");
                            }else{
                                holder.ratingbar.setRating(0);
                                holder.txt_ratingCount.setText("(reviews "+0+")");
                            }
                        }else{
                            holder.ratingbar.setRating(0);
                            holder.txt_ratingCount.setText("(reviews "+0+")");
                        }
                    }
                }
        );

        // WishList Icon change
        if(currentUserWishList.contains(id)){
            holder.getImg_wishlist().setImageResource(R.drawable.heart_red);
        }else{
            holder.getImg_wishlist().setImageResource(R.drawable.heart);
        }

        // Cart Icon change
        if(!currentUserCart.isEmpty()){
            for(CartItem cartItem : currentUserCart){
                if(cartItem.getProductId().equals(id)){
                    holder.getImg_cart().setImageResource(R.drawable.cart_blue);
                    break;
                }else{
                    holder.getImg_cart().setImageResource(R.drawable.cart_black);
                }
            }
        }else{
            holder.getImg_cart().setImageResource(R.drawable.cart_black);
        }

        Glide.with(holder.itemView.getContext())
                .load(product.getImage())
                .into(holder.getImg_product());

        // Use this to identify the clicked product and move to the product view
        holder.getTxt_productName().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onProductItemClickListner != null){
                    onProductItemClickListner.onPoductItemClicked(id);
                }
            }
        });

        // Use to add cart
        holder.getImg_cart().setOnClickListener(view -> {
            // checking the product is already in the cart or remove it
            boolean notInCart = false;
                if(!currentUserCart.isEmpty()){
                    for(CartItem cartItem : currentUserCart){
                        if(cartItem.getProductId().equals(id)){
                            notInCart = false;
                            currentUserCart.remove(cartItem);
                            saveCart(holder.itemView.getContext(), false, holder.getImg_cart());
                            break;
                        }else{
                            notInCart = true;
                        }
                    }
                }else{
                    notInCart = true;
                }

            // Add new item to the cart
            if(notInCart){
                CartItem newCartItem = new CartItem();
                newCartItem.setProductId(id);
                newCartItem.setQty(1);
                currentUserCart.add(newCartItem);
                saveCart(holder.itemView.getContext(), true, holder.getImg_cart());
            }

        });

        // Use to add wishList
        holder.getImg_wishlist().setOnClickListener(view -> {
            if(currentUserWishList.contains(id)){
                currentUserWishList.remove(id);
                new CustomerRepository().updateWishList(new FirebaseAuthRepository().getCurrentUser().getUid(), currentUserWishList,null);
                holder.getImg_wishlist().setImageResource(R.drawable.heart);
            }else{
                currentUserWishList.add(id);
                new CustomerRepository().updateWishList(new FirebaseAuthRepository().getCurrentUser().getUid(), currentUserWishList,null);
                holder.getImg_wishlist().setImageResource(R.drawable.heart_red);
            }
        });
    }

    private void saveCart(Context context, boolean addToTheCart, ImageView view){
        String UID = new FirebaseAuthRepository().getCurrentUser().getUid();
        new CustomerRepository().saveCart(UID, currentUserCart, new MessageCallBack() {
            @Override
            public void onComplete(boolean status, String message) {
                if(status){
                    if(addToTheCart){
                        Toast.makeText(context, "Item added to the cart!", Toast.LENGTH_SHORT).show();
                        view.setImageResource(R.drawable.cart_blue);
                    }else{
                        Toast.makeText(context, "Item removed to the cart!", Toast.LENGTH_SHORT).show();
                        view.setImageResource(R.drawable.cart_black);
                    }
                }else{
                    if(addToTheCart){
                        Toast.makeText(context, "Add item to the cart failed!", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context, "Item removing failed!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_product,img_cart,img_wishlist;
        private TextView txt_productName, txt_ratingCount,txt_price,txt_priceWithDiscount;
        private RatingBar ratingbar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_product = itemView.findViewById(R.id.img_product_wishlist_item);
            img_cart = itemView.findViewById(R.id.img_cart);
            img_wishlist = itemView.findViewById(R.id.img_wishlist);
            txt_productName = itemView.findViewById(R.id.txt_product_name_wishlist_item);
            txt_ratingCount = itemView.findViewById(R.id.txt_rating_count);
            txt_price = itemView.findViewById(R.id.txt_price_wishlist_item);
            ratingbar = itemView.findViewById(R.id.rating_bar);
            txt_priceWithDiscount = itemView.findViewById(R.id.txt_totalpricewithdiscount_item);
        }

        public TextView getTxt_priceWithDiscount() {
            return txt_priceWithDiscount;
        }

        public void setTxt_priceWithDiscount(TextView txt_priceWithDiscount) {
            this.txt_priceWithDiscount = txt_priceWithDiscount;
        }

        public ImageView getImg_product() {
            return img_product;
        }

        public void setImg_product(ImageView img_product) {
            this.img_product = img_product;
        }

        public ImageView getImg_cart() {
            return img_cart;
        }

        public void setImg_cart(ImageView img_cart) {
            this.img_cart = img_cart;
        }

        public ImageView getImg_wishlist() {
            return img_wishlist;
        }

        public void setImg_wishlist(ImageView img_wishlist) {
            this.img_wishlist = img_wishlist;
        }

        public TextView getTxt_productName() {
            return txt_productName;
        }

        public void setTxt_productName(TextView txt_productName) {
            this.txt_productName = txt_productName;
        }

        public TextView getTxt_ratingCount() {
            return txt_ratingCount;
        }

        public void setTxt_ratingCount(TextView txt_ratingCount) {
            this.txt_ratingCount = txt_ratingCount;
        }

        public TextView getTxt_price() {
            return txt_price;
        }

        public void setTxt_price(TextView txt_price) {
            this.txt_price = txt_price;
        }

        public RatingBar getRatingbar() {
            return ratingbar;
        }

        public void setRatingbar(RatingBar ratingbar) {
            this.ratingbar = ratingbar;
        }
    }
}
