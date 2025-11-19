package com.example.diabfit;

import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;

public class AddTaskActivity extends AppCompatActivity {

    private TextInputEditText editTextTask;
    private Button buttonSaveTask, BackToHome;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_add_task);



        editTextTask = findViewById(R.id.edit_text_task);
        buttonSaveTask = findViewById(R.id.button_save_task);
        BackToHome = findViewById(R.id.BackToHome);




        buttonSaveTask.setOnClickListener(view -> {


            finish();
        });
    }
}
