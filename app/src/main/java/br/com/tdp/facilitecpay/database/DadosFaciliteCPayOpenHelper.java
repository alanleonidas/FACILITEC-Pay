package br.com.tdp.facilitecpay.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DadosFaciliteCPayOpenHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    public DadosFaciliteCPayOpenHelper(@Nullable Context context) {
        super(context, "DBFaciliteCPay", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(ScriptDDL.getCreateTabelaConfiguracao());
        sqLiteDatabase.execSQL(ScriptDDL.getCreateTabelaReprese());
        sqLiteDatabase.execSQL(ScriptDDL.getCreateTabelaCobra());
        sqLiteDatabase.execSQL(ScriptDDL.getCreateTabelaTipoComanda());
        sqLiteDatabase.execSQL(ScriptDDL.getCreateTabelaComanda());
        sqLiteDatabase.execSQL(ScriptDDL.getCreateTabelaComandaV());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if (oldVersion != newVersion){
            sqLiteDatabase.execSQL(ScriptDDL.getDropTabelas());
            onCreate(sqLiteDatabase);
        }

    }

}
