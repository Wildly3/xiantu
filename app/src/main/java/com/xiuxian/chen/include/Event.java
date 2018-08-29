package com.xiuxian.chen.include;
import android.app.*;
import android.content.*;
import com.xiuxian.chen.*;
import com.xiuxian.chen.map.*;
import com.xiuxian.chen.set.*;
import java.util.*;

import android.util.Log;
import android.view.*;
import com.xiuxian.chen.view.*;
import android.graphics.*;
import com.xiuxian.chen.data.*;
import com.xiuxian.chen.util.*;

import rx.*;
import rx.Observable;
import rx.android.schedulers.*;
import rx.schedulers.Schedulers;

import android.os.*;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 *内置js调用方法
 **/
public final class Event
{
    public static final String TAG = "com.xiuxian.chen.include.Event";

	private Activity app;
	
	private MapView mv;
	
	private java.util.Map<String, Integer> data;
	
	private static Event event = null;
	
	private Event(Activity app){
		this.app=app;
		this.data = new HashMap<>();
	}
	
	public static Event New(Activity app){
		if(event == null) event=new Event(app);
		return event;
	}
	
	public void setMapView(MapView mv){
		this.mv = mv;
	}
	
	public MapView getMapView(){
		return this.mv;
	}
	
	//提示框
	public MyDialog Alert(String title, String str, int _type){
		MyDialog md = new MyDialog(app)
		.SetTitle(title)
		.setMessage(str);
		switch(_type){
			default:
			case 0:
				md.setTouchDownDissmis(true);
			break;
			
			case 1:
				md.setCancel("确定", null);
			break;
		}
		md.Show();
		
		return md;
	}
	
	public MyDialog Alert(){
		return new MyDialog(app);
	}
	
	//提示框，end按下确定键调用函数名
	public MyDialog Alert(String title, String str, final String end)
	{
		return new MyDialog(app)
			.SetTitle(title)
			.setMessage(str)
			.setCancel(false)
			.setCancel("取消", null)
			.setSelect("确定", new MyDialog.OnClickListener(){
				@Override
				public void onClick(MyDialog dialog)
				{
					EventRun.GO(end);
				}
			})
			.Show();
	}
	
	public MyDialog Alert(String t, String m, String posn, String celn, final String posend, final String celend){
		MyDialog md = new MyDialog(app)
		.SetTitle(t)
		.setMessage(m)
			.setSelect(posn, new MyDialog.OnClickListener(){
				@Override
				public void onClick(MyDialog dialog)
				{
					if(posend!=null&&!posend.equals(""))
					EventRun.GO(posend);
				}
			})
			.setCancel(celn, new MyDialog.OnClickListener(){
				@Override
				public void onClick(MyDialog dialog)
				{
					if(celend!=null&&!celend.equals(""))
					EventRun.GO(celend);
				}
			})
			.Show();
		return md;
	}
	
	//对话框
	public static final class Dialog
	{
		private String title;
		
		private String message;
		
		private String select;
		
		private String cancel;
		
		private String click1;
		
	}
	
	public void EnemyMsg(String name, String msg, final String posc){
		Enemy ene = BaseActivity.ScanEnemy(name);
		this.EnemyMsg(ene, msg, posc, "挑战", "取消");
		
	}
	
