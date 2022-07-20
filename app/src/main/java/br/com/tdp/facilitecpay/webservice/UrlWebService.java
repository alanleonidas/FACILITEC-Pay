package br.com.tdp.facilitecpay.webservice;

public class UrlWebService {
    public static String IP_Pref = "http://192.168.2.21";//"187.1.66.120"; /*"192.168.0.51"*/;
    public static String Porta =  "8688";
    public static String BaseAPI = "/datasnap/rest/TServerMethods1/";
    public static String BASE_URL = IP_Pref+":"+Porta+BaseAPI;
    public static String metodoTestarConexao = "testarServidor";
    public static String getRepresentante = "Representantes";
    public static String getSerieEquipamentoPermidido = "getCelularImeiPermitido";
    public static String efetuarLogin = "efetuarLoginApi/";
    public static String getCobrancas = "getCobrancas/";
    public static String getTipoComandaApi = "getTipoComandaApi/";
    public static String getComandasFinalizacao = "getComandasFinalizacao/";
    public static String postFinalizarComanda = "FinalizarComanda/";
}
