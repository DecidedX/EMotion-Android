package top.decided.emotion.listener;

import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;

import top.decided.emotion.cemuhook.Controller;
import top.decided.emotion.config.Config;
import top.decided.emotion.service.ConnectionService;

public class SensorChangeEventListener implements android.hardware.SensorEventListener {

    @SuppressLint("DefaultLocale")
    @Override
    public void onSensorChanged(SensorEvent event) {
        Controller controller = ConnectionService.getInstance().getController();
        float[] values = new float[3];
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            switch (Config.getGyroDirection()){
                case 0:
                    values[0] = event.values[1] / 9.81F;
                    values[1] = -event.values[2] / 9.81F;
                    values[2] = event.values[0] / 9.81F;
                    break;
                case 1:
                    values[0] = event.values[2] / 9.81F;
                    values[1] = -event.values[0] / 9.81F;
                    values[2] = event.values[1] / 9.81F;
                    break;
                case 2:
                    values[0] = -event.values[2] / 9.81F;
                    values[1] = event.values[0] / 9.81F;
                    values[2] = event.values[1] / 9.81F;
                    break;
            }

            controller.updateAccSensor(values);
            long nanoTime = System.nanoTime();
            controller.setTimestamp(System.currentTimeMillis() * 1000 + (nanoTime - nanoTime / 1000000 * 1000000) / 1000);
        }else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE){
            switch (Config.getGyroDirection()){
                case 0:
                    values[0] = (float) - Math.toDegrees(event.values[1]);
                    values[1] = (float) - Math.toDegrees(event.values[2]);
                    values[2] = (float) Math.toDegrees(event.values[0]);
                    break;
                case 1:
                    values[0] = (float) - Math.toDegrees(event.values[2]);
                    values[1] = (float) - Math.toDegrees(event.values[0]);
                    values[2] = (float) Math.toDegrees(event.values[1]);
                    break;
                case 2:
                    values[0] = (float) Math.toDegrees(event.values[2]);
                    values[1] = (float) Math.toDegrees(event.values[0]);
                    values[2] = (float) Math.toDegrees(event.values[1]);
                    break;
            }
            controller.updateGyroSensor(values);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

}
