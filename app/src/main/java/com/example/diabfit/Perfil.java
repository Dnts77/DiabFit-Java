package com.example.diabfit;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

public class Perfil extends AppCompatActivity {


    private TextView textViewNome, textViewEmail, textViewPeso, textViewAltura;

    private Button buttonExcluirConta;

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
        buttonExcluirConta = findViewById(R.id.buttonExcluirConta);

        userDAO = new UserDAO(this);
        mAuth = FirebaseAuth.getInstance();

        buttonExcluirConta.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Excluir Conta")
                    .setMessage("Você tem certeza que deseja excluir sua conta? Esta ação não pode ser desfeita.")
                    .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            excluirConta();
                        }
                    })
                    .setNegativeButton("Não", null)
                    .show();
        });


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
        String userId = firebaseUser.getUid();
        textViewEmail.setText(email != null ? email : "E-mail não disponível");


        userDAO.open();
        String nome = userDAO.getNomePorId(userId);
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
            textViewAltura.setText(String.format(Locale.getDefault(), "%.0f cm", alturaEmMetros));
        } else {
            textViewAltura.setText("Não informada");
        }
    }

    private void excluirConta() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            user.delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Perfil.this, "Conta excluída com sucesso.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Perfil.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(Perfil.this, "Falha ao excluir a conta.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
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
