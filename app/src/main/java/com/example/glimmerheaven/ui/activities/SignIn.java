package com.example.glimmerheaven.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.glimmerheaven.MainActivity;
import com.example.glimmerheaven.R;
import com.example.glimmerheaven.ui.viewmodel.SignInViewModel;
import com.example.glimmerheaven.utils.dialogs.SimpleAlertDialog;

public class SignIn extends AppCompatActivity {

    private TextView txt_signUp,txt_email,txt_password, txt_forgotPassword;
    private Button btn_signin;
    private Context context;
    private ConstraintLayout cl_main, cl_loading;
    private SignInViewModel signInViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        signInViewModel = new ViewModelProvider(this).get(SignInViewModel.class);
        context = this;

        txt_email = findViewById(R.id.txt_email_passwordrecovery);
        txt_password = findViewById(R.id.txt_password_signin);
        txt_signUp = findViewById(R.id.txt_sign_up_signin);
        txt_forgotPassword = findViewById(R.id.txt_forgot_password_signin);
        btn_signin = findViewById(R.id.btn_signin_signin);
        cl_main = findViewById(R.id.cl_main_signin);
        cl_loading = findViewById(R.id.cl_loading_signin);

        enableLoadingCL(false);

        txt_forgotPassword.setOnClickListener(view -> {
            startActivity(new Intent(context, ForgotPassword.class));
        });

        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        txt_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SignUp.class);
                startActivity(intent);
            }
        });
    }

    private void signIn() {
        String email = txt_email.getText().toString();
        String password = txt_password.getText().toString();
        enableLoadingCL(true);
        signInViewModel.login(email, password, firebaseUser -> {
            if(firebaseUser != null){
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
                finish();
            }else {
                enableLoadingCL(false);
                new SimpleAlertDialog(context,
                        "Invalid request",
                        "Invalid email or password !",
                        null,
                        "Ok",
                        null,
                        () -> {}
                        ).createDialog().show();
            }
        });
    }

    private void enableLoadingCL(boolean status){
        if(status){
            cl_main.setVisibility(View.INVISIBLE);
            cl_loading.setVisibility(View.VISIBLE);
        }else{
            cl_main.setVisibility(View.VISIBLE);
            cl_loading.setVisibility(View.INVISIBLE);
        }
    }
}