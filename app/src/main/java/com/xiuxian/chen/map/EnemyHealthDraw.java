package com.xiuxian.chen.map;
import com.xiuxian.chen.set.*;

public class EnemyHealthDraw extends HealthDraw implements PlayerAll.AttackAnimationListener
{
	public EnemyHealthDraw(Player p){
		super(p);
	}
	
	@Override
	public void anim(int index)
	{
		super.left();
	}
}
