package com.example.glimmerheaven.ui.fragments.cartFragments;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.glimmerheaven.R;
import com.example.glimmerheaven.data.model.PreOrder;
import com.example.glimmerheaven.ui.adapters.CartAdapters.CartItemListAdapter;
import com.example.glimmerheaven.ui.viewmodel.CartViewModel;
import com.example.glimmerheaven.utils.singleton.UserManage;

public class CartItemListFragment extends Fragment {

    private RecyclerView rcy_cartItemList;
    private TextView txt_proceedToBy;
    private Context context;
    private CartViewModel cartViewModel;

    public CartItemListFragment() {
        super(R.layout.fragment_cart_item_list);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = view.getContext();
        cartViewModel = new ViewModelProvider(getParentFragment()).get(CartViewModel.class);
        cartViewModel.setPreOrder(new PreOrder());

        txt_proceedToBy = view.findViewById(R.id.txt_proceedToBy_cartitemlist);

        UserManage.getInstance().getCurrentUser().observe(getViewLifecycleOwner(), customer -> {
            rcy_cartItemList = view.findViewById(R.id.rcy_main_cartitemlist);
            rcy_cartItemList.setAdapter(new CartItemListAdapter(customer.getCart(),context,cartViewModel));
            rcy_cartItemList.setLayoutManager(new LinearLayoutManager(context));
        });

        FragmentManager fragmentManager = getParentFragmentManager();

        // Continue process button press
        txt_proceedToBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!cartViewModel.getPreOrder().getSelectedCartItemList().isEmpty()){
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container_cart, CartAddAddressFragment.class, null)
                            .setReorderingAllowed(true)
                            .addToBackStack(null)
                            .commit();
                }else{
                    Toast.makeText(context,"No selected items!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
