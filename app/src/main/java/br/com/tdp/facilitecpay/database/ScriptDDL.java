package br.com.tdp.facilitecpay.database;

public class ScriptDDL {
    public  static String getDropTabelas(){
        StringBuilder sql = new StringBuilder();
        sql.append("DROP TABLE CONFIGURACAO;");
        sql.append("DROP TABLE REPRESE;");
        sql.append("DROP TABLE COBRA;");
        sql.append("DROP TABLE TIPOCOMANDA;");
        sql.append("DROP TABLE COMANDA");
        sql.append("DROP TABLE COMANDAV");
        return sql.toString();
    }

    public static String getCreateTabelaConfiguracao(){
        StringBuilder sql = new StringBuilder();

        sql.append("CREATE TABLE CONFIGURACAO (");
        sql.append("        CONF_IP          VARCHAR(255) NOT NULL,");
        sql.append("        CONF_PORTA       VARCHAR(10) NOT NULL,");
        sql.append("        CONF_INTEGRADOR  VARCHAR(50))");
        return sql.toString();
    }

    public static String getCreateTabelaReprese(){
        StringBuilder sql = new StringBuilder();

        sql.append("CREATE TABLE REPRESE (");
        sql.append("        REP_CODIGO       VARCHAR(4) NOT NULL,");
        sql.append("        REP_NOME         VARCHAR(30) NOT NULL,");
        sql.append("        REP_USUARIO      VARCHAR(15) NOT NULL,");
        sql.append("        REP_SENHAVENDA   VARCHAR(10)) ");
        return sql.toString();
    }

    public static String getCreateTabelaCobra(){
        StringBuilder sql = new StringBuilder();

        sql.append("CREATE TABLE COBRA (");
        sql.append("        COB_DESCRICAO       VARCHAR(15) NOT NULL,");
        sql.append("        COB_TIPOCOB         VARCHAR(2),");
        sql.append("        COB_CODIGO          VARCHAR(2), ");
        sql.append("        COB_OCULTABALCAO    VARCHAR(5)) ");
        return sql.toString();
    }

    public static String getCreateTabelaTipoComanda(){
        StringBuilder sql = new StringBuilder();

        sql.append("CREATE TABLE TIPOCOMANDA (");
        sql.append("        TCOM_CODIGO       VARCHAR(2) NOT NULL,");
        sql.append("        TCOM_BOTAOMOBILE  VARCHAR(30))");
        return sql.toString();
    }

    public static String getCreateTabelaComanda(){
        StringBuilder sql = new StringBuilder();

        sql.append("CREATE TABLE COMANDA (");
        sql.append("        COM_EMPRESA            VARCHAR(2) NOT NULL,");
        sql.append("        COM_TIPOCOMANDA        VARCHAR(2) NOT NULL,");
        sql.append("        COM_COMANDA            VARCHAR(6) NOT NULL,");
        sql.append("        COM_SEQUENCIA          INTEGER NOT NULL,");
        sql.append("        COM_STATUS             VARCHAR(1),");
        sql.append("        COM_DATA               DATE,");
        sql.append("        COM_REPRESENTANTE      VARCHAR(4),");
        sql.append("        COM_HORAABERTURA       VARCHAR(8),");
        sql.append("        COM_CAIXA              VARCHAR(3),");
        sql.append("        COM_TAXASERVICO        NUMERIC(12,2),");
        sql.append("        COM_TAXAENTREGA        NUMERIC(12,2),");
        sql.append("        COM_COUVER             NUMERIC(12,2),");
        sql.append("        COM_MESA               INTEGER,");
        sql.append("        COM_IGNORATAXASERVICO  VARCHAR(5),");
        sql.append("        COM_NOMECLIENTE        VARCHAR(50),");
        sql.append("        TOTAL_PROD             NUMERIC(12,2),");
        sql.append("        VLR_TAXA_SERVICO       NUMERIC(12,2),");
        sql.append("        COUVER_ENTREGA         NUMERIC(12,2),");
        sql.append("        TOTAL_RECEBIDO         NUMERIC(12,2),");
        sql.append("        TOTAL_COMANDA          NUMERIC(12,2))");
        return sql.toString();
    }

    public static String getCreateTabelaComandaV(){
        StringBuilder sql = new StringBuilder();

        sql.append("CREATE TABLE COMANDAV (");
        sql.append("        COMV_EMPRESA      VARCHAR(2) NOT NULL,");
        sql.append("        COMV_TIPOCOMANDA  VARCHAR(2) NOT NULL,");
        sql.append("        COMV_COMANDA      VARCHAR(6) NOT NULL,");
        sql.append("        COMV_SEQUENCIA    INTEGER NOT NULL,");
        sql.append("        COMV_COBRANCA     VARCHAR(15) NOT NULL,");
        sql.append("        COMV_VALOR         NUMERIC(12,2))");
        return sql.toString();
    }
}

