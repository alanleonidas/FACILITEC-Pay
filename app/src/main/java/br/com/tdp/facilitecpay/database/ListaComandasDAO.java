package br.com.tdp.facilitecpay.database;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.tdp.facilitecpay.R;
import br.com.tdp.facilitecpay.model.ComandasLiberadasModel;

public class ListaComandasDAO {
    private String Tabela = "COMANDA";
    private String SQLBase = "SELECT COM_EMPRESA, COM_TIPOCOMANDA, COM_COMANDA, COM_SEQUENCIA, COM_STATUS, "+
                             " COM_DATA, COM_REPRESENTANTE, COM_HORAABERTURA, COM_CAIXA, COM_TAXASERVICO, "+
                             " COM_TAXAENTREGA, COM_COUVER, COM_MESA, COM_VLRDESCONTO, COM_PORDESCONTO, "+
                             " COM_IGNORATAXASERVICO, COM_NOMECLIENTE, TOTAL_PROD, VLR_TAXA_SERVICO, "+
                             " COUVER_ENTREGA, TOTAL_RECEBIDO, TOTAL_COMANDA, COM_VLRDESCONTOAPP, "+
                             " COM_PORDESCONTOAPP, SINCRONIZADO "+
                             " FROM COMANDA ";
    private SQLiteDatabase conexao;
    private Context context;

    public ListaComandasDAO(SQLiteDatabase conexao){
        this.conexao = conexao;
    }

    public void setContext(Context context){
        this.context = context;
    }

