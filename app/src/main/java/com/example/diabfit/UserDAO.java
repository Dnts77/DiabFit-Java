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
    public int inserir(String nome, String email, String senha) {
        if(checarEmail(email)){
            return 0;
        }

        ContentValues values = new ContentValues();
        values.put("nome", nome);
        values.put("email", email);
        values.put("senha", senha);
        long resultado = database.insert("cadastros", null, values);

        if(resultado == -1){
            return -1; //Deu ruim
        }
        return 1; //Sucesso
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

    public boolean checarEmail(String email){
        Cursor cursor = null;
        try{
           cursor = database.query(
                   "cadastros",
                   new String[]{"email"},
                   "email = ?",
                   new String[]{email},
                   null,
                   null,
                   null
           );
           return cursor.getCount() > 0;
        } catch(Exception e){
            Log.e("Email já cadastrado", String.valueOf(e));
            return false;
        } finally{
            if(cursor != null){
                cursor.close();
            }
        }
    }

    public String getSenhaPorEmail(String email) {
        String[] columns = {"senha"};
        String selection = "email = ?";
        String[] selectionArgs = {email};
        Cursor cursor = null;
        String senha = null;

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

            if (cursor != null && cursor.moveToFirst()) {
                int senhaColumnIndex = cursor.getColumnIndex("senha");
                if (senhaColumnIndex != -1) {
                    senha = cursor.getString(senhaColumnIndex);
                }
            }
        } catch (Exception e) {
            Log.e("UserDAO", "Erro ao buscar a senha através do email", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return senha;

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


