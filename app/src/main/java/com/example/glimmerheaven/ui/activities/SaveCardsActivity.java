package com.example.glimmerheaven.ui.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.glimmerheaven.ui.adapters.cardAdapters.SavedCardsAdapter;
import com.example.glimmerheaven.ui.viewmodel.SaveCardsViewModel;
import com.example.glimmerheaven.utils.dialogs.AddNewCardFragmentDialog;
import com.example.glimmerheaven.R;
import com.example.glimmerheaven.utils.singleton.UserManage;
import com.google.android.material.card.MaterialCardView;

public class SaveCardsActivity extends AppCompatActivity {
    private RecyclerView rcy_reRecyclerView;
    private ImageView img_back;
    private MaterialCardView addNewCard;
    private SaveCardsViewModel saveCardsViewModel;
    private SavedCardsAdapter savedCardsAdapter;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_save_cards);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        saveCardsViewModel = new ViewModelProvider(this).get(SaveCardsViewModel.class);
        context = this;

        img_back = findViewById(R.id.back_arrow_add_card);
        addNewCard = findViewById(R.id.card_add_new_card_saved_cards);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        addNewCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               new AddNewCardFragmentDialog()
                       .show(getSupportFragmentManager(), "");
            }
        });

        UserManage.getInstance().getCurrentUser().observe(this, customer -> {
            rcy_reRecyclerView = findViewById(R.id.rcy_cards_savedcards);
            savedCardsAdapter = new SavedCardsAdapter(customer.getCard(), context, getSupportFragmentManager());
            rcy_reRecyclerView.setAdapter(savedCardsAdapter);
            rcy_reRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        });

    }
}