package com.xiuxian.chen.map;
import com.xiuxian.chen.*;
import com.xiuxian.chen.data.*;
import com.xiuxian.chen.view.*;
import java.io.*;
import java.util.*;

public final class MapManager
{
	//地图缓存栈最大数
	private static final int MAPSTACK_MAX = 5;
	
	private MyMap now_map;
	
	private java.util.Map<Integer, String> map_message;
	
	//地图缓存栈
	private MyMap[] mapstack;
	
	private int map_index;
	
	public MapManager(){
		//this.maps=new ArrayList<>();
		mapstack = new MyMap[MAPSTACK_MAX];
		map_message = new HashMap<>();
		map_index = 0;
		
//		File[] files = listFile(MyILoadResource.MAP_PATH);
//		for(File f : files){
//			MyMap map = readMap(f.getAbsolutePath());
//			if(map!=null)
//				map_message.put(map.id, map.expainl);
//		}
	}

	//预加载地图
	public boolean CacheMap(int id){
        MyMap map = ScanMap(id);
        if(map != null)
            return addStackMap(map);
        else return false;
    }

    //检测地图是否存在
    public boolean isMap(int id){
        if(ScanStackMap(id) != null) {
            return true;
        }else {
            MyMap map = ScanMap(id);
            if (map == null)
                return false;
            else
                addStackMap(map);
        }

        return true;
    }

	//添加地图到缓存
	private boolean addStackMap(MyMap map){
		if(ScanStackMap(map.id) != null) return false;
		if(map_index >= MAPSTACK_MAX) map_index = 0;
		mapstack[map_index] = map;
		map_index ++;
        return true;
	}
	
	//扫描缓存地图
	private MyMap ScanStackMap(int id){
		if(mapstack == null) return null;
		for(MyMap map : mapstack)
		if(map != null && id == map.id) return map;
		
		return null;
	}
	
	/*
	public void addMap(MyMap map){
		this.maps.add(map);
	}
	
	public List<MyMap> getMaps(){
		return this.maps;
	}
	*/
	
	//刷新map固定enemy块
	private void RefreshEnemy(MyMap map){
		map.now_enemy.clear();
		for(MyMap.MapItem mi : map.item)
			if(mi.flag == MyMap.MapItem.ENEMY)
				map.now_enemy.add(mi);
	}
	
	//读取地图
	private String err="";
	private MyMap readMap(String filename){
        MyMap map = null;

        List<MyMap.MapItem> item = new ArrayList<>();

        org.mozilla.javascript.Context cx=org.mozilla.javascript.Context.enter();

        cx.setOptimizationLevel(-1);

        cx.setLanguageVersion(org.mozilla.javascript.Context.VERSION_1_2);

        try{
            org.mozilla.javascript.Scriptable scope=cx.initStandardObjects();

            scope.put("map", scope, map);

            scope.put("game", scope, MainActivity.event);

            scope.put("list", scope, item);

			cx.evaluateString(scope, MyILoadResource.JAVASCRIPT_BASE, "xiuxian", 1, null);

            cx.evaluateReader(scope, new FileReader(filename), "xiuxian", 1, null);

            map = (MyMap)cx.jsToJava(cx.evaluateString(scope, "onCreate();", "xiuxian", 1, null), MyMap.class);
        }catch(Exception e){
            err += filename+"\n"+e.getMessage()+"\n";
            return null;
        }finally{
            if (cx != null)
                    cx.exit();
        }
        return map;
    }
	
	//扫描指定id的，地图
	private MyMap ScanMap(int id){
		return readMap(MyILoadResource.MAP_PATH + id + ".js");
	}
	
	private File[] listFile(String path){
		return new File(path)
			.listFiles(new FileFilter(){
				@Override
				public boolean accept(File f)
				{
					if(f.isFile()&f.getName().endsWith(".js"))
						return true;
					return false;
				}
			});
	}
	
	//加载地图
	public MyMap LoadMap(int id)
	{
		if(this.now_map != null && this.now_map.id == id)
			return this.now_map;
			
		if (this.now_map != null)
		{
			now_map.Event_onDestory();

			now_map.EventFinish();
		}
		
		MyMap map = ScanStackMap(id);
		
		if(map != null)
		{
			map.EventInit();
			
			map.Event_onLoad();
			
			return this.now_map = map;
		}
		
		map = ScanMap(id);
		
		if (map == null)
		{
			ERR(err);
			
			err = "";
			
			return null;
		}

		RefreshEnemy(this.now_map = map);
		
		if (this.now_map != null)
		{
			this.now_map.EventInit();
			
			this.now_map.Event_onLoad();
		}
		
		this.addStackMap(now_map);
		
		return now_map;
	}
	
	public MyMap ExitLoad(MyMap.MapItem item, Action act){
		MyMap map = this.LoadMap(item.next_map);
        if(map != null) act.map_id = item.next_map;
			
		return map;
	}
	
	/*
	public MyMap ExitLoad(MyMap.MapItem item){
		return this.LoadMap(item.next_map);
	}*/
	
	//获取下一个地图的信息
    @Deprecated
	public String getNextMapExpainl(MyMap.MapItem i){
		/*
		for(MyMap map : maps)
			if(map.id == i.next_map)
				return map.expainl;
			*/
//		return map_message.get(i.next_map);
        return "";
	}
	
	//获取当前地图
	public MyMap getNowMap(){
		return now_map;
	}
	
	//获取action所在地图
	public MyMap getActionNowMap(Action act){
		return this.LoadMap(act.map_id);
	}
	
	//获取缓存数量
	public int getCacheQuantity(){
		int q = 0;
		
		for(MyMap map : mapstack)
		if(map != null) q++;
		
		return q;
	}

	public String getError(){
        return err;
    }

	void ERR(String err){
		MainActivity.event.Alert("地图加载异常", err, 1);
	}
	
}
