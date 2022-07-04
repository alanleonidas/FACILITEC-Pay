package br.com.tdp.facilitecpay;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.database.SQLException;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import br.com.tdp.facilitecpay.adapter.ListaComandasAdapterRecyclerView;
import br.com.tdp.facilitecpay.database.Conexao;
import br.com.tdp.facilitecpay.database.ConfiguracaoDAO;
import br.com.tdp.facilitecpay.database.ListaComandasDAO;
import br.com.tdp.facilitecpay.database.RepreseDAO;
import br.com.tdp.facilitecpay.database.TipoComandaDAO;
import br.com.tdp.facilitecpay.databinding.ActivityListaComandasBinding;
import br.com.tdp.facilitecpay.model.ComandasLiberadasModel;
import br.com.tdp.facilitecpay.model.ConfiguracaoModel;
import br.com.tdp.facilitecpay.model.RepreseModel;
import br.com.tdp.facilitecpay.model.TipoComandaModel;
import br.com.tdp.facilitecpay.util.OpcaoSinc;
import br.com.tdp.facilitecpay.webservice.Sincronizacao;

public class ListaComandas extends AppCompatActivity {
    private RecyclerView lvListaComandas;
    private ActivityListaComandasBinding binding;
    private SwipeRefreshLayout swipeContainer;
    private TextView tvTotalComandas;
    private OpcaoSinc sincAtual;
    private int contador = -1;
    private ConfiguracaoDAO configuracaoDAO;
    private TipoComandaDAO tipoComandaDAO;
    private ListaComandasDAO listaComandasDAO;
    private Conexao conexao = new Conexao();
    private Spinner spinnerTipoComanda;
    private TipoComandaModel tipoComandaSelecionado;
    private ListaComandasAdapterRecyclerView listaComandasAdapterRecyclerView;
    private Sincronizacao sincronizacao = new Sincronizacao();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Lista de Comandas");

        binding = ActivityListaComandasBinding.inflate(getLayoutInflater());

        setContentView(R.layout.activity_lista_comandas);

        tipoComandaDAO = new TipoComandaDAO(conexao.retornaConexao(binding.getRoot().getContext(),binding.getRoot()));
        listaComandasDAO = new ListaComandasDAO(conexao.retornaConexao(binding.getRoot().getContext(),binding.getRoot()));
        listaComandasDAO.setContext(binding.getRoot().getContext());

        tvTotalComandas = (TextView)findViewById(R.id.TotalComandas);
        tvTotalComandas.setText("0 Comandas");
        spinnerTipoComanda = (Spinner)findViewById(R.id.spinnerTipoComanda);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(binding.getRoot().getContext(),LinearLayoutManager.VERTICAL,false);
        lvListaComandas = (RecyclerView) findViewById(R.id.lvComandasParaFinalizacao);
        lvListaComandas.setHasFixedSize(true);
        lvListaComandas.setLayoutManager(linearLayoutManager);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_red_dark,
                android.R.color.darker_gray,
                android.R.color.holo_red_dark,
                android.R.color.darker_gray,
                android.R.color.holo_red_dark,
                android.R.color.darker_gray);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_listacomandas, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void refresh(){
        loadComandasPorTipoComanda();
        swipeContainer.setRefreshing(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_item_refresh:
                carregarDadosBD();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume(){
        super.onResume();
        contador++;
        onVerificaConfiguracao();
    }

    public void onReconectar(){
        onVerificaConfiguracao();
    }
    private boolean onVerificaConfiguracao() {
        boolean Retorno = true;
        try {
            if (configuracaoDAO == null) {
                configuracaoDAO = new ConfiguracaoDAO(conexao.retornaConexao(binding.getRoot().getContext(), binding.getRoot()));
                configuracaoDAO.setContext(binding.getRoot().getContext());
            }
            //if (contador > 0) {
            carregarDadosBD();
            //}
        } catch (SQLException ex){
            AlertDialog.Builder dlg = new AlertDialog.Builder(binding.getRoot().getContext());
            dlg.setTitle(R.string.title_erro);
            dlg.setMessage(ex.getMessage());
            dlg.setNeutralButton(R.string.action_ok,null);
            dlg.show();
            Retorno = false;
        }
        return Retorno;
    }

    private void carregarDadosBD(){
        List<ConfiguracaoModel> configuracaoList = configuracaoDAO.buscarTodos();
        if (configuracaoList.size() == 0) {
            finish();
        } else {
            onCarregarDados();
        }
    }

    private void onLoadComponentes(){
        List<TipoComandaModel> listaTipoComanda = tipoComandaDAO.buscarTodos();
        List<String> tipocomanda = new ArrayList<String>();

        if ((listaTipoComanda != null) && (listaTipoComanda.size()>0)) {
            for(int i=0; i<listaTipoComanda.size(); i++) {
                tipocomanda.add(listaTipoComanda.get(i).getTCOM_BOTAOMOBILE());
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, tipocomanda);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipoComanda.setAdapter(adapter);
        spinnerTipoComanda.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                loadComandasPorTipoComanda();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void onCarregarDados(){
        sincAtual = OpcaoSinc.TipoComanda;
        sincronizacao.Sincronizacao(binding.getRoot().getContext(), binding.getRoot(), sincAtual, null, this::onPosExecute, this::onException);
    }

    public void onPosExecute(JSONObject object)  {
        switch (sincAtual) {
            case TipoComanda:
                onLoadComponentes();
                sincAtual = OpcaoSinc.ComandasFinalizacao;
                sincronizacao.Sincronizacao(binding.getRoot().getContext(), binding.getRoot(), sincAtual, null, this::onPosExecute, this::onException);
                break;
            case ComandasFinalizacao:
                loadComandasPorTipoComanda();
                break;
        }
    }

    public void loadComandasPorTipoComanda(){
        if (spinnerTipoComanda.getSelectedItem().toString().equals("")==false){
            TipoComandaModel tipoComandaModel = tipoComandaDAO.buscarTipoComandaModel(spinnerTipoComanda.getSelectedItem().toString());
            carregarComandas(tipoComandaModel.getTCOM_CODIGO());
        }
    }

    public void carregarComandas(String tipoComanda){
        List<ComandasLiberadasModel> listComandasLiberadasModel = listaComandasDAO.buscarTodos(tipoComanda);
        tvTotalComandas.setText(listComandasLiberadasModel.size()+" Comandas");
        listaComandasAdapterRecyclerView = new ListaComandasAdapterRecyclerView(listComandasLiberadasModel);
        lvListaComandas.setAdapter(null);
        lvListaComandas.setAdapter(listaComandasAdapterRecyclerView);
    }

    public void onException(int mensagem){

        AlertDialog.Builder dlgR = new AlertDialog.Builder(binding.getRoot().getContext());
        dlgR.setTitle(R.string.title_erro);
        dlgR.setMessage(mensagem);
        dlgR.setNeutralButton(R.string.action_ok,null);
        dlgR.show();

    }
}