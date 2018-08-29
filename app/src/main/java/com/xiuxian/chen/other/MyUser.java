package com.xiuxian.chen.other;

import cn.bmob.v3.*;

public class MyUser extends BmobObject
{
	private int id;
	
	public void setId(int id)
	{
		this.id = id;
	}

	public int getId()
	{
		return id;
	}
}
