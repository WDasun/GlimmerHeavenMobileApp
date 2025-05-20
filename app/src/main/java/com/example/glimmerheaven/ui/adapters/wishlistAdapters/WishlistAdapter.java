package com.example.glimmerheaven.ui.adapters.wishlistAdapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.glimmerheaven.R;
import com.example.glimmerheaven.data.model.CartItem;
import com.example.glimmerheaven.data.model.Product;
import com.example.glimmerheaven.data.repository.CustomerRepository;
import com.example.glimmerheaven.data.repository.FirebaseAuthRepository;
import com.example.glimmerheaven.utils.callBacks.MessageCallBack;
import com.example.glimmerheaven.utils.singleton.UserManage;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.ViewHolder> {

    private final int addToCatImg = R.drawable.add_to_cart_green;
    private final int removeFromCatImg = R.drawable.remove_from_cart_red;
    private ArrayList<String> pidList;
    private ArrayList<Product> productList;
    private ArrayList<CartItem> cart;
    private Context context;
    private LifecycleOwner lifecycleOwner;

    public WishlistAdapter(Context context, LifecycleOwner lifecycleOwner, ArrayList<String> pidList, ArrayList<Product> productList) {
        this.pidList = pidList;
        this.productList = productList;
        this.context = context;
        this.lifecycleOwner = lifecycleOwner;

        loadCart();
    }

    private void loadCart(){
        UserManage.getInstance().getCurrentUser().observe(lifecycleOwner,customer -> {
            cart = customer.getCart();
        });
    }

    @NonNull
    @Override
    public WishlistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.wish_list_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WishlistAdapter.ViewHolder holder, int position) {
        Product product = productList.get(position);
        String pid = pidList.get(position);
        AtomicBoolean includedInTheCart = new AtomicBoolean(false);
        AtomicInteger cartPosition = new AtomicInteger();

        holder.txt_productName.setText(product.getName());
        holder.txt_price.setText(product.getPrice()+" LKR");
        Glide.with(holder.itemView.getContext())
                .load(product.getImage())
                .into(holder.img_productImage);

        // Setting cart image on product
        holder.img_addToCart.setImageResource(addToCatImg);
        for(int i = 0; i < cart.size();i++){
            if(pid.equals(cart.get(i).getProductId())){
                holder.img_addToCart.setImageResource(removeFromCatImg);
                includedInTheCart.set(true);
                cartPosition.set(i);
                break;
            }
        }

        holder.img_removeFromWishlist.setOnClickListener(view -> {
            ArrayList<String> wishList = UserManage.getInstance().getCurrentUser().getValue().getWishList();
            if(wishList.contains(pid)){
                wishList.remove(pid);
                String customerUID = new FirebaseAuthRepository().getCurrentUser().getUid();
                new CustomerRepository().updateWishList(customerUID, wishList,(status, message) -> {
                    if(status){
                        Toast.makeText(context,"Item removed from the wishlist!",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context,"Failed!",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        holder.img_addToCart.setOnClickListener(view -> {
            if(includedInTheCart.get()){
                CartItem item = cart.get(cartPosition.get());
                cart.remove(item);
                new CustomerRepository().saveCart(
                        new FirebaseAuthRepository().getCurrentUser().getUid(),
                        cart,
                        new MessageCallBack() {
                            @Override
                            public void onComplete(boolean status, String message) {
                                if(status){
                                    includedInTheCart.set(false);
                                    holder.img_addToCart.setImageResource(addToCatImg);
                                    Toast.makeText(context,"Item removed from the cart",Toast.LENGTH_SHORT).show();
                                }else{
                                    holder.img_addToCart.setImageResource(removeFromCatImg);
                                    Toast.makeText(context,"Failed!",Toast.LENGTH_SHORT).show();
                                    cart.add(cartPosition.get(),item);
                                }
                            }
                        }
                );
            }else{
                CartItem item = new CartItem(
                        pid,
                        1
                );
                cart.add(item);
                new CustomerRepository().saveCart(
                        new FirebaseAuthRepository().getCurrentUser().getUid(),
                        cart,
                        new MessageCallBack() {
                            @Override
                            public void onComplete(boolean status, String message) {
                                if(status){
                                    includedInTheCart.set(true);
                                    holder.img_addToCart.setImageResource(removeFromCatImg);
                                    Toast.makeText(context,"Item added to the cart",Toast.LENGTH_SHORT).show();
                                }else{
                                    holder.img_addToCart.setImageResource(addToCatImg);
                                    Toast.makeText(context,"Failed!",Toast.LENGTH_SHORT).show();
                                    cart.remove(item);
                                }
                            }
                        }
                );
            }
        });
    }

    @Override
    public int getItemCount() {
        return pidList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_productImage, img_addToCart, img_removeFromWishlist;
        private TextView txt_productName, txt_price;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_productImage = itemView.findViewById(R.id.img_product_wishlist_item);
            img_addToCart = itemView.findViewById(R.id.img_add_to_cart_wishlist_item);
            img_removeFromWishlist = itemView.findViewById(R.id.img_remove_from_wishlist_wishlist_item);
            txt_productName = itemView.findViewById(R.id.txt_product_name_wishlist_item);
            txt_price = itemView.findViewById(R.id.txt_price_wishlist_item);
        }
    }
}
