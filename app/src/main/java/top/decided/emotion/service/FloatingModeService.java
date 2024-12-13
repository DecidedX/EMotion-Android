package top.decided.emotion.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import top.decided.emotion.R;
import top.decided.emotion.controller.ConManager;
import top.decided.emotion.listener.DragEventListener;

public class FloatingModeService extends Service {
    private final FloatingServiceBinder binder = new FloatingServiceBinder();
    private WindowManager windowManager;
    private View floating, conContainer;
    private ImageView floatingButton;
    private ConManager conManager;
    private boolean showCon;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        showCon = false;
        super.onCreate();
    }

    public void floating(Context context){
        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        floating = LayoutInflater.from(context).inflate(R.layout.floating_button, null);
        floatingButton = floating.findViewById(R.id.float_button);
        conContainer = LayoutInflater.from(context).inflate(R.layout.activity_con, null);
        conManager = new ConManager(
                context,
                conContainer.findViewById(R.id.layoutControllerViewer),
                ConnectionService.getInstance().getController(),
                null);
        conContainer.setVisibility(View.GONE);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        layoutParams.x = 0;
        layoutParams.y = 0;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR |
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        layoutParams.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        layoutParams.width = 120;
        layoutParams.height = 120;
        layoutParams.format = PixelFormat.TRANSPARENT;
        floating.setOnTouchListener(new DragEventListener(windowManager, layoutParams));
        floating.setOnClickListener(view -> {
            floatingButton.setImageResource(showCon ? R.drawable.show : R.drawable.hide);
            conContainer.setVisibility(showCon ? View.GONE : View.VISIBLE);
            showCon = !showCon;
        });
        conContainer.setAlpha(0.5F);
        WindowManager.LayoutParams containerLayoutParams = new WindowManager.LayoutParams();
        containerLayoutParams.copyFrom(layoutParams);
        containerLayoutParams.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        containerLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        containerLayoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        windowManager.addView(conContainer, containerLayoutParams);
        windowManager.addView(floating, layoutParams);
    }

    public void switchLayout(int nextLayout){
        conManager.switchLayout(nextLayout);
    }

    @Override
    public void onDestroy() {
        windowManager.removeView(conContainer);
        windowManager.removeView(floating);
        super.onDestroy();
    }

    public class FloatingServiceBinder extends Binder {
        public FloatingModeService getService(){
            return FloatingModeService.this;
        }
    }

}
