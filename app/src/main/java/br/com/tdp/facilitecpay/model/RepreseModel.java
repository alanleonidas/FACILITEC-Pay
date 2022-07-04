package br.com.tdp.facilitecpay.model;

import java.util.Objects;

public class RepreseModel {
    private String REP_CODIGO;

    public String getREP_CODIGO() {
        return REP_CODIGO;
    }

    public void setREP_CODIGO(String REP_CODIGO) {
        this.REP_CODIGO = REP_CODIGO;
    }

    public String getREP_NOME() {
        return REP_NOME;
    }

    public void setREP_NOME(String REP_NOME) {
        this.REP_NOME = REP_NOME;
    }

    public String getREP_USUARIO() {
        return REP_USUARIO;
    }

    public void setREP_USUARIO(String REP_USUARIO) {
        this.REP_USUARIO = REP_USUARIO;
    }

    private String REP_NOME;
    private String REP_USUARIO;
    private String REP_SENHAVENDA;

    public String getREP_SENHAVENDA() {
        return REP_SENHAVENDA;
    }

    public void setREP_SENHAVENDA(String REP_SENHAVENDA) {
        this.REP_SENHAVENDA = REP_SENHAVENDA;
    }

    public RepreseModel() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RepreseModel that = (RepreseModel) o;
        return REP_CODIGO == that.REP_CODIGO;
    }

    @Override
    public int hashCode() {
        return Objects.hash(REP_CODIGO);
    }
}
