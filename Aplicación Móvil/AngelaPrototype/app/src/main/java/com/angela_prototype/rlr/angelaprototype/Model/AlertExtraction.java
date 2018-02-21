package com.angela_prototype.rlr.angelaprototype.Model;

import android.os.AsyncTask;

import com.angela_prototype.rlr.angelaprototype.Pojos.Alert;
import com.angela_prototype.rlr.angelaprototype.Pojos.User;
import com.angela_prototype.rlr.angelaprototype.Pojos.UserCredentials;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by Raúl on 22/06/2017.
 */

public class AlertExtraction extends AsyncTask<Void, Void, ArrayList<Alert>> {

    private int user_id;
    private ArrayList<Alert> result = new ArrayList<>();
    private static final String DB_URL = "jdbc:mysql://192.168.1.128/angelaprototype_app";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public AlertExtraction(int user_id) {
        this.user_id = user_id;
    }

    @Override
    public ArrayList<Alert> doInBackground(Void... params) {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            java.sql.Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            if (conn == null){
                System.out.println("Ha ocurrido un error, y no se ha podido establecer la conexión con la base de datos.");
            }else{
                System.out.println("Conectado correctamente.");
                String query = "SELECT * FROM alerta WHERE id_usuario = \"" + this.user_id + "\"";
                Statement stm = conn.createStatement();
                ResultSet rs = stm.executeQuery(query);
                while(rs.next()){
                    Alert alert = new Alert(rs.getInt("id"),
                            rs.getInt("id_usuario"),
                            rs.getString("tipo"),
                            rs.getString("problema"),
                            rs.getString("lecturas"),
                            "00/00/00",
                            "00:00:00");

                    result.add(alert);
                }

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
    protected void onPostExecute(final ArrayList<Alert> result) {}
}
