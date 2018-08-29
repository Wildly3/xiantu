package com.xiuxian.chen.adapter;
import android.content.*;
import android.view.*;
import android.widget.*;
import com.xiuxian.chen.*;
import java.util.*;

public class MyAdapter extends BaseAdapter
{
	Context mContext;
	
	List<String> item;

	private ViewGroup parent;

	public MyAdapter(Context c, String[] item){
		this.mContext=c;
		this.item = new ArrayList<>(item.length);
		for(String s : item){
			this.item.add(s);
		}
	}

	public MyAdapter(Context c, List<String> item){
		this.mContext = c;
		this.item = item;
	}

	public MyAdapter(Context cx){
        this.mContext = cx;
        this.item = new ArrayList<>();
    }

	public void setParentView(ViewGroup parent){
		this.parent = parent;
	}
	
	@Override
	public int getCount()
	{
		// TODO: Implement this method
		return item.size();
	}

	@Override
	public Object getItem(int p1)
	{
		// TODO: Implement this method
		return item.get(p1);
	}

	@Override
	public long getItemId(int p1)
	{
		// TODO: Implement this method
		return p1;
	}

	@Override
	public View getView(int index, View contview, ViewGroup p3)
	{
		if(contview == null){
			if(parent == null)
			contview = LayoutInflater.from(mContext).inflate(R.layout.colourlist, null);
			else
			contview = LayoutInflater.from(mContext).inflate(R.layout.colourlist, parent, false);
			
			TextView t = (TextView)contview.findViewById(R.id.colourlistTextView1);
			
			contview.setTag(t);
			
			t.setText(item.get(index));
		}else {
			((TextView)contview.getTag()).setText(item.get(index));
		}
		
		return contview;
	}

	public void set(List<String> item){
        this.item = item;
        notifyDataSetChanged();
    }

	public void add(String str){
        this.item.add(str);
        notifyDataSetChanged();
    }

	public void remove(int index){
		this.item.remove(index);
		notifyDataSetChanged();
	}
	
	public void clear(){
		this.item.clear();
		notifyDataSetChanged();
	}
	
}
