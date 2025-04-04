package com.example.glimmerheaven.ui.adapters.addressAdapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.glimmerheaven.R;
import com.example.glimmerheaven.data.model.Address;
import com.example.glimmerheaven.data.repository.CustomerRepository;
import com.example.glimmerheaven.data.repository.FirebaseAuthRepository;
import com.example.glimmerheaven.utils.callBacks.MessageCallBack;
import com.example.glimmerheaven.utils.dialogs.UpdateAddressFragmentDialog;
import com.example.glimmerheaven.utils.singleton.UserManage;

import java.util.ArrayList;

public class SavedAddressesAdapter extends RecyclerView.Adapter<SavedAddressesAdapter.holder> {

    private ArrayList<Address> addressList;
    private Context context;
    private FragmentManager fragmentManager;

    public SavedAddressesAdapter(ArrayList<Address> addressList, Context context, FragmentManager fragmentManager) {
        this.addressList = addressList;
        this.context = context;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public SavedAddressesAdapter.holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_address_layout, parent,false);
        return new holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedAddressesAdapter.holder holder, int position) {

        Address address = addressList.get(position);
        holder.txt_address.setText(address.getAddressLineOne()+" "+address.getAddressLineTwo());

        // Update process
        holder.btn_edit.setOnClickListener(view1 -> {
            if(fragmentManager != null){
                UpdateAddressFragmentDialog updateAddressFragmentDialog = new UpdateAddressFragmentDialog();
                Bundle bundle = new Bundle();
                bundle.putInt("addressPosition", position);
                bundle.putString("adlOne", address.getAddressLineOne());
                bundle.putString("adlTwo", address.getAddressLineTwo());
                bundle.putString("province", address.getProvince());
                bundle.putString("district", address.getDistrict());
                bundle.putString("villageOrTown", address.getVillageOrTown());
                updateAddressFragmentDialog.setArguments(bundle);
                updateAddressFragmentDialog.show(fragmentManager,"");
            }
        });

        holder.btn_remove.setOnClickListener(view -> {
            String UID = new FirebaseAuthRepository().getCurrentUser().getUid();
            ArrayList<Address> currentUserAddressList = UserManage.getInstance().getCurrentUser().getValue().getAddress();

            // Remove Process
            if(currentUserAddressList.contains(address)){
                currentUserAddressList.remove(address);

                new CustomerRepository().saveAddressForCustomer(UID, currentUserAddressList, new MessageCallBack() {
                    @Override
                    public void onComplete(boolean status, String message) {
                        if(status){
                            Toast.makeText(context, "Address removed!", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(context, "Failed to remove the address!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    public class holder extends RecyclerView.ViewHolder {
        private TextView txt_address;
        private Button btn_remove,btn_edit;
        public holder(@NonNull View itemView) {
            super(itemView);
            txt_address = itemView.findViewById(R.id.txt_address_singleaddresslayout);
            btn_remove = itemView.findViewById(R.id.btn_remove_singleaddresslayout);
            btn_edit = itemView.findViewById(R.id.btn_edit_singleaddresslayout);
        }
    }
}
