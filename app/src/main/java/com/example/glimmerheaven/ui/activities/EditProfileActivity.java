package com.example.glimmerheaven.ui.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.ViewModelProvider;

import com.example.glimmerheaven.data.model.Customer;
import com.example.glimmerheaven.ui.viewmodel.EditProfileViewModel;
import com.example.glimmerheaven.utils.callBacks.MessageCallBack;
import com.example.glimmerheaven.utils.pickers.DatePickerFragment;
import com.example.glimmerheaven.R;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EditProfileActivity extends AppCompatActivity {
    private ImageView img_back;
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

        img_back = findViewById(R.id.back_arrow_add_profile);
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