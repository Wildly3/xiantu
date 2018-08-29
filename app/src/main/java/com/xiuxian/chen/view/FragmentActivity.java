package com.xiuxian.chen.view;

import android.os.Bundle;
import android.view.View;

/**
 * Created by Wildly on 2018/7/28.6:35
 */

public class FragmentActivity extends LayoutStack {
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.bundle = new Bundle();
    }

    public boolean startFragment(Fragment fragment){
        return this.startFragment(fragment, this.bundle);
    }

    public boolean startFragment(Fragment fragment, Bundle bundle)
    {
        if(isAnimation()) return false;

        fragment.attchBaseActivity(this);

        fragment.onCreate(bundle);

        return true;
    }

    public View FindView(int id){
        return getCurrentView().findViewById(id);
    }

}
