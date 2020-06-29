package com.karol.piekarski.mydiabetics_dzienniczek.Java.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.karol.piekarski.mydiabetics_dzienniczek.R;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private Button logOut;
    private TextView userData;
    private TextView userEmail;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout=findViewById(R.id.drawerLayout);
        logOut=findViewById(R.id.logOut);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();

        userId = firebaseAuth.getCurrentUser().getUid();

        NavigationView navigationView = findViewById(R.id.navigationView);
        View headerView = navigationView.inflateHeaderView(R.layout.navigation_header);
        userData=headerView.findViewById(R.id.userData);
        userEmail=headerView.findViewById(R.id.userEmail);
        navigationView.setItemIconTintList(null);


        NavController navController = Navigation.findNavController(this, R.id.navHostFragment);
        NavigationUI.setupWithNavController(navigationView,navController);

        DocumentReference documentReference = firebaseFirestore.collection("Users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(firebaseAuth.getCurrentUser() != null) {
                     userData.setText(documentSnapshot.getString("Name") + " " + documentSnapshot.getString("Surname"));
                     userEmail.setText(documentSnapshot.getString("Email"));
                }
            }
        });

      findViewById(R.id.imageMenu).setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              drawerLayout.openDrawer(GravityCompat.START);
          }
      });

      logOut.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              FirebaseAuth.getInstance().signOut();
              Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
              startActivity(intent);
              finish();
          }
      });
    }
}

