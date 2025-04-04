package com.example.glimmerheaven.ui.adapters.categoryAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.glimmerheaven.R;
import com.example.glimmerheaven.data.model.Category;

import java.util.ArrayList;
import java.util.Map;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.ViewHolder> {

    ArrayList<String> catId;
    ArrayList<Category> catList;
    private OnCategoryItemListner onCategoryItemListner;

    public CategoryListAdapter(Map<String, Category> categoryMap, OnCategoryItemListner onCategoryItemListner) {
        catId = new ArrayList<>(categoryMap.keySet());
        catList = new ArrayList<>(categoryMap.values());
        this.onCategoryItemListner = onCategoryItemListner;
    }

    public interface OnCategoryItemListner{
        void onCategoryItemClicked(String categoryId, Category category);
    }

    @NonNull
    @Override
    public CategoryListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryListAdapter.ViewHolder holder, int position) {
        Category category = catList.get(position);
        String categoryId = catId.get(position);
        holder.getNameView().setText(category.getCategoryName());
        Glide.with(holder.imageView.getContext())
                        .load(category.getCategoryImage())
                                .into(holder.getImageView());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onCategoryItemListner != null){
                    onCategoryItemListner.onCategoryItemClicked(categoryId, category);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return catList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nameView;
        private ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.cat_name);
            imageView = itemView.findViewById(R.id.cat_image);
        }
        public TextView getNameView() {
            return nameView;
        }

        public ImageView getImageView() {
            return imageView;
        }
    }
}
