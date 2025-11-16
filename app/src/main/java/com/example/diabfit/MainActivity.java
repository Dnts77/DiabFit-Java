package com.example.diabfit;

// 1. ADICIONAR IMPORTAÇÕES NECESSÁRIAS
import android.content.Context;
import android.content.SharedPreferences;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText txtEmail, txtSenha;
    Button btCadastro, btEntrar, btFGSenha;
    private UserDAO userDAO;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btCadastro = findViewById(R.id.btCadastro);
        btEntrar = findViewById(R.id.btEntrar);
        txtEmail = findViewById(R.id.txtEmail);
        txtSenha = findViewById(R.id.txtSenha);
        btFGSenha = findViewById(R.id.btFGSenha);

        mAuth = FirebaseAuth.getInstance();

        btCadastro.setOnClickListener(this);
        btEntrar.setOnClickListener(this);
        btFGSenha.setOnClickListener(this);

        userDAO = new UserDAO(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btCadastro) {
            Intent intent = new Intent(MainActivity.this, Cadastro.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btEntrar) {
            tentarLogin();
        } else if(v.getId()==R.id.btFGSenha){
            esqueceuSenha();
        }
    }

    private void tentarLogin() {
        String email = txtEmail.getText().toString().trim();
        String senha = txtSenha.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            txtEmail.setError("Este campo é obrigatório");
            txtEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(senha)) {
            txtSenha.setError("Este campo é obrigatório");
            txtSenha.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(this, task -> {
            if(task.isSuccessful()){
                Toast.makeText(this, "Login realizado com sucesso", Toast.LENGTH_SHORT).show();

                SharedPreferences prefs = getSharedPreferences("DiabFitPrefs", Context.MODE_PRIVATE);
                boolean isSetupComplete = prefs.getBoolean("isSetupComplete", false);

                Intent intent;
                if(isSetupComplete) {
                    intent = new Intent(MainActivity.this, Home.class);
                } else{
                    intent = new Intent(MainActivity.this, InfoValidation.class);
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

            } else {
                Toast.makeText(this, "Algo deu errado", Toast.LENGTH_SHORT).show();


            }
        });


    }

    public void esqueceuSenha() {
        String email = txtEmail.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            txtEmail.setError("Digite seu email para recuperar a senha");
            txtEmail.requestFocus();
            return;
        }

        FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(MainActivity.this, "E-mail enviado com sucesso para" + email, Toast.LENGTH_LONG).show();
            } else{
                Toast.makeText(MainActivity.this, "Erro ao enviar e-mail", Toast.LENGTH_LONG).show();
            }
        });

    };
}