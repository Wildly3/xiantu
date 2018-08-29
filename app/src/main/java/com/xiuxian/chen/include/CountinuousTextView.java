package com.xiuxian.chen.include;

import android.widget.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.util.*;
import com.xiuxian.chen.view.*;
import com.xiuxian.chen.*;

public class CountinuousTextView extends TextView implements Runnable
{
	public static final int
	ACTION_DEFAULT = 0,//默认
	ACTION_SHOW = 1;//显示中
	
	//默认显示速度
	private static final int SHOW_SPEED_DEFAULT = 100;
	
	Context mContext;
	
	boolean is_first_draw;
	
	Handler handle;
	
	long show_speed;
	
	CharSequence text;
	
	int action;
	
	public CountinuousTextView(Context c){
		this(c, null, 0);
	}

	public CountinuousTextView(android.content.Context context, android.util.AttributeSet attrs) {
		this(context, attrs, 0);
	}

    public CountinuousTextView(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		mContext=context;
		text = "";
		show_speed = SHOW_SPEED_DEFAULT;
		setTextColor(context.getResources().getColor(R.color.color_alltext));
		handle = new Handler();
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		//System.exit(0);
		//Log.i(LoadResources.TAG, "绘制");
		if (!is_first_draw)
		{
			if (!"".equals(text))
			{
				index = 1;
				
				action = ACTION_SHOW;
				
				handle.postDelayed(this, show_speed);
			}
			
			is_first_draw = true;
		}
		
		super.onDraw(canvas);
	}
	
	int index;
	
	@Override
	public void run()
	{
		CharSequence text = this.text.subSequence(0, index);
		
		super.setText(text);
		
		//Log.i(LoadResources.TAG, text.length()+"--length--"+index+"---"+text);
		//System.exit(0);
		index++;
		
		if(index > this.text.length())
		{
			handle.removeCallbacks(this);
			
			action = ACTION_DEFAULT;
			
			return;
		}
		handle.postDelayed(this, show_speed);
	}
	
	//
	public void countText(CharSequence text){
		if(action == ACTION_SHOW) return;
		if("".equals(text)) return;
		this.text = text;
		if(!is_first_draw) return;
		index = 1;
		action = ACTION_SHOW;
		handle.postDelayed(this, show_speed);
	}
	
	public void reFirst(){
		this.index = 1;
		this.is_first_draw = false;
	}
	
	public CharSequence getCountText(){
		return this.text;
	}
	
	//显示全部内容
	public void showAllText(){
		handle.removeCallbacks(this);
		super.setText(text);
		action = ACTION_DEFAULT;
		index = 1;
	}
	
	//设置显示速度
	public void setShowSpeed(long speed){
		this.show_speed = speed;
	}
	
	//获取状态
	public int getAction(){
		return this.action;
	}
	
}
