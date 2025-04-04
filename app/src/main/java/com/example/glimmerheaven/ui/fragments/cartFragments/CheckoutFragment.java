package com.example.glimmerheaven.ui.fragments.cartFragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.glimmerheaven.MainActivity;
import com.example.glimmerheaven.R;
import com.example.glimmerheaven.ui.viewmodel.CartViewModel;
import com.example.glimmerheaven.utils.callBacks.MessageCallBack;

public class CheckoutFragment extends Fragment {

    private ImageView img_checkoutBack;
    private FragmentManager fragmentManager;
    private CartViewModel cvm;
    private TextView txt_address,txt_paymentMethod,txt_card,txt_additionalNote, txt_priceAndCountLabel;
    private TextView txt_price,txt_discount,txt_deliveryCharge,txt_total,txt_submitOrder, txt_cnt;
    private ConstraintLayout cl_main,cl_complete;
    private Button btn_backToCart;

    public CheckoutFragment() {
        super(R.layout.fragment_cart_checkout);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        img_checkoutBack = view.findViewById(R.id.img_checkout_back);
        fragmentManager = getParentFragmentManager();
        cvm = new ViewModelProvider(getParentFragment()).get(CartViewModel.class);

        txt_address = view.findViewById(R.id.txt_deliveryaddress_checkout);
        txt_paymentMethod = view.findViewById(R.id.txt_paymentmethod_checkout);
        txt_card = view.findViewById(R.id.txt_card_checkout);
        txt_additionalNote = view.findViewById(R.id.txt_additionalnotes_checkout);
        txt_priceAndCountLabel = view.findViewById(R.id.txt_priceandcount_checkout);
        txt_price = view.findViewById(R.id.txt_price_checkout);
        txt_discount = view.findViewById(R.id.txt_discount_checkout);
        txt_deliveryCharge = view.findViewById(R.id.txt_deliveycharge_checkout);
        txt_total = view.findViewById(R.id.txt_total_checkout);
        txt_submitOrder = view.findViewById(R.id.txt_submitorder_checkout);
        txt_cnt = view.findViewById(R.id.txt_cnt_checkout);
        cl_main = view.findViewById(R.id.cl_main_checkout);
        cl_complete = view.findViewById(R.id.cl_completed_checkout);
        btn_backToCart = view.findViewById(R.id.btn_gobacktocart_checkout);

        cl_main.setVisibility(View.VISIBLE);
        cl_complete.setVisibility(View.INVISIBLE);

        img_checkoutBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.popBackStack();
            }
        });

        txt_address.setText(cvm.getPreOrder().getAddress().getAddressLineOne()+" "+cvm.getPreOrder().getAddress().getAddressLineTwo());
        if(cvm.getPreOrder().getEnteredCard().getCard() != null){
            txt_paymentMethod.setText("Payment method : "+cvm.getPreOrder().getPaymentType());
            txt_card.setText(cvm.getPreOrder().getEnteredCard().getCard().getCardType()+" "+cvm.getPreOrder().getEnteredCard().getCard().getCardNumber());
        }else{
            txt_paymentMethod.setText("Payment method : "+"Pay on Delivery");
            txt_card.setText("---");
        }
        txt_priceAndCountLabel.setText("Price("+cvm.getSelectedCartItemCount()+" Items)");
        cvm.getTotalLiveData().observe(getViewLifecycleOwner(),aDouble -> {
            txt_price.setText("LKR "+aDouble);
            txt_total.setText("LKR "+aDouble);
        });
        txt_discount.setText("None");
        txt_deliveryCharge.setText("Paid on delivery");

        txt_submitOrder.setOnClickListener(view1 -> {
            String additionalNote = txt_additionalNote.getText().toString();
            String cnt = txt_cnt.getText().toString();

            if(cnt != null && !cnt.isEmpty() && !cnt.isBlank()){
                cvm.getPreOrder().setAdditionalNote(additionalNote);
                cvm.getPreOrder().setCnt(cnt);
                boolean noError = cvm.createOrder(new MessageCallBack() {
                    @Override
                    public void onComplete(boolean status, String message) {
                        if(status){
                            cvm.productAndCartAdjustmentAfterOrder();
                            cl_main.setVisibility(View.INVISIBLE);
                            cl_complete.setVisibility(View.VISIBLE);
                            Toast.makeText(getContext(), "Order completed!", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getContext(), "Order failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                if(noError){

                }else{
                    Toast.makeText(getContext(), "Got an Error!, Please contact customer service.", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(getContext(), "Contact number is required.", Toast.LENGTH_SHORT).show();
            }
        });

        btn_backToCart.setOnClickListener(view1 -> {
            Intent intent = new Intent(getContext(), MainActivity.class);
            intent.putExtra("fromCart", true);
            startActivity(intent);
            requireActivity().finish();
        });
    }
}
