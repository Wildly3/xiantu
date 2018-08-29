package com.xiuxian.chen.set;

import java.util.*;

public interface SkillPassiveAction
{
	public int attack(int atk, Skill k, Player holder, Player enemy, String atktype);
	public int beingattacked(int atk, Skill k, Player holder, Player enemy, String atktype);
	public void onstartfight(Player holder, List<Player> enes);
	public int[] fightend(int[] exp_m);
	public int[] death(int[] exp_m);
}
