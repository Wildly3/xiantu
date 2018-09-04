package com.xiuxian.chen.view;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by Wildly on 2018/8/29.20:50
 */

public abstract class ViewListener
{
    private Bundle bundle;

    //初次加载时调用
    public abstract void onLoad(LayoutStack app);

    public void onStop(){}

    public void onStart(){}

    //当前页面被另一个页面覆盖时
    public void onPause(){}

    //恢复
    public void onResume(){}

    //销毁
    public void onDestroy(){}

    //当前一个页面关闭时，回调数据
    public void Callback(Bundle bundle){}

    public boolean onCreateOptionsMenu(Menu menu){
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        return true;
    }

    public boolean onKeyDown(int KeyCode, KeyEvent event){
        return false;
    }

    public boolean onKeyUp(int KeyCode, KeyEvent event){
        return false;
    }

    /**当按下返回键时调用
     * @return 返回true时则不继续下面的事件
     */
    public boolean onBack(){
        return false;
    }

    public final void setCallback(Bundle bundle){
        if(this.bundle == null)
            this.bundle = bundle;
    }

    public final Bundle getCallback(){
        return this.bundle;
    }

}