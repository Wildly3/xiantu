package com.xiuxian.chen.adapter;
import android.widget.*;
import android.view.*;
import android.content.*;
import android.view.View.*;
import com.xiuxian.chen.*;
import java.util.*;

public class ButtonAdapter extends BaseAdapter implements android.view.View.OnClickListener
{
	private Context mContext;
	
	private OnClickListener onc;
	
	private OnClickListener onc2;
	
	private String[] item;
	
	private String btntitle;
	
	private boolean[] item_isclick;
	
	private String[] button_titles;
	
	public ButtonAdapter(Context cx){
		this.mContext = cx;
	}
	
	public ButtonAdapter(Context cx, String btntitle){
		this(cx);
		this.btntitle = btntitle;
	}
	
	public ButtonAdapter(Context cx, String[] str, String btntitle){
		this(cx);
		this.setItem(str, btntitle);
		this.btntitle = btntitle;
	}
	
	public void setItem(String[] item, String btnt){
		this.item = item;
		
		this.item_isclick = new boolean[item.length];
		
		this.button_titles = new String[item.length
		];
		for(int i=0;i<item.length;i++)
			this.button_titles[i] = btnt;
		
		notifyDataSetChanged();
	}
	
	public void setButtonText(String text, int index){
		this.button_titles[index] = text;
		notifyDataSetChanged();
	}
	
	//
	public void setButtonClickable(int index, boolean is){
		this.item_isclick[index] = !is;
	}
	
	public void clear(){
		this.item = null;
		this.item_isclick = null;
		notifyDataSetChanged();
		
	}
	
	public void setOnclickListener(OnClickListener o){
		this.onc=o;
	}
	
	public void setOnItemclickListener(OnClickListener o){
		this.onc2=o;
	}
	
	@Override
	public int getCount()
	{
		// TODO: Implement this method
		return item == null ? 0 : item.length;
	}

	@Override
	public Object getItem(int p1)
	{
		// TODO: Implement this method
		return item == null ? null : item[p1];
	}

	@Override
	public long getItemId(int p1)
	{
		// TODO: Implement this method
		return p1;
	}
	
	@Override
	public void onClick(View v)
	{
		if(onc2!=null && v.getTag().equals("text"))onc2.OnClick(v.getId());
		if(onc!=null && v.getTag().equals("btn"))onc.OnClick(v.getId());
	}
	
	@Override
	public View getView(int index, View contview, ViewGroup p3)
	{
		Holder h=null;
		if(contview==null){
			contview=LayoutInflater.from(mContext)
			.inflate(R.layout.button_list,null);
			h=new Holder();
			//h.r=(RelativeLayout)contview.findViewById(R.id.buttonlistRelativeLayout1);
			h.text=(TextView)contview.findViewById(R.id.buttonlistTextView1);
			h.btn=(Button)contview.findViewById(R.id.buttonlistButton1);
			h.back = (LinearLayout)contview.findViewById(R.id.buttonlistLinearLayout1);
			contview.setTag(h);
		}
			h=(Holder)contview.getTag();
			h.text.setText(item[index]);
			h.btn.setText(button_titles[index]);
			h.btn.setId(index);
			h.btn.setTag("btn");
			h.btn.setEnabled(!item_isclick[index]);
			//);
			h.back.setId(index);
			h.back.setTag("text");
			h.back.setOnClickListener(this);
			h.btn.setOnClickListener(this);
		
		return contview;
	}
	
	class Holder{
		LinearLayout back;
		TextView text;
		Button btn;
	}
	
	
	
	
	
}
