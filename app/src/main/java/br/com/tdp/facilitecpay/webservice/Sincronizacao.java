package br.com.tdp.facilitecpay.webservice;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import br.com.tdp.facilitecpay.database.CobraDAO;
import br.com.tdp.facilitecpay.database.Conexao;
import br.com.tdp.facilitecpay.database.ConfiguracaoDAO;
import br.com.tdp.facilitecpay.database.ListaComandasDAO;
import br.com.tdp.facilitecpay.database.PagamentosComandaDAO;
import br.com.tdp.facilitecpay.database.RepreseDAO;
import br.com.tdp.facilitecpay.database.TipoComandaDAO;
import br.com.tdp.facilitecpay.model.CobraModel;
import br.com.tdp.facilitecpay.model.ComandasLiberadasModel;
import br.com.tdp.facilitecpay.model.ConfiguracaoModel;
import br.com.tdp.facilitecpay.model.PagamentosComandaModel;
import br.com.tdp.facilitecpay.model.RepreseModel;
import br.com.tdp.facilitecpay.model.TipoComandaModel;
import br.com.tdp.facilitecpay.util.OpcaoSinc;

public class Sincronizacao extends AppCompatActivity implements DoComunicacao,DoException {
    private OpcaoSinc opcaoSinc;
    private Context context;
    private View view;
    private Conexao conexao = new Conexao();
    private ConfiguracaoModel configuracao;
    private JSONObject jsonObject;
    private DoComunicacao callback;
    private DoException callbackException;

    public void Sincronizacao(Context context, View view, OpcaoSinc opcaoSinc, JSONObject jsonObject, DoComunicacao callback, DoException callbackException){
        this.opcaoSinc = opcaoSinc;
        this.context = context;
        this.view = view;
        this.jsonObject = jsonObject;
        this.callback = callback;
        this.callbackException = callbackException;

        carregarDadosBD();

        switch (this.opcaoSinc) {
            case LoginAutorizado:
                efetuarLoginApi();
                break;
            case Represe:
                sincronizarReprese();
                break;
            case TipoComanda:
                sincronizarTipoComnada();
                break;
            case ComandasFinalizacao:
                sincronizarComandasLiberadas();
                break;
            case getCobrancas:
                sincronizargetCobrancas();
                break;
            case postFinalizarComanda:
                postFinalizarComanda();
                break;
        }
    }

    @Override
    public void onPosExecute(JSONObject object) {
        switch (opcaoSinc) {
            case LoginAutorizado:
                break;
            case Represe:
                try {
                    onRegistrarRepesentantes(object);
                    callback.onPosExecute(object);
                } catch (JSONException e) {
                    callbackException.onException(e.hashCode());
                }
                break;
            case TipoComanda:
                try {
                    onRegistrarTipoComanda(object);
                    callback.onPosExecute(object);
                } catch (JSONException e) {
                    callbackException.onException(e.hashCode());
                }
                break;
            case getCobrancas:
                try {
                    onRegistrarCobrancas(object);
                    callback.onPosExecute(object);
                } catch (JSONException e) {
                    callbackException.onException(e.hashCode());
                }
                break;
            case ComandasFinalizacao:
                try {
                    onRegistrarComandasLiberadas(object);
                    callback.onPosExecute(object);
                } catch (JSONException e) {
                    callbackException.onException(e.hashCode());
                }
                break;
            case postFinalizarComanda:
                try {
                    onFinalizarComanda(object);
                    callback.onPosExecute(object);
                } catch (JSONException e) {
                    callbackException.onException(e.hashCode());
                }
                break;
        }
    }


