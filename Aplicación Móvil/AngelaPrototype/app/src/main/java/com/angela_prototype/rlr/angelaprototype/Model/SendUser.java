package com.angela_prototype.rlr.angelaprototype.Model;

import android.os.AsyncTask;

import com.angela_prototype.rlr.angelaprototype.Pojos.User;
import com.angela_prototype.rlr.angelaprototype.Pojos.UserCredentials;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by Raúl on 19/06/2017.
 */

public class SendUser extends AsyncTask<Void, Void, Boolean> {

    private User user;
    private boolean result;
    private static final String DB_URL = "jdbc:mysql://192.168.1.128/angelaprototype_app";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public SendUser(User user) {
        this.user = user;
    }

    @Override
    public Boolean doInBackground(Void... params) {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            java.sql.Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            if (conn == null){
                System.out.println("Ha ocurrido un error, y no se ha podido establecer la conexión con la base de datos.");
                result = false;
            }else{
                System.out.println("Conectado correctamente.");
                String query = "INSERT INTO usuario(`dni`, `nombre`, `apellido1`, `apellido2`, `tarjetaSanitaria`, `localidad`, `municipio`, `direccion`, `portal`, `puerta`, `cp`) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
                PreparedStatement stm = conn.prepareStatement(query);

                stm.setString(1, user.getDNI());
                stm.setString(2, user.getNombre());
                stm.setString(3, user.getApellido1());
                stm.setString(4, user.getApellido2());
                stm.setString(5, user.getTarjetaSanitaria());
                stm.setString(6, user.getLocalidad());
                stm.setString(7, user.getMunicipio());
                stm.setString(8, user.getDireccion());
                stm.setInt(9, user.getPortal());
                stm.setString(10, user.getPuerta());
                stm.setInt(11, user.getCp());

                stm.executeUpdate();

                String query2 = "SELECT id FROM usuario WHERE dni=\""+user.getDNI()+"\"";
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(query2);
                rs.next();
                int user_id = rs.getInt("id");

                String query3 = "INSERT INTO login (`id_usuario`, `username`, `email`, `password`) VALUES (?,?,?,?)";
                PreparedStatement stm2 = conn.prepareStatement(query3);

                stm2.setInt(1, user_id);
                stm2.setString(2, user.getCredentials().getUsername());
                stm2.setString(3, user.getCredentials().getEmail());
                stm2.setString(4, user.getCredentials().getPassword());

                stm2.executeUpdate();

                result = true;
            }
            conn.close();

            publishProgress();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
