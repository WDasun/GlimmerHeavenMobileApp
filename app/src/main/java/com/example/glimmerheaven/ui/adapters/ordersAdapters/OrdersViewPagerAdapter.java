package com.example.glimmerheaven.ui.adapters.ordersAdapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.glimmerheaven.ui.fragments.ordersFragments.AllOrdersFragment;
import com.example.glimmerheaven.ui.fragments.ordersFragments.CompletedOrdersFragment;
import com.example.glimmerheaven.ui.fragments.ordersFragments.OngoingOrdersFragment;

public class OrdersViewPagerAdapter extends FragmentStateAdapter {
    public OrdersViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
       switch (position){
           case 0 : return new AllOrdersFragment();
           case 1 : return new OngoingOrdersFragment();
           case 2 : return new CompletedOrdersFragment();
           default: return new AllOrdersFragment();
       }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
