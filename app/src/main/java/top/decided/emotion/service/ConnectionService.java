package top.decided.emotion.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;

import top.decided.emotion.R;
import top.decided.emotion.cemuhook.Controller;
import top.decided.emotion.cemuhook.ControllerInformation;
import top.decided.emotion.config.Config;
import top.decided.emotion.listener.SensorChangeEventListener;
import top.decided.emotion.thread.ResponseThread;
import top.decided.emotion.thread.connection.QuickConnectThread;

public class ConnectionService extends Service {
    private static ConnectionService instance;
    private final ConnectionServiceBinder binder = new ConnectionServiceBinder();
    private DatagramSocket socket = null;
    private boolean serviceStatus = false;
    private final Controller controller = new Controller(ControllerInformation.DEVICE_FULL_GYRO);
    private ResponseThread responseThread = null;
    private QuickConnectThread quickConnectThread = null;
    private SensorManager sensorManager;
    private Sensor accSensor, gyroSensor;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        instance = this;
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        startForeground(1,createNotification());
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    public void start() throws IOException{
        start(Config.SERVICE_PORT, InetAddress.getByName(Config.getListenIP()));
        switchSensor(true);
    }

    public void start(int port, InetAddress laddr) throws IOException {
        if (socket != null){
            socket.close();
        }
        quickConnectThread = new QuickConnectThread(laddr);
        quickConnectThread.start();
        socket = new DatagramSocket(port, laddr);
        serviceStatus = true;
        responseThread = new ResponseThread(socket, controller);
        responseThread.start();
    }

    public QuickConnectThread getQuickConnectThread() {
        return quickConnectThread;
    }

    public DatagramSocket getSocket(){
        return socket;
    }

    public boolean getServiceStatus(){
        return serviceStatus;
    }

    public Controller getController() {
        return controller;
    }

    public void close(){
        serviceStatus = false;
        if (!socket.isClosed()){
            socket.close();
        }
        if (responseThread != null){
            responseThread.exit();
        }
        if (quickConnectThread != null){
            quickConnectThread.exit();
        }
        switchSensor(false);
    }

    private void switchSensor(boolean on){
        if (on){
            sensorManager.registerListener(new SensorChangeEventListener(), accSensor, SensorManager.SENSOR_DELAY_GAME);
            sensorManager.registerListener(new SensorChangeEventListener(), gyroSensor,SensorManager.SENSOR_DELAY_GAME);
        }else {
            sensorManager.unregisterListener(new SensorChangeEventListener(), accSensor);
            sensorManager.unregisterListener(new SensorChangeEventListener(), gyroSensor);
        }
    }

    public static ConnectionService getInstance() {
        return instance;
    }

    private Notification createNotification(){
        return new NotificationCompat.Builder(this, "ConnectionServiceChannel")
                .setContentTitle("EMotion Connection")
                .setContentText("EMotion connection is running in background")
                .setSmallIcon(R.drawable.ic_launcher)
                .build();
    }

    private void createNotificationChannel(){
        NotificationChannel serviceChannel = new NotificationChannel(
                "ConnectionServiceChannel",
                "ConnectionService",
                NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager manager = getSystemService(NotificationManager.class);
        if (manager != null){
            manager.createNotificationChannel(serviceChannel);
        }
    }

    public class ConnectionServiceBinder extends Binder {
        public ConnectionService getService(){
            return ConnectionService.this;
        }
    }
}
