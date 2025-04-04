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

import com.example.glimmerheaven.data.model.Order;
import com.example.glimmerheaven.ui.adapters.ordersAdapters.AllOrderListAdapter;
import com.example.glimmerheaven.R;
import com.example.glimmerheaven.ui.viewmodel.OrderViewModel;

import java.util.ArrayList;

public class AllOrdersFragment extends Fragment {

    private RecyclerView recyclerView;
    private AllOrderListAdapter allOrderListAdapter;
    private FragmentManager fragmentManager;

    private OrderViewModel orderViewModel;

    public AllOrdersFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_all_orders, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        orderViewModel = new ViewModelProvider(requireActivity()).get(OrderViewModel.class);
        fragmentManager = getParentFragmentManager();

        orderViewModel.getOrdersLiveData().observe(getViewLifecycleOwner(), orderMap -> {
            ArrayList<String> orderIds = new ArrayList<>(orderMap.keySet());
            ArrayList<Order> orders = new ArrayList<>(orderMap.values());
            recyclerView = view.findViewById(R.id.recycler_view_all_orders);
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
            recyclerView.setAdapter(allOrderListAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        });

    }
}