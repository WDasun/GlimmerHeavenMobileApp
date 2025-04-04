package com.example.glimmerheaven.ui.viewmodel;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.glimmerheaven.data.model.Address;
import com.example.glimmerheaven.data.model.Card;
import com.example.glimmerheaven.data.model.CartItem;
import com.example.glimmerheaven.data.model.Order;
import com.example.glimmerheaven.data.model.OrderItem;
import com.example.glimmerheaven.data.model.PreOrder;
import com.example.glimmerheaven.data.model.Product;
import com.example.glimmerheaven.data.repository.CustomerRepository;
import com.example.glimmerheaven.data.repository.FirebaseAuthRepository;
import com.example.glimmerheaven.data.repository.OrderRepository;
import com.example.glimmerheaven.data.repository.ProductRepository;
import com.example.glimmerheaven.utils.callBacks.MessageCallBack;
import com.example.glimmerheaven.utils.callBacks.ResultCallBack;
import com.example.glimmerheaven.utils.generators.OrderGenerator;
import com.example.glimmerheaven.utils.singleton.UserManage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartViewModel extends ViewModel {

    private PreOrder preOrder = new PreOrder();
    private MutableLiveData<Double> totalLiveData = new MutableLiveData<>();
    public CartViewModel() {
    }

    public PreOrder getPreOrder() {
        return preOrder;
    }

    public void saveCustomerAddress(Address newAddress, MessageCallBack callBack){
        String UID = new FirebaseAuthRepository().getCurrentUser().getUid();
        ArrayList<Address> currentUserAddressList = UserManage.getInstance().getCurrentUser().getValue().getAddress();
        currentUserAddressList.add(newAddress);
        new CustomerRepository().saveAddressForCustomer(UID, currentUserAddressList, callBack);
    }

    public LiveData<Double> getTotalLiveData(){
        generateTotal();
        return totalLiveData;
    }

    private void generateTotal(){
            // Separate productId list
            ArrayList<String> productIdList = new ArrayList<>();
            for(CartItem cartItem : preOrder.getSelectedCartItemList()){
                productIdList.add(cartItem.getProductId());
            }
            // Get separated products and generate total
            new ProductRepository().searchProductsOnce(productIdList, new ResultCallBack() {
                @Override
                public void onDataComplete(Object keys, Object values, boolean status, String message) {
                    if(keys != null){
                        List<OrderItem> orderItemList = new ArrayList<>();
                        ArrayList<Product> pl = (ArrayList<Product>) values;
                        double total = 0;
                        for(int i = 0; i < pl.size();i++){
                            Product product = pl.get(i);
                            double uPrice = product.getPrice();
                            double disRt = product.getDiscount() != null ? new ArrayList<>(product.getDiscount().values()).get(0).getRate() : 0;
                            int qty = preOrder.getSelectedCartItemList().get(i).getQty();

                            total += (uPrice - (uPrice*(disRt/100)))*qty;

                            // Create OrderItem
                            OrderItem orderItem = new OrderItem();
                            orderItem.setProductId(productIdList.get(i));
                            if(product.getDiscount() != null){
                                orderItem.setDiscountId(new ArrayList<>(product.getDiscount().keySet()).get(0));
                            }
                            orderItem.setUnitPrice(uPrice);
                            orderItem.setQty(qty);
                            orderItemList.add(orderItem);
                        }
                        totalLiveData.setValue(total);
                        preOrder.setTotal(total);
                        preOrder.setOrderItemList(orderItemList);
                    }
                }
            });

    }

    public ArrayList<Card> getUserCardList(){
        ArrayList<Card> cardList = UserManage.getInstance().getCurrentUser().getValue().getCard();
        return cardList;
    }

    public int getSelectedCartItemCount(){
        return preOrder.getSelectedCartItemList().size();
    }
    public void setPreOrder(PreOrder preOrder) {
        this.preOrder = preOrder;
    }
    public boolean createOrder(MessageCallBack callBack){
        boolean noError = false;
        try {
            Order order = new OrderGenerator(preOrder).generateOrderObj();
            ArrayList<String> currentOrderList = UserManage.getInstance().getCurrentUser().getValue().getOrders();
            Log.v("ats3", "currentOrderList Size : "+currentOrderList.size());
            String UID = new FirebaseAuthRepository().getCurrentUser().getUid();
            new OrderRepository().addOrder(order,currentOrderList,UID,callBack);
            noError = true;
        } catch (Exception e) {
            noError = false;
        }
        return noError;
    }

    public void productAndCartAdjustmentAfterOrder(){

        // Adjust product qtys
        Map<String, Integer> productIdsAndQtys = new HashMap<>();
        ArrayList<String> productIdList = new ArrayList<>();
        for(CartItem cartItem : preOrder.getSelectedCartItemList()){
            productIdList.add(cartItem.getProductId());
        }
        new ProductRepository().searchProductsOnce(productIdList, new ResultCallBack() {
            @Override
            public void onDataComplete(Object keys, Object values, boolean status, String message) {
                ArrayList<Product> productList = (ArrayList<Product>) values;
                for(int i=0; i<productList.size();i++){
                    int newQty = productList.get(i).getQty() - preOrder.getSelectedCartItemList().get(i).getQty();
                    productIdsAndQtys.put(preOrder.getSelectedCartItemList().get(i).getProductId(),newQty);
                }
                new ProductRepository().updateQtyInProducts(productIdsAndQtys, null);
            }
        });

        // Remove items from the Cart
        ArrayList<CartItem> currentCart = UserManage.getInstance().getCurrentUser().getValue().getCart();
        List<CartItem> toRemove = new ArrayList<>();
        for(int i=0;i<preOrder.getSelectedCartItemList().size();i++){
            for(CartItem item : currentCart){
                if(item.getProductId().equals(preOrder.getSelectedCartItemList().get(i).getProductId())){
                    toRemove.add(item);
                }
            }
        }
        currentCart.removeAll(toRemove);

        String UID = new FirebaseAuthRepository().getCurrentUser().getUid();
        new CustomerRepository().saveCart(UID,currentCart,null);
    }

}
