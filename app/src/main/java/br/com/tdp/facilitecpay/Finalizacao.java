package br.com.tdp.facilitecpay;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import br.com.tdp.facilitecpay.adapter.ListaMeiosPagamentosComandaRecycleView;
import br.com.tdp.facilitecpay.database.Conexao;
import br.com.tdp.facilitecpay.database.PagamentosComandaDAO;
import br.com.tdp.facilitecpay.databinding.ActivityFinalizacaoBinding;
import br.com.tdp.facilitecpay.model.ComandasLiberadasModel;
import br.com.tdp.facilitecpay.model.PagamentosComandaModel;

public class Finalizacao extends AppCompatActivity {
    private RecyclerView lvPagamentosComanda;
    private ActivityFinalizacaoBinding binding;
    private PagamentosComandaDAO pagamentosComandaDAO;
    private Conexao conexao = new Conexao();
    private ComandasLiberadasModel comandaSelecionada;
    private ListaMeiosPagamentosComandaRecycleView listaComandasAdapterRecyclerView;
    private TextView tvTotalComanda;
    private TextView tvDesconto;
    private TextView tvAReceber;
    private TextView tvStatus;
    private TextView tvNumeroComanda;
    private TextView tvNomeCliente;
    private TextView tvMesa;
    private RelativeLayout card_constraint;
    private ConstraintLayout vStatus;
    private Button btVoltar;

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

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(binding.getRoot().getContext(),LinearLayoutManager.VERTICAL,false);
        lvPagamentosComanda = (RecyclerView)findViewById(R.id.lvListaCobrancasPagamento);
        lvPagamentosComanda.setHasFixedSize(true);
        lvPagamentosComanda.setLayoutManager(linearLayoutManager);


        tvTotalComanda = (TextView)findViewById(R.id.tvValorTotalComanda);
        tvDesconto     = (TextView)findViewById(R.id.tvTotalDesconto);
        tvAReceber     = (TextView)findViewById(R.id.tvAReceber);
        card_constraint = (RelativeLayout)findViewById(R.id.card_constraint_selecionada);
        tvStatus        = (TextView)findViewById(R.id.tvStatusSelecionada);
        vStatus         = (ConstraintLayout)findViewById(R.id.llStatusSelecionada);
        tvNomeCliente   = (TextView)findViewById(R.id.tvClienteSelecionada);
        tvNumeroComanda = (TextView)findViewById(R.id.tvNumeroComandaSelecionada);
        tvMesa          = (TextView)findViewById(R.id.tvMesaSelecionada);

        btVoltar        = (Button)findViewById(R.id.bFinalizacaoVoltar);
        btVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        carregarPagamentos();
    }
    private void carregaParametro() {
        Bundle bundle = getIntent().getExtras();

        if ((bundle != null) && (bundle.containsKey("COMANDA"))) {
            comandaSelecionada = (ComandasLiberadasModel)bundle.getSerializable("COMANDA");

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
        carregaParametro();
        List<PagamentosComandaModel> listPagamentosComandaModels = pagamentosComandaDAO.buscarTodos(
                comandaSelecionada.getCOM_EMPRESA(), comandaSelecionada.getCOM_TIPOCOMANDA(),
                comandaSelecionada.getCOM_COMANDA(), comandaSelecionada.getCOM_SEQUENCIA());
        if (listPagamentosComandaModels.size()>0) {
            listaComandasAdapterRecyclerView = new ListaMeiosPagamentosComandaRecycleView(listPagamentosComandaModels);
            lvPagamentosComanda.setAdapter(null);
            lvPagamentosComanda.setAdapter(listaComandasAdapterRecyclerView);
        } else {
            lvPagamentosComanda.setVisibility(View.GONE);
        }
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
        Double totalComanda = comandaSelecionada.getTOTAL_PROD()+comandaSelecionada.getCOM_TAXASERVICO()+comandaSelecionada.getCOUVER_ENTREGA();
        tvTotalComanda.setText("R$ "+formatter.format(totalComanda));
        tvAReceber.setText("R$ "+formatter.format(comandaSelecionada.getTOTAL_COMANDA()));
        tvDesconto.setText("( - ) 0,00");
    }
}