package com.xiuxian.chen.other;

public class Shoping
{
	private static Shoping shop;
	
	private int id;
	
	//管理员id
	public static final int ADMINISTRATOR = -666;
	
	private Shoping(){
		this.id = -1;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public int getId()
	{
		return id;
	}
	
	public static Shoping getInstance(){
		if(shop == null)
			shop = new Shoping();
		
		return shop;
	}
	
}
