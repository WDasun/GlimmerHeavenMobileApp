package com.example.glimmerheaven.ui.adapters.ordersAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.glimmerheaven.R;
import com.example.glimmerheaven.data.model.Product;

import java.util.ArrayList;

public class OrderDetailItemsAdapter extends RecyclerView.Adapter<OrderDetailItemsAdapter.ViewHolder> {

    private ArrayList<Product> products;
    private ArrayList<Integer> qty;

    public OrderDetailItemsAdapter(ArrayList<Product> products, ArrayList<Integer> qty) {
        this.products = products;
        this.qty = qty;
    }

    @NonNull
    @Override
    public OrderDetailItemsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_order_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailItemsAdapter.ViewHolder holder, int position) {
        if(products != null && qty != null){
            holder.txt_name.setText(products.get(position).getName());
            holder.txt_qty.setText("x"+qty.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txt_name,txt_qty;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_name = itemView.findViewById(R.id.txt_name_singleorderitem);
            txt_qty = itemView.findViewById(R.id.txt_qty_singleorderitem);
        }
    }
}
