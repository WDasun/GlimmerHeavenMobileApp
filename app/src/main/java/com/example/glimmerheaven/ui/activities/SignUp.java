package com.example.glimmerheaven.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.glimmerheaven.MainActivity;
import com.example.glimmerheaven.R;
import com.example.glimmerheaven.data.model.Customer;
import com.example.glimmerheaven.ui.viewmodel.SignUpViewModel;
import com.example.glimmerheaven.utils.SharedPreferences.SPManageUID;
import com.example.glimmerheaven.utils.callBacks.FireBaseAuthUserCallBack;
import com.example.glimmerheaven.utils.callBacks.MessageCallBack;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class SignUp extends AppCompatActivity {

    private FloatingActionButton btn_back;
    private MaterialButton btn_signup;
    private TextView txt_fname,txt_lname,txt_email,txt_password;
    private SignUpViewModel signUpViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        signUpViewModel = new ViewModelProvider(this).get(SignUpViewModel.class);
        txt_fname = findViewById(R.id.txt_fname_signup);
        txt_lname = findViewById(R.id.txt_lname_signup);
        txt_email = findViewById(R.id.txt_email_signup);
        txt_password = findViewById(R.id.txt_password_signup);
        btn_back = findViewById(R.id.btn_flt_back_signup);
        btn_signup = findViewById(R.id.btn_signup_signup);

        btn_back.setOnClickListener(new View.OnClickListener() {
            // Back button process
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            // Sign up button process
            @Override
            public void onClick(View view) {
                createNewCustomer();
            }
        });

    }


    private void createNewCustomer() {
        String email = txt_email.getText().toString();
        String password = txt_password.getText().toString();
        String fname = txt_fname.getText().toString();
        String lname = txt_lname.getText().toString();

        signUpViewModel.createNewAccount(email, password, new FireBaseAuthUserCallBack() {
            @Override
            public void onDataComplete(FirebaseUser currentUser) {
                if(currentUser != null){
                    Log.v("ats2", "New account created !");
                    String userUID = currentUser.getUid();
                    Customer customer = new Customer(fname,lname,email,"",0,0,0,true,new ArrayList<>(),new ArrayList<>(),null, null,null,null,null,null,null);
                    signUpViewModel.saveCustomer(userUID,customer, new MessageCallBack() {
                        @Override
                        public void onComplete(boolean isSuccess, String message) {
                            if(isSuccess){
                                // Success message
                                Log.v("ats2", "Customer account created !");
                                Intent intent = new Intent(SignUp.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }else{
                                // Error message
                                Log.v("ats2", "Customer account not created !");
                                Toast.makeText(SignUp.this, "Account not created !", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    // Error message
                    Log.v("ats2", "New account not created !");
                    Toast.makeText(SignUp.this, "Account not created !", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}