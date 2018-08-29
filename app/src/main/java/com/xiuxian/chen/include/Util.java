package com.xiuxian.chen.include;

import android.graphics.Color;
import android.os.*;
import dalvik.system.*;
import java.io.*;
import java.util.Random;

import android.content.*;

import com.xiuxian.chen.R;

public final class Util
{
	public static Object DexToObject(Context cx, String filename, String classname){
		Object obj=null;
		
		 DexClassLoader cl = new DexClassLoader(filename,
		 cx.getCacheDir().getAbsolutePath(), null, cx.getClassLoader());
		 Class libProviderClazz = null;
		 try
		 {
		 libProviderClazz = cl.loadClass(classname);

		 obj = libProviderClazz.newInstance();
		 
		 }
		 catch (InstantiationException e)
		 {}
		 catch (IllegalAccessException e)
		 {}
		 catch (ClassNotFoundException e)
		 {}
		
		 return obj;
	}
	
	public static void Log(Context cx, String tag, String str){
		
		FileOutputStream fos = null;
		try
		{
			fos = cx.openFileOutput(Environment.getExternalStorageDirectory().toString()+"/print.txt", cx.MODE_APPEND);
			//fos = new FileOutputStream(Environment.getExternalStorageDirectory().toString()+"/print.txt");
			fos.write((tag+"   "+str+"\n").getBytes());
		}
		catch (IOException e)
		{}
		finally
		{
			try
			{
				if (fos != null)
					fos.close();
			}
			catch (IOException e)
			{}

		}
	}

	//几率（千分之）
    public static boolean odds(float o){
        int a=(new Random().nextInt(1000));
        if(a<(o*1000))return true;
        return false;
    }

    public static String[] getRaceString(int race, Context cx){
        String[] str = new String[2];
        switch (race){
            default: return new String[]{"null", String.valueOf(Color.WHITE)};
            case 0: return new String[]{"人族", String.valueOf(cx.getResources().getColor(R.color.people))};
            case 1: return new String[]{"妖族", String.valueOf(cx.getResources().getColor(R.color.beast))};
            case 2: return new String[]{"魔族", String.valueOf(cx.getResources().getColor(R.color.monster))};
            case 3: return new String[]{"鬼族", String.valueOf(cx.getResources().getColor(R.color.ghost))};
        }
    }

}