    public void inserir(ComandasLiberadasModel comandasLiberadasModel){
        ContentValues contentValues = getContentValue(comandasLiberadasModel);
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

    private ContentValues getContentValue(ComandasLiberadasModel comandasLiberadasModel){
        ContentValues contentValues = new ContentValues();

        contentValues.put("COM_EMPRESA",comandasLiberadasModel.getCOM_EMPRESA());
        contentValues.put("COM_TIPOCOMANDA",comandasLiberadasModel.getCOM_TIPOCOMANDA());
        contentValues.put("COM_COMANDA",comandasLiberadasModel.getCOM_COMANDA());
        contentValues.put("COM_SEQUENCIA",comandasLiberadasModel.getCOM_SEQUENCIA());
        contentValues.put("COM_STATUS",comandasLiberadasModel.getCOM_STATUS());
        contentValues.put("COM_STATUS",comandasLiberadasModel.getCOM_STATUS());
        contentValues.put("COM_REPRESENTANTE",comandasLiberadasModel.getCOM_REPRESENTANTE());
        contentValues.put("COM_HORAABERTURA",comandasLiberadasModel.getCOM_HORAABERTURA());
        contentValues.put("COM_CAIXA",comandasLiberadasModel.getCOM_CAIXA());
        contentValues.put("COM_DATA",comandasLiberadasModel.getCOM_DATA());
        contentValues.put("COM_TAXAENTREGA",comandasLiberadasModel.getCOM_TAXAENTREGA());
        contentValues.put("COM_COUVER",comandasLiberadasModel.getCOM_COUVER());
        contentValues.put("COM_MESA",comandasLiberadasModel.getCOM_MESA());
        contentValues.put("COM_IGNORATAXASERVICO",comandasLiberadasModel.getCOM_IGNORATAXASERVICO());
        contentValues.put("COM_NOMECLIENTE",comandasLiberadasModel.getCOM_NOMECLIENTE());
        contentValues.put("TOTAL_PROD",comandasLiberadasModel.getTOTAL_PROD());
        contentValues.put("VLR_TAXA_SERVICO",comandasLiberadasModel.getVLR_TAXA_SERVICO());
        contentValues.put("COUVER_ENTREGA",comandasLiberadasModel.getCOUVER_ENTREGA());
        contentValues.put("TOTAL_RECEBIDO",comandasLiberadasModel.getTOTAL_RECEBIDO());
        contentValues.put("TOTAL_COMANDA",comandasLiberadasModel.getTOTAL_COMANDA());
        contentValues.put("COM_VLRDESCONTO",comandasLiberadasModel.getCOM_VLRDESCONTO());
        contentValues.put("COM_PORDESCONTO",comandasLiberadasModel.getCOM_PORDESCONTO());
        contentValues.put("COM_VLRDESCONTOAPP",comandasLiberadasModel.getCOM_VLRDESCONTOAPP());
        contentValues.put("COM_PORDESCONTOAPP",comandasLiberadasModel.getCOM_PORDESCONTOAPP());
        contentValues.put("SINCRONIZADO",comandasLiberadasModel.getSINCRONIZADO());
        return contentValues;
    }
    public void inserirAll(List<ComandasLiberadasModel> listComandaLiberadasModel){
        if ((listComandaLiberadasModel != null) && (listComandaLiberadasModel.size()>0)) {

            for(int i=0; i<listComandaLiberadasModel.size(); i++) {
                ComandasLiberadasModel comandasLiberadasModel = listComandaLiberadasModel.get(i);
                inserir(comandasLiberadasModel);
            }
        }
    }

    public void excluirRegistro(ComandasLiberadasModel comandasLiberadasModel){
        String[] where = new String[4];
        where[0] = String.valueOf(comandasLiberadasModel.getCOM_EMPRESA());
        where[1] = String.valueOf(comandasLiberadasModel.getCOM_TIPOCOMANDA());
        where[2] = String.valueOf(comandasLiberadasModel.getCOM_COMANDA());
        where[3] = String.valueOf(comandasLiberadasModel.getCOM_SEQUENCIA());
        conexao.delete(Tabela, "COM_EMPRESA=? AND COM_TIPOCOMANDA=? AND COM_COMANDA=? AND COM_SEQUENCIA=?",where);
    }

    public void excluirTodos(){
        try{
            //String[] where = new String[1];
            //where[0] = String.valueOf("False");
            //conexao.delete(Tabela, " SINCRONIZADO = ? or SINCRONIZADO IS NULL ",where);
            conexao.delete(Tabela, null,null);
        } catch (SQLException ex){
            Log.e("SERVICO", "Seu erro: ", ex);
        }
    }

    public void alterar(ComandasLiberadasModel comandasLiberadasModel){
        ContentValues contentValues = getContentValue(comandasLiberadasModel);

        String[] where = new String[4];
        where[0] = String.valueOf(comandasLiberadasModel.getCOM_EMPRESA());
        where[1] = String.valueOf(comandasLiberadasModel.getCOM_TIPOCOMANDA());
        where[2] = String.valueOf(comandasLiberadasModel.getCOM_COMANDA());
        where[3] = String.valueOf(comandasLiberadasModel.getCOM_SEQUENCIA());
        try{
            conexao.update(Tabela,contentValues,
                    " COM_EMPRESA = ? AND COM_TIPOCOMANDA = ? AND COM_COMANDA = ? AND COM_SEQUENCIA = ? ",
                    where);
        } catch (SQLException ex) {
            AlertDialog.Builder dlg = new AlertDialog.Builder(context);
            dlg.setTitle(R.string.title_erro);
            dlg.setMessage(ex.getMessage());
            dlg.setNeutralButton(R.string.action_ok, null);
            dlg.show();
        }
    }

    public ComandasLiberadasModel retornaComandaLiberada(Cursor resultado){
        ComandasLiberadasModel comandasLiberadasModel = new ComandasLiberadasModel();
        comandasLiberadasModel.setCOM_EMPRESA(resultado.getString(resultado.getColumnIndexOrThrow("COM_EMPRESA")));
        comandasLiberadasModel.setCOM_TIPOCOMANDA(resultado.getString(resultado.getColumnIndexOrThrow("COM_TIPOCOMANDA")));
        comandasLiberadasModel.setCOM_COMANDA(resultado.getString(resultado.getColumnIndexOrThrow("COM_COMANDA")));
        comandasLiberadasModel.setCOM_SEQUENCIA(resultado.getInt(resultado.getColumnIndexOrThrow("COM_SEQUENCIA")));
        comandasLiberadasModel.setCOM_STATUS(resultado.getString(resultado.getColumnIndexOrThrow("COM_STATUS")));
        comandasLiberadasModel.setCOM_DATA(resultado.getString(resultado.getColumnIndexOrThrow("COM_DATA")));
        comandasLiberadasModel.setCOM_REPRESENTANTE(resultado.getString(resultado.getColumnIndexOrThrow("COM_REPRESENTANTE")));
        comandasLiberadasModel.setCOM_HORAABERTURA(resultado.getString(resultado.getColumnIndexOrThrow("COM_HORAABERTURA")));
        comandasLiberadasModel.setCOM_CAIXA(resultado.getString(resultado.getColumnIndexOrThrow("COM_CAIXA")));
        comandasLiberadasModel.setCOM_TAXASERVICO(resultado.getDouble(resultado.getColumnIndexOrThrow("COM_TAXASERVICO")));
        comandasLiberadasModel.setCOM_TAXAENTREGA(resultado.getDouble(resultado.getColumnIndexOrThrow("COM_TAXAENTREGA")));
        comandasLiberadasModel.setCOM_COUVER(resultado.getDouble(resultado.getColumnIndexOrThrow("COM_COUVER")));
        comandasLiberadasModel.setCOM_MESA(resultado.getInt(resultado.getColumnIndexOrThrow("COM_MESA")));
        comandasLiberadasModel.setCOM_IGNORATAXASERVICO(resultado.getString(resultado.getColumnIndexOrThrow("COM_IGNORATAXASERVICO")));
        comandasLiberadasModel.setCOM_NOMECLIENTE(resultado.getString(resultado.getColumnIndexOrThrow("COM_NOMECLIENTE")));
        comandasLiberadasModel.setTOTAL_PROD(resultado.getDouble(resultado.getColumnIndexOrThrow("TOTAL_PROD")));
        comandasLiberadasModel.setVLR_TAXA_SERVICO(resultado.getDouble(resultado.getColumnIndexOrThrow("VLR_TAXA_SERVICO")));
        comandasLiberadasModel.setCOUVER_ENTREGA(resultado.getDouble(resultado.getColumnIndexOrThrow("COUVER_ENTREGA")));
        comandasLiberadasModel.setTOTAL_RECEBIDO(resultado.getDouble(resultado.getColumnIndexOrThrow("TOTAL_RECEBIDO")));
        comandasLiberadasModel.setTOTAL_COMANDA(resultado.getDouble(resultado.getColumnIndexOrThrow("TOTAL_COMANDA")));
        comandasLiberadasModel.setCOM_VLRDESCONTO(resultado.getDouble(resultado.getColumnIndexOrThrow("COM_VLRDESCONTO")));
        comandasLiberadasModel.setCOM_PORDESCONTO(resultado.getDouble(resultado.getColumnIndexOrThrow("COM_PORDESCONTO")));
        comandasLiberadasModel.setCOM_VLRDESCONTOAPP(resultado.getDouble(resultado.getColumnIndexOrThrow("COM_VLRDESCONTOAPP")));
        comandasLiberadasModel.setCOM_PORDESCONTOAPP(resultado.getDouble(resultado.getColumnIndexOrThrow("COM_PORDESCONTOAPP")));
        comandasLiberadasModel.setSINCRONIZADO(resultado.getString(resultado.getColumnIndexOrThrow("SINCRONIZADO")));
        return comandasLiberadasModel;
    }

    public List<ComandasLiberadasModel> buscarTodos(String tipoComanda) {
        List<ComandasLiberadasModel> comandasLiberadasModels = new ArrayList<ComandasLiberadasModel>();

        StringBuilder sql = new StringBuilder();
        sql.append(SQLBase);
        if (tipoComanda.equals("")==false){
            sql.append(" WHERE COM_TIPOCOMANDA ='"+String.valueOf(tipoComanda)+"'");
        }
        sql.append(" ORDER BY COM_EMPRESA, COM_TIPOCOMANDA, COM_COMANDA, COM_SEQUENCIA");

        try {
            Cursor resultado = conexao.rawQuery(sql.toString(), new String[]{});

            if (resultado.getCount() > 0) {
                resultado.moveToFirst();

                do {
                    comandasLiberadasModels.add(retornaComandaLiberada(resultado));
                } while (resultado.moveToNext());
            }
        } catch (SQLException ex) {
            AlertDialog.Builder dlg = new AlertDialog.Builder(context);
            dlg.setTitle(R.string.title_erro);
            dlg.setMessage(ex.getMessage());
            dlg.setNeutralButton(R.string.action_ok, null);
            dlg.show();
        }


        return comandasLiberadasModels;
    }

    public boolean findExist(ComandasLiberadasModel comandasLiberadasModel){
        boolean retorno = false;
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT COUNT(*) AS REG FROM COMANDA ");
        sql.append(" WHERE COM_EMPRESA = '"+String.valueOf(comandasLiberadasModel.getCOM_EMPRESA())+"' ");
        sql.append(" AND COM_TIPOCOMANDA = '"+String.valueOf(comandasLiberadasModel.getCOM_TIPOCOMANDA())+"' ");
        sql.append(" AND COM_COMANDA = '"+String.valueOf(comandasLiberadasModel.getCOM_COMANDA())+"' ");
        sql.append(" AND COM_SEQUENCIA = "+String.valueOf(comandasLiberadasModel.getCOM_SEQUENCIA().toString()));

        try {
            Cursor resultado = conexao.rawQuery(sql.toString(), new String[]{});
            resultado.moveToFirst();
            if (resultado.getInt(resultado.getColumnIndexOrThrow("REG")) > 0) {
                retorno = true;
            }
        } catch (SQLException ex) {
            AlertDialog.Builder dlg = new AlertDialog.Builder(context);
            dlg.setTitle(R.string.title_erro);
            dlg.setMessage(ex.getMessage());
            dlg.setNeutralButton(R.string.action_ok, null);
            dlg.show();
        }


        return retorno;
    }
}
