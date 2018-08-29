package com.xiuxian.chen.data;

public class DataBackup
{
	private static final int LEFT=100;
	
	private int data1;
	
	private int data2;
	
	public DataBackup(){
		this.set(0);
	}
	
	//
	public void set(int i){
		this.data1 = i + LEFT;
		this.data2 = i + LEFT;
	}

	//判断是否修改
	public boolean equals(int i)
	{
		if(isSet())
		return false;
		
		if(this.getdata1() != i) return false;
		
		return true;
	}
	
	//备份数据是否修改
	public boolean isSet(){
		if(this.getdata1() != this.getdata2())
			return true;
		return false;
	}
	
	public int get(){
		if(this.getdata1() != this.getdata2())return 0;
		return this.getdata1();
	}
	
	private int getdata1(){
		return this.data1 - LEFT;
	}
	
	private int getdata2(){
		return this.data2 - LEFT;
	}
	
}
