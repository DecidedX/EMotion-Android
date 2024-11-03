package top.decided.emotion.listener;

import android.view.MotionEvent;
import android.view.View;

import top.decided.emotion.MainActivity;
import top.decided.emotion.config.Config;
import top.decided.emotion.service.Service;

public class ControllerButtonTouchListener implements View.OnTouchListener {

    private static ControllerButtonTouchListener instance;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            Service.getController().setPressed(Integer.parseInt((String) v.getTag()), true);
            MainActivity.pressedVibration(Config.isButtonVibration(), Config.getVibrator());
        }else if(event.getAction() == MotionEvent.ACTION_UP) {
            Service.getController().setPressed(Integer.parseInt((String) v.getTag()), false);
        }
        return false;
    }

    public static ControllerButtonTouchListener getInstance(){
        if (instance == null) instance = new ControllerButtonTouchListener();
        return instance;
    }

}
