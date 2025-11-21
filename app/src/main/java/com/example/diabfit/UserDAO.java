package com.example.diabfit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class UserDAO {

    private final AppDatabase dbHelper;
    private SQLiteDatabase database;



    public UserDAO(Context context) {
        dbHelper = new AppDatabase(context);
    }


    public void open() {
        database = dbHelper.getWritableDatabase();
    }


    public void close() {
        dbHelper.close();
    }

    // Método para inserir um novo usuário (será usado na tela de Cadastro)
    public boolean PerfilUsuario(String userId, String nome, String email) {
        ContentValues values = new ContentValues();
        values.put("firebase_uid", userId);
        values.put("nome", nome);
        values.put("email", email);

        long resultado = database.insertWithOnConflict("cadastros", null, values, SQLiteDatabase.CONFLICT_REPLACE);

        return resultado != -1;
    }


    public String getNomePorId(String userId) {
        String nome = null;
        Cursor cursor = null;
        try {

            cursor = database.query("cadastros", new String[]{"nome"},
                    "firebase_uid = ?", new String[]{userId},
                    null, null, null);

            if (cursor != null && cursor.moveToFirst()) {

                int nomeIndex = cursor.getColumnIndex("nome");
                if (nomeIndex != -1) {
                    nome = cursor.getString(nomeIndex);
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return nome;
    }


    // Método para inserir as informações do usuário (na tela de informações)
    public boolean infos(String sugarLevel, String peso, String altura){
        ContentValues values = new ContentValues();
        values.put("sugarLevel", sugarLevel);
        values.put("peso", peso);
        values.put("altura", altura);
        long resultado = -1;
        try{
            resultado = database.insert("dados", null, values);
        } catch(Exception e){
            Log.e("UserDAO", "Erro ao inserir informações", e);
        }
        return resultado != -1;
    }



    public long insertReminder(String description, long reminderTime, int pendingIntentId) {
        ContentValues values = new ContentValues();
        values.put("description", description);
        values.put("reminder_time", reminderTime);
        values.put("pending_intent_id", pendingIntentId);

        return database.insert("reminders", null, values);
    }


    public boolean updateReminder(int reminderId, String description, long reminderTime, int pendingIntentId) {
        ContentValues values = new ContentValues();
        values.put("description", description);
        values.put("reminder_time", reminderTime);
        values.put("pending_intent_id", pendingIntentId);

        int rowsAffected = database.update("reminders", values, "id = ?", new String[]{String.valueOf(reminderId)});
        return rowsAffected > 0;
    }


    public boolean deleteReminder(int reminderId) {
        int rowsAffected = database.delete("reminders", "id = ?", new String[]{String.valueOf(reminderId)});
        return rowsAffected > 0;
    }


    public Cursor getAllReminders() {
        return database.query("reminders", null, null, null, null, null, "reminder_time ASC");
    }


    public Cursor getReminderById(int reminderId) {
        return database.query("reminders", null, "id = ?", new String[]{String.valueOf(reminderId)}, null, null, null);
    }

    public boolean deleteUser(String userId) {
        int rowsAffected = database.delete("cadastros", "firebase_uid = ?", new String[]{userId});
        return rowsAffected > 0;
    }
}
