package com.example.diabfit;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

public class Perfil extends AppCompatActivity {

    // --- Componentes da UI ---
    private TextView textViewNome, textViewEmail, textViewPeso, textViewAltura;

    // --- Fontes de Dados ---
    private UserDAO userDAO;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_perfil);


        MaterialToolbar toolbar = findViewById(R.id.top_app_bar_perfil);
        setSupportActionBar(toolbar);


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Meu Perfil");
        }


        textViewNome = findViewById(R.id.textViewNome);
        textViewEmail = findViewById(R.id.textViewEmail);
        textViewPeso = findViewById(R.id.textViewPeso);
        textViewAltura = findViewById(R.id.textViewAltura);

        userDAO = new UserDAO(this);
        mAuth = FirebaseAuth.getInstance();


        loadUserProfile();
    }

    private void loadUserProfile() {

        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        if (firebaseUser == null) {
            Toast.makeText(this, "Usuário não encontrado.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }


        String email = firebaseUser.getEmail();
        textViewEmail.setText(email != null ? email : "E-mail não disponível");


        userDAO.open();
        String nome = userDAO.getNomePorId(firebaseUser.getUid());
        userDAO.close();
        textViewNome.setText(nome != null && !nome.isEmpty() ? nome : "Nome não informado");


        SharedPreferences prefs = getSharedPreferences("DiabFitPrefs", Context.MODE_PRIVATE);
        float peso = prefs.getFloat("PESO", 0f);
        float alturaEmMetros = prefs.getFloat("ALTURA", 0f);


        if (peso > 0) {
            textViewPeso.setText(String.format(Locale.US, "%.1f kg", peso));
        } else {
            textViewPeso.setText("Não informado");
        }

        if (alturaEmMetros > 0) {
            textViewAltura.setText(String.format(Locale.getDefault(), "%.1f", alturaEmMetros));
        } else {
            textViewAltura.setText("Não informada");
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
