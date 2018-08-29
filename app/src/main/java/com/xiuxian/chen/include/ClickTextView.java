package com.xiuxian.chen.include;

import android.content.*;
import android.graphics.*;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import com.xiuxian.chen.*;
import android.util.*;

public class ClickTextView extends ScrollView implements OnTouchListener, OnClickListener
{
	Context mContext;
	
	RelativeLayout re;
	
	LinearLayout main;
	
	TextView tv;
	
	String text;
	
	boolean isFirst = true;
	
	public ClickTextView(Context cx){
		this(cx, null);
	}
	
	public ClickTextView(Context cx, AttributeSet attr){
		super(cx, attr);
		
		this.mContext = cx;
		
		re = new RelativeLayout(cx);
		
		main = new LinearLayout(cx);
		
		main.setOrientation(main.VERTICAL);
		
		re.addView(main);
		
		this.tv = new TextView(cx);
		
		this.tv.setTextColor(Color.WHITE);
		
		this.tv.setTextSize(15.0f);
		
		tv.setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
		
		tv.setBackgroundColor(Color.rgb(80, 80, 80));
		
		tv.setVisibility(View.GONE);
		
		tv.setOnClickListener(this);
		
		this.setOnClickListener(this);
		
		re.addView(tv);
		
		super.addView(re);
	}
	
	public void addText(String text, String ctext){
		this.addText(text, ctext, 15f, mContext.getResources().getColor(R.color.color_alltext));
	}
	
	public void addText(String text, String ctext, float ts, int color){
		TextView tv = new TextView(mContext);
		
		tv.setTextSize(ts);
		
		tv.setTextColor(color);
		
		tv.setText(text);
		
		if (ctext != null && !ctext.equals(""))
		{
			tv.setTag(ctext);
			
			tv.setBackgroundResource(R.drawable.selector_button);
			
			tv.setGravity(Gravity.CENTER);
			
			tv.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v)
					{
						if(!is_click){
							ClickTextView.this.is_click = true;
							ClickTextView.this.text = (String)v.getTag();
							ClickTextView.this.tv.setVisibility(View.VISIBLE);

							if(isFirst){
                                String first = "小提示：点击框内关闭详细信息！";

                                String str = ClickTextView.this.text + "\n" + first;

                                ClickTextView.this.tv.setText(getColorText(str, first, Color.YELLOW));

								isFirst = false;
							}
							else
							ClickTextView.this.tv.setText(ClickTextView.this.text);
						}else{
							ClickTextView.this.text = (String)v.getTag();
							//ClickTextView.this.tv.setVisibility(View.VISIBLE);
							ClickTextView.this.tv.setText(ClickTextView.this.text);
						}
					}
				});
		}
		
		this.main.addView(tv);
	}


    /**
     * 把string关键字改变颜色并返回数据
     * @param str 源字符串
     * @param key 关键字
     * @param color 颜色
     * @return 更改后数据
     */
    public SpannableString getColorText(String str, String key, int color){
        SpannableString sps=new SpannableString(str);
        ForegroundColorSpan fcs = new ForegroundColorSpan(color);
        int start=str.indexOf(key);
        int len=key.length();
        if(start == -1)return null;
        sps.setSpan(fcs, start, start+len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sps;
    }

	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
		switch(event.getAction()){
			case MotionEvent.ACTION_DOWN:
			if(!is_click){
			this.is_click = true;
			this.text = (String)v.getTag();
			tv.setVisibility(View.VISIBLE);
			tv.setText(this.text);
			}
			else onClick(null);
			break;
		}
		
		return true;
	}
	
	boolean is_click;
	
	@Override
	public void onClick(View v)
	{
		if(this.is_click){
		this.is_click = false;
		tv.setVisibility(View.GONE);
		}
	}
	
	
}
