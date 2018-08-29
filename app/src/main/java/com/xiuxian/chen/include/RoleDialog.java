package com.xiuxian.chen.include;
import android.content.*;
import android.text.*;
import android.text.style.*;
import android.view.*;
import android.widget.*;
import com.xiuxian.chen.*;
import com.xiuxian.chen.data.*;
import com.xiuxian.chen.map.*;
import java.util.*;

public class RoleDialog extends MyDialog
{
	private List<Content> message;
	
	private int index;
	
	private String function;
	
	private TextView title;
	
	private CountinuousTextView msg;
	
	private LinearLayout line;
	
	public RoleDialog(Context cx){
		super(cx, MyILoadResource.SCREEN_WIDTH,
			  (MyILoadResource.SCREEN_HEIGHT - MainActivity.event.getMapView().getHeight())-50
			  -MainActivity.tabHost.getTabWidget().getHeight()
		);
		setCancelable(false);
		message=new ArrayList<>();
		index = 0;
		function = null;
		setinitView(true);
		RemoveTitle();
		View v = LayoutInflater.from(cx).inflate(R.layout.roledialog, null);
		title=(TextView)v.findViewById(R.id.roledialogTextView1_title);
		//title.setTextColor(Color.WHITE);
		//title.setTextSize(35);
		//msg=(CountinuousTextView)v.findViewById(R.id.roledialogTextView2_message);
		line = (LinearLayout)v.findViewById(R.id.roledialogLinearLayout1);
		msg = new CountinuousTextView(cx);
		msg.reFirst();
		line.addView(msg);
		setView(v);
		getWindow().setGravity(Gravity.BOTTOM);
		getWindow().getDecorView()
		.setPadding(0,0,0,MainActivity.tabHost.getTabWidget().getHeight());
		//.setLayoutParams(new ViewGroup.LayoutParams(-1, 500));
	}
	
	//com.xiuxian.chen.include.CountinuousTextView b;
	
	public void addsay(CharSequence t, CharSequence str){
		//CharSequence title = getColorText((String)t, Color.BLUE);
		if(message.size()<1){
			setTitleText(t);
			setMessageText(str);
			}
		message.add(Content.toContent(t, str));
	}
	/*
	public void addsay(String t, String str){
		SpannableString sp = new SpannableString(t.subSequence(0, t.length()));
		ForegroundColorSpan fcs = new ForegroundColorSpan(Color.RED);
		sp.setSpan(fcs, 0, t.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
		this.addsay(sp, str);
	}*/
	
	private void setTitleText(CharSequence text){
		this.title.setText(text);
	}
	
	private void setMessageText(CharSequence text){
		this.msg.countText(text);
	}
	
	public void add(Content c){
		if(message.size()<1){
			setTitleText(c.title);
			setMessageText(c.message);
		}
		message.add(c);
	}
	
	//对话结束时调用函数
	public void end(String end){
		this.function = end;
	}
	
	public void say(){
		this.Show();
		//RemoveTitle();
	}
	
	public void addMessage(List<Content> str){
		setTitleText(str.get(0).title);
		setMessageText(str.get(0).message);
		message.addAll(str);
	}
/*
	@Override
	public void setTitle(CharSequence title)
	{
		super.setTitle(getColorText((String)title, Color.RED));
	}
	*/
	/*
	局部字体改变颜色
	@str 原文
	@key 变色文字
	@color 颜色
	*/
	public SpannableString getColorText(String str, String key, int color){
		SpannableString sps=new SpannableString(str);
		ForegroundColorSpan fcs = new ForegroundColorSpan(color);
		int start=str.indexOf(key);
		int len=key.length();
		if(start==-1)return null;
		sps.setSpan(fcs, start, start+len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return sps;
	}
	
	public SpannableString getColorText(String str, int color){
		return this.getColorText(str, str, color);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if(event.getAction()==MotionEvent.ACTION_DOWN){
			if(index==message.size()-1)
			{
				if(msg.getAction()!=CountinuousTextView.ACTION_DEFAULT){
					msg.showAllText();
					return true;
				}
				dismiss();
				if(function != null && !function.equals("")) EventRun.GO(function);
				return true;
			}
			if(msg.getAction() == CountinuousTextView.ACTION_DEFAULT){
				//Log.i(LoadResources.TAG, ""+msg.getCountText());
			index=(index<message.size()-1 ? index+1 : index);
			setTitleText(message.get(index).title);
			setMessageText(message.get(index).message);
			}else msg.showAllText();
		}
		return true;
	}
	
	public static class Content
	{
		public CharSequence title;
		public CharSequence message;
		
		public static final Content toContent(CharSequence t, CharSequence m){
			Content c = new Content();
			c.title=t;
			c.message=m;
			return c;
		}
		
		
	}
	
}
