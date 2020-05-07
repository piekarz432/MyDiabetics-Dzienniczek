package com.karol.piekarski.mydiabetics_dzienniczek.Java.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.karol.piekarski.mydiabetics_dzienniczek.Java.Class.Repository;
import com.karol.piekarski.mydiabetics_dzienniczek.Java.Interfaces.Validate;
import com.karol.piekarski.mydiabetics_dzienniczek.R;

public class ForgotPassword extends AppCompatActivity implements Validate {

    private EditText login;
    private EditText newPassword;
    private EditText repeatPassword;
    private Button changePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        login=findViewById(R.id.name);
        newPassword =findViewById(R.id.password);
        repeatPassword=findViewById(R.id.repeatPassword);
        changePassword=findViewById(R.id.changePassword);

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
            }
        });
    }

    @Override
    public boolean validate() {

        if(Repository.user!=null) {

            if (login.getText().toString().equals(Repository.user.getUsername()) && newPassword.getText().toString().equals(repeatPassword.getText().toString())) {
                return true;
            }
            if (login.getText().toString().isEmpty() || !login.getText().toString().equals(Repository.user.getUsername())) {
                login.setError("Nazwa użytkownika jest niepoprawna.");
                return false;
            }

            if (!newPassword.getText().equals(" ") && !repeatPassword.getText().equals(" ")
                    && newPassword.getText().toString().equals(repeatPassword.getText().toString())) {

                newPassword.setError("Hasła muszą być takie same.");
                repeatPassword.setError("Hasła muszą być takie same.");
                return false;
            }
        }else
        {
            Toast.makeText(this,"Proszę utworzyć konto", Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    private void check()
    {
        if(validate())
        {
            Repository.user.setPassword(newPassword.getText().toString());
            Toast.makeText(getApplicationContext(), "Hasło zostało zmienione pomyślnie.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
