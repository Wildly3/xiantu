package com.xiuxian.chen.other;
import java.util.*;

public class ExChangeGoodsAll
{
	public String password;
	public boolean isuse;
	List<ExChangeGoods> ecgs;
	
	public ExChangeGoodsAll(String pw){
		isuse=false;
		this.password=pw;
	}
	
	public ExChangeGoodsAll(String pw, ExChangeGoods ecg){
		isuse=false;
		this.password=pw;
		this.ecgs=new ArrayList<ExChangeGoods>();
		ecgs.add(ecg);
	}
	
	public void add(ExChangeGoods eg){
		if(ecgs==null)ecgs=new ArrayList<ExChangeGoods>();
		ecgs.add(eg);
	}
	
	public String Exchange(){
		if(isuse)return "该咒语已被使用！";
		StringBuilder sb=new StringBuilder("白光闪过，阵法启动完成！");
		for(ExChangeGoods ec : ecgs){
			sb.append("\n"+ec.ExChange());
		}
		isuse=true;
		return sb.toString();
	}
	
}
