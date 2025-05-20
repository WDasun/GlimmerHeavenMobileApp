package com.example.glimmerheaven.ui.fragments.profileFragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.glimmerheaven.MainActivity;
import com.example.glimmerheaven.data.model.Customer;
import com.example.glimmerheaven.data.repository.CustomerRepository;
import com.example.glimmerheaven.data.repository.FirebaseAuthRepository;
import com.example.glimmerheaven.data.repository.supabase.SupabaseImageUploader;
import com.example.glimmerheaven.ui.activities.EditProfileActivity;
import com.example.glimmerheaven.ui.activities.GoogleMapActivity;
import com.example.glimmerheaven.ui.activities.NotificationManage;
import com.example.glimmerheaven.ui.activities.OrdersActivity;
import com.example.glimmerheaven.R;
import com.example.glimmerheaven.ui.activities.ProductActivity;
import com.example.glimmerheaven.ui.activities.ReviewsActivity;
import com.example.glimmerheaven.ui.activities.SaveCardsActivity;
import com.example.glimmerheaven.ui.activities.SavedAddressesActivity;
import com.example.glimmerheaven.ui.activities.SignIn;
import com.example.glimmerheaven.ui.activities.WishlistActivity;
import com.example.glimmerheaven.ui.viewmodel.ProfileFragmentViewModel;
import com.example.glimmerheaven.utils.SharedPreferences.SPManageUID;
import com.example.glimmerheaven.utils.callBacks.CustomerCallBack;
import com.example.glimmerheaven.utils.dialogs.SimpleAlertDialog;
import com.example.glimmerheaven.utils.eventCanceller.ValueEventListnerHolder;
import com.example.glimmerheaven.utils.singleton.PermissionHandler;
import com.example.glimmerheaven.utils.singleton.UserManage;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ProfileFragment extends Fragment {
    private ProfileFragmentViewModel profileFragmentViewModel;
    private ShapeableImageView img_profile;
    private FrameLayout subFl, fl_profileImage;
    private LinearLayout lrl_editProfile, lrl_saveCardsProfile, lrl_logout, lrl_savedAddress, ll_reviews;
    private CardView card_yourOrder, card_wishList, card_notification, card_nearShops;
    private TextView txt_customerName;
    private Context context;
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private static final int REQUEST_IMAGE_CAPTURE = 100;
    private static final int REQUEST_IMAGE_SELECT_FROM_GALLERY = 101;


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

        profileFragmentViewModel = new ViewModelProvider(requireActivity()).get(ProfileFragmentViewModel.class);
        // Permission manage
        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if(isGranted){
                openCamera();
            }else{

                    new SimpleAlertDialog(requireContext(),
                            "Permission Denied Permanently",
                            "You have permanently denied camera permission. Please enable it in app settings.",
                            "Go to Settings",
                            "Cancel",
                            () -> {
                                Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(android.net.Uri.parse("package:" + requireContext().getPackageName()));
                                startActivity(intent);
                            },
                            null)
                            .createDialog()
                            .show();

            }
        });

        context = requireContext();

        card_yourOrder = view.findViewById(R.id.card_your_order);
        card_wishList = view.findViewById(R.id.card_wishlist_profile);
        card_notification = view.findViewById(R.id.notification_profile);
        card_nearShops = view.findViewById(R.id.cv_nearshops_profile);
        lrl_editProfile = view.findViewById(R.id.lrl_edit_profile_profile);
        lrl_saveCardsProfile = view.findViewById(R.id.lrl_save_cards_profile);
        lrl_logout = view.findViewById(R.id.lyt_exit_profile);
        lrl_savedAddress = view.findViewById(R.id.lrl_savedaddress_profile);
        ll_reviews = view.findViewById(R.id.ll_reviews_profile);
        txt_customerName = view.findViewById(R.id.txt_customer_name_profile);
        fl_profileImage = view.findViewById(R.id.profile_image_profile);
        img_profile = view.findViewById(R.id.img_profile_profile);

        loadProfileImage();

        ll_reviews.setOnClickListener(view1 -> {
            Intent intent = new Intent(getContext(), ReviewsActivity.class);
            startActivity(intent);
        });

        card_nearShops.setOnClickListener(view1 -> {
            startActivity(new Intent(context, GoogleMapActivity.class));
        });

        fl_profileImage.setOnClickListener(view1 -> {

            new SimpleAlertDialog(requireContext(),
                    "Select Image Source",
                    "Select an option to upload an image:",
                    "Gallery",
                    "Camera",
                    () -> {
                        openGallery();
                    },
                    () ->{
                        if (requireContext().getPackageManager().hasSystemFeature(
                                PackageManager.FEATURE_CAMERA_FRONT)) {
                            requestPermissionLauncher.launch(Manifest.permission.CAMERA);
                        } else {
                            new SimpleAlertDialog(requireContext(),
                                    "Not Found",
                                    "Camera Not Available.",
                                    "Ok",
                                    null,
                                    null,
                                    null)
                                    .createDialog()
                                    .show();
                        }
                    })
                    .createDialog()
                    .show();

        });

        card_notification.setOnClickListener(view1 -> {
            Intent intent = new Intent(requireContext(), NotificationManage.class);
            startActivity(intent);
        });

        UserManage.getInstance().getCurrentUser().observe(getViewLifecycleOwner(),customer -> {
            txt_customerName.setText(customer.getFname()+" "+customer.getLname());
        });

        lrl_savedAddress.setOnClickListener(view1 -> {
            Intent intent = new Intent(view.getContext(), SavedAddressesActivity.class);
            startActivity(intent);
        });

        lrl_logout.setOnClickListener(view1 -> {
            // Remove FCM Token from the user
            new CustomerRepository().removeFCMTokenFromCustomer(new FirebaseAuthRepository().getCurrentUser().getUid(),null);
            // Sign out from Firebase Auth
            new FirebaseAuthRepository().signOutCurrentUser();
//             Remove Uid from sharedPreferences
            boolean rls = new SPManageUID(requireContext()).removeUid();

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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if(requestCode == REQUEST_IMAGE_CAPTURE){
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");

                saveImageInDBAndProfileProcess(imageBitmap);

            } else if (requestCode == REQUEST_IMAGE_SELECT_FROM_GALLERY && data != null && data.getData() != null) {
                Uri imageUri = data.getData();
                try {
                    Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), imageUri);
                    saveImageInDBAndProfileProcess(imageBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Failed to load image from gallery", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void saveImageInDBAndProfileProcess(Bitmap imageBitmap){
        new SimpleAlertDialog(requireContext(),
                "Upload profile image",
                "Do you want to set this image as the profile picture ?",
                "Yes",
                "No",
                () -> {
                    File file = saveBitmapToFile(imageBitmap);
                    String uid = new FirebaseAuthRepository().getCurrentUser().getUid();

                    if(file != null && uid != null){
                        try{
                            SupabaseImageUploader.uploadImage(file, uid, new SupabaseImageUploader.UploadCallback() {
                                @Override
                                public void onSuccess(String savedUrl) {
                                    new CustomerRepository().changeImageUrl(uid, savedUrl, (status, message) -> {
                                        requireActivity().runOnUiThread(() -> {
                                            if(status){
                                                loadProfileImage();
                                                Toast.makeText(context, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                                            }else{
                                                Toast.makeText(context, "Image not changed!", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    });
                                }

                                @Override
                                public void onFailure(String error) {
                                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                null)
                .createDialog()
                .show();
    }
    private File saveBitmapToFile(Bitmap bitmap) {
        File imageFile = null;
        try {

            File storageDir = requireContext().getCacheDir();
            imageFile = new File(storageDir, "captured_image_" + System.currentTimeMillis() + ".jpg");

            // 2. Write the bitmap into the file
            FileOutputStream fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos); // quality = 100 (max)
            fos.flush();
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageFile;
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(requireContext().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_SELECT_FROM_GALLERY);
    }


    private void loadProfileImage(){
        UserManage.getInstance().getCurrentUser().observe(getViewLifecycleOwner(), customer -> {
            if(customer.getImgUrl() != null){
                Glide.with(context)
                        .load(UserManage.getInstance().getCurrentUser().getValue().getImgUrl())
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(img_profile);
            }
        });
    }

}