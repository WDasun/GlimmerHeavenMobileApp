package com.example.glimmerheaven.ui.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.glimmerheaven.data.model.Customer;
import com.example.glimmerheaven.data.repository.CustomerRepository;
import com.example.glimmerheaven.data.repository.FirebaseAuthRepository;
import com.example.glimmerheaven.data.repository.supabase.SupabaseImageUploader;
import com.example.glimmerheaven.ui.viewmodel.EditProfileViewModel;
import com.example.glimmerheaven.utils.callBacks.MessageCallBack;
import com.example.glimmerheaven.utils.dialogs.SimpleAlertDialog;
import com.example.glimmerheaven.utils.pickers.DatePickerFragment;
import com.example.glimmerheaven.R;
import com.example.glimmerheaven.utils.singleton.UserManage;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditProfileActivity extends AppCompatActivity {
    private ImageView img_back, profilPic;
    private TextInputEditText txt_dob,txt_fname,txt_lname,txt_email, txt_cnt;
    private AutoCompleteTextView actvGender;
    private Button btn_save;
    private long selectedDateInMillis;
    private String[]  genders = {
            "Male",
            "Female"
    };
    private ArrayAdapter<String> adapterGenders;
    private EditProfileViewModel editProfileViewModel;

    private ActivityResultLauncher<String> requestPermissionLauncher;
    private static final int REQUEST_IMAGE_CAPTURE = 100;
    private static final int REQUEST_IMAGE_SELECT_FROM_GALLERY = 101;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editProfileViewModel = new ViewModelProvider(this).get(EditProfileViewModel.class);
        context = this;

        // Permission manage
        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if(isGranted){
                openCamera();
            }else{

                new SimpleAlertDialog(context,
                        "Permission Denied Permanently",
                        "You have permanently denied camera permission. Please enable it in app settings.",
                        "Go to Settings",
                        "Cancel",
                        () -> {
                            Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.setData(android.net.Uri.parse("package:" + context.getPackageName()));
                            startActivity(intent);
                        },
                        null)
                        .createDialog()
                        .show();

            }
        });

        profilPic = findViewById(R.id.ivProfilePicture);
        img_back = findViewById(R.id.img_back_availableshops);
        txt_dob = findViewById(R.id.etDateOfBirth);
        txt_fname = findViewById(R.id.txt_fname_profile);
        txt_lname = findViewById(R.id.txt_lname_profile);
        txt_email = findViewById(R.id.txt_email_profile);
        txt_cnt = findViewById(R.id.txt_cnt_profile);
        actvGender = findViewById(R.id.actvGender);
        btn_save = findViewById(R.id.btn_save_profile);

        adapterGenders = new ArrayAdapter<>(this, com.google.android.material.R.layout.support_simple_spinner_dropdown_item, genders);

        // To manage gender selection
        actvGender.setAdapter(adapterGenders);

        // Load customer data
        setCustomerData();

        profilPic.setOnClickListener(view -> {
            new SimpleAlertDialog(context,
                    "Select Image Source",
                    "Select an option to upload an image:",
                    "Gallery",
                    "Camera",
                    () -> {
                        openGallery();
                    },
                    () ->{
                        if (context.getPackageManager().hasSystemFeature(
                                PackageManager.FEATURE_CAMERA_FRONT)) {
                            requestPermissionLauncher.launch(Manifest.permission.CAMERA);
                        } else {
                            new SimpleAlertDialog(context,
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

        btn_save.setOnClickListener(view -> {

            String fname =txt_fname.getText().toString();
            String lname = txt_lname.getText().toString();
            String email = txt_email.getText().toString();
            String cnt = txt_cnt.getText().toString();
            Long seletedDate = selectedDateInMillis;
            String gender = actvGender.getText().toString();


            if(checkNullOrEmptyString(fname) &&
                    checkNullOrEmptyString(lname) &&
                    checkNullOrEmptyString(email) &&
                    checkNullOrEmptyString(cnt) &&
                    seletedDate != 0 &&
                    checkNullOrEmptyString(gender)){

                Customer customer = editProfileViewModel.getCurrentCustomer();

                customer.setFname(fname);
                customer.setLname(lname);
                customer.setEmail(email);
                customer.setCnt(cnt);
                customer.setBirthDay(seletedDate);
                customer.setGender(gender);

                editProfileViewModel.updateCustomer(customer, new MessageCallBack() {
                    @Override
                    public void onComplete(boolean status, String message) {
                        if(status){
                            sendToast("Details saved!");
                        }else{
                            sendToast("Updating user failed!");
                        }
                    }
                });

            }else{
                sendToast("All fields must be completed!");
            }

        });

        // To manage date picker for get DOB
        txt_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dobPicker = new DatePickerFragment();
                dobPicker.show(getSupportFragmentManager(), "dobPicker");
            }
        });

        getSupportFragmentManager().setFragmentResultListener("datePickerResult", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(String requestKey, Bundle result) {
                if (requestKey.equals("datePickerResult")) {
                    String selectedDate = result.getString("selectedDate");
                    txt_dob.setText(selectedDate);
                    selectedDateInMillis = result.getLong("selectedDateInMillis");
                }
            }
        });

        // Back button function
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void setCustomerData() {
        Customer customer = editProfileViewModel.getCurrentCustomer();
        if(checkNullOrEmptyString(customer.getFname())){
            txt_fname.setText(customer.getFname());
        }
        if(checkNullOrEmptyString(customer.getLname())){
            txt_lname.setText(customer.getLname());
        }
        if(checkNullOrEmptyString(customer.getEmail())){
            txt_email.setText(customer.getEmail());
        }
        if(checkNullOrEmptyString(customer.getCnt())){
            txt_cnt.setText(customer.getCnt());
        }
        if(customer.getBirthDay() != 0){
            selectedDateInMillis = customer.getBirthDay();
            Date date = new Date(customer.getBirthDay());
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            txt_dob.setText(sdf.format(date));
        }
        if(checkNullOrEmptyString(customer.getGender())){
            if(customer.getGender().equals("Male")){
                actvGender.setText("Male", false);
            }else{
                actvGender.setText("Female", false);
            }
        }
        if(checkNullOrEmptyString(customer.getImgUrl())){
            loadProfileImage();
        }
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
                    Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);
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
        new SimpleAlertDialog(context,
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
                                        runOnUiThread(() -> {
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

            File storageDir = context.getCacheDir();
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
        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_SELECT_FROM_GALLERY);
    }


    private void loadProfileImage(){
        UserManage.getInstance().getCurrentUser().observe(this, customer -> {
            if(customer.getImgUrl() != null){
                Glide.with(context)
                        .load(UserManage.getInstance().getCurrentUser().getValue().getImgUrl())
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(profilPic);
            }
        });
    }

    private boolean checkNullOrEmptyString(String text){
       boolean notNullOrEmty = false;

       if(text != null && !text.equals("")){
           notNullOrEmty = true;
       }else{
           notNullOrEmty = false;
       }

       return  notNullOrEmty;
    }

    private void sendToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}