    private void onRegistrarRepesentantes(JSONObject object) throws JSONException {
        JSONArray parentArray = object.getJSONArray("result");
        JSONObject parentObjectnew = parentArray.getJSONObject(0);
        JSONArray jsonArray = parentObjectnew.getJSONArray("representantes");
        List<RepreseModel> listRepresentantes = new ArrayList<>();
        Gson gson = new Gson();
        RepreseDAO represeDAO = new RepreseDAO(conexao.retornaConexao(context, view));
        represeDAO.excluirTodos();
        for(int i=0; i<jsonArray.length(); i++) {
            JSONObject finalObject = jsonArray.getJSONObject(i);
            RepreseModel represeModel = gson.fromJson(finalObject.toString(), RepreseModel.class);
            listRepresentantes.add(represeModel);
        }
        represeDAO.inserirAll(listRepresentantes);

    }

    private void onRegistrarTipoComanda(JSONObject object) throws JSONException {
        JSONArray parentArray = object.getJSONArray("result");
        JSONObject parentObjectnew = parentArray.getJSONObject(0);
        JSONArray jsonArray = parentObjectnew.getJSONArray("tiposcomanda");
        List<TipoComandaModel> listTipoComanda = new ArrayList<>();
        Gson gson = new Gson();
        TipoComandaDAO tipoComandaDAO = new TipoComandaDAO(conexao.retornaConexao(context, view));
        tipoComandaDAO.excluirTodos();
        for(int i=0; i<jsonArray.length(); i++) {
            JSONObject finalObject = jsonArray.getJSONObject(i);
            TipoComandaModel tipoComandaModel = gson.fromJson(finalObject.toString(), TipoComandaModel.class);
            listTipoComanda.add(tipoComandaModel);
        }
        tipoComandaDAO.inserirAll(listTipoComanda);
    }

    private void onRegistrarCobrancas(JSONObject object) throws JSONException {
        JSONArray parentArray = object.getJSONArray("result");
        JSONObject parentObjectnew = parentArray.getJSONObject(0);
        JSONArray jsonArray = parentObjectnew.getJSONArray("cobrancas");
        List<CobraModel> listcobraModels = new ArrayList<>();
        Gson gson = new Gson();
        CobraDAO cobraDAO = new CobraDAO(conexao.retornaConexao(context, view));
        cobraDAO.excluirTodos();
        for(int i=0; i<jsonArray.length(); i++) {
            JSONObject finalObject = jsonArray.getJSONObject(i);
            CobraModel cobraModel = gson.fromJson(finalObject.toString(), CobraModel.class);
            listcobraModels.add(cobraModel);
        }
        cobraDAO.inserirAll(listcobraModels);
    }

    private void onRegistrarComandasLiberadas(JSONObject object) throws JSONException {
        JSONArray parentArray = object.getJSONArray("result");
        JSONObject parentObjectnew = parentArray.getJSONObject(0);
        JSONArray jsonArray = parentObjectnew.getJSONArray("COMANDAS");
        List<ComandasLiberadasModel> listComandasLiberadas = new ArrayList<>();
        Gson gson = new Gson();
        ListaComandasDAO listaComandasDAO = new ListaComandasDAO(conexao.retornaConexao(context, view));
        listaComandasDAO.excluirTodos();
        for(int i=0; i<jsonArray.length(); i++) {
            JSONObject finalObject = jsonArray.getJSONObject(i);
            ComandasLiberadasModel comandasLiberadasModel = gson.fromJson(finalObject.toString(), ComandasLiberadasModel.class);
            if (listaComandasDAO.findExist(comandasLiberadasModel)!=true){
                listComandasLiberadas.add(comandasLiberadasModel);

                JSONArray jsonArrayV = finalObject.getJSONArray("COMANDAV");
                List<PagamentosComandaModel> listPagamentosComandaModels = new ArrayList<>();
                PagamentosComandaDAO listaPagamentosComandaDAO = new PagamentosComandaDAO(conexao.retornaConexao(context, view));
                listaPagamentosComandaDAO.excluirRegistroComanda(comandasLiberadasModel.getCOM_EMPRESA(),
                        comandasLiberadasModel.getCOM_TIPOCOMANDA(), comandasLiberadasModel.getCOM_COMANDA(),
                        comandasLiberadasModel.getCOM_SEQUENCIA(),"");
                for(int a=0; a<jsonArrayV.length(); a++) {
                    JSONObject finalObjectV = jsonArrayV.getJSONObject(a);
                    PagamentosComandaModel pagamentosComandaModel = gson.fromJson(finalObjectV.toString(), PagamentosComandaModel.class);
                    listPagamentosComandaModels.add(pagamentosComandaModel);
                }
                listaPagamentosComandaDAO.inserirAll(listPagamentosComandaModels);
            }
        }
        listaComandasDAO.inserirAll(listComandasLiberadas);

    }

