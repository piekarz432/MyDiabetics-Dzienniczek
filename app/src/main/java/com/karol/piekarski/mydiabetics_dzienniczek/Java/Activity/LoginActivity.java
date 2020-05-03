package com.karol.piekarski.mydiabetics_dzienniczek.Java.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;
import com.karol.piekarski.mydiabetics_dzienniczek.Java.Class.Repository;
import com.karol.piekarski.mydiabetics_dzienniczek.Java.Class.User;
import com.karol.piekarski.mydiabetics_dzienniczek.Java.Interfaces.Validate;
import com.karol.piekarski.mydiabetics_dzienniczek.R;

public class LoginActivity extends AppCompatActivity implements Validate{


    static final int RC_SIGN_IN = 1;

    private EditText login;
    private EditText password;
    private TextView signUp;
    private Button btnLogin;
    private TextView forgotPassword;
    private ImageView google;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login=findViewById(R.id.name);
        password=findViewById(R.id.password);
        signUp=findViewById(R.id.signUp);
        btnLogin=findViewById(R.id.changePassword);
        forgotPassword=findViewById(R.id.forgotPassword);
        google=findViewById(R.id.googleLogin);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

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

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

    }


    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            //Repository.user=new User(account.getEmail(), account.getDisplayName());


            // Signed in successfully, show authenticated UI.
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Error", "signInResult:failed code=" + e.getStatusCode());
        }
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

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }



}
