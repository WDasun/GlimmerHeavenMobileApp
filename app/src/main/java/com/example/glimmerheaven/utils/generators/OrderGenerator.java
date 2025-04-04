package com.example.glimmerheaven.utils.generators;

import com.example.glimmerheaven.data.model.Order;
import com.example.glimmerheaven.data.model.PreOrder;
import com.example.glimmerheaven.data.repository.FirebaseAuthRepository;
import com.example.glimmerheaven.utils.singleton.UserManage;

public class OrderGenerator {
    private PreOrder preOrder;

    public OrderGenerator(PreOrder preOrder) {
        this.preOrder = preOrder;
    }

    public Order generateOrderObj() throws Exception{
        return createOrder();
    }
    private Order createOrder() throws Exception{
        Order order = new Order();

        order.setOrderDate(System.currentTimeMillis()); // current timestamp
        order.setCustomerId(new FirebaseAuthRepository().getCurrentUser().getUid());
        order.setCnt(preOrder.getCnt());
        order.setEmail(UserManage.getInstance().getCurrentUser().getValue().getEmail());
        order.setAddress(preOrder.getAddress());
        order.setPaymentType(preOrder.getPaymentType());
        order.setShippingType(preOrder.getShippingType());
        order.setAdditionalNote(preOrder.getAdditionalNote()); // use correct note
        order.setOnlinePaymentDetails(preOrder.getOnlinePaymentDetails());
        order.setOrderItemList(preOrder.getOrderItemList());
        order.setOrderStatus("Pending");
        order.setTotal(preOrder.getTotal());

        return order;
    }
}
