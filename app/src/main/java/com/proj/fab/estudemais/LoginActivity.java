package com.proj.fab.estudemais;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    private EditText email, pass;
    private Button loginB;
    private TextView forgotPassB, signupB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        pass=findViewById(R.id.password);
        loginB=findViewById(R.id.loginB);
        forgotPassB=findViewById(R.id.forgot_password);
        signupB=findViewById(R.id.signupB);

        loginB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateData()){
                    login();
                }
            }
        });

    }
    private boolean validateData()
    {
       
        if (email.getText().toString().isEmpty()){
            email.setError("Preencha o e-mail");
            return false;
        }
        if (pass.getText().toString().isEmpty()){
            pass.setError("Preencha a senha");
            return false;
        }

        return true;
    }
    private void login()
    {

    }

}