    private void onFinalizarComanda(JSONObject object) throws JSONException {
        JSONArray parentArray = object.getJSONArray("result");
        JSONObject parentObjectnew = parentArray.getJSONObject(0);
    }

    private void sincronizarReprese(){
        ClientComunicacaoServer comunicacaoServer = new ClientComunicacaoServer(context,
                "http://"+configuracao.getCONF_IP()+":"+configuracao.getCONF_PORTA()+UrlWebService.BaseAPI+UrlWebService.getRepresentante,
                jsonObject, this::onPosExecute, this::onException);
        comunicacaoServer.execute("Comunicação","Sincronizando dados",true);
    }

    private void sincronizarTipoComnada(){
        ClientComunicacaoServer comunicacaoServer = new ClientComunicacaoServer(context,
                "http://"+configuracao.getCONF_IP()+":"+configuracao.getCONF_PORTA()+UrlWebService.BaseAPI+UrlWebService.getTipoComandaApi,
                jsonObject, this::onPosExecute, this::onException);
        comunicacaoServer.execute("Comunicação","Sincronizando dados",false);
    }

    private void sincronizarComandasLiberadas(){
        ClientComunicacaoServer comunicacaoServer = new ClientComunicacaoServer(context,
                "http://"+configuracao.getCONF_IP()+":"+configuracao.getCONF_PORTA()+UrlWebService.BaseAPI+UrlWebService.getComandasFinalizacao,
                jsonObject, this::onPosExecute, this::onException);
        comunicacaoServer.execute("Comunicação","Sincronizando dados",true);
    }


    private void sincronizargetCobrancas(){
        ClientComunicacaoServer comunicacaoServer = new ClientComunicacaoServer(context,
                "http://"+configuracao.getCONF_IP()+":"+configuracao.getCONF_PORTA()+UrlWebService.BaseAPI+UrlWebService.getCobrancas,
                jsonObject, this::onPosExecute, this::onException);
        comunicacaoServer.execute("Comunicação","Sincronizando dados",true);
    }

    private void postFinalizarComanda(){
        ClientComunicacaoServer comunicacaoServer = new ClientComunicacaoServer(context,
                "http://"+configuracao.getCONF_IP()+":"+configuracao.getCONF_PORTA()+UrlWebService.BaseAPI+UrlWebService.postFinalizarComanda,
                jsonObject, this::onPosExecute, this::onException);
        comunicacaoServer.execute("Comunicação","Sincronizando dados",true);
    }

    private void efetuarLoginApi(){
        ClientComunicacaoServer comunicacaoServer = new ClientComunicacaoServer(context,
                "http://"+configuracao.getCONF_IP()+":"+configuracao.getCONF_PORTA()+UrlWebService.BaseAPI+UrlWebService.efetuarLogin,
                jsonObject, this::onPosExecute, this::onException);
        comunicacaoServer.execute("Comunicação","Validando Login", true);
    }

    private void carregarDadosBD(){
        ConfiguracaoDAO configuracaoDAO = new ConfiguracaoDAO(conexao.retornaConexao(context, view));
        configuracaoDAO.setContext(context);
        List<ConfiguracaoModel> configuracaoList = configuracaoDAO.buscarTodos();
        int posicao;
        if (configuracaoList.size() > 0) {
            configuracao = configuracaoList.get(0);
        }

    }

    @Override
    public void onException(int mensagem) {
        callbackException.onException(mensagem);
    }
}
