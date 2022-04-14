package com.proj.fab.estudemais;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class RecuperarSenhaActivity extends AppCompatActivity {

    EditText editTextEmail;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    Button recuperarB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_senha);

        editTextEmail=findViewById(R.id.email_recuperar);
        mAuth=FirebaseAuth.getInstance();
        progressBar=findViewById(R.id.progressBar_rec);
        recuperarB=findViewById(R.id.recuperarB);

        recuperarB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recuperarSenha();
            }
        });

    }


    public void recuperarSenha()
    {
        resetSenha();

    }
    private void resetSenha()
    {
        String txtEmail=editTextEmail.getText().toString().trim();

        if (! Patterns.EMAIL_ADDRESS.matcher(txtEmail).matches())
        {
            editTextEmail.setError("Utilize um e-mail valido.");
            editTextEmail.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.sendPasswordResetEmail(txtEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(RecuperarSenhaActivity.this, "E-mail para recuperação de senha enviado.",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RecuperarSenhaActivity.this,LoginActivity.class);
                    startActivity(intent);
                    progressBar.setVisibility(View.GONE);
                }
                else
                {
                    Toast.makeText(RecuperarSenhaActivity.this, "Erro ao recuperar, tente novamente.",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }

            }
        });
    }
}