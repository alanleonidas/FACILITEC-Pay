package br.com.tdp.facilitecpay;

import android.content.Intent;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.com.tdp.facilitecpay.database.Conexao;
import br.com.tdp.facilitecpay.database.ConfiguracaoDAO;
import br.com.tdp.facilitecpay.database.RepreseDAO;
import br.com.tdp.facilitecpay.databinding.ActivityLoginBinding;
import br.com.tdp.facilitecpay.model.ConfiguracaoModel;
import br.com.tdp.facilitecpay.model.RepreseModel;
import br.com.tdp.facilitecpay.util.OpcaoSinc;
import br.com.tdp.facilitecpay.util.Permissoes;
import br.com.tdp.facilitecpay.webservice.Sincronizacao;

public class Login extends AppCompatActivity {

    private static final int HTTP_REQUEST_TIMEOUT = 4000;
    private static final int HTTP_READ_TIMEOUT = 2000;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityLoginBinding binding;
    private TextView titulo;
    private Spinner spinner;
    private EditText editPassRepresentantes;
    private Button btnMenu;
    private Button btnEntrar;
    private Button btnReconectar;
    private DrawerLayout drawer;
    private Permissoes permissao;
    private int contador = -1;
    private ConfiguracaoDAO configuracaoDAO;
    private Conexao conexao = new Conexao();
    private View viewLogin;
    private LinearLayout reconectar;
    private OpcaoSinc sincAtual;
    private RepreseModel represeSelecionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarLogin.toolbar);
        btnMenu = (Button)findViewById(R.id.menu);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickMenu();
                }
            });
        viewLogin = (View)findViewById(R.id.containerLogin);
        reconectar = (LinearLayout)findViewById(R.id.linearReconectar);

        titulo = (TextView)findViewById(R.id.textView);
        spinner = (Spinner)findViewById(R.id.spinnerRepresentantes);
        editPassRepresentantes = (EditText) findViewById(R.id.editPassRepresentantes);
        btnEntrar = (Button)findViewById(R.id.btnEntrar);
        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    onLogin();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        btnReconectar = (Button)findViewById(R.id.btnTentarNovamente);
        btnReconectar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { onReconectar(); }
        });
        Bitmap logoApp = BitmapFactory.decodeResource(getResources(), R.drawable.logo_app);

        drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_configuracoes, R.id.nav_sobre)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_login);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(this::OnNavigationItemSelectedListener);
        permissao = new Permissoes(this);
        permissao.HabilitarNetWork();
        permissao.HabilitarWifi();
        permissao.permissoesINTERNET();

        onVisibility(View.VISIBLE);
    }

    public void onClickMenu(){
        drawer.open();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_login);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void carregarConfiguracoes(){
        Intent intent = new Intent(this, Configuracao.class);
        startActivity(intent);
    }

    public void carregarListaComandas(){
        Intent intent = new Intent(this, ListaComandas.class);
        startActivity(intent);
    }

    public boolean OnNavigationItemSelectedListener(@NonNull MenuItem item){
        int id= item.getItemId();

        if (id == R.id.nav_configuracoes) {
            carregarConfiguracoes();
        } else if (id == R.id.nav_sobre){
            Intent intent = new Intent(this, Sobre.class);
            startActivity(intent);
        } else {
            finish();//finishAffinity();
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
        btnEntrar.setEnabled(false);
        if ((configuracaoList.size() == 0) && (contador == 0)) {
            carregarConfiguracoes();
        } else if ((configuracaoList.size() == 0) && (contador > 0)) {
            AlertDialog.Builder dlg = new AlertDialog.Builder(binding.getRoot().getContext());
            dlg.setTitle(R.string.title_erro);
            dlg.setMessage("Sem a configuração não será possível continuar!");
            dlg.setNeutralButton(R.string.action_ok,null);
            dlg.show();
        } else {
            Sincronizacao sincronizacao = new Sincronizacao();
            sincAtual = OpcaoSinc.Represe;
            sincronizacao.Sincronizacao(binding.getRoot().getContext(), binding.getRoot(), OpcaoSinc.Represe, null, this::onPosExecute, this::onException);
        }
    }

    private void onLoadComponentes(){
        RepreseDAO represeDAO = new RepreseDAO(conexao.retornaConexao(binding.getRoot().getContext(), binding.getRoot()));
        List<RepreseModel> listaRepresentante = represeDAO.buscarTodos();
        btnEntrar.setEnabled(true);
        List<String> represe = new ArrayList<String>();

        if ((listaRepresentante != null) && (listaRepresentante.size()>0)) {
            for(int i=0; i<listaRepresentante.size(); i++) {
                represe.add(listaRepresentante.get(i).getREP_NOME());
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, represe);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                RepreseDAO represeDAO = new RepreseDAO(conexao.retornaConexao(binding.getRoot().getContext(), binding.getRoot()));
                represeSelecionado = represeDAO.buscarRepreseModel(spinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void onLogin() throws JSONException {
        String senha = editPassRepresentantes.getText().toString().trim();
        if (senha.isEmpty()){
            AlertDialog.Builder dlgR = new AlertDialog.Builder(binding.getRoot().getContext());
            dlgR.setTitle(R.string.title_erro);
            dlgR.setMessage(R.string.mensagem_senha_branco);
            dlgR.setNeutralButton(R.string.action_ok,null);
            dlgR.show();
        } else {
            if (represeSelecionado.getREP_SENHAVENDA().toString().trim().equals(senha)){
                carregarListaComandas();
            } else {
                AlertDialog.Builder dlgR = new AlertDialog.Builder(binding.getRoot().getContext());
                dlgR.setTitle(R.string.title_erro);
                dlgR.setMessage(R.string.senha_invalida);
                dlgR.setNeutralButton(R.string.action_ok,null);
                dlgR.show();
                editPassRepresentantes.setFocusable(true);

            }
        }
    }

    private void onVisibility(int visibility){
        titulo.setVisibility(visibility);
        spinner.setVisibility(visibility);
        editPassRepresentantes.setVisibility(visibility);
        btnEntrar.setVisibility(visibility);
        if (visibility==View.VISIBLE){
            reconectar.setVisibility(View.GONE);
        } else{
            reconectar.setVisibility(View.VISIBLE);
        }
    }

    public void onPosExecute(JSONObject object)  {
        switch (sincAtual) {
            case LoginAutorizado:
                try {
                    String valor = object.getString("retornoDS");
                } catch (JSONException e){
                    e.printStackTrace();
                }
                break;
            case Represe:
                viewLogin.setVisibility(View.VISIBLE);
                onVisibility(View.VISIBLE);
                onLoadComponentes();
                break;
            case ComandasFinalizacao:
                break;
        }
    }

    public void onException(int mensagem){
        switch (sincAtual) {
            case LoginAutorizado:
                AlertDialog.Builder dlg = new AlertDialog.Builder(binding.getRoot().getContext());
                dlg.setTitle(R.string.title_erro);
                dlg.setMessage("Erro ao validar dados de acesso!");
                dlg.setNeutralButton(R.string.action_ok,null);
                dlg.show();
                break;
            case Represe:
                viewLogin.setVisibility(View.GONE);
                onVisibility(View.INVISIBLE);
                AlertDialog.Builder dlgR = new AlertDialog.Builder(binding.getRoot().getContext());
                dlgR.setTitle(R.string.title_erro);
                dlgR.setMessage(mensagem);
                dlgR.setNeutralButton(R.string.action_ok,null);
                dlgR.show();
                break;
            case ComandasFinalizacao:
                break;
        }

    }
}