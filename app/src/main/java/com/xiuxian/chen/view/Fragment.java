package com.xiuxian.chen.view;
import android.content.*;
import android.view.*;
import android.widget.*;
import com.xiuxian.chen.include.*;
import android.os.*;

public abstract class Fragment extends ViewListener
{
	private FragmentActivity mBase;
	
	private boolean first;
	
	@Override
	public void onLoad(LayoutStack app) {}
	
	public void attchBaseActivity(FragmentActivity mBase){
		if(this.mBase == null)
		this.mBase = mBase;
	}
	
	protected FragmentActivity getBaseActivity(){
		return this.mBase;
	}
	
	protected void onCreate(Bundle bundle){
		super.setCallback(bundle);
	}
	
	protected void setContentView(int id){
		if(!first){
			first = this.mBase.Goin(id, this);
		}
	}
	
	private void setContentView(View contentview){
		if(!first){
			first = this.mBase.Goin(contentview, this);
		}
	}
	
	protected View findViewById(int id){
		return this.mBase.FindView(id);
	}
	
	public Bundle getBundle(){
		return super.getCallback();
	}
	
	public Context getContext(){
		return this.mBase;
	}

	public FragmentActivity getActivity(){
        return this.mBase;
    }

    public boolean startFragment(Fragment fragment){
        return mBase.startFragment(fragment);
    }

    public boolean startFragment(Fragment fragment, Bundle bundle){
        return mBase.startFragment(fragment, bundle);
    }

	//退出
	public boolean exit(){
        if(!first) return false;
        return this.mBase.GoBack();
	}

	public void finish(){
        ViewListener vl = (ViewListener) this;
        this.mBase.RemoveListView(vl);
    }

	public void msgdialog(String t){
		new MyDialog(this.getContext())
			.setMessage(t)
			.setCancel("关闭", null)
			.show();
	}
	
	public void toast(String str){
		MyToast.makeText(mBase, str, Toast.LENGTH_SHORT, Gravity.CENTER).show();
	}
	
}
