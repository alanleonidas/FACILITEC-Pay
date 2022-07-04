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
import br.com.tdp.facilitecpay.model.ConfiguracaoModel;
import br.com.tdp.facilitecpay.model.RepreseModel;

public class RepreseDAO {
    private String Tabela = "REPRESE";
    private String SQLBase = "SELECT REP_CODIGO, REP_NOME, REP_USUARIO, REP_SENHAVENDA FROM REPRESE ";
    private SQLiteDatabase conexao;
    private Context context;

    public RepreseDAO(SQLiteDatabase conexao){
        this.conexao = conexao;
    }

    public void setContext(Context context){
        this.context = context;
    }

    public void inserir(RepreseModel represeModel){
        ContentValues contentValues = new ContentValues();

        contentValues.put("REP_CODIGO",represeModel.getREP_CODIGO());
        contentValues.put("REP_NOME",represeModel.getREP_NOME());
        contentValues.put("REP_USUARIO",represeModel.getREP_USUARIO());
        contentValues.put("REP_SENHAVENDA",represeModel.getREP_SENHAVENDA());
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

    public void inserirAll(List<RepreseModel> listReprese){
        if ((listReprese != null) && (listReprese.size()>0)) {

            for(int i=0; i<listReprese.size(); i++) {
                RepreseModel represeModel = listReprese.get(i);
                inserir(represeModel);
            }
        }
    }

    public void excluirRegistro(int id){
        String[] where = new String[1];
        where[0] = String.valueOf(id);
        conexao.delete(Tabela, "REP_CODIGO=?",where);
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

    public void alterar(RepreseModel represeModel){
        ContentValues contentValues = new ContentValues();

        contentValues.put("REP_CODIGO",represeModel.getREP_CODIGO());
        contentValues.put("REP_NOME",represeModel.getREP_NOME());
        contentValues.put("REP_USUARIO",represeModel.getREP_USUARIO());
        contentValues.put("REP_SENHAVENDA",represeModel.getREP_SENHAVENDA());

        String[] where = new String[1];
        where[0] = String.valueOf(represeModel.getREP_CODIGO());
        try{
            conexao.update(Tabela,contentValues,"",where);
            AlertDialog.Builder dlg = new AlertDialog.Builder(context);
            dlg.setTitle(R.string.title_informacao);
            dlg.setMessage("Representante alterado com Sucesso!");
            dlg.setNeutralButton(R.string.action_ok,null);
            dlg.setIcon(R.drawable.ic_baseline_check_circle_24);
            dlg.show();
        } catch (SQLException ex) {
            AlertDialog.Builder dlg = new AlertDialog.Builder(context);
            dlg.setTitle(R.string.title_erro);
            dlg.setMessage(ex.getMessage());
            dlg.setNeutralButton(R.string.action_ok, null);
            dlg.show();
        }
    }

    public RepreseModel retornaReprese(Cursor resultado){
        RepreseModel represeModel = new RepreseModel();
        represeModel.setREP_CODIGO(resultado.getString(resultado.getColumnIndexOrThrow("REP_CODIGO")));
        represeModel.setREP_NOME(resultado.getString(resultado.getColumnIndexOrThrow("REP_NOME")));
        represeModel.setREP_USUARIO(resultado.getString(resultado.getColumnIndexOrThrow("REP_USUARIO")));
        represeModel.setREP_SENHAVENDA(resultado.getString(resultado.getColumnIndexOrThrow("REP_SENHAVENDA")));

        return represeModel;
    }

    public List<RepreseModel> buscarTodos(){
        List<RepreseModel> represeModels = new ArrayList<RepreseModel>();

        StringBuilder sql = new StringBuilder();
        sql.append(SQLBase);
        sql.append(" ORDER BY REP_NOME");

        try{
            Cursor resultado = conexao.rawQuery(sql.toString(),new String[]{});

            if (resultado.getCount() > 0) {
                resultado.moveToFirst();

                do {
                    represeModels.add(retornaReprese(resultado));
                }while (resultado.moveToNext());
            }
        } catch (SQLException ex){
            AlertDialog.Builder dlg = new AlertDialog.Builder(context);
            dlg.setTitle(R.string.title_erro);
            dlg.setMessage(ex.getMessage());
            dlg.setNeutralButton(R.string.action_ok,null);
            dlg.show();
        }


        return represeModels;
    }

    public RepreseModel buscarRepreseModel(String nome){
        StringBuilder sql = new StringBuilder();
        sql.append(SQLBase);
        sql.append(" WHERE REP_NOME=?");

        String[] where = new String[1];
        where[0] = nome;

        Cursor resultado = conexao.rawQuery(sql.toString(),where);
        resultado.moveToFirst();
        return retornaReprese(resultado);
    }
}
