package top.decided.emotion.cemuhook.body;

import top.decided.emotion.cemuhook.Controller;

public class Body {

    public static final int PROTOCOL_VERSION = 0x100000;
    public static final int CONTROLLER_INFORMATION = 0x100001;
    public static final int ACTUAL_DATA = 0x100002;
    public static final int MOTORS_INFORMATION = 0x110001;
    public static final int RUMBLE_MOTORS = 0x110002;

    int messageType;
    byte[] bytes;

    public static Body generate(int messageType, Controller controller){
        Body body = null;
        if (messageType == CONTROLLER_INFORMATION){
            body = new InformationOutBody(messageType, controller.getInformation());
        }else if (messageType == ACTUAL_DATA){
            body =  new ActualDataOutBody(messageType, controller);
        }
        return body;
    }

    public byte[] getByteArray(){
        return bytes;
    }

}
