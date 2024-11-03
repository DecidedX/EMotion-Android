package top.decided.emotion.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Consumer;

import top.decided.emotion.MainActivity;
import top.decided.emotion.R;
import top.decided.emotion.cemuhook.Controller;
import top.decided.emotion.config.Config;
import top.decided.emotion.config.CustomLayoutData;
import top.decided.emotion.dialog.InputDialog;
import top.decided.emotion.dialog.SettingDialog;
import top.decided.emotion.widget.LayoutContainer;

//TODO:联合布局编辑重构
public class CustomConFragment extends FullConFragment{

    AppCompatSeekBar sizeSeekBar;
    ImageButton editSave, editReset, editQuit;
    ConstraintLayout editToolsContainer;

    private LayoutContainer layoutContainer;

    public CustomConFragment(Controller controller, SettingDialog settingDialog) {
        super(controller, settingDialog);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param controller Virtual controller.
     * @param settingDialog Setting dialog page.
     * @return A new instance of fragment CustomConFragment.
     */
    public static CustomConFragment newInstance(Controller controller, SettingDialog settingDialog) {
        return new CustomConFragment(controller, settingDialog);
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

        editReset.setOnClickListener(view -> {
            setCustomLayout(Config.getDefaultLayoutData());
        });

        editQuit.setOnClickListener(view -> {
            setEditMode(false);
            if (Config.getCurrentLayout() == 3) Config.setCurrentLayout(2);
//            initLayout(Config.getCurrentLayout());
        });

        sizeSeekBar.setOnTouchListener((view, motionEvent) -> {
            layoutContainer.resizeView((((AppCompatSeekBar) view).getProgress() - 50) * 0.002f);
            return false;
        });
    }

    public void setEditMode(boolean isOpen){
        settingDialog.hide();
        layoutContainer.setEditMode(isOpen);
        editToolsContainer.setVisibility(isOpen ? View.VISIBLE : View.GONE);
        setting.setVisibility(isOpen ? View.GONE : View.VISIBLE);
    }

    public void saveNewCustomLayout(String name){
        setEditMode(false);
        int layout = Config.addCustomLayoutData(getNowLayoutData(name));
        Config.setCurrentLayout(layout);
//        initLayout(layout);
    }

    private void readCustomLayout(int layout){
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
        ViewGroup viewWrapper = (ViewGroup) layoutContainer.getChildAt(0);
        for (int i = 0; i < viewWrapper.getChildCount(); i++){
            View v = viewWrapper.getChildAt(i);
            if (v.getTag() != null){
                process.accept(v);
            }
        }
    }

}
