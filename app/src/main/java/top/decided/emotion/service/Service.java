package top.decided.emotion.service;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;

import top.decided.emotion.cemuhook.Controller;
import top.decided.emotion.cemuhook.ControllerInformation;
import top.decided.emotion.config.Config;
import top.decided.emotion.thread.ResponseThread;
import top.decided.emotion.thread.connection.QuickConnectThread;

public class Service {

    private static DatagramSocket socket = null;
    private static boolean serviceStatus = false;
    private static Controller controller = new Controller(ControllerInformation.DEVICE_FULL_GYRO);
    private static ResponseThread responseThread = null;
    private static QuickConnectThread quickConnectThread = null;

    public static void start() throws IOException{
        start(Config.SERVICE_PORT, InetAddress.getByName(Config.getListenIP()));
    }

    public static void start(int port, InetAddress laddr) throws IOException {
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

    public static QuickConnectThread getQuickConnectThread() {
        return quickConnectThread;
    }

    public static DatagramSocket getSocket(){
        return socket;
    }

    public static boolean getServiceStatus(){
        return serviceStatus;
    }

    public static Controller getController() {
        return controller;
    }

    public static void close(){
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
    }

}
