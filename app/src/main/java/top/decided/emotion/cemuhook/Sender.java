package top.decided.emotion.cemuhook;

import java.util.zip.CRC32;

import top.decided.emotion.cemuhook.body.Body;

public class Sender {

    Header header;
    Body body;

    public Sender(long deviceID) {
        this.header = new Header(Header.SERVER_MAGIC_STRING, deviceID);
    }

    public void update(int messageType, Controller controller){
        this.body = Body.generate(messageType, controller);
    }

    public byte[] getByteArray(){
        byte[] body = this.body.getByteArray();
        header.packetLength = body.length;
        byte[] bytes = new byte[16+body.length];
        System.arraycopy(header.toByteArray(), 0, bytes, 0, 16);
        System.arraycopy(body, 0, bytes, 16, body.length);
        return bytes;
    }

    public byte[] getByteArrayWithCRC32(){
        return doCrc32(getByteArray());
    }

    public byte[] doCrc32(byte[] bytes){
        CRC32 crc32gen = new CRC32();
        crc32gen.update(bytes);
        System.arraycopy(Utils.int2ByteArray((int) crc32gen.getValue()), 0, bytes, 8, 4);
        return bytes;
    }

}
