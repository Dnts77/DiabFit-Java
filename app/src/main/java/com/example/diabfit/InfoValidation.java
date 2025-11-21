package com.example.diabfit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.SharedPreferences;


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
        String alturaStr = etAltura.getText().toString().trim();


        if (nivelAcucarStr.isEmpty()) {
            Toast.makeText(this, "Por favor, insira seu nível de açúcar.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (pesoStr.isEmpty()) {

            Toast.makeText(this, "Por favor, insira seu peso.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (alturaStr.isEmpty()) {

            Toast.makeText(this, "Por favor, insira sua altura.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(nivelAcucarStr.length() != 3){
            Toast.makeText(this, "O nível de açúcar não pode exceder 3 dígitos.", Toast.LENGTH_LONG).show();
            return;
        }
        if(pesoStr.length() != 4){
            Toast.makeText(this, "O peso não pode exceder 3 dígitos.", Toast.LENGTH_LONG).show();//O peso tem um parâmetro diferente para caso o usuário insira valor decimal, o "." conta como um dígito.
            return;
        }
        if(alturaStr.length() != 3){
            Toast.makeText(this, "A altura não pode exceder 3 dígitos.", Toast.LENGTH_LONG).show();
            return;
        }


        try {
            int nivelAcucar = Integer.parseInt(nivelAcucarStr);
            float peso = Float.parseFloat(pesoStr);
            float altura = Float.parseFloat(alturaStr);




            SharedPreferences prefs = getSharedPreferences("DiabFitPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();


            editor.putInt("NIVEL_ACUCAR", nivelAcucar);
            editor.putFloat("PESO", peso);
            editor.putFloat("ALTURA", altura);
            editor.putBoolean("isSetupComplete", true);
            editor.apply();


            Intent intent = new Intent(InfoValidation.this, Home.class);
            startActivity(intent);


            finish();



        } catch (NumberFormatException e) {

            Toast.makeText(this, "Por favor, insira números válidos nos campos.", Toast.LENGTH_SHORT).show();
        }
    }
}



