package com.example.glimmerheaven.ui.fragments.cartFragments;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.glimmerheaven.data.model.Card;
import com.example.glimmerheaven.R;
import com.example.glimmerheaven.ui.viewmodel.CartViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class CartPaymentFragment extends Fragment {

    private RelativeLayout rl_paymentMethod,rl_addCard;
    private CardView card_payByCard, card_payOnDelivery;
    private ImageView img_selectPaymentTypeBack, img_enterCardDetailsBack;
    private TextView textLineToPayment;
    private ImageView img_stepThree;
    private TextView txt_subTotal,txt_total,txt_addCard;
    private Spinner spinner_cardList;
    private TextInputEditText txt_cardNumber,txt_holderName,txt_expYear,txt_cvv;
    private AutoCompleteTextView atxt_expMonth,atxt_cardType;
    private FragmentManager subFragmentManager;
    private CartViewModel cartViewModel;
    private String[] cardTypes = {
            "VISA",
            "MASTER"
    };
    private String[] months = {
            "01", "02", "03", "04", "05", "06",
            "07", "08", "09", "10", "11", "12"
    };
    private Fragment fragment;

    public CartPaymentFragment() {
        super(R.layout.fragment_cart_payment);
    }

    public CartPaymentFragment(TextView textLineToAddress, ImageView img_stepThree) {
        super(R.layout.fragment_cart_payment);
        this.textLineToPayment = textLineToAddress;
        this.img_stepThree = img_stepThree;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fragment = getParentFragment();
        subFragmentManager = getParentFragmentManager();
        if(fragment != null){
            cartViewModel = new ViewModelProvider(getParentFragment()).get(CartViewModel.class);
        }else{
            cartViewModel = new ViewModelProvider(getActivity()).get(CartViewModel.class);
        }


        rl_paymentMethod = view.findViewById(R.id.rl_payment_method);
        rl_addCard = view.findViewById(R.id.rl_add_card);
        card_payByCard = view.findViewById(R.id.card_paybycard_selectpaymenttype);
        card_payOnDelivery = view.findViewById(R.id.card_payondelivery_selectpaymenttype);
        img_selectPaymentTypeBack = view.findViewById(R.id.img_select_payment_type_back);
        img_enterCardDetailsBack = view.findViewById(R.id.img_enter_card_details_back);
        txt_subTotal = view.findViewById(R.id.txt_subtotal_selectpaymenttype);
        txt_total = view.findViewById(R.id.txt_totalamount_selectpayenttype);
        txt_addCard = view.findViewById(R.id.txt_add_card_cartpayment);
        txt_cardNumber = view.findViewById(R.id.txt_card_number_cartpayment);
        txt_holderName = view.findViewById(R.id.txt_holdername_cartpayment);
        txt_expYear = view.findViewById(R.id.txt_year_cartpayment);
        txt_cvv = view.findViewById(R.id.txt_cvv_cartpayment);
        atxt_expMonth = view.findViewById(R.id.atxt_month_cartpayment);
        atxt_cardType = view.findViewById(R.id.atext_cardtype_cartpayemt);
        spinner_cardList = view.findViewById(R.id.spinner_cards_cartpayments);

        if(textLineToPayment != null){
            img_stepThree.setBackgroundResource(R.drawable.full_round_cart_nav_number_pass);
            textLineToPayment.setTextColor(view.getResources().getColor(R.color.cat_status_changing));
        }

        atxt_cardType.setAdapter(new ArrayAdapter<>(view.getContext(), com.google.android.material.R.layout.support_simple_spinner_dropdown_item, cardTypes));
        atxt_expMonth.setAdapter(new ArrayAdapter<>(view.getContext(), com.google.android.material.R.layout.support_simple_spinner_dropdown_item, months));
        rl_addCard.setVisibility(View.GONE);

        int size = cartViewModel.getPreOrder().getSelectedCartItemList().size();
        Log.v("ats7","Before size : "+size);
        // Setting totals
        cartViewModel.getTotalLiveData().observe(getViewLifecycleOwner(), aDouble -> {
            Log.v("ats7","Double"+aDouble);
            txt_subTotal.setText("Rs. "+aDouble);
            txt_total.setText("Rs. "+aDouble);
        });

        // Button Pay on delivery
        card_payOnDelivery.setOnClickListener(view1 -> {
            cartViewModel.getPreOrder().setPaymentType("PayOnDelivery");
            subFragmentManager.beginTransaction()
                    .replace(cartViewModel.getFragmentContainerId(), CheckoutFragment.class, null)
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .commit();
        });
        // Button Pay by Card
        card_payByCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rl_paymentMethod.setVisibility(view.INVISIBLE);
                rl_addCard.setVisibility(View.VISIBLE);
                cartViewModel.getPreOrder().setPaymentType("CreditCard");
            }
        });

        img_enterCardDetailsBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rl_addCard.setVisibility(View.GONE);
                rl_paymentMethod.setVisibility(view.VISIBLE);
            }
        });

        // Add card button process
        txt_addCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Fill entered card details
                String cardType = atxt_cardType.getText().toString();
                String cardNumber = txt_cardNumber.getText().toString();
                String holderName = txt_holderName.getText().toString();
                String expMonth = atxt_expMonth.getText().toString();
                String expYear = txt_expYear.getText().toString();
                String cvv = txt_cvv.getText().toString();

                try{
                    if(!cardNumber.isEmpty() && !holderName.isEmpty() && !expMonth.isEmpty() &&
                        !expYear.isEmpty() && !cvv.isEmpty() && !cardType.isEmpty()){
                        Card card = new Card(cardNumber,holderName,Integer.parseInt(expYear),Integer.parseInt(expMonth),cardType);
                        cartViewModel.getPreOrder().getEnteredCard().setCard(card);
                        cartViewModel.getPreOrder().getEnteredCard().setCvv(Integer.parseInt(cvv));

                        subFragmentManager.beginTransaction()
                                .replace(cartViewModel.getFragmentContainerId(), CheckoutFragment.class, null)
                                .setReorderingAllowed(true)
                                .addToBackStack(null)
                                .commit();
                    }else{
                        Toast.makeText(getContext(), "Please complete all the fields!!", Toast.LENGTH_SHORT).show();
                    }
                } catch (NullPointerException ne) {
                    Toast.makeText(getContext(), "Invalid inputs!", Toast.LENGTH_SHORT).show();
                } catch (Exception e){
                    Toast.makeText(getContext(), "Something wrong!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        img_selectPaymentTypeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subFragmentManager.popBackStack();
            }
        });

        // to load info to the cardList spinner
        ArrayList<Card> cardList = cartViewModel.getUserCardList();
        ArrayList<String> carddisplayInfoList = new ArrayList<>();
        carddisplayInfoList.add("Select card");
        for(Card card : cardList){
            carddisplayInfoList.add(card.getCardType()+" : "+card.getCardNumber());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getContext(), com.google.android.material.R.layout.support_simple_spinner_dropdown_item, carddisplayInfoList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_cardList.setAdapter(adapter);
        // when select a item from the cardlist spinner
        spinner_cardList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i != 0){
                    Card selectedCard = cardList.get(i-1);
                    atxt_cardType.setText(selectedCard.getCardType(),false);
                    txt_cardNumber.setText(selectedCard.getCardNumber());
                    txt_holderName.setText(selectedCard.getCardHolderName());
                    atxt_expMonth.setText(String.valueOf(selectedCard.getExpireMonth()),false);
                    txt_expYear.setText(String.valueOf(selectedCard.getExpireYear()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(textLineToPayment != null){
            img_stepThree.setBackgroundResource(R.drawable.full_round_cart_nav_number);
            textLineToPayment.setTextColor(getResources().getColor(R.color.cat_status_default));
        }
    }
}
