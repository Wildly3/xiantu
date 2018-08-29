package com.xiuxian.chen.view;
import android.os.*;
import android.widget.*;
import android.view.View.*;
import android.view.*;
import android.app.*;
import com.xiuxian.chen.set.*;
import android.graphics.*;
import com.xiuxian.chen.include.*;
import android.widget.AdapterView.*;
import com.xiuxian.chen.map.*;
import android.content.*;
import com.xiuxian.chen.adapter.*;
import com.xiuxian.chen.*;

public class RoleActivity extends BaseActivity implements View.OnClickListener
{
    public static RoleActivity activity;

	final int resid[]={
		R.id.roleButton1_attr,
		R.id.roleButton1_space,
		R.id.roleButton1_kill,
		R.id.roleButton1_task
	};

	public static boolean isloadarchive;
	
	FrameLayout frame;
	
	ImageButton btn;
	
	TextView t1, t2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

        activity = this;
		
		LinearLayout line = (LinearLayout)getLayoutInflater().inflate(R.layout.tab, null);
		
		btn = (ImageButton)line.findViewById(R.id.tabImageButton1_close);
		
		t1=(TextView)line.findViewById(R.id.tabTextView1_title);
		
		t2=(TextView)line.findViewById(R.id.tabTextView1_title2);
		
		frame=(FrameLayout)line.findViewById(R.id.tabFrameLayout1);
		
		btn.setOnClickListener(this);
		
		frame.addView(getDecorView());
		
		setContentView(line);

        if (isloadarchive)
            startFragment(new RoleUI());
	}

	@Override
	public boolean GoBack()
	{
		boolean is = false;
		if((is = super.GoBack()))
		MainActivity.showBottom();
		return is;
	}
	
	@Override
	public void onClick(View p1)
	{
		GoBack();
	}
	
	@Override
	public void Action(int count)
	{
		super.Action(count);
		if(count < 2){
			btn.setVisibility(View.GONE);
		}else btn.setVisibility(View.VISIBLE);
		
		showTitle();
	}
	
	//刷新标题栏状态
	public final void showTitle(){
		if(base != null)
			t1.setText(base.name+"LV."+base.level);
		if(space != null)
			t2.setText("金币："+space.getMoney());
	}
	
}
