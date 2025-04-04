package com.example.glimmerheaven.utils.dialogs;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.glimmerheaven.R;
import com.example.glimmerheaven.data.model.Card;
import com.example.glimmerheaven.ui.viewmodel.SaveCardsViewModel;
import com.example.glimmerheaven.utils.callBacks.MessageCallBack;
import com.example.glimmerheaven.utils.singleton.UserManage;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class UpdateCardFragmentDialog extends DialogFragment {

    private TextInputEditText txt_cardNumber, txt_holderName,txt_expireYear;
    private AutoCompleteTextView txt_cardType,txt_expireMonth;
    private MaterialCardView card_update, card_cancel;
    private String[] cardTypes = {
            "VISA",
            "MASTER"
    };
    private String[] months = {
            "01", "02", "03", "04", "05", "06",
            "07", "08", "09", "10", "11", "12"
    };
    private SaveCardsViewModel saveCardsViewModel;


    public UpdateCardFragmentDialog() {
        super(R.layout.dialog_fragment_update_card);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        saveCardsViewModel = new ViewModelProvider(requireActivity()).get(SaveCardsViewModel.class);

        card_update = view.findViewById(R.id.update_updatecard_dialog);
        card_cancel = view.findViewById(R.id.card_cancel_update_card_dialog);
        txt_cardType = view.findViewById(R.id.atext_cardtype_updatecard);
        txt_cardNumber = view.findViewById(R.id.txt_card_number_cartpayment);
        txt_holderName = view.findViewById(R.id.txt_holdername_cartpayment);
        txt_expireMonth = view.findViewById(R.id.atxt_month_cartpayment);
        txt_expireYear = view.findViewById(R.id.txt_year_cartpayment);

        txt_cardType.setAdapter(new ArrayAdapter<>(view.getContext(), com.google.android.material.R.layout.support_simple_spinner_dropdown_item, cardTypes));
        txt_expireMonth.setAdapter(new ArrayAdapter<>(view.getContext(), com.google.android.material.R.layout.support_simple_spinner_dropdown_item, months));

        Bundle valuesBundle = getArguments();
        txt_cardNumber.setText(valuesBundle.getString("cardNumber",""));
        txt_holderName.setText(valuesBundle.getString("holderName",""));
        txt_expireMonth.setText(valuesBundle.getString("month","1"),false);
        txt_expireYear.setText(valuesBundle.getString("year",""));
        txt_cardType.setText(valuesBundle.getString("cardType",""),false);

        card_update.setOnClickListener(view1 -> {
            String cardType = txt_cardType.getText().toString();
            String cardNumber = txt_cardNumber.getText().toString();
            String holderName = txt_holderName.getText().toString();
            String expireMoth = txt_expireMonth.getText().toString();
            String expireYear = txt_expireYear.getText().toString();
            try {
                if(!cardType.isEmpty() && !cardNumber.isEmpty() && !holderName.isEmpty() &&
                        !expireMoth.isEmpty() && !expireYear.isEmpty()){
                    int position = valuesBundle.getInt("position");
                    Card newCard = new Card(cardNumber,holderName, Integer.parseInt(expireYear),Integer.parseInt(expireMoth), cardType);
                    ArrayList<Card> cardList = UserManage.getInstance().getCurrentUser().getValue().getCard();
                    cardList.set(position,newCard);

                    saveCardsViewModel.saveCard(cardList, new MessageCallBack() {
                        @Override
                        public void onComplete(boolean status, String message) {
                            if(status){
                                Toast.makeText(view.getContext(), "Card save successful!", Toast.LENGTH_SHORT).show();
                                dismiss();
                            }else{
                                Toast.makeText(view.getContext(), "Card save failed!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }else{
                    Toast.makeText(view.getContext(), "All fields must be completed!", Toast.LENGTH_SHORT).show();
                }
            } catch (NullPointerException ne) {
                Toast.makeText(view.getContext(), "Parameters must not null!", Toast.LENGTH_SHORT).show();
            }
        });

        card_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });


    }
}
