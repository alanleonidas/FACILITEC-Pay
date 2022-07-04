package br.com.tdp.facilitecpay.webservice;

import org.json.JSONObject;

public interface DoComunicacao {
    public void onPosExecute(JSONObject object);
}

