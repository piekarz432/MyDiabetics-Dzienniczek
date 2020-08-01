package com.karol.piekarski.mydiabetics_dzienniczek.Java.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.karol.piekarski.mydiabetics_dzienniczek.Java.Class.ApplicationStorage;
import com.karol.piekarski.mydiabetics_dzienniczek.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class LoginActivity extends AppCompatActivity{

    static final int RC_SIGN_IN = 1;

    private EditText email;
    private EditText password;
    private TextView signUp;
    private Button btnLogin;
    private TextView forgotPassword;
    private ImageView google;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private ProgressBar progressBar;
    private String userId;
    private ApplicationStorage applicationStorage;

    @Override
    public void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
        if(firebaseAuth.getCurrentUser() != null)
        {
            loadMainActivity();
        }

        applicationStorage = ApplicationStorage.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email =findViewById(R.id.email);
        password=findViewById(R.id.password);
        signUp=findViewById(R.id.signUp);
        btnLogin=findViewById(R.id.changePassword);
        forgotPassword=findViewById(R.id.forgotPassword);
        google=findViewById(R.id.googleLogin);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        progressBar=findViewById(R.id.progressBarLogin);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
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

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText resetEmail = new EditText(v.getContext());
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Resetowanie Hasła");
                passwordResetDialog.setMessage("Wprowadź adres email twojego konta na który wyślemy link do zamiany hasła.");
                passwordResetDialog.setView(resetEmail);

                passwordResetDialog.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        firebaseAuth.fetchSignInMethodsForEmail(resetEmail.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                            @Override
                            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                List<String> isNewUser = task.getResult().getSignInMethods();
                                for(String n : isNewUser)
                                {
                                    if(!n.equals("google.com"))
                                    {
                                        firebaseAuth.sendPasswordResetEmail(resetEmail.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(getApplicationContext(), "Link do zmiany hasła został wysłana na podany adres email.", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(getApplicationContext(), "Konto o podanym adresie email nie istnieje.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }else {
                                        Toast.makeText(getApplicationContext(), "Nie można zmenić hasła do konta Google.", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            }
                        });
                    }
                });

                passwordResetDialog.setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                    passwordResetDialog.create().show();
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
            Log.d("Logowanie przez Googla", "firebaseAuthWithGoogle:" + account.getId());
            firebaseAuthWithGoogle(account.getIdToken());
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Logowanie przez Googla", "Google sign in failed", e);
        }
    }

    public void checkData() {

        if(email.getText().toString().isEmpty())
        {
            email.setError("Prosze wpisac nazwę użytkownika.");
            return;
        }

        if(password.getText().toString().isEmpty())
        {
            password.setError("Prosze wpisac hasło.");
            return;
        }

        btnLogin.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.signInWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(getApplicationContext(), "Logowanie przebiegło pomyślnie.", Toast.LENGTH_SHORT).show();
                    applicationStorage.setLoggedGoogle(false);
                    loadMainActivity();
                }else
                {
                    String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                    errorCode(errorCode);
                    btnLogin.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Logowanie przez googla", "signInWithCredential:success");
                    userId=firebaseAuth.getCurrentUser().getUid();
                    GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
                    if (acct != null) {
                        DocumentReference documentReference = firebaseFirestore.collection("Users").document(userId);
                        Map<String, Object> user = new HashMap<>();
                        user.put("Name", acct.getGivenName());
                        user.put("Surname", acct.getFamilyName());
                        user.put("Email", acct.getEmail());
                        user.put("Gender", null);
                        documentReference.set(user).addOnSuccessListener(aVoid -> Log.d("Zapis do bazy", "Dodano uzytkownika dla id " + userId
                        )).addOnFailureListener(e -> {
                            Log.d("Zapis do bazy", "Blad " + e.toString());
                        });
                    }
                    applicationStorage.setLoggedGoogle(true);
                    loadMainActivity();
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Logowanie przez googla", "signInWithCredential:failure", task.getException());
                    Toast.makeText(getApplicationContext(), "Bład podczas logowania." + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadMainActivity() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void errorCode(String error) {
        switch (error)
        {
            case "ERROR_INVALID_EMAIL":
                Toast.makeText(getApplicationContext(), "Format adresu email jest niepoprawny." , Toast.LENGTH_SHORT).show();
                email.setError("Format adresu email jest niepoprawny.");
                break;

            case "ERROR_WRONG_PASSWORD":
                Toast.makeText(getApplicationContext(), "Hasło jest niepoprawne." , Toast.LENGTH_SHORT).show();
                password.setError("Hasło jest niepoprawne.");
                break;

            case "ERROR_USER_NOT_FOUND":
                Toast.makeText(getApplicationContext(), "Użytkownik o wprowadzonych danych nie istnieje." , Toast.LENGTH_SHORT).show();
                break;

        }
    }
}

