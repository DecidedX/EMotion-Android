package top.decided.emotion;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.widget.ImageButton;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Group;

import java.util.ArrayList;
import java.util.List;

import top.decided.emotion.cemuhook.Controller;
import top.decided.emotion.config.Config;
import top.decided.emotion.listener.ControllerButtonTouchListener;
import top.decided.emotion.listener.FullConRockerTouchListener;
import top.decided.emotion.listener.SplitConRockerTouchListener;
import top.decided.emotion.listener.TouchPadTouchListener;
import top.decided.emotion.widget.Rocker;

public class GamePad {

    private ImageButton left, down, right, up, x, y, a, b, r1, l1, menu, view, rt, lt;
    private Rocker rocker, rockerLeft, rockerRight;
    private Controller controller;
    private List<Group> conLayouts;
    private View touchPad;

    public GamePad(Activity activity, int layout){
        left = activity.findViewById(R.id.buttonLeft);
        down = activity.findViewById(R.id.buttonDown);
        right = activity.findViewById(R.id.buttonRight);
        up = activity.findViewById(R.id.buttonUp);
        r1 = activity.findViewById(R.id.buttonRB);
        l1 = activity.findViewById(R.id.buttonLB);
        x = activity.findViewById(R.id.buttonX);
        y = activity.findViewById(R.id.buttonY);
        a = activity.findViewById(R.id.buttonA);
        b = activity.findViewById(R.id.buttonB);
        view = activity.findViewById(R.id.buttonView);
        menu = activity.findViewById(R.id.buttonMenu);
        touchPad = activity.findViewById(R.id.touchpad);

        if (layout < 2) {
            rocker = activity.findViewById(R.id.rocker);
            conLayouts = new ArrayList<>();
            conLayouts.add(activity.findViewById(R.id.leftConGroup));
            conLayouts.add(activity.findViewById(R.id.rightConGroup));
        }else {
            rt = activity.findViewById(R.id.buttonRT);
            lt = activity.findViewById(R.id.buttonLT);
            rockerLeft = activity.findViewById(R.id.rockerLeft);
            rockerRight = activity.findViewById(R.id.rockerRight);
        }
        init(layout);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init(int layout){
        ControllerButtonTouchListener cbTouchListener = new ControllerButtonTouchListener();
        if (layout < 2){
            rocker.setOnTouchListener(new SplitConRockerTouchListener());
            switchLRCon(layout);
        }else {
            FullConRockerTouchListener fcrTouchListener = new FullConRockerTouchListener();
            rt.setOnTouchListener(cbTouchListener);
            lt.setOnTouchListener(cbTouchListener);
            rockerRight.setOnTouchListener(fcrTouchListener);
            rockerLeft.setOnTouchListener(fcrTouchListener);
            if (Config.isAbxySwitch() && layout < 3) abxySwitch();
        }
        touchPad.setOnTouchListener(new TouchPadTouchListener());
        left.setOnTouchListener(cbTouchListener);
        down.setOnTouchListener(cbTouchListener);
        right.setOnTouchListener(cbTouchListener);
        up.setOnTouchListener(cbTouchListener);
        x.setOnTouchListener(cbTouchListener);
        y.setOnTouchListener(cbTouchListener);
        a.setOnTouchListener(cbTouchListener);
        b.setOnTouchListener(cbTouchListener);
        r1.setOnTouchListener(cbTouchListener);
        l1.setOnTouchListener(cbTouchListener);
        view.setOnTouchListener(cbTouchListener);
        menu.setOnTouchListener(cbTouchListener);
    }

    public void switchLRCon(int layout){
        conLayouts.get(0).setVisibility(layout == 1 ? View.INVISIBLE : View.VISIBLE);
        conLayouts.get(1).setVisibility(layout == 1 ? View.VISIBLE : View.INVISIBLE);
    }

    public ImageButton getLeft() {
        return left;
    }

    public void setLeft(ImageButton left) {
        this.left = left;
    }

    public ImageButton getDown() {
        return down;
    }

    public void setDown(ImageButton down) {
        this.down = down;
    }

    public ImageButton getRight() {
        return right;
    }

    public void setRight(ImageButton right) {
        this.right = right;
    }

    public ImageButton getUp() {
        return up;
    }

    public void setUp(ImageButton up) {
        this.up = up;
    }

    public ImageButton getX() {
        return x;
    }

    public void setX(ImageButton x) {
        this.x = x;
    }

    public ImageButton getY() {
        return y;
    }

    public void setY(ImageButton y) {
        this.y = y;
    }

    public ImageButton getA() {
        return a;
    }

    public void setA(ImageButton a) {
        this.a = a;
    }

    public ImageButton getB() {
        return b;
    }

    public void setB(ImageButton b) {
        this.b = b;
    }

    public ImageButton getR1() {
        return r1;
    }

    public void setR1(ImageButton r1) {
        this.r1 = r1;
    }

    public ImageButton getL1() {
        return l1;
    }

    public void setL1(ImageButton l1) {
        this.l1 = l1;
    }

    public ImageButton getMenu() {
        return menu;
    }

    public void setMenu(ImageButton menu) {
        this.menu = menu;
    }

    public ImageButton getView() {
        return view;
    }

    public void setView(ImageButton view) {
        this.view = view;
    }

    public ImageButton getRt() {
        return rt;
    }

    public void setRt(ImageButton rt) {
        this.rt = rt;
    }

    public ImageButton getLt() {
        return lt;
    }

    public void setLt(ImageButton lt) {
        this.lt = lt;
    }

    public Rocker getRocker() {
        return rocker;
    }

    public void setRocker(Rocker rocker) {
        this.rocker = rocker;
    }

    public Rocker getRockerLeft() {
        return rockerLeft;
    }

    public void setRockerLeft(Rocker rockerLeft) {
        this.rockerLeft = rockerLeft;
    }

    public Rocker getRockerRight() {
        return rockerRight;
    }

    public void setRockerRight(Rocker rockerRight) {
        this.rockerRight = rockerRight;
    }

    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public View getTouchPad() {
        return touchPad;
    }

    public void setTouchPad(View touchPad) {
        this.touchPad = touchPad;
    }

    public void abxySwitch(){
        boolean abxySwitch = Config.isAbxySwitch();
        ConstraintLayout.LayoutParams
                aLayoutParams = (ConstraintLayout.LayoutParams) a.getLayoutParams(),
                bLayoutParams = (ConstraintLayout.LayoutParams) b.getLayoutParams(),
                xLayoutParams = (ConstraintLayout.LayoutParams) x.getLayoutParams(),
                yLayoutParams = (ConstraintLayout.LayoutParams) y.getLayoutParams(),
                r1LayoutParams = (ConstraintLayout.LayoutParams) r1.getLayoutParams(),
                rtLayoutParams = (ConstraintLayout.LayoutParams) rt.getLayoutParams(),
                rrLayoutParams = (ConstraintLayout.LayoutParams) rockerRight.getLayoutParams();

        if (abxySwitch)
            aLayoutParams.endToStart = R.id.buttonA;
        else
            bLayoutParams.endToStart = R.id.buttonB;

        yLayoutParams.endToStart = abxySwitch ? R.id.buttonA : R.id.buttonY;
        xLayoutParams.endToStart = abxySwitch ? R.id.buttonX : R.id.buttonB;
        r1LayoutParams.endToStart = abxySwitch ? R.id.buttonX : R.id.buttonY;
        r1LayoutParams.topToTop = abxySwitch ? R.id.buttonX : R.id.buttonY;
        rtLayoutParams.endToStart = abxySwitch ? R.id.buttonX : R.id.buttonY;
        rrLayoutParams.endToStart = abxySwitch ? R.id.buttonB : R.id.buttonA;

        a.setLayoutParams(bLayoutParams);
        b.setLayoutParams(aLayoutParams);
        x.setLayoutParams(yLayoutParams);
        y.setLayoutParams(xLayoutParams);
        r1.setLayoutParams(r1LayoutParams);
        rt.setLayoutParams(rtLayoutParams);
        rockerRight.setLayoutParams(rrLayoutParams);

        Object tagA = a.getTag(), tagB = b.getTag(), tagX = x.getTag(), tagY = y.getTag();
        a.setTag(tagB);
        b.setTag(tagA);
        x.setTag(tagY);
        y.setTag(tagX);

    }

}
