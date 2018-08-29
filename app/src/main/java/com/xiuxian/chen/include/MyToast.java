package com.xiuxian.chen.include;
import android.widget.*;
import android.content.*;
import android.view.*;
import com.xiuxian.chen.*;
import android.view.ViewGroup.*;
import android.app.*;

public class MyToast
{
	public static Toast makeText(Activity cx, String text, int dura){
		Toast t=new Toast(cx);
		View v=View.inflate(cx, R.layout.toast, null);
		TextView tv=(TextView)v.findViewById(R.id.toastTextView1);
		LayoutParams lp=tv.getLayoutParams();
		float dp =cx.getResources().getDisplayMetrics().density;
		lp.width=cx.getWindow().getWindowManager().getDefaultDisplay().getWidth()-(int)(dp*30);
		tv.setLayoutParams(lp);
		tv.setText(text);
		t.setView(v);
		t.setGravity(Gravity.CENTER, 0, 0);
		t.setDuration(dura);
		return t;
	}
	
	public static Toast makeText(Activity cx, String text, int dura, int gravity){
		Toast t=makeText(cx, text, dura);
		t.setGravity(gravity, 0, 0);
		return t;
	}
	
}
