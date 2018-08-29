package com.xiuxian.chen.adapter;

import android.content.*;
import android.graphics.*;
import android.view.*;
import android.widget.*;
import com.xiuxian.chen.*;
import java.io.*;
import java.util.*;
import android.os.*;

public class MyImageAdapter extends BaseAdapter implements Runnable
{
	private Context mContext;

	private List<Item> item;
	
	private Map<Integer, Bitmap> images;
	
	public MyImageAdapter(Context c){
		this.images = new HashMap<>();
		this.mContext = c;
		this.item = new ArrayList<>();
		//setItem(item);
	}
	
	public MyImageAdapter(Context cx, List<Item> item){
		this(cx);
		this.setItem(item);
	}

	public void add(Item item){
        String filename = "item/0"+item.ImageResid+".png";

        if(!images.containsKey(item.ImageResid)) {
            Bitmap bit = ReadBitmap(filename);

            images.put(item.ImageResid, bit);
        }

        this.item.add(item);

        notifyDataSetChanged();
    }

	public void clear(){
		this.item.clear();
		notifyDataSetChanged();
	}
	
	List<Item> temp;
	
	public void setItem(List<Item> item){
		this.item = item;
		for(Item it : item){
			String filename = "item/0"+it.ImageResid+".png";
			if(!images.containsKey(it.ImageResid)){
				Bitmap bit = ReadBitmap(filename);
				images.put(it.ImageResid, bit);
			}
			//this.item.add(it);
			//hanlde.sendEmptyMessage(0);
		}
		notifyDataSetChanged();
	}
	
	/*
	public void setItem(List<Item> item){
		this.setItem(item.toArray(new Item[item.size()]));
	}*/
	
	private Bitmap ReadBitmap(String filename){
		Bitmap bit = null;
		InputStream in=null;
		ByteArrayOutputStream bos = null;
		byte[] buf=new byte[1024];
		try
		{
			in = mContext.getAssets().open(filename);
			//fos=new FileOutputStream(filename);
			bos = new ByteArrayOutputStream();
			int len=0;
			while((len=in.read(buf))!=-1){
				bos.write(buf, 0, len);
			}
			
			//byte[] bit = bos.toByteArray();
			bit = BitmapFactory.decodeByteArray(bos.toByteArray(), 0, bos.size());
			
		}
		catch (IOException e)
		{}finally{
			try
			{
				if(in!=null)
					in.close();
				if(bos!=null)
					bos.close();
			}
			catch (IOException e)
			{}
		}
		
		return bit;
	}
	
	@Override
	public void run()
	{
		this.item=temp;
		for(Item it : item){
			String filename = "item/0"+it.ImageResid+".png";
			if(!images.containsKey(it.ImageResid)){
				Bitmap bit = ReadBitmap(filename);
				images.put(it.ImageResid, bit);
			}
			this.item.add(it);
			//hanlde.sendEmptyMessage(0);
		}
		//temp = null;
		notifyDataSetChanged();
	}
	
	Handler hanlde = new Handler(){
		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			
			
		}
	};
	
	@Override
	public int getCount()
	{
		return item.size();
	}

	@Override
	public Object getItem(int p1)
	{
		return item.get(p1);
	}

	@Override
	public long getItemId(int p1)
	{
		return p1;
	}

	@Override
	public View getView(int index, View contview, ViewGroup p3)
	{
		ViewHolder holder = null;
		if(contview==null){
			contview=LayoutInflater.from(mContext).inflate(R.layout.imagetv_list, null);
			holder = new ViewHolder();
			holder.image=(ImageView)contview.findViewById(R.id.imagetvlistImageView1);
			holder.tv=(TextView)contview.findViewById(R.id.imagetvlistTextView1);
			contview.setTag(holder);
			//t.setText(item[index]);
		}
		holder = (ViewHolder) contview.getTag();
		Bitmap bit = images.get(item.get(index).ImageResid);
		holder.image.setImageBitmap(bit);
		holder.tv.setText(item.get(index).text);
		
		return contview;
	}
	
	class ViewHolder{
		ImageView image;
		TextView tv;
	}
	
	public static class Item{
		int ImageResid;
		String text;
	}
	
	public static Item toItem(int id, String text){
		Item i=new Item();
		i.ImageResid=id;
		i.text=text;
		return i;
	}
	
}
