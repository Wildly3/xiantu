package com.xiuxian.chen.map;

import com.xiuxian.chen.*;
import com.xiuxian.chen.data.*;
import com.xiuxian.chen.view.*;
import java.io.*;

public class EventRun
{
	private static org.mozilla.javascript.Context cx;

	private static org.mozilla.javascript.Scriptable scope;
	
	public static void init(String filename){
		//Log.i(LoadResources.TAG, filename);
		//System.exit(0);
		cx = org.mozilla.javascript.Context.enter();
		cx.setOptimizationLevel(-1);
		cx.setLanguageVersion(org.mozilla.javascript.Context.VERSION_1_2);
		scope = cx.initStandardObjects();
		try
		{
			scope.put("game", scope, MainActivity.event);
			
			cx.evaluateString(scope, MyILoadResource.JAVASCRIPT_BASE + MyILoadResource.JAVASCRIPT_FUNCTION, "xiuxian", 1, null);
			
			cx.evaluateReader(scope, new FileReader(MyILoadResource.MAP_PATH+filename), MyILoadResource.TAG, 1, null);

			//Log.i("地图脚本路径", LoadResources.MAP_PATH+eventfile);
		}
		catch (IOException e)
		{}
	}
	
	public static void exit(){
		cx.exit();
		scope = null;
	}
	
	//调用一个函数
	public static void GO(String function){
		cx.evaluateString(scope, function + "();", MyILoadResource.TAG, 1, null);
	}
	
	public static boolean Select(MyMap.MapItem Item, int x, int y){
		//if(eventfile.equals(""))return;
		scope.put("game_pos_x_1", scope, x);
		scope.put("game_pos_y_1", scope, y);
		scope.put("game_mapitem_1", scope, Item);
		return (boolean) cx.evaluateString(scope, "Select(game_mapitem_1, game_pos_x_1, game_pos_y_1);", MyILoadResource.TAG, 1, null);
	}

	public static void MoveAfter(int x, int y){
		//if(eventfile.equals(""))return;
		scope.put("game_pos_x_1", scope, x);
		scope.put("game_pos_y_1", scope, y);
		cx.evaluateString(scope, "Moveafter(game_pos_x_1, game_pos_y_1);", MyILoadResource.TAG, 1, null);
	}

	public static void MoveBfore(int x, int y){
		//if(eventfile.equals(""))return;
		//System.exit(0);
		scope.put("game_pos_x_1", scope, x);
		scope.put("game_pos_y_1", scope, y);
		cx.evaluateString(scope, "Movebfore(game_pos_x_1, game_pos_y_1);", MyILoadResource.TAG, 1, null);
	}

	public static void onLoad(){
		//if(eventfile.equals(""))return;
		cx.evaluateString(scope, "onLoad();", MyILoadResource.TAG, 1, null);
	}

	public static void onDestory(){
		//if(eventfile.equals(""))return;

		cx.evaluateString(scope, "onDestory();", MyILoadResource.TAG, 1, null);
	}

	public static void Fightend(int flag){
		//if(eventfile.equals(""))return;
		scope.put("game_pos_y_1", scope, flag);

		cx.evaluateString(scope, "Fightend(game_pos_y_1);", MyILoadResource.TAG, 1, null);
	}
}
