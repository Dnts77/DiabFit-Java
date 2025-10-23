package com.example.diabfit;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText txtEmail, txtSenha;
    Button btCadastro, btEntrar;
    private UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btCadastro = findViewById(R.id.btCadastro);
        btEntrar = findViewById(R.id.btEntrar);
        txtEmail = findViewById(R.id.txtEmail);
        txtSenha = findViewById(R.id.txtSenha);

        btCadastro.setOnClickListener(this);
        btEntrar.setOnClickListener(this);


        userDAO = new UserDAO(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btCadastro) {
            Intent intent = new Intent(MainActivity.this, Cadastro.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btEntrar) {
            tentarLogin();
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


        userDAO.open();
        boolean loginValido = userDAO.checarUsuario(email, senha);
        userDAO.close();


        if (loginValido) {
            Toast.makeText(this, "Login efetuado com sucesso!", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "E-mail ou senha incorretos.", Toast.LENGTH_LONG).show();
            txtSenha.setError("Credenciais inválidas");
            txtSenha.requestFocus();
        }
    }
}
