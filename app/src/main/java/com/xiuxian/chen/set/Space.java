package com.xiuxian.chen.set;

import java.util.*;
import android.util.*;
import com.xiuxian.chen.data.*;

public class Space
{
	public int space_max;
	
	private int money;
	
	private transient DataBackup mb;
	
	public List<Weapon> weapons;
	
	public List<Consunables> consunables;
	
	public List<Material> materials;
	
	public Space(){
		this.weapons=new ArrayList<Weapon>();
		this.consunables=new ArrayList<Consunables>();
		this.materials=new ArrayList<Material>();
		//this.skills=new ArrayList<Skill>();
		mb = new DataBackup();
	}
	
	public int getMoney(){
		if(!mb.equals(this.money)){
			if(mb.isSet()){
				mb.set(this.money = 0);
				return 0;
			}
			
			return this.money = mb.get();
		}
		
		return this.money;
	}
	
	public void addMoney(int money){
		this.money += money;
		mb.set(this.money);
	}

	//重置备份
	public void reMoney(){
        mb.set(this.money);
    }
	
	//添加一个装备
	public boolean addWeapon(Weapon wp){
		if(wp==null)return false;
		if(getNumbers()<space_max)
		weapons.add(wp);
		else return false;
		return true;
	}
	
	//获取装备位置(首个出现)
	public int FindFirstWeapon(String name){
		for(int i=0;i<weapons.size();i++)
		{
			Weapon wp = weapons.get(i);
			if(name.equals(wp.name)) return i;
		}
		return -1;
	}
	
	//添加一个消耗品
	public boolean addConsunables(Consunables cb){
		if(cb==null)return false;
		if(getSurplusNumber()<1)return false;
		if(consunables.size()>0)
		{
			for(int i=0;i<consunables.size();i++){
				Consunables c=consunables.get(i);
				if(c.name.equals(cb.name))
				{if(getSurplusNumber()>=cb.quantity)
					c.quantity+=cb.quantity;
					else c.quantity+=getSurplusNumber();
					consunables.set(i, c);
					return false;
				} }
				
			consunables.add(cb);
		}else{
			if(getSurplusNumber()>=cb.quantity)
			consunables.add(cb);
			else{
				cb.quantity=getSurplusNumber();
				consunables.add(cb);}
			}
		
		return true;
	}
	
	//添加一个材料
	public boolean addMaterail(Material m){
		if(m==null)return false;
		if(getSurplusNumber()<1)return false;
		if(materials.size()>0)
		{
			for(int i=0;i<materials.size();i++){
				Material c=materials.get(i);
				if(c.name.equals(m.name))
				{if(getSurplusNumber()>=m.quantity)
					c.quantity+=m.quantity;else
					c.quantity+=getSurplusNumber();
					//Log.i("有该物品下",getSurplusNumber()+"---"+c.quantity);
					materials.set(i, c);
					return false;
				} }

			materials.add(m);
		}else{
			//Log.i("没有该物品下",getSurplusNumber()+"---"+m.quantity);
			if(getSurplusNumber()>=m.quantity)
			materials.add(m);
			else{
				
				m.quantity = getSurplusNumber();
				
				materials.add(m);
				}
			
			}
		return true;
	}
	
	//取出装备
	public Weapon takeout_weapon(int i){
		if(i < 0 || i > weapons.size()-1)
			return null;
		Weapon wp = weapons.get(i);
		weapons.remove(i);
		return wp;
	}
	
	//取出装备
	public boolean takeout_weapon(List<Position> pos){
		if(pos == null || pos.size() < 1)
			return false;
		
		List<Weapon> ws = new ArrayList<>(pos.size());
		
		for(Position p : pos)
			ws.add(weapons.get(p.index));
		
		return weapons.removeAll(ws);
	}
	
	//取出消耗品
	public boolean takeout_consunables(int i, int n){
		if(i < 0 || i > consunables.size()-1)
			return false;
		
		final Consunables cb = consunables.get(i);
		
		if(n > cb.quantity)return false;
		if(n == cb.quantity){
			consunables.remove(i);
			return true;
			}
		cb.quantity-=n;
		
		return true;
	}
	
