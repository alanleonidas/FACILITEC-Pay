package br.com.tdp.facilitecpay.database;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import br.com.tdp.facilitecpay.R;
import br.com.tdp.facilitecpay.model.ConfiguracaoModel;

public class ConfiguracaoDAO {
    private String Tabela = "CONFIGURACAO";
    private String SQLBase = "SELECT CONF_IP, CONF_PORTA, CONF_INTEGRADOR FROM CONFIGURACAO";
    private SQLiteDatabase conexao;
    private Context context;

    public ConfiguracaoDAO(SQLiteDatabase conexao){
        this.conexao = conexao;
    }

    public void setContext(Context context){
        this.context = context;
    }

    public void inserir(ConfiguracaoModel configuracaoModel){
        ContentValues contentValues = new ContentValues();

        contentValues.put("CONF_IP",configuracaoModel.getCONF_IP());
        contentValues.put("CONF_PORTA",configuracaoModel.getCONF_PORTA());
        contentValues.put("CONF_INTEGRADOR",configuracaoModel.getCONF_INTEGRADOR());
        try{
            conexao.insertOrThrow(Tabela,null, contentValues);
            AlertDialog.Builder dlg = new AlertDialog.Builder(context);
            dlg.setTitle(R.string.title_informacao);
            dlg.setMessage("Configuração registrada com Sucesso!");
            dlg.setNeutralButton(R.string.action_ok,null);
            dlg.setIcon(R.drawable.ic_baseline_check_circle_24);
            dlg.show();
        } catch (SQLException ex){
            AlertDialog.Builder dlg = new AlertDialog.Builder(context);
            dlg.setTitle(R.string.title_erro);
            dlg.setMessage(ex.getMessage());
            dlg.setNeutralButton(R.string.action_ok,null);
            dlg.show();
        }
    }

    public void inserirAll(List<ConfiguracaoModel> listConfiguracao){
        if ((listConfiguracao != null) && (listConfiguracao.size()>0)) {

            for(int i=0; i<listConfiguracao.size(); i++) {
                ConfiguracaoModel configuracaoModel = listConfiguracao.get(i);
                inserir(configuracaoModel);
            }
        }
    }

    public void excluirregistro(int id){
        String[] where = new String[1];
        where[0] = String.valueOf(id);
        conexao.delete(Tabela, "CONF_IP=?",where);
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

    public void alterar(ConfiguracaoModel configuracaoModel){
        ContentValues contentValues = new ContentValues();

        contentValues.put("CONF_IP",configuracaoModel.getCONF_IP());
        contentValues.put("CONF_PORTA",configuracaoModel.getCONF_PORTA());
        contentValues.put("CONF_INTEGRADOR",configuracaoModel.getCONF_INTEGRADOR());

        String[] where = new String[1];
        //where[0] = String.valueOf(configuracaoModel.getCONF_IP());
        try{
            conexao.update(Tabela,contentValues,"",where);
            AlertDialog.Builder dlg = new AlertDialog.Builder(context);
            dlg.setTitle(R.string.title_informacao);
            dlg.setMessage("Configuração registrada com Sucesso!");
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

    public ConfiguracaoModel retornaConfiguracao(Cursor resultado){
        ConfiguracaoModel configuracaoModel = new ConfiguracaoModel();
        configuracaoModel.setCONF_IP(resultado.getString(resultado.getColumnIndexOrThrow("CONF_IP")));
        configuracaoModel.setCONF_PORTA(resultado.getString(resultado.getColumnIndexOrThrow("CONF_PORTA")));
        configuracaoModel.setCONF_INTEGRADOR(resultado.getString(resultado.getColumnIndexOrThrow("CONF_INTEGRADOR")));

        return configuracaoModel;
    }

    public List<ConfiguracaoModel> buscarTodos(){
        List<ConfiguracaoModel> configuracaoModels = new ArrayList<ConfiguracaoModel>();

        StringBuilder sql = new StringBuilder();
        sql.append(SQLBase);
        sql.append(" ORDER BY CONF_IP DESC");

        try{
            Cursor resultado = conexao.rawQuery(sql.toString(),new String[]{});

            if (resultado.getCount() > 0) {
                resultado.moveToFirst();

                do {
                    configuracaoModels.add(retornaConfiguracao(resultado));
                }while (resultado.moveToNext());
            }
        } catch (SQLException ex){
            AlertDialog.Builder dlg = new AlertDialog.Builder(context);
            dlg.setTitle(R.string.title_erro);
            dlg.setMessage(ex.getMessage());
            dlg.setNeutralButton(R.string.action_ok,null);
            dlg.show();
        }


        return configuracaoModels;
    }

    public ConfiguracaoModel buscarConfiguracaoModel(String ip){
        StringBuilder sql = new StringBuilder();
        sql.append(SQLBase);
        sql.append("WHERE CONF_IP=?");

        String[] where = new String[1];
        where[0] = ip;

        Cursor resultado = conexao.rawQuery(sql.toString(),where);
        return retornaConfiguracao(resultado);
    }
}
