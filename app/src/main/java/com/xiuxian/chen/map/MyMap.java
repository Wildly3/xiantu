package com.xiuxian.chen.map;
import android.graphics.*;
import android.os.Bundle;

import com.xiuxian.chen.set.*;
import java.io.*;
import java.util.*;
import com.xiuxian.chen.*;
import android.util.*;
import com.xiuxian.chen.include.*;

public final class MyMap
{
	//所有数据最大数量
	private static final int NUM_MAX = 100;
	
	//public int[][] map;
	public String name;
	public String expainl;
	public int width;
	public int height;
	public int id;
	
	public MapItem[] item;
	
	public List<String> enes;
	
	//遇敌几率
	public float encounter;

	//该地图是否有敌人
	public boolean isenemy;
	
	//固定遇敌
	public List<MapItem> now_enemy;
	
	//该地图可以接受的任务
	public List<String> tacks;
	
	//public Map<String, Integer> data;
	
	//程序脚本文件名
	public String eventfile;
	
	//重生地图
	public int afresh_map;
	
	//地图事件
	//public MapEventListener mel;
	
	public MyMap(int id){
		this.id=id;
		
		enes=new ArrayList<>();
		
		now_enemy=new ArrayList<>();
		
		tacks = new ArrayList<>();
		
		this.eventfile = id+".js";
	}
	
	/*
	public void setEventListener(MapEventListener mel){
		this.mel=mel;
	}*/
	
	//初始化
	public void EventInit(){
		if(eventfile.equals(""))return;
		EventRun.init(eventfile);
	}
	
	public void EventFinish(){
		EventRun.exit();
	}
	
	public boolean Event_Select(MapItem item, int x, int y){
		if(eventfile.equals(""))return true;
		return EventRun.Select(item, x, y);
	}
	
	public void Event_MoveAfter(int x, int y){
		if(eventfile.equals(""))return;
		EventRun.MoveAfter(x, y);
	}
	
	public void Event_MoveBfore(int x, int y){
		if(eventfile.equals(""))return;
		EventRun.MoveBfore(x, y);
		//EventRun.Event_onLoad();
	}
	
	public void Event_onLoad(){
		if(eventfile.equals(""))return;
		EventRun.onLoad();
	}
	
	public void Event_onDestory(){
		if(eventfile.equals(""))return;
		
		EventRun.onDestory();
	}
	
	public void Event_Fightend(int flag){
		if(eventfile.equals(""))return;
		EventRun.Fightend(flag);
	}

	public void setEventThread(){

    }
	
	public void setItem(MapItem[] item){
		this.item = item;
	}

    /**
     * 设置地图item
     * @param item
     */
    public void setItem(List<MapItem> item){
        if(item == null || item.size() < 1) return;
        this.item = item.toArray(new MapItem[item.size()]);
    }

    public void addItem(MapItem item)
    {
        if(this.item == null){
			item.id = 1;
            this.item = new MapItem[]{item};
            return;
        }
        int length = this.item.length;
        MapItem[] is = new MapItem[length+1];
        System.arraycopy(this.item, 0, is, 0, length);
        is[length] = item;
		item.id = is.length;
        this.item = is;
    }
	
    /**
     * 在后面追加一段
     * @param items 数组
     */
    public void AppendItems(MapItem[] items){
        if(this.item == null)
            this.item = items;
		int num=this.item.length+items.length;
		MapItem[] result=new MapItem[num];
		int cont=0;
		for(int i=0;i<this.item.length;i++)
		result[i]=this.item[cont=i];
		cont++;
		for(int i=0;i<items.length;i++){
			result[cont]=items[i];
			cont++;
		}
		this.item=result;
	}
	
	//添加一个任务
	public void addTask(String name){
		this.tacks.add(name);
	}
	
	public List<String> getTasks(){
		return this.tacks;
	}
	
    /**
     * 设置地图敌人列表
     * @param e 敌人列表list
     */
	 /*
    public void setEnemy(List<Enemy> e){
		this.enes=e;
		this.isenemy=true;
	}*/

    /**
     * 在地图添加敌人
     * @param e 敌人
     */
    public void addEnemy(String e){
		if(e == null) return ;
		this.enes.add(e);
		this.isenemy = true;
	}
	
	public static boolean odds(float o){
		int a=(new Random(System.currentTimeMillis()).nextInt(1000));
		if(a<(o*1000))return true;
		return false;
	}
	
	//获取指定xy所在的Item
	public MapItem getNowPositionMapItem(int x, int y){
		for(MapItem mi : item)
		if(mi.pos_x==x&mi.pos_y==y)
			return mi;
		
		return null;
	}
	
	//获取当前action位置的item
	public MapItem getNowPositionMapItem(Action act){
		for(MapItem mi : item)
			if(mi.pos_x==act.pos_x&mi.pos_y==act.pos_y)
				return mi;

		return null;
	}
	
	/*
	public void put(String key, int value)
    {
        this.data.put(key, value);
    }

    public int get(String key, int mdefault)
    {
        Integer value = this.data.get(key);
        if(value == null)value = mdefault;
        return value;
    }
	*/

