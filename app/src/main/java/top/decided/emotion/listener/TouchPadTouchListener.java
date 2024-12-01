package top.decided.emotion.listener;

import android.view.MotionEvent;
import android.view.View;

import top.decided.emotion.cemuhook.Controller;
import top.decided.emotion.config.Config;
import top.decided.emotion.service.ConnectionService;

public class TouchPadTouchListener implements View.OnTouchListener {
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        System.out.println(12341);
        System.out.printf("x:%.2f | y:%.2f | cx:%.2f | cy:%.2f", motionEvent.getX(), motionEvent.getY(), motionEvent.getX()/ view.getWidth() * 1920, motionEvent.getY()/ view.getHeight() * 943);
        if (Config.isTouchpadSwitch()){
            Controller controller = ConnectionService.getInstance().getController();
            switch (motionEvent.getAction()){
                case MotionEvent.ACTION_DOWN:
                    controller.updateTouch(true,true);
                    break;
                case MotionEvent.ACTION_MOVE:
                    controller.updateTouch(true, (short) (motionEvent.getX()/ view.getWidth() * 1920), (short) (motionEvent.getY()/ view.getHeight() * 943));
                    break;
                case MotionEvent.ACTION_UP:
                    controller.updateTouch(true,false);
                    break;
            }
        }
        return false;
    }
}
