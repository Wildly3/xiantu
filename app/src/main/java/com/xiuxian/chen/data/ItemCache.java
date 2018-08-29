package com.xiuxian.chen.data;

import com.xiuxian.chen.set.*;
import com.xiuxian.chen.view.*;
import java.io.*;

public final class ItemCache
{
	//最大缓存数量
	public static final int OBJECT_MAX = 30;

	private static ItemCache cache = null;

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

	private ItemCache(String path){
		this.path = path;
		this.EQUIPS = new Weapon[OBJECT_MAX];
		this.CONSUNABLES = new Consunables[OBJECT_MAX];
		this.MATERAIALS = new Material[OBJECT_MAX];
		this.ENEMYS = new Enemy[OBJECT_MAX];
		this.SKILLS = new Skill[OBJECT_MAX];
	}

	public static ItemCache getInstance(String path){
		synchronized(ItemCache.class){
			if(cache == null) cache = new ItemCache(path);
		}
		return cache;
	}

	String err="";

	private Object readJs(String filename, Class<?> claz){
		Object obj=null;
		org.mozilla.javascript.Context cx=org.mozilla.javascript.Context.enter();
		cx.setOptimizationLevel(-1);
		cx.setLanguageVersion(org.mozilla.javascript.Context.VERSION_1_2);
		try{
			FileInputStream fin=new FileInputStream(filename);
			byte[] buf=new byte[fin.available()];
			fin.read(buf);
			final String js=new String(buf);

			fin.close();
			org.mozilla.javascript.Scriptable scope=cx.initStandardObjects();
			obj=claz.newInstance();
			scope.put("jobject", scope, obj);
			cx.evaluateString(scope, MyILoadResource.JAVASCRIPT_BASE, "xiuxian", 1, null);
			cx.evaluateString(scope, js, "xiuxian", 1, null);

			//mi=(MapItem)cx.jsToJava(scope.get("obj", scope), MapItem.class);
		}catch(Exception e){
			//Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
			err+=filename+"\n"+e.getMessage()+"\n";
			return null;
		}finally{
			cx.exit();
		}
		return obj;
	}
	
	public Weapon ScanEquip(String name){
		for(Weapon wp : EQUIPS)
			if(wp != null && name.equals(wp.name))
				return wp;

		String filename = path+"/equip/"+name+".js";

		Weapon wp = (Weapon)readJs(filename, Weapon.class);
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

		String filename = path+"/consunables/"+name+".js";

		Consunables con = (Consunables)readJs(filename, Consunables.class);

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

		String filename = path+"/material/"+name+".js";

		//JSONObject obj = readJson(filename);

		Material mat = (Material)readJs(filename, Material.class);

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

		String filename = path+"/enemy/"+name+".js";

		Enemy ene = (Enemy)readJs(filename, Enemy.class);

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

		String filename = path+"/skill/"+name+".js";

		Skill sk = (Skill)readJs(filename, Skill.class);

		if(sk != null)
		{
			SKILLS[sk_index] = sk;

			sk_index++;

			if(sk_index > OBJECT_MAX-1) sk_index = 0;
		}

		return sk;
	}

//获取缓存数量
	public int getCacheQuantity(){
		int q = 0;
		
		for(Weapon wp : EQUIPS)
		if(wp != null) q++;
		
		for(Consunables con : CONSUNABLES)
		if(con != null) q++;
		
		for(Material mat : MATERAIALS)
			if(mat != null) q++;

		for(Enemy ene : ENEMYS)
			if(ene != null) q++;
			
		for(Skill sk : SKILLS)
			if(sk != null) q++;
			
		return q;
	}
	
}
