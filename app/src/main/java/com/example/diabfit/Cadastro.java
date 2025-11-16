package com.example.diabfit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Cadastro extends AppCompatActivity implements View.OnClickListener {

    Button btCADCadastro, btVoltar;
    EditText txtCADNome, txtCADEmail, txtCADSenha, txtCADCONFSenha;
    private UserDAO userDAO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cadastro);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Id's da página de cadastro
        btCADCadastro = findViewById(R.id.btCADCadastro);
        btVoltar = findViewById(R.id.BtVoltar);
        txtCADCONFSenha = findViewById(R.id.txtCADCONFSenha);
        txtCADEmail = findViewById(R.id.txtCADEmail);
        txtCADNome = findViewById(R.id.txtCADNome);
        txtCADSenha = findViewById(R.id.txtCADSenha);

        btVoltar.setOnClickListener(this);
        btCADCadastro.setOnClickListener(this);

        userDAO = new UserDAO(this);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.BtVoltar) {


            Intent intent = new Intent(Cadastro.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else if (v.getId() == R.id.btCADCadastro) {
            Cadastrar();
        }
    }

    public void Cadastrar() {
        String nome = txtCADNome.getText().toString().trim();
        String email = txtCADEmail.getText().toString().trim();
        String senha = txtCADSenha.getText().toString().trim();
        String confSenha = txtCADCONFSenha.getText().toString().trim();

        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!senha.equals(confSenha)){
            Toast.makeText(this, "As senhas não coincidem", Toast.LENGTH_SHORT).show();
            return;
        }

        userDAO.open();
        int cadastroRealizado = userDAO.inserir(nome, email, senha);
        userDAO.close();


        switch(cadastroRealizado){
            case 1: //Deu certo
                Toast.makeText(this, "Cadastro realizado com êxito!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Cadastro.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                break;

            case 0: //O Email já existe
                Toast.makeText(this, "Erro ao realizar o cadastro. O email já pode estar em uso.", Toast.LENGTH_LONG).show();
                txtCADEmail.setError("Email já cadastrado");
                break;

            case -1: //deu ruim
                default:
                    Toast.makeText(this, "Erro ao realizar o cadastro. Tente novamente.", Toast.LENGTH_LONG).show();
                    break;




        }
    }
}
