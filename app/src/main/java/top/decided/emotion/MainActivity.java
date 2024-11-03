package top.decided.emotion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.function.Consumer;

import top.decided.emotion.cemuhook.Controller;
import top.decided.emotion.config.Config;
import top.decided.emotion.config.CustomLayoutData;
import top.decided.emotion.dialog.InputDialog;
import top.decided.emotion.dialog.SettingDialog;
import top.decided.emotion.fragment.BaseConFragment;
import top.decided.emotion.fragment.CustomConFragment;
import top.decided.emotion.fragment.FullConFragment;
import top.decided.emotion.fragment.NSConFragment;
import top.decided.emotion.listener.FullConRockerTouchListener;
import top.decided.emotion.service.Service;
import top.decided.emotion.widget.LayoutContainer;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

//    private ImageButton setting, unlockScreen;
    private SensorManager sensorManager;
    private Sensor accSensor, gyroSensor;
    private Vibrator vibrator;
    private Controller controller;
//    private GamePad gamePad;
    private SettingDialog settingDialog;
//    private Group lockScreen;
    private LayoutContainer layoutContainer;
//    private AppCompatSeekBar sizeSeekBar;
    BaseConFragment conFragment;
    private static Handler handler;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_con);
        Config.init(getApplication());
        screenCutoutConfigure();

        if (Service.getServiceStatus()) Service.close();
        //TODO:首次启动选择使用模式并存储，同时可在设置中切换使用模式
        handler = new MainActivityHandler(getMainLooper(), this);
        settingDialog = new SettingDialog(this, handler);

        if (Config.getCurrentLayout() == 3){
            Config.setCurrentLayout(2);
        }

        initLayout(Config.getCurrentLayout());

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        Config.setVibrator(vibrator);

        controller = Service.getController();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }

    @Override
    protected void onNightModeChanged(int mode) {
        super.onNightModeChanged(mode);
    }

    @Override
    protected void onResume() {
        super.onResume();
        switchSensor(sensorManager != null && Service.getServiceStatus());
    }

    @Override
    protected void onPause() {
        super.onPause();
        switchSensor(false);
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacksAndMessages(null);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onSensorChanged(SensorEvent event) {
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

    private void screenCutoutConfigure() {
        getWindow().getDecorView().post(() -> {
            Config.setCutout(getWindow().getDecorView().getRootWindowInsets().getDisplayCutout() != null);
            if (Config.isCutout() && Config.isUseCutout()) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
        });
    }

    private void initLayout(int layout){
        switch (layout){
            case 0:
            case 1:
                conFragment = NSConFragment.newInstance(controller, settingDialog, layout == 0);
                break;
            case 2:
                conFragment = FullConFragment.newInstance(controller, settingDialog);
                break;
            default:
                conFragment = CustomConFragment.newInstance(controller, settingDialog, layout);
                break;
        }
        switchConLayout();
    }

    private void switchConLayout(){
        FragmentManager fgm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fgm.beginTransaction();
        fragmentTransaction.replace(R.id.layoutFragmentViewer,conFragment);
        fragmentTransaction.commit();
    }

    private void switchSensor(boolean turnOn){
        if (turnOn){
            sensorManager.registerListener(MainActivity.this, accSensor, SensorManager.SENSOR_DELAY_GAME);
            sensorManager.registerListener(MainActivity.this, gyroSensor,SensorManager.SENSOR_DELAY_GAME);
        }else {
            sensorManager.unregisterListener(MainActivity.this, accSensor);
            sensorManager.unregisterListener(MainActivity.this, gyroSensor);
        }
    }

    private void switchLayout(int nextLayout){
        int currentLayout = Config.getCurrentLayout();
        if (currentLayout < 2 && nextLayout < 2){
            ((NSConFragment) conFragment).switchLRCon(nextLayout == 0);
        }else if (currentLayout >= 3 && nextLayout >= 3){
            if (nextLayout == 3){
                ((CustomConFragment) conFragment).setEditMode(true);
            }
            ((CustomConFragment) conFragment).setLayout(nextLayout);
        }else {
            initLayout(nextLayout);
        }
        Config.setCurrentLayout(nextLayout);
    }

    private void switchNSLayout(boolean bool){
        boolean abxySwitch = Config.isAbxySwitch();
        if (abxySwitch == bool || Config.getCurrentLayout() < 2)
            return;
        Config.setAbxySwitch(bool);
        ((FullConFragment) conFragment).abxySwitch();
    }

    private void startVibrate(long time, int amplitude){
        if (!Config.isVibration())
            return;
        VibrationEffect effect = VibrationEffect.createOneShot(time, amplitude);
        vibrator.vibrate(effect);
    }

    private void expandToCutout(boolean useCutout){
        if (useCutout){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }else getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        Config.setUseCutout(useCutout);
    }

    public static void pressedVibration(boolean buttonVibration, Vibrator vibrator){
        if (!buttonVibration)
            return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK));
        }else {
            vibrator.vibrate(VibrationEffect.createOneShot(1, 255));
        }
    }

    public static Handler getHandler(){
        return handler;
    }

    private static class MainActivityHandler extends Handler{

        private final WeakReference<MainActivity> mainActivityWeakReference;

        public MainActivityHandler(@NonNull Looper looper, MainActivity mainActivity){
            super(looper);
            this.mainActivityWeakReference = new WeakReference<>(mainActivity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    mainActivityWeakReference.get().switchSensor((boolean) msg.obj);
                    break;
                case 1:
                    mainActivityWeakReference.get().switchLayout((int) msg.obj);
                    break;
                case 2:
                    mainActivityWeakReference.get().switchNSLayout((boolean) msg.obj);
                    break;
                case 3:
                    mainActivityWeakReference.get().startVibrate(100, (int) msg.obj);
                    break;
                case 4:
                    mainActivityWeakReference.get().conFragment.lockScreen((boolean) msg.obj);
                    break;
                case 5:
                    mainActivityWeakReference.get().expandToCutout((boolean)msg.obj);
                    break;
                case 6:
                    ((CustomConFragment) mainActivityWeakReference.get().conFragment).setEditMode((boolean) msg.obj);
                    break;
                case 7:
                    ((CustomConFragment) mainActivityWeakReference.get().conFragment).saveNewCustomLayout((String) msg.obj);
                    break;
            }
        }

    }

}