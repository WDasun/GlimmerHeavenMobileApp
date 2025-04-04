package com.example.glimmerheaven.ui.CustomFragmentFactory;

import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;

import com.example.glimmerheaven.ui.fragments.cartFragments.CartFragment;
import com.example.glimmerheaven.ui.fragments.categoryFragment.CategoryFragment;
import com.example.glimmerheaven.ui.fragments.homeFragments.HomeFragment;
import com.example.glimmerheaven.ui.fragments.profileFragments.ProfileFragment;
import com.example.glimmerheaven.ui.fragments.wishListFragments.WishListFragment;

import java.util.List;

public class MainNavFactory extends FragmentFactory {
    private List<FrameLayout> subFlList;

     public MainNavFactory(List<FrameLayout> subFlList) {
          super();
          this.subFlList = subFlList;
     }

     @NonNull
     @Override
     public Fragment instantiate(@NonNull ClassLoader classLoader, @NonNull String className) {
          Class<? extends Fragment> fragmentClass = loadFragmentClass(classLoader, className);
          if (fragmentClass == HomeFragment.class) {
              return new HomeFragment(subFlList.get(0));
          }else if(fragmentClass == WishListFragment.class){
              return new WishListFragment(subFlList.get(1));
          }else if(fragmentClass == CartFragment.class){
              return new CartFragment(subFlList.get(2));
          }else if(fragmentClass == CategoryFragment.class){
              return new CategoryFragment(subFlList.get(3));
          }else if(fragmentClass == ProfileFragment.class){
              return new ProfileFragment(subFlList.get(4));
          }else {
               return super.instantiate(classLoader, className);
          }
     }
}
