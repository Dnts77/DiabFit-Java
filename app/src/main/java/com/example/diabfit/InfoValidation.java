package com.example.diabfit;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class InfoValidation extends AppCompatActivity implements View.OnClickListener {

    Button btInserir, btVoltar;
    EditText SugarLevel, Peso, Altura;

    private UserDAO userDAO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_infovalidation);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btInserir = findViewById(R.id.btInserir);
        SugarLevel = findViewById(R.id.SugarLevel);
        Peso = findViewById(R.id.Peso);
        Altura = findViewById(R.id.Altura);
        btVoltar = findViewById(R.id.btVoltar);

        btInserir.setOnClickListener(this);
        btVoltar.setOnClickListener(this);

        userDAO = new UserDAO(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btInserir) {
            Inserir();
        }
        if(v.getId() == R.id.btVoltar){
            Intent intent = new Intent(InfoValidation.this, MainActivity.class);
            startActivity(intent);
        }
    }

    public void Inserir() {
        String sugarLevel = SugarLevel.getText().toString().trim();
        String peso = Peso.getText().toString().trim();
        String altura = Altura.getText().toString().trim();

        if (TextUtils.isEmpty(sugarLevel)) {
            SugarLevel.setError("Este campo é obrigatório");
            SugarLevel.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(peso)) {
            Peso.setError("Este campo é obrigatório");
            Peso.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(altura)) {
            Altura.setError("Este campo é obrigatório");
            Altura.requestFocus();
            return;

        }

        userDAO.open();
        boolean Inserido = userDAO.infos(sugarLevel, peso, altura);
        userDAO.close();

        if(Inserido){
            Toast.makeText(this, "Informações Inseridas com sucesso", Toast.LENGTH_SHORT).show();

        }
        else{
            Toast.makeText(this, "Erro ao inserir informações", Toast.LENGTH_SHORT).show();
        }
    }
}