	//批量取出
	public boolean takeout_consunables(List<Position> pos){
		if(pos == null || pos.size() < 1)
			return false;
		
		final List<Consunables> cons = new ArrayList<>(pos.size());
		
		for(Position p : pos)
			cons.add(consunables.get(p.index));
			
		int i = 0;
			
		for(Consunables con : cons){
			final int n = pos.get(i).quantity;
			
			if(n > con.quantity)return false;
			
			if(n == con.quantity){
				consunables.remove(con);
			}else con.quantity -= n;
			
			i++;
		}
		
		return true;
	}
	
	//取出材料
	public boolean takeout_material(int i, int n){
		if(i < 0 || i > materials.size()-1)
			return false;
		final Material mt = materials.get(i);
		
		if(n > mt.quantity)return false;
		if(n == mt.quantity){
			materials.remove(i);
			return true;
			}
			
		mt.quantity-=n;
		
		return true;
	}
	
	//批量取出材料
	public boolean takeout_material(List<Position> pos){
		if(pos == null || pos.size() < 1)
			return false;

		final List<Material> mats = new ArrayList<>(pos.size());

		for(Position p : pos)
			mats.add(materials.get(p.index));

		int i = 0;

		for(Material mat : mats){
			final int n = pos.get(i).quantity;

			if(n > mat.quantity)return false;

			if(n == mat.quantity){
				materials.remove(mat);
			}else mat.quantity -= n;

			i++;
		}

		return true;
	}
	
	//根据名称取出物品(不分类)
	public boolean TakeoutAll(String name, int num){
		int info[] = getGoodInfoMation(name);
		if(info[0] == Item.KIND_NULL) return false;
		switch(info[0]){
			case Item.KIND_EQUIP:
			if(takeout_weapon(FindFirstWeapon(name)) == null) return false; else return true;
			case Item.KIND_CONSUNABLES:
			return takeout_consunables(FindConsunables(name), num);
			case Item.KIND_MATERIAL:
			return takeout_material(FindMaterial(name), num);
		}
		return false;
	}
	
	//根据名称获取物品类型
	public int[] getGoodInfoMation(String name){
		int wpn = getEquipAllNumber(name);
		int conn = getConsunablesNumber(name);
		int man = getMaterialNumber(name);
		if(wpn > 0) return new int[]{Item.KIND_EQUIP, wpn};
		else
		if(conn > 0) return new int[]{Item.KIND_CONSUNABLES, conn};
		else
		if(man > 0) return new int[]{Item.KIND_MATERIAL, man};
		
		return new int[]{Item.KIND_NULL, 0};
	}
	
	//获取装备数量
	public int getEquipAllNumber(String name){
		int num = 0;
		for(Weapon wp : weapons)
		if(name.equals(wp.name)) num++;
		
		return num;
	}
	
	//查找材料索引
	public int FindMaterial(String name){
		int i=0;
		for(Material m : materials){
			if(m.name.equals(name))
				return i;
			i++;
		}
		return -1;
	}
	
	//获取材料数量
	public int getMaterialNumber(String name){
		int i=FindMaterial(name);
		
		if(i==-1)return -1;
		
		return materials.get(i).quantity;
	}
	
	//查找消耗品索引
	public int FindConsunables(String name){
		int i=0;
		for(Consunables c : consunables){
			if(c.name.equals(name))
				return i;
			i++;
		}
		return -1;
	}
	
	//获取消耗品个数
	public int getConsunablesNumber(String name){
		int i=FindConsunables(name);
		
		if(i==-1)return -1;
		
		return consunables.get(i).quantity;
	}
	
	//获取空间内物品总数量
	public int getNumbers(){
		int wps=weapons.size();
		int cb=0;
		int mt=0;
		if(consunables.size()>0)
			for(Consunables c : consunables)
			cb+=c.quantity;
		
		if(materials.size()>0)
			for(Material m : materials)
			mt+=m.quantity;
		return wps+cb+mt;
	}
	
	//获取空间剩余数量
	public int getSurplusNumber(){
		return space_max-getNumbers();
	}
	
	//位置
	public static class Position
	{
		public int index;
		
		public int quantity;
	}
	
}
