package com.example.glimmerheaven.ui.fragments.ordersFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.glimmerheaven.ui.adapters.ordersAdapters.OrdersViewPagerAdapter;
import com.example.glimmerheaven.R;
import com.google.android.material.tabs.TabLayout;

public class OrdersFragment extends Fragment {
    private ImageView img_backArrow;
    private FragmentManager fragmentManager;
    private TabLayout tableLayout;
    private ViewPager2 viewPager2;
    private OrdersViewPagerAdapter ordersViewPagerAdapter;

    public OrdersFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_orders, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        img_backArrow = view.findViewById(R.id.back_arrow_orders);
        tableLayout = view.findViewById(R.id.tab_layout);
        viewPager2 = view.findViewById(R.id.view_pager_2);
        fragmentManager = getParentFragmentManager();
        ordersViewPagerAdapter = new OrdersViewPagerAdapter(this.getActivity());

        viewPager2.setAdapter(ordersViewPagerAdapter);

        tableLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tableLayout.getTabAt(position).select();
            }
        });

        img_backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
    }
}