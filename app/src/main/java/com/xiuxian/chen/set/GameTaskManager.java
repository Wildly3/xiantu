package com.xiuxian.chen.set;
import com.xiuxian.chen.view.*;
import java.util.*;

//任务管理器
public class GameTaskManager
{
	private List<GameTask> task;

	//已完成的任务
	private List<String> CompletedTaskName;

	public GameTaskManager()
	{
		task = new ArrayList<>();
		
		CompletedTaskName = new ArrayList<>();
	}

	//添加一个任务
	public boolean addTask(GameTask task)
	{
		if (isEmpty(task.name)) return false;
		task.setSpace(BaseActivity.space);
		this.task.add(task);
		return true;
	}

	//更新所有任务击杀信息
	public void UpdateKillTask(String name, int num)
	{
		for (GameTask gt : task)
			gt.UpdateKillTask(name, num);
	}

	//判断一个任务是否存在
	public boolean isEmpty(String name)
	{
		for (GameTask gt : task)
			if (name.equals(gt.name)) return true;

		return false;
	}

	//判断一个任务是否已完成
	public boolean isEmptyCompleted(String name)
	{
		for (String str : this.CompletedTaskName)
			if (str.equals(name)) return true;

		return false;
	}

	//判断一个任务是否存在或者已完成
	public boolean isEmptyAll(String name)
	{
		if (isEmpty(name) || isEmptyCompleted(name)) return true;

		return false;
	}

	public List<GameTask> getTasks()
	{
		return this.task;
	}

	//添加一个完成的任务
	public void addCompleteTask(String name)
	{
		this.CompletedTaskName.add(name);
	}

	//完成一个任务
	public String CompletedTask(String name)
	{
		if (!isEmpty(name)) return null;
		for (GameTask gt : task)
			if (name.equals(gt.name))
			{
				String result = CompTask(gt);
				addCompleteTask(gt.name);
				task.remove(gt);
				return result;
			}
		return null;
	}

	//完成任务
	private String CompTask(GameTask gt)
	{
		StringBuilder sb = new StringBuilder("");
		int exp = gt.exp;
		int money = gt.money;
		List<Item> item = gt.getGoods();
		
		Player play = BaseActivity.base;

		Space sp = BaseActivity.space;
		
		for(GameTask.TaskInfoMation tm : gt.getTaskInfoMation()){
			switch(tm.type)
			{
				default: break;
				case GameTask.MONEY:
					sp.addMoney(-tm.enough);
				break;
				case GameTask.GOOD:
				sp.TakeoutAll(tm.name, tm.enough);
				break;
			}
		}
		
		play.exp += exp;
		if (exp > 0) sb.append("获得经验： " + exp);

		boolean is_lvup = play.UpdateExp();
		if (is_lvup) sb.append("\n等级提升 LV." + play.level);

		sp.addMoney(money);
		if (money > 0) sb.append("\n获得金币： " + money);

		for (Item i : item)
		{
			boolean isget = false;
			switch (i.kind)
			{
				case Item.KIND_EQUIP:
					Weapon wp = BaseActivity.ScanWeapon(i.name);
					if (wp == null) break;
					wp = wp.NewObject();
					wp.quality = i.quality;
					wp.MaxDura();
					sp.addWeapon(wp);
					isget = true;
					break;
				case Item.KIND_CONSUNABLES:
					Consunables con = BaseActivity.ScanConsunables(i.name);
					if (con == null) break;
					con = con.NewObject();
					con.quantity = i.quantity;
					sp.addConsunables(con);
					isget = true;
					break;
				case Item.KIND_MATERIAL:
					Material mat = BaseActivity.ScanMaterial(i.name);
					if (mat == null) break;
					mat = mat.NewObject();
					mat.quantity = i.quantity;
					sp.addMaterail(mat);
					isget = true;
					break;
			}
			if (isget)
				sb.append("\n获得： " + i.name);
		}
		return sb.toString();
	}

	//返回已完成的任务名单
	public List<String> getCompletedTaskNames()
	{
		return this.CompletedTaskName;
	}

}