	public void EnemyMsg(final Enemy ene, final String msg, final String posc, String select, String cancel){
		if(ene == null)return;
		if(ene.base == null) ene.NewBuffer();

        final LinearLayout main = new LinearLayout(app);

        main.setOrientation(LinearLayout.VERTICAL);

        final AllLayout layout = new AllLayout(app);

		final ClickTextView ct = new ClickTextView(app);

        final ClickTextView ct2 = new ClickTextView(app);

        main.addView(layout);

        LinearLayout textall = new LinearLayout(app);

        textall.setOrientation(LinearLayout.HORIZONTAL);

        textall.addView(ct);

        ct2.setPadding((int) MainActivity.DP * 4, 0, 0, 0);

        textall.addView(ct2);

        main.addView(textall);
		
		final MyDialog md = new MyDialog(app)
			.SetTitle("信息")
			.setView(main);
		if (select != null)
			md.setSelect(select, new MyDialog.OnClickListener(){
					@Override
					public void onClick(MyDialog dialog)
					{
						if(posc!=null&&!posc.equals(""))
							EventRun.GO(posc);
					}
				});

		if (cancel != null)
			md.setCancel(cancel, null);

		md.Show();
		
		rx.Observable.create(new Observable.OnSubscribe<Bundle>(){
				@Override
				public void call(Subscriber<? super Bundle> call)
				{
                    Bundle b = new Bundle();

                    b.putBoolean("isbasic", true);

                    call.onNext(b);
					
					if (ene.getWearEquip() != null)
					{
                        b = new Bundle();
                        b.putString("text", "装备");
						b.putString("ctext", null);
                        b.putInt("type", 1);
						b.putFloat("textsize", 20f);
						b.putInt("color", Color.YELLOW);
						call.onNext(b);
						
						for (String wpn : ene.getWearEquip())
						{
							Weapon wp = BaseActivity.ScanWeapon(wpn);
							if (wp != null){
                                b = new Bundle();
								b.putString("text", wpn);
								b.putString("ctext", wpn+"\n"+wp.getExplain());
                                b.putInt("type", 1);
								b.putFloat("textsize", 16f);
								b.putInt("color", Color.YELLOW);
								call.onNext(b);
							}
						}
					}

					if (ene.getWearSkill() != null)
					{
                        b = new Bundle();
						b.putString("text", "技能");
						b.putString("ctext", null);
                        b.putInt("type", 2);
						b.putFloat("textsize", 20f);
						b.putInt("color", Color.BLUE);
						call.onNext(b);
						
						for (String skn : ene.getWearSkill())
						{
							Skill sk = BaseActivity.ScanSkill(skn);
							if (sk != null)
							{
                                b = new Bundle();
								b.putString("text", skn);
								b.putString("ctext", skn+"\n"+sk.getExplain());
                                b.putInt("type", 2);
								b.putFloat("textsize", 16f);
								b.putInt("color", Color.YELLOW);
								call.onNext(b);
							}

						}
					}

					if(msg!=null&&!"".equals(msg)){
                        b = new Bundle();
						b.putString("text", msg);
						b.putString("ctext", null);
						b.putFloat("textsize", 25f);
						b.putInt("color", Color.WHITE);
						call.onNext(b);
					}

				}
			})
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(new rx.Observer<Bundle>(){
				@Override
				public void onCompleted()
				{}

				@Override
				public void onError(Throwable e)
				{}

				@Override
				public void onNext(Bundle b)
				{
                    boolean isbasic = b.getBoolean("isbasic", false);

                    if(isbasic){
                        float textsize = 16f;

                        LinearLayout linearLayout1 = layout.CreateLayout();

                        LinearLayout linearLayout2 = layout.CreateLayout();

                        LinearLayout head = new LinearLayout(app);

                        head.setOrientation(LinearLayout.HORIZONTAL);

                        layout.addText(ene.name, 20f, Color.WHITE, head);

                        String[] race = Util.getRaceString(ene.race, app);

                        layout.addText(" [" + race[0] + "] ", 20f, Integer.parseInt(race[1]), head);

                        layout.addText("LV. " + ene.level, 20f, Color.WHITE, head);

                        layout.addLayout(head);

                        layout.addText("生命 "+ene.base.max_health, textsize, getColor(R.color.health), linearLayout1);

                        layout.addText("攻击 "+ene.base.atk, textsize, getColor(R.color.atk), linearLayout1);

                        layout.addText("灵力 "+ene.base.max_mp, textsize, getColor(R.color.mp), linearLayout1);

                        layout.addText("防御 "+ene.base.def, textsize, getColor(R.color.def), linearLayout1);

                        layout.addText("速度 "+ene.base.speed, textsize, getColor(R.color.speed), linearLayout1);

                        layout.addText("气运 "+ene.base.luck, textsize, getColor(R.color.luck), linearLayout1);

                        layout.addText("金属性 "+ene.base.metal + "   金属性抗性 " + ene.base.metal_resist,
                                textsize, getColor(R.color.metal), linearLayout2);

                        layout.addText("木属性 "+ene.base.wood + "   木属性抗性 " + ene.base.wood_resist,
                                textsize, getColor(R.color.wood), linearLayout2);

                        layout.addText("水属性 "+ene.base.water + "   水属性抗性 " + ene.base.water_resist,
                                textsize, getColor(R.color.water), linearLayout2);

                        layout.addText("火属性 "+ene.base.fire + "   火属性抗性 " + ene.base.fire_resist,
                                textsize, getColor(R.color.fire), linearLayout2);

                        layout.addText("土属性 "+ene.base.soil + "   土属性抗性 " + ene.base.soil_resist,
                                textsize, getColor(R.color.soil), linearLayout2);

                        LinearLayout layout3 = new LinearLayout(app);

                        layout3.setOrientation(LinearLayout.HORIZONTAL);

                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);

                        layoutParams.weight = 0.7f;

                        layoutParams.setMargins((int) (MainActivity.DP * 8), 0, 0, 0);

                        linearLayout2.setLayoutParams(layoutParams);

                        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-2, -1);

                        layoutParams2.weight = 0.3f;

                        linearLayout1.setLayoutParams(layoutParams2);

                        layout3.addView(linearLayout1);

                        layout3.addView(linearLayout2);

                        layout.addView(layout3);
                    }

					String text = b.getString("text");
					
					String ctext = b.getString("ctext", null);

                    int color = b.getInt("color");
					
					float textsize = b.getFloat("textsize");

                    int type = b.getInt("type");
					if (type == 1)
					    ct.addText(text, ctext, textsize, color);
                    else
                        ct2.addText(text, ctext, textsize, color);
				}
			});
	}
	
	private int getColor(int id){
		return app.getResources().getColor(id);
	}
	
	//对话框
	public RoleDialog ListAlert(){
		RoleDialog rd = new RoleDialog(app);
		rd.setCancel(false);
		return rd;
	}
	
	public void Message(String text){
		MyToast.makeText(app, text, 500).show();
	}
	
	public void Message(String text, int gravity){
		MyToast.makeText(app, text, 500, gravity).show();
	}
	/*
	//移动到指定地图
	@SuppressWarnings("all")
	public void MoveMap(int id, int x, int y){
		this.mv.MoveRoleMap(id, x, y);
	}
	
	//移动到当前地图指定坐标
	@SuppressWarnings("all")
	public void MoveRoleMap(int x, int y){
		
		//BaseActivity.map_action.pos_x=x;
		//BaseActivity.map_action.pos_y=y;
		
		
		this.mv.MoveRole(x, y);
	}
	*/
	//移动到指定地图
	public void MoveMap(int id, int x, int y){
		if(this.mv == null){
		BaseActivity.map_action.map_id=id;
		BaseActivity.map_action.pos_x=x;
		BaseActivity.map_action.pos_y=y;
		}else
		mv.MoveRoleMap(id, x, y);
	}

	//移动到指定位置
	public void MoveRoleMap(int x, int y){
		if(this.mv == null){
		BaseActivity.map_action.pos_x=x;
		BaseActivity.map_action.pos_y=y;
		}else
		mv.MoveRole(x, y);
	}
	
	//停止当前移动
	public void StopMove(){
		if(this.mv == null) return;
		this.mv.StopMove();
	}
	
	//获取当前所在地图的x
	public int getX(){
		return BaseActivity.map_action.pos_x;
	}
	
	//获取当前所在地图的y
	public int getY(){
		return BaseActivity.map_action.pos_y;
	}
	
	//获取玩家等级
	public int getPlayLevel(){
		return BaseActivity.base.level;
	}
	
	//获取玩家名称
	public String getPlayName(){
		return BaseActivity.base.name;
	}
	
	//添加一个任务
	public boolean addTask(String name){
		GameTask task = Tasks.ScanTask(name);
		if(task == null)return false;
		return BaseActivity.gtmanager.addTask(task);
	}
	
	//在空间里添加装备
	public boolean addEquip(String name, int quality){
		Weapon equip=BaseActivity.ScanWeapon(name);
		if(equip==null)return false;
		equip=equip.NewObject();
		equip.quality=quality;
		equip.MaxDura();
		return BaseActivity.space.addWeapon(equip);
	}
	
	//在空间里添加材料
	public boolean addMaterial(String name, int num){
		Material mat=BaseActivity.ScanMaterial(name);
		if(mat==null)return false;
		mat=mat.NewObject();
		mat.quantity=num;
		return BaseActivity.space.addMaterail(mat);
	}
	
	//在空间里添加消耗品
	public boolean addConsunables(String name, int num){
		Consunables con=BaseActivity.ScanConsunables(name);
		if(con==null)return false;
		con=con.NewObject();
		con.quantity=num;
		return BaseActivity.space.addConsunables(con);
	}
	
	//扩大空间
	public void ExpandSpace(int expand){
		if(expand < 1) return ;
		BaseActivity.space.space_max += expand;
	}
	
	//增加玩家的金币
	public void addMoney(int m){
		BaseActivity.space.addMoney(m);
	}
	
	//增加玩家的经验
	public boolean addExp(int e){
		BaseActivity.base.exp += e;
		return BaseActivity.base.UpdateExp();
	}
	
	//获取空间该物品的数量
	public int getGoodsNumber(String name){
		return BaseActivity.space.getGoodInfoMation(name)[1];
	}
	
	//取出物品
	public boolean TakeOutGoods(String name, int num){
		
		return BaseActivity.space.TakeoutAll(name, num);
	}
	
	//战斗
	public void Fight(List<Enemy> enes)
	{
		if(this.mv == null) return;
		this.mv.Fight(enes);
	}

    public void FightName(List<String> enes){
		if(this.mv == null) return;
        List<Enemy> list = new ArrayList<>();
        for(String name : enes){
            Enemy e = BaseActivity.ScanEnemy(name);
            list.add(e);
        }
        this.mv.Fight(list);
    }

	public void TestFight(String[] enes){
		if(this.mv == null) return;
		List<Enemy> list = new ArrayList<>();
        for(String name : enes){
            Enemy e = BaseActivity.ScanEnemy(name);
            list.add(e);
        }
        this.mv.TestFight(list);
	}
	
    /**
     * 发起战斗
     * @param enes 敌人名称数组对象
     */
    public void FightName(String[] enes){
        this.FightName(enes, false);
    }

    /**
     * 发起战斗
     * @param enes 敌人名称数组
     * @param isc 是否为不能逃跑的战斗
     */
    public void FightName(String[] enes, boolean isc){
		if(this.mv == null) return;
        List<Enemy> list = new ArrayList<>();
        for(String name : enes){
            Enemy e = BaseActivity.ScanEnemy(name);
            list.add(e);
        }
        this.mv.Fight(list, isc);
    }

	//判断任务是否完成
	public boolean isCompleteTask(String name){
		return BaseActivity.gtmanager.isEmptyCompleted(name);
	}
	
	//判断是否已经接取
	public boolean isAcceptTask(String name){
		return BaseActivity.gtmanager.isEmpty(name);
	}
	
    /**
     * 获取当前地图对象
     * @return 当前地图对象
     */
    public MyMap getNowMap()
    {
		if(this.mv == null){
			return BaseActivity.mapmanager.getActionNowMap(BaseActivity.map_action);
		}
		
        return this.mv.getNowMap();
    }

    /**
     * 插入数据
     * @param key 键
     * @param value 值
     */
    public void put(String key, int value)
    {
		this.data.put("map_data_"+this.getNowMap().id+"_"+key, value);
		
        //this.getNowMap().put(key, value);
    }

    public int get(String key, int mdefault)
    {
		String tkey = "map_data_"+this.getNowMap().id+"_"+key;
		
		if(!this.data.containsKey(tkey))return mdefault;
		
        return this.data.get(tkey);
    }

	public void Save(){
		if(this.data.size() < 1)return;
		Saver save = new Saver(app, "event_data");
		for(java.util.Map.Entry<String, Integer> entry : data.entrySet())
			save.put(entry.getKey(), entry.getValue());
		
		save.complete();
	}
	
	public void Read(){
		Saver save = new Saver(app, "event_data");
		if(save.Reader()!=Saver.SUCCESS)return;
		for(Saver.Entry entry : save.toList())
		this.data.put(entry.getKey(), entry.getIntegerValue());
	}
	
}
