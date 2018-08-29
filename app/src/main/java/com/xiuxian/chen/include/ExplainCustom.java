package com.xiuxian.chen.include;

import java.io.*;
import java.util.regex.*;

public final class ExplainCustom
{
	private final String LEFT="<";
	private final String RIGHT=">";
	private String str;
	private Class<?> mclass;
	private Object obj;
	private Handle h=null;
	
	public ExplainCustom(Class<?> c, Object obj, String str){
		this.mclass=c;
		this.obj=obj;
		this.str=str;
	}
	
	public void setHandle(Handle h){
		this.h=h;
	}
	
	public String getString(){
		String[] key=getKeyString(str, "<", ">");

		for(int i=0;i<key.length;i++){

			str=str.replace("<"+key[i]+">", "%d");
			try
			{
				Object objs=mclass.getField(key[i]).get(obj);
				if(h!=null)objs=h.handle(objs, key[i]);
				str = printf(str, objs);
			}
			catch (NoSuchFieldException e)
			{}
			catch (IllegalAccessException e)
			{}
			catch (IllegalArgumentException e)
			{}
		}

		return str;
	}

	private String[] getKeyString(String str, String start, String end){
		String list[]=null;
		int max=0;
		if((max=getkeynum(str, start))!=getkeynum(str, end))
			return null;
		int[] s=new int[max], e=new int[max];
		list=new String[max];
		s=getKeyIndex(str,start);
		e=getKeyIndex(str,end);
		for(int i=0;i<max;i++)
			list[i]=str.substring(s[i]+1, e[i]);
		return list;
	}

	private int[] getKeyIndex(String str, String key){
		int index[]=new int[getkeynum(str,key)];
		int i=0;
		Pattern pt=Pattern.compile(key);
		Matcher mt=pt.matcher(str);
		while(mt.find())index[i++]=mt.start();
		return index;
	}


	private int getkeynum(String str, String key){
		int num=0;
		Pattern pt=Pattern.compile(key);
		Matcher mt=pt.matcher(str);
		while(mt.find())num++;

		return num;
	}


	private String printf(String str, Object...obj){
		ByteArrayOutputStream bos=new ByteArrayOutputStream();
		PrintStream ps=new PrintStream(bos);
		ps.printf(str,obj);
		ps.close();
		return new String(bos.toString());
	}
	
	public static interface Handle{
		public Object handle(Object obj, String s);
	}
	
	
}
