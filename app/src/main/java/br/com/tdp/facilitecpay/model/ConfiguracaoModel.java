package br.com.tdp.facilitecpay.model;

import java.util.Objects;

public class ConfiguracaoModel {
    private String CONF_IP;
    private String CONF_PORTA;
    private String CONF_INTEGRADOR;

    public ConfiguracaoModel() {
    }

    public String getCONF_IP() {
        return CONF_IP;
    }

    public void setCONF_IP(String CONF_IP) {
        this.CONF_IP = CONF_IP;
    }

    public String getCONF_PORTA() {
        return CONF_PORTA;
    }

    public void setCONF_PORTA(String CONF_PORTA) {
        this.CONF_PORTA = CONF_PORTA;
    }

    public String getCONF_INTEGRADOR() {
        return CONF_INTEGRADOR;
    }

    public void setCONF_INTEGRADOR(String CONF_INTEGRADOR) {
        this.CONF_INTEGRADOR = CONF_INTEGRADOR;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConfiguracaoModel that = (ConfiguracaoModel) o;
        return CONF_IP == that.CONF_IP;
    }

    @Override
    public int hashCode() {
        return Objects.hash(CONF_IP);
    }

}
