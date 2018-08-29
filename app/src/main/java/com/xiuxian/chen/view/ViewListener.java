package com.xiuxian.chen.view;
import android.os.*;
import android.view.KeyEvent;

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

    public boolean onKeyDown(int KeyCode, KeyEvent event){
        return false;
    }

    public boolean onKeyUp(int KeyCode, KeyEvent event){
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
