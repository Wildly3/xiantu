package com.xiuxian.chen.data;

import com.xiuxian.chen.set.*;
import java.io.*;
import java.util.*;
import org.json.*;

public final class ObjectScan
{
	//最大缓存数量
	public static final int OBJECT_MAX = 20;
	
	private static ObjectScan objectscan = null;
	
	private String path;
	
	private Weapon[] EQUIPS;
	
	private int eq_index;
	
	private Consunables[] CONSUNABLES;
	
	private int con_index;
	
	private Material[] MATERAIALS;
	
	private int mt_index;
	
	private Enemy[] ENEMYS;
	
	private int ene_index;
	
	private Skill[] SKILLS;
	
	private int sk_index;
	
	private ObjectScan(String path){
		this.path = path;
		this.EQUIPS = new Weapon[OBJECT_MAX];
		this.CONSUNABLES = new Consunables[OBJECT_MAX];
		this.MATERAIALS = new Material[OBJECT_MAX];
		this.ENEMYS = new Enemy[OBJECT_MAX];
		this.SKILLS = new Skill[OBJECT_MAX];
	}
	
	public ObjectScan getInstance(String path){
		if(objectscan == null) objectscan = new ObjectScan(path);
		return objectscan;
	}
	
	/*
	
	//将字符转换成base64
	private String StringtoBase64(String str){
		return new String(Base64.encodeBase64(str.getBytes()));
	}
	
	String err="";
	private JSONObject readJson(String filename){
		JSONObject obj = null;
		FileInputStream fin = null;
		try
		{
			//obj = claz.newInstance();
			fin = new FileInputStream(filename);
			byte[] buf = new byte[fin.available()];
			fin.read(buf);
			obj = new JSONObject(new String(buf));
			fin.close();
		}
		catch (FileNotFoundException e)
		{}
		catch (JSONException e)
		{}
		catch (IOException e)
		{}
		
		return obj;
	}
	
	private Object readObject(JSONObject jsonobj, int kind)
	{
		Object obj = null;
		try
		{
			switch (kind)
			{
				case Item.KIND_EQUIP:
					Weapon wp = new Weapon();
					wp.applylv = jsonobj.getInt("applylv");
					wp.attrauto();
					wp.name = Base64ToString(jsonobj.getString("name"));
					wp.explain = Base64ToString(jsonobj.getString("explain"));
					wp.buy_price = jsonobj.getInt("buy");
					wp.price = jsonobj.get("price");
					wp.atk += jsonobj.getInt("atk");
					wp.def += jsonobj.getInt("def");
					wp.health += jsonobj.getInt("health");
					wp.mp += jsonobj.getInt("mp");
					wp.speed += jsonobj.getInt("speed");
					wp.luck += jsonobj.getInt("luck");
					wp.prog = Base64ToString(jsonobj.getString("prog"));
					obj = wp;
					break;
				case Item.KIND_CONSUNABLES:
					Consunables con = new Consunables();
					con.applylv = jsonobj.getInt("applylv");
					con.name = Base64ToString(jsonobj.getString("name"));
					con.explain = Base64ToString(jsonobj.getString("explain"));
					con.buy_price = jsonobj.getInt("buy");
					con.price = jsonobj.get("price");
					con.prog = Base64ToString(jsonobj.getString("prog"));
					obj = con;
					break;
				case Item.KIND_MATERIAL:
					Material mat = new Material();
					mat.name = Base64ToString(jsonobj.getString("name"));
					mat.explain = Base64ToString(jsonobj.getString("explain"));
					mat.buy_price = jsonobj.getInt("buy");
					mat.price = jsonobj.get("price");
					obj = mat;
					break;
				case Item.KIND_SKILL:
					Skill sk = new Skill();
					sk.name = Base64ToString(jsonobj.getString("name"));
					sk.explain = Base64ToString(jsonobj.getString("explain"));
					sk.prog = Base64ToString(jsonobj.getString("prog"));
					obj = sk;
					break;
				case Item.KIND_ENEMY:
					Enemy ene = new Enemy();
					ene.name = Base64ToString(jsonobj.getString("name"));
					//ene.explain = Base64ToString(jsonobj.getString("explain"));
					ene.IncareaseLv(jsonobj.getInt("level"));
					ene.atk += jsonobj.getInt("atk");
					ene.def += jsonobj.getInt("def");
					ene.health += jsonobj.getInt("health");
					ene.mp += jsonobj.getInt("mp");
					ene.speed += jsonobj.getInt("speed");
					ene.prog = Base64ToString(jsonobj.getString("prog"));
					obj = ene;
					break;
			} }
		catch (JSONException e)
		{
			return null;
		}

		return obj;
	}
	
	private String Base64ToString(String str){
		return new String(Base64.decodeBase64(str.getBytes()));
	}
	
	
	public Weapon ScanEquip(String name){
		for(Weapon wp : EQUIPS)
		if(wp != null && name.equals(wp.name))
			return wp;
			
		String filename = path+"/equip/"+name+".json";
		
		JSONObject obj = readJson(filename);
		
		Weapon wp = (Weapon)readObject(obj, Item.KIND_EQUIP);
		if(wp != null)
		{
			EQUIPS[eq_index] = wp;
			
			eq_index++;
			
			if(eq_index > OBJECT_MAX-1) eq_index = 0;
		}
		return wp;
	}
	

	public Consunables ScanConsunables(String name){
		for(Consunables con : CONSUNABLES)
			if(con != null && name.equals(con.name))
				return con;

		String filename = path+"/consunables/"+name+".json";

		Consunables con = (Consunables)readObject(readJson(filename), Item.KIND_CONSUNABLES);

		if(con != null)
		{
			CONSUNABLES[con_index] = con;

			con_index++;

			if(con_index > OBJECT_MAX-1) con_index = 0;
		}
		return con;
	}

	public Material ScanMaterial(String name){
		for(Material mat : MATERAIALS)
		if(mat != null && name.equals(mat.name))
			return mat;
		
		String filename = path+"/material/"+name+".json";
		
		JSONObject obj = readJson(filename);
		
		Material mat = (Material)readObject(obj, Item.KIND_MATERIAL);

		//(Material)readJs(filename, Material.class);
		
		if(mat != null)
		{
			MATERAIALS[mt_index] = mat;
			
			mt_index++;
			
			if(mt_index > OBJECT_MAX-1) mt_index = 0;
		}
		
		return mat;
	}

	public Enemy ScanEnemy(String name){
		for(Enemy e : ENEMYS)
		if(e != null && name.equals(e.name))
			return e;
		
		String filename = path+"/enemy/"+name+".json";
		
		Enemy ene = (Enemy)readObject(readJson(filename), Item.KIND_ENEMY);
		
		if(ene != null)
		{
			ENEMYS[ene_index] = ene;
			
			ene_index++;
			
			if(ene_index > OBJECT_MAX-1) ene_index = 0;
		}
		
		return ene;
	}

	public Skill ScanSkill(String name){
		for(Skill sk : SKILLS)
			if(sk != null && name.equals(sk.name))
				return sk;

		String filename = path+"/skill/"+StringtoBase64(name);

		Skill sk = (Skill)readObject(readJson(filename), Item.KIND_SKILL);

		if(sk != null)
		{
			SKILLS[sk_index] = sk;

			sk_index++;

			if(sk_index > OBJECT_MAX-1) sk_index = 0;
		}

		return sk;
	}
	*/
	
}
