package com.xiuxian.chen.data;

import android.content.*;
import android.os.*;
import android.view.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xiuxian.chen.*;
import com.xiuxian.chen.include.*;
import com.xiuxian.chen.map.*;
import com.xiuxian.chen.other.*;
import com.xiuxian.chen.set.*;
import com.xiuxian.chen.view.*;

import java.util.ArrayList;
import java.util.List;

public class MyIArchive implements IArchive
{
	public static final String Path=Environment.getExternalStorageDirectory().toString();

	public static final String FileName="PlayFile";

	public static final String SpaceFileName="Space";

	public static final String MapFileName="Map";

	public static final String TaskFileName = "Task";

    public static final String OtherFileName = "other";
	
	Context cx;
	
	public MyIArchive(Context cx){
		this.cx = cx;

		BaseActivity.gtmanager = new GameTaskManager();
	}
	
	@Override
	public boolean LoadArchive()
	{
		Saver sv=new Saver(cx, FileName);
		
		if(sv.Reader() != Saver.SUCCESS) return false;
		
		GetFile();
		
		return true;
	}

	void GetFile()
	{
        BaseActivity.base = ReadPlay(FileName);

        BaseActivity.space = ReadSpace(SpaceFileName);

        BaseActivity.map_action = ReadPosition(MapFileName);

        BaseActivity.gtmanager = ReadTask(TaskFileName);

        ReadOther();

//		BaseActivity.base = new Player();
//
//		BaseActivity.space = new com.xiuxian.chen.set.Space();
//
//		Saver sv=new Saver(cx, FileName);
//
//		if (sv.Reader() != Saver.SUCCESS) return ;
//
//		Shoping shop = Shoping.getInstance();
//
//		shop.setId(sv.getInt("shopid"));
//
//		BaseActivity.base.name = sv.get("name");
//
//		BaseActivity.base.health = sv.getInt("health");
//
//		BaseActivity.base.atk = sv.getInt("atk");
//
//		BaseActivity.base.mp = sv.getInt("mp");
//
//		BaseActivity.base.def = sv.getInt("def");
//
//		BaseActivity.base.speed = sv.getInt("speed");
//
//		BaseActivity.base.luck = sv.getInt("luck");
//
//		BaseActivity.base.exp = sv.getInt("exp");
//
//		BaseActivity.base.max_exp = sv.getInt("max_exp");
//
//		BaseActivity.base.attrs = sv.getInt("attrs");
//
//		BaseActivity.base.level = sv.getInt("level");
//
//		int size=sv.getInt("ecgs_size");
//
//		for (int i=0;i < size;i++)
//		{
//			GOODS.ECGS.get(i).isuse = Boolean.parseBoolean(sv.get("isuse" + i));
//		}
//
//		String name = null;
//
//		String quality = null;
//
//		String dura = null;
//
//		String type = null;
//
//		Weapon wp = null;
//
//		for (int i=0;i < 7;i++)
//		{
//			name = sv.get("equip_name" + i);
//			quality = sv.get("equip_quality" + i);
//			dura = sv.get("equip_dura" + i);
//			type = sv.get("equip_type" + i);
//			if (name != null && !name.equals(""))
//			{
//				wp = BaseActivity.ScanWeapon(name);
//				if (wp != null)
//				{
//					wp = wp.NewObject();
//					wp.quality = INT(quality);
//					wp.dura = INT(dura);
//					wp.type = INT(type);
//					BaseActivity.base.equipment[wp.type] = wp;
//				}
//			}
//		}
//
//		int sk_size=sv.getInt("skillments_size");
//
//		for (int i=0, length = BaseActivity.base.skillment.length; i < length; i++)
//		{
//			BaseActivity.base.skillment[i] = BaseActivity.ScanSkill(sv.get("mentskill" + i));
//		}
//
//		for (int i=0;i < sk_size;i++)
//		{
//			BaseActivity.base.skillments.add(BaseActivity.ScanSkill(sv.get("skillments" + i)));
//		}
//
//		BaseActivity.base.NewBuffer();
//
//		BaseActivity.base.base.now_health = sv.getInt("now_health");
//
//		BaseActivity.base.base.now_mp = sv.getInt("now_mp");
//
//		ReadSpace();
//		ReadMap();
//		ReadTask();
	}

