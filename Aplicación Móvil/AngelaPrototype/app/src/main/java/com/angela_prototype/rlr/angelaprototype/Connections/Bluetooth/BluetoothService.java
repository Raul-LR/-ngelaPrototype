package com.angela_prototype.rlr.angelaprototype.Connections.Bluetooth;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;

/**
 * Created by Raúl on 21/06/2017.
 */

public class BluetoothService{
        private static final String TAG = "com.angela_prototype.rlr.angelaprototype.Connections.Bluetooth.BluetoothService";

        public static final int ESTADO_NINGUNO = 0;
        public static final int ESTADO_CONECTADO = 1;
        public static final int ESTADO_REALIZANDO_CONEXION = 2;
        public static final int ESTADO_ATENDIENDO_PETICIONES = 3;

        public static final int MSG_READ = 11;

        private final Handler handler = null;
        private BluetoothSocket socket;

        private int state;
}

