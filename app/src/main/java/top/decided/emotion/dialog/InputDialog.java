package top.decided.emotion.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import top.decided.emotion.R;
import top.decided.emotion.utils.HandlerCaseType;

public class InputDialog extends Dialog {

    private Handler handler;
    private final Context context;
    private EditText input;

    public InputDialog(@NonNull Context context) {
        super(context, R.style.DialogStyle);
        this.context = context;
    }

    public InputDialog(@NonNull Context context, Handler handler) {
        super(context,R.style.DialogStyle);
        this.context = context;
        this.handler = handler;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_input);
        setCanceledOnTouchOutside(false);
        input = findViewById(R.id.layoutNameInput);
        Button confirm = findViewById(R.id.confirm);
        Button cancel = findViewById(R.id.cancel);

        confirm.setOnClickListener((view -> {
            if (input.getText().toString().isEmpty()){
                Toast.makeText(context, "请输入自定义布局名称", Toast.LENGTH_SHORT).show();
                return;
            }
            Message msg = Message.obtain();
            msg.what = HandlerCaseType.SAVE_CUSTOM_LAYOUT;
            msg.obj = input.getText().toString();
            handler.sendMessage(msg);
            this.cancel();
        }));

        cancel.setOnClickListener((view -> {
            this.cancel();
        }));
    }
}