	//读取空间物品
    @Deprecated
	void ReadSpace(){
		Saver sv = new Saver(cx, SpaceFileName);
		if(sv.Reader()!=Saver.SUCCESS)return;
		int wps=sv.getInt("weapon_num");
		int cbs=sv.getInt("consunables_num");
		int mts=sv.getInt("materail_num");

		BaseActivity.space.addMoney(sv.getInt("money"));

		BaseActivity.space.space_max=sv.getInt("space_max");

		String name = null;

		Weapon wp = null;

		Consunables con = null;

		Material mat = null;

		for(int i=0;i<wps;i++){
			name=sv.get("equip_name"+i);
			int quality=sv.getInt("equip_quality"+i);
			int dura=sv.getInt("equip_dura"+i);

			wp = BaseActivity.ScanWeapon(name);

			if(wp!=null){
				wp = wp.NewObject();
				wp.quality=quality;
				wp.dura=dura;
				BaseActivity.space.addWeapon(wp);
			}
		}

		for(int i=0;i<cbs;i++){
			name=sv.get("con_name"+i);
			int num=sv.getInt("con_num"+i);

			con = BaseActivity.ScanConsunables(name);

			if(con!=null){
				con = con.NewObject();
				con.quantity=num;
				BaseActivity.space.addConsunables(con);
			}
		}

		for(int i=0;i<mts;i++){
			name=sv.get("mat_name"+i);
			int num=sv.getInt("mat_num"+i);
			mat=BaseActivity.ScanMaterial(name);
			if(mat!=null){
				mat = mat.NewObject();
				mat.quantity=num;
				BaseActivity.space.addMaterail(mat);
			}
		}
	}

	//读取地图数据
    @Deprecated
	void ReadMap(){
		Saver sv = new Saver(cx, MapFileName);

		sv.Reader();

        int mapid = sv.getInt("action_map_id");

		BaseActivity.map_action=new Action(mapid);

        BaseActivity.mapmanager.CacheMap(mapid);

		BaseActivity.map_action.pos_x=sv.getInt("action_map_pos_x");

		BaseActivity.map_action.pos_y=sv.getInt("action_map_pos_y");

		MainActivity.event.Read();
	}

	//读取任务信息
    @Deprecated
	void ReadTask(){
		Saver save = new Saver(cx, this.TaskFileName);
		if(save.Reader() != Saver.SUCCESS) return ;


		/***   读取已完成任务信息   ***/
		int completed_task_size = save.getInt("completed_task_size");

		//Log.i(LoadResources.TAG, "cts:"+completed_task_size);

		if(completed_task_size > 0)
			for(int i=0;i<completed_task_size;i++){
				String name = save.get("completed_task_name_"+i);
				BaseActivity.gtmanager.addCompleteTask(name);
			}

		/***  读取进行中任务信息  ***/

		int run_task_size = save.getInt("run_task_size");

		//Log.i(LoadResources.TAG, "rts:"+run_task_size);

		String key = null;

		String str = null;

		String info[] = null;

		if(run_task_size > 0)
			for(int i=0;i<run_task_size;i++){
				key = "run_task_"+i;

				String task_name = save.get(key+"_name");
				//Log.i(LoadResources.TAG, "name:"+task_name);
				GameTask gt = Tasks.ScanTask(task_name);

				int taskInfoMationSize = save.getInt(key+"_taskinfomation_size");
				//Log.i(LoadResources.TAG, "tims:"+taskInfoMationSize);

				for(int j=0;j<taskInfoMationSize;j++){
					str = save.get(key+"_taskinfomation_enemykill_number_"+j);
					//Log.i(LoadResources.TAG, "str:"+str);

					info = str.split(",");

					gt.setKillTask(info[0], Integer.parseInt(info[1]));

				}
				BaseActivity.gtmanager.addTask(gt);
			}
	}

	int INT(String str){
		return Integer.parseInt(str);
	}
	
	@Override
	public void SaveArchive()
	{
		SaveFile(BaseActivity.base);
	}

	private void SaveFile(Player play){
        SavePlay(play);

        SaveSpace(BaseActivity.space);

        SavePosition(BaseActivity.map_action);

        SaveTask(BaseActivity.gtmanager);

        SaveOther();
	}

