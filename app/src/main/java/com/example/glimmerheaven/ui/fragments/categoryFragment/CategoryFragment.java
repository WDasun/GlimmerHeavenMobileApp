package com.example.glimmerheaven.ui.fragments.categoryFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.glimmerheaven.data.model.Category;
import com.example.glimmerheaven.ui.activities.ProductActivity;
import com.example.glimmerheaven.R;
import com.example.glimmerheaven.ui.adapters.categoryAdapters.CategoryListAdapter;
import com.example.glimmerheaven.ui.viewmodel.CategoryViewModel;

import java.util.Map;

public class CategoryFragment extends Fragment {

    private CategoryViewModel categoryViewModel;
    private Context context;
    private FrameLayout subFl;
    private RecyclerView categoryListRecyclerView;
    private CategoryListAdapter categoryListAdapter;

    public CategoryFragment() {
        super(R.layout.fragment_category_fragment);
    }

    public CategoryFragment(FrameLayout subFl) {
        super(R.layout.fragment_category_fragment);
        this.subFl = subFl;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        context = this.getContext();

        categoryListRecyclerView = view.findViewById(R.id.Recycler_view_category_list);

        categoryViewModel.getCategoryList().observe(getViewLifecycleOwner(), new Observer<Map<String, Category>>() {
            @Override
            public void onChanged(Map<String, Category> categoryMap) {
                categoryListAdapter = new CategoryListAdapter(categoryMap, new CategoryListAdapter.OnCategoryItemListner() {
                    @Override
                    public void onCategoryItemClicked(String categoryId, Category category) {
                        Intent intent = new Intent(view.getContext(), ProductActivity.class);

                        intent.putExtra("categoryId", categoryId);
                        intent.putExtra("categoryName", category.getCategoryName());
                        intent.putExtra("catgoryImage", category.getCategoryImage());
                        startActivity(intent);
                    }
                });
                categoryListRecyclerView.setLayoutManager(new GridLayoutManager(context, 2));
                categoryListRecyclerView.setAdapter(categoryListAdapter);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        if(subFl != null){
            subFl.setBackgroundResource(R.drawable.rounded_framel);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.v("ats", "CategoryFragment : onStop");
        if(subFl != null){
            subFl.setBackground(null);
        }
    }

}