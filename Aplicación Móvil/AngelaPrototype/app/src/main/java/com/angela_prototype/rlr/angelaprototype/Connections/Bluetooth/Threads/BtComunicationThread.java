package com.angela_prototype.rlr.angelaprototype.Connections.Bluetooth.Threads;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.angela_prototype.rlr.angelaprototype.Connections.Bluetooth.BluetoothService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static android.content.ContentValues.TAG;
import static com.angela_prototype.rlr.angelaprototype.Connections.Bluetooth.BluetoothService.MSG_READ;

/**
 * Created by Raúl on 21/06/2017.
 */

public class BtComunicationThread extends Thread{

    private final BluetoothSocket socket;
    private final InputStream inputStream;
    private final OutputStream outputStream;

    public BtComunicationThread(BluetoothSocket socket) {
        this.socket = socket;
        setName(socket.getRemoteDevice().getName() + " [" + socket.getRemoteDevice().getAddress() + "]");

        InputStream tmpInputStream = null;
        OutputStream tmpOutputStream = null;

        try {
            tmpInputStream = this.socket.getInputStream();
            tmpOutputStream = this.socket.getOutputStream();
        }
        catch(IOException e){
            Log.e(TAG, "HiloConexion(): Error al obtener flujos de E/S", e);
        }
        this.inputStream = tmpInputStream;
        this.outputStream = tmpOutputStream;
    }

    public void run(){
        byte[] buffer = new byte[1024];
        int bytes;
        // Mientras se mantenga la conexion el hilo se mantiene en espera ocupada
        // leyendo del flujo de entrada
        while(true)
        {
            try {
                // Leemos del flujo de entrada del socket
                bytes = inputStream.read(buffer);

                // Enviamos la informacion a la actividad a traves del handler.
                // El metodo handleMessage sera el encargado de recibir el mensaje
                // y mostrar los datos recibidos en el TextView
                handler.obtainMessage(MSG_READ, bytes, -1, buffer).sendToTarget();
            }
            catch(IOException e) {
                Log.e(TAG, "HiloConexion.run(): Error al realizar la lectura", e);
            }
        }
    }

    private final Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            byte[] buffer = null;
            String mensaje = null;

            // Atendemos al tipo de mensaje
            switch (msg.what) {
                // Mensaje de lectura: se mostrara en un TextView
                case BluetoothService.MSG_READ: {
                    buffer = (byte[]) msg.obj;
                    mensaje = new String(buffer, 0, msg.arg1);
                    System.out.println(mensaje);
                    break;
                }
                default:
                    break;
            }
        }
    };
}
