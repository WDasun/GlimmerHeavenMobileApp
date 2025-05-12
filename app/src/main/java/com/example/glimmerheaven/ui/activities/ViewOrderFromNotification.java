package com.example.glimmerheaven.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.glimmerheaven.MainActivity;
import com.example.glimmerheaven.R;
import com.example.glimmerheaven.data.model.Order;
import com.example.glimmerheaven.data.model.OrderItem;
import com.example.glimmerheaven.data.model.Product;
import com.example.glimmerheaven.ui.adapters.ordersAdapters.OrderDetailItemsAdapter;
import com.example.glimmerheaven.ui.viewmodel.OrderViewModel;
import com.example.glimmerheaven.ui.viewmodel.ViewOrderFromNotificationModel;
import com.example.glimmerheaven.utils.SharedPreferences.SPManageUID;
import com.example.glimmerheaven.utils.singleton.UserManage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ViewOrderFromNotification extends AppCompatActivity {

    private ConstraintLayout cl_notForYou;
    private LinearLayout lyl_main;
    private ImageView backArrow;
    private ViewOrderFromNotificationModel viewOrderFromNotificationModel;
    private TextView txt_status,txt_orderDate,txt_orderId,txt_paymentMethod,txt_subTotal;
    private TextView txt_deliveryFee,txt_total,txt_deliveredDate,txt_address,txt_cnt;
    private RecyclerView rcy_orderItemList;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_order_from_notification);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        context = this;
        viewOrderFromNotificationModel = new ViewModelProvider(this).get(ViewOrderFromNotificationModel.class);

        lyl_main = findViewById(R.id.lyl_order_details);
        cl_notForYou = findViewById(R.id.cl_order_details_notforyou);
        backArrow = findViewById(R.id.back_arrow_order_details_notify_notification);
        txt_status = findViewById(R.id.txt_status_orderdetails_notification);
        txt_orderDate = findViewById(R.id.txt_orderdate_orderdetails);
        txt_orderId = findViewById(R.id.txt_orderid_orderdetails);
        txt_paymentMethod = findViewById(R.id.txt_paymentmethod_orderdetails);
        txt_subTotal = findViewById(R.id.txt_subtotal_orderdetails);
        txt_deliveryFee = findViewById(R.id.txt_deliveryfee_orderdetails);
        txt_total = findViewById(R.id.txt_total_orderdetails);
        txt_address = findViewById(R.id.txt_address_orderdetails);
        txt_cnt = findViewById(R.id.txt_cnt_orderdetails);
        txt_deliveredDate = findViewById(R.id.txt_delivereddate_orderdetails);

        cl_notForYou.setVisibility(View.GONE);

        String orderId = getIntent().getStringExtra("orderId");
        String relatedUid = getIntent().getStringExtra("uid");

        String currentUid = new SPManageUID(this).getUid();

        Log.v("ats6", "currentUid : "+currentUid);
        Log.v("ats6", "relatedUid : "+relatedUid);

        // Checking if there are not any user login or this is the correct user
        if(currentUid != null && relatedUid.equals(currentUid)){
            try {
                viewOrderFromNotificationModel.getSelectedOrder(orderId).observe(this,order -> {
                    if(order.getOrderStatus().equals("Delivered")){
                        txt_status.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_green_light));
                    } else if (order.getOrderStatus().equals("Dispatched")) {
                        txt_status.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_orange_light));
                    } else if (order.getOrderStatus().equals("Pending")) {
                        txt_status.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_red_light));
                    } else if (order.getOrderStatus().equals("Placed")) {
                        txt_status.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_blue_dark));
                    } else if (order.getOrderStatus().equals("Processing")) {
                        txt_status.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_purple));
                    }

                    txt_status.setText(order.getOrderStatus());
                    txt_orderDate.setText("Ordered on:"+new SimpleDateFormat("dd.MM.yyyy").format(new Date(order.getOrderDate())));
                    txt_orderId.setText(orderId);
                    txt_paymentMethod.setText("Payment Method: "+order.getPaymentType());
                    txt_subTotal.setText("Rs. "+order.getTotal());
                    txt_deliveryFee.setText("None");
                    txt_total.setText("Rs. "+order.getTotal());
                    String address = order.getAddress().getAddressLineOne()+",\n "+order.getAddress().getAddressLineTwo();
                    txt_address.setText(address);
                    txt_cnt.setText(order.getCnt());

                    if(order.getDeliveredDate() != 0L){
                        txt_deliveredDate.setText("Delivered on :"+new SimpleDateFormat("dd.MM.yyyy").format(new Date(order.getDeliveredDate())));
                    }else{
                        txt_deliveredDate.setText("Delivered on : 0000.00.00");
                    }

                    viewOrderFromNotificationModel.getRelatedProducts((keys, values, status, message) -> {
                        ArrayList<Integer> qtyList = new ArrayList<>();
                        for(OrderItem i : order.getOrderItemList()){
                            qtyList.add(i.getQty());
                        }
                        ArrayList<Product> products = (ArrayList<Product>) values;

                        rcy_orderItemList = findViewById(R.id.rcy_orderitems_orderdetails);
                        rcy_orderItemList.setAdapter(new OrderDetailItemsAdapter(products,qtyList));
                        rcy_orderItemList.setLayoutManager(new LinearLayoutManager(this));
                    });

                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            lyl_main.setVisibility(View.GONE);
            cl_notForYou.setVisibility(View.VISIBLE);
        }

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(viewOrderFromNotificationModel != null){
            viewOrderFromNotificationModel.getOrderVEHolder().removeListener();
        }
    }
}