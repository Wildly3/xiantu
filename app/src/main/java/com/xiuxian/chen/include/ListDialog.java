package com.xiuxian.chen.include;
import android.content.*;
import android.view.*;
import android.widget.*;
import com.xiuxian.chen.adapter.*;
import java.util.*;
import android.view.ViewGroup.*;

public class ListDialog
{
	public MyDialog dialog;
	private ListView list;
	private Context mContext;
	
	public ListDialog(Context c){
		this.dialog=new MyDialog(mContext=c);
		list=new ListView(c);
		list.setDivider(null);
		list.setVerticalScrollBarEnabled(false);
		list.setDividerHeight((int)c.getResources().getDisplayMetrics().density * 3);
		dialog.setView(list);
	}
	
	public ListDialog setListItem(String[] item){
		this.list.setAdapter(new MyAdapter(mContext, item));
		return this;
	}
	
	public ListDialog setListItem(List<String> item){
		this.list.setAdapter(new MyAdapter(mContext, item));
		return this;
	}
	
	public ListDialog setImageListItem(List<MyImageAdapter.Item> item){
		this.list.setAdapter(new MyImageAdapter(mContext, item));
		return this;
	}

	public ListDialog setAdapter(ListAdapter adapter){
        this.list.setAdapter(adapter);
        return this;
    }

	public ListDialog setTitle(String title){
		this.dialog.SetTitle(title);
		return this;
	}
	
	public ListDialog setOnItemClickListener(android.widget.AdapterView.OnItemClickListener l){
		this.list.setOnItemClickListener(l);
		return this;
	}
	
	public ListDialog setHeight(int height){
		//dialog.getWindow().getDecorView().getHeight();
		LayoutParams lp = new LayoutParams(-1, height);
		list.setLayoutParams(lp);
		return this;
	}
	
	public ListDialog show(){
		dialog.show();
		return this;
	}
	
	public void dismiss(){
		this.dialog.dismiss();
	}
	
}
