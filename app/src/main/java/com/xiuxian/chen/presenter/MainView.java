package com.xiuxian.chen.presenter;
import com.xiuxian.chen.include.*;

public interface MainView
{
	public void Complete(int type);
	
	public void ShowDialog(MyDialog dialog);
	
	public void ShowMessage(String msg);
}
