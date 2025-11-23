package com.example.diabfit;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import com.google.android.material.appbar.MaterialToolbar;

public class ReminderListActivity extends AppCompatActivity implements ReminderAdapter.OnReminderListener {

    private UserDAO userDAO;
    private RecyclerView recyclerView;
    private ReminderAdapter adapter;
    private final List<Reminder> reminderList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_list);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        userDAO = new UserDAO(this);
        recyclerView = findViewById(R.id.recyclerViewReminders);
        FloatingActionButton fab = findViewById(R.id.fabAddReminder);

        MaterialToolbar toolbar = findViewById(R.id.top_app_bar_lembretes);

        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });


        adapter = new ReminderAdapter(this, reminderList, this);
        recyclerView.setAdapter(adapter);


        fab.setOnClickListener(v -> {
            Intent intent = new Intent(ReminderListActivity.this, AddTaskActivity.class);
            startActivity(intent);
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        loadReminders();
    }

    private void loadReminders() {
        userDAO.open();
        Cursor cursor = userDAO.getAllReminders();
        reminderList.clear();

        if (cursor != null && cursor.moveToFirst()) {

            int idIndex = cursor.getColumnIndex("id");
            int descIndex = cursor.getColumnIndex("description");
            int timeIndex = cursor.getColumnIndex("reminder_time");
            int pendingIdIndex = cursor.getColumnIndex("pending_intent_id");

            do {

                if (idIndex != -1 && descIndex != -1 && timeIndex != -1 && pendingIdIndex != -1) {
                    int id = cursor.getInt(idIndex);
                    String description = cursor.getString(descIndex);
                    long reminderTime = cursor.getLong(timeIndex);
                    int pendingIntentId = cursor.getInt(pendingIdIndex);


                    reminderList.add(new Reminder(id, description, reminderTime, pendingIntentId));
                }
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }
        userDAO.close();


        adapter.setReminders(reminderList);
    }



    @Override
    public void onEditClick(Reminder reminder) {

        Intent intent = new Intent(this, AddTaskActivity.class);
        intent.putExtra("REMINDER_ID", reminder.getId());
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(Reminder reminder) {

        new AlertDialog.Builder(this)
                .setTitle("Apagar Lembrete")
                .setMessage("Você tem certeza que quer apagar este lembrete?")
                .setPositiveButton("Sim", (dialog, which) -> {

                    deleteReminder(reminder);
                })
                .setNegativeButton("Não", null)
                .show();
    }

    private void deleteReminder(Reminder reminder) {
        userDAO.open();
        boolean success = userDAO.deleteReminder(reminder.getId());
        userDAO.close();

        if (success) {

            cancelAlarm(reminder.getPendingIntentId());
            Toast.makeText(this, "Lembrete apagado.", Toast.LENGTH_SHORT).show();

            loadReminders();
        } else {
            Toast.makeText(this, "Erro ao apagar o lembrete.", Toast.LENGTH_SHORT).show();
        }
    }

    private void cancelAlarm(int pendingIntentId) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, pendingIntentId, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }
}
