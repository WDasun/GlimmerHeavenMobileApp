package com.example.glimmerheaven.ui.CustomFragmentFactory;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;

import com.example.glimmerheaven.ui.fragments.cartFragments.CartAddAddressFragment;
import com.example.glimmerheaven.ui.fragments.cartFragments.CartPaymentFragment;

public class CartFactory extends FragmentFactory {
    TextView textLineToAddress, textLineToPayment;
    ImageView img_stepTwo, img_stepThree;

    public CartFactory(TextView textLineToAddress, TextView textLineToPayment, ImageView img_stepTwo, ImageView img_stepThree) {
        this.textLineToAddress = textLineToAddress;
        this.textLineToPayment = textLineToPayment;
        this.img_stepTwo = img_stepTwo;
        this.img_stepThree = img_stepThree;
    }

    @NonNull
    @Override
    public Fragment instantiate(@NonNull ClassLoader classLoader, @NonNull String className) {
        Class<? extends Fragment> fragmentClass = loadFragmentClass(classLoader, className);
        if(fragmentClass == CartAddAddressFragment.class){
            return new CartAddAddressFragment(textLineToAddress, img_stepTwo);
        } else if(fragmentClass == CartPaymentFragment.class){
            return new CartPaymentFragment(textLineToPayment, img_stepThree);
        }else{
            return super.instantiate(classLoader, className);
        }
    }
}
