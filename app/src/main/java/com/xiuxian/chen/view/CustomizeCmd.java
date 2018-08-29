package com.xiuxian.chen.view;
import android.app.*;
import android.content.*;
import com.xiuxian.chen.include.*;
import java.text.*;
import android.os.*;
import com.xiuxian.chen.data.*;
import com.xiuxian.chen.other.*;

public final class CustomizeCmd
{
	private Context cx;
	
	private static CustomizeCmd customizecmd = null;
	
	private static final String[] CMD = {
		"mem",
		"set",
		"sv_cheats",
		"ver",
		"help",
		"info"
	};
	
	private Var now;
	
	private Bundle bundle;
	
	private String message;

	private String infomation;
	
	private String last_time_input;

	public void setLastTimeInput(String last_time_input)
	{
		this.last_time_input = last_time_input;
	}

	public String getLastTimeInput()
	{
		return last_time_input;
	}
	
	public String getMessage()
	{
		if(message == null) return null;
		String temp = new String(message);
		message = null;
		return temp;
	}
	
	private void setMessage(String msg){
		this.message = msg;
	}
	
	public static CustomizeCmd getInstance(){
		if(customizecmd == null)
			customizecmd = new CustomizeCmd();
		return customizecmd;
	}
	
	private CustomizeCmd(){
		this.bundle = new Bundle();
		this.infomation = "";
	}
	
	public void Context(Context cx){
		this.cx = cx;
	}
	
	public boolean enter(String cmd){
		now = new Var(cmd);
		
		int id = getItemId(now.getName());
		
		if(id == -1) return false;
		
		switch(id){
			case 0:
			memory();
			break;
			
			case 1:
			set();
			break;
			
			case 2:
			cheats();
			break;
			
			case 3:
			setMessage("当前版本："+MyILoadResource.getLocalVersionName(cx));
			break;
			
			case 4:
			
			break;
			
			case 5:
			info();
			break;
			
			default: return false;
		}
		
		return true;
	}

	private void info()
	{
		StringBuilder sb = new StringBuilder("/***  游戏信息  ***/");
		sb.append("\n游戏id：");
		sb.append(Shoping.getInstance().getId());
		sb.append("\n地图缓存数量：");
		sb.append(String.valueOf(BaseActivity.mapmanager.getCacheQuantity()));
		sb.append("\n物品、敌人、技能缓存数量：");
		sb.append(String.valueOf(BaseActivity.cache.getCacheQuantity()));
		
		
		setMessage(sb.toString());
	}
	
	public void setInfo(String str){
		this.infomation = str;
	}

	public String getInfoMation(){
		return this.infomation;
	}
	
	
	
	private void cheats()
	{
		int length = now.getVarNumber();
		if (length < 1)
		{
			message = "启动码空！sv_cheats <StartCode>";
			return ;
		}
		if (now.getVars()[0].equals("65592"))
			enter("set sv_cheats true");
		else message = "启动码错误！";
	}

	private void set()
	{
		int length = now.getVarNumber();

		if (length < 2)
		{
			message = "请输入正确格式 set <name> <value>";
			return ;
		}

		String key = now.getVars()[0];

		String var = now.getVars()[1];

		boolean isboolean = (var.equals("true") || var.equals("false"));

		if (isboolean)
		{
			Environment().putBoolean(key, Boolean.parseBoolean(var));
			return ;
		}

		boolean isstring = (var.indexOf("\"") == 0 && var.lastIndexOf("\"") == var.length() - 1);

		if (isstring)
		{
			Environment().putString(key, var.replaceAll("\"", ""));
			return ;
		}

		boolean isnumber = isNumber(var);

		if (isnumber)
		{
			Environment().putInt(key, Integer.parseInt(var));
			return ;
		}

		message = "请输入正确格式 set <name> <value>";
	}
	
	//设置环境变量
	public Bundle Environment(){
		return this.bundle;
	}
	
	void memory(){
		float memory = ((float)getMemory()/1024);
		
		StringBuilder sb = new StringBuilder("程序当前占用 ");
		
		DecimalFormat df = new DecimalFormat("#.##");
		
		ActivityManager manager = (ActivityManager)cx.getSystemService(Context.ACTIVITY_SERVICE);

		int heapSize = manager.getMemoryClass();
		
		sb.append(df.format(memory));
		
		sb.append(" MB");
		
		sb.append("\n程序堆最大内存 ");
		
		sb.append(String.valueOf(heapSize));
		
		sb.append(" MB");
		
		message = sb.toString();
	}
	
	//判断字符串是否为数字
	private boolean isNumber(String str){
		if(str == null || str.equals(""))
			return false;
		final char[] string = str.toCharArray();
		final char[] number = "0123456789".toCharArray();
		
		for(char s : string)
		for(char n : number)
		if(s != n)
			return false;
		
		return true;
	}
	
	private int getItemId(String cmd){
		int i=0;
		for(String c : CMD){
			if(c.equals(cmd))
				return i;
			i++;
		}
		
		return -1;
	}
	
	//获取本应用所占用的内存
	private int getMemory(){
		ActivityManager am = (ActivityManager)cx.getSystemService(Context.ACTIVITY_SERVICE);

		int pid = android.os.Process.myPid();

		android.os.Debug.MemoryInfo[] ms = am.getProcessMemoryInfo(new int[]{pid});

		return ms[0].getTotalPrivateDirty();
	}
	
	private final class Var
	{
		String[] list;
		
		String cmd;
		
		public Var(String cmd){
			if(cmd == null || cmd.equals(""))
				return;
			list = cmd.split(" ");
			this.cmd = list[0];
			if(list.length > 1)
			{
				String[] temp = new String[list.length-1];
				int j = 0;
				for(int i=1;i<list.length;i++){
					temp[j++] = list[i];
				}
				list = temp;
			} else list = null;
		}
		
		public int getVarNumber(){
			return list == null ? 0 : list.length;
		}
		
		public String getName(){
			return cmd;
		}
		
		public String[] getVars(){
			return list;
		}
		
	}
	
	
}
