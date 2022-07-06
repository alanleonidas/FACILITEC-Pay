package br.com.tdp.facilitecpay.model;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;
import java.util.Objects;

public class ComandasLiberadasModel implements Serializable {
    private String COM_EMPRESA;
    private String COM_TIPOCOMANDA;
    private String COM_COMANDA;
    private int COM_SEQUENCIA;
    private String COM_STATUS;
    private String COM_DATA;
    private String COM_REPRESENTANTE;
    private String COM_HORAABERTURA;
    private String COM_CAIXA;
    private Double COM_TAXASERVICO;
    private Double COM_TAXAENTREGA;
    private Double COM_COUVER;
    private int COM_MESA;
    private String COM_IGNORATAXASERVICO;
    private String COM_NOMECLIENTE;
    private Double TOTAL_PROD;
    private Double VLR_TAXA_SERVICO;
    private Double COUVER_ENTREGA;
    private Double TOTAL_RECEBIDO;
    private Double TOTAL_COMANDA;

    public ComandasLiberadasModel(){
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ComandasLiberadasModel that = (ComandasLiberadasModel) o;
        return COM_COMANDA == that.COM_COMANDA;
    }

    @Override
    public int hashCode() {
        return Objects.hash(COM_COMANDA);
    }


    public String getCOM_COMANDA() {
        return COM_COMANDA;
    }

    public void setCOM_COMANDA(String COM_COMANDA) {
        this.COM_COMANDA = COM_COMANDA;
    }

    public String getCOM_EMPRESA() {
        return COM_EMPRESA;
    }

    public void setCOM_EMPRESA(String COM_EMPRESA) {
        this.COM_EMPRESA = COM_EMPRESA;
    }

    public String getCOM_TIPOCOMANDA() {
        return COM_TIPOCOMANDA;
    }

    public void setCOM_TIPOCOMANDA(String COM_TIPOCOMANDA) {
        this.COM_TIPOCOMANDA = COM_TIPOCOMANDA;
    }

    public Integer getCOM_SEQUENCIA() {
        return COM_SEQUENCIA;
    }

    public void setCOM_SEQUENCIA(Integer COM_SEQUENCIA) {
        this.COM_SEQUENCIA = COM_SEQUENCIA;
    }

    public String getCOM_STATUS() {
        return COM_STATUS;
    }

    public void setCOM_STATUS(String COM_STATUS) {
        this.COM_STATUS = COM_STATUS;
    }

    public String getCOM_DATA() {
        return COM_DATA;
    }

    public void setCOM_DATA(String COM_DATA) {
        this.COM_DATA = COM_DATA;
    }

    public String getCOM_REPRESENTANTE() {
        return COM_REPRESENTANTE;
    }

    public void setCOM_REPRESENTANTE(String COM_REPRESE) {
        this.COM_REPRESENTANTE = COM_REPRESENTANTE;
    }

    public String getCOM_HORAABERTURA() {
        return COM_HORAABERTURA;
    }

    public void setCOM_HORAABERTURA(String COM_HORAABERTURA) {
        this.COM_HORAABERTURA = COM_HORAABERTURA;
    }

    public String getCOM_CAIXA() {
        return COM_CAIXA;
    }

    public void setCOM_CAIXA(String COM_CAIXA) {
        this.COM_CAIXA = COM_CAIXA;
    }

    public Double getCOM_TAXASERVICO() {
        return COM_TAXASERVICO;
    }

    public void setCOM_TAXASERVICO(Double COM_TAXASERVICO) {
        this.COM_TAXASERVICO = COM_TAXASERVICO;
    }

    public Double getCOM_TAXAENTREGA() {
        return COM_TAXAENTREGA;
    }

    public void setCOM_TAXAENTREGA(Double COM_TAXAENTREGA) {
        this.COM_TAXAENTREGA = COM_TAXAENTREGA;
    }

    public Double getCOM_COUVER() {
        return COM_COUVER;
    }

    public void setCOM_COUVER(Double COM_COUVER) {
        this.COM_COUVER = COM_COUVER;
    }

    public Integer getCOM_MESA() {
        return COM_MESA;
    }

    public void setCOM_MESA(Integer COM_MESA) {
        this.COM_MESA = COM_MESA;
    }

    public String getCOM_IGNORATAXASERVICO() {
        return COM_IGNORATAXASERVICO;
    }

    public void setCOM_IGNORATAXASERVICO(String COM_IGNORATAXASERVICO) {
        this.COM_IGNORATAXASERVICO = COM_IGNORATAXASERVICO;
    }

    public String getCOM_NOMECLIENTE() {
        return COM_NOMECLIENTE;
    }

    public void setCOM_NOMECLIENTE(String COM_NOMECLIENTE) {
        this.COM_NOMECLIENTE = COM_NOMECLIENTE;
    }

    public Double getTOTAL_PROD() {
        return TOTAL_PROD;
    }

    public void setTOTAL_PROD(Double TOTAL_PROD) {
        this.TOTAL_PROD = TOTAL_PROD;
    }

    public Double getVLR_TAXA_SERVICO() {
        return VLR_TAXA_SERVICO;
    }

    public void setVLR_TAXA_SERVICO(Double VLR_TAXA_SERVICO) {
        this.VLR_TAXA_SERVICO = VLR_TAXA_SERVICO;
    }

    public Double getCOUVER_ENTREGA() {
        return COUVER_ENTREGA;
    }

    public void setCOUVER_ENTREGA(Double COUVER_ENTREGA) {
        this.COUVER_ENTREGA = COUVER_ENTREGA;
    }

    public Double getTOTAL_RECEBIDO() {
        return TOTAL_RECEBIDO;
    }

    public void setTOTAL_RECEBIDO(Double TOTAL_RECEBIDO) {
        this.TOTAL_RECEBIDO = TOTAL_RECEBIDO;
    }

    public Double getTOTAL_COMANDA() {
        return TOTAL_COMANDA;
    }

    public void setTOTAL_COMANDA(Double TOTAL_COMANDA) {
        this.TOTAL_COMANDA = TOTAL_COMANDA;
    }
}
