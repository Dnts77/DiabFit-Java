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
    Button btCadastro, btEntrar, btFGSenha;
    private UserDAO userDAO;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btCadastro = findViewById(R.id.btCadastro);
        btEntrar = findViewById(R.id.btEntrar);
        txtEmail = findViewById(R.id.txtEmail);
        txtSenha = findViewById(R.id.txtSenha);
        btFGSenha = findViewById(R.id.btFGSenha);

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


        userDAO.open();
        boolean loginValido = userDAO.checarUsuario(email, senha);
        userDAO.close();


        if (loginValido) {
            Toast.makeText(this, "Login efetuado com sucesso!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, InfoValidation.class);
            startActivity(intent);

        } else {
            Toast.makeText(this, "E-mail ou senha incorretos.", Toast.LENGTH_LONG).show();
            txtSenha.setError("Credenciais inválidas");
            txtSenha.requestFocus();
        }
    }
    public void esqueceuSenha(){
       String email = txtEmail.getText().toString().trim();

       if(TextUtils.isEmpty(email)){
           txtEmail.setError("Digite seu email para recuperar a senha");
           txtEmail.requestFocus();
           return;
       }
       userDAO.open();
       String senha = userDAO.getSenhaPorEmail(email);
       userDAO.close();

       if (senha != null && !senha.isEmpty()){
           String assunto = "Recuperação de Senha - Equipe DiabFit";
           String msg = "Olá, você solicitou a recuperação de senha. \n\nSua senha é:" + "\n\n" +senha + "\n\nAtenciosamente, \n\nEquipe DiabFit!";

           EmailManager.sendEmailInBackground(email, assunto, msg);
           Toast.makeText(this, "E-mail de recuperação enviado com sucesso!", Toast.LENGTH_SHORT).show();
       } else{
           Toast.makeText(this, "E-mail não encontrado", Toast.LENGTH_SHORT).show();

       }
    }
}
