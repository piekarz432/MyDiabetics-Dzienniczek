package com.karol.piekarski.mydiabetics_dzienniczek.Java.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.karol.piekarski.mydiabetics_dzienniczek.R;

public class ForgotPassword extends AppCompatActivity {

    private EditText email;
    private Button changePassword;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        email=findViewById(R.id.email);
        changePassword=findViewById(R.id.changePassword);
        firebaseAuth=FirebaseAuth.getInstance();

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
            }
        });
    }
    private void check()
    {
        firebaseAuth.sendPasswordResetEmail(email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(getApplicationContext(), "Link do zmiany hasła został wysłana na podany adres email.", Toast.LENGTH_SHORT).show();
                    finish();
                }else
                {
                    Toast.makeText(getApplicationContext(), "Konto o podanym adresie email nie istnieje.", Toast.LENGTH_SHORT).show();
                    email.setError("Konto nie istnieje.");
                }
            }
        });

    }
}
