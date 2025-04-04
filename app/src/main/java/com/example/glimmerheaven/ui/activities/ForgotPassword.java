package com.example.glimmerheaven.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.glimmerheaven.R;
import com.example.glimmerheaven.ui.viewmodel.FogotPasswordViewModel;
import com.example.glimmerheaven.utils.dialogs.SimpleAlertDialog;

public class ForgotPassword extends AppCompatActivity {

    private TextView txt_email;
    private Button btn_sendRecoveryLink, btn_back;
    private ConstraintLayout cl_progress, cl_main;
    private FogotPasswordViewModel fogotPasswordViewModel;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        context = this;
        fogotPasswordViewModel = new ViewModelProvider(this).get(FogotPasswordViewModel.class);

        txt_email = findViewById(R.id.txt_email_passwordrecovery);
        btn_sendRecoveryLink = findViewById(R.id.btn_send_link_passwordrecovery);
        btn_back = findViewById(R.id.btn_back_passwordrecovery);
        cl_progress = findViewById(R.id.cl_loading_signin);
        cl_main = findViewById(R.id.cl_main_passwordrecovery);

        cl_progress.setVisibility(View.INVISIBLE);

        btn_sendRecoveryLink.setOnClickListener(view -> {
            String email = txt_email.getText().toString();
            if(true){
                cl_progress.setVisibility(View.VISIBLE);
                cl_main.setVisibility(View.INVISIBLE);
                fogotPasswordViewModel.sendResetPasswordLink(email, (keys, values, status, message) -> {
                    cl_progress.setVisibility(View.INVISIBLE);
                    cl_main.setVisibility(View.VISIBLE);
                    if(status){
                        new SimpleAlertDialog(
                                context,
                                "Response",
                                "Password reset link sent to the email !",
                                "Close",
                                "Go Back to Login",
                                () -> {},
                                () -> {
                                    startActivity(new Intent(context, SignIn.class));
                                }
                        ).createDialog().show();
                    }else{
                        new SimpleAlertDialog(
                                context,
                                "Response",
                                "Failed to send reset email !",
                                "Close",
                                "Go Back to Login",
                                () -> {},
                                () -> {
                                    startActivity(new Intent(context, SignIn.class));
                                }
                        ).createDialog().show();
                    }
                });
            }

        });

        btn_back.setOnClickListener(view -> {
            onBackPressed();
        });


    }
}