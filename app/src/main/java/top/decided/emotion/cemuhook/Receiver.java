package top.decided.emotion.cemuhook;

import java.util.Arrays;
import java.util.zip.CRC32;

import top.decided.emotion.cemuhook.body.Body;
import top.decided.emotion.cemuhook.body.InformationInBody;

public class Receiver {

    private boolean result;
    private Header header;
    private InformationInBody body;
    private int messageType;
    private byte[] bytes;

    public Receiver(){
        result = false;
        header = null;
        body = null;
        messageType = 0;
    }

    public void update(byte[] bytes){
        if (bytes.length < 16) {
            result = false;
        }
        long crc32 = Utils.byteArray2Long(Arrays.copyOfRange(bytes, 8, 12));
        Arrays.fill(bytes, 8, 12, (byte) 0);
        CRC32 crc32gen = new CRC32();
        crc32gen.update(bytes);
        if (crc32 != crc32gen.getValue()){
            result = false;
        }else {
            result = true;
            this.bytes = bytes;
            messageType = Utils.byteArray2Int(Arrays.copyOfRange(bytes, 16, 20));
            header = new Header(Arrays.copyOfRange(bytes, 0, 16));
            body = messageType == Body.CONTROLLER_INFORMATION ?
                    new InformationInBody(Arrays.copyOfRange(bytes, 20,16+header.getPacketLength())) : null;
        }
    }

    public int getMessageType() {
        return messageType;
    }

    public Header getHeader() {
        return header;
    }

    public InformationInBody getBody() {
        return body;
    }

    public byte[] getBytes() {
        return bytes;
    }
}
