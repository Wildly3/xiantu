package com.xiuxian.chen.set;

import java.util.*;

public interface EventImplements
{
	
	public void onCreate(PlayerAll pall, Space space);

	public void onstartfight(Player holder, List<Player> enes);


	public int[] fightend(int[] exp_m);


	public int[] death(int[] exp_m);


	public int beattack(int atk, Player holder, Player enemy);


	public int attack(int atk, Player holder, Player enemy);

	public int attack(int atk, Skill k, Player holder, Player enemy);

	public int beingattacked(int atk, Skill k, Player holder, Player enemy);

	//攻击前判断是否符合攻击条件
	public boolean Judge(Skill k, Player holder, Player enemy);

	//攻击时
	public int attack(Skill k, Player holder, Player enemy);

	public boolean use(Player holder, Player target);
	
	
}
