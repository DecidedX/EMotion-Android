package top.decided.emotion.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.widget.Toast;

public class PermissionApply {

    public static boolean applyOverlay(Context context){
        if (Settings.canDrawOverlays(context)){
            return true;
        }
        Intent request = new Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + context.getPackageName()));
        context.startActivity(request);
        Toast.makeText(context, "请允许显示在其他应用的上层", Toast.LENGTH_SHORT).show();
        return false;
    }

}
