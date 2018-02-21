package com.angela_prototype.rlr.angelaprototype.Activities;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.angela_prototype.rlr.angelaprototype.Adapters.AlertsAdapter;
import com.angela_prototype.rlr.angelaprototype.Bluetooth.Bluetooth;
import com.angela_prototype.rlr.angelaprototype.Model.AlertExtraction;
import com.angela_prototype.rlr.angelaprototype.Model.SendAlert;
import com.angela_prototype.rlr.angelaprototype.Pojos.Alert;
import com.angela_prototype.rlr.angelaprototype.Pojos.User;
import com.angela_prototype.rlr.angelaprototype.Pojos.UserCredentials;
import com.angela_prototype.rlr.angelaprototype.R;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.UUID;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Bluetooth.CommunicationCallback {

    private AlertExtraction alertsExtracted;
    private ListView list;

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 0;
    private UserCredentials current_userCredentials;
    private User current_user;

    private Bluetooth b;
    private boolean registered = false;

    private String macAddress = "98:D3:31:FB:46:66";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = this.getIntent().getExtras();
        this.current_userCredentials = new UserCredentials(extras.getInt("userCredentials_id"),
                extras.getInt("user_id"),
                extras.getString("email"),
                extras.getString("username"),
                extras.getString("password"));
        this.current_user = new User(this.current_userCredentials,
                extras.getInt("user_id"),
                extras.getString("DNI"),
                extras.getString("nombre"),
                extras.getString("apellido1"),
                extras.getString("apellido2"),
                extras.getString("tarjetaSanitaria"),
                extras.getString("localidad"),
                extras.getString("municipio"),
                extras.getString("direccion"),
                extras.getInt("portal"),
                extras.getString("puerta"),
                extras.getInt("cp"));

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = LayoutInflater.from(this).inflate(R.layout.nav_header_main, null);
        navigationView.addHeaderView(header);
        TextView name = (TextView) header.findViewById(R.id.name);
        TextView email = (TextView) header.findViewById(R.id.email);

        name.setText(this.current_user.getNombre() + " " + this.current_user.getApellido1());
        email.setText(this.current_user.getCredentials().getEmail());

        b = new Bluetooth(this);
        b.enableBluetooth();

        b.setCommunicationCallback(this);

        b.connectToAddress(macAddress);

        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mReceiver, filter);
        registered = true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (registered) {
            unregisterReceiver(mReceiver);
            registered = false;
        }
    }

    public void Display(final String s) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getBaseContext(), s, Toast.LENGTH_LONG).show();
                System.out.println(s);
            }
        });
    }

    @Override
    public void onConnect(BluetoothDevice device) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getBaseContext(), "Pulsera emparejada", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onDisconnect(BluetoothDevice device, String message) {
        Display("Pulsera desconectada");
        Display("Reintentando emparejamiento");
        b.connectToDevice(device);
    }

    @Override
    public void onMessage(String message) {
        Time now = new Time(Time.getCurrentTimezone());
        System.out.println(message);
        String type = "";
        String lectures = "";
        String problem = "";
        String temp = "";
        int i = 0;
        StringTokenizer st = new StringTokenizer(message, "#");
        while (st.hasMoreTokens()) {
            temp = st.nextToken();
            if (i == 2) {
                type = temp;
            }
            if (i == 3) {
                problem = temp;
            }
            if (i == 4) {
                lectures = temp;
            }
            i++;
        }
        Alert alert = new Alert(type, problem, lectures, current_user.getId());
        System.out.println(alert.toString());
        if (alert.getType().equals("Leve")) {
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(MainActivity.this)
                            .setSmallIcon(android.R.drawable.stat_sys_warning)
                            .setLargeIcon((((BitmapDrawable) getResources()
                                    .getDrawable(R.mipmap.ic_launcher)).getBitmap()))
                            .setContentTitle("Alerta Leve")
                            .setContentText("Tiene un/a " + alert.getProblem())
                            .setContentInfo("4")
                            .setTicker("Aviso sanitario");
            /*
            Intent notIntent =
                    new Intent(MainActivity.this, MainActivity.class);
            PendingIntent contIntent =
                    PendingIntent.getActivity(
                            MainActivity.this, 0, notIntent, 0);
            mBuilder.setContentIntent(contIntent);
            */
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(10, mBuilder.build());

        } else if (alert.getType().equals("Grave")) {
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(MainActivity.this)
                            .setSmallIcon(android.R.drawable.stat_sys_warning)
                            .setLargeIcon((((BitmapDrawable) getResources()
                                    .getDrawable(R.mipmap.ic_launcher)).getBitmap()))
                            .setContentTitle("Alerta Grave")
                            .setContentText("Está sufriendo un/a " + alert.getProblem())
                            .setContentInfo("4")
                            .setTicker("¡Alerta sanitaria!");

            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(12, mBuilder.build());

            /*
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:958992425"));
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        MY_PERMISSIONS_REQUEST_CALL_PHONE);
            }
            startActivity(intent);
            */
        }

        SendAlert sendAlert = new SendAlert(alert);
        sendAlert.execute();
        if(sendAlert.doInBackground()==false){
            Display("Ha sucedido un error, y no se ha podido subir la alerta");
        }else{
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertExtraction alertsExtracted = new AlertExtraction(current_user.getId());
                    alertsExtracted.execute();
                    ArrayList<Alert> alerts = alertsExtracted.doInBackground();
                    AlertsAdapter adapter = new AlertsAdapter(MainActivity.this, alerts);

                    list = (ListView) findViewById(R.id.list_items);
                    list.setAdapter(adapter);
                }
            });
        }
    }

    @Override
    public void onError(String message) {
        Display("Ha ocurrido un error");
    }

    @Override
    public void onConnectError(final BluetoothDevice device, String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        b.connectToDevice(device);
                    }
                }, 2000);
            }
        });
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                Intent intent1 = new Intent(MainActivity.this, LoginActivity.class);

                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        if(registered) {
                            unregisterReceiver(mReceiver);
                            registered=false;
                        }
                        startActivity(intent1);
                        finish();
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        if(registered) {
                            unregisterReceiver(mReceiver);
                            registered=false;
                        }
                        startActivity(intent1);
                        finish();
                        break;
                }
            }
        }
    };
}
