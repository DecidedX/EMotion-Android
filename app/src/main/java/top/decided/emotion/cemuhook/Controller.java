package top.decided.emotion.cemuhook;

import java.util.Arrays;

public class Controller {

    public static final int HOME_BUTTON = 0;
    public static final int TOUCH_BUTTON = 1;
    public static final int LEFT_STICK_X = 2;
    public static final int LEFT_STICK_Y = 3;
    public static final int RIGHT_STICK_X = 4;
    public static final int RIGHT_STICK_Y = 5;
    public static final int D_PAD_LEFT = 6;
    public static final int D_PAD_DOWN = 7;
    public static final int D_PAD_RIGHT = 8;
    public static final int D_PAD_UP = 9;
    public static final int Y = 10;
    public static final int B = 11;
    public static final int A = 12;
    public static final int X = 13;
    public static final int R1 = 14;
    public static final int L1 = 15;
    public static final int R2 = 16;
    public static final int L2 = 17;
    public static final int RIGHT_STICK = 18;
    public static final int LEFT_STICK = 19;
    public static final int SENSOR_ACC_X = 0;
    public static final int SENSOR_ACC_Y = 1;
    public static final int SENSOR_ACC_Z = 2;
    public static final int SENSOR_GYRO_PITCH = 3;
    public static final int SENSOR_GYRO_YAW = 4;
    public static final int SENSOR_GYRO_ROLL = 5;

    private final ControllerInformation information;
    private final byte[] buttons;
    private long timestamp;
    private final float[] sensor;
    private final Touch[] touches;

    public Controller(byte deviceModel){
        information = new ControllerInformation((byte) 0, deviceModel, "000000000000");
        buttons = new byte[20];
        timestamp = 0L;
        sensor = new float[6];
        touches = new Touch[]{new Touch(true), new Touch(false)};
    }

    public void update(int button, byte value){
        buttons[button] = value;
    }

    public void setPressed(int button, boolean pressed){
        buttons[button] = (byte) (pressed ? 255:0);
    }

    public void updateAccSensor(float[] values){
        updateSensor(0, values);
    }

    public void updateGyroSensor(float[] values){
        updateSensor(3, values);
    }

    public void updateSensor(int offset,float[] values){
        for (float value:values){
            sensor[offset++] = value;
        }
    }

    public Touch getTouch() {
        return touches[0];
    }

    public void updateTouch(boolean isFirst, boolean isActive){
        touches[isFirst ? 0 : 1].setStatus(isActive);
    }

    public void updateTouch(boolean isFirst, short x, short y){
        touches[isFirst ? 0 : 1].updateTouch(x, y);
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public ControllerInformation getInformation() {
        return information;
    }

    public byte[] getByteArray(){
        byte[] bytes = Arrays.copyOf(information.getByteArray(), 80);
        bytes[11] = 1;
        bytes[16] |= (buttons[18] != 0 ? 1 : 0) << 1;
        bytes[16] |= (buttons[19] != 0 ? 1 : 0) << 2;
        System.arraycopy(buttons, 0, bytes, 18, 18);
        System.arraycopy(touches[0].getByteArray(), 0, bytes, 36, 6);
        System.arraycopy(touches[1].getByteArray(), 0, bytes, 42, 6);
        System.arraycopy(Utils.long2ByteArray(timestamp), 0, bytes, 48, 8);
        for (int i=0; i < sensor.length; i++) {
            System.arraycopy(Utils.float2ByteArray(sensor[i]), 0, bytes, 56+i*4 , 4);
        }
        return bytes;
    }

}
