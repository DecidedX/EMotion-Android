package top.decided.emotion.cemuhook;

public class Touch {

    private boolean isActive;
    private byte id;
    private short x, y;

    public Touch(boolean isFirst){
        isActive = false;
        id = (byte) (isFirst ? 1:2);
        x = 0;
        y = 0;
    }

    public void updateTouch(short x, short y){
        this.x = x;
        this.y = y;
    }

    public void setStatus(boolean isActive){
        this.isActive = isActive;
        if (!isActive){
            x = 0;
            y = 0;
        }
    }

    public byte[] getByteArray(){
        byte[] bytes = new byte[6];
        bytes[0] = (byte) (isActive ? 1 : 0);
        bytes[1] = id;
        if (isActive){
            System.arraycopy(Utils.short2ByteArray(x), 0, bytes, 2, 2);
            System.arraycopy(Utils.short2ByteArray(y), 0, bytes, 4, 2);
        }
        return bytes;
    }

}
