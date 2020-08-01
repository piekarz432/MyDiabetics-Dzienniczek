package com.karol.piekarski.mydiabetics_dzienniczek.Java.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.karol.piekarski.mydiabetics_dzienniczek.Java.Interfaces.Validate;
import com.karol.piekarski.mydiabetics_dzienniczek.R;

import java.util.HashMap;
import java.util.Map;


public class SignUpActivity extends AppCompatActivity implements Validate {

    private EditText name;
    private EditText surname;
    private EditText email;
    private EditText password;
    private Button signUp;
    private RadioGroup gender;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private String userId;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        name = findViewById(R.id.name);
        surname = findViewById(R.id.surname);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        signUp=findViewById(R.id.signUp);
        gender = findViewById(R.id.gender);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        progressBar=findViewById(R.id.progressBarSignUp);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkData();
            }
        });

    }

    public boolean validate() {
        if(name.getText().toString().isEmpty() || surname.getText().toString().isEmpty()
                || email.getText().toString().isEmpty()
                || password.getText().toString().isEmpty() || gender.getCheckedRadioButtonId()== -1)
        {
            Toast.makeText(getApplicationContext(),"Uzupelnij wszystkie pola.", Toast.LENGTH_LONG).show();
            return false;
        }

        if(password.getText().toString().length()<8)
        {
            password.setError("Hasło mus zawierać co najmniej 8 znaków." );
            return false;
        }

        return true;
    }

    private void checkData() {
        if(validate())
        {
            signUp.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            firebaseAuth.createUserWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        addUserToDatabase();
                    }else {
                        String error = ((FirebaseAuthException) task.getException()).getErrorCode();
                        errorCode(error);
                        signUp.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }
            });
        }
    }

    private String checkGender() {
        int radioId = gender.getCheckedRadioButtonId();

        if(radioId==R.id.female)
        {
            return "Kobieta";
        }
        return "Mężczyzna";

    }

    private void errorCode(String error) {
        switch (error)
        {
            case "ERROR_INVALID_EMAIL":
                Toast.makeText(getApplicationContext(), "Format adresu email jest niepoprawny." , Toast.LENGTH_SHORT).show();
                email.setError("Format adresu email jest niepoprawny.");
                break;

            case "ERROR_EMAIL_ALREADY_IN_USE":
                Toast.makeText(getApplicationContext(), "Użytkownik o takim adresie email już istnieje." , Toast.LENGTH_SHORT).show();
                break;

        }
    }

    private void addUserToDatabase() {
        Toast.makeText(getApplicationContext(), "Konto zostało utworzone.", Toast.LENGTH_SHORT).show();
        userId=firebaseAuth.getCurrentUser().getUid();
        DocumentReference documentReference = firebaseFirestore.collection("Users").document(userId);
        Map<String, Object> user = new HashMap<>();
        user.put("Name", name.getText().toString().trim());
        user.put("Surname", surname.getText().toString().trim());
        user.put("Email", email.getText().toString().trim());
        user.put("Gender", checkGender());
        documentReference.set(user).addOnSuccessListener(aVoid -> Log.d("Zapisd o bazy", "Dodano uzytkownika dla id " + userId
        )).addOnFailureListener(e -> {
            Log.d("Zapis do bazy", "Blad " + e.toString());
        });
        finish();
    }

}
