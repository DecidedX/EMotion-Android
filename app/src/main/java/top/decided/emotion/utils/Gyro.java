package top.decided.emotion.utils;

public class Gyro {

    public enum GyroDirection {
        FULL_DEFAULT,
        LEFT_DEFAULT,
        RIGHT_DEFAULT,
        HAND_RIGHT,
        HAND_LEFT
    }

    public static float[] processGyro(float[] values, GyroDirection direction){
        float[] result = new float[3];
        switch (direction){
            case FULL_DEFAULT:
                result[0] = (float) - Math.toDegrees(values[1]);
                result[1] = (float) - Math.toDegrees(values[2]);
                result[2] = (float) Math.toDegrees(values[0]);
                break;
            case LEFT_DEFAULT:
                result[0] = (float) - Math.toDegrees(values[0]);
                result[1] = (float) - Math.toDegrees(values[2]);
                result[2] = (float) - Math.toDegrees(values[1]);
                break;
            case RIGHT_DEFAULT:
                result[0] = (float) Math.toDegrees(values[0]);
                result[1] = (float) - Math.toDegrees(values[2]);
                result[2] = (float) Math.toDegrees(values[1]);
                break;
            case HAND_RIGHT:
                result[0] = (float) - Math.toDegrees(values[2]);
                result[1] = (float) - Math.toDegrees(values[0]);
                result[2] = (float) Math.toDegrees(values[1]);
                break;
            case HAND_LEFT:
                result[0] = (float) Math.toDegrees(values[2]);
                result[1] = (float) Math.toDegrees(values[0]);
                result[2] = (float) Math.toDegrees(values[1]);
                break;
        }
        return result;
    }

    public static float[] processAcc(float[] values, GyroDirection direction){
        float[] result = new float[3];
        switch (direction){
            case FULL_DEFAULT:
                result[0] = values[1] / 9.81F;
                result[1] = -values[2] / 9.81F;
                result[2] = values[0] / 9.81F;
                break;
            case LEFT_DEFAULT:
                result[0] = values[0] / 9.81F;
                result[1] = -values[2] / 9.81F;
                result[2] = -values[1] / 9.81F;
                break;
            case RIGHT_DEFAULT:
                result[0] = -values[0] / 9.81F;
                result[1] = -values[2] / 9.81F;
                result[2] = values[1] / 9.81F;
                break;
            case HAND_RIGHT:
                result[0] = values[2] / 9.81F;
                result[1] = -values[0] / 9.81F;
                result[2] = values[1] / 9.81F;
                break;
            case HAND_LEFT:
                result[0] = -values[2] / 9.81F;
                result[1] = values[0] / 9.81F;
                result[2] = values[1] / 9.81F;
                break;
        }
        return result;
    }

}
