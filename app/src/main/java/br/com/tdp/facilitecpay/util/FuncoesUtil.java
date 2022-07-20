package br.com.tdp.facilitecpay.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import br.com.tdp.facilitecpay.R;
import br.com.tdp.facilitecpay.database.PagamentosComandaDAO;
import br.com.tdp.facilitecpay.model.ComandasLiberadasModel;
import br.com.tdp.facilitecpay.model.PagamentosComandaModel;

public class FuncoesUtil  {

    public void showSoftKeyboard(View view) {
        if (view != null && view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.SHOW_FORCED);
        }
    }

    public void hideSoftKeyboard(View view) {
        if (view != null && view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.RESULT_UNCHANGED_SHOWN,InputMethodManager.RESULT_UNCHANGED_SHOWN);
        }
    }

    public Boolean showDialogCobranca(View view, ComandasLiberadasModel comandaSelecionada, PagamentosComandaDAO pagamentosComandaDAO, String cobranca, Double valor,DoAtualizarCobrancas callback) {
        final Boolean[] descValor = {true};
        final Boolean[] retorno = {false};
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        AlertDialog.Builder dlgR = new AlertDialog.Builder(view.getContext());
        View viewDialog = inflater.inflate(R.layout.layout_dialog_cobranca, null);
        dlgR.setView(viewDialog);
        AlertDialog dialog = dlgR.create();
        dialog.show();
        dialog.getWindow().setLayout(950, 900);

        NumberFormat formatter = new DecimalFormat("0.00");
        TextView tituloCobranca = (TextView) viewDialog.findViewById(R.id.tvTituloCobranca);
        tituloCobranca.setText(cobranca);

        EditText etValorCobranca = (EditText) viewDialog.findViewById(R.id.etValorCobranca);

        if (valor > 0){
            etValorCobranca.setText(formatter.format(valor).toString().replace(",","."));
        } else {
            etValorCobranca.setText(formatter.format(comandaSelecionada.getTOTAL_COMANDA()).toString().replace(",","."));
        }

        etValorCobranca.setFocusable(true);
        etValorCobranca.selectAll();
        etValorCobranca.requestFocus();

        showSoftKeyboard(viewDialog);

        Button btCancelar = (Button) viewDialog.findViewById(R.id.bCancelarCobranca);
        btCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard(viewDialog);
                dialog.dismiss();
            }
        });

        Button btConfirmar = (Button) viewDialog.findViewById(R.id.bConfirmarCobranca);
        btConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Boolean[] finalizar = {true};
                final Boolean[] incluir = {true};
                PagamentosComandaModel pagamentosComandaModel = pagamentosComandaDAO.buscarPagamento(comandaSelecionada.getCOM_EMPRESA(),
                        comandaSelecionada.getCOM_TIPOCOMANDA(), comandaSelecionada.getCOM_COMANDA(), comandaSelecionada.getCOM_SEQUENCIA(),
                        cobranca);

                if (pagamentosComandaModel == null){
                    pagamentosComandaModel = new PagamentosComandaModel();
                    pagamentosComandaModel.setCOMV_EMPRESA(comandaSelecionada.getCOM_EMPRESA());
                    pagamentosComandaModel.setCOMV_TIPOCOMANDA(comandaSelecionada.getCOM_TIPOCOMANDA());
                    pagamentosComandaModel.setCOMV_COMANDA(comandaSelecionada.getCOM_COMANDA());
                    pagamentosComandaModel.setCOMV_SEQUENCIA(comandaSelecionada.getCOM_SEQUENCIA());
                    pagamentosComandaModel.setCOMV_COBRANCA(cobranca);
                    pagamentosComandaModel.setCOMV_VALOR(0.0);
                    pagamentosComandaModel.setCOMV_VALORAPP(0.0);
                } else{
                    incluir[0] = false;
                }
                if(etValorCobranca.getText().toString().equals("")==true){
                    etValorCobranca.setText("0.00");
                }
                if (etValorCobranca.getText().toString().equals(",") || etValorCobranca.getText().toString().equals(".")){
                    etValorCobranca.setText("0.00");
                }

                String valor = formatter.format(Float.parseFloat(etValorCobranca.getText().toString()));
                valor = valor.toString().replace(",", ".");
                pagamentosComandaModel.setCOMV_VALORAPP(Double.parseDouble(valor));
                if (pagamentosComandaModel.getCOMV_VALORAPP() > 0) {
                    if (incluir[0] == true) {
                        pagamentosComandaDAO.inserir(pagamentosComandaModel);
                    } else {
                        pagamentosComandaDAO.alterar(pagamentosComandaModel);
                    }
                } else {
                    if (pagamentosComandaModel.getCOMV_VALOR() == 0 && incluir[0] != true){
                        pagamentosComandaDAO.excluirRegistroComanda(pagamentosComandaModel.getCOMV_EMPRESA(),
                                pagamentosComandaModel.getCOMV_TIPOCOMANDA(), pagamentosComandaModel.getCOMV_COMANDA(),
                                pagamentosComandaModel.getCOMV_SEQUENCIA(), pagamentosComandaModel.getCOMV_COBRANCA());
                    } else if (incluir[0] != true) {
                        pagamentosComandaDAO.alterar(pagamentosComandaModel);
                    }

                }
                if (finalizar[0]) {
                    hideSoftKeyboard(viewDialog);
                    callback.onConcluir();
                    dialog.dismiss();
                }
            }
        });
        return retorno[0];
    }
}
