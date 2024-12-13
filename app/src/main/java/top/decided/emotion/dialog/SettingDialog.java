package top.decided.emotion.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import top.decided.emotion.R;
import top.decided.emotion.config.Config;
import top.decided.emotion.listener.SwitchCheckedChangeListener;
import top.decided.emotion.service.ConnectionService;
import top.decided.emotion.utils.HandlerCaseType;
import top.decided.emotion.utils.PermissionApply;

public class SettingDialog extends Dialog {

    private Handler handler;
    private final Context context;
    private Spinner ipSelector;
    private Spinner layoutSelector;
    private SwitchCompat serviceSwitch, abxySwitch, lockScreenSwitch, touchpadSwitch,
            vibration, buttonVibration, layoutEditSwitch, useCutoutSwitch, floatingSwitch;
    private SharedPreferences sharedPreferences;
    private RadioGroup gyroDirectionGroup;
    private ConstraintLayout layoutEditContainer, abxySwitchContainer, useCutoutContainer;

    public SettingDialog(@NonNull Context context) {
        super(context, R.style.DialogStyle);
        this.context = context;
    }

    public SettingDialog(@NonNull Context context, Handler handler) {
        super(context,R.style.DialogStyle);
        this.context = context;
        this.handler = handler;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_setting);
        sharedPreferences = getContext().getSharedPreferences("config", Context.MODE_PRIVATE);

        serviceSwitch = findViewById(R.id.serviceSwitch);
        abxySwitch = findViewById(R.id.abxySwitch);
        lockScreenSwitch = findViewById(R.id.lock_screen_switch);
        touchpadSwitch = findViewById(R.id.touchpadSwitch);
        vibration = findViewById(R.id.vibration);
        buttonVibration = findViewById(R.id.buttonVibration);
        layoutEditSwitch = findViewById(R.id.layoutEditSwitch);
        ipSelector = findViewById(R.id.ipSelector);
        layoutSelector = findViewById(R.id.layoutSelector);
        gyroDirectionGroup = findViewById(R.id.gyroDirectionGroup);
        layoutEditContainer = findViewById(R.id.layoutEditContainer);
        abxySwitchContainer = findViewById(R.id.abxySwitchContainer);
        useCutoutSwitch = findViewById(R.id.useCutoutSwitch);
        useCutoutContainer = findViewById(R.id.useCutoutContainer);
        floatingSwitch = findViewById(R.id.floatingSwitch);

        serviceSwitch.setOnCheckedChangeListener((compoundButton, bool) -> {
            if (bool){
                try {
                    ConnectionService.getInstance().start();
                } catch (IOException e) {
                    Toast.makeText(context, "服务启动异常\n" + e.getMessage(), Toast.LENGTH_LONG).show();
                    System.exit(0);
                }
            }else ConnectionService.getInstance().close();
        });

        abxySwitch.setOnCheckedChangeListener(new SwitchCheckedChangeListener(HandlerCaseType.SWITCH_ABXY, null, handler, null,null));
        lockScreenSwitch.setOnCheckedChangeListener(new SwitchCheckedChangeListener(HandlerCaseType.LOCK_SCREEN, null, handler,null, null));
        useCutoutSwitch.setOnCheckedChangeListener(new SwitchCheckedChangeListener(HandlerCaseType.EXPAND_CUTOUT, null, handler, null,null));
        layoutEditSwitch.setOnCheckedChangeListener(new SwitchCheckedChangeListener(HandlerCaseType.EDIT_MODE, null, handler,null, bool -> layoutEditSwitch.setChecked(false)));

        touchpadSwitch.setOnCheckedChangeListener((compoundButton, bool) -> Config.setTouchpadSwitch(bool));
        vibration.setOnCheckedChangeListener((compoundButton, bool) -> Config.setVibration(bool));
        buttonVibration.setOnCheckedChangeListener((compoundButton, bool) -> Config.setButtonVibration(bool));

        gyroDirectionGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton direction = findViewById(checkedId);
            Config.setGyroDirection(Byte.parseByte((String) direction.getTag()));
        });

        floatingSwitch.setOnCheckedChangeListener(new SwitchCheckedChangeListener(HandlerCaseType.SET_FLOATING, null, handler, bool -> {
            if (Config.isFloating() == bool)
                return false;
            if (bool){
                boolean result = PermissionApply.applyOverlay(context);
                floatingSwitch.setChecked(result);
                return result;
            }
            return true;
        }, null));

        findViewById(R.id.donateButton).setOnClickListener(view -> new DonateDialog(context).show());

        initLayoutSelector();
    }

    @Override
    public void show() {
        super.show();
        initIpSelector();
        initLayoutSelector();
        serviceSwitch.setChecked(ConnectionService.getInstance().getServiceStatus());
        abxySwitch.setChecked(Config.isAbxySwitch());
        vibration.setChecked(Config.isVibration());
        buttonVibration.setChecked(Config.isButtonVibration());
        touchpadSwitch.setChecked(Config.isTouchpadSwitch());
        adjustCustomLayoutSetting(Config.getCurrentLayout());
        useCutoutSwitch.setChecked(Config.isUseCutout());
        useCutoutContainer.setVisibility(Config.isCutout() ? View.VISIBLE : View.GONE);
        floatingSwitch.setChecked(Config.isFloating());
    }

    public void resetLockScreen(){
        lockScreenSwitch.setChecked(false);
    }

    private void initIpSelector(){
        ArrayAdapter<String> stringArrayAdapter = getIpAdapter();
        ipSelector.setAdapter(stringArrayAdapter);
        if (ConnectionService.getInstance().getServiceStatus()){
            ipSelector.setSelection(stringArrayAdapter.getPosition(ConnectionService.getInstance().getSocket().getLocalAddress().getHostAddress()));
        }else {
            ipSelector.setSelection(0);
        }
        ipSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Config.setListenIP(stringArrayAdapter.getItem(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private @NonNull ArrayAdapter<String> getIpAdapter() {
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(context, R.layout.spinner_selecte);
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()){
                NetworkInterface networkInterface = interfaces.nextElement();
                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements()){
                    InetAddress address = addresses.nextElement();
                    if (address instanceof Inet4Address){
                        stringArrayAdapter.add(address.getHostAddress());
                    }
                }
            }
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        stringArrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        return stringArrayAdapter;
    }

    private void initLayoutSelector(){
        String[] fixedLayouts = {"左控制器布局", "右控制器布局", "完整布局", "新建自定义布局"};
        List<String> allLayouts = new ArrayList<>(Arrays.asList(fixedLayouts));
        allLayouts.addAll(Config.getCustomLayoutNames());
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(context, R.layout.spinner_selecte, allLayouts);
        stringArrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        layoutSelector.setAdapter(stringArrayAdapter);
        layoutSelector.setSelected(false);
        layoutSelector.setSelection(sharedPreferences.getInt("currentLayout", 0),true);
        layoutSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (handler != null && Config.getCurrentLayout() != position){
                    Message msg = Message.obtain();
                    msg.what = HandlerCaseType.SWITCH_LAYOUT;
                    msg.obj = position;
                    handler.sendMessage(msg);
                }
                adjustCustomLayoutSetting(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void adjustCustomLayoutSetting(int position){
        layoutEditContainer.setVisibility(position > 3 ? View.VISIBLE : View.GONE);
        abxySwitchContainer.setVisibility(position == 2 ? View.VISIBLE : View.GONE);
    }

}
