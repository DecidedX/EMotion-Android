package top.decided.emotion.cemuhook.body;

import java.util.Arrays;

import top.decided.emotion.cemuhook.ControllerInformation;
import top.decided.emotion.cemuhook.Utils;

public class InformationOutBody extends Body{

    ControllerInformation information;

    public InformationOutBody(int messageType, ControllerInformation information){
        this.messageType = messageType;
        this.information = information;
    }

    @Override
    public byte[] getByteArray(){
        byte[] bytes = Arrays.copyOf(Utils.int2ByteArray(Body.CONTROLLER_INFORMATION), 16);
        System.arraycopy(information.getByteArray(), 0, bytes, 4, 11);
        return bytes;
    }

}
