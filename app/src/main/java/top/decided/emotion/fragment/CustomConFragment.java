package top.decided.emotion.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import java.util.function.Consumer;

import top.decided.emotion.MainActivity;
import top.decided.emotion.R;
import top.decided.emotion.cemuhook.Controller;
import top.decided.emotion.config.Config;
import top.decided.emotion.config.CustomLayoutData;
import top.decided.emotion.dialog.InputDialog;
import top.decided.emotion.dialog.SettingDialog;
import top.decided.emotion.utils.HandlerCaseType;
import top.decided.emotion.widget.LayoutContainer;

public class CustomConFragment extends FullConFragment{

    AppCompatSeekBar sizeSeekBar;
    ImageButton editSave, editReset, editQuit;
    ConstraintLayout editToolsContainer;

    private LayoutContainer layoutContainer;

    private int layout;

    public CustomConFragment(Controller controller, SettingDialog settingDialog, int layout) {
        super(controller, settingDialog);
        this.layout = layout;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param controller Virtual controller.
     * @param settingDialog Setting dialog page.
     * @return A new instance of fragment CustomConFragment.
     */
    public static CustomConFragment newInstance(Controller controller, SettingDialog settingDialog, int layout) {
        return new CustomConFragment(controller, settingDialog, layout);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.custom_con, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        layoutContainer = view.findViewById(R.id.layoutContainer);
        editToolsContainer = view.findViewById(R.id.editToolsContainer);
        sizeSeekBar = view.findViewById(R.id.sizeSeekBar);
        editSave = view.findViewById(R.id.editSaveButton);
        editReset = view.findViewById(R.id.editResetButton);
        editQuit = view.findViewById(R.id.editQuitButton);
        super.onViewCreated(view, savedInstanceState);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    void init() {
        super.init();
        sizeSeekBar.setEnabled(false);

        editSave.setOnClickListener(view -> {
            layoutContainer.clearSelect();
            int currentLayout = Config.getCurrentLayout();
            if(currentLayout == 3){
                new InputDialog(requireActivity(), MainActivity.getHandler()).show();
            }else {
                setEditMode(false);
                Config.modifyCustomLayoutData(getNowLayoutData(""),currentLayout);
            }
        });

        editReset.setOnClickListener(view -> setCustomLayout(Config.getDefaultLayoutData()));

        editQuit.setOnClickListener(view -> {
            setEditMode(false);
            Message msg = Message.obtain();
            msg.what = HandlerCaseType.SWITCH_LAYOUT;
            msg.obj = Config.getCurrentLayout() == 3 ? 2:Config.getCurrentLayout();
            MainActivity.getHandler().sendMessage(msg);
        });

        sizeSeekBar.setOnTouchListener((view, motionEvent) -> {
            layoutContainer.resizeView((((AppCompatSeekBar) view).getProgress() - 50) * 0.002f);
            return false;
        });

        readCustomLayout(layout);
    }

    public void setEditMode(boolean isOpen){
        settingDialog.hide();
        layoutContainer.setEditMode(isOpen);
        editToolsContainer.setVisibility(isOpen ? View.VISIBLE : View.GONE);
        setting.setVisibility(isOpen ? View.GONE : View.VISIBLE);
    }

    public void setLayout(int layout){
        this.layout = layout;
        readCustomLayout(layout);
    }

    public void saveNewCustomLayout(String name){
        setEditMode(false);
        int layout = Config.addCustomLayoutData(getNowLayoutData(name));
        Config.setCurrentLayout(layout);
        setLayout(layout);
    }

    private void readCustomLayout(int layout){
        if(layout == 3) {
            setEditMode(true);
            return;
        }
        setCustomLayout(Config.getCustomLayoutData(layout));
    }

    private void setCustomLayout(CustomLayoutData layoutData){
        foreachConLayout((v -> {
            CustomLayoutData.ViewValues value = layoutData.getViewValue(Integer.parseInt((String) v.getTag()));
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) v.getLayoutParams();
            layoutParams.verticalBias = value.getVBias();
            layoutParams.horizontalBias = value.getHBias();
            layoutParams.matchConstraintPercentHeight = value.getDeltaSize();
            v.setLayoutParams(layoutParams);
        }));
    }

    private CustomLayoutData getNowLayoutData(String layoutName){
        CustomLayoutData customLayout = new CustomLayoutData(layoutName , new ArrayList<>());
        foreachConLayout((v ->{
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) v.getLayoutParams();
            customLayout.addViewValue(
                    Integer.parseInt((String) v.getTag()),
                    layoutParams.verticalBias,
                    layoutParams.horizontalBias,
                    layoutParams.matchConstraintPercentHeight);
        }));
        return customLayout;
    }

    private void foreachConLayout(Consumer<View> process){
        for (int i = 0; i < layoutContainer.getChildCount(); i++){
            View v = layoutContainer.getChildAt(i);
            if (v.getTag() != null){
                process.accept(v);
            }
        }
    }

    @Override
    public void abxySwitch() {
    }
}
