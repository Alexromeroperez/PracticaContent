package com.arp.practicacontent.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.arp.practicacontent.provider.Contrato;

/**
 * Created by Alex on 19/01/2016.
 */
public class Ayudante extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="musica.sqlite";
    public static final int DATABASE_VERSION=1;

    public Ayudante(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql="create table "+ Contrato.TablaDisco.TABLA+
                " ("+ Contrato.TablaDisco._ID+" integer primary key autoincrement, "+
                Contrato.TablaDisco.NOMBRE+" text, "+
                Contrato.TablaDisco.INTERPRETE+" integer )";
        db.execSQL(sql);
        sql="create table "+Contrato.TablaCancion.TABLA+
                " ("+ Contrato.TablaCancion._ID+" integer primary key autoincrement, "+
                Contrato.TablaCancion.TITULO+" text, "+
                Contrato.TablaCancion.IDDISCO+" integer )";
        db.execSQL(sql);
        sql="create table "+Contrato.TablaInterprete.TABLA+
                " ("+ Contrato.TablaInterprete._ID+" integer primary key autoincrement, "+
                Contrato.TablaInterprete.NOMBRE+" text )";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }
}
