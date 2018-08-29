package com.xiuxian.chen.set;
import com.xiuxian.chen.view.*;
import java.util.*;
import org.mozilla.javascript.*;

public abstract class JRunnable
{
	private Context cx;
	
	private Scriptable scope;
	
	public JRunnable(String source, PlayerAll pall, Space sp){
//		cx = Context.enter();
//		
//		cx.setOptimizationLevel(-1);
//		
//		cx.setLanguageVersion(Context.VERSION_1_2);
//		
//		scope = cx.initStandardObjects();
//		
//		this.onCreate(pall, sp);
//		
//		cx.evaluateString(scope, LoadResources.JAVASCRIPT_BASE, LoadResources.TAG, 1, null);
//		
//		cx.evaluateString(scope, LoadResources.JAVASCRIPT_FUNCTION, LoadResources.TAG, 1, null);
//		
//		cx.evaluateString(scope, source, LoadResources.TAG, 1, null);
	}
	
//	public void exit(){
//		this.exit();
//	}
//	
//	public void onLoad(Enemy ene){
//		scope.put("main_ene", scope, ene);
//		cx.evaluateString(scope, "onload();", LoadResources.TAG, 1, null);
//	}
//	
//	public void onCreate(PlayerAll pall, Space space){
//		scope.put("main_playerpall", scope, pall);
//		scope.put("game", scope, pall.getEvent());
//		scope.put("main_space", scope, space);
//	}
//	
//	public void onstartfight(Player holder, List<Player> enes)
//	{
//		scope.put("onstartfight_holder", scope, holder);
//		
//		scope.put("onstartfight_enes", scope, enes);
//		
//		cx.evaluateString(scope, "onstartfight(onstartfight_holder, onstartfight_enes);", LoadResources.TAG, 1, null);
//	}
//
//	
//	public int[] fightend(int[] exp_m)
//	{
//		scope.put("fightend_exp_m", scope, exp_m);
//		return (int[])cx.evaluateString(scope, "fightend(fightend_exp_m);", LoadResources.TAG, 1, null);
//	}
//
//	
//	public int[] death(int[] exp_m)
//	{
//		scope.put("death_exp_m", scope, exp_m);
//		return (int[])cx.evaluateString(scope, "death(death_exp_m);", LoadResources.TAG, 1, null);
//	}
//
//	
//	public int beattack(int atk, Player holder, Player enemy)
//	{
//		scope.put("main_atk", scope, atk);
//		scope.put("main_holder", scope, holder);
//		scope.put("main_enemy", scope, enemy);
//		
//		return cx.evaluateString(scope, "beattack();", LoadResources.TAG, 1, null);
//	}
//
//	
//	public int attack(int atk, Player holder, Player enemy)
//	{
//		scope.put("main_atk", scope, atk);
//		scope.put("main_holder", scope, holder);
//		scope.put("main_enemy", scope, enemy);
//		
//		return cx.evaluateString(scope, "attack();", LoadResources.TAG, 1, null);
//	}
//	/*
//	public int attack(int atk, Skill k, Player holder, Player enemy)
//	{
//		scope.put("main_atk", scope, atk);
//		scope.put("main_holder", scope, holder);
//		scope.put("main_enemy", scope, enemy);
//		scope.put("main_skill", scope, k);
//		
//		return cx.evaluateString(scope, "skattack();", LoadResources.TAG, 1, null);
//	}*/
//	
//	public int beingattacked(int atk, Skill k, Player holder, Player enemy){
//		scope.put("main_atk", scope, atk);
//		scope.put("main_holder", scope, holder);
//		scope.put("main_enemy", scope, enemy);
//		scope.put("main_skill", scope, k);
//		
//		return cx.evaluateString(scope, "beskillattack();", LoadResources.TAG, 1, null);
//	}
//	
//	//攻击前判断是否符合攻击条件
//	public boolean Judge(Skill k, Player holder, Player enemy){
//		scope.put("main_holder", scope, holder);
//		scope.put("main_enemy", scope, enemy);
//		scope.put("main_skill", scope, k);
//		
//		return cx.evaluateString(scope, "judge();", LoadResources.TAG, 1, null);
//	}
//
//	//攻击时
//	public int attack(Skill k, Player holder, Player enemy){
//		scope.put("main_holder", scope, holder);
//		scope.put("main_enemy", scope, enemy);
//		scope.put("main_skill", scope, k);
//
//		return cx.evaluateString(scope, "skillattack();", LoadResources.TAG, 1, null);
//	}
//	
//	public boolean use(Player holder, Player target){
//		scope.put("main_holder", scope, holder);
//		scope.put("main_enemy", scope, target);
//		
//		return cx.evaluateString(scope, "use();", LoadResources.TAG, 1, null); 
//	}
	
	
}