	void SaveOther(){
        Saver sv = new Saver(cx, OtherFileName);
        int i=0;
		sv.put("ecgs_size",GOODS.ECGS.size());
		for(ExChangeGoodsAll ec : GOODS.ECGS)
			sv.put("isuse"+i++,""+ec.isuse);

        sv.put("myid", Shoping.getInstance().getId());

		sv.complete();
    }

    void ReadOther(){
        Saver sv = new Saver(cx, OtherFileName);
        if (sv.Reader() != Saver.SUCCESS) return;
        int size = sv.getInt("ecgs_size");
		for (int i=0;i < size;i++)
			GOODS.ECGS.get(i).isuse = Boolean.parseBoolean(sv.get("isuse" + i));
        Shoping.getInstance().setId(sv.getInt("myid"));

    }

	void SavePlay(Player player){
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(player, Player.class);
        new Saver(cx, FileName)
                .complete(json);
    }

    Player ReadPlay(String fileName){
        Gson gson = new GsonBuilder().create();

        Saver saver = new Saver(cx, fileName);

        String json = saver.ReadString();

        if (json == null) return null;

        Player player = gson.fromJson(json, Player.class);

        for (int i=0;i<player.equipment.length;i++){
            if (player.equipment[i] != null){
                Weapon wp = BaseActivity.ScanWeapon(player.equipment[i].name);
                if (wp != null) {
                    wp = wp.NewObject();
                    wp.quality = player.equipment[i].quality;
                    wp.dura = player.equipment[i].dura;
                    player.equipment[i] = wp;
                }
            }
        }

        for (int i=0;i<player.skillment.length;i++){
            if (player.skillment[i] != null){
                Skill skill = BaseActivity.ScanSkill(player.skillment[i].name);
                if (skill != null) {
                    skill = skill.newObject();
                    player.skillment[i] = skill;
                }
            }
        }

        for (int i = 0; i < player.skillments.size(); i++) {
            Skill sks = player.skillments.get(i);
            Skill skill = BaseActivity.ScanSkill(sks.name);
            if (skill != null) {
                skill = skill.newObject();
                player.skillments.set(i, skill);
            }
        }

        player.NewBuffer();

        return player;
    }

    //保存空间
    void SaveSpace(Space space){
        Gson gson = new GsonBuilder().create();

        String json = gson.toJson(space, Space.class);

        new Saver(cx, SpaceFileName)
                .complete(json);
    }

    //读取空间
    Space ReadSpace(String fileName){
        Gson gson = new GsonBuilder().create();

        Saver saver = new Saver(cx, fileName);

        String json = saver.ReadString();

        if (json == null) return null;

        Space space = gson.fromJson(json, Space.class);

        space.reMoney();

        if (space.weapons.size() > 0){
            List<Weapon> ws = new ArrayList<>(space.weapons.size());
            ws.addAll(space.weapons);
            space.weapons.clear();
            for (Weapon wp : ws){
                Weapon wwp = BaseActivity.ScanWeapon(wp.name);
                if (wwp != null){
                    wwp = wwp.NewObject();
                    wwp.quality = wp.quality;
                    wwp.dura = wp.dura;
                    space.addWeapon(wwp);
                }
            }
        }

        if (space.consunables.size() > 0){
            List<Consunables> cons = new ArrayList<>(space.consunables.size());
            cons.addAll(space.consunables);
            space.consunables.clear();
            for (Consunables con : cons){
                Consunables conss = BaseActivity.ScanConsunables(con.name);
                if (conss != null) {
                    conss = conss.NewObject();
                    conss.quantity = con.quantity;
                    space.addConsunables(conss);
                }
            }
        }

        if (space.materials.size() > 0){
            List<Material> mats = new ArrayList<>(space.materials.size());

            mats.addAll(space.materials);

            space.materials.clear();

            for (Material mat : mats){
                Material material = BaseActivity.ScanMaterial(mat.name);
                if (material != null) {
                    material = material.NewObject();
                    material.quantity = mat.quantity;
                    space.addMaterail(material);
                }
            }
        }

        return space;
    }

