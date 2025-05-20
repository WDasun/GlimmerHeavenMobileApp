package com.example.glimmerheaven.ui.adapters.ReviewsAdapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.glimmerheaven.R;
import com.example.glimmerheaven.data.model.CommentAndRating;
import com.example.glimmerheaven.data.model.Product;
import com.example.glimmerheaven.data.repository.ProductRepository;
import com.example.glimmerheaven.ui.activities.ProductActivity;
import com.example.glimmerheaven.ui.adapters.productsAdapters.ProductListAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class MyReviewsAdapter extends RecyclerView.Adapter<MyReviewsAdapter.ViewHolder> {
    private Context context;
    private ArrayList<String> pidList;
    private ArrayList<CommentAndRating> ratingsList;
    private ArrayList<Product> relatedProductList;

    public MyReviewsAdapter(Context context, ArrayList<String> pidList, ArrayList<CommentAndRating> ratingsList, ArrayList<Product> relatedProductList) {
        this.context = context;
        this.pidList = pidList;
        this.ratingsList = ratingsList;
        this.relatedProductList = relatedProductList;
    }

    @NonNull
    @Override
    public MyReviewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_myreview_details_layout, parent, false);
        return new MyReviewsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyReviewsAdapter.ViewHolder holder, int position) {
        String pid = pidList.get(position);
        Product product = relatedProductList.get(position);
        CommentAndRating cat = ratingsList.get(position);
        holder.txt_date.setText(new SimpleDateFormat("yyyy/MM/dd").format(new Date(cat.getChangedDate())));
        holder.txt_productName.setText(product.getName());
        holder.rb_rate.setRating(cat.getRate());
        holder.txt_comment.setText(cat.getComment());

        holder.ctl_main.setOnClickListener(view -> {
            Intent intent = new Intent(context, ProductActivity.class);
            intent.putExtra("pid",pid);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return pidList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txt_date,txt_productName,txt_comment;
        private RatingBar rb_rate;
        private ConstraintLayout ctl_main;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_date = itemView.findViewById(R.id.txt_date_smdl);
            txt_productName = itemView.findViewById(R.id.txt_productname_smdl);
            txt_comment = itemView.findViewById(R.id.txt_comment_smdl);
            rb_rate = itemView.findViewById(R.id.rb_rating_smdl);
            ctl_main = itemView.findViewById(R.id.ctl_main_cmdl);
        }
    }
}
