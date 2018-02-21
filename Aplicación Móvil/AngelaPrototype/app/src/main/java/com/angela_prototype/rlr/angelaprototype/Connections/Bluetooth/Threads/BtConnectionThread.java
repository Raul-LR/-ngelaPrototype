package com.angela_prototype.rlr.angelaprototype.Connections.Bluetooth.Threads;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.angela_prototype.rlr.angelaprototype.Activities.MainActivity;
import com.angela_prototype.rlr.angelaprototype.Connections.Bluetooth.BluetoothService;

import java.io.IOException;
import java.util.UUID;

import static android.content.ContentValues.TAG;

/**
 * Created by Raúl on 21/06/2017.
 */

public class BtConnectionThread extends Thread {

    private final BluetoothSocket socket;
    private final BluetoothDevice dispositivo;
    private final String UUID_SEGURO = "00001101-0000-1000-8000-00805F9B34FB";

    public BtConnectionThread(BluetoothDevice dispositivo) {
        BluetoothSocket tmpSocket = null;
        this.dispositivo = dispositivo;
        try {
            tmpSocket = dispositivo.createRfcommSocketToServiceRecord(UUID.fromString(UUID_SEGURO));
        } catch (IOException e) {
            Log.e(TAG, "HiloCliente.HiloCliente(): Error al abrir el socket", e);
        }
        this.socket = tmpSocket;
    }

    public void run() {
        setName("BtConnectionThread");
        if (MainActivity.bAdapter.isDiscovering())
            MainActivity.bAdapter.cancelDiscovery();

        try {
            this.socket.connect();
        } catch (IOException e) {
            Log.e(TAG, "HiloCliente.run(): socket.connect(): Error realizando la conexion", e);
            try {
                this.socket.close();
            } catch (IOException inner) {
                Log.e(TAG, "HiloCliente.run(): Error cerrando el socket", inner);
            }
        }

        BtComunicationThread comunication = new BtComunicationThread(this.socket);
        comunication.start();
    }

    public void cancelarConexion() {
        try {
            socket.close();
        } catch (IOException e) {
            Log.e(TAG, "HiloCliente.cancelarConexion(): Error al cerrar el socket", e);
        }

    }
}
