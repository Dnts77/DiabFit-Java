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







}


