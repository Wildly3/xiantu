package com.xiuxian.chen.set;

import com.xiuxian.chen.view.*;
import java.util.*;

public class GameTask
{
	//任务需求类型
	public transient static final int
	ENEMY = 0,//击杀敌人
	GOOD = 1,//物品需求
	MONEY = 2;//金币需求
	
	private List<TaskInfoMation> Ekns;
	
	private transient Space space;
	
	//来自于谁的任务，名称
	public String from_name;
	
	//任务名称
	public String name;
	
	//任务介绍
	public String explain;

	//接受任务所需等级
	public int applylv;
	
	//任务获得经验
	public int exp;
	
	//任务获得金币
	public int money;
	
	//任务获得物品
	private List<Item> goods;
	
	public GameTask(){
		Ekns = new ArrayList<>();
		goods = new ArrayList<>();
	}
	
	public GameTask(String name){
		this();
		this.name=name;
	}
	
	public void setSpace(Space sp){
		this.space = sp;
	}
	
	public void addGoods(Item goods){
		this.goods.add(goods);
	}
	
	public List<Item> getGoods(){
		return this.goods;
	}
	
	//添加一个任务
	public void addTask(String name, int enough, int type){
		//EnemyKillingNumber ekn = getTask(name);
		//if(ekn==null)return ;
		if(!isEmptyTask(name))
		this.Ekns.add(NewTask(name, enough, type));
	}
	
	//更新击杀任务信息
	public void UpdateKillTask(String name, int num){
		if(!isEmptyTask(name))return ;
		getTask(name).number+=num;
	}
	
	
	public void setKillTask(String name, int num){
		if(!isEmptyTask(name))return ;
		getTask(name).number=num;
	}
	
	//获取一个任务信息
	private TaskInfoMation getTask(String name){
		for(TaskInfoMation ekn : this.Ekns)
		if(name.equals(ekn.name)) return ekn;
		
		return null;
	}
	
	//新建一个任务信息
	private TaskInfoMation NewTask(String name, int enough, int type){
		TaskInfoMation ekn = new TaskInfoMation();
		ekn.name = name;
		ekn.enough = enough;
		ekn.type = type;
		return ekn;
	}
	
	//判断一个任务信息是否存在
	private boolean isEmptyTask(String name){
		for(TaskInfoMation ekn : Ekns)
		if(name.equals(ekn.name))return true;
		return false;
	}
	
	//判断任务是否完成
	public boolean isComplate(){
		for(TaskInfoMation tm : Ekns){
			if(!tm.isComplate())return false;
		}
		
		return true;
	}
	
	//获取任务信息
	public List<TaskInfoMation> getTaskInfoMation(){
		return Ekns; 
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder("");
		
		for(TaskInfoMation ekn : Ekns)
		{
			sb.append("name:"+ekn.name+", enough:"+ekn.enough+", number:"+ekn.number);
			sb.append("\n");
		}
		
		return sb.toString();
	}
	
	//任务信息
	public static class TaskInfoMation
	{
		//名称
		public String name;
		//类型
		public int type;
		//击杀数量条件
		public int enough;
		//现有击杀数
		public int number;
		
		public TaskInfoMation(){
		}
		
		//获取完成数量
		public int getNumber(){
			switch(this.type){
				case ENEMY:
					return this.number;

				case GOOD:
					return BaseActivity.space.getGoodInfoMation(this.name)[1];

				case MONEY:
					return BaseActivity.space.getMoney();
				
				default: return 0;
			}
		}
		
		//判断是否完成
		public boolean isComplate(){
			switch(this.type){
				case ENEMY:
				if(number >= enough) return true;
				break;
				
				case GOOD:
					int[] info = BaseActivity.space.getGoodInfoMation(name);
				if(info[0]==Item.KIND_NULL) return false;
				if(info[1] >= enough) return true;
				break;
				
				case MONEY:
					if(BaseActivity.space.getMoney() >= enough) return true;
				break;
				
			}
			return false;
		}
		
	}
	
}
