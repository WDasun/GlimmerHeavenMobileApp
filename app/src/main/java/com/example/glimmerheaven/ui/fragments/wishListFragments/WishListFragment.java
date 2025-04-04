package com.example.glimmerheaven.ui.fragments.wishListFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.View;
import android.widget.FrameLayout;

import com.example.glimmerheaven.R;

public class WishListFragment extends Fragment {

    FrameLayout subFl;

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
    }

    @Override
    public void onStop() {
        super.onStop();
        if(subFl != null){
            subFl.setBackground(null);
        }
    }
}