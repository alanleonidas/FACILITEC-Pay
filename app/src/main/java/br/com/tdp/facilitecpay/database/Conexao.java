package br.com.tdp.facilitecpay.database;

import android.app.AlertDialog;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.material.snackbar.Snackbar;

import br.com.tdp.facilitecpay.R;

public class Conexao {
    private SQLiteDatabase conexao;
    private DadosFaciliteCPayOpenHelper dadosFaciliteCPayOpenHelper;
    private LinearLayout layoutfragment;

    public SQLiteDatabase retornaConexao(Context context, View view){
        try{
            dadosFaciliteCPayOpenHelper = new DadosFaciliteCPayOpenHelper(context);
            conexao = dadosFaciliteCPayOpenHelper.getWritableDatabase();

            /*Snackbar.make(view, R.string.mensage_conexao_criada_com_sucesso,Snackbar.LENGTH_SHORT)
                    .setAction(R.string.action_ok,null)
                    .show();*/
        } catch (SQLException ex){
            AlertDialog.Builder dlg = new AlertDialog.Builder(context);
            dlg.setTitle(R.string.title_erro);
            dlg.setMessage(ex.getMessage());
            dlg.setNeutralButton(R.string.action_ok,null);
            dlg.show();
        }
        return conexao;
    }
}
