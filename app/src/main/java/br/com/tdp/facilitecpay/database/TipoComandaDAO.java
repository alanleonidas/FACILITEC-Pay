package br.com.tdp.facilitecpay.database;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.tdp.facilitecpay.R;
import br.com.tdp.facilitecpay.model.TipoComandaModel;

public class TipoComandaDAO {
    private String Tabela = "TIPOCOMANDA";
    private String SQLBase = "SELECT TCOM_CODIGO, TCOM_BOTAOMOBILE FROM TIPOCOMANDA";
    private SQLiteDatabase conexao;
    private Context context;

    public TipoComandaDAO(SQLiteDatabase conexao){
        this.conexao = conexao;
    }

    public void setContext(Context context){
        this.context = context;
    }

    public void inserir(TipoComandaModel tipoComandaModel){
        ContentValues contentValues = new ContentValues();

        contentValues.put("TCOM_CODIGO",tipoComandaModel.getTCOM_CODIGO());
        contentValues.put("TCOM_BOTAOMOBILE",tipoComandaModel.getTCOM_BOTAOMOBILE());
        try{
            conexao.insertOrThrow(Tabela,null, contentValues);
        } catch (SQLException ex){
            AlertDialog.Builder dlg = new AlertDialog.Builder(context);
            dlg.setTitle(R.string.title_erro);
            dlg.setMessage(ex.getMessage());
            dlg.setNeutralButton(R.string.action_ok,null);
            dlg.show();
        }
    }

    public void inserirAll(List<TipoComandaModel> listTipoComanda){
        if ((listTipoComanda != null) && (listTipoComanda.size()>0)) {

            for(int i=0; i<listTipoComanda.size(); i++) {
                TipoComandaModel tipoComandaModel = listTipoComanda.get(i);
                inserir(tipoComandaModel);
            }
        }
    }

    public void excluirregistro(int id){
        String[] where = new String[1];
        where[0] = String.valueOf(id);
        conexao.delete(Tabela, "TCOM_CODIGO=?",where);
    }

    public void excluirTodos(){
        try{
            conexao.delete(Tabela, null,null);
        } catch (SQLException ex){
            AlertDialog.Builder dlg = new AlertDialog.Builder(context);
            dlg.setTitle(R.string.title_erro);
            dlg.setMessage(ex.getMessage());
            dlg.setNeutralButton(R.string.action_ok,null);
            dlg.show();
        }
    }

    public void alterar(TipoComandaModel tipoComandaModel){
        ContentValues contentValues = new ContentValues();

        contentValues.put("TCOM_CODIGO",tipoComandaModel.getTCOM_CODIGO());
        contentValues.put("TCOM_BOTAOMOBILE",tipoComandaModel.getTCOM_BOTAOMOBILE());

        String[] where = new String[1];
        try{
            conexao.update(Tabela,contentValues,"",where);
        } catch (SQLException ex) {
            AlertDialog.Builder dlg = new AlertDialog.Builder(context);
            dlg.setTitle(R.string.title_erro);
            dlg.setMessage(ex.getMessage());
            dlg.setNeutralButton(R.string.action_ok, null);
            dlg.show();
        }
    }

    public TipoComandaModel retornaTipoComanda(Cursor resultado){
        TipoComandaModel tipoComandaModel = new TipoComandaModel();
        tipoComandaModel.setTCOM_CODIGO(resultado.getString(resultado.getColumnIndexOrThrow("TCOM_CODIGO")));
        tipoComandaModel.setTCOM_BOTAOMOBILE(resultado.getString(resultado.getColumnIndexOrThrow("TCOM_BOTAOMOBILE")));

        return tipoComandaModel;
    }

    public List<TipoComandaModel> buscarTodos(){
        List<TipoComandaModel> tipoComandaModels = new ArrayList<TipoComandaModel>();

        StringBuilder sql = new StringBuilder();
        sql.append(SQLBase);
        sql.append(" ORDER BY TCOM_BOTAOMOBILE DESC");

        try{
            Cursor resultado = conexao.rawQuery(sql.toString(),null);

            if (resultado.getCount() > 0) {
                resultado.moveToFirst();

                do {
                    tipoComandaModels.add(retornaTipoComanda(resultado));
                }while (resultado.moveToNext());
            }
        } catch (SQLException ex){
            AlertDialog.Builder dlg = new AlertDialog.Builder(context);
            dlg.setTitle(R.string.title_erro);
            dlg.setMessage(ex.getMessage());
            dlg.setNeutralButton(R.string.action_ok,null);
            dlg.show();
        }


        return tipoComandaModels;
    }

    public TipoComandaModel buscarTipoComandaModel(String ip){
        if (ip.equals("")==false){
            StringBuilder sql = new StringBuilder();
            sql.append(SQLBase);
            sql.append(" WHERE TCOM_BOTAOMOBILE= '"+ip+"' ");

            Cursor resultado = conexao.rawQuery(sql.toString(),null);
            if (resultado.getCount() > 0) {
                resultado.moveToFirst();
                return retornaTipoComanda(resultado);
            } else {
                return null;
            }
        } else{
            return null;
        }

    }
}
