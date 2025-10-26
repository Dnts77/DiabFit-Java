package com.example.diabfit;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AppDatabase extends SQLiteOpenHelper {

    private static final String NOME_BANCO = "diabfit_db";
    private static final int VERSAO = 4;
    public AppDatabase(Context context) {
        super(context, NOME_BANCO, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE cadastros ("
                + "senha integer,"
                + "nome text,"
                + "email text)";
        db.execSQL(sql);
        sql = "CREATE TABLE dados("
                + "sugarLevel integer,"
                + "peso integer,"
                + "altura integer)";
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS cadastros");
        db.execSQL("DROP TABLE IF EXISTS dados");
        onCreate(db);
    }
}

