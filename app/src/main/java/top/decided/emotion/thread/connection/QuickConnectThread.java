package top.decided.emotion.thread.connection;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import top.decided.emotion.config.Config;

public class QuickConnectThread extends Thread {

    private final InetAddress broadcastAddress;
    private final InetAddress laddr;
    private boolean exit = false;
    private DatagramSocket socket;

    public QuickConnectThread(InetAddress laddr) throws UnknownHostException {
        this.laddr = laddr;
        broadcastAddress = toBroadcastAddress(laddr);
    }

    @Override
    public void run() {
        try{
            socket = new DatagramSocket(Config.QUICK_CONN_PORT);
            byte[] msg = laddr.getAddress();
            DatagramPacket packet = new DatagramPacket(msg, msg.length, broadcastAddress, Config.QUICK_CONN_PORT);
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        if(!exit) socket.send(packet);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }, 0, 2500);
            byte[] recvBuff = new byte[4];
            DatagramPacket ackPacket = new DatagramPacket(recvBuff, recvBuff.length);
            while (!exit){
                socket.receive(ackPacket);
                if (!ackPacket.getAddress().equals(laddr) && Objects.equals(ackPacket.getAddress(), InetAddress.getByAddress(ackPacket.getData()))){
                    exit();
                }
            }
            timer.cancel();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exit(){
        exit = true;
        socket.close();
    }

    private InetAddress toBroadcastAddress(InetAddress laddr) throws UnknownHostException {
        byte[] addr = laddr.getAddress();
        addr[3] = (byte) 255;
        return InetAddress.getByAddress(addr);
    }

}