    //保存位置
    void SavePosition(Action action){
        Gson gson = new GsonBuilder().create();

        String json = gson.toJson(action, Action.class);

        new Saver(cx, MapFileName)
                .complete(json);

        MainActivity.event.Save();
    }

    //读取位置
    Action ReadPosition(String fileName){
        Gson gson = new GsonBuilder().create();

        String json = new Saver(cx, fileName)
                .ReadString();

        if (json == null) return null;

        Action action = gson.fromJson(json, Action.class);

        return action;
    }

    void SaveTask(GameTaskManager manager){
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(manager, GameTaskManager.class);
        new Saver(cx, TaskFileName)
                .complete(json);
    }

    //读取任务
    GameTaskManager ReadTask(String fileName){
        Gson gson = new GsonBuilder().create();

        String json = new Saver(cx, fileName)
                .ReadString();

        if (json == null) return null;

        GameTaskManager gameTaskManager = gson.fromJson(json, GameTaskManager.class);

        return gameTaskManager;
    }

    @Deprecated
	void save_space(){
		//Player base = BaseActivity.base;
		Space space = BaseActivity.space;
		Saver sv = new Saver(cx, SpaceFileName);
		int wps=space.weapons.size();
		int cbs=space.consunables.size();
		int mts=space.materials.size();
		sv.put("weapon_num",wps);
		sv.put("consunables_num",cbs);
		sv.put("materail_num",mts);
		sv.put("space_max",space.space_max);
		sv.put("money",space.getMoney());
		int i=0;
		for(Weapon wp : space.weapons){
			sv.put("equip_name"+i,wp.name);
			sv.put("equip_quality"+i,""+wp.quality);
			sv.put("equip_dura"+i,""+wp.dura);
			i++;
		}
		i=0;
		for(Consunables c : space.consunables){
			sv.put("con_name"+i,c.name);
			sv.put("con_num"+i,""+c.quantity);
			i++;
		}
		i=0;
		for(Material m : space.materials){
			sv.put("mat_name"+i,m.name);
			sv.put("mat_num"+i,""+m.quantity);
			i++;
		}

		sv.complete();
		savemapposition();
	}

	//保存人物坐标信息
    @Deprecated
	void savemapposition(){
		Action map_action = BaseActivity.map_action;
		Saver sv=new Saver(cx, MapFileName);
		sv.put("action_map_id", map_action.map_id);
		sv.put("action_map_pos_x", map_action.pos_x);
		sv.put("action_map_pos_y", map_action.pos_y);
		//int i=0;
		/*
		 for(MyMap map : mapmanager.getMaps()){
		 sv.putInteger(map.name+"_length",map.item.length);
		 i=0;
		 for(MyMap.MapItem mi : map.item){
		 sv.put(map.name+i, mi.pos_x+","+mi.pos_y+","+mi.istigger);
		 i++;
		 }}*/

		sv.complete();

		MainActivity.event.Save();

		savetask();
	}

	//保存任务信息
    @Deprecated
	void savetask(){
		GameTaskManager gtmanager = BaseActivity.gtmanager;
		
		Saver save = new Saver(cx, TaskFileName);

		int completed_task_size = gtmanager.getCompletedTaskNames().size();

		save.put("completed_task_size", completed_task_size);

		for(int i=0;i<completed_task_size;i++){
			String name = gtmanager.getCompletedTaskNames().get(i);
			save.put("completed_task_name_"+i, name);
		}

		int run_task_size = gtmanager.getTasks().size();
		save.put("run_task_size", run_task_size);
		for(int i=0;i<run_task_size;i++){
			GameTask gt = gtmanager.getTasks().get(i);
			String key = "run_task_"+i;
			save.put(key+"_name", gt.name);
			int taskInfoMationSize = gt.getTaskInfoMation().size();

			int c = 0;
			for(int j=0;j<taskInfoMationSize;j++){
				GameTask.TaskInfoMation ti = gt.getTaskInfoMation().get(j);
				if(ti.type == GameTask.ENEMY){
					save.put(key+"_taskinfomation_enemykill_number_"+j, ti.name+","+ti.getNumber());
					c++;
				}
			}
			save.put(key+"_taskinfomation_size", c);
		}

		save.complete();
	}
	
}
