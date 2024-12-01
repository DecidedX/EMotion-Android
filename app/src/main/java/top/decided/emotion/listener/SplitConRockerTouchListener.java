package top.decided.emotion.listener;

import android.view.MotionEvent;
import android.view.View;

import top.decided.emotion.cemuhook.Controller;
import top.decided.emotion.config.Config;
import top.decided.emotion.service.ConnectionService;
import top.decided.emotion.widget.Rocker;

public class SplitConRockerTouchListener implements View.OnTouchListener {
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        view.onTouchEvent(motionEvent);
        Rocker rocker = (Rocker) view;
        byte x, y;
        boolean down;
        switch (Config.getCurrentLayout()){
            case 0:
                x = rocker.getRockerY(true);
                y = rocker.getRockerX(true);
                break;
            case 1:
                x = rocker.getRockerY(false);
                y = rocker.getRockerX(false);
                break;
            default:
                x = rocker.getRockerX(false);
                y = rocker.getRockerY(true);
                break;
        }
        if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
            x = (byte) 0;
            y = (byte) 0;
            down = false;
        }else {
            down = rocker.isDown();
        }
        Controller controller = ConnectionService.getInstance().getController();
        if (Config.getCurrentLayout() == 0){
            controller.update(Controller.LEFT_STICK_X, x);
            controller.update(Controller.LEFT_STICK_Y, y);
            controller.setPressed(Controller.LEFT_STICK, down);
        }else if (Config.getCurrentLayout() == 1){
            controller.update(Controller.RIGHT_STICK_X, x);
            controller.update(Controller.RIGHT_STICK_Y, y);
            controller.setPressed(Controller.RIGHT_STICK, down);
        }
        return true;
    }
}
