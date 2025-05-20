package com.example.glimmerheaven.ui.fragments.wishListFragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.glimmerheaven.R;
import com.example.glimmerheaven.data.model.Product;
import com.example.glimmerheaven.ui.adapters.wishlistAdapters.WishlistAdapter;
import com.example.glimmerheaven.ui.viewmodel.WishListViewModel;

import java.util.ArrayList;
import java.util.Map;

public class WishListFragment extends Fragment {
    private RecyclerView recyclerView;
    private WishListViewModel wishListViewModel;
    private Context context;
    private WishlistAdapter wishlistAdapter;
    private LifecycleOwner lifecycleOwner;
    private FrameLayout subFl;

    public WishListFragment() {
        super(R.layout.fragment_wish_list);
    }

    public WishListFragment(FrameLayout subFlHome) {
        super(R.layout.fragment_wish_list);
        this.subFl = subFlHome;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(subFl != null){
            subFl.setBackgroundResource(R.drawable.rounded_framel);
        }

        lifecycleOwner = getViewLifecycleOwner();
        context = getContext();
        wishListViewModel = new ViewModelProvider(this).get(WishListViewModel.class);

        wishListViewModel.getwishListProductListLiveData().observe(getViewLifecycleOwner(), new Observer<Map<String, Product>>() {
            @Override
            public void onChanged(Map<String, Product> productMap) {

                recyclerView = view.findViewById(R.id.rv_wishlist_main);
                if(productMap != null){
                    wishlistAdapter = new WishlistAdapter(context,lifecycleOwner,new ArrayList<>(productMap.keySet()), new ArrayList<>(productMap.values()));
                }else{
                    wishlistAdapter = new WishlistAdapter(context,lifecycleOwner,new ArrayList<>(), new ArrayList<>());
                }
                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context, 2);

                recyclerView.setAdapter(wishlistAdapter);
                recyclerView.setLayoutManager(layoutManager);
            }
        });

    }

    @Override
    public void onStop() {
        super.onStop();
        if(subFl != null){
            subFl.setBackground(null);
        }
    }
}