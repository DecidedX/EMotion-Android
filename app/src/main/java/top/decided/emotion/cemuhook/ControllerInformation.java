package top.decided.emotion.cemuhook;

public class ControllerInformation {

    //Slot State
    public static final byte SLOT_NOT_CONNECTED = 0;
    public static final byte SLOT_RESERVED = 1;
    public static final byte SLOT_CONNECTED = 2;
    //Device Model
    public static final byte DEVICE_NOT_APPLICABLE = 0;
    public static final byte DEVICE_NO_OR_PARTIAL_GYRO = 1;
    public static final byte DEVICE_FULL_GYRO = 2;
    //Connection Type
    public static final byte CONNECTION_NOT_APPLICABLE = 0;
    public static final byte CONNECTION_FOR_USB = 1;
    public static final byte CONNECTION_FOR_BLUETOOTH = 2;
    //Battery Value
    public static final byte BATTERY_NOT_APPLICABLE = 0x00;
    public static final byte BATTERY_DYING = 0x01;
    public static final byte BATTERY_LOW = 0x02;
    public static final byte BATTERY_MEDIUM = 0x03;
    public static final byte BATTERY_HIGH = 0x04;
    public static final byte BATTERY_FULL = 0x05;
    public static final byte BATTERY_CHARGING = (byte) 0xEE;
    public static final byte BATTERY_CHARGED = (byte) 0xEF;

    byte slot, slotState, deviceModel, connectionType, batteryStatus;
    String macAddress;

    public ControllerInformation(byte slot, byte deviceModel, String macAddress) {
        this.slot = slot;
        this.slotState = SLOT_CONNECTED;
        this.deviceModel = deviceModel;
        this.connectionType = CONNECTION_FOR_BLUETOOTH;
        this.batteryStatus = BATTERY_CHARGING;
        this.macAddress = macAddress;
    }

    public byte getSlot() {
        return slot;
    }

    public void setSlot(byte slot) {
        this.slot = slot;
    }

    public byte getSlotState() {
        return slotState;
    }

    public void setSlotState(byte slotState) {
        this.slotState = slotState;
    }

    public byte getDeviceModel() {
        return deviceModel;
    }

    public byte getConnectionType() {
        return connectionType;
    }

    public byte getBatteryStatus() {
        return batteryStatus;
    }

    public void setBatteryStatus(byte batteryStatus) {
        this.batteryStatus = batteryStatus;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public byte[] getByteArray(){
        byte[] bytes = new byte[11];
        bytes[0] = slot;
        bytes[1] = slotState;
        bytes[2] = deviceModel;
        bytes[3] = connectionType;
        System.arraycopy(Utils.reverse(macAddress.getBytes()), 0, bytes, 4, 6);
        bytes[10] = batteryStatus;
        return bytes;
    }

}
