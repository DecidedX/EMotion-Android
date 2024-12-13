package top.decided.emotion.listener;

import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class DragEventListener implements View.OnTouchListener {

    private float x;
    private float y;
    private boolean moving = false;
    private final WindowManager.LayoutParams layoutParams;
    private final WindowManager windowManager;

    public DragEventListener(WindowManager windowManager, WindowManager.LayoutParams layoutParams){
        this.windowManager = windowManager;
        this.layoutParams = layoutParams;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                x = motionEvent.getRawX();
                y = motionEvent.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (!moving) moving = true;
                layoutParams.x = (int) (layoutParams.x + motionEvent.getRawX() - x);
                layoutParams.y = (int) (layoutParams.y + motionEvent.getRawY() - y);
                x = motionEvent.getRawX();
                y = motionEvent.getRawY();
                windowManager.updateViewLayout(view, layoutParams);
                break;
            case MotionEvent.ACTION_UP:
                if (moving) {
                    moveToEdge(view, motionEvent);
                    moving = false;
                }else {
                    view.performClick();
                }
                break;
        }
        return true;
    }

    private void moveToEdge(View view, MotionEvent motionEvent){
        Point size = new Point();
        windowManager.getDefaultDisplay().getSize(size);
        int xMiddle = size.x / 2;
        int yMiddle = size.y / 2;
        EdgeDistance x = new EdgeDistance(motionEvent.getRawX(), -xMiddle, false);
        EdgeDistance dx = new EdgeDistance(size.x - motionEvent.getRawX(), xMiddle, false);
        EdgeDistance y = new EdgeDistance(motionEvent.getRawY(), -yMiddle, true);
        EdgeDistance dy = new EdgeDistance(size.y - motionEvent.getRawY(), yMiddle, true);
        EdgeDistance min = EdgeDistance.min(x, dx, y, dy);
        if (min.vertical)
            layoutParams.y = min.edge;
        else layoutParams.x = min.edge;
        windowManager.updateViewLayout(view, layoutParams);
    }

    static class EdgeDistance {
        final float distance;
        final int edge;
        final boolean vertical;
        public EdgeDistance(float distance, int edge, boolean vertical){
            this.distance = distance;
            this.edge = edge;
            this.vertical = vertical;
        }

        public static EdgeDistance min(EdgeDistance... distances){
            EdgeDistance min = null;
            for (EdgeDistance edgeDistance : distances){
                if (min == null || min.distance > edgeDistance.distance){
                    min = edgeDistance;
                }
            }
            return min;
        }

    }
}
