package com.example.glimmerheaven.utils.dialogs;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.glimmerheaven.R;
import com.example.glimmerheaven.data.model.Address;
import com.example.glimmerheaven.ui.viewmodel.SaveAddressesViewModel;
import com.example.glimmerheaven.utils.callBacks.MessageCallBack;
import com.example.glimmerheaven.utils.singleton.UserManage;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class AddNewAddressFragmentDialog extends DialogFragment {

    private MaterialCardView card_save, card_cancel;
    private TextInputEditText txt_addressLineOne,txt_addressLineTwo,txt_province,txt_district,txt_townOrVillage;
    private SaveAddressesViewModel saveAddressesViewModel;

    public AddNewAddressFragmentDialog() {
        super(R.layout.dialog_fragment_add_new_address);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        saveAddressesViewModel = new ViewModelProvider(requireActivity()).get(SaveAddressesViewModel.class);

        card_save = view.findViewById(R.id.card_save_new_address);
        card_cancel = view.findViewById(R.id.card_cancel_new_address);
        txt_addressLineOne = view.findViewById(R.id.txt_addresslineone_update_address);
        txt_addressLineTwo = view.findViewById(R.id.txt_addresslinetwo_update_address);
        txt_province = view.findViewById(R.id.txt_province_update_address);
        txt_district = view.findViewById(R.id.txt_district_update_address);
        txt_townOrVillage = view.findViewById(R.id.txt_townorvillage_update_address);

        card_save.setOnClickListener(view1 -> {
            String adl1 = txt_addressLineOne.getText().toString();
            String adl2 = txt_addressLineTwo.getText().toString();
            String province = txt_province.getText().toString();
            String district = txt_district.getText().toString();
            String townOrVillage = txt_townOrVillage.getText().toString();

            try {
                if(!adl1.isEmpty() && !adl2.isEmpty() && !province.isEmpty() && !district.isEmpty() && !townOrVillage.isEmpty()){
                    ArrayList<Address> addressArrayList = UserManage.getInstance().getCurrentUser().getValue().getAddress();
                    addressArrayList.add(new Address(adl1, adl2, townOrVillage, district, province));
                    saveAddressesViewModel.saveNewAddress(addressArrayList, new MessageCallBack() {
                        @Override
                        public void onComplete(boolean status, String message) {
                            if(status){
                                Toast.makeText(view.getContext(), "Address save successful!", Toast.LENGTH_SHORT).show();
                                dismiss();
                            }else{
                                Toast.makeText(view.getContext(), "Address save failed!", Toast.LENGTH_SHORT).show();
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

        card_cancel.setOnClickListener(view1 -> {
            dismiss();
        });
    }

}
