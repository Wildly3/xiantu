package com.xiuxian.chen.include;

import android.app.*;
import android.content.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import com.xiuxian.chen.*;
import android.text.*;
import android.view.View.*;
import android.graphics.*;

public class NumberOperateDialog implements TextWatcher, OnClickListener
{
	public MyDialog dialog;
	private EditText edit;
	private Button reduce, plus, max;
	private TextView text;
	private Context mContext;
	private View view;
	private int input_number_max;
	private int input_number_min;
	private InputTextWatcher itw;
	
	public NumberOperateDialog(Context c){
		this.dialog = new MyDialog(mContext = c);
		this.input_number_min = 1;
		dialog.setTitle("输入数量");
		view = LayoutInflater.from(c).inflate(R.layout.dialog_numberop, null);
		edit = (EditText) view.findViewById(R.id.dialognumberopEditText1);
		reduce = (Button)view.findViewById(R.id.dialognumberopButton1);
		plus = (Button)view.findViewById(R.id.dialognumberopButton2);
		max = (Button)view.findViewById(R.id.dialognumberopButton3);
		text = (TextView)view.findViewById(R.id.dialognumberopTextView1);
		text.setTextColor(Color.RED);
		edit.setTextColor(c.getResources().getColor(R.color.color_alltext));
		edit.setText("1");
		edit.addTextChangedListener(this);
		reduce.setOnClickListener(this);
		plus.setOnClickListener(this);
		max.setOnClickListener(this);
		dialog.setinitView(true);
		dialog.setView(view);
        dialog.setTag(this);
	}
	
	public NumberOperateDialog setTitle(String title){
		this.dialog.SetTitle(title);
		return this;
	}
	
	//设置输入变化监听器
	public NumberOperateDialog setInputWatcher(InputTextWatcher i){
		itw=i;
		return this;
	}
	
	public NumberOperateDialog setText(String str){
		this.edit.setText(str);
		
		return this;
	}
	
	public NumberOperateDialog setMessage(String str){
		text.setText(str);
		return this;
	}
	
	public NumberOperateDialog setok(String text, MyDialog.OnClickListener o){
		dialog.setSelect(text, o);
		return this;
	}
	
	public NumberOperateDialog setcancel(String text, MyDialog.OnClickListener o){
		dialog.setCancel(text, o);
		return this;
	}
	
	//设置输入最大数
	public NumberOperateDialog setInputNumberMax(int m){
		this.input_number_max=m;
		return this;
	}
	
	//设置输入最小数
	public NumberOperateDialog setInputNumberMin(int m){
		this.input_number_min=m;
		return this;
	}

	public NumberOperateDialog show(){
		dialog.show();
		return this;
	}

	public void dismiss(){
		this.dialog.dismiss();
	}
	
	//获取输入数量
	public int getInputNumber(){
		return Integer.parseInt(
		edit.getText().toString().equals("")
		? "0" : edit.getText().toString());
	}
	
	@Override
	public void onClick(View v)
	{
		int id=v.getId();
		
		String s=edit.getText().toString();
		
		int n=Integer.parseInt(s.equals("") ? "0" : s);
		
		if(n==0)edit.setText(""+input_number_min);
		
		switch(id){
			case R.id.dialognumberopButton1:
				if(n>input_number_min)edit.setText(String.valueOf(--n));
			break;
			
			case R.id.dialognumberopButton2:
				edit.setText(String.valueOf(++n));
			break;
			
			case R.id.dialognumberopButton3:
				edit.setText(String.valueOf(input_number_max));
			break;
		}
	}
	
	@Override
	public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4)
	{
		// TODO: Implement this method
	}

	@Override
	public void onTextChanged(CharSequence p1, int p2, int p3, int p4)
	{
		// TODO: Implement this method
	}

	@Override
	public void afterTextChanged(Editable e)
	{
		if(e==null)return;
		
		if(e.toString().equals("")){
			edit.setText(""+input_number_min);
			if(itw!=null)itw.afterTextChanged(input_number_min);
			return;}
		int n=Integer.parseInt(e.toString());
		if(n>input_number_max)edit.setText(""+input_number_max);
		
		if(input_number_max>=input_number_min&&n<input_number_min)edit.setText(""+input_number_min);
		
		if(itw!=null)itw.afterTextChanged(Integer.parseInt(edit.getText().toString()));
	}
	
	public static interface InputTextWatcher
	{
		public void afterTextChanged(int i);
	}
	
	
}
