package com.xiuxian.chen.set;

public class Skill extends Item implements Cloneable
{
	//技能类型
	public transient static final int
	PASSIVE=0, //被动技能
	ACTIVE=1, //主动技能
	//主动特殊技能，消耗物品，指定数量
	ACTIVE_SPECIAL_SPECIFIED_NUMBER=2;
	
	//消耗类型
	public transient static final int
	USE_MONEY = 0,
	USE_CONSUNABLES=1,
	USE_MATERIAL=2;
	
	public int type;
	//消耗物品类型
	public int consume_type;

    //技能属性伤害类型
    public int hurt_type;

	//攻击目标数量
	public int attack_num;
	
	public int use_health;//使用所消耗的生命值
	
	public int use_mp;//使用所消耗的灵力
	
	public String use_name;
	
	//增加的生命值
	public int health;
	//增加的灵力值
	public int mp;
	//增加的攻击力
	public int atk;
	//增加的防御力
	public int def;
	//增加的速度
	public int speed;
	//增加的运气
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

	//技能冷却
	public int cd;
	
	public int now_cd;
	
	public transient SkillAttackAction satk;
	
	public transient SkillPassiveAction spa;
	
	public transient Space space;
	
	public int use_num;
	
	private transient PlayerAll pall;
	
	public Skill(){
		this.use_num=1;
	}
	
	public Skill(String name, int type){
		this.name=name;
		this.type=type;
		this.use_num=0;
	}

	public void setNowCd(int now_cd)
	{
		this.now_cd = now_cd;
	}

	public int getNowCd()
	{
		return now_cd;
	}

	public void setCd(int cd)
	{
		this.cd = (cd == 0 ? 0 : cd+1);
	}

	public int getCd()
	{
		return cd;
	}
	
	//指定消耗物品数量，没有返回-1
	public int SpeciFiedNumber(){
		int num=0;
		switch(consume_type){
			default: break;
			case Skill.USE_MONEY:
				if(space.getMoney()>0)
					return space.getMoney();
				break;
			case Skill.USE_CONSUNABLES:
				num=space.getConsunablesNumber(use_name);
				if(num>0)return num;
				break;
			case Skill.USE_MATERIAL:
				num=space.getMaterialNumber(use_name);
				if(num>0)return num;
				break;
		}
		return -1;
	}
	
	public boolean isMp(Player play){
		return play.base.now_mp >= use_mp*play.level;
	}
	
	public boolean isHp(Player play){
		return play.base.now_health >= use_health*play.level;
	}
	
	public void setAttack(SkillAttackAction s){
		this.satk=s;
	}
	
	public void setPassive(SkillPassiveAction s){
		this.spa=s;
	}
	
	public void setPall(PlayerAll pall){
		this.pall = pall;
	}
	
	public PlayerAll getpall(){
		return this.pall;
	}

	@Override
	protected Skill clone() throws CloneNotSupportedException
	{
		return (Skill)super.clone();
	}
	
	public Skill newObject()
	{
		Skill sk = null;
		try
		{
			sk = clone();
			
			sk.satk = this.satk;
			
			sk.spa = this.spa;

            sk.explain = this.explain;
		}
		catch (CloneNotSupportedException e)
		{}

		return sk;
	}
	
}
