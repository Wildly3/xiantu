package com.xiuxian.chen.include;
import android.content.*;
import java.io.*;
import java.math.*;
import java.security.*;
import java.util.*;
import android.util.*;

/*
数据的读写，所有的数据皆为String
@Saver(c, filename)
c 上下文
filename 文件名
*/

public class Saver
{
	static final String MD5=".md5";
	
	static final String DATA=".dat";

    //读取状态
	public static final int SUCCESS=0, FAIL=1;
	
	private Context mContext;
	
	private Map<String, String> data;
	
	private String filename;
	
	public Saver(Context c, String filename){
		this.mContext=c;
		this.data=new HashMap<>();
		this.filename=filename;
	}
	
	//插入值
	public void put(String key, String value){
		this.data.put(key, value);
	}
	
	public void put(String key, int value){
		this.data.put(key, String.valueOf(value));
	}
	
	public void put(String key, boolean value){
		this.data.put(key, String.valueOf(value));
	}
	
	//获取值
	public String get(String key){
		return this.data.get(key);
	}
	
	public int getInt(String key){
		String value = this.data.get(key);
		return value == null ? -1 : Integer.parseInt(value);
	}
	
	public boolean getBoolean(String key){
		return Boolean.parseBoolean(this.data.get(key));
	}
	
	//写入数据
	public boolean complete(){
		StringBuffer sb=new StringBuffer("");

		int i=0;

		for(Map.Entry<String,String> m : data.entrySet()){
			sb.append(m.getKey()+":"+m.getValue()+(i++ == data.size()-1 ? "" : "\n"));
		}

		return write(sb.toString());
	}

	//保存字符串
	public boolean complete(String str){
        return write(str);
    }

	//写入数据并生成md5
	private boolean write(String str){
        FileOutputStream fos = null;

        FileOutputStream fos2 = null;
        try
        {
            fos = mContext.openFileOutput(getDataFileName(), Context.MODE_PRIVATE);

            fos2 = mContext.openFileOutput(getDataMd5FileName(), Context.MODE_PRIVATE);

            byte[] buffer = (str).getBytes();

            String md5="";
            try
            {
                md5 = getMD5(buffer);
            }
            catch (NoSuchAlgorithmException e)
            {}
            fos.write(buffer);

            fos2.write(md5.getBytes());
        }
        catch (IOException e)
        {
            return false;
        }
        finally{
            try
            {
                if(fos!=null)
                    fos.close();
            }
            catch (IOException e)
            {}

            try
            {
                if(fos2!=null)
                    fos2.close();
            }
            catch (IOException e)
            {}
        }

        return true;
    }

	//读取数据
	public int Reader(){
		data.clear();

        String content = read();

        if (content == null || content.equals(""))
            return FAIL;

        String maps[] = (content.split("\n"));

        for(String s : maps){
            String ss[]=s.split(":");
            data.put(ss[0], ss[1]);
        }

		return SUCCESS;
	}

	//读取字符串
	public String ReadString(){
        return read();
    }

    private String read(){
        String md5="", md52="";

        FileInputStream fis=null;

        FileInputStream fis2=null;

        try {
            fis = this.mContext.openFileInput(getDataFileName());

            fis2 = this.mContext.openFileInput(getDataMd5FileName());

            if (fis == null | fis2 == null) {
                return null;
            }

            if (fis.available() <= 0 | fis2.available() <= 0) {
                return null;
            }

            byte[] buffer1 = new byte[fis.available()];

            byte[] buffer2 = new byte[fis2.available()];

            fis.read(buffer1);

            fis2.read(buffer2);

            md5 = new String(buffer2);

            md52 = getMD5(buffer1);

            if (!md52.equals(md5)) {
                buffer1 = buffer2 = null;

                fis.close();

                fis2.close();

                return null;
            }

            return new String(buffer1);
        }catch (Exception e){
            return null;
        }finally {
            try {
                if (fis != null)
                    fis.close();
                if (fis2 != null)
                    fis2.close();
            } catch (IOException e) {}

        }
    }

	//把获得的数据以map形式返回
	public List<Entry> toList()
    {
        List<Entry> list = new ArrayList<>();
        for(Map.Entry<String, String> entry : this.data.entrySet())
        {
            Entry e = new Entry();
            e.setKey(entry.getKey());
            e.setValue(entry.getValue());
            list.add(e);
        }
        return list;
    }

	public static class Entry
    {
        private String key;
        private String value;

        public String getKey()
        {
            return this.key;
        }

        public String getValue()
        {
            return this.value;
        }

        public int getIntegerValue()
        {
            return Integer.parseInt(this.value);
        }

        public void setKey(String key)
        {
            this.key = key;
        }

        public void setValue(String value)
        {
            this.value = value;
        }

    }

	//删除数据
	public boolean Delete(){
		return this.mContext.deleteFile(getDataFileName()) & this.mContext.deleteFile(getDataMd5FileName());
	}
	
	//获取一个字节集的md5值
	private String getMD5(byte[] buffer) throws NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance("MD5");
		digest.update(buffer);
		BigInteger bigInt = new BigInteger(1, digest.digest());
		return bigInt.toString(16);
	}
	
	private String getDataFileName(){
		StringBuilder sb = new StringBuilder(filename);
		sb.append(DATA);
		return sb.toString();
	}
	
	private String getDataMd5FileName(){
		StringBuilder sb = new StringBuilder(filename);
		sb.append(MD5);
		return sb.toString();
	}
	
}
