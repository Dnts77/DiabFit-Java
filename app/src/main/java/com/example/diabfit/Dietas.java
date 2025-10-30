package com.example.diabfit;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;

public class Dietas extends AppCompatActivity {


    private TextView tvNivelAcucarInfo;
    private TextView tvOpcaoCafeDaManha;
    private TextView tvOpcaoAlmoco;
    private TextView tvOpcaoJantar;
    private Button btVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dietas);


        initComponents();
        setupListeners();
        loadDietData();
    }


    private void initComponents() {
        tvNivelAcucarInfo = findViewById(R.id.tvNivelAcucarInfo);
        tvOpcaoCafeDaManha = findViewById(R.id.tvOpcaoCafeDaManha);
        tvOpcaoAlmoco = findViewById(R.id.tvOpcaoAlmoco);
        tvOpcaoJantar = findViewById(R.id.tvOpcaoJantar);
        btVoltar = findViewById(R.id.btVoltar);
    }


    private void setupListeners() {
        btVoltar.setOnClickListener(v -> {
            finish();
        });
    }


    private void loadDietData() {

        int nivelAcucar = getIntent().getIntExtra("NIVEL_ACUCAR", -1);
        int peso = getIntent().getIntExtra("PESO", -1);
        int altura = getIntent().getIntExtra("ALTURA", -1);


        if (nivelAcucar != -1) {

            carregarDietas(nivelAcucar, peso, altura);
        } else {

            tvNivelAcucarInfo.setText("Nível de açúcar não informado.");
            carregarDietaPadrao();
        }
    }


    private void carregarDietas(int nivelAcucar, int peso, int altura) {
        String status;

        if (nivelAcucar < 70) {
            status = "Baixo (Hipoglicemia)";
            setDietaHipoglicemia();
        } else if (nivelAcucar >= 70 && nivelAcucar <= 99) {
            status = "Normal";
            setDietaNormal();
        }else if(nivelAcucar > 99 && nivelAcucar <= 125){
            status = "Pré-diabetes";
            setDietaPreDiabetes();
        }
         else {
            status = "Alto (Hiperglicemia)";
            setDietaHiperglicemia();
        }

        tvNivelAcucarInfo.setText(String.format(Locale.getDefault(), "Nível de Açúcar: %d mg/dL - %s", nivelAcucar, status));
    }



    // São exemplos, o melhor a se fazer é consultar um nutricionista.
    private void setDietaHipoglicemia() {
        tvOpcaoCafeDaManha.setText("• 1 copo de suco de laranja natural (rápida absorção)\n• 1 fatia de pão integral com queijo branco.");
        tvOpcaoAlmoco.setText("• Salada de folhas verdes\n• 1 filé de frango grelhado\n• 2 colheres de sopa de arroz integral\n• 1 porção de legumes cozidos.");
        tvOpcaoJantar.setText("• Sopa de legumes com pedaços de carne magra\n• 1 fruta (maçã ou pera).");
    }

    private void setDietaNormal() {
        tvOpcaoCafeDaManha.setText("• Iogurte natural com aveia e frutas vermelhas\n• 1 xícara de café ou chá sem açúcar.");
        tvOpcaoAlmoco.setText("• Salada colorida à vontade\n• 1 posta de salmão assado\n• 3 colheres de sopa de purê de batata-doce.");
        tvOpcaoJantar.setText("• Omelete com 2 ovos, tomate e espinafre\n• Salada de acompanhamento.");
    }

    private void setDietaHiperglicemia() {
        tvOpcaoCafeDaManha.setText("• 2 ovos mexidos\n• 1/2 abacate pequeno\n• Chá de ervas sem açúcar.");
        tvOpcaoAlmoco.setText("• Salada verde com pepino e tomate\n• Pedaços de frango desfiado\n• Abobrinha e berinjela grelhadas.");
        tvOpcaoJantar.setText("• Caldo de abóbora com gengibre\n• Lascas de frango ou tofu.");
    }

    private void setDietaPreDiabetes(){
        tvOpcaoCafeDaManha.setText("• Ovos mexidos com espinafre e cogumelos.\n• 1 fatia de pão 100% integral.\n• Café ou chá sem açúcar.");
        tvOpcaoAlmoco.setText("• Salada grande de folhas verdes com tomate, pepino e grão de bico.\n• 1 filé de peito de frango grelhado em cubos.\n• 2 colheres de sopa de quinoa.");
        tvOpcaoJantar.setText("• Sopa de lentilhas com legumes (cenoura, aipo, abobrinha).\n• 1 porção de brócolis cozido no vapor com azeite de oliva.");
    }

    private void carregarDietaPadrao() {
        String msgPadrao = "Consulte um profissional de saúde para uma dieta personalizada.";
        tvOpcaoCafeDaManha.setText(msgPadrao);
        tvOpcaoAlmoco.setText(msgPadrao);
        tvOpcaoJantar.setText(msgPadrao);
    }
}
