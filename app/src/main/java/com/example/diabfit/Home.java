package com.example.diabfit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

public class Home extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        MaterialToolbar toolbar = findViewById(R.id.top_app_bar);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        navigationView.setNavigationItemSelectedListener(menuItem -> {

            drawerLayout.closeDrawer(GravityCompat.START);


            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                int itemId = menuItem.getItemId();

                if (itemId == R.id.nav_diets) {
                    Intent intent = new Intent(Home.this, Dietas.class);
                    startActivity(intent);
                }
                else if(itemId == R.id.nav_workouts){

                    SharedPreferences prefs = getSharedPreferences("DiabFitPrefs", Context.MODE_PRIVATE);

                    float peso = prefs.getFloat("PESO", 0f);
                    float altura = prefs.getFloat("ALTURA", 0f);

                    if(peso > 0 && altura > 0){
                        float alturaMetros = altura / 100.0f;

                        float imc = peso / (alturaMetros * alturaMetros);

                        Intent intent = new Intent(Home.this, Treinos.class);
                        intent.putExtra("USER_IMC", imc);
                        startActivity(intent);

                    }
                    else{
                        Toast.makeText(this, "Por favor, insira seu peso e altura.", Toast.LENGTH_SHORT).show();
                    }
                }
                else if(itemId == R.id.nav_infos){
                    Intent intent = new Intent(Home.this, InfoValidation.class);
                    startActivity(intent);
                }
                else if(itemId == R.id.nav_sair){
                    Intent intent = new Intent(Home.this, MainActivity.class);
                    startActivity(intent);
                }
                else if(itemId == R.id.nav_perfil){
                    Intent intent = new Intent(Home.this, Perfil.class);
                    startActivity(intent);
                }

            }, 250);

            drawerLayout.closeDrawer(GravityCompat.START);

            return true;
        });




        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    setEnabled(false);
                    getOnBackPressedDispatcher().onBackPressed();
                }
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }
}
