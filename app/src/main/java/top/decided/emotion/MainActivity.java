package top.decided.emotion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.WindowManager;

import java.io.IOException;
import java.lang.ref.WeakReference;

import top.decided.emotion.cemuhook.Controller;
import top.decided.emotion.config.Config;
import top.decided.emotion.dialog.SettingDialog;
import top.decided.emotion.fragment.BaseConFragment;
import top.decided.emotion.fragment.CustomConFragment;
import top.decided.emotion.fragment.FullConFragment;
import top.decided.emotion.fragment.NSConFragment;
import top.decided.emotion.service.ConnectionService;
import top.decided.emotion.service.FloatingModeService;
import top.decided.emotion.utils.HandlerCaseType;

public class MainActivity extends AppCompatActivity {
    private Vibrator vibrator;
    private Controller controller;
    private SettingDialog settingDialog;
    BaseConFragment conFragment;
    private static Handler handler;
    private FloatingModeService floatingService;
    private ConnectionService connectionService;
    private final ServiceConnection floatingServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            floatingService = ((FloatingModeService.FloatingServiceBinder) iBinder).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            floatingService = null;
        }
    };

    private final ServiceConnection connectionServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            connectionService = ((ConnectionService.ConnectionServiceBinder) iBinder).getService();
            controller = connectionService.getController();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            connectionService = null;
        }
    };

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_con);
        Config.init(getApplication());
        screenCutoutConfigure();

        bindService(new Intent(this, ConnectionService.class), connectionServiceConnection, Context.BIND_AUTO_CREATE);

        //TODO:首次启动选择使用模式并存储，同时可在设置中切换使用模式
        handler = new MainActivityHandler(getMainLooper(), this);
        settingDialog = new SettingDialog(this, handler);

        if (Config.getCurrentLayout() == 3){
            Config.setCurrentLayout(2);
        }

        initLayout(Config.getCurrentLayout());

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        Config.setVibrator(vibrator);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }

    @Override
    protected void onNightModeChanged(int mode) {
        super.onNightModeChanged(mode);
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacksAndMessages(null);
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
        conFragment = BaseConFragment.getConFragment(layout, controller, settingDialog);
        switchConLayout();
    }

    private void switchConLayout(){
        FragmentManager fgm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fgm.beginTransaction();
        fragmentTransaction
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                .replace(R.id.layoutFragmentViewer,conFragment);
        fragmentTransaction.commit();
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

    private void expandToCutout(boolean useCutout) throws IOException {
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

    public void setFloatingMode(boolean open){
        if (open){
            startFloatingModeService();
        }else {
            unbindService(floatingServiceConnection);
        }
    }

    private void startFloatingModeService(){
        Intent startFMS = new Intent(this, FloatingModeService.class);
        bindService(startFMS, floatingServiceConnection, Context.BIND_AUTO_CREATE);
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
                case HandlerCaseType.SWITCH_LAYOUT:
                    mainActivityWeakReference.get().switchLayout((int) msg.obj);
                    break;
                case HandlerCaseType.SWITCH_ABXY:
                    mainActivityWeakReference.get().switchNSLayout((boolean) msg.obj);
                    break;
                case HandlerCaseType.START_VIBRATE:
                    mainActivityWeakReference.get().startVibrate(100, (int) msg.obj);
                    break;
                case HandlerCaseType.LOCK_SCREEN:
                    mainActivityWeakReference.get().conFragment.lockScreen((boolean) msg.obj);
                    break;
                case HandlerCaseType.EXPAND_CUTOUT:
                    try {
                        mainActivityWeakReference.get().expandToCutout((boolean)msg.obj);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case HandlerCaseType.EDIT_MODE:
                    ((CustomConFragment) mainActivityWeakReference.get().conFragment).setEditMode((boolean) msg.obj);
                    break;
                case HandlerCaseType.SAVE_CUSTOM_LAYOUT:
                    ((CustomConFragment) mainActivityWeakReference.get().conFragment).saveNewCustomLayout((String) msg.obj);
                    break;
                case HandlerCaseType.SET_FLOATING:
                    mainActivityWeakReference.get().setFloatingMode((boolean) msg.obj);
                    break;
            }
        }

    }

}