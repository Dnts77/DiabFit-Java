package com.example.diabfit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class LauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        SharedPreferences prefs = getSharedPreferences("DiabFitPrefs", Context.MODE_PRIVATE);


        boolean isSetupComplete = prefs.getBoolean("isSetupComplete", false);

        Intent intent;
        if (isSetupComplete) {

            intent = new Intent(this, Home.class);
        } else {

            intent = new Intent(this, InfoValidation.class);
        }

        startActivity(intent);
        finish();
    }
}
