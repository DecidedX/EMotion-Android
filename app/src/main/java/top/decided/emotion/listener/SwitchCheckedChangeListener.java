package top.decided.emotion.listener;

import android.os.Handler;
import android.os.Message;
import android.widget.CompoundButton;

public class SwitchCheckedChangeListener implements CompoundButton.OnCheckedChangeListener{

    private final int msgWhat;
    private final Object msgObj;
    private final Handler handler;
    private final DoSomethingElse doSomethingElse;

    public SwitchCheckedChangeListener(int msgWhat, Object msgObj, Handler handler, DoSomethingElse doSomethingElse){
        this.msgWhat = msgWhat;
        this.msgObj = msgObj;
        this.handler = handler;
        this.doSomethingElse = doSomethingElse;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (handler != null){
            Message msg = Message.obtain();
            msg.what = msgWhat;
            msg.obj = msgObj == null ? b : msgObj;
            handler.sendMessage(msg);
        }
        if (doSomethingElse != null){
            doSomethingElse.doSomething(b);
        }
    }

    public interface DoSomethingElse{

        void doSomething(boolean bool);

    }

}