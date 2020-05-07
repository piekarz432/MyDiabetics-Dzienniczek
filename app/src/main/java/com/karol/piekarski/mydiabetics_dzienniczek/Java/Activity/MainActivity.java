package com.karol.piekarski.mydiabetics_dzienniczek.Java.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.karol.piekarski.mydiabetics_dzienniczek.R;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private TextView userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        drawerLayout=findViewById(R.id.drawerLayout);

        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);
        View header = navigationView.getHeaderView(0);

        userData=header.findViewById(R.id.userData);
        userData.setText("aaa");

        navigationView.setItemIconTintList(null);


      findViewById(R.id.imageMenu).setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
               drawerLayout.openDrawer(GravityCompat.START);
          }
      });


        NavController navController = Navigation.findNavController(this, R.id.navHostFragment);
        NavigationUI.setupWithNavController(navigationView,navController);

        userData.setText("aaa");

    }

}

