package top.decided.emotion.listener;

import android.view.MotionEvent;
import android.view.View;

import top.decided.emotion.cemuhook.Controller;
import top.decided.emotion.config.Config;
import top.decided.emotion.service.Service;

public class TouchPadTouchListener implements View.OnTouchListener {
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (Config.isTouchpadSwitch()){
            Controller controller = Service.getController();
            switch (motionEvent.getAction()){
                case MotionEvent.ACTION_DOWN:
                    controller.updateTouch(true,true);
                    break;
                case MotionEvent.ACTION_MOVE:
                    controller.updateTouch(true, (short) (motionEvent.getX()/2310 * 1920), (short) (motionEvent.getY()/1080 * 943));
                    break;
                case MotionEvent.ACTION_UP:
                    controller.updateTouch(true,false);
                    break;
            }
        }
        return false;
    }
}
