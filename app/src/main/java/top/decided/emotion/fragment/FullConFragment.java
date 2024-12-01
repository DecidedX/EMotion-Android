package top.decided.emotion.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import top.decided.emotion.R;
import top.decided.emotion.cemuhook.Controller;
import top.decided.emotion.config.Config;
import top.decided.emotion.dialog.SettingDialog;
import top.decided.emotion.listener.ControllerButtonTouchListener;
import top.decided.emotion.listener.FullConRockerTouchListener;
import top.decided.emotion.listener.TouchPadTouchListener;
import top.decided.emotion.widget.Rocker;

public class FullConFragment extends BaseConFragment{
    ImageButton rt, lt;
    Rocker rockerLeft, rockerRight;
    View touchPad;

    public FullConFragment(Controller controller, SettingDialog settingDialog) {
        super(controller, settingDialog);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param controller Virtual controller.
     * @param settingDialog Setting dialog page.
     * @return A new instance of fragment FullConFragment.
     */
    public static FullConFragment newInstance(Controller controller, SettingDialog settingDialog) {
        return new FullConFragment(controller, settingDialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.full_con, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        touchPad = view.findViewById(R.id.touchpad);
        rt = view.findViewById(R.id.buttonRT);
        lt = view.findViewById(R.id.buttonLT);
        rockerLeft = view.findViewById(R.id.rockerLeft);
        rockerRight = view.findViewById(R.id.rockerRight);
        super.onViewCreated(view, savedInstanceState);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    void init() {
        super.init();
        FullConRockerTouchListener fcrTouchListener = new FullConRockerTouchListener();
        rt.setOnTouchListener(ControllerButtonTouchListener.getInstance());
        lt.setOnTouchListener(ControllerButtonTouchListener.getInstance());
        rockerRight.setOnTouchListener(fcrTouchListener);
        rockerLeft.setOnTouchListener(fcrTouchListener);
        touchPad.setOnTouchListener(new TouchPadTouchListener());
        if (Config.isAbxySwitch()){
            abxySwitch();
        }
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
