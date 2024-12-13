package top.decided.emotion.controller;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.constraintlayout.widget.Group;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import top.decided.emotion.MainActivity;
import top.decided.emotion.R;
import top.decided.emotion.cemuhook.Controller;
import top.decided.emotion.config.Config;
import top.decided.emotion.dialog.SettingDialog;
import top.decided.emotion.listener.ControllerButtonTouchListener;


public class BaseController {

    ImageButton setting, unlockScreen, left, down, right, up, x, y, a, b, r1, l1, menu, buttonView;
    Group lockScreen;
    final Controller controller;
    final SettingDialog settingDialog;
    final View rootView;
    final Context context;

    protected BaseController(Context context, ViewGroup container, Controller controller, SettingDialog settingDialog) {
        this.controller = controller;
        this.settingDialog = settingDialog;
        this.context = context;
        rootView = onCreateView(LayoutInflater.from(context), container);
        onViewCreated();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container){
        return null;
    }


    public void onViewCreated() {
        setting = rootView.findViewById(R.id.buttonSetting);
        lockScreen = rootView.findViewById(R.id.lockScreenGroup);
        unlockScreen = rootView.findViewById(R.id.unlockScreen);

        left = rootView.findViewById(R.id.buttonLeft);
        down = rootView.findViewById(R.id.buttonDown);
        right = rootView.findViewById(R.id.buttonRight);
        up = rootView.findViewById(R.id.buttonUp);
        r1 = rootView.findViewById(R.id.buttonRB);
        l1 = rootView.findViewById(R.id.buttonLB);
        x = rootView.findViewById(R.id.buttonX);
        y = rootView.findViewById(R.id.buttonY);
        a = rootView.findViewById(R.id.buttonA);
        b = rootView.findViewById(R.id.buttonB);
        buttonView = rootView.findViewById(R.id.buttonView);
        menu = rootView.findViewById(R.id.buttonMenu);

        init();
    }

    @SuppressLint("ClickableViewAccessibility")
    void init(){
        if (settingDialog != null){
            setting.setOnClickListener(view -> settingDialog.show());
            unlockScreen.setOnClickListener(view -> {
                MainActivity.pressedVibration(Config.isButtonVibration(), Config.getVibrator());
                lockScreen.setVisibility(View.INVISIBLE);
                settingDialog.resetLockScreen();
            });
        }else {
            setting.setVisibility(View.INVISIBLE);
        }
        left.setOnTouchListener(ControllerButtonTouchListener.getInstance());
        down.setOnTouchListener(ControllerButtonTouchListener.getInstance());
        right.setOnTouchListener(ControllerButtonTouchListener.getInstance());
        up.setOnTouchListener(ControllerButtonTouchListener.getInstance());
        x.setOnTouchListener(ControllerButtonTouchListener.getInstance());
        y.setOnTouchListener(ControllerButtonTouchListener.getInstance());
        a.setOnTouchListener(ControllerButtonTouchListener.getInstance());
        b.setOnTouchListener(ControllerButtonTouchListener.getInstance());
        r1.setOnTouchListener(ControllerButtonTouchListener.getInstance());
        l1.setOnTouchListener(ControllerButtonTouchListener.getInstance());
        buttonView.setOnTouchListener(ControllerButtonTouchListener.getInstance());
        menu.setOnTouchListener(ControllerButtonTouchListener.getInstance());
    }

    public void lockScreen(boolean lock){
        lockScreen.setVisibility(lock ? View.VISIBLE : View.INVISIBLE);
    }

    public View getRootView(){
        return rootView;
    }

    public static BaseController getControllerLayout(int layout, Context context, ViewGroup container, Controller controller, SettingDialog settingDialog){
        BaseController conFragment;
        switch (layout){
            case 0:
            case 1:
                conFragment = NSController.newInstance(context, container, controller, settingDialog, layout == 0);
                break;
            case 2:
                conFragment = FullController.newInstance(context, container, controller, settingDialog);
                break;
            default:
                conFragment = CustomController.newInstance(context, container, controller, settingDialog, layout);
                break;
        }
        return conFragment;
    }

}