package com.example.glimmerheaven.ui.fragments.ordersFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.glimmerheaven.R;
import com.example.glimmerheaven.data.model.Order;
import com.example.glimmerheaven.ui.adapters.ordersAdapters.AllOrderListAdapter;
import com.example.glimmerheaven.ui.viewmodel.OrderViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OngoingOrdersFragment extends Fragment {

    private RecyclerView rcy_main;
    private OrderViewModel orderViewModel;
    private AllOrderListAdapter allOrderListAdapter;
    private FragmentManager fragmentManager;

    public OngoingOrdersFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ongoing_orders, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        orderViewModel = new ViewModelProvider(requireActivity()).get(OrderViewModel.class);
        fragmentManager = getParentFragmentManager();

        orderViewModel.getOrdersLiveData().observe(getViewLifecycleOwner(), orderMap -> {

            Map<String, Order> onGoingOrders = new HashMap<>();

            for(String key : orderMap.keySet()){
                Order order = orderMap.get(key);
                if(!order.getOrderStatus().equals("Delivered")){
                    onGoingOrders.put(key,order);
                }
            }

            ArrayList<String> orderIds = new ArrayList<>(onGoingOrders.keySet());
            ArrayList<Order> orders = new ArrayList<>(onGoingOrders.values());
            rcy_main = view.findViewById(R.id.rcy_main_ongoingorders);
            allOrderListAdapter = new AllOrderListAdapter(orderIds, orders, selectedOrderId -> {
                if(fragmentManager.findFragmentByTag("OrderDetailsFragment") == null){
                    orderViewModel.setSelectedOrderId(selectedOrderId);
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container_activity_orders, OrderDetailsFragment.class,null,"OrderDetailsFragment")
                            .setReorderingAllowed(true)
                            .addToBackStack(null)
                            .commit();
                }
            });
            rcy_main.setAdapter(allOrderListAdapter);
            rcy_main.setLayoutManager(new LinearLayoutManager(view.getContext()));
        });


    }
}