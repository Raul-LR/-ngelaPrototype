package com.angela_prototype.rlr.angelaprototype.Pojos;

/**
 * Created by Raúl on 19/06/2017.
 */

public class UserCredentials {

    private int id;
    private int id_usuario;
    private String email;
    private String username;
    private String password;

    public UserCredentials(){
        this.id = 0;
        this.id_usuario = 0;
        this.email = "";
        this.username = "";
        this.password = "";
    }

    public UserCredentials(int id, int id_usuario, String email, String username, String password){
        this.id = id;
        this.id_usuario = id_usuario;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserCredentials{" +
                "id=" + id +
                ", id_usuario=" + id_usuario +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
