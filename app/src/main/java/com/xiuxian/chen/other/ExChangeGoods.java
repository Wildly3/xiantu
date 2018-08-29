package com.xiuxian.chen.other;
import com.xiuxian.chen.set.*;
import com.xiuxian.chen.*;
import com.xiuxian.chen.view.*;

public class ExChangeGoods
{
	static final String NULL = "空";
	
	public static final int
	EXP=3,//经验
	MONEY=4,//金币
	SPACE_MAX=5,//增加空间
	SKILL=6;//获得技能
	
	public int kind;
	public String name;
	public int quantity;
	public int quality;
	public int exp;
	public int money;
	public int space_max;
	
	public ExChangeGoods(int type){
		this(null, type, 0, 1);
	}
	
	public ExChangeGoods(String name, int type){
		this(name, type, 0, 1);
	}
	
	//一般初始化装备
	public ExChangeGoods(String name, int type, int quality){
		this(name, type, quality, 1);
	}
	
	public ExChangeGoods(String name, int type, int quality, int quantity){
		this.name=name;
		this.kind=type;
		this.quality=quality;
		this.quantity=quantity;
	}
	
	public String ExChange(){
		switch(kind){
			case Item.KIND_EQUIP:
			Weapon wp=BaseActivity.ScanWeapon(name);
			if(wp==null)return NULL;
			wp = wp.NewObject();
			wp.quality=quality;
			wp.MaxDura();
			BaseActivity.space.addWeapon(wp);
			return "获得 "+wp.name;
			
			case Item.KIND_CONSUNABLES:
			Consunables con=BaseActivity.ScanConsunables(name);
			if(con==null)return NULL;
			con.quantity=quantity;
			BaseActivity.space.addConsunables(con);
				return "获得 "+con.name+" "+con.quantity+"个";
				
			case Item.KIND_MATERIAL:
			Material mat=BaseActivity.ScanMaterial(name);
			if(mat == null)return NULL;
			mat.quantity=quantity;
			BaseActivity.space.addMaterail(mat);
				return "获得 "+mat.name+" "+mat.quantity+"个";
			case EXP:
			BaseActivity.base.exp+=exp;
			BaseActivity.base.UpdateExp();
				return "获得经验 "+exp;
			case MONEY:
			BaseActivity.space.addMoney(money);
				return "获得金币 "+money;
			case SPACE_MAX:
			BaseActivity.space.space_max+=space_max;
			return "空间扩大 "+space_max;
			
			case SKILL:
				boolean is = BaseActivity.base.addSkill(name);
				return is ? "获得技能 "+name : "没有该技能！";
			
			default: return "未知！";
		}
	}
	
	
	
}
