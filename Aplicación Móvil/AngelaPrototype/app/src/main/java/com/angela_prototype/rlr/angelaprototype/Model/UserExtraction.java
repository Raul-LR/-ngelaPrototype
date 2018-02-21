package com.angela_prototype.rlr.angelaprototype.Model;

import android.os.AsyncTask;

import com.angela_prototype.rlr.angelaprototype.Pojos.User;
import com.angela_prototype.rlr.angelaprototype.Pojos.UserCredentials;
import com.angela_prototype.rlr.angelaprototype.R;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by Raúl on 19/06/2017.
 */

public class UserExtraction extends AsyncTask<Void, Void, User> {

    private final String mEmail;
    private UserCredentials credentials;
    private User result = new User();
    private static final String DB_URL = "jdbc:mysql://192.168.1.128/angelaprototype_app";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public UserExtraction(String email) {
        this.mEmail = email;
    }

    @Override
    public User doInBackground(Void... params) {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            java.sql.Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            if (conn == null){
                System.out.println("Ha ocurrido un error, y no se ha podido establecer la conexión con la base de datos.");
            }else{
                System.out.println("Conectado correctamente.");
                String query = "SELECT * FROM login WHERE email = \"" + mEmail + "\"";
                Statement stm = conn.createStatement();
                ResultSet rs = stm.executeQuery(query);
                rs.next();
                credentials = new UserCredentials(rs.getInt("id"),
                        rs.getInt("id_usuario"),
                        rs.getString("email"),
                        rs.getString("username"),
                        rs.getString("password"));

                System.out.println(credentials.toString());

                String query2 = "SELECT * FROM usuario WHERE id = \"" + credentials.getId_usuario() + "\"";
                rs = stm.executeQuery(query2);
                rs.next();

                result = new User(credentials,
                        rs.getInt("id"),
                        rs.getString("DNI"),
                        rs.getString("nombre"),
                        rs.getString("apellido1"),
                        rs.getString("apellido2"),
                        rs.getString("tarjetaSanitaria"),
                        rs.getString("localidad"),
                        rs.getString("municipio"),
                        rs.getString("direccion"),
                        rs.getInt("portal"),
                        rs.getString("puerta"),
                        rs.getInt("cp"));

                System.out.println(result.toString());
            }
            conn.close();

            publishProgress();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    protected void onPostExecute(final User result) {}
}
