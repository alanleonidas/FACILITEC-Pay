package br.com.tdp.facilitecpay.model;

import java.util.Objects;

public class CobraModel {
    private String COB_DESCRICAO;
    private String COB_TIPOCOB;
    private String COB_CODIGO;
    private String COB_OCULTABALCAO;
    private String COB_CARTEIRADIGITAL;

    public CobraModel() {
    }

    public String getCOB_DESCRICAO() {
        return COB_DESCRICAO;
    }

    public void setCOB_DESCRICAO(String COB_DESCRICAO) {
        this.COB_DESCRICAO = COB_DESCRICAO;
    }

    public String getCOB_TIPOCOB() {
        return COB_TIPOCOB;
    }

    public void setCOB_TIPOCOB(String COB_TIPOCOB) {
        this.COB_TIPOCOB = COB_TIPOCOB;
    }

    public String getCOB_CODIGO() {
        return COB_CODIGO;
    }

    public void setCOB_CODIGO(String COB_CODIGO) {
        this.COB_CODIGO = COB_CODIGO;
    }

    public String getCOB_OCULTABALCAO() {
        return COB_OCULTABALCAO;
    }

    public void setCOB_OCULTABALCAO(String COB_OCULTABALCAO) {
        this.COB_OCULTABALCAO = COB_OCULTABALCAO;
    }

    public String getCOB_CARTEIRADIGITAL() {
        return COB_CARTEIRADIGITAL;
    }

    public void setCOB_CARTEIRADIGITAL(String COB_CARTEIRADIGITAL) {
        this.COB_CARTEIRADIGITAL = COB_CARTEIRADIGITAL;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CobraModel that = (CobraModel) o;
        return COB_DESCRICAO == that.COB_DESCRICAO;
    }

    @Override
    public int hashCode() {
        return Objects.hash(COB_DESCRICAO);
    }
}
