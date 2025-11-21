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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

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

        String namePattern = "^[\\p{L} .'-]+$"; //Padrão de nome atualizado também
        String passwordPattern = "^(?=.*[0-9]).{6,}$"; //Padrão de senha atualizado hehe

        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!nome.matches(namePattern)){
            txtCADNome.setError("O nome deve conter apenas letras");
            txtCADNome.requestFocus();
            Toast.makeText(this, "Por gentileza, insira um nome válido.", Toast.LENGTH_LONG).show();
            return;
        }

        if(!senha.equals(confSenha)){
            Toast.makeText(this, "As senhas não coincidem", Toast.LENGTH_SHORT).show();
            return;
        }


        if(!senha.matches(passwordPattern)){
            txtCADSenha.setError("A senha deve conter no mínimo 6 caracteres e conter pelo menos um número!");
            txtCADSenha.requestFocus();
            Toast.makeText(this, "A senha não atende aos requisitos.", Toast.LENGTH_LONG).show();
            return;
        }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                String userId = firebaseUser.getUid();



                userDAO.open();
                boolean sucesso = userDAO.PerfilUsuario( userId, nome, email);
                userDAO.close();

                if(sucesso){
                    Toast.makeText(this, "Cadastro realizado com êxito", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Cadastro.this, InfoValidation.class);
                    startActivity(intent);
                    finish();
                }

               else {
                    Toast.makeText(this, "Erro ao cadastrar usuário", Toast.LENGTH_SHORT).show();
                }

            }else{
                try{
                    throw task.getException();
                } catch (FirebaseAuthUserCollisionException e){
                    Toast.makeText(this, "E-mail já cadastrado", Toast.LENGTH_SHORT).show();
                    txtCADEmail.setError("E-mail já em uso");
                    txtCADEmail.requestFocus();

                } catch (Exception e){
                    Toast.makeText(this, "Erro ao cadastrar usuário:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
