package com.xiuxian.chen.include;
import android.graphics.*;
import android.text.*;
import android.text.style.*;
import android.view.*;
import java.util.*;

public final class TextItemDraw
{
	int text_max_item;
	
	Paint mPaint;
	
	int px, py;
	
	int sw;
	
	List<Text> texts;
	
	StaticLayout textdraw;
	
	//Context mContext;
	
	public TextItemDraw(View v)
	{
		sw = v.getWidth();
		this.text_max_item = 5;
		this.mPaint = new Paint();
		mPaint.setTextSize(30);
		this.texts = new ArrayList<Text>();
		//StaticLayout slayout;
		//th = getTextWH("陈[]→", mPaint).height();
	}
	
	public void setTextItemMax(int m){
		this.text_max_item=m;
	}
	
	public void setPosition(int x, int y){
		this.px = x;
		this.py = y;
	}
	
	public void setTextSize(float size){
		this.mPaint.setTextSize(size);
	}
	
	public void addText(Text t){
        if (t == null) return;

		if(texts.size() >= text_max_item)
			texts.remove(0);

        texts.add(t);

		SpannableStringBuilder sb = new SpannableStringBuilder("");

		for(Text tt : texts)
			sb.append(getColorText(tt.text+"\n", tt.text, tt.color));

		TextPaint paint = new TextPaint();

		paint.setColor(Color.WHITE);

		paint.setTextSize(mPaint.getTextSize());

        textdraw = new StaticLayout(sb, 0, sb.length(), paint, sw, Layout.Alignment.ALIGN_NORMAL, 1.1f, 0, false);

        //存在兼容问题
//		textdraw = StaticLayout.Builder
//		.obtain(sb, 0, sb.length(), paint, sw)
//		.build();
	}
	
	public SpannableString getColorText(String str, String key, int color){
		SpannableString sps=new SpannableString(str);
		ForegroundColorSpan fcs = new ForegroundColorSpan(color);
		int start=str.indexOf(key);
		int len=key.length();
		if(start==-1)return null;
		sps.setSpan(fcs, start, start+len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return sps;
	}
	
	private CharSequence getColorText(String str, int color, String... key){
		//SpannableString sps=new SpannableString(str);
		SpannableStringBuilder sb = new SpannableStringBuilder(str);
		for(String k : key){
		ForegroundColorSpan fcs = new ForegroundColorSpan(color);
		int start=str.indexOf(k);
		int len=k.length();
		if(start==-1)return null;
		sb.setSpan(fcs, start, start+len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		return sb;
	}
	
	public void onDraw(Canvas c)
	{
		c.save();
		c.translate(px, py);
		if(textdraw!=null) textdraw.draw(c);
		c.restore();
		/*
		int i=0;
		for (Text t : texts)
		{
			String text = t.text;
			
			int color = t.color;
			
			mPaint.setColor(color);
			
			int th = getTextWH(text, mPaint).height();
			
			c.drawText(text+"h"+th, px, py + ((th * i) + 10), mPaint);
			
			i++;
		}*/
	}
	
	private Rect getTextWH(String str, Paint mp){
		Rect r=new Rect();
		mp.getTextBounds(str,0,str.length(),r);
		return r;
	}
	
	public static class Text{
		public String text;
		public int color;
	}
	
	
}
