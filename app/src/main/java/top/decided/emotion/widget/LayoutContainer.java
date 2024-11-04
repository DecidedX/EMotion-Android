package top.decided.emotion.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.constraintlayout.widget.ConstraintLayout;

import top.decided.emotion.R;
import top.decided.emotion.config.Config;
import top.decided.emotion.config.CustomLayoutData;

public class LayoutContainer extends ConstraintLayout {

    private float lastX, lastY;
    private View touchedView;
    private boolean editMode = false;
    private AppCompatSeekBar sizeSeekBar;

    public LayoutContainer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public LayoutContainer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public LayoutContainer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LayoutContainer(@NonNull Context context) {
        super(context);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (editMode && event.getAction() == MotionEvent.ACTION_DOWN
                || event.getAction() == MotionEvent.ACTION_UP){
            View view = findTouchedView(this, (int) event.getX(), (int) event.getY());
            if (view == null) return false;
            if (view.getTag() == null) return false;
            if (touchedView != null) touchedView.setSelected(false);
            touchedView = view;
        }
        return editMode;//根据编辑模式是否拦截触摸，转至onTouchEvent
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if (touchedView != null) {
                    touchedView.setSelected(true);
                    sizeSeekBar.setEnabled(true);
                    LayoutParams layoutParams = (LayoutParams) touchedView.getLayoutParams();
                    CustomLayoutData.ViewValues value = Config.getDefaultLayoutValue(Integer.parseInt((String) touchedView.getTag()));
                    sizeSeekBar.setProgress((int)(((layoutParams.matchConstraintPercentHeight - value.getDeltaSize()) / 0.002f) + 50));
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (touchedView != null){
                    if (touchedView.getTag() != null){
                        float left = touchedView.getX() + x - lastX;
                        float top = touchedView.getY() + y - lastY;
                        float h_bias = left < 0 ? 0 : left / (this.getWidth() - touchedView.getWidth());
                        float v_bias = top < 0 ? 0 : top / (this.getHeight() - touchedView.getHeight());
                        updateViewBias(touchedView, v_bias, h_bias);
                        //System.out.printf("HB:%.3f|VB:%.3f|X:%.3f|Y:%.3f|LX:%.3f|LY:%.3f", h_bias, v_bias,x, y, lastX, lastY);
                    }
                }
                break;
        }
        lastX = x;
        lastY = y;
        return true;
    }

    public static void updateViewBias(View view, float newVerticalBias, float newHorizontalBias){
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        layoutParams.verticalBias = newVerticalBias;
        layoutParams.horizontalBias = newHorizontalBias;
        view.setLayoutParams(layoutParams);
    }

    private View findTouchedView(ViewGroup parent, int x, int y){
        for (int i = parent.getChildCount()-1; i >= 0 ; i--) {
            View child = parent.getChildAt(i);
            if (child.getVisibility() == View.VISIBLE) {
                Rect rect = new Rect();
                child.getHitRect(rect);
                if (rect.contains(x, y)) {
                    // 如果是ViewGroup，递归继续查找
                    if (child instanceof ViewGroup) {
                        return findTouchedView((ViewGroup) child, x - rect.left, y - rect.top);
                    } else {
                        return child.getTag() == null ? null : child;
                    }
                }
            }
        }
        return null;
    }

    public boolean isEditMode(){
        return editMode;
    }

    public void setEditMode(boolean editMode){
        if (editMode) {
            openEditMode();
        } else {
            closeEditMode();
        }
    }

    public void openEditMode(){
        this.editMode = true;
        if (sizeSeekBar == null) sizeSeekBar = findViewById(R.id.sizeSeekBar);
    }

    public void closeEditMode(){
        clearSelect();
        editMode = false;
        sizeSeekBar.setEnabled(false);
        sizeSeekBar.setProgress(50);
    }

    public void clearSelect(){
        if (touchedView == null) return;
        touchedView.setSelected(false);
        touchedView = null;
    }

    public void resizeView(float deltaSize){
        LayoutParams layoutParams = (LayoutParams) touchedView.getLayoutParams();
        CustomLayoutData.ViewValues value = Config.getDefaultLayoutValue(Integer.parseInt((String) touchedView.getTag()));
        layoutParams.matchConstraintPercentHeight = deltaSize + value.getDeltaSize();
        touchedView.setLayoutParams(layoutParams);
    }

}
