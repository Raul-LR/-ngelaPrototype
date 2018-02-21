package com.angela_prototype.rlr.angelaprototype.Model;

import android.os.AsyncTask;

import com.angela_prototype.rlr.angelaprototype.Pojos.Alert;
import com.angela_prototype.rlr.angelaprototype.Pojos.User;

import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;

/**
 * Created by Raúl on 22/06/2017.
 */

public class SendAlert extends AsyncTask<Void, Void, Boolean> {

    private Alert alert;
    private boolean result;
    private static final String DB_URL = "jdbc:mysql://192.168.1.128/angelaprototype_app";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public SendAlert(Alert alert) {
        this.alert = alert;
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
                String query = "INSERT INTO alerta(`id_usuario`, `tipo`, `problema`,  `lecturas`, `fecha`, `hora`) VALUES (?,?,?,?,?,?)";
                PreparedStatement stm = conn.prepareStatement(query);

                stm.setInt(1, alert.getUser_id());
                stm.setString(2, alert.getType());
                stm.setString(3, alert.getProblem());
                stm.setString(4, alert.getLectures());
                stm.setString(5, alert.getDate());
                stm.setString(6, alert.getTime());

                stm.executeUpdate();

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
