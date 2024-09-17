package top.decided.emotion.thread;

import android.os.Message;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;
import java.util.Date;

import top.decided.emotion.MainActivity;
import top.decided.emotion.cemuhook.Controller;
import top.decided.emotion.cemuhook.Header;
import top.decided.emotion.cemuhook.Receiver;
import top.decided.emotion.cemuhook.Sender;
import top.decided.emotion.cemuhook.body.Body;
import top.decided.emotion.cemuhook.body.InformationInBody;
import top.decided.emotion.service.Service;

public class ResponseThread extends Thread{

    private final DatagramSocket socket;
    private final long serverId;
    private final Receiver receiver;
    private final Sender sender;
    private final Controller controller;
    private final ControllerDataThread dataThread;
    private boolean exit;

    public ResponseThread(DatagramSocket socket, Controller controller){
        this.socket = socket;
        serverId = new Date().getTime();
        receiver = new Receiver();
        sender = new Sender(serverId);
        this.controller = controller;
        this.dataThread = new ControllerDataThread(socket, controller, sender);
        exit = false;
    }

    public ControllerDataThread getDataThread() {
        return dataThread;
    }

    @Override
    public void run() {
        dataThread.start();
        byte slot = controller.getInformation().getSlot();
        while (!exit) {
            byte[] recvBuffer = new byte[128];
            DatagramPacket packet = new DatagramPacket(recvBuffer, recvBuffer.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                Message msg = Message.obtain();
                msg.what = 0;
                msg.obj = false;
                MainActivity.getHandler().sendMessage(msg);
                Service.close();
            }
            receiver.update(Arrays.copyOfRange(packet.getData(), 0, packet.getLength()));
            if (receiver.getMessageType() == Body.CONTROLLER_INFORMATION) {
                if (receiver.getHeader().getMagicString().equals(Header.CLIENT_MAGIC_STRING)){
                    InformationInBody body = receiver.getBody();
                    byte[] slots = body.getSlots();
                    for (int i=0; i < body.getAmountOfPorts(); i++){
                        DatagramPacket response;
                        byte[] bytes;
                        if (slot != slots[i]){
                            slot = slots[i];
                            controller.getInformation().setSlot(slots[i]);
                        }
                        sender.update(receiver.getMessageType(), controller);
                        bytes = sender.getByteArrayWithCRC32();
                        response = new DatagramPacket(bytes, bytes.length, packet.getAddress(), packet.getPort());
                        try {
                            socket.send(response);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }else if (receiver.getMessageType() == Body.ACTUAL_DATA){
                dataThread.startSend(packet.getAddress(), packet.getPort());
                Service.getQuickConnectThread().exit();
            }else if (receiver.getMessageType() == Body.RUMBLE_MOTORS){
                int motor = Byte.toUnsignedInt(receiver.getBytes()[29]);
                if (motor != 0){
                    Message msg = Message.obtain();
                    msg.what = 3;
                    msg.obj = motor;
                    MainActivity.getHandler().sendMessage(msg);
                }
            }
        }
    }

    public void exit(){
        dataThread.exit();
        exit = true;
    }

}
