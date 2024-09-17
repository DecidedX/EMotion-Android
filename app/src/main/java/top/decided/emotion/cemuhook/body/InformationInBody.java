package top.decided.emotion.cemuhook.body;

import java.util.Arrays;

import top.decided.emotion.cemuhook.Utils;

public class InformationInBody {

    int amountOfPorts;
    byte[] slots;

    public InformationInBody(byte[] body){
        amountOfPorts = Utils.byteArray2Int(Arrays.copyOfRange(body, 0, 4));
        slots = Arrays.copyOfRange(body, 4, 8);
    }

    public int getAmountOfPorts() {
        return amountOfPorts;
    }

    public byte[] getSlots() {
        return slots;
    }

}
