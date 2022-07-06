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
import br.com.tdp.facilitecpay.model.ComandasLiberadasModel;
import br.com.tdp.facilitecpay.model.PagamentosComandaModel;

public class PagamentosComandaDAO {
    private String Tabela = "COMANDAV";
    private String SQLBase = "SELECT COMV_EMPRESA, COMV_TIPOCOMANDA, COMV_COMANDA, COMV_SEQUENCIA, "+
            " COMV_COBRANCA, COMV_VALOR "+
            " FROM COMANDAV ";
    private SQLiteDatabase conexao;
    private Context context;

    public PagamentosComandaDAO(SQLiteDatabase conexao){
        this.conexao = conexao;
    }

    public void setContext(Context context){
        this.context = context;
    }

    public void inserir(PagamentosComandaModel pagamentosComandaModel){
        ContentValues contentValues = getContentValue(pagamentosComandaModel);
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

    private ContentValues getContentValue(PagamentosComandaModel pagamentosComandaModel){
        ContentValues contentValues = new ContentValues();

        contentValues.put("COMV_EMPRESA",pagamentosComandaModel.getCOMV_EMPRESA());
        contentValues.put("COMV_TIPOCOMANDA",pagamentosComandaModel.getCOMV_TIPOCOMANDA());
        contentValues.put("COMV_COMANDA",pagamentosComandaModel.getCOMV_COMANDA());
        contentValues.put("COMV_SEQUENCIA",pagamentosComandaModel.getCOMV_SEQUENCIA());
        contentValues.put("COMV_COBRANCA",pagamentosComandaModel.getCOMV_COBRANCA());
        contentValues.put("COMV_VALOR",pagamentosComandaModel.getCOMV_VALOR());
        return contentValues;
    }
    public void inserirAll(List<PagamentosComandaModel> listpaPagamentosComandaModels){
        if ((listpaPagamentosComandaModels != null) && (listpaPagamentosComandaModels.size()>0)) {

            for(int i=0; i<listpaPagamentosComandaModels.size(); i++) {
                PagamentosComandaModel pagamentosComandaModel = listpaPagamentosComandaModels.get(i);
                inserir(pagamentosComandaModel);
            }
        }
    }

    public void excluirRegistro(PagamentosComandaModel pagamentosComandaModel){
        String[] where = new String[4];
        where[0] = String.valueOf(pagamentosComandaModel.getCOMV_EMPRESA());
        where[1] = String.valueOf(pagamentosComandaModel.getCOMV_TIPOCOMANDA());
        where[2] = String.valueOf(pagamentosComandaModel.getCOMV_COMANDA());
        where[3] = String.valueOf(pagamentosComandaModel.getCOMV_SEQUENCIA());
        conexao.delete(Tabela, "COMV_EMPRESA=? AND COMV_TIPOCOMANDA=? AND COMV_COMANDA=? AND COMV_SEQUENCIA=?",where);
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

    public void alterar(PagamentosComandaModel pagamentosComandaModel){
        ContentValues contentValues = getContentValue(pagamentosComandaModel);

        String[] where = new String[4];
        where[0] = String.valueOf(pagamentosComandaModel.getCOMV_EMPRESA());
        where[1] = String.valueOf(pagamentosComandaModel.getCOMV_TIPOCOMANDA());
        where[2] = String.valueOf(pagamentosComandaModel.getCOMV_COMANDA());
        where[3] = String.valueOf(pagamentosComandaModel.getCOMV_SEQUENCIA());
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

    public PagamentosComandaModel retornaPagamentosComanda(Cursor resultado){
        PagamentosComandaModel pagamentosComandaModel = new PagamentosComandaModel();
        pagamentosComandaModel.setCOMV_EMPRESA(resultado.getString(resultado.getColumnIndexOrThrow("COMV_EMPRESA")));
        pagamentosComandaModel.setCOMV_TIPOCOMANDA(resultado.getString(resultado.getColumnIndexOrThrow("COMV_TIPOCOMANDA")));
        pagamentosComandaModel.setCOMV_COMANDA(resultado.getString(resultado.getColumnIndexOrThrow("COMV_COMANDA")));
        pagamentosComandaModel.setCOMV_SEQUENCIA(resultado.getInt(resultado.getColumnIndexOrThrow("COMV_SEQUENCIA")));
        pagamentosComandaModel.setCOMV_COBRANCA(resultado.getString(resultado.getColumnIndexOrThrow("COMV_COBRANCA")));
        pagamentosComandaModel.setCOMV_VALOR(resultado.getDouble(resultado.getColumnIndexOrThrow("COMV_VALOR")));
        return pagamentosComandaModel;
    }

    public List<PagamentosComandaModel> buscarTodos(String empresa, String tipoComanda, String comanda, int sequencia) {
        List<PagamentosComandaModel> pagamentosComandaModels = new ArrayList<PagamentosComandaModel>();

        StringBuilder sql = new StringBuilder();
        sql.append(SQLBase);
        if (tipoComanda.equals("")==false){
            sql.append(" WHERE COMV_EMPRESA ='"+String.valueOf(empresa)+"' and COMV_TIPOCOMANDA ='"+String.valueOf(tipoComanda)+"'"+
                       "       and COMV_COMANDA ='"+String.valueOf(comanda)+"' and COMV_SEQUENCIA = "+Integer.toString(sequencia));
        }
        sql.append(" ORDER BY COMV_EMPRESA, COMV_TIPOCOMANDA, COMV_COMANDA, COMV_SEQUENCIA");

        try {
            Cursor resultado = conexao.rawQuery(sql.toString(), new String[]{});

            if (resultado.getCount() > 0) {
                resultado.moveToFirst();

                do {
                    pagamentosComandaModels.add(retornaPagamentosComanda(resultado));
                } while (resultado.moveToNext());
            }
        } catch (SQLException ex) {
            AlertDialog.Builder dlg = new AlertDialog.Builder(context);
            dlg.setTitle(R.string.title_erro);
            dlg.setMessage(ex.getMessage());
            dlg.setNeutralButton(R.string.action_ok, null);
            dlg.show();
        }


        return pagamentosComandaModels;
    }
}
