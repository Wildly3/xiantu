package com.xiuxian.chen.set;
import android.text.*;
import android.util.*;
import android.graphics.*;
import java.util.concurrent.*;

public class Buffer
{
	public transient static final int
	POISONING=0,//中毒
	PROMOTE=1,//增益效果
	REDUCE=2,//减益效果
	CONT_PROMOTE_HEALTH=3,//持续回血
	BECONTROLLED=4;//控制效果
	//BECONTROLLED
	//当前名称
	public transient String name;

    //减益效果伤害属性类型
    public transient int hurt_type;

	public transient int type = -1;
	//攻击次数
	public transient int attack_num;
	
	//回合数和持续回合
	public transient int rounds;
	
	public int health;
	
	public int now_health;
	
	public int max_health;
	
	public int mp;
	
	public int now_mp;
	
	public int max_mp;
	
	//护盾
	private transient int shield;
	
	private transient int max_shield;
	
	//当前护盾标志
	private transient String shieldflag;
	
	public int atk;
	//防御
	public int def;
	//速度
	public int speed;
	//运气
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
	
	public transient int index;
	
	public transient int enemy;
	
	public transient int color;
	
	//public Weapon[] equipment;
	//发起效果对象
	public transient Player base;
	//被施加效果对象
	public transient Player target;
	//是否有效果
	public transient boolean effect;
	
	public Buffer(){
		this.color = -1;
		this.effect = true;
	}
	
	public void onStart(Player play){
		
		switch(type){
			default: break;
			case PROMOTE:
				play.base.atk+=atk;
				play.base.def+=def;
				play.base.speed+=speed;
				play.base.attack_num+=attack_num;
				break;
			case REDUCE:
				play.base.atk-=atk;
				play.base.def-=def;
				play.base.speed-=speed;
				play.base.attack_num-=attack_num;
				break;
		}
		
	}
	
	public void setRounds(int round){
		this.setEffect(true);
		this.rounds = round;
	}
	
	public int getRounds(){
		return this.rounds;
	}
	
	public void setEffect(boolean effect){
		this.effect = effect;
	}
	
	public boolean getEffect(){
		return this.effect;
	}
	
	//设置当前护盾标识
	public void setShieldFlag(String shieldflag)
	{
		this.shieldflag = shieldflag;
	}

	public String getShieldFlag()
	{
		return shieldflag;
	}
	
	//设置护盾
	public void setShield(int shield)
	{
		this.shield = shield;
		this.max_shield = shield;
		this.max_shield = this.max_shield < 100 ? 100 : this.max_shield;
	}
	
	public void setShield(int shield, String flag)
	{
		setShieldFlag(flag);
		setShield(shield);
	}
	
	//添加护盾
	public int addShield(int shield)
	{
		//Log.i("伤害", ""+shield);
		
		if(shield == 0) return 0;
		if(shield < 0){
			this.shield+=shield;
			if(this.shield < 0){
				int surplus = Math.abs(this.shield);
				//Log.i("剩余", ""+surplus);
				//System.exit(0);
				this.shield = 0;
				return surplus;
			}else return 0;
		}
		else{
			if(this.shield+shield <= this.max_shield)
				this.shield+=shield;
			else
			{
				int surplus = (this.shield+shield) - this.max_shield;
				setShield(this.max_shield+surplus);
			}
			
			return 0;
		}
	}
	
	public int addShield(int shield, String flag)
	{
		setShieldFlag(flag);
		return addShield(shield);
	}
	
	//获取护盾
	public int getShield()
	{
		return shield;
	}
	
	public int getShieldMax()
	{
		return max_shield;
	}
	
	//减少当前血量
	public int SubtractHealth(int h){
		if(shield < 1)
			setShield(0);
		if(shield == 0){
			now_health = (now_health - h < 0 ? 0 : now_health - h);
			return h;
		}
		else
		{
			int surplus = addShield(-h);
			now_health = (now_health - surplus < 0 ? 0 : now_health - surplus);
			return surplus;
		}
	}
	
	public void onDestory(Player play){
		switch(type){
			default: break;
			case PROMOTE:
				play.base.atk-=atk;
				play.base.def-=def;
				play.base.speed-=speed;
				play.base.attack_num-=attack_num;
				break;
			case REDUCE:
				play.base.atk+=atk;
				play.base.def+=def;
				play.base.speed+=speed;
				play.base.attack_num+=attack_num;
				break;
		}
	}
	
	public int BeAttacked(int atk){
		
		return atk;
	}
	
	public int Attack(int atk){
		
		return atk;
	}
	
	public void Rounds(PlayerAll pall)
	{
		if(!this.effect)return;
		if (rounds > 0)
		{
			switch (type)
			{
				default: break;
				case POISONING:
					pall.buff_attack(target, this, atk);
					break;
				case CONT_PROMOTE_HEALTH:
					base.base.now_health += health;
					if (base.base.now_health > base.base.max_health)base.base.now_health = base.base.max_health;
					pall.SendMessage(base.name + "恢复[" + health + "]" + "点生命", Color.GREEN);
					break;
			}
				rounds--;
		}
	}
	
	//设置发起效果者buf
	public void setBaseBuffer(Player buf){
		this.base=buf;
	}
	
	//设置生效对像buf
	public void setTargetBuffer(Player buf){
		this.target=buf;
	}
	
	//获取生效目标名称
	public String getTargetName(){
		return this.target.name;
	}
	
}
