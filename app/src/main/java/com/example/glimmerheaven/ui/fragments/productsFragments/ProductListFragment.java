package com.example.glimmerheaven.ui.fragments.productsFragments;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.glimmerheaven.data.model.Product;
import com.example.glimmerheaven.R;
import com.example.glimmerheaven.ui.adapters.productsAdapters.ProductListAdapter;
import com.example.glimmerheaven.ui.viewmodel.ProductListViewModel;
import com.example.glimmerheaven.utils.dialogs.ProductFilterDialog;

import java.util.ArrayList;
import java.util.Map;

public class ProductListFragment extends Fragment {
    private ConstraintLayout lyt_search;
    private String categoryId, catName, catImage, brand;
    private ImageView img_productlistIcon, img_back, img_filter;
    private TextView txt_categoryName;
    private RecyclerView recyclerView;
    private ProductListViewModel productListViewModel;
    public ProductListFragment(String categoryId, String catName, String catImage, String brand) {
        super(R.layout.fragment_product_list);
        this.categoryId = categoryId;
        this.catName = catName;
        this.catImage = catImage;
        this.brand = brand;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lyt_search = view.findViewById(R.id.lyt_search_productlist);
        txt_categoryName = view.findViewById(R.id.txt_category_name_inproduct);
        img_productlistIcon = view.findViewById(R.id.img_productlist_icon);
        img_back = view.findViewById(R.id.img_back_productList);
        img_filter = view.findViewById(R.id.img_filter_productList);

        // Open the filter dialog
        img_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductFilterDialog productFilterDialog = new ProductFilterDialog();
                Bundle bundle = new Bundle();
                bundle.putString("categoryId",categoryId);
                productFilterDialog.setArguments(bundle);
                productFilterDialog.show(getChildFragmentManager(), "");
            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            // Back button operation
            @Override
            public void onClick(View view) {
                requireActivity().onBackPressed();
            }
        });

        // Loading top category logo
        Glide.with(view.getContext()).load(catImage).into(img_productlistIcon);
        // Set top Category name
        txt_categoryName.setText(catName);

        Map<String,String> optionVariations = null;

        if(brand != null){
            optionVariations.put("Brand",brand);
        }else{
            optionVariations = null;
        }

        productListViewModel = new ViewModelProvider(this).get(ProductListViewModel.class);
        if(productListViewModel.getPreviouslySelectedVariations() != null){
            optionVariations = productListViewModel.getPreviouslySelectedVariations();
        }
        productListViewModel.getFilteredProductsLiveData(categoryId, optionVariations, this).observe(getViewLifecycleOwner(), productMap -> {
            recyclerView = view.findViewById(R.id.recycler_view_product_list);
                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(view.getContext(),2);
                ProductListAdapter productListAdapter = new ProductListAdapter(productMap, new ProductListAdapter.OnProductItemClickListner() {
                    @Override
                    public void onPoductItemClicked(String productItemId) {
                        FragmentManager fragmentManager = getParentFragmentManager();
                        Bundle bundle = new Bundle();
                        bundle.putString("productId", productItemId);
                        fragmentManager.beginTransaction()
                                .replace(R.id.fragment_container_products, ProductViewFragment.class, bundle)
                                .setReorderingAllowed(true)
                                .addToBackStack(null)
                                .commit();
                    }
                });
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(productListAdapter);
        });

        lyt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();
    }
}