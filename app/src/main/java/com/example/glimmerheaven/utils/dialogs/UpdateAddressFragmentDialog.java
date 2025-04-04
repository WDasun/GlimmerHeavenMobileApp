package com.example.glimmerheaven.utils.dialogs;

import android.os.Bundle;
import android.util.Log;
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

public class UpdateAddressFragmentDialog extends DialogFragment {

    private MaterialCardView card_save, card_cancel;
    private TextInputEditText txt_addressLineOne,txt_addressLineTwo,txt_province,txt_district,txt_townOrVillage;
    private SaveAddressesViewModel saveAddressesViewModel;

    public UpdateAddressFragmentDialog() {
        super(R.layout.dialog_fragment_update_address);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        saveAddressesViewModel = new ViewModelProvider(requireActivity()).get(SaveAddressesViewModel.class);

        card_save = view.findViewById(R.id.card_update_update_address);
        card_cancel = view.findViewById(R.id.card_cancel_update_address);
        txt_addressLineOne = view.findViewById(R.id.txt_addresslineone_update_address);
        txt_addressLineTwo = view.findViewById(R.id.txt_addresslinetwo_update_address);
        txt_province = view.findViewById(R.id.txt_province_update_address);
        txt_district = view.findViewById(R.id.txt_district_update_address);
        txt_townOrVillage = view.findViewById(R.id.txt_townorvillage_update_address);

        String adlOne = getArguments().getString("adlOne","");
        String adlTwo = getArguments().getString("adlTwo","");
        String province = getArguments().getString("province","");
        String district = getArguments().getString("district","");
        String villageOrTown = getArguments().getString("villageOrTown","");

        txt_addressLineOne.setText(adlOne);
        txt_addressLineTwo.setText(adlTwo);
        txt_province.setText(province);
        txt_district.setText(district);
        txt_townOrVillage.setText(villageOrTown);

        card_save.setOnClickListener(view1 -> {
            String adl1New = txt_addressLineOne.getText().toString();
            String adl2New = txt_addressLineTwo.getText().toString();
            String provinceNew = txt_province.getText().toString();
            String districtNew = txt_district.getText().toString();
            String townOrVillageNew = txt_townOrVillage.getText().toString();

            try {
                if(!adl1New.isEmpty() && !adl2New.isEmpty() && !provinceNew.isEmpty() && !districtNew.isEmpty() && !townOrVillageNew.isEmpty()){

                    int addressPosition = getArguments().getInt("addressPosition");

                    Address updatedAddress = new Address(adl1New, adl2New, townOrVillageNew, districtNew, provinceNew);

                    ArrayList<Address> addressArrayList = UserManage.getInstance().getCurrentUser().getValue().getAddress();

                    addressArrayList.set(addressPosition, updatedAddress);

                    saveAddressesViewModel.saveNewAddress(addressArrayList, new MessageCallBack() {
                        @Override
                        public void onComplete(boolean status, String message) {
                            if(status){
                                Toast.makeText(view.getContext(), "Address Updated!", Toast.LENGTH_SHORT).show();
                                dismiss();
                            }else{
                                Toast.makeText(view.getContext(), "Update failed!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(view.getContext(), "Please fill in all required fields!", Toast.LENGTH_SHORT).show();
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
