package top.decided.emotion.listener;

import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;

import top.decided.emotion.cemuhook.Controller;
import top.decided.emotion.config.Config;
import top.decided.emotion.service.ConnectionService;
import top.decided.emotion.utils.Gyro;

public class SensorChangeEventListener implements android.hardware.SensorEventListener {

    @SuppressLint("DefaultLocale")
    @Override
    public void onSensorChanged(SensorEvent event) {
        Controller controller = ConnectionService.getInstance().getController();
        Gyro.GyroDirection direction = Gyro.GyroDirection.FULL_DEFAULT;
        switch (Config.getCurrentLayout()){
            case 0:
                direction = Gyro.GyroDirection.LEFT_DEFAULT;
                break;
            case 1:
                direction = Gyro.GyroDirection.RIGHT_DEFAULT;
                break;
        }
        direction = Config.getGyroDirection() == Gyro.GyroDirection.FULL_DEFAULT ? direction : Config.getGyroDirection();
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            controller.updateAccSensor(Gyro.processAcc(event.values, direction));
            long nanoTime = System.nanoTime();
            controller.setTimestamp(System.currentTimeMillis() * 1000 + (nanoTime - nanoTime / 1000000 * 1000000) / 1000);
        }else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE){
            controller.updateGyroSensor(Gyro.processGyro(event.values, direction));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

}
