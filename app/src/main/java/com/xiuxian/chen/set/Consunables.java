package com.xiuxian.chen.set;

//消耗品
public class Consunables extends Item implements Cloneable
{
	//消耗品使用途径。。。
	public transient static final int
	ENTIRE=0,//全部可用
	FIGHT=1,//战斗时可用
	REST=2,//非战斗可用
	FIGHT_ATK=3,//战斗时可用，用于攻击
	FIGHT_BUFF=4;//战斗时可用，获取buf
	
	public int road;
	
	public transient ConsunablesUse conuse;
	
	//使用方式
	public int usetype;
	
	public Consunables(){
		super.kind=KIND_CONSUNABLES;
	}
	
	public void setUse(ConsunablesUse use){
		this.conuse=use;
	}
	
	//增加生命值
	public static boolean addHealth(Player p, int x){
		if((p.base.max_health-p.base.now_health)<=0)return false;
		if((p.base.max_health-p.base.now_health)<x){
			x=p.base.max_health-p.base.now_health;
		}
		p.base.now_health+=x;
		return true;
	}
	
	//增加灵力值
	public static boolean addMp(Player p, int x){
		if((p.base.max_mp-p.base.now_mp)<=0)return false;
		if((p.base.max_mp-p.base.now_mp)<x){
			x=p.base.max_mp-p.base.now_mp;
		}
		p.base.now_mp+=x;

		return true;
	}
	
	public static boolean reduceHealth(Player p, int x){
		if(x>=0)return false;
		p.base.now_health+=x;
		return true;
	}

    @Override
    protected Consunables clone() throws CloneNotSupportedException {
        return (Consunables) super.clone();
    }

    public Consunables NewObject(){
		Consunables con = null;
        try {
            con = this.clone();
            con.conuse = this.conuse;
            con.explain = this.explain;
        } catch (CloneNotSupportedException e) {
        }
//		wp.name=name;
//		wp.explain=explain;
//		wp.applylv=applylv;
//		wp.buy_price=buy_price;
//		wp.price=price;
//		wp.dura=dura;
//		wp.duramax=duramax;
//		wp.quality=quality;
//		wp.quantity=quantity;
//		wp.kind=kind;
//		wp.conuse=conuse;
//		wp.road=road;
		return con;
	}
	
}
