package top.decided.emotion.cemuhook;

import java.util.Arrays;

public class Header {

    public static String SERVER_MAGIC_STRING = "DSUS";
    public static String CLIENT_MAGIC_STRING = "DSUC";

    String magicString;
    int protocol_version;
    int packetLength;
    long crc32;
    long deviceID;

    public Header(byte[] header){
        magicString = new String(Arrays.copyOfRange(header, 0, 4));
        protocol_version = Utils.byteArray2Int(Arrays.copyOfRange(header, 4, 6));
        packetLength = Utils.byteArray2Int(Arrays.copyOfRange(header, 6, 8));
        crc32 = Utils.byteArray2Long(Arrays.copyOfRange(header, 8, 12));
        deviceID = Utils.byteArray2Long(Arrays.copyOfRange(header, 12, 16));
    }

    public Header(String magicString, long deviceID) {
        this.magicString = magicString;
        this.protocol_version = 1001;
        this.deviceID = deviceID;
    }

    public byte[] toByteArray(){
        byte[] header = new byte[16];
        System.arraycopy(magicString.getBytes(), 0, header, 0, 4);
        System.arraycopy(Utils.int2ByteArray(protocol_version), 0, header, 4, 2);
        System.arraycopy(Utils.int2ByteArray(packetLength), 0, header, 6, 2);
        System.arraycopy(Utils.int2ByteArray((int) deviceID), 0, header, 12, 4);
        return header;
    }

    public String getMagicString() {
        return magicString;
    }

    public int getProtocol_version() {
        return protocol_version;
    }

    public int getPacketLength() {
        return packetLength;
    }

    public long getCrc32() {
        return crc32;
    }

    public long getDeviceID() {
        return deviceID;
    }

}
