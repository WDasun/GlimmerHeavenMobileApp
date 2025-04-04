package com.example.glimmerheaven.ui.fragments.homeFragments;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.glimmerheaven.R;

public class HomeFragment extends Fragment {

    FrameLayout subFlHome;

    public HomeFragment() {
        super(R.layout.fragment_home_fragment);
    }

    public HomeFragment(FrameLayout subFlHome) {
        super(R.layout.fragment_home_fragment);
        this.subFlHome = subFlHome;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(subFlHome != null){
            subFlHome.setBackgroundResource(R.drawable.rounded_framel);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(subFlHome != null){
            subFlHome.setBackground(null);
        }
    }
}