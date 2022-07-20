package br.com.tdp.facilitecpay;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Typeface;
import android.icu.lang.UCharacter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import br.com.tdp.facilitecpay.adapter.ListaCobrancasAdapterRecyclerView;
import br.com.tdp.facilitecpay.adapter.ListaMeiosPagamentosComandaRecycleView;
import br.com.tdp.facilitecpay.database.CobraDAO;
import br.com.tdp.facilitecpay.database.Conexao;
import br.com.tdp.facilitecpay.database.ListaComandasDAO;
import br.com.tdp.facilitecpay.database.PagamentosComandaDAO;
import br.com.tdp.facilitecpay.databinding.ActivityFinalizacaoBinding;
import br.com.tdp.facilitecpay.model.CobraModel;
import br.com.tdp.facilitecpay.model.ComandasLiberadasModel;
import br.com.tdp.facilitecpay.model.PagamentosComandaModel;
import br.com.tdp.facilitecpay.util.FuncoesUtil;
import br.com.tdp.facilitecpay.util.OpcaoSinc;
import br.com.tdp.facilitecpay.webservice.Sincronizacao;

public class Finalizacao extends AppCompatActivity {
    private RecyclerView lvPagamentosComanda;
    private OpcaoSinc sincAtual;
    private RecyclerView lvCobrancas;
    private ActivityFinalizacaoBinding binding;
    private ListaComandasDAO listaComandasDAO;
    private PagamentosComandaDAO pagamentosComandaDAO;
    private CobraDAO cobraDAO;
    private Conexao conexao = new Conexao();
    private ComandasLiberadasModel comandaSelecionada;
    private ListaMeiosPagamentosComandaRecycleView listaComandasAdapterRecyclerView;
    private ListaCobrancasAdapterRecyclerView listaCobrancasAdapterRecyclerView;
    private TextView tvTotalComanda;
    private TextView tvDesconto;
    private TextView tvAReceber;
    private TextView tvStatus;
    private TextView tvNumeroComanda;
    private TextView tvNomeCliente;
    private TextView tvMesa;
    private TextView tvTSaldo;
    private RelativeLayout card_constraint;
    private ConstraintLayout vStatus;
    private Button btVoltar;
    private Button btConformar;
    private Sincronizacao sincronizacao = new Sincronizacao();
    private double valorDesconto = 0.00;
    private double valorRecebido = 0.00;
    private double troco = 0.00;
    private FuncoesUtil funcoes = new FuncoesUtil();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_finalizacao);
        binding = ActivityFinalizacaoBinding.inflate(getLayoutInflater());
        pagamentosComandaDAO = new PagamentosComandaDAO(conexao.retornaConexao(binding.getRoot().getContext(),binding.getRoot()));
        pagamentosComandaDAO.setContext(binding.getRoot().getContext());

        listaComandasDAO = new ListaComandasDAO(conexao.retornaConexao(binding.getRoot().getContext(),binding.getRoot()));
        listaComandasDAO.setContext(binding.getRoot().getContext());

        cobraDAO = new CobraDAO(conexao.retornaConexao(binding.getRoot().getContext(),binding.getRoot()));
        cobraDAO.setContext(binding.getRoot().getContext());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(binding.getRoot().getContext(),LinearLayoutManager.VERTICAL,false);
        lvPagamentosComanda = (RecyclerView)findViewById(R.id.lvListaCobrancasPagamento);
        lvPagamentosComanda.setHasFixedSize(true);
        lvPagamentosComanda.setLayoutManager(linearLayoutManager);

        LinearLayoutManager linearLayoutManagerH = new LinearLayoutManager(binding.getRoot().getContext(),LinearLayoutManager.HORIZONTAL,false);
        lvCobrancas = (RecyclerView)findViewById(R.id.lvFinalizadoras);
        lvCobrancas.setHasFixedSize(true);
        lvCobrancas.setLayoutManager(linearLayoutManagerH);

        tvTotalComanda = (TextView)findViewById(R.id.tvValorTotalComanda);
        tvDesconto     = (TextView)findViewById(R.id.tvTotalDesconto);
        tvAReceber     = (TextView)findViewById(R.id.tvAReceber);
        card_constraint = (RelativeLayout)findViewById(R.id.card_constraint_selecionada);
        tvStatus        = (TextView)findViewById(R.id.tvStatusSelecionada);
        vStatus         = (ConstraintLayout)findViewById(R.id.llStatusSelecionada);
        tvNomeCliente   = (TextView)findViewById(R.id.tvClienteSelecionada);
        tvNumeroComanda = (TextView)findViewById(R.id.tvNumeroComandaSelecionada);
        tvMesa          = (TextView)findViewById(R.id.tvMesaSelecionada);

        tvTSaldo        = (TextView)findViewById(R.id.tvTSaldo);

        btVoltar        = (Button)findViewById(R.id.bFinalizacaoVoltar);
        btVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btConformar     = (Button)findViewById(R.id.bFinalizacaoConfirmar);
        btConformar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    onConfirmar();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        carregaParametro();
        carregarPagamentos();
    }

    private void onConfirmar() throws JSONException {
        List<PagamentosComandaModel> listPagamentosComandaModels = pagamentosComandaDAO.buscarTodos(
                comandaSelecionada.getCOM_EMPRESA(), comandaSelecionada.getCOM_TIPOCOMANDA(),
                comandaSelecionada.getCOM_COMANDA(), comandaSelecionada.getCOM_SEQUENCIA());
        if (listPagamentosComandaModels.size()>0) {
            //comandaSelecionada.setSINCRONIZADO("False");
            listaComandasDAO.alterar(comandaSelecionada);
            JSONObject sincComanda = new JSONObject();
            sincComanda.put("COM_EMPRESA", comandaSelecionada.getCOM_EMPRESA());
            sincComanda.put("COM_TIPOCOMANDA", comandaSelecionada.getCOM_TIPOCOMANDA());
            sincComanda.put("COM_COMANDA", comandaSelecionada.getCOM_COMANDA());
            sincComanda.put("COM_SEQUENCIA", comandaSelecionada.getCOM_SEQUENCIA());
            sincComanda.put("COM_VLRDESCONTO", comandaSelecionada.getCOM_VLRDESCONTOAPP());
            sincComanda.put("COM_STATUS", comandaSelecionada.getCOM_STATUS());
            sincComanda.put("COM_CAIXA", comandaSelecionada.getCOM_CAIXA());
            sincComanda.put("COM_TROCO", troco);

            JSONArray pagamamentos = new JSONArray();
            for (int i = 0; i < listPagamentosComandaModels.size(); i++) {
                JSONObject pagamento = new JSONObject();
                pagamento.put("COMV_COBRANCA", listPagamentosComandaModels.get(i).getCOMV_COBRANCA());
                pagamento.put("COMV_VALOR", listPagamentosComandaModels.get(i).getCOMV_VALORAPP());
                pagamamentos.put(pagamento);
            }
            sincComanda.put("COMANDAV", pagamamentos);
            sincAtual = OpcaoSinc.postFinalizarComanda;
            sincronizacao.Sincronizacao(binding.getRoot().getContext(), binding.getRoot(), sincAtual, sincComanda, this::onPosExecute, this::onException);
        } else {
            Toast.makeText(binding.getRoot().getContext(), "Não existe pagamento efetuado.", Toast.LENGTH_LONG).show();
        }
    }

    private void carregaParametro() {
        Bundle bundle = getIntent().getExtras();

        if ((bundle != null) && (bundle.containsKey("COMANDA"))) {
            comandaSelecionada = (ComandasLiberadasModel)bundle.getSerializable("COMANDA");
            onCarregarFinalizadoras();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_finalizacao, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void carregarPagamentos(){
        List<PagamentosComandaModel> listPagamentosComandaModels = pagamentosComandaDAO.buscarTodos(
                comandaSelecionada.getCOM_EMPRESA(), comandaSelecionada.getCOM_TIPOCOMANDA(),
                comandaSelecionada.getCOM_COMANDA(), comandaSelecionada.getCOM_SEQUENCIA());
        if (listPagamentosComandaModels.size()>0) {
            listaComandasAdapterRecyclerView = new ListaMeiosPagamentosComandaRecycleView(listPagamentosComandaModels, comandaSelecionada, pagamentosComandaDAO, this::onConcluir);
            lvPagamentosComanda.setAdapter(null);
            lvPagamentosComanda.setAdapter(listaComandasAdapterRecyclerView);
            lvPagamentosComanda.setVisibility(View.VISIBLE);
        } else {
            lvPagamentosComanda.setVisibility(View.GONE);
            lvPagamentosComanda.setVisibility(View.INVISIBLE);
        }
        valorRecebido = 0.0;
        for(int i=0; i<listPagamentosComandaModels.size(); i++) {
            valorRecebido = valorRecebido + listPagamentosComandaModels.get(i).getCOMV_VALOR() + listPagamentosComandaModels.get(i).getCOMV_VALORAPP();
        }
        onCarregarInformacoesCampos();
    }

    private void onCarregarInformacoesCampos(){
        int numeroComanda = Integer.parseInt(comandaSelecionada.getCOM_COMANDA());
        tvNumeroComanda.setText(Integer.toString(numeroComanda));
        if (comandaSelecionada.getCOM_MESA() > 0) {
            tvMesa.setText("Mesa "+Integer.toString(comandaSelecionada.getCOM_MESA()));
        } else{
            tvMesa.setVisibility(View.INVISIBLE);
        }

        if (comandaSelecionada.getCOM_STATUS().equalsIgnoreCase("P")){
            tvStatus.setText("Recebimento");
            vStatus.setBackgroundResource(R.drawable.borda_status_recebimento);
            card_constraint.setBackgroundResource(R.drawable.borda_recebimento);
        } else{
            tvStatus.setText("Ocupada");
            vStatus.setBackgroundResource(R.drawable.borda_status_ocupado);
            card_constraint.setBackgroundResource(R.drawable.borda_aberto);
        }
        tvNomeCliente.setText(comandaSelecionada.getCOM_NOMECLIENTE());
        NumberFormat formatter = new DecimalFormat("0.00");
        //Double totalComanda = comandaSelecionada.getTOTAL_COMANDA();
        Double totalComanda = comandaSelecionada.getTOTAL_PROD()+
                              comandaSelecionada.getVLR_TAXA_SERVICO()+
                              comandaSelecionada.getCOUVER_ENTREGA();
        tvTotalComanda.setText("R$ "+formatter.format(totalComanda));
        Double saldo = Double.parseDouble(formatter.format(totalComanda - valorRecebido -
                comandaSelecionada.getCOM_VLRDESCONTO()-comandaSelecionada.getCOM_VLRDESCONTOAPP()).replace(",","."));
        if (saldo < 0.0){
            saldo = saldo *-1;
            troco = saldo;
            tvTSaldo.setText(R.string.troco);
            comandaSelecionada.setCOM_STATUS("F");
        } else {
            if (saldo == -0.0){
                saldo = 0.0;
            }
            if (saldo > 0){
               comandaSelecionada.setCOM_STATUS("P");
            } else {
                comandaSelecionada.setCOM_STATUS("F");
            }
            tvTSaldo.setText(R.string.a_receber);
        }
        tvAReceber.setText("R$ "+formatter.format(saldo));
        comandaSelecionada.setTOTAL_COMANDA(saldo);
        tvDesconto.setText("( - ) "+formatter.format(comandaSelecionada.getCOM_VLRDESCONTO()+ comandaSelecionada.getCOM_VLRDESCONTOAPP()));
    }

    private void onCarregarFinalizadoras(){
        sincAtual = OpcaoSinc.getCobrancas;
        sincronizacao.Sincronizacao(binding.getRoot().getContext(), binding.getRoot(), sincAtual, null, this::onPosExecute, this::onException);
    }

    public void onPosExecute(JSONObject object)  {
        switch (sincAtual) {
            case getCobrancas:
                List<CobraModel> listCobraModels = cobraDAO.buscarTodos();
                if (listCobraModels.size()>0){
                    listaCobrancasAdapterRecyclerView = new ListaCobrancasAdapterRecyclerView(listCobraModels, comandaSelecionada, pagamentosComandaDAO, this::onConcluir);
                    lvCobrancas.setAdapter(null);
                    lvCobrancas.setAdapter(listaCobrancasAdapterRecyclerView);
                }
                break;
            case postFinalizarComanda:
                //comandaSelecionada.setSINCRONIZADO("True");
                //listaComandasDAO.alterar(comandaSelecionada);
                finish();
        }
    }

    public void onConcluir() {
        carregarPagamentos();
    }

    public void onException(int mensagem){

        AlertDialog.Builder dlgR = new AlertDialog.Builder(binding.getRoot().getContext());
        dlgR.setTitle(R.string.title_erro);
        dlgR.setMessage(mensagem);
        dlgR.setNeutralButton(R.string.action_ok,null);
        dlgR.show();

    }

    public void showDialogDesconto(View view) {
        if (comandaSelecionada.getCOM_VLRDESCONTO() == 0) {
            AlertDialog.Builder dlgR = new AlertDialog.Builder(binding.getRoot().getContext());
            View viewDialog = getLayoutInflater().inflate(R.layout.layout_dialog_desconto, null);
            dlgR.setView(viewDialog);
            AlertDialog dialog = dlgR.create();

            TextView tituloPorc = (TextView) viewDialog.findViewById(R.id.tvTituloDescPorc);
            TextView tituloValor = (TextView) viewDialog.findViewById(R.id.tvTituloDescValor);
            LinearLayoutCompat llPorc = (LinearLayoutCompat) viewDialog.findViewById(R.id.llDescPorc);
            LinearLayoutCompat llValor = (LinearLayoutCompat) viewDialog.findViewById(R.id.llDescValor);

            EditText etValorDesconto = (EditText) viewDialog.findViewById(R.id.etValorDesconto);
            etValorDesconto.setFocusable(true);
            etValorDesconto.setFocusableInTouchMode(true);
            etValorDesconto.requestFocus();
            funcoes.showSoftKeyboard(etValorDesconto);
            dialog.show();
            dialog.getWindow().setLayout(950, 1200);
            final Boolean[] descValor = {true};


            Button btConfirmar = (Button) viewDialog.findViewById(R.id.bConfirmarDesconto);
            btConfirmar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NumberFormat formatter = new DecimalFormat("0.00");
                    if(etValorDesconto.getText().toString().equals("")==true){
                        etValorDesconto.setText("0.00");
                    }
                    if (etValorDesconto.getText().toString().equals(",") || etValorDesconto.getText().toString().equals(".")){
                        etValorDesconto.setText("0.00");
                    }
                    String valor = formatter.format(Float.parseFloat(etValorDesconto.getText().toString()));
                    valor = valor.toString().replace(",", ".");
                    valorDesconto = Double.parseDouble(valor);
                    final Boolean[] finalizar = {true};

                    if (descValor[0] == false) {
                        if (valorDesconto >= 99) {
                            finalizar[0] = false;
                            Toast.makeText(view.getContext(), "Porcentagem de desconto deve ser inferior a 99 % ", Toast.LENGTH_LONG).show();
                            etValorDesconto.setText("");
                            etValorDesconto.selectAll();
                            etValorDesconto.setFocusable(true);
                        } else {
                            valorDesconto = ((comandaSelecionada.getTOTAL_COMANDA() * valorDesconto) / 100);
                        }
                    }

                    if ((comandaSelecionada.getTOTAL_COMANDA() < valorDesconto)) {
                        valorDesconto = 0;
                        finalizar[0] = false;
                        Toast.makeText(view.getContext(), "Desconto superior ao Saldo Aberto", Toast.LENGTH_LONG).show();
                        etValorDesconto.setText("");
                        etValorDesconto.selectAll();
                        etValorDesconto.setFocusable(true);
                    }

                    if (finalizar[0]) {
                        funcoes.hideSoftKeyboard(etValorDesconto);
                        comandaSelecionada.setCOM_VLRDESCONTOAPP(valorDesconto);
                        listaComandasDAO.alterar(comandaSelecionada);
                        dialog.dismiss();
                        onCarregarInformacoesCampos();
                    }

                }
            });
            Button btCancelar = (Button) viewDialog.findViewById(R.id.bCancelarDesconto);
            btCancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    funcoes.hideSoftKeyboard(etValorDesconto);
                    Toast.makeText(view.getContext(), "Cancelando", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }
            });


            CardView cvDescontoPorPorcentagem = (CardView) viewDialog.findViewById(R.id.cvDescontoPorPorcentagem);
            cvDescontoPorPorcentagem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    etValorDesconto.setInputType(UCharacter.NumericType.DIGIT);
                    llPorc.setBackgroundResource(R.drawable.container_meios_pagamentos_comfoco);
                    llValor.setBackgroundResource(R.drawable.container_meios_pagamentos_semfoco);
                    tituloValor.setTypeface(null, Typeface.NORMAL);
                    tituloPorc.setTypeface(null, Typeface.BOLD);
                    descValor[0] = false;
                    etValorDesconto.requestFocus();
                    etValorDesconto.setClickable(true);
                    funcoes.showSoftKeyboard(etValorDesconto);
                }
            });

            CardView cvDescontoPorValor = (CardView) viewDialog.findViewById(R.id.cvDescontoPorValor);

            cvDescontoPorValor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    descValor[0] = true;
                    etValorDesconto.setInputType(UCharacter.NumericType.NUMERIC);
                    llPorc.setBackgroundResource(R.drawable.container_meios_pagamentos_semfoco);
                    llValor.setBackgroundResource(R.drawable.container_meios_pagamentos_comfoco);
                    tituloValor.setTypeface(null, Typeface.BOLD);
                    tituloPorc.setTypeface(null, Typeface.NORMAL);
                    etValorDesconto.requestFocus();
                    etValorDesconto.setClickable(true);
                    funcoes.showSoftKeyboard(etValorDesconto);
                }
            });
        } else {
            Toast.makeText(view.getContext(), "Desconto aplicado no módulo de Gestão, o mesmo não pode ser alterado!", Toast.LENGTH_LONG).show();
        }
    }
}