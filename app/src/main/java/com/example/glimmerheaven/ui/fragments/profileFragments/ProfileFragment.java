package com.example.glimmerheaven.ui.fragments.profileFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.glimmerheaven.MainActivity;
import com.example.glimmerheaven.data.model.Customer;
import com.example.glimmerheaven.data.repository.CustomerRepository;
import com.example.glimmerheaven.data.repository.FirebaseAuthRepository;
import com.example.glimmerheaven.ui.activities.EditProfileActivity;
import com.example.glimmerheaven.ui.activities.OrdersActivity;
import com.example.glimmerheaven.R;
import com.example.glimmerheaven.ui.activities.SaveCardsActivity;
import com.example.glimmerheaven.ui.activities.SavedAddressesActivity;
import com.example.glimmerheaven.ui.activities.WishlistActivity;
import com.example.glimmerheaven.utils.callBacks.CustomerCallBack;
import com.example.glimmerheaven.utils.eventCanceller.ValueEventListnerHolder;
import com.example.glimmerheaven.utils.singleton.UserManage;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileFragment extends Fragment {
    private FrameLayout subFl;
    private LinearLayout lrl_editProfile, lrl_saveCardsProfile, lrl_logout, lrl_savedAddress;
    private FragmentManager fragmentManager;
    private CardView card_yourOrder, card_wishList;
    private ValueEventListnerHolder canceller;
    private TextView txt_customerName;

    public ProfileFragment() {
        super(R.layout.fragment_profile);
    }

    public ProfileFragment(FrameLayout subFlHome) {
        super(R.layout.fragment_profile);
        this.subFl = subFlHome;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        card_yourOrder = view.findViewById(R.id.card_your_order);
        card_wishList = view.findViewById(R.id.card_wishlist_profile);
        lrl_editProfile = view.findViewById(R.id.lrl_edit_profile_profile);
        lrl_saveCardsProfile = view.findViewById(R.id.lrl_save_cards_profile);
        lrl_logout = view.findViewById(R.id.lyt_exit_profile);
        lrl_savedAddress = view.findViewById(R.id.lrl_savedaddress_profile);
        txt_customerName = view.findViewById(R.id.txt_customer_name_profile);

        UserManage.getInstance().getCurrentUser().observe(getViewLifecycleOwner(),customer -> {
            txt_customerName.setText(customer.getFname()+" "+customer.getLname());
        });

        lrl_savedAddress.setOnClickListener(view1 -> {
            Intent intent = new Intent(view.getContext(), SavedAddressesActivity.class);
            startActivity(intent);
        });

        lrl_logout.setOnClickListener(view1 -> {
            new FirebaseAuthRepository().signOutCurrentUser();
            Intent intent = new Intent(view.getContext(), MainActivity.class);
            startActivity(intent);
            getActivity().finish();
        });

        card_wishList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), WishlistActivity.class);
                startActivity(intent);
            }
        });

        card_yourOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), OrdersActivity.class);
                startActivity(intent);
            }
        });

        lrl_editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), EditProfileActivity.class);
                startActivity(intent);
            }
        });

        lrl_saveCardsProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), SaveCardsActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v("ats2", "PF on destroy");
    }
}