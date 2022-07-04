package br.com.tdp.facilitecpay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.database.SQLException;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import org.json.JSONObject;

import java.util.List;
import java.util.Objects;

import br.com.tdp.facilitecpay.database.Conexao;
import br.com.tdp.facilitecpay.database.ConfiguracaoDAO;
import br.com.tdp.facilitecpay.databinding.ActivityConfiguracaoBinding;
import br.com.tdp.facilitecpay.model.ConfiguracaoModel;
import br.com.tdp.facilitecpay.webservice.ClientComunicacaoServer;
import br.com.tdp.facilitecpay.webservice.DoComunicacao;
import br.com.tdp.facilitecpay.webservice.DoException;
import br.com.tdp.facilitecpay.webservice.UrlWebService;

public class Configuracao extends AppCompatActivity  implements DoComunicacao, DoException {
    private ActivityConfiguracaoBinding binding;
    private ConfiguracaoDAO configuracaoDAO;
    private ConfiguracaoModel configuracao;
    private Conexao conexao = new Conexao();
    private int contador = 0;
    private Boolean inclusao = true;
    private EditText edtIP;
    private EditText edtPorta;
    private Spinner spinner;
    private Button btnGravar;
    private Button btnTestar;
    private Button btnSincronizar;
    private ProgressDialog barraProgresso;
    private JSONObject retorno;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        binding = ActivityConfiguracaoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Configurações");
        setContentView(R.layout.activity_configuracao);

        edtIP = (EditText) findViewById(R.id.editIPServidor);
        edtPorta = (EditText) findViewById(R.id.editPortaServidor);
        spinner = (Spinner) findViewById(R.id.spinnerIntegrador);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                        R.array.integradores, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        btnGravar = (Button) findViewById(R.id.btnGravarParâmetros);
        btnGravar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { onGravarConfiguracao(); }
        });
        btnTestar = (Button) findViewById(R.id.btnTestarConexao);
        btnTestar.setEnabled(false);
        btnTestar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { onTestarConexao(); }
        });
        btnSincronizar = (Button) findViewById(R.id.btnSincronizarDados);
        btnSincronizar.setEnabled(false);
        btnSincronizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { onSincronizarDados(); }
        });
        if (onVerificaConfiguracao()) {
            carregarDadosBD();
        }
    }

    private void onSincronizarDados() {

    }

    public void onPosExecute(@NonNull JSONObject retorno){
        if (retorno.toString() != ""){
            AlertDialog.Builder dlg = new AlertDialog.Builder(binding.getRoot().getContext());
            dlg.setTitle(R.string.title_informacao);
            dlg.setMessage(R.string.comunicacao_sucesso);
            dlg.setNeutralButton(R.string.action_ok,null);
            dlg.setIcon(R.drawable.ic_baseline_check_circle_24);
            dlg.show();
        }
    }

    public void onException(int mensagem){
        AlertDialog.Builder dlg = new AlertDialog.Builder(binding.getRoot().getContext());
        dlg.setTitle(R.string.title_erro);
        dlg.setMessage(mensagem);
        dlg.setNeutralButton(R.string.action_ok,null);
        dlg.setIcon(R.drawable.ic_baseline_check_circle_24);
        dlg.show();
    }

    private void onTestarConexao() {
        ClientComunicacaoServer comunicacaoServer = new ClientComunicacaoServer(binding.getRoot().getContext(),
                "http://"+configuracao.getCONF_IP()+":"+configuracao.getCONF_PORTA()+UrlWebService.BaseAPI+UrlWebService.metodoTestarConexao,null,
                this::onPosExecute, this::onException);
        comunicacaoServer.execute("Comunicação","Aguarde, testando comunicação",true);
        //comunicacaoServer.onPostExecute(retorno);
    }

    private void onGravarConfiguracao() {
        if (configuracao == null){
            configuracao = new ConfiguracaoModel();
        }
        configuracaoDAO.excluirTodos();
        configuracao.setCONF_IP(edtIP.getText().toString());
        configuracao.setCONF_PORTA(edtPorta.getText().toString());
        configuracao.setCONF_INTEGRADOR(spinner.getSelectedItem().toString());
        configuracaoDAO.inserir(configuracao);
        btnTestar.setEnabled(true);
    }

    private void refresh(){
        barraProgresso = ProgressDialog.show(binding.getRoot().getContext(),
                "Carregando Nota Técnica",
                "Aguarde por favor...",
                true);
        barraProgresso.setIndeterminate(true);
        barraProgresso.setCancelable(false);
    }

    @Override
    public void onResume(){
        super.onResume();
        contador++;
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

    private boolean onVerificaConfiguracao() {
        boolean Retorno = true;
        try {
            configuracaoDAO = new ConfiguracaoDAO(conexao.retornaConexao(binding.getRoot().getContext(), binding.getRoot()));
            configuracaoDAO.setContext(binding.getRoot().getContext());
            if (contador > 0) {
                carregarDadosBD();
            }
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
        int posicao;
        if (configuracaoList.size() > 0) {
            inclusao = false;
            configuracao = configuracaoList.get(0);
            edtIP.setText(configuracao.getCONF_IP());
            edtPorta.setText(configuracao.getCONF_PORTA());
            if (spinner.getSelectedItem().toString() != configuracao.getCONF_INTEGRADOR()){
                posicao = spinner.getSelectedItemPosition();
                if (posicao == 0){
                    spinner.setSelection(1);
                }else{
                    spinner.setSelection(0);
                }
            } else{
                configuracao = new ConfiguracaoModel();
            }

        }

    }

}