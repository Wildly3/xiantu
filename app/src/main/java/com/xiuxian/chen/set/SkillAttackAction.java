package com.xiuxian.chen.set;

public interface SkillAttackAction
{
	//攻击前判断是否符合攻击条件
	public boolean Judge(Skill k, Player holder, Player enemy);
	
	//攻击时
	public int attack(Skill k, Player holder, Player enemy);
}
