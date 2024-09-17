package top.decided.emotion.cemuhook.body;

import java.util.Arrays;

import top.decided.emotion.cemuhook.Controller;
import top.decided.emotion.cemuhook.Utils;

public class ActualDataOutBody extends Body{

    Controller controller;

    public ActualDataOutBody(int messageType, Controller controller){
        this.messageType = messageType;
        this.controller = controller;
    }

    @Override
    public byte[] getByteArray() {
        byte[] bytes = Arrays.copyOf(Utils.int2ByteArray(Body.ACTUAL_DATA), 84);
        System.arraycopy(controller.getByteArray(), 0, bytes, 4, 80);
        return bytes;
    }

}
