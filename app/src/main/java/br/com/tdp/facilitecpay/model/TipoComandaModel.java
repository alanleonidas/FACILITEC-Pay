package br.com.tdp.facilitecpay.model;

import java.util.Objects;

public class TipoComandaModel {
    private String TCOM_CODIGO;
    private String TCOM_BOTAOMOBILE;

    public TipoComandaModel() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TipoComandaModel that = (TipoComandaModel) o;
        return TCOM_CODIGO == that.TCOM_CODIGO;
    }

    @Override
    public int hashCode() {
        return Objects.hash(TCOM_CODIGO);
    }

    public String getTCOM_CODIGO() {
        return TCOM_CODIGO;
    }

    public void setTCOM_CODIGO(String TCOM_CODIGO) {
        this.TCOM_CODIGO = TCOM_CODIGO;
    }

    public String getTCOM_BOTAOMOBILE() {
        return TCOM_BOTAOMOBILE;
    }

    public void setTCOM_BOTAOMOBILE(String TCOM_BOTAOMOBILE) {
        this.TCOM_BOTAOMOBILE = TCOM_BOTAOMOBILE;
    }
}
