package top.decided.emotion.cemuhook;

import java.math.BigInteger;
import java.nio.ByteBuffer;

public class Utils {

    public static byte[] reverse(byte[] bytes){
        int length = bytes.length;
        for (int i=0, j=length-1; i < length/2; i++, j--){
            byte temp = bytes[i];
            bytes[i] = bytes[j];
            bytes[j] = temp;
        }
        return bytes;
    }

    public static int byteArray2Int(byte[] bytes){
        return new BigInteger(1, Utils.reverse(bytes)).intValue();
    }

    public static long byteArray2Long(byte[] bytes){
        return new BigInteger(1, Utils.reverse(bytes)).longValue();
    }

    public static byte[] int2ByteArray(int value){
        byte[] bytes = new byte[4];
        bytes[0] = (byte) (value & 0xFF);
        bytes[1] = (byte) ((value >> 8) & 0xFF);
        bytes[2] = (byte) ((value >> 16) & 0xFF);
        bytes[3] = (byte) ((value >> 24) & 0xFF);
        return bytes;
    }

    public static byte[] short2ByteArray(short value){
        byte[] bytes = new byte[2];
        bytes[0] = (byte) (value & 0xFF);
        bytes[1] = (byte) ((value >> 8) & 0xFF);
        return bytes;
    }

    public static byte[] long2ByteArray(long value){
        ByteBuffer byteBuffer = ByteBuffer.allocate(8);
        byteBuffer.putLong(value);
        return reverse(byteBuffer.array());
    }

    public static byte[] float2ByteArray(float value){
        ByteBuffer byteBuffer = ByteBuffer.allocate(4);
        byteBuffer.putFloat(value);
        return reverse(byteBuffer.array());
    }

}
