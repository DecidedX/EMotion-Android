package top.decided.emotion.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;

import java.util.ArrayList;
import java.util.List;

import top.decided.emotion.R;
import top.decided.emotion.cemuhook.Controller;
import top.decided.emotion.dialog.SettingDialog;
import top.decided.emotion.listener.SplitConRockerTouchListener;
import top.decided.emotion.widget.Rocker;

public class NSConFragment extends BaseConFragment{

    Rocker rocker;
    List<Group> conLayouts;

    private boolean lOrR;

    public NSConFragment(Controller controller, SettingDialog settingDialog, boolean lOrR) {
        super(controller, settingDialog);
        this.lOrR = lOrR;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param controller Virtual controller.
     * @param settingDialog Setting dialog page.
     * @param lOrR Show Left or Right controller layout.
     * @return A new instance of fragment NSConFragment.
     */
    public static NSConFragment newInstance(Controller controller, SettingDialog settingDialog, boolean lOrR) {
        return new NSConFragment(controller, settingDialog, lOrR);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        rocker = view.findViewById(R.id.rocker);
        conLayouts = new ArrayList<>();
        conLayouts.add(view.findViewById(R.id.leftConGroup));
        conLayouts.add(view.findViewById(R.id.rightConGroup));
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    void init() {
        super.init();
        rocker.setOnTouchListener(new SplitConRockerTouchListener());
        switchLRCon();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.ns_con, container, false);
    }

    private void switchLRCon(){
        conLayouts.get(0).setVisibility(lOrR ? View.INVISIBLE : View.VISIBLE);
        conLayouts.get(1).setVisibility(lOrR ? View.VISIBLE : View.INVISIBLE);
    }

    public void switchLRCon(boolean lOrR){
        this.lOrR = lOrR;
        switchLRCon();
    }

}
