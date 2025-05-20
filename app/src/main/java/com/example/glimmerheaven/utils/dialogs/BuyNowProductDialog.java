package com.example.glimmerheaven.utils.dialogs;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.glimmerheaven.R;
import com.example.glimmerheaven.ui.activities.BuyProductNowActivity;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class BuyNowProductDialog extends DialogFragment {
    private TextInputLayout til_qty;
    private TextInputEditText txt_qty;
    private MaterialCardView card_buyNow,card_cancel;
    private String productId;
    private int maxQty, fragmentContainerId;

    public BuyNowProductDialog() {
        super(R.layout.dialog_fragment_buy_now_product_layout);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        til_qty = view.findViewById(R.id.til_qty_buynowproductdialog);
        txt_qty = view.findViewById(R.id.txt_qty_buynowproductdialog);
        card_buyNow = view.findViewById(R.id.card_buynow_buynowproduct_dialog);
        card_cancel = view.findViewById(R.id.card_cancel_buynowproduct_dialog);

        Bundle bundle = getArguments();
        if(bundle != null){
            productId = bundle.getString("pid");
            maxQty = bundle.getInt("qty");
            fragmentContainerId = bundle.getInt("fcid");
        }
        til_qty.setHint("Enter qty (Max "+maxQty+")");

        card_buyNow.setOnClickListener(view1 -> {
            try{
                int qty = Integer.valueOf(txt_qty.getText().toString());

                if(qty != 0){
                    if(!(qty > maxQty)){
                        Intent intent = new Intent(getContext(), BuyProductNowActivity.class);
                        bundle.putInt("qty",qty);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }else{
                        Toast.makeText(getContext(),"Invalid qty!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getContext(),"Enter qty!", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                Toast.makeText(getContext(),"Invalid value", Toast.LENGTH_SHORT).show();
            }
        });

        card_cancel.setOnClickListener(view1 -> {
            dismiss();
        });
    }
}