	//地图物品
	public static class Goods
	{
		public String name;
		public int kind;
		public int quality;
		public int quantity;
		
		public Goods(String name, int type){
			this(name, type, 0, 1);
		}
		
		public Goods(String name, int type, int quality){
			this(name, type, quality, 1);
		}
		
		/*
		@name 名称
		@type 类型，Item.KIND_XXX
		@quality 品质，Item.QUALITY_XXX
		@quantity 数量
		*/
		public Goods(String name, int type, int quality, int quantity){
			this.name=name;
			this.kind=type;
			this.quality=quality;
			this.quantity=quantity;
		}
		
	}
	
	//生成x1,x2开始到x2,y2终止的item
	public static MapItem[] CreateBuilds(MapItem mi, int x1, int y1,int x2, int y2){
		List<MapItem> result=new ArrayList<>();
		while(true){
			MapItem mitem=new MapItem(x1, y1, MapItem.BUILD);
			mitem.name=mi.name;
			mitem.word=mi.word;
			mitem.color=mi.color;
			result.add(mitem);
			if(x1>=x2&y1>=y2)break;
			if(x1<x2)x1++;
			if(y1<y2)y1++;
		}
		
		MapItem[] ms=new MapItem[result.size()];
		for(int i=0;i<result.size();i++)
		ms[i]=result.get(i);
		
		return ms;
	}
	
	//地图项目
	public static class MapItem implements Serializable
	{
		//***    标识   ***
		public static final int
		NPC_ORDINARY=0,//普通人物
		NPC_SHOP_SELL=1,//商店
		NPC_SHOP_BUY=2,//出售
		EXIT=3,//出口
		GOODS=4,//物品
		BUILD=5,//建筑
		REST=6,//休息
		REPAIR=7,//装备修理
		ENEMY=8;//固定遇敌
		
		public String name;
		
		public int id;
		
		//添加的敌人
		public List<String> names;
		public String word;
		//连续对话
		public List<RoleDialog.Content> words;
		
		//当前坐标
		public int pos_x;
		public int pos_y;
		//标识
		public int flag;
		//下一个地图id
		public int next_map;
		//下一个地图坐标
		public int next_map_pos_x;
		public int next_map_pos_y;
		//颜色
		public int color;
		//表示该物品是否被查看过，true表示查看过
		//public boolean istigger;
		
		public List<Goods> goods;
		//范围坐标
		public int[] tig_x, tig_y;
		
		/*
		 @name 名称
		 @x 坐标x
		 @y 坐标y
		 @f 标识
		 */
		public MapItem(String name, int x, int y, int f){
			this.name=name;
			this.pos_x=x;
			this.pos_y=y;
			this.flag=f;
			//this.istigger=false;
			switch(f){
				case NPC_ORDINARY:
				case NPC_SHOP_BUY:
				case NPC_SHOP_SELL:
					this.color=Color.rgb(255,215,0);
					break;
				case EXIT:
					this.color=Color.rgb(120,120,120);
					break;
				case REST:
					this.color=Color.rgb(34,139,34);
					break;
			}
		}
		
		public MapItem(int x, int y, int f){
			this("", x, y, f);
		}
		
		public MapItem(String name, int f){
			this(name, 0, 0, f);
		}
		
		public MapItem(int f){
			this("", 0, 0, f);
		}

		@Override
		protected MapItem clone() throws CloneNotSupportedException
		{
			return (MapItem)super.clone();
		}
		
		//复制一个item
		public MapItem NewItem(int x, int y){
			MapItem item = new MapItem(this.name, x, y, this.flag);
			item.word = this.word;
			item.color = this.color;
			return item;
		}
		
		//项目标识为ENEMY时添加所战斗的敌人
		public void addEnemy(String name){
			if(names==null)names=new ArrayList<>();
			names.add(name);
		}
		
		public void addSay(String title, String str){
			if(words == null)words = new ArrayList<>();
			this.words.add(RoleDialog.Content.toContent(title, str));
		}
		
		//生成一个以xy为中心的矩形坐标
		public void CreateRectTigger(int x, int y){
			tig_x=new int[4];
			tig_y=new int[4];
			tig_x[0]=x;
			tig_y[0]=y<=0 ? 0 : y-1;
			tig_x[1]=x;
			tig_y[1]=y+1;
			tig_x[2]=x<=0 ? 0 : x-1;
			tig_y[2]=y;
			tig_x[3]=x+1;
			tig_y[3]=y;
		}
		
		//生成一个xy点的坐标
		public void CreatePointTigger(int x, int y){
			tig_x=new int[1];
			tig_y=new int[1];
			tig_x[0]=x;
			tig_y[0]=y;
			pos_x=x;
			pos_y=y;
		}
		
		//设置下一个地图的坐标
		public void setNextMapPosition(int x, int y){
			next_map_pos_x=x;
			next_map_pos_y=y;
		}
		
		//添加物品
		public void addGoods(Goods g){
			if(this.goods==null)goods=new ArrayList<>();
			goods.add(g);
		}
		
	}
	
}
