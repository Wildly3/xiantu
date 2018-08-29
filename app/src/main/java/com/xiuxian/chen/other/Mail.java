package com.xiuxian.chen.other;

import cn.bmob.v3.*;

public class Mail extends BmobObject
{
	//接收人id
	public int id;
	
	//发送人id
	public int pid;
	
	//发送人名称
	public String publisher;
	
	//信息
	public String msg;
	
	//邮件中的物品，以json形式表达
	public String goods;
	
}
