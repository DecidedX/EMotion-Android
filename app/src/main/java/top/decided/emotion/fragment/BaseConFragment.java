package top.decided.emotion.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.constraintlayout.widget.Group;

import android.view.View;
import android.widget.ImageButton;

import top.decided.emotion.MainActivity;
import top.decided.emotion.R;
import top.decided.emotion.cemuhook.Controller;
import top.decided.emotion.config.Config;
import top.decided.emotion.dialog.SettingDialog;
import top.decided.emotion.listener.ControllerButtonTouchListener;


public class BaseConFragment extends Fragment {

    ImageButton setting, unlockScreen, left, down, right, up, x, y, a, b, r1, l1, menu, buttonView;
    Group lockScreen;
    final Controller controller;
    final SettingDialog settingDialog;

    protected BaseConFragment(Controller controller, SettingDialog settingDialog) {
        this.controller = controller;
        this.settingDialog = settingDialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setting = view.findViewById(R.id.buttonSetting);
        lockScreen = view.findViewById(R.id.lockScreenGroup);
        unlockScreen = view.findViewById(R.id.unlockScreen);

        left = view.findViewById(R.id.buttonLeft);
        down = view.findViewById(R.id.buttonDown);
        right = view.findViewById(R.id.buttonRight);
        up = view.findViewById(R.id.buttonUp);
        r1 = view.findViewById(R.id.buttonRB);
        l1 = view.findViewById(R.id.buttonLB);
        x = view.findViewById(R.id.buttonX);
        y = view.findViewById(R.id.buttonY);
        a = view.findViewById(R.id.buttonA);
        b = view.findViewById(R.id.buttonB);
        buttonView = view.findViewById(R.id.buttonView);
        menu = view.findViewById(R.id.buttonMenu);

        init();
    }


    @SuppressLint("ClickableViewAccessibility")
    void init(){
        setting.setOnClickListener(view -> settingDialog.show());
        unlockScreen.setOnClickListener(view -> {
            MainActivity.pressedVibration(Config.isButtonVibration(), Config.getVibrator());
            lockScreen.setVisibility(View.INVISIBLE);
            settingDialog.resetLockScreen();
        });

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

}