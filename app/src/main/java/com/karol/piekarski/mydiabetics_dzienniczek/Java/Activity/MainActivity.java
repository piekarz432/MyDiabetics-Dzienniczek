package com.karol.piekarski.mydiabetics_dzienniczek.Java.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.karol.piekarski.mydiabetics_dzienniczek.Java.Class.ApplicationStorage;
import com.karol.piekarski.mydiabetics_dzienniczek.R;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private TextView userData;
    private TextView userEmail;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private String userId;
    private ApplicationStorage applicationStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        applicationStorage = ApplicationStorage.getInstance();

        drawerLayout=findViewById(R.id.drawerLayout);

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


        final TextView textTitle = findViewById(R.id.textTitle);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @androidx.annotation.Nullable Bundle arguments) {

                Integer id = navController.getCurrentDestination().getId();

                if(id==R.id.noteDetails)
                {
                    textTitle.setText(applicationStorage.getNoteViewHolder().getNoteTitle());

                }else
                {
                    textTitle.setText(navController.getCurrentDestination().getLabel());
                }


            }
        });

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
    }


}

