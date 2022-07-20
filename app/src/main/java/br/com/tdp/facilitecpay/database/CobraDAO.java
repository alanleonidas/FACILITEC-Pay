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
import br.com.tdp.facilitecpay.model.CobraModel;
import br.com.tdp.facilitecpay.model.PagamentosComandaModel;

public class CobraDAO {
    private String Tabela = "COBRA";
    private String SQLBase = "SELECT COB_DESCRICAO, COB_TIPOCOB, COB_CODIGO, COB_OCULTABALCAO, "+
            " COB_CARTEIRADIGITAL "+
            " FROM COBRA ";
    private SQLiteDatabase conexao;
    private Context context;

    public CobraDAO(SQLiteDatabase conexao){
        this.conexao = conexao;
    }

    public void setContext(Context context){
        this.context = context;
    }

    public void inserir(CobraModel cobraModel){
        ContentValues contentValues = getContentValue(cobraModel);
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

    private ContentValues getContentValue(CobraModel cobraModel){
        ContentValues contentValues = new ContentValues();

        contentValues.put("COB_CODIGO",cobraModel.getCOB_CODIGO());
        contentValues.put("COB_TIPOCOB",cobraModel.getCOB_TIPOCOB());
        contentValues.put("COB_CARTEIRADIGITAL",cobraModel.getCOB_CARTEIRADIGITAL());
        contentValues.put("COB_DESCRICAO",cobraModel.getCOB_DESCRICAO());
        contentValues.put("COB_OCULTABALCAO",cobraModel.getCOB_OCULTABALCAO());
        return contentValues;
    }
    public void inserirAll(List<CobraModel> cobraModelList){
        if ((cobraModelList != null) && (cobraModelList.size()>0)) {

            for(int i=0; i<cobraModelList.size(); i++) {
                CobraModel cobraModel = cobraModelList.get(i);
                inserir(cobraModel);
            }
        }
    }

    public void excluirRegistro(CobraModel cobraModel){
        String[] where = new String[1];
        where[0] = String.valueOf(cobraModel.getCOB_DESCRICAO());
        conexao.delete(Tabela, "COB_DESCRICAO=? ",where);
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

    public void alterar(CobraModel cobraModel){
        ContentValues contentValues = getContentValue(cobraModel);

        String[] where = new String[1];
        where[0] = String.valueOf(cobraModel.getCOB_DESCRICAO());

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

    public CobraModel retornaCobranca(Cursor resultado){
        CobraModel cobraModel = new CobraModel();
        cobraModel.setCOB_CODIGO(resultado.getString(resultado.getColumnIndexOrThrow("COB_CODIGO")));
        cobraModel.setCOB_CARTEIRADIGITAL(resultado.getString(resultado.getColumnIndexOrThrow("COB_CARTEIRADIGITAL")));
        cobraModel.setCOB_DESCRICAO(resultado.getString(resultado.getColumnIndexOrThrow("COB_DESCRICAO")));
        cobraModel.setCOB_OCULTABALCAO(resultado.getString(resultado.getColumnIndexOrThrow("COB_OCULTABALCAO")));
        cobraModel.setCOB_TIPOCOB(resultado.getString(resultado.getColumnIndexOrThrow("COB_TIPOCOB")));
        return cobraModel;
    }

    public List<CobraModel> buscarTodos() {
        List<CobraModel> cobraModels = new ArrayList<CobraModel>();

        StringBuilder sql = new StringBuilder();
        sql.append(SQLBase);
        sql.append(" ORDER BY COB_CODIGO, COB_DESCRICAO");

        try {
            Cursor resultado = conexao.rawQuery(sql.toString(), new String[]{});

            if (resultado.getCount() > 0) {
                resultado.moveToFirst();

                do {
                    cobraModels.add(retornaCobranca(resultado));
                } while (resultado.moveToNext());
            }
        } catch (SQLException ex) {
            AlertDialog.Builder dlg = new AlertDialog.Builder(context);
            dlg.setTitle(R.string.title_erro);
            dlg.setMessage(ex.getMessage());
            dlg.setNeutralButton(R.string.action_ok, null);
            dlg.show();
        }
        return cobraModels;
    }
}
