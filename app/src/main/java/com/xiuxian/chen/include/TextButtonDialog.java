package com.xiuxian.chen.include;

import android.app.*;
import android.content.*;
import android.widget.*;
import java.util.*;
import android.view.*;
import com.xiuxian.chen.*;
import android.view.View.*;

public class TextButtonDialog
{
	public MyDialog dialog;
	private Context mContext;
	private View view;
	public TextView text;
	private Button button;

	public TextButtonDialog(Context c){
		this.dialog=new MyDialog(mContext=c);
		dialog.setCancel("关闭",null);
		view=LayoutInflater.from(c).inflate(R.layout.dialog_textbutton,null);
		text=(TextView)view.findViewById(R.id.dialogtextbuttonTextView1);
		button=(Button)view.findViewById(R.id.dialogtextbuttonButton1);
		dialog.setView(view);
		dialog.setCancelable(false);
	}
	
	public TextButtonDialog setText(String str){
		text.setText(str);
		return this;
	}
	
	public TextButtonDialog setNegativeButton(String str, MyDialog.OnClickListener o){
		dialog.setCancel(str, o);
		return this;
	}
	
	public TextButtonDialog setButtonText(String text){
		button.setText(text);
		return this;
	}
	
	public TextButtonDialog setOnclickListener(OnClickListener o){
		button.setOnClickListener(o);
		return this;
	}
	
	public TextButtonDialog show(){
		dialog.show();
		return this;
	}

	public void dismiss(){
		this.dialog.dismiss();
	}
}
