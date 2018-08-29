package com.xiuxian.chen.set;

import java.io.*;
import com.xiuxian.chen.include.*;
import android.util.*;
import java.util.*;

public class Weapon extends Item implements Cloneable
{
	public transient static final int
	HEAD=1,//头部
	NECKLACE=3,//项链
	CLOTHES=4,//衣服
	GLOVES=5,//手套
	SHOES=6,//鞋
	WEAPON=0,//武器
	EARRINGS=2;//耳环
	
	//装备等阶
	public transient static final int
	GREEN=0,//绿色
	VIOLET=1,//紫色
	YELLOW=2,//黄色
	RED=3;//红色

	//装备类型
	public int type;

	public int atk;

	public int health;

	public int mp;

	public int def;

	public int speed;

	public int luck;

    /***
     * 以下为属性
     */

    //金
    public int metal;

    //木
    public int wood;

    //水
    public int water;

    //火
    public int fire;

    //土
    public int soil;

    /*
    以下为属性抗性
     */

    //金
    public int metal_resist;

    //木
    public int wood_resist;

    //水
    public int water_resist;

    //火
    public int fire_resist;

    //土
    public int soil_resist;

	public transient BeBuffer buffer;

	public transient String explain2="";
	
	public void Change(){
		int m = 0;
		m+=applylv*5;
		m+=atk*20;
		m+=health/2;
		m+=mp;
		m+=def*22;
		m+=speed*30;
		m+=luck*15;
		super.buy_price = m;
		super.price = m/2;
	}
	
    public Weapon NewObject(){
        Weapon wp = null;
        try {
            wp = this.clone();
            wp.buffer = this.buffer;
            wp.explain2 = this.explain2;
        } catch (CloneNotSupportedException e) {
            return null;
        }
        return wp;
    }

    @Override
    protected Weapon clone() throws CloneNotSupportedException {
        return (Weapon) super.clone();
    }

    public int getatk(){
		return getQuality(atk);
	}
	
	public int gethealth(){
		return getQuality(health);
	}
	
	public int getmp(){
		return getQuality(mp);
	}
	
	public int getdef(){
		return getQuality(def);
	}
	
	public int getspeed(){
		return getQuality(speed);
	}

    public int getluck(){
        return getQuality(luck);
    }

	public int getmetal(){
        return getQuality(metal);
	}

	public int getwood(){
        return getQuality(wood);
    }

    public int getwater(){
        return getQuality(water);
    }

    public int getfire(){
        return getQuality(fire);
    }

    public int getsoil(){
        return getQuality(soil);
    }



    public int getmetal_resist(){
        return getQuality(metal_resist);
    }

    public int getwood_resist(){
        return getQuality(wood_resist);
    }

    public int getwater_resist(){
        return getQuality(water_resist);
    }

    public int getfire_resist(){
        return getQuality(fire_resist);
    }

    public int getsoil_resist(){
        return getQuality(soil_resist);
    }

	public int getDuraMax(){
		return getQuality(duramax);
	}
	
	public Weapon MaxDura(){
		super.dura = getDuraMax();
		return this;
	}
	
	public void attrauto(){
		this.duramax=35+(kind*10);
		MaxDura();
		switch(type){
			case WEAPON:
			//health=(applylv)+(applylv*p);
			atk+=(applylv*2)+((applylv*kind)/2);
			break;
			case HEAD:
			def+=(applylv/2)+((applylv*kind)/2);
			break;
			case CLOTHES:
			health+=(applylv*3)+(applylv*kind);
			def+=(applylv/3)+((applylv*kind)/4);
			break;
			case GLOVES:
			atk+=(applylv/2)+((applylv*kind)/4);
			def+=(applylv/3)+((applylv*kind)/5);
			break;
			case SHOES:
			speed+=(applylv*2)+((applylv*kind)/2);
			def+=(applylv/5)+((applylv*kind)/10);
			break;
		}
		this.Change();
	}
	
	
	public Weapon(){
		super.kind=KIND_EQUIP;
	}
	
	@Override
	public String getExplain(){
		StringBuilder sb=new StringBuilder("LV <applylv>\n品质 quality\n耐久 <dura>/<duramax>");
		if(health!=0)sb.append("\n生命 <health>");
		if(mp!=0)sb.append("\n灵力 <mp>");
		if(atk!=0)sb.append("\n攻击 <atk>");
		if(def!=0)sb.append("\n防御 <def>");
		if(speed!=0)sb.append("\n速度 <speed>");
		if(luck!=0)sb.append("\n气运 <luck>");

        if (metal!=0)sb.append("\n金属性 <metal>");
        if (wood!=0)sb.append("\n木属性 <wood>");
        if (water!=0)sb.append("\n水属性 <water>");
        if (fire!=0)sb.append("\n火属性 <fire>");
        if (soil!=0)sb.append("\n土属性 <soil>");

        if (metal_resist!=0)sb.append("\n金属性抗性 <metal_resist>");
        if (wood_resist!=0)sb.append("\n木属性抗性 <wood_resist>");
        if (water_resist!=0)sb.append("\n水属性抗性 <water_resist>");
        if (fire_resist!=0)sb.append("\n火属性抗性 <fire_resist>");
        if (soil_resist!=0)sb.append("\n土属性抗性 <soil_resist>");
		
		ExplainCustom ec=new ExplainCustom(getClass(), this, sb.toString());
		ExplainCustom.Handle h=new ExplainCustom.Handle(){
			@Override
			public Object handle(Object obj, String s)
			{
				int num=(int)obj;
				if(!s.equals("applylv"))
					if(!s.equals("dura"))
				num=getQuality((int)obj);
				return num;
			}
		};
		ec.setHandle(h);
		String el=ec.getString();
		String qua="";
		switch(super.quality){
			case QUALITY_INFELIOR:
			qua="下品";
			break;
			case QUALITY_MEDIUM:
				qua="中品";
				break;
			case QUALITY_FIRST_CLASS:
				qua="上品";
				break;
			case QUALITY_NONSUCH:
				qua="极品";
				break;
		}
		el=el.replace("quality",qua);
		return el+"\n"+explain2;
	}
	
	public void setBeBuff(BeBuffer buf){
		this.buffer=buf;
	}
	
	public static interface BeBuffer{
		public int beattack(int atk, Player holder, Player enemy, PlayerAll pall, String atktype);
		public int attack(int atk, Player holder, Player enemy, PlayerAll pall, String atktype);
		public void onstartfight(Player holder, List<Player> enes);
		public int[] fightend(int[] exp_m);
		public int[] death(int[] exp_m);
		
	}
	
}
