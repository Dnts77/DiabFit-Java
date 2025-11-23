package com.example.diabfit;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddTaskActivity extends AppCompatActivity {

    private TextInputEditText editTextTask;
    private TextInputEditText editTextDate;
    private TextInputEditText editTextTime;
    private Button buttonSaveTask;
    private Button buttonBackToHome;

    private UserDAO userDAO;


    private boolean isEditMode = false;
    private int currentReminderId = -1;
    private int currentPendingIntentId = -1;


    private final Calendar selectedDateTime = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        userDAO = new UserDAO(this);
        userDAO.open();

        createNotificationChannel();

        editTextTask = findViewById(R.id.edit_text_task);
        editTextDate = findViewById(R.id.edit_text_date);
        editTextTime = findViewById(R.id.edit_text_time);
        buttonSaveTask = findViewById(R.id.button_save_task);
        buttonBackToHome = findViewById(R.id.BackToHome);


        if (getIntent().hasExtra("REMINDER_ID")) {
            currentReminderId = getIntent().getIntExtra("REMINDER_ID", -1);
            if (currentReminderId != -1) {
                isEditMode = true;
                prepareEditMode();
            }
        }


        setupClickListeners();
    }


    private void prepareEditMode() {
        buttonSaveTask.setText("Salvar Alterações");
        Cursor cursor = userDAO.getReminderById(currentReminderId);
        if (cursor != null && cursor.moveToFirst()) {

            String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
            long reminderTime = cursor.getLong(cursor.getColumnIndexOrThrow("reminder_time"));
            currentPendingIntentId = cursor.getInt(cursor.getColumnIndexOrThrow("pending_intent_id"));


            editTextTask.setText(description);
            selectedDateTime.setTimeInMillis(reminderTime);

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            editTextDate.setText(dateFormat.format(selectedDateTime.getTime()));
            editTextTime.setText(timeFormat.format(selectedDateTime.getTime()));

            cursor.close();
        }
    }

    private void cancelAlarm(int pendingIntentId) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, pendingIntentId, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }

    private void setupClickListeners() {
        editTextDate.setOnClickListener(v -> showDatePicker());
        editTextTime.setOnClickListener(v -> showTimePicker());

        buttonSaveTask.setOnClickListener(v -> {
            String taskDescription = editTextTask.getText().toString();
            String date = editTextDate.getText().toString();
            String time = editTextTime.getText().toString();

            if (taskDescription.isEmpty() || date.isEmpty() || time.isEmpty()) {
                Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();
                return;
            }

            long reminderTimeInMillis = selectedDateTime.getTimeInMillis();


            if (isEditMode) {

                cancelAlarm(currentPendingIntentId);
                boolean success = userDAO.updateReminder(currentReminderId, taskDescription, reminderTimeInMillis, currentPendingIntentId);
                if (success) {
                    scheduleNotification(taskDescription, reminderTimeInMillis, currentPendingIntentId);
                    Toast.makeText(this, "Lembrete atualizado!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Erro ao atualizar o lembrete.", Toast.LENGTH_SHORT).show();
                }
            } else {

                int pendingIntentId = (int) System.currentTimeMillis();
                long newReminderId = userDAO.insertReminder(taskDescription, reminderTimeInMillis, pendingIntentId);

                if (newReminderId != -1) {
                    scheduleNotification(taskDescription, reminderTimeInMillis, pendingIntentId);
                    Toast.makeText(this, "Lembrete salvo com sucesso!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Erro ao salvar o lembrete.", Toast.LENGTH_SHORT).show();
                }
            }
            finish();

        });

        buttonBackToHome.setOnClickListener(v -> {
            finish();
        });
    }


    private void scheduleNotification(String taskDescription, long reminderTimeInMillis, int pendingIntentId) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, NotificationReceiver.class);
        intent.putExtra("TASK_DESCRIPTION", taskDescription);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, pendingIntentId, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);


        long triggerAtMillis = reminderTimeInMillis - (5 * 60 * 1000); // 5 minutos

        if (triggerAtMillis < System.currentTimeMillis()) {
            Toast.makeText(this, "O horário do lembrete deve ser no futuro.", Toast.LENGTH_LONG).show();

            return;
        }

        if (alarmManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
                Toast.makeText(this, "Permissão para alarmes exatos não concedida.", Toast.LENGTH_LONG).show();

                alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
            } else {

                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        userDAO.close();
    }


    private void showDatePicker() {
        Calendar currentDate = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    selectedDateTime.set(Calendar.YEAR, year);
                    selectedDateTime.set(Calendar.MONTH, month);
                    selectedDateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    editTextDate.setText(dateFormat.format(selectedDateTime.getTime()));
                },
                currentDate.get(Calendar.YEAR),
                currentDate.get(Calendar.MONTH),
                currentDate.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void showTimePicker() {
        Calendar currentTime = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute) -> {
                    selectedDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    selectedDateTime.set(Calendar.MINUTE, minute);
                    selectedDateTime.set(Calendar.SECOND, 0);
                    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                    editTextTime.setText(timeFormat.format(selectedDateTime.getTime()));
                },
                currentTime.get(Calendar.HOUR_OF_DAY),
                currentTime.get(Calendar.MINUTE),
                true
        );
        timePickerDialog.show();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Canal de Lembretes";
            String description = "Canal para notificações de lembretes do DiabFit";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("REMINDER_CHANNEL_ID", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
