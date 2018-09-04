package com.xiuxian.chen.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * Created by Wildly on 2018/8/30.1:13
 */

public class MyLayout extends FrameLayout {
    private static final String TAG = "MyLayout";

    private OnTouchListener listener;

    public MyLayout(@NonNull Context context) {
        super(context);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if (listener != null)
            listener.onTouch(this, ev);

        if (getChildCount() > 0){
            getChildAt(0).dispatchTouchEvent(ev);
        }
        return true;
    }

    public void setOnTouchEvent(OnTouchListener listener){
        this.listener = listener;
    }
}
