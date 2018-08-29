package com.xiuxian.chen.other;

import android.content.*;
import android.util.*;
import android.widget.*;


/**
 * Created by wildly on 2018/4/5.
 */

public class perfromLongClickTextView extends TextView {
    public perfromLongClickTextView(Context cx)
    {
        super(cx);
    }

    public perfromLongClickTextView(Context cx, AttributeSet attr){
        super(cx, attr);
    }

    public perfromLongClickTextView(Context cx, AttributeSet attr, int def){
        super(cx, attr, def);
    }

    @Override
    public boolean performLongClick() {
        return false;
    }
}
