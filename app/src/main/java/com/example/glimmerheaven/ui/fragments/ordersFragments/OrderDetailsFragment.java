package com.example.glimmerheaven.ui.fragments.ordersFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.glimmerheaven.R;
import com.example.glimmerheaven.data.model.Order;
import com.example.glimmerheaven.data.model.OrderItem;
import com.example.glimmerheaven.data.model.Product;
import com.example.glimmerheaven.ui.adapters.ordersAdapters.OrderDetailItemsAdapter;
import com.example.glimmerheaven.ui.viewmodel.OrderViewModel;
import com.example.glimmerheaven.utils.singleton.UserManage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class OrderDetailsFragment extends Fragment {

    private FragmentManager fragmentManager;
    private ImageView backArrow;
    private OrderViewModel orderViewModel;
    private TextView txt_status,txt_orderDate,txt_orderId,txt_paymentMethod,txt_subTotal;
    private TextView txt_deliveryFee,txt_total,txt_customerName,txt_address,txt_cnt;
    private RecyclerView rcy_orderItemList;

    public OrderDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fragmentManager = getParentFragmentManager();
        orderViewModel = new ViewModelProvider(requireActivity()).get(OrderViewModel.class);

        backArrow = view.findViewById(R.id.back_arrow_order_details);
        txt_status = view.findViewById(R.id.txt_status_orderdetails);
        txt_orderDate = view.findViewById(R.id.txt_orderdate_orderdetails);
        txt_orderId = view.findViewById(R.id.txt_orderid_orderdetails);
        txt_paymentMethod = view.findViewById(R.id.txt_paymentmethod_orderdetails);
        txt_subTotal = view.findViewById(R.id.txt_subtotal_orderdetails);
        txt_deliveryFee = view.findViewById(R.id.txt_deliveryfee_orderdetails);
        txt_total = view.findViewById(R.id.txt_total_orderdetails);
        txt_customerName = view.findViewById(R.id.txt_customername_orderdetails);
        txt_address = view.findViewById(R.id.txt_address_orderdetails);
        txt_cnt = view.findViewById(R.id.txt_cnt_orderdetails);

        String orderId = orderViewModel.getSelectedOrderId();
        Order order = orderViewModel.getSelectedOrderAndId();

        if(order.getOrderStatus().equals("Delivered")){
            txt_status.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.holo_green_light));
        } else if (order.getOrderStatus().equals("Dispatched")) {
            txt_status.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.holo_orange_light));
        } else if (order.getOrderStatus().equals("Pending")) {
            txt_status.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.holo_red_light));
        } else if (order.getOrderStatus().equals("Placed")) {
            txt_status.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.holo_blue_dark));
        } else if (order.getOrderStatus().equals("Processing")) {
            txt_status.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.holo_purple));
        }

        txt_status.setText(order.getOrderStatus());
        txt_orderDate.setText("Ordered on:"+new SimpleDateFormat("dd.MM.yyyy").format(new Date(order.getOrderDate())));
        txt_orderId.setText(orderId);
        txt_paymentMethod.setText("Payment Method: "+order.getPaymentType());
        txt_subTotal.setText("Rs. "+order.getTotal());
        txt_deliveryFee.setText("None");
        txt_total.setText("Rs. "+order.getTotal());
        String customerName = UserManage.getInstance().getCurrentUser().getValue().getFname()+" "+UserManage.getInstance().getCurrentUser().getValue().getLname();
        txt_customerName.setText(customerName);
        String address = order.getAddress().getAddressLineOne()+",\n "+order.getAddress().getAddressLineTwo();
        txt_address.setText(address);
        txt_cnt.setText(order.getCnt());

        orderViewModel.getRelatedOrders((keys, values, status, message) -> {
            ArrayList<Integer> qtyList = new ArrayList<>();
            for(OrderItem i : order.getOrderItemList()){
                qtyList.add(i.getQty());
            }
            ArrayList<Product> products = (ArrayList<Product>) values;

            rcy_orderItemList = view.findViewById(R.id.rcy_orderitems_orderdetails);
            rcy_orderItemList.setAdapter(new OrderDetailItemsAdapter(products,qtyList));
            rcy_orderItemList.setLayoutManager(new LinearLayoutManager(getContext()));
        });

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.popBackStack();
            }
        });

    }
}