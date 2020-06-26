package com.karol.piekarski.mydiabetics_dzienniczek.Java.Activity;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.karol.piekarski.mydiabetics_dzienniczek.Java.Class.Repository;
import com.karol.piekarski.mydiabetics_dzienniczek.Java.Class.User;
import com.karol.piekarski.mydiabetics_dzienniczek.Java.Interfaces.Validate;
import com.karol.piekarski.mydiabetics_dzienniczek.R;

public class SignUpActivity extends AppCompatActivity implements Validate {

    private EditText name;
    private EditText surname;
    private EditText username;
    private EditText email;
    private EditText password;
    private Button signUp;
    private RadioGroup gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        name = findViewById(R.id.name);
        surname = findViewById(R.id.surname);
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        signUp=findViewById(R.id.signUp);
        gender = findViewById(R.id.gender);


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkData();
            }
        });
    }


    public boolean validate()
    {
        if(name.getText().toString().isEmpty() || surname.getText().toString().isEmpty()
                || username.getText().toString().isEmpty() || email.getText().toString().isEmpty()
                || password.getText().toString().isEmpty() || gender.getCheckedRadioButtonId()== -1)
        {
            Toast.makeText(getApplicationContext(),"Uzupelnij wszystkie pola.", Toast.LENGTH_LONG).show();
            return false;
        }

        if(username.getText().toString().length()<3)
        {
            username.setError("Nazwa musi być dłuższa.");
            return false;
        }

        if(password.getText().toString().length()<8)
        {
           password.setError("Hasło mus zawierać co najmniej 8 znaków." );
            return false;
        }

        return true;
    }

    private void checkData()
    {
        if(validate())
        {
            Repository.user =new User(username.getText().toString(),email.getText().toString(),name.getText().toString(),
                                      surname.getText().toString(), password.getText().toString(), checkGender());
            Toast.makeText(getApplicationContext(),"Konto zostalo utworzone.", Toast.LENGTH_SHORT).show();

            finish();

        }
    }

    private String checkGender()
    {
        int radioId = gender.getCheckedRadioButtonId();

        if(radioId==R.id.female)
        {
            return "Kobieta";
        }

        return "Mężczyzna";

    }


}
