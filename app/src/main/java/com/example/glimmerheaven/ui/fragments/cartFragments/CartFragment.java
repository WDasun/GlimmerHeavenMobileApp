package com.example.glimmerheaven.ui.fragments.cartFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.glimmerheaven.ui.CustomFragmentFactory.CartFactory;
import com.example.glimmerheaven.R;
import com.example.glimmerheaven.ui.viewmodel.CartViewModel;

public class CartFragment extends Fragment {

    private FrameLayout subFl;
    private TextView textLineToAddress, textLineToPayment;
    private ImageView img_stepTwo, img_stepThree;
    private FragmentManager fragmentManager;
    private CartViewModel cardViewModel;


    public CartFragment(FrameLayout subFlHome) {
        super(R.layout.fragment_cart);
        this.subFl = subFlHome;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cardViewModel = new ViewModelProvider(this).get(CartViewModel.class);

        textLineToAddress = view.findViewById(R.id.txt_line_to_address);
        textLineToPayment = view.findViewById(R.id.txt_line_to_payment);
        img_stepTwo = view.findViewById(R.id.img_cart_step_2);
        img_stepThree = view.findViewById(R.id.img_cart_step_3);

        if(subFl != null){
            subFl.setBackgroundResource(R.drawable.rounded_framel);
        }

        fragmentManager = getChildFragmentManager();
        fragmentManager.setFragmentFactory(new CartFactory(
                textLineToAddress,
                textLineToPayment,
                img_stepTwo,
                img_stepThree
        ));

        Fragment cartItemListFragment = fragmentManager.findFragmentByTag("CartItemList");

        if(cartItemListFragment == null){
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_cart, CartItemListFragment.class, null,"CartItemList")
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .commit();
        }

    }

    @Override
    public void onStop() {
        super.onStop();

        Log.v("ats1", "onPause");

        if(subFl != null){
            subFl.setBackground(null);
        }
    }

}