package com.example.glimmerheaven;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import com.example.glimmerheaven.ui.activities.SignIn;
import com.example.glimmerheaven.ui.fragments.cartFragments.CartFragment;
import com.example.glimmerheaven.ui.fragments.categoryFragment.CategoryFragment;
import com.example.glimmerheaven.ui.CustomFragmentFactory.MainNavFactory;
import com.example.glimmerheaven.ui.fragments.homeFragments.HomeFragment;
import com.example.glimmerheaven.ui.fragments.profileFragments.ProfileFragment;
import com.example.glimmerheaven.ui.fragments.wishListFragments.WishListFragment;
import com.example.glimmerheaven.ui.viewmodel.MainViewModel;
import com.example.glimmerheaven.utils.callBacks.MessageCallBack;
import com.example.glimmerheaven.utils.singleton.UserManage;
import com.example.glimmerheaven.utils.testHelp.TestCaseOne;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private FrameLayout home,wishList,cart,category,profile;
    private FrameLayout subFlHome,subFlWishList,subFlCart,subFlCategory,subFlProfile;
    private List<FrameLayout> subFlList = new ArrayList<>();
    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        // User search
        FirebaseUser user = mainViewModel.getCurrentUser();
        if(user == null){
            Intent intent = new Intent(this, SignIn.class);
            startActivity(intent);
            finish();
        }else{
            // Setting customer as current user for future use
            UserManage.getInstance().setCurrentUser(user.getUid(), null);
        }

        // Initialize navigation buttons
        home = findViewById(R.id.fl_home);
        wishList = findViewById(R.id.fl_wishlist);
        cart = findViewById(R.id.fl_cart);
        category = findViewById(R.id.fl_category);
        profile = findViewById(R.id.fl_profile);

        // Initialize sub-frame layouts for managing secondary views
        subFlHome = findViewById(R.id.sub_fl_home);
        subFlWishList = findViewById(R.id.sub_fl_wishlist);
        subFlCart = findViewById(R.id.sub_fl_cart);
        subFlCategory = findViewById(R.id.sub_fl_category);
        subFlProfile = findViewById(R.id.sub_fl_profile);

        // Store sub-frame layouts in a list for reference
        subFlList.add(subFlHome);
        subFlList.add(subFlWishList);
        subFlList.add(subFlCart);
        subFlList.add(subFlCategory);
        subFlList.add(subFlProfile);

        // Initialize FragmentManager with a custom fragment factory
        fragmentManager = getSupportFragmentManager();
        fragmentManager.setFragmentFactory(new MainNavFactory(subFlList));

        boolean fromCart = getIntent().getBooleanExtra("fromCart",false);

        if(fromCart){
            Fragment cartFragment = fragmentManager.findFragmentByTag("cartFragment");
            if(cartFragment == null){
                fragmentManager.beginTransaction()
                        .replace(R.id.main_fragment_container,CartFragment.class, null,"cartFragment")
                        .commit();
            }
        }else{
            // Home fragment create when app Start
            HomeFragment homeFragment = new HomeFragment(subFlHome);
            fragmentManager.beginTransaction()
                    .replace(R.id.main_fragment_container, homeFragment, null)
                    .commit();
        }

        // Set up click listeners to switch fragments
        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    CategoryFragment categoryFragment = new CategoryFragment(subFlCategory);
                    fragmentManager.beginTransaction()
                            .replace(R.id.main_fragment_container, categoryFragment, null)
                            .commit();
            }
        });

        wishList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WishListFragment wishListFragment = new WishListFragment(subFlWishList);
                fragmentManager.beginTransaction()
                        .replace(R.id.main_fragment_container,wishListFragment, null)
                        .commit();
            }
        });

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment cartFragment = fragmentManager.findFragmentByTag("cartFragment");
                if(cartFragment == null){
                    fragmentManager.beginTransaction()
                            .replace(R.id.main_fragment_container,CartFragment.class, null,"cartFragment")
                            .commit();
                }
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    HomeFragment homeFragment = new HomeFragment(subFlHome);
                    fragmentManager.beginTransaction()
                            .replace(R.id.main_fragment_container,homeFragment, null)
                            .commit();
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileFragment profileFragment = new ProfileFragment(subFlProfile);
                fragmentManager.beginTransaction()
                        .replace(R.id.main_fragment_container,profileFragment, null)
                        .commit();
            }
        });

        new TestCaseOne();
    }
}