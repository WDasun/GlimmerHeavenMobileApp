package com.example.glimmerheaven.ui.adapters.CartAdapters;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.glimmerheaven.R;
import com.example.glimmerheaven.data.model.CartItem;
import com.example.glimmerheaven.data.model.Discount;
import com.example.glimmerheaven.data.model.Product;
import com.example.glimmerheaven.data.repository.CustomerRepository;
import com.example.glimmerheaven.data.repository.DiscountRepository;
import com.example.glimmerheaven.data.repository.FirebaseAuthRepository;
import com.example.glimmerheaven.data.repository.ProductRepository;
import com.example.glimmerheaven.ui.viewmodel.CartViewModel;
import com.example.glimmerheaven.utils.callBacks.MessageCallBack;
import com.example.glimmerheaven.utils.callBacks.ResultCallBack;
import com.example.glimmerheaven.utils.singleton.UserManage;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class CartItemListAdapter extends RecyclerView.Adapter<CartItemListAdapter.holder> {

    private ArrayList<CartItem> currentUserCart;
    private Context context;
    private CartViewModel cartViewModel;

    public CartItemListAdapter(ArrayList<CartItem> currentUserCart, Context context, CartViewModel cartViewModel) {
        this.currentUserCart = currentUserCart;
        this.context = context;
        this.cartViewModel = cartViewModel;
    }

    @NonNull
    @Override
    public CartItemListAdapter.holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_cart_item_layout, parent, false);
        return new holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemListAdapter.holder holder, int position) {
        CartItem cartItem = currentUserCart.get(position);
        new ProductRepository().searchProduct(cartItem.getProductId(), product -> {

            // Check if the cart item selected in the beginning
            if(cartViewModel.getPreOrder().getSelectedCartItemList().contains(cartItem)){
                holder.cb_select.setChecked(true);
            }

            String UID = new FirebaseAuthRepository().getCurrentUser().getUid();

            Glide.with(holder.itemView.getContext()).load(product.getImage()).into(holder.img_product);
            holder.txt_productName.setText(product.getName());
            holder.txt_price.setText("LKR "+product.getPrice());
            holder.txt_qty.setText(String.valueOf(cartItem.getQty()));

            // Discount related process
            if(product.getDiscount() != null){
                Map<String,Discount> discount = product.getDiscount();
                String discountID = new ArrayList<String>(discount.keySet()).get(0);
                new DiscountRepository().getDiscount(discountID, new ResultCallBack() {
                    @Override
                    public void onDataComplete(Object keys, Object values, boolean status, String message) {
                        String dID = (String) keys;
                        Discount discount = (Discount) values;
                        long currentTime = System.currentTimeMillis();
                        if(currentTime > discount.getStartDate() && currentTime < discount.getEndDate()){
                            holder.txt_price.setPaintFlags(holder.txt_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                            double newPrice = product.getPrice()-(product.getPrice()*(discount.getRate()/100));
                            holder.txt_priceWithDiscount.setText("LKR "+newPrice+" ("+discount.getRate()+"% Saving!)");
                            holder.txt_priceWithDiscount.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }

            holder.img_plus.setOnClickListener(view -> {
                if((cartItem.getQty()+1) <= product.getQty()){
                    int newQty = cartItem.getQty()+1;
                    new CustomerRepository().saveCartItemQty(UID, String.valueOf(position), newQty, new MessageCallBack() {
                        @Override
                        public void onComplete(boolean status, String message) {

                        }
                    });
                }
            });

            holder.img_minus.setOnClickListener(view -> {
                if((cartItem.getQty()-1) >= 1){
                    int newQty = cartItem.getQty()-1;
                    new CustomerRepository().saveCartItemQty(UID, String.valueOf(position), newQty, new MessageCallBack() {
                        @Override
                        public void onComplete(boolean status, String message) {

                        }
                    });
                }
            });

            // Remove process
            holder.rl_remove.setOnClickListener(view -> {
                if(currentUserCart.contains(cartItem)){
                    currentUserCart.remove(cartItem);
                    new CustomerRepository().saveCart(UID, currentUserCart, new MessageCallBack() {
                        @Override
                        public void onComplete(boolean status, String message) {
                            if(status){
                                Toast.makeText(context, "Item removed!", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(context, "Failed to remove the item!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });

            // Select process
            // add or remove items from the "preOrder" object's "selectedCartItemList"
            holder.cb_select.setOnClickListener(view -> {
                if(holder.cb_select.isChecked()){
                    if(!cartViewModel.getPreOrder().getSelectedCartItemList().contains(cartItem)){
                        cartViewModel.getPreOrder().getSelectedCartItemList().add(cartItem);
                    }else{
                        Toast.makeText(context, "Already selected!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    if(cartViewModel.getPreOrder().getSelectedCartItemList().contains(cartItem)){
                        cartViewModel.getPreOrder().getSelectedCartItemList().remove(cartItem);
                    }else{
                        Toast.makeText(context, "Item already not selected!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });

    }

    @Override
    public int getItemCount() {
        if(currentUserCart != null){
            return currentUserCart.size();
        }else{
            return 0;
        }
    }

    public class holder extends RecyclerView.ViewHolder {
        private ImageView img_product,img_plus,img_minus;
        private TextView txt_qty, txt_productName,txt_review,txt_price, txt_priceWithDiscount;
        private RelativeLayout rl_remove;
        private RatingBar rb_ratingBar;
        private CheckBox cb_select;
        public holder(@NonNull View itemView) {
            super(itemView);
            img_product = itemView.findViewById(R.id.img_product_singlecartitem);
            img_plus = itemView.findViewById(R.id.img_pluse_singlecartitem);
            img_minus = itemView.findViewById(R.id.img_minus_singlecartitem);
            txt_qty = itemView.findViewById(R.id.txt_qty_singlecartitem);
            txt_productName = itemView.findViewById(R.id.txt_productname_singlecartitem);
            txt_review = itemView.findViewById(R.id.txt_reviews_singlecartitem);
            txt_price = itemView.findViewById(R.id.txt_totalprice_singlecartitem);
            txt_priceWithDiscount = itemView.findViewById(R.id.txt_totalpricewithdiscount_singlecartitem);
            rl_remove = itemView.findViewById(R.id.rl_remove_singlecartitem);
            rb_ratingBar = itemView.findViewById(R.id.rb_productrate_singlecartitem);
            cb_select = itemView.findViewById(R.id.cb_select_singlecartitem);
        }
    }
}
