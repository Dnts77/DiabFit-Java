package com.example.diabfit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText txtNome, txtEmail, txtSenha;
    Button btCadastro, btEntrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btCadastro = findViewById(R.id.btCadastro);
        btEntrar = findViewById(R.id.btEntrar);
        txtEmail = findViewById(R.id.txtEmail);
        txtNome = findViewById(R.id.txtNome);
        txtSenha = findViewById(R.id.txtSenha);

        btCadastro.setOnClickListener(this);
        btEntrar.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btCadastro) {
            Intent intent = new Intent(MainActivity.this, Cadastro.class);
            startActivity(intent);
        }

    }
}