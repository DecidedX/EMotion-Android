package top.decided.emotion.controller;

import android.content.Context;
import android.view.ViewGroup;

import top.decided.emotion.cemuhook.Controller;
import top.decided.emotion.config.Config;
import top.decided.emotion.dialog.SettingDialog;

public class ConManager {

    private final Context context;
    private final ViewGroup container;
    private final Controller controller;
    private final SettingDialog settingDialog;
    private BaseController controllerLayout;

    public ConManager(Context context, ViewGroup container, Controller controller, SettingDialog settingDialog){
        this.context = context;
        this.container = container;
        this.controller = controller;
        this.settingDialog = settingDialog;
        initLayout(Config.getCurrentLayout());
    }

    private void initLayout(int layout){
        controllerLayout = BaseController.getControllerLayout(layout, context, container, controller, settingDialog);
        switchConLayout();
    }

    public void switchConLayout(){
        container.removeAllViews();
        container.addView(controllerLayout.getRootView());
    }

    public void switchLayout(int nextLayout){
        int currentLayout = Config.getCurrentLayout();
        if (currentLayout < 2 && nextLayout < 2){
            ((NSController) controllerLayout).switchLRCon(nextLayout == 0);
        }else if (currentLayout >= 3 && nextLayout >= 3){
            if (nextLayout == 3){
                ((CustomController) controllerLayout).setEditMode(true);
            }
            ((CustomController) controllerLayout).setLayout(nextLayout);
        }else {
            initLayout(nextLayout);
        }
    }

    public void switchNSLayout(boolean bool){
        boolean abxySwitch = Config.isAbxySwitch();
        if (abxySwitch == bool || Config.getCurrentLayout() < 2)
            return;
        Config.setAbxySwitch(bool);
        ((FullController) controllerLayout).abxySwitch();
    }

    public BaseController getControllerLayout() {
        return controllerLayout;
    }
}
