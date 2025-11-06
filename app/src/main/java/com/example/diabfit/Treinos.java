package com.example.diabfit;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.appbar.MaterialToolbar;

public class Treinos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treinos);


        MaterialToolbar toolbar = findViewById(R.id.top_app_bar_treinos);


        toolbar.setNavigationOnClickListener(view -> {

            finish();
        });
    }
}
