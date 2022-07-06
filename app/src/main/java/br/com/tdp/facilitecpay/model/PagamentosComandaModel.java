package br.com.tdp.facilitecpay.model;

import java.util.Objects;

public class PagamentosComandaModel {
    private String COMV_EMPRESA;
    private String COMV_TIPOCOMANDA;
    private String COMV_COMANDA;
    private int COMV_SEQUENCIA;
    private String COMV_COBRANCA;
    private Double COMV_VALOR;

    public String getCOMV_EMPRESA() {
        return COMV_EMPRESA;
    }

    public void setCOMV_EMPRESA(String COMV_EMPRESA) {
        this.COMV_EMPRESA = COMV_EMPRESA;
    }

    public String getCOMV_TIPOCOMANDA() {
        return COMV_TIPOCOMANDA;
    }

    public void setCOMV_TIPOCOMANDA(String COMV_TIPOCOMANDA) {
        this.COMV_TIPOCOMANDA = COMV_TIPOCOMANDA;
    }

    public String getCOMV_COMANDA() {
        return COMV_COMANDA;
    }

    public void setCOMV_COMANDA(String COMV_COMANDA) {
        this.COMV_COMANDA = COMV_COMANDA;
    }

    public int getCOMV_SEQUENCIA() {
        return COMV_SEQUENCIA;
    }

    public void setCOMV_SEQUENCIA(int COMV_SEQUENCIA) {
        this.COMV_SEQUENCIA = COMV_SEQUENCIA;
    }

    public String getCOMV_COBRANCA() {
        return COMV_COBRANCA;
    }

    public void setCOMV_COBRANCA(String COMV_COBRANCA) {
        this.COMV_COBRANCA = COMV_COBRANCA;
    }

    public Double getCOMV_VALOR() {
        return COMV_VALOR;
    }

    public void setCOMV_VALOR(Double COMV_VALOR) {
        this.COMV_VALOR = COMV_VALOR;
    }

    public PagamentosComandaModel(){
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PagamentosComandaModel that = (PagamentosComandaModel) o;
        return COMV_COMANDA == that.COMV_COMANDA;
    }

    @Override
    public int hashCode() {
        return Objects.hash(COMV_COMANDA);
    }
}
