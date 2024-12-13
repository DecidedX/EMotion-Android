package top.decided.emotion.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.constraintlayout.widget.Group;

import java.util.ArrayList;
import java.util.List;

import top.decided.emotion.R;
import top.decided.emotion.cemuhook.Controller;
import top.decided.emotion.dialog.SettingDialog;
import top.decided.emotion.listener.SplitConRockerTouchListener;
import top.decided.emotion.widget.Rocker;

public class NSController extends BaseController {

    Rocker rocker;
    List<Group> conLayouts;

    private boolean lOrR;

    public NSController(Context context, ViewGroup container, Controller controller, SettingDialog settingDialog, boolean lOrR) {
        super(context,container, controller, settingDialog);
        switchLRCon(lOrR);
    }

    public static NSController newInstance(Context context, ViewGroup container, Controller controller, SettingDialog settingDialog, boolean lOrR) {
        return new NSController(context, container, controller, settingDialog, lOrR);
    }

    public void onViewCreated() {
        rocker = rootView.findViewById(R.id.rocker);
        conLayouts = new ArrayList<>();
        conLayouts.add(rootView.findViewById(R.id.leftConGroup));
        conLayouts.add(rootView.findViewById(R.id.rightConGroup));
        super.onViewCreated();
    }

    @Override
    void init() {
        super.init();
        rocker.setOnTouchListener(new SplitConRockerTouchListener());
        switchLRCon();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.ns_con, container, false);
    }

    private void switchLRCon(){
        conLayouts.get(0).setVisibility(lOrR ? View.VISIBLE : View.INVISIBLE);
        conLayouts.get(1).setVisibility(lOrR ? View.INVISIBLE : View.VISIBLE);
    }

    public void switchLRCon(boolean lOrR){
        this.lOrR = lOrR;
        switchLRCon();
    }

}
