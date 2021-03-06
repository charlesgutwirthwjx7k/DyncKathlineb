package org.dync.softkeyboarddemo;


import android.app.DialogFragment;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class DefaultDialogFragment extends DialogFragment {


    private View view;
    private LinearLayout llEdit;
    private TextView close;
    private EditText editText;
    private Button send;

    public DefaultDialogFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);//必须放在setContextView之前调用, 去掉Dialog中的蓝线
        view = inflater.inflate(R.layout.fragment_default_dialog, container);
        setLayout();
        initData();
        return view;
    }

    private void initData() {
//        final Window window = getDialog().getWindow();
//        window.getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
//            @Override
//            public void onSystemUiVisibilityChange(int visibility) {
//                setHideVirtualKey(window);
//            }
//        });
        llEdit = (LinearLayout) view.findViewById(R.id.ll_input_layout);
        close = (TextView) view.findViewById(R.id.close);
        editText = (EditText) view.findViewById(R.id.edit_send_message);
        send = (Button) view.findViewById(R.id.btn_send);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
                           public void run() {
                               SoftKeyboardUtil.showKeyboard(editText.getContext(), editText);
                           }
                       },
                150);//这里键盘没有自动弹起可以把时间值设大一点
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        SoftKeyboardUtil softKeyboardUtil = new SoftKeyboardUtil();
        softKeyboardUtil.observeSoftKeyboard(getActivity(), new SoftKeyboardUtil.OnSoftKeyboardChangeListener() {
            @Override
            public void onSoftKeyBoardChange(int softKeybardHeight, boolean isShow) {
                if (isShow){
                    dismiss();
                }
            }
        });
    }

    public void setHideVirtualKey(Window window) {
        int systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//              | View.SYSTEM_UI_FLAG_LOW_PROFILE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_FULLSCREEN;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            systemUiVisibility |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }
        window.getDecorView().setSystemUiVisibility(systemUiVisibility);
    }

    private void setLayout() {
        Window window = getDialog().getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏，即没有系统状态栏
//        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//透明状态栏
        window.getDecorView().setPadding(0, 0, 0, 0);
        window.setBackgroundDrawable(new ColorDrawable(0));//背景透明
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.dimAmount = 0;
        window.setAttributes(lp);
    }
}
