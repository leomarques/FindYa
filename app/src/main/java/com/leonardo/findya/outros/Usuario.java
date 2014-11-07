package com.leonardo.findya.outros;

import java.io.Serializable;

public class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nome;
    private String idFace;
    private String idGcm;
    private String idInstalacao;
    private double latitude;
    private double longitude;
    private String data;
    private String status;
    private String localizacao;
    private Double distancia;
    private boolean publico;

    public Usuario() {
        publico = true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idFace == null) ? 0 : idFace.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Usuario other = (Usuario) obj;
        if (idFace == null) {
            if (other.idFace != null)
                return false;
        } else if (!idFace.equals(other.idFace))
            return false;
        return true;
    }

    public Double getDistancia() {
        return distancia;
    }

    public void setDistancia(Double distancia) {
        this.distancia = distancia;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getIdFace() {
        return idFace;
    }

    public void setIdFace(String idFace) {
        this.idFace = idFace;
    }

    public String getIdGcm() {
        return idGcm;
    }

    public void setIdGcm(String idGcm) {
        this.idGcm = idGcm;
    }

    public String getIdInstalacao() {
        return idInstalacao;
    }

    public void setIdInstalacao(String idDispositivo) {
        this.idInstalacao = idDispositivo;
    }

    public void setStatus(String paramStatus) {
        status = paramStatus;
    }

    public String getStatus() {
        return status;
    }

    public boolean isPublico() {
        return publico;
    }

    public void setPublico(boolean publico) {
        this.publico = publico;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }
}
