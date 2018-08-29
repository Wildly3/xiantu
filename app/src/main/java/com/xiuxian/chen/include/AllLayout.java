package com.xiuxian.chen.include;
import android.content.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import com.xiuxian.chen.*;
import com.xiuxian.chen.view.*;

public final class AllLayout extends LinearLayout
{
	Context cx;
	
	public AllLayout(Context cx){
		this(cx, null);
	}
	
	public AllLayout(Context cx, AttributeSet attr){
		super(cx, attr);
		this.cx = cx;
		setOrientation(VERTICAL);
	}
	
	//判断view里是否有元素
	public boolean isEmpty(){
		return getChildCount() > 0;
	}

	public LinearLayout CreateLayout(){
        LinearLayout layout = new LinearLayout(cx);

        layout.setOrientation(LinearLayout.VERTICAL);

//        layout.setBackgroundResource(R.drawable.shape_corner);

        return layout;
    }

    public void addText(CharSequence text, float textsize, int color, LinearLayout layout){
        TextView tv = new TextView(cx);
        tv.setText(text);
        tv.setTextSize(textsize);
        tv.setTextColor(color);
        layout.addView(tv);
    }

    public void addLayout(LinearLayout view){
        this.addView(view);
    }

	public void addText(CharSequence text, float textsize, int color){
		TextView tv=new TextView(cx);
		tv.setText(text);
		tv.setTextSize(textsize);
		tv.setTextColor(color);
		addView(tv);
	}
	
	public void addProgres(ProgressBar progress){
		View v = LayoutInflater.from(cx).inflate(R.layout.textprogres, null);
		TextView tv=(TextView)v.findViewById(R.id.textprogresTextView1);
		TextView tv2 = (TextView)v.findViewById(R.id.textprogresTextView2);
		android.widget.ProgressBar prog = (android.widget.ProgressBar)v.findViewById(R.id.textprogresProgressBar1);
		tv.setText(progress.getTitle());
		tv.setTextSize(progress.getTextSize());
		tv.setTextColor(progress.getTextColor());
		tv2.setTextColor(Color.WHITE);
		if(progress.getMsg() != null)
			tv2.setText(progress.getMsg());
			GradientDrawable gd = new GradientDrawable();
			gd.setColor(progress.getProgressColor() == -1 ? progress.getTextColor() : progress.getProgressColor());
			gd.setCornerRadius(MainActivity.DP*5);
		ClipDrawable d =
			new ClipDrawable(gd, Gravity.LEFT, ClipDrawable.HORIZONTAL);
		prog.setProgressDrawable(d);
		
		prog.setProgress(progress.getProgress());
		addView(v);
	}
	
	public static class ProgressBar
	{
		private CharSequence title;
		
		private CharSequence msg;
		
		private float textsize;
		
		private int textcolor;
		
		private int progress;
		
		private int progresscolor;
		
		public ProgressBar(){
			textsize = 15f;
			progresscolor = Color.GREEN;
			textcolor = Color.WHITE;
		}

		public void setProgressColor(int progresscolor)
		{
			this.progresscolor = progresscolor;
		}

		public int getProgressColor()
		{
			return progresscolor;
		}

		public void setProgress(int progress)
		{
			this.progress = progress;
		}

		public int getProgress()
		{
			return progress;
		}

		public void setTextColor(int textcolor)
		{
			this.textcolor = textcolor;
		}

		public int getTextColor()
		{
			return textcolor;
		}

		public void setTextSize(float textsize)
		{
			this.textsize = textsize;
		}

		public float getTextSize()
		{
			return textsize;
		}

		public void setMsg(CharSequence msg)
		{
			this.msg = msg;
		}

		public CharSequence getMsg()
		{
			return msg;
		}

		public void setTitle(CharSequence title)
		{
			this.title = title;
		}

		public CharSequence getTitle()
		{
			return title;
		}
		
		public void setAllColor(int color){
			setTextColor(color);
			setProgressColor(-1);
		}
	}
	
}
