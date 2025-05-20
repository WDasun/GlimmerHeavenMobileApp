package com.example.glimmerheaven.ui.CustomFragmentFactory;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;

import com.example.glimmerheaven.ui.fragments.productsFragments.ProductListFragment;

public class ProductListFactory extends FragmentFactory {
    private String categoryId, catName, catImage, brand;
    public ProductListFactory(String categoryId, String catName, String catImage, String brand) {
        super();
        this.categoryId = categoryId;
        this.catName = catName;
        this.catImage = catImage;
        this.brand = brand;
    }

    @NonNull
    @Override
    public Fragment instantiate(@NonNull ClassLoader classLoader, @NonNull String className) {
        Class<? extends Fragment> fragmentClass = loadFragmentClass(classLoader, className);
        if(fragmentClass == ProductListFragment.class){
            return new ProductListFragment(categoryId, catName, catImage,brand);
        }else{
            return super.instantiate(classLoader, className);
        }


    }
}
