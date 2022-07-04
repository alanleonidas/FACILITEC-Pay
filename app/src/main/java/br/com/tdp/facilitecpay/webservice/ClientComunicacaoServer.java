package br.com.tdp.facilitecpay.webservice;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import br.com.tdp.facilitecpay.R;

public class ClientComunicacaoServer implements Runnable{
    private static final int HTTP_REQUEST_TIMEOUT = 4000;
    private static final int HTTP_READ_TIMEOUT = 2000;
    private JSONObject parentObjectnew;
    private Context context;
    private String url;
    private DoComunicacao callback;
    private DoException callbackException;
    private JSONObject jsonEnvio;
    ProgressDialog barraProgresso;

    public ClientComunicacaoServer(Context context,String url, JSONObject jsonEnvio,DoComunicacao callback, DoException callbackException){
        this.context = context;
        this.url = url;
        this.callback = callback;
        this.jsonEnvio = jsonEnvio;
        this.callbackException = callbackException;
    }

    public JSONObject getRetorno(){
        return parentObjectnew;
    }
    public void onPostExecute(final JSONObject result){}

    public void execute (String titulo, String mensagemLoad, Boolean chamarProgresso){
        if (chamarProgresso==true) {
            barraProgresso = ProgressDialog.show(context,
                    titulo,
                    mensagemLoad,
                    true);
            barraProgresso.setIndeterminate(true);
            barraProgresso.setCancelable(false);
        }
        new JSONTask().execute(url);
    }

    @Override
    public void run(){
    }

    private class JSONTask extends AsyncTask<String, String, JSONObject>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... strings) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                try {
                    connection.setReadTimeout(HTTP_READ_TIMEOUT);
                    connection.setConnectTimeout(HTTP_REQUEST_TIMEOUT);

                    if (jsonEnvio != null){
                        connection.setDoInput(true);
                        connection.setRequestMethod("GET");
                        connection.setRequestProperty("Accept", "application/json");
                        connection.setRequestProperty("Content-Type", "application/json");
                        connection.setDoOutput(true);
                        OutputStream outStream = connection.getOutputStream();
                        OutputStreamWriter outStreamWriter = new OutputStreamWriter(outStream, "UTF-8");
                        connection.setRequestMethod("GET");

                        outStreamWriter.write(jsonEnvio.toString());
                        //connection.getOutputStream().write(jsonEnvio.toString().getBytes(StandardCharsets.UTF_8));
                        outStreamWriter.flush();
                        outStreamWriter.close();
                        outStream.close();
                        connection.connect();
                        Log.i("SERVICO","Envio: "+connection.getOutputStream().toString());
                        Log.i("SERVICO", "Retorno: "+ connection.getResponseMessage());
                        Log.i("SERVICO", "Retorno: "+ connection.getResponseCode());
                        Log.i("SERVICO", "Retorno: "+ connection.getResponseCode());
                    }
                    connection.connect();
                    Log.i("SERVICO", "Retorno: "+ connection.getResponseMessage());
                    Log.i("SERVICO", "Retorno: "+ connection.getResponseCode());
                    if (connection.getResponseCode() == 200){

                        InputStream stream = connection.getInputStream();
                        reader = new BufferedReader(new InputStreamReader(stream));
                        StringBuffer buffer = new StringBuffer();
                        String line ="";
                        while ((line = reader.readLine()) != null){
                            buffer.append(line);
                        }

                        String finalJson = buffer.toString();
                        JSONObject parentObject = new JSONObject(finalJson);
                        parentObjectnew = parentObject;
                    } else {
                        parentObjectnew = null;
                    }
                } catch (IOException e) {
                    hideProgressDialog();
                    Log.e("SERVICO", "Seu erro: ", e);
                    //callbackException.onException(R.string.erro_comunicao_servidor);
                } catch (JSONException e) {
                    hideProgressDialog();
                    Log.e("SERVICO", "Erro JSONException: ", e);
                    e.printStackTrace();
                }
            } catch (MalformedURLException e) {
                hideProgressDialog();
                Log.e("SERVICO", "Erro MalformedURLException: ", e);
                e.printStackTrace();
            } catch (IOException e) {
                hideProgressDialog();
                Log.e("SERVICO", "Erro IOException: ", e);
                e.printStackTrace();
            } finally {
                if(connection != null) {
                    connection.disconnect();
                }
                try {
                    if(reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return parentObjectnew;
        }

        @Override
        protected void onPostExecute(final JSONObject result) {
            super.onPostExecute(result);
            if(result == null) {
                callbackException.onException(R.string.erro_comunicao_servidor);
            } else{
                callback.onPosExecute(result);
            }
            hideProgressDialog();
        }
    }

    public void hideProgressDialog() {
        if (barraProgresso != null && barraProgresso.isShowing()) {
            barraProgresso.dismiss();
        }
    }

}
