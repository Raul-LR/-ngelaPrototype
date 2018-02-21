package com.angela_prototype.rlr.angelaprototype.Pojos;

import java.io.Serializable;

/**
 * Created by Raúl on 19/06/2017.
 */

public class User implements Serializable {

    private UserCredentials credentials;
    private int id;
    private String DNI;
    private String nombre;
    private String apellido1;
    private String apellido2;
    private String tarjetaSanitaria;
    private String localidad;
    private String municipio;
    private String direccion;
    private int portal;
    private String puerta;
    private int cp;

    public User(){
        this.credentials = new UserCredentials();
        this.id = 0;
        this.DNI = "";
        this.nombre = "";
        this.apellido1 = "";
        this.apellido2 = "";
        this.tarjetaSanitaria = "";
        this.localidad = "";
        this.municipio = "";
        this.direccion = "";
        this.portal = 0;
        this.puerta = "";
        this.cp = 0;
    }

    public User(UserCredentials credentials, int id, String DNI, String nombre, String apellido1, String apellido2, String tarjetaSanitaria, String localidad, String municipio, String direccion, int portal, String puerta, int cp){
        this.credentials = credentials;
        this.id = id;
        this.DNI = DNI;
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.tarjetaSanitaria = tarjetaSanitaria;
        this.localidad = localidad;
        this.municipio = municipio;
        this.direccion = direccion;
        this.portal = portal;
        this.puerta = puerta;
        this.cp = cp;
    }

    public UserCredentials getCredentials() {
        return credentials;
    }

    public void setCredentials(UserCredentials credentials) {
        this.credentials = credentials;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDNI() {
        return DNI;
    }

    public void setDNI(String DNI) {
        this.DNI = DNI;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido1() {
        return apellido1;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    public String getTarjetaSanitaria() {
        return tarjetaSanitaria;
    }

    public void setTarjetaSanitaria(String tarjetaSanitaria) {
        this.tarjetaSanitaria = tarjetaSanitaria;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getPortal() {
        return portal;
    }

    public void setPortal(int portal) {
        this.portal = portal;
    }

    public String getPuerta() {
        return puerta;
    }

    public void setPuerta(String puerta) {
        this.puerta = puerta;
    }

    public int getCp() {
        return cp;
    }

    public void setCp(int cp) {
        this.cp = cp;
    }

    @Override
    public String toString() {
        return "User{" +
                "credentials=" + credentials.toString() +
                ", id=" + id +
                ", DNI='" + DNI + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellido1='" + apellido1 + '\'' +
                ", apellido2='" + apellido2 + '\'' +
                ", tarjetaSanitaria='" + tarjetaSanitaria + '\'' +
                ", localidad='" + localidad + '\'' +
                ", municipio='" + municipio + '\'' +
                ", direccion='" + direccion + '\'' +
                ", portal=" + portal +
                ", puerta='" + puerta + '\'' +
                ", cp=" + cp +
                '}';
    }
}
