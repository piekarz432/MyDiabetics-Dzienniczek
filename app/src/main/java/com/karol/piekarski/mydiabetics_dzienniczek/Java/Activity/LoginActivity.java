package com.karol.piekarski.mydiabetics_dzienniczek.Java.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.karol.piekarski.mydiabetics_dzienniczek.Java.Class.Repository;
import com.karol.piekarski.mydiabetics_dzienniczek.Java.Interfaces.Validate;
import com.karol.piekarski.mydiabetics_dzienniczek.R;

public class LoginActivity extends AppCompatActivity implements Validate {

    private EditText login;
    private EditText password;
    private TextView signUp;
    private Button btnLogin;
    private TextView forgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login=findViewById(R.id.name);
        password=findViewById(R.id.password);
        signUp=findViewById(R.id.signUp);
        btnLogin=findViewById(R.id.changePassword);
        forgotPassword=findViewById(R.id.forgotPassword);


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this.getApplicationContext(), SignUpActivity.class);
                startActivity(intent);

            }
        });



        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkData();
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this.getApplicationContext(), ForgotPassword.class);
                startActivity(intent);
            }
        });

    }


    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public boolean validate() {

        if (Repository.user != null) {

            if (login.getText().toString().equals(Repository.user.getUsername()) && password.getText().toString().equals(Repository.user.getPassword())) {
                return true;
            }

            if (!login.getText().toString().equals(Repository.user.getUsername()) || login.getText().toString().equals(" ")) {
                login.setError("Login jest nieprawidłowy");
                return false;
            }

            if (!password.getText().toString().equals(Repository.user.getPassword()) || password.getText().toString().equals(" ")) {
                password.setError("Hasło jest nieprawidłowe");
                return false;
            }

        }
        return false;
    }

    private void checkData()
    {
        if(validate())
        {
            if(Repository.user.getGender().equals("Kobieta"))
            {
                Toast.makeText(getApplicationContext(), "Zostałaś zalogowana pomyślnie", Toast.LENGTH_SHORT).show();
                return;
            }

            if(Repository.user.getGender().equals("Mężczyzna"))
            {
                Toast.makeText(getApplicationContext(), "Zostałaś zalogowany pomyślnie", Toast.LENGTH_SHORT).show();
                return;
            }

        }
    }

}
