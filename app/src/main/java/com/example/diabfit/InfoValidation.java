package com.example.diabfit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class InfoValidation extends AppCompatActivity {


    private EditText etSugarLevel;
    private EditText etPeso;
    private EditText etAltura;
    private Button btAvancar;
    private Button btVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infovalidation);


        initComponents();
        setupListeners();
    }


    private void initComponents() {

        etSugarLevel = findViewById(R.id.SugarLevel);
        etPeso = findViewById(R.id.Peso);
        etAltura = findViewById(R.id.Altura);
        btAvancar = findViewById(R.id.btAvancar);
        btVoltar = findViewById(R.id.btVoltar);
    }


    private void setupListeners() {

        btVoltar.setOnClickListener(v -> {

            finish();
        });


        btAvancar.setOnClickListener(v -> {

            validateAndProceed();
        });
    }


    private void validateAndProceed() {

        String nivelAcucarStr = etSugarLevel.getText().toString().trim();
        String pesoStr = etPeso.getText().toString().trim();
        String alturaStr = etAltura.getText().toString().trim()
;

        if (nivelAcucarStr.isEmpty()) {
            Toast.makeText(this, "Por favor, insira seu nível de açúcar.", Toast.LENGTH_SHORT).show();
            return; // Interrompe a execução do método
        }
        if(pesoStr.isEmpty()){
            Toast.makeText(this, "Por favor, insira sua altura.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(alturaStr.isEmpty()){
            Toast.makeText(this, "Por favor, insira seu peso.", Toast.LENGTH_SHORT).show();
            return;
        }

        try {

            int nivelAcucar = Integer.parseInt(nivelAcucarStr);
            int peso = Integer.parseInt(pesoStr);
            int altura = Integer.parseInt(alturaStr);


            Intent intent = new Intent(InfoValidation.this, Dietas.class);


            intent.putExtra("NIVEL_ACUCAR", nivelAcucar);
            intent.putExtra("PESO", peso);
            intent.putExtra("ALTURA", altura);


            startActivity(intent);

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Por favor, insira um número válido para o nível de açúcar.", Toast.LENGTH_SHORT).show();
        }
    }
}
