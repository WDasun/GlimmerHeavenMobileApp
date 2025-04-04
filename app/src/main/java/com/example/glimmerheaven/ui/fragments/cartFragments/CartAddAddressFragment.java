package com.example.glimmerheaven.ui.fragments.cartFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.glimmerheaven.R;
import com.example.glimmerheaven.data.model.Address;
import com.example.glimmerheaven.data.repository.CustomerRepository;
import com.example.glimmerheaven.ui.viewmodel.CartViewModel;
import com.example.glimmerheaven.utils.callBacks.MessageCallBack;
import com.example.glimmerheaven.utils.singleton.UserManage;

import java.util.ArrayList;

public class CartAddAddressFragment extends Fragment {
    private FragmentManager fragmentManager;
    private TextView textLineToAddress,btn_addNewAddress;
    private EditText txt_addressLineOne,txt_addressLineTwo,txt_province,txt_district,txt_villageOrTown;
    private ImageView img_stepTwo,img_selectAddressBack;
    private LinearLayout lyr_addressList;
    private CartViewModel cartViewModel;
    public CartAddAddressFragment(TextView textLineToAddress, ImageView img_stepTwo) {
        super(R.layout.fragment_cart_add_address);
        this.textLineToAddress = textLineToAddress;
        this.img_stepTwo = img_stepTwo;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fragmentManager = getParentFragmentManager();
        cartViewModel = new ViewModelProvider(getParentFragment()).get(CartViewModel.class);

        btn_addNewAddress = view.findViewById(R.id.txt_add_new_address_cartaddress);
        img_selectAddressBack = view.findViewById(R.id.img_select_address_back);
        lyr_addressList = view.findViewById(R.id.lyr_address_list_cartaddaddress);
        txt_addressLineOne = view.findViewById(R.id.txt_address_line_one_cartaddaddress);
        txt_addressLineTwo = view.findViewById(R.id.txt_address_line_two_cartaddaddress);
        txt_province = view.findViewById(R.id.txt_province_cartaddaddress);
        txt_district = view.findViewById(R.id.txt_district_cartaddaddress);
        txt_villageOrTown = view.findViewById(R.id.txt_address_villageortown_cartaddaddress);

        // Changing cart process navigation colors
        img_stepTwo.setBackgroundResource(R.drawable.full_round_cart_nav_number_pass);
        textLineToAddress.setTextColor(view.getResources().getColor(R.color.cat_status_changing));

        // Load addresses and set listeners
        UserManage.getInstance().getCurrentUser().observe(getViewLifecycleOwner(), customer -> {
            ArrayList<Address> addressList = customer.getAddress();
            for (Address address : addressList){
                View singleAddressView = LayoutInflater.from(getContext()).inflate(R.layout.single_cart_address_layout, lyr_addressList, false);
                TextView txt_name = singleAddressView.findViewById(R.id.txt_address_name_singlecartaddress);
                txt_name.setText(address.getAddressLineOne()+" "+address.getAddressLineTwo());

                singleAddressView.setOnClickListener(view1 -> {
                    cartViewModel.getPreOrder().setAddress(address);
                    moveToCartPayment();
                });

                lyr_addressList.addView(singleAddressView);
            }
        });

        // New Address button function
        btn_addNewAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String addressLineOne = txt_addressLineOne.getText().toString();
                String addressLineTwo = txt_addressLineTwo.getText().toString();
                String province = txt_province.getText().toString();
                String district = txt_district.getText().toString();
                String villageOrTown = txt_villageOrTown.getText().toString();
                try {
                    if(!addressLineOne.isEmpty() && !addressLineTwo.isEmpty() &&
                        !province.isEmpty() && !district.isEmpty() && !villageOrTown.isEmpty()){
                        Address address = new Address(addressLineOne,addressLineTwo,villageOrTown,district,province);
                        cartViewModel.saveCustomerAddress(address, new MessageCallBack() {
                            @Override
                            public void onComplete(boolean status, String message) {
                                if(status){
                                    Toast.makeText(getContext(), "Address saved!", Toast.LENGTH_SHORT).show();
                                    cartViewModel.getPreOrder().setAddress(address);
                                    moveToCartPayment();
                                }else{
                                    Toast.makeText(getContext(), "Address save failed!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else{
                        Toast.makeText(getContext(), "Please fill in all required fields!", Toast.LENGTH_SHORT).show();
                    }
                } catch (NullPointerException ne) {
                    Toast.makeText(getContext(), "Invalid Inputs !", Toast.LENGTH_SHORT).show();
                }
            }
        });

        img_selectAddressBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.popBackStack();
            }
        });
    }

    private void moveToCartPayment(){
        Fragment cartPaymentFragment = fragmentManager.findFragmentByTag("CartPaymentFragment");
        if(cartPaymentFragment == null){
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_cart, CartPaymentFragment.class, null)
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        img_stepTwo.setBackgroundResource(R.drawable.full_round_cart_nav_number);
        textLineToAddress.setTextColor(getResources().getColor(R.color.cat_status_default));
    }
}