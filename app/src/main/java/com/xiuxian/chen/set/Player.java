package com.xiuxian.chen.set;

import com.xiuxian.chen.view.*;
import java.util.*;
import java.util.concurrent.*;

public class Player extends Item implements Cloneable
{
    //无属性
    public transient static final int NULL = 0;

    //金
    public transient static final int METAL = 1;

    //木
    public transient static final int WOOD = 2;

    //水
    public transient static final int WATER = 3;

    //火
    public transient static final int FIRE = 4;

    //土
    public transient static final int SOIL = 5;

    //人族
    public transient static final int RACE_PEOPLE = 0;

    //妖族
    public transient static final int RACE_BEAST = 1;

    //魔族
    public transient static final int RACE_MONSTER = 2;

    //鬼族
    public transient static final int RACE_GHOST = 3;

    //种族属性成长
    public transient static final float[][] APTITUDE_RACE = {
            {3.0f, 1.0f, 1.3f, 0.2f, 0.7f},
            {3.5f, 1.5f, 1.0f, 0.2f, 0.4f},
            {2.5f, 1.0f, 1.8f, 0.1f, 0.5f},
            { 2.7f, 2.2f, 0.9f, 0.1f, 1.0f}
    };

	//等级
	public int level;
	//当前经验值
	public int exp;
	//升级需要的经验值
	public int max_exp;
	//生命值
	public int health;

	public int now_health;
	//灵力
	public int mp;

	public int now_mp;
	//攻击力
	public int atk;
	//防御
	public int def;
	//速度
	public int speed;
	//运气
	public int luck;

    //种族
    public int race;

    //灵根
    public int[] aptitude;

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

	//属性点
	public int attrs;
	//已经穿戴的装备
	public Weapon[] equipment;
	//已装备技能
	public Skill[] skillment;
	//已学习的技能
	public List<Skill> skillments;
	//属性数值
	public Buffer base;
	//效果列
	public transient List<Buffer> buffer;
	
	public int enemy;
	//是否重置属性数值
	public boolean isrebuff;
	
	private transient PlayerAll all;
	
	//经验成长倍率
	private transient static final float[][] ExpUp={
		{0,3,1.0f},
		{4,8,2.8f},
		{9,13,3.5f},
		{14,18,4.0f},
		{19,23,5.5f},
		{24,28,6.85f},
		{29,30,8.7f},
		{31,35,10.0f},
		{36,41,12.0f},
		{42,47,13.8f},
		{48,49,17.5f},
		{50,53,19.6f},
		{54,57,22.2f},
		{58,59,33.5f},
		{60,63,35.8f},
		{64,67,38.8f},
		{68,69,55.8f},
            {70,74,60.0f},
            {75,79,66.5f}
	};

	//基础升级所需经验值
	private transient static final int basic_exp = 100;
	
	public Player(){
		this.equipment=new Weapon[7];
		
		this.skillment=new Skill[4];

        this.aptitude = new int[5];
		
		this.buffer=new ArrayList<>();
		
		this.skillments=new ArrayList<>();
		
		this.level = 1;
		
		this.exp=0;
		
		this.max_exp = basic_exp;
		
		this.name = "";
	}

	//初始化属性，会根据种族
	public void Initattr(){
        health = getfi(APTITUDE_RACE[race][0]);
        mp = getfi(APTITUDE_RACE[race][1]);
        atk = getfi(APTITUDE_RACE[race][2]);
        def = getfi(APTITUDE_RACE[race][3]);
        speed = getfi(APTITUDE_RACE[race][4]);
        luck = 0;
    }

	public void setAll(PlayerAll all)
	{
		this.all = all;
	}

	public PlayerAll getAll()
	{
		return all;
	}
	
//	public void levelup(){
//		for(int i=0;i<ExpUp.length;i++)
//			if(isnum(level, (int)ExpUp[i][0], (int)ExpUp[i][1]))
//			{
//				int m=(int)(((float)this.basic_exp)*ExpUp[i][2]);
//				this.max_exp+=m;
//			}
////			this.exp=0;
////			this.attrs+=5;
////			this.health+=30;
////			this.mp+=20;
////			this.atk+=8;
////			this.def+=1;
////			this.speed+=5;
//
//
//
//		level++;
//	}

	public void LevelUp(){
        for(int i=0;i<ExpUp.length;i++)
            if(isnum(level, (int)ExpUp[i][0], (int)ExpUp[i][1]))
            {
                int m=(int)(((float)this.basic_exp)*ExpUp[i][2]);
                this.max_exp+=m;
            }

        attrUp(APTITUDE_RACE[this.race]);

        AptitudeUp(1);

        level++;
    }

    protected void AptitudeUp(int lv){
        int metal_up = aptitude[0] * lv;

        int wood_up = aptitude[1] * lv;

        int water_up = aptitude[2] * lv;

        int fire_up = aptitude[3] * lv;

        int soil_up = aptitude[4] * lv;

        this.metal += metal_up;
        this.wood += wood_up;
        this.water += water_up;
        this.fire += fire_up;
        this.soil += soil_up;

        this.metal_resist += wood_up;
        this.wood_resist += soil_up;
        this.water_resist += fire_up;
        this.fire_resist += metal_up;
        this.soil_resist += water_up;
    }

