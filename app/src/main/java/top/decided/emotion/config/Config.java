package top.decided.emotion.config;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Vibrator;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import top.decided.emotion.R;

public class Config {

    public static final int SERVICE_PORT = 26760;
    public static final int QUICK_CONN_PORT = 26761;
    private static String listenIP;
    private static boolean abxySwitch, touchpadSwitch, vibration, buttonVibration;
    private static int currentLayout;
    private static byte gyroDirection = 0;
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private static Vibrator vibrator;
    private static boolean cutout, useCutout;
    private static List<CustomLayoutData> customLayoutDataList;
    private static CustomLayoutData defaultLayoutData;

    public static void init(Application application) {
        defaultLayoutData = new Gson().fromJson(application.getResources().getString(R.string.default_layout_data), CustomLayoutData.class);
        Config.sharedPreferences = application.getSharedPreferences("config", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        currentLayout = sharedPreferences.getInt("currentLayout", 0);
        abxySwitch = sharedPreferences.getBoolean("abxySwitch", true);
        touchpadSwitch = sharedPreferences.getBoolean("touchpadSwitch", false);
        vibration = sharedPreferences.getBoolean("vibration", true);
        buttonVibration = sharedPreferences.getBoolean("buttonVibration", true);
        cutout = sharedPreferences.getBoolean("cutout", false);
        useCutout = sharedPreferences.getBoolean("useCutout", false);
        customLayoutDataList = new Gson().fromJson(sharedPreferences.getString("customLayoutData", "[]"),
                new TypeToken<List<CustomLayoutData>>(){}.getType());
    }

    public static String getListenIP() {
        return listenIP;
    }

    public static void setListenIP(String listenIP) {
        Config.listenIP = listenIP;
    }

    public static boolean isAbxySwitch() {
        return abxySwitch;
    }

    public static void setAbxySwitch(boolean abxySwitch) {
        Config.abxySwitch = abxySwitch;
        editor.putBoolean("abxySwitch", abxySwitch);
        editor.apply();
    }

    public static boolean isTouchpadSwitch() {
        return touchpadSwitch;
    }

    public static void setTouchpadSwitch(boolean touchpadSwitch) {
        Config.touchpadSwitch = touchpadSwitch;
        editor.putBoolean("touchpadSwitch", touchpadSwitch);
        editor.apply();
    }

    public static boolean isVibration() {
        return vibration;
    }

    public static void setVibration(boolean vibration) {
        Config.vibration = vibration;
        editor.putBoolean("vibration", vibration);
        editor.apply();
    }

    public static boolean isButtonVibration() {
        return buttonVibration;
    }

    public static void setButtonVibration(boolean buttonVibration) {
        Config.buttonVibration = buttonVibration;
        editor.putBoolean("buttonVibration", buttonVibration);
        editor.apply();
    }

    public static int getCurrentLayout() {
        return currentLayout;
    }

    public static void setCurrentLayout(int currentLayout) {
        Config.currentLayout = currentLayout;
        editor.putInt("currentLayout", currentLayout);
        editor.apply();
    }

    public static byte getGyroDirection() {
        return gyroDirection;
    }

    public static void setGyroDirection(byte gyroDirection) {
        Config.gyroDirection = gyroDirection;
    }

    public static Vibrator getVibrator() {
        return vibrator;
    }

    public static void setVibrator(Vibrator vibrator) {
        Config.vibrator = vibrator;
    }

    public static boolean isCutout(){
        return cutout;
    }

    public static void setCutout(boolean cutout) {
        Config.cutout = cutout;
        editor.putBoolean("cutout", cutout);
        editor.apply();
    }

    public static boolean isUseCutout(){
        return useCutout;
    }

    public static void setUseCutout(boolean useCutout){
        Config.useCutout = useCutout;
        editor.putBoolean("useCutout", useCutout);
        editor.apply();
    }

    public static List<CustomLayoutData> getCustomLayoutDataList(){
        return customLayoutDataList;
    }

    public static CustomLayoutData getCustomLayoutData(String layoutName){
        CustomLayoutData data = null;
        for (CustomLayoutData layoutData : customLayoutDataList){
            if (layoutData.getLayoutName().equals(layoutName)) data = layoutData;
        }
        return data;
    }

    public static CustomLayoutData getCustomLayoutData(int layout){
        return customLayoutDataList.get(layout - 4);
    }

    public static int addCustomLayoutData(CustomLayoutData customLayoutData){
        customLayoutDataList.add(customLayoutData);
        saveCustomLayoutData();
        return customLayoutDataList.indexOf(customLayoutData) + 4;
    }

    public static void delCustomLayoutData(String layoutName){
        customLayoutDataList.removeIf(data -> data.getLayoutName().equals(layoutName));
        saveCustomLayoutData();
    }

    public static void modifyCustomLayoutData(CustomLayoutData customLayoutData, int layout){
        customLayoutData.setLayoutName(customLayoutDataList.get(layout - 4).getLayoutName());
        customLayoutDataList.set(layout - 4, customLayoutData);
        saveCustomLayoutData();
    }

    public static List<String> getCustomLayoutNames(){
        List<String> names = new ArrayList<>();
        for (CustomLayoutData data : customLayoutDataList){
            names.add(data.getLayoutName());
        }
        return names;
    }

    public static int getCustomLayoutCount(){
        return customLayoutDataList.size();
    }

    private static void saveCustomLayoutData(){
        editor.putString("customLayoutData", new Gson().toJson(customLayoutDataList));
        editor.apply();
    }

    public static CustomLayoutData getDefaultLayoutData() {
        return defaultLayoutData;
    }

    public static CustomLayoutData.ViewValues getDefaultLayoutValue(int buttonTag) {
        return defaultLayoutData.getViewValue(buttonTag);
    }

    public static SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

}
