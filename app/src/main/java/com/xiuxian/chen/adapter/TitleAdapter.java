package com.xiuxian.chen.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xiuxian.chen.R;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.a.a.This;

/**
 * Created by Wildly on 2018/7/29.21:09
 */

public class TitleAdapter extends BaseAdapter
{
    List<InfoMation> item;

    Context cx;

    public TitleAdapter(Context cx){
        this.cx = cx;
        this.item = new ArrayList<>();
    }

    public void set(List<InfoMation> item){
        this.item = item;
        notifyDataSetChanged();
    }

    public void add(InfoMation item){
        this.item.add(item);
        notifyDataSetChanged();
    }

    public void clear(){
        this.item.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return item.size();
    }

    @Override
    public InfoMation getItem(int position) {

        return item.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(cx).inflate(R.layout.titleitem, null);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.TitleItem_title);
            holder.msg = (TextView) convertView.findViewById(R.id.TitleItem_msg);
            holder.setTitle(item.get(position).title);
            holder.setMsg(item.get(position).msg);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            holder.setTitle(item.get(position).title);
            holder.setMsg(item.get(position).msg);
        }

        return convertView;
    }

    private class ViewHolder{
        TextView title;
        TextView msg;

        void setTitle(String title){
            this.title.setText(title);
        }

        void setMsg(String msg){
            this.msg.setText(msg);
        }
    }

    public static final class InfoMation
    {
        public String title;

        public String msg;
    }
}