    private void attrUp(float[] ap){
        this.health+=getfi(ap[0]);
        this.mp+=getfi(ap[1]);
        this.atk+=getfi(ap[2]);
        this.def+=getfi(ap[3]);
        this.speed+=getfi(ap[4]);
    }

    protected int getfi(float a){
        return (int) (a * 10);
    }

	public boolean UpdateExp(){
		if(exp >= max_exp){
			int sur = exp - max_exp;
			sur = sur<1 ? 0 : sur;
			LevelUp();
			exp = sur;
			if(exp >= max_exp)UpdateExp();
			return true;
		}
		
		return false;
	}
	
	private boolean isnum(int n, int a, int b){
		return n>=a&n<=b;
	}

	//获取当前生命值
	public int getnowhealth(){
		return this.base.now_health;
	}
	
	public int getmaxhealth(){
		return this.base.max_health;
	}
	
	public Buffer NewBuffer(){
		Buffer b = Real();

		if(!isrebuff & base != null){
			b.now_health = base.now_health;
			b.now_mp = base.now_mp;
		}
		b.now_health = (b.now_health>b.max_health ? b.max_health : b.now_health);
		b.now_mp = (b.now_mp>b.max_mp ? b.max_mp : b.now_mp);
		
		return this.base = b;
	}
	
	public void addBuffer(Buffer buf){
		try{
		for(Buffer b : buffer){
			if(b.name!=null && b.name.equals(buf.name) & b.getEffect()){
				b.setRounds(buf.getRounds());
				return ;
			}	
		}
		}catch(ConcurrentModificationException e){
//			addBuffer(buf);
			return;
		}
		switch(buf.type){
			default: break;
			case Buffer.PROMOTE:
			case Buffer.REDUCE:
			buf.onStart(this);
			break;
		}
		buffer.add(buf);
	}
	
	public void CleafBuffer(){
		this.buffer.clear();
	}

	//获取真实属性（包括装备、技能的加成）
	public Buffer Real(){
		Buffer buf = new Buffer();
		buf.health = buf.max_health = buf.now_health = health;
		buf.mp = buf.max_mp = buf.now_mp = mp;
		buf.atk = atk;
		buf.def = def;
		buf.speed = speed;
		buf.luck = luck;
        buf.metal = metal;
        buf.wood = wood;
        buf.water = water;
        buf.fire = fire;
        buf.soil = soil;
        buf.metal_resist = metal_resist;
        buf.wood_resist = wood_resist;
        buf.water_resist = water_resist;
        buf.fire_resist = fire_resist;
        buf.soil_resist = soil_resist;

		buf.enemy = enemy;
		
		for(int i=0;i<7;i++){
			Weapon wp=equipment[i];
			if(wp!=null&&wp.dura>0){
				buf.max_health = buf.health+=wp.gethealth();
				buf.max_mp = buf.mp+=wp.getmp();
				buf.atk+=wp.getatk();
				buf.def+=wp.getdef();
				buf.speed+=wp.getspeed();
				buf.luck+=wp.getluck();

                buf.metal+=wp.getmetal();
                buf.wood+=wp.getwood();
                buf.water+=wp.getwater();
                buf.fire+=wp.getfire();
                buf.soil+=wp.getsoil();

                buf.metal_resist+=wp.getmetal_resist();
                buf.wood_resist+=wp.getwood_resist();
                buf.water_resist+=wp.getwater_resist();
                buf.fire_resist+=wp.getfire_resist();
                buf.soil_resist+=wp.getsoil_resist();
			}
			
		}
		
		for(int i=0;i<4;i++){
			Skill sk = skillment[i];
			if(sk!=null){
				buf.max_health=buf.health+=sk.health;
				buf.max_mp=buf.mp+=sk.mp;
				buf.atk+=sk.atk;
				buf.def+=sk.def;
				buf.speed+=sk.def;
				buf.luck+=sk.luck;

                buf.metal+=sk.metal;
                buf.wood+=sk.wood;
                buf.water+=sk.water;
                buf.fire+=sk.fire;
                buf.soil+=sk.soil;

                buf.metal_resist+=sk.metal_resist;
                buf.wood_resist+=sk.wood_resist;
                buf.water_resist+=sk.water_resist;
                buf.fire_resist+=sk.fire_resist;
                buf.soil_resist+=sk.soil_resist;
			}
		}
		
		return buf;
	}
	
	public boolean addSkill(String name){
		Skill sk=BaseActivity.ScanSkill(name).newObject();
		if(sk==null)return false;
		for(int i=0;i<4;i++){
			if(skillment[i]!=null)
			if(skillment[i].name.equals(name))return false;
		}
		for(Skill msk : skillments){
			if(msk.name.equals(name))return false;
		}
		skillments.add(sk);
		
		return true;
	}
	
	
}
