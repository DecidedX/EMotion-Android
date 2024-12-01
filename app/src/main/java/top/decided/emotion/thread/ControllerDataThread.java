package top.decided.emotion.thread;

import android.os.Message;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import top.decided.emotion.MainActivity;
import top.decided.emotion.cemuhook.Controller;
import top.decided.emotion.cemuhook.Sender;
import top.decided.emotion.cemuhook.Utils;
import top.decided.emotion.cemuhook.body.Body;
import top.decided.emotion.service.ConnectionService;

public class ControllerDataThread extends Thread {

    private final DatagramSocket socket;
    private final Controller controller;
    private final Sender sender;
    private int start;
    private boolean exit;
    private int count;
    private InetAddress address;
    private int port;

    public ControllerDataThread(DatagramSocket socket, Controller controller, Sender sender){
        this.socket = socket;
        this.controller = controller;
        this.sender = sender;
        count = 0;
        start = 0;
        exit = false;
    }

    public void startSend(InetAddress address, int port){
        if (start == 0){
            this.address = address;
            this.port = port;
        }
        start = 1000;
    }

    public void pauseSend(){
        start = 0;
    }

    public void exit(){
        exit = true;
    }

    @Override
    public void run() {
        while (!exit){
            for (; start > 0; start--) {
                if (count < 0) count = 0;
                sender.update(Body.ACTUAL_DATA, controller);
                byte[] bytes = sender.getByteArray();
                System.arraycopy(Utils.int2ByteArray(count), 0, bytes, 32, 4);
                DatagramPacket response;
                try {
                    response = new DatagramPacket(sender.doCrc32(bytes), bytes.length, address, port);
                    socket.send(response);
                    sleep(5);
                } catch (IOException | InterruptedException e) {
                    ConnectionService.getInstance().close();
                }
                count++;
            }
        }
    }

}
