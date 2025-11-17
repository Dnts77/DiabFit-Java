package com.example.diabfit;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.Locale;

public class Treinos extends AppCompatActivity {

    private TextView cardioTitle, cardioContent, strengthTitle, strengthContent, tvIMCInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treinos);


        MaterialToolbar toolbar = findViewById(R.id.top_app_bar_treinos);
        toolbar.setNavigationOnClickListener(view -> finish());


        cardioTitle = findViewById(R.id.cardioTitle);
        cardioContent = findViewById(R.id.cardioContent);
        strengthTitle = findViewById(R.id.strengthTitle);
        strengthContent = findViewById(R.id.strengthContent);
        tvIMCInfo = findViewById(R.id.tvIMCInfo);


        float imc = getIntent().getFloatExtra("USER_IMC", 0);

        String imcFormat = String.format(Locale.getDefault(), "%.1f", imc);


        generateWorkoutPlan(imc, imcFormat);
    }


    private void generateWorkoutPlan(float imc, String imcFormat) {
        String cardioPlan;
        String strengthPlan;
        String status;

        if (imc < 18.5) { // Abaixo do peso
            cardioTitle.setText("Cardio Leve e Consistente");
            cardioPlan = "• Caminhada Moderada: 30 minutos\n" +
                    "• Ciclismo Leve: 20 minutos\n" +
                    "• Elíptico: 15 minutos";
            status = "Abaixo do peso";

            strengthTitle.setText("Foco em Hipertrofia (Ganho de Massa)");
            strengthPlan = "• Agachamentos com peso: 4 séries de 10 repetições\n" +
                    "• Supino: 3 séries de 8 repetições\n" +
                    "• Remada Curvada: 3 séries de 10 repetições";

            tvIMCInfo.setText(String.format("Seu IMC atual é: %s - %s", imcFormat, status));

        } else if (imc >= 18.5 && imc < 24.9) { // Peso Normal
            cardioTitle.setText("Cardio para Manutenção e Saúde");
            cardioPlan = "• Corrida Moderada: 30 minutos\n" +
                    "• Natação: 30 minutos\n" +
                    "• HIIT (20s de esforço, 40s de descanso): 15 minutos";
            status = "Peso Normal";

            strengthTitle.setText("Treino Funcional Completo");
            strengthPlan = "• Levantamento Terra: 3 séries de 8 repetições\n" +
                    "• Flexões: 4 séries até a falha\n" +
                    "• Afundo com halteres: 3 séries de 12 repetições por perna";

            tvIMCInfo.setText(String.format("Seu IMC atual é: %s - %s", imcFormat, status));


        } else { // Sobrepeso ou Obesidade (IMC >= 25)
            cardioTitle.setText("Cardio com Foco em Queima de Gordura (Baixo Impacto)");
            cardioPlan = "• Caminhada Inclinada na Esteira: 40 minutos\n" +
                    "• Ciclismo Estacionário: 35 minutos\n" +
                    "• Remo: 20 minutos";
            status = "Soprepeso ou Obesidade";

            strengthTitle.setText("Treino de Força para Acelerar o Metabolismo");
            strengthPlan = "• Circuito: Agachamento > Remada > Elevação de joelhos > Descanso 1 min. (Repetir 4x)\n" +
                    "• Prancha: 4 séries de 45 segundos\n" +
                    "• Desenvolvimento de ombros: 3 séries de 12 repetições";

            tvIMCInfo.setText(String.format("Seu IMC atual é: %s - %s", imcFormat, status));
        }


        cardioContent.setText(cardioPlan);
        strengthContent.setText(strengthPlan);

    }
}
