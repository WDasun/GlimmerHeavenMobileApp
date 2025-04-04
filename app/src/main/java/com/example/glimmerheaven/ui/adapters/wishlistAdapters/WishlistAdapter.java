package com.example.glimmerheaven.ui.adapters.wishlistAdapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.glimmerheaven.R;
import com.example.glimmerheaven.data.model.Product;
import com.example.glimmerheaven.data.repository.CustomerRepository;
import com.example.glimmerheaven.data.repository.FirebaseAuthRepository;
import com.example.glimmerheaven.utils.singleton.UserManage;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.ViewHolder> {

    private ArrayList<String> pidList;
    private ArrayList<Product> productList;

    public WishlistAdapter(ArrayList<String> pidList, ArrayList<Product> productList) {
        this.pidList = pidList;
        this.productList = productList;
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

        holder.txt_productName.setText(product.getName());
        holder.txt_price.setText(product.getPrice()+" LKR");
        Glide.with(holder.itemView.getContext())
                .load(product.getImage())
                .into(holder.img_productImage);

        holder.img_removeFromWishlist.setOnClickListener(view -> {
            ArrayList<String> wishList = UserManage.getInstance().getCurrentUser().getValue().getWishList();
            if(wishList.contains(pid)){
                wishList.remove(pid);
                String customerUID = new FirebaseAuthRepository().getCurrentUser().getUid();
                new CustomerRepository().updateWishList(customerUID, wishList);
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
