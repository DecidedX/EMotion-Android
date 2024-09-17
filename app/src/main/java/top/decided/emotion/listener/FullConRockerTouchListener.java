package top.decided.emotion.listener;

import android.view.MotionEvent;
import android.view.View;

import top.decided.emotion.cemuhook.Controller;
import top.decided.emotion.service.Service;
import top.decided.emotion.widget.Rocker;

public class FullConRockerTouchListener implements View.OnTouchListener {
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        view.onTouchEvent(motionEvent);
        Rocker rocker = (Rocker) view;
        byte x = rocker.getRockerX(false),
                y = rocker.getRockerY(true);
        boolean down;
        if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
            x = (byte) 0;
            y = (byte) 0;
            down = false;
        }else {
            down = rocker.isDown();
        }
        int tag = Integer.parseInt((String) rocker.getTag()),
                stickIDStart = tag == Controller.LEFT_STICK ? 2 : 4;
        Controller controller = Service.getController();
        controller.update(stickIDStart, x);
        controller.update(stickIDStart + 1, y);
        controller.setPressed(tag, down);
        return true;
    }
}
