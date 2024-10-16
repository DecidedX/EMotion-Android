package top.decided.emotion.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import top.decided.emotion.MainActivity;
import top.decided.emotion.config.Config;

public class Rocker extends View {

    private final Paint paint;
    private int h, clickCount;
    private float centre, large_r, little_r, x, y;
    private boolean down;
    private final Handler handler;

    public Rocker(Context context) {
        this(context, null);
    }

    public Rocker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Rocker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
        handler = new Handler();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        init();
    }

    private void init(){
        h = getMeasuredHeight();
        centre = h / 2F;
        large_r = h / 3F;
        little_r = h / 6F;
        x = 3 * little_r;
        y = 3 * little_r;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        paint.setColor(Color.DKGRAY);
        canvas.drawCircle(centre,centre,large_r, paint);
        paint.setColor(Color.LTGRAY);
        canvas.drawCircle(x,y,little_r, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        float l = (float) Math.sqrt(x*x + y*y - h*(x+y) + h*h*0.5F);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if (l < large_r){
                    this.x = x;
                    this.y = y;
                }
                down = clickCount == 1;
                if (down) {
                    MainActivity.pressedVibration(Config.isButtonVibration(), Config.getVibrator());
                }
                clickCount++;
                handler.postDelayed(() -> clickCount = 0, 200);
                return true;
            case MotionEvent.ACTION_MOVE:
                if (l <= large_r){
                    this.x = x;
                    this.y = y;
                }else {
                    this.x = large_r * (x - centre) / l + centre;
                    this.y = large_r * (y - centre) / l + centre;
                }
                break;
            case MotionEvent.ACTION_UP:
                this.x = centre;
                this.y = centre;
                if (down) down = false;
                performClick();
                break;
        }
        invalidate();
        return super.onTouchEvent(event);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    public boolean isDown(){
        return down;
    }

    public byte getRockerX(boolean revers){
        float percent = (revers ? -1 : 1) * (this.x-centre) / large_r;
        return (byte) (percent > 0 ? Math.ceil(percent * 127):Math.floor(percent * 128));
    }

    public byte getRockerY(boolean revers){
        float percent = (revers ? -1 : 1) * (this.y-centre) / large_r;
        return (byte) (percent > 0 ? Math.ceil(percent * 127):Math.floor(percent * 128));
    }


}
