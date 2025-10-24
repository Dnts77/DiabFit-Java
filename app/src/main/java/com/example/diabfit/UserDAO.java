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
    public boolean inserir(String nome, String email, String senha) {
        ContentValues values = new ContentValues();
        values.put("nome", nome);
        values.put("email", email);
        values.put("senha", senha);
        long resultado = database.insert("cadastros", null, values);
        return resultado != -1;
    }


    public boolean checarUsuario(String email, String senha) {

        String[] columns = {"email"};

        String selection = "email = ? AND senha = ?";

        String[] selectionArgs = {email, senha};

        Cursor cursor = null;
        try {
            cursor = database.query(
                    "cadastros",
                    columns,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );


            int count = cursor.getCount();
            return count > 0;

        } catch (Exception e) {
            Log.e("UserDAO", "Erro ao checar usuário", e);
            return false;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}

