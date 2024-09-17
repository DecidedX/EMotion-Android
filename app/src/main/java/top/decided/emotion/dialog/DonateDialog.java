package top.decided.emotion.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;

import top.decided.emotion.R;

public class DonateDialog extends Dialog {

    public DonateDialog(@NonNull Context context) {
        super(context, R.style.DialogStyle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.dialog_donate);
    }

}
