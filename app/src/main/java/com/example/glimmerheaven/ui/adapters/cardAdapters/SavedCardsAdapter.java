package com.example.glimmerheaven.ui.adapters.cardAdapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.glimmerheaven.R;
import com.example.glimmerheaven.data.model.Card;
import com.example.glimmerheaven.data.repository.CustomerRepository;
import com.example.glimmerheaven.data.repository.FirebaseAuthRepository;
import com.example.glimmerheaven.utils.callBacks.MessageCallBack;
import com.example.glimmerheaven.utils.dialogs.UpdateCardFragmentDialog;
import com.example.glimmerheaven.utils.singleton.UserManage;

import java.util.ArrayList;

public class SavedCardsAdapter extends RecyclerView.Adapter<SavedCardsAdapter.holder> {
    private ArrayList<Card> cardList;
    private Context context;
    private FragmentManager fragmentManager;

    public SavedCardsAdapter(ArrayList<Card> cardList, Context context, FragmentManager fragmentManager) {
        this.cardList = cardList;
        this.context = context;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public SavedCardsAdapter.holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_card_layout, parent,false);
        return new holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedCardsAdapter.holder holder, int position) {
        if(cardList != null){
            Card card = cardList.get(position);
            holder.txt_cardType.setText(card.getCardType());
            holder.txt_cardNumber.setText(card.getCardNumber());
            holder.txt_holderName.setText(card.getCardHolderName());
            holder.txt_expireDate.setText("Expires: "+card.getExpireMonth()+"/"+String.valueOf(card.getExpireYear()).substring(2));

            holder.btn_edit.setOnClickListener(view -> {
                Bundle bundle = new Bundle();
                bundle.putInt("position", position);
                bundle.putString("cardNumber", card.getCardNumber());
                bundle.putString("holderName", card.getCardHolderName());
                bundle.putString("month", String.valueOf(card.getExpireMonth()));
                bundle.putString("year", String.valueOf(card.getExpireYear()));
                bundle.putString("cardType", card.getCardType());

                UpdateCardFragmentDialog dialog = new UpdateCardFragmentDialog();
                dialog.setArguments(bundle);
                dialog.show(fragmentManager, "");

            });

            holder.btn_remove.setOnClickListener(view -> {
                cardList.remove(card);
                String UID = new FirebaseAuthRepository().getCurrentUser().getUid();
                new CustomerRepository().saveCards(UID, cardList, new MessageCallBack() {
                    @Override
                    public void onComplete(boolean status, String message) {
                        if(status){
                            Toast.makeText(context, "Card removed!", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(context, "Failed to remove the card!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            });
        }
    }

    @Override
    public int getItemCount() {
        if(cardList != null){
            return cardList.size();
        }else{
            return 0;
        }
    }

    public class holder extends RecyclerView.ViewHolder {
        private TextView txt_cardType, txt_cardNumber, txt_holderName, txt_expireDate;
        private Button btn_edit,btn_remove;
        public holder(@NonNull View itemView) {
            super(itemView);
            txt_cardType = itemView.findViewById(R.id.txt_card_type_single_card_layout);
            txt_cardNumber = itemView.findViewById(R.id.txt_card_number_single_card_layout);
            txt_holderName = itemView.findViewById(R.id.txt_holder_name_single_card_layout);
            txt_expireDate = itemView.findViewById(R.id.txt_expire_date_single_card_layout);
            btn_remove = itemView.findViewById(R.id.btn_remove_single_card_layout);
            btn_edit = itemView.findViewById(R.id.btn_edit_single_card_layout);
        }
    }
}
