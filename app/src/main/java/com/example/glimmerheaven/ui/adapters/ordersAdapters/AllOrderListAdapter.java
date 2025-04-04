package com.example.glimmerheaven.ui.adapters.ordersAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.glimmerheaven.R;
import com.example.glimmerheaven.data.model.Order;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AllOrderListAdapter extends RecyclerView.Adapter<AllOrderListAdapter.ViewHolder> {

    private ArrayList<String> orderIdList;
    private ArrayList<Order> orderList;
    private OnSingleOrderClickListner onSingleOrderClickListner;

    public AllOrderListAdapter(ArrayList<String> orderIdList, ArrayList<Order> orderList, OnSingleOrderClickListner onSingleOrderClickListner) {
        this.orderIdList = orderIdList;
        this.orderList = orderList;
        this.onSingleOrderClickListner = onSingleOrderClickListner;
    }

    public interface OnSingleOrderClickListner{
        void onOrderClick(String selectedOrderId);
    }


    @NonNull
    @Override
    public AllOrderListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_order_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllOrderListAdapter.ViewHolder holder, int position) {

        String orderId = orderIdList.get(position);
        Order order = orderList.get(position);

        holder.txt_orderId.setText(orderId);
        if(order.getOrderStatus().equals("Delivered")){
            holder.txt_status.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), android.R.color.holo_green_light));
        } else if (order.getOrderStatus().equals("Dispatched")) {
            holder.txt_status.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), android.R.color.holo_orange_light));
        } else if (order.getOrderStatus().equals("Pending")) {
            holder.txt_status.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), android.R.color.holo_red_light));
        } else if (order.getOrderStatus().equals("Placed")) {
            holder.txt_status.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), android.R.color.holo_blue_dark));
        } else if (order.getOrderStatus().equals("Processing")) {
            holder.txt_status.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), android.R.color.holo_purple));
        }
        holder.txt_status.setText(order.getOrderStatus());
        holder.txt_itemCount.setText("Items: "+order.getOrderItemList().size());
        holder.txt_address.setText(order.getAddress().getAddressLineOne()+", "+order.getAddress().getAddressLineTwo());
        String orderDate = new SimpleDateFormat("dd.MM.yyyy").format(new Date(order.getOrderDate()));
        holder.txt_orderDate.setText(orderDate);
        holder.txt_total.setText("Rs. "+order.getTotal());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onSingleOrderClickListner != null){
                    onSingleOrderClickListner.onOrderClick(orderId);
                }
            }
        });
    }

    @Override
    public int getItemCount() {

        return orderIdList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txt_orderId,txt_status,txt_itemCount,txt_address,txt_orderDate,txt_total;
        private CardView card_body;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_orderId = itemView.findViewById(R.id.txt_orderid_singleorder);
            txt_status = itemView.findViewById(R.id.txt_status_singleorder);
            txt_itemCount = itemView.findViewById(R.id.txt_itemcount_singleorder);
            txt_address = itemView.findViewById(R.id.txt_address_singleorder);
            txt_orderDate = itemView.findViewById(R.id.txt_orderdate_singleorder);
            txt_total = itemView.findViewById(R.id.txt_total_singleorder);
            card_body = itemView.findViewById(R.id.card_card_singleorder);
        }
    }
}
