package com.xiuxian.chen.view;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;
import com.xiuxian.chen.*;
import com.xiuxian.chen.adapter.*;
import com.xiuxian.chen.data.*;
import com.xiuxian.chen.include.*;
import com.xiuxian.chen.map.*;
import com.xiuxian.chen.set.*;
import java.util.*;
import com.xiuxian.chen.util.*;

import rx.*;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public final class MapView extends View
{
    public static final String TAG = "com.xiuxian.chen.include.MapView";

	//移动速度
	static final int MOVE_SPEED = 130;
	//背景颜色
	static int BACKGROUND_COLOR = 95;

    //战斗界面显示基本高度
    static int FIGHTVIEW_HEIGHT = 55;

	static int TEXT_COLOR;
	
	//地图块数量
	static final int MAP_TILE_WIDTH = 20;
	
	static final int MAP_TILE_HEIGHT = 25;
	
	Context mContext;
	//MapManager mapmanager;
	//当前地图
	MyMap map;
	//是否处于战斗状态
	boolean isfight;

    //是否已经结束战斗
    public boolean isfightend;
	
	public TextView pos = null;
	
	public MapActivity ma;

    Timing timer, timer2;

    //是否自动寻路
    boolean isAutoMove;

    //自动寻路坐标
    int auto_x, auto_y;

    //在刚才是否遇敌
    boolean isEnenFight;

    //结束战斗后行走步数
    int MoveQuanlity;

	public MapView(Context c){
		this(c, null, 0);
		mContext=c;
	}
	
	public MapView(android.content.Context context, android.util.AttributeSet attrs) {
		this(context, attrs, 0);
		mContext=context;
	}

    public MapView(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		mContext = context;

		BACKGROUND_COLOR = context.getResources().getColor(R.color.color_background);

		TEXT_COLOR = context.getResources().getColor(R.color.color_alltext);

        timer = new Timing(MOVE_SPEED);

        //长按查看信息响应时间
        timer2 = new Timing(500);
	}
	
	void msgdialog(String t){
		msgdialog(null, t);
	}
	
	void msgdialog(String t, String m){
		new MyDialog(getContext())
		.SetTitle(t)
			.setMessage(m)
			//.setCancel("关闭", null)
			.show();
	}
	
	//绘制地图界面
	boolean isc, isc2;
	
	int MoveX;
	
	int MoveY;
	
	int screen_x;
	
	int screen_y;

    boolean isene = true;
	
	@Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        update();

        canvas.drawColor(BACKGROUND_COLOR);

        if (!isc) {
            init();

            isc = true;
        }

        if (!isc2 && pos != null) {
            showmsg();
            isc2 = true;
        }

        if (isfight) fightDraw(canvas);
        else {
            Paint p = new Paint();

            p.setColor(Color.RED);

            int LineWidth = (map.width <= MAP_TILE_WIDTH ? map.width : MAP_TILE_WIDTH) * wt;

            int LineHeight = (map.height <= MAP_TILE_HEIGHT ? map.height : MAP_TILE_HEIGHT) * ht;

            canvas.drawRect(0, 0, LineWidth, LineHeight, p);

            p.setColor(Color.rgb(BACKGROUND_COLOR, BACKGROUND_COLOR, BACKGROUND_COLOR));

            canvas.drawRect(2, 2, (LineWidth) - 2, (LineHeight) - 2, p);

            if (map == null) canvas.drawRGB(0, 0, 0);

            for (MyMap.MapItem mi : map.item) {
                if (mi.flag == MyMap.MapItem.ENEMY) continue;

                p.setColor(mi.color);

                canvas.drawRect((mi.pos_x * wt) + MoveX * wt, (mi.pos_y * ht) + MoveY * ht, (mi.pos_x * wt) + wt + (MoveX * wt), (mi.pos_y * ht) + ht + (MoveY * ht), p);

                p.setColor(TEXT_COLOR);

                p.setTextSize(35);

                int tw = getTextWH(mi.name, p).width();

                int tx = (mi.pos_x * wt) + MoveX * wt;

                if ((tx + tw) > getWidth() && mi.pos_x < MAP_TILE_WIDTH)
                    tx -= (tx + tw) - getWidth();

                int ty = (mi.pos_y * ht) + (ht / 2) + MoveY * ht;

                canvas.drawText(mi.name, tx, ty, p);
            }

            if (isAutoMove) {
                p.setColor(Color.WHITE);
                canvas.drawCircle(((auto_x * wt) + MoveX * wt) + wt / 2, ((auto_y * ht) + MoveY * ht) + ht / 2, MAP_TILE_WIDTH, p);
            }

            canvas.drawBitmap(action_pos, (screen_x * wt) - wt / 2, (screen_y * ht) - getRate(action_pos.getHeight(), 0.6f), p);
        }

        invalidate();
    }

    //逻辑更新
	void update(){
        if (isfight) isAutoMove = false;

        if(!isfight && timer.isInterval()){
            if(isAutoMove){
                if (isfight){
                    isAutoMove = false;
                } else {
                    Action action = BaseActivity.map_action;

                    int xd = Math.abs(action.pos_x - auto_x);

                    int yd = Math.abs(action.pos_y - auto_y);

                    if (yd == 0 || xd < yd & xd != 0) {
                        if (action.pos_x < auto_x) {
                            direction = 3;
                            keyevent();
                        } else if (action.pos_x != auto_x) {
                            direction = 2;
                            keyevent();
                        }
                    } else if (yd != 0) {
                        if (action.pos_y < auto_y) {
                            direction = 1;
                            keyevent();
                        } else if (action.pos_y != auto_y) {
                            direction = 0;
                            keyevent();
                        }

                    }

                    if (action.pos_x == auto_x & action.pos_y == auto_y) {
                        isAutoMove = false;
                    }
                }
            }
        }

        if (is_click && timer2.isInterval()){
            is_click = false;

            MainActivity.event.EnemyMsg(enemys.get(click_index), null, null, null, "关闭");
        }

    }
	
	Rect getTextWH(String str, Paint mp){
		Rect r=new Rect();
		mp.getTextBounds(str,0,str.length(),r);
		return r;
	}
	
	int getRate(int n, float r){
		
		return (int)((float)n*r);
	}
	
	public MyMap getNowMap(){
        return this.map;
    }

	//行走
	void keyevent(){
		map.Event_MoveBfore(BaseActivity.map_action.pos_x, BaseActivity.map_action.pos_y);
		
		boolean isWidthMove = (map.width > MAP_TILE_WIDTH);
		
		boolean isHeightMove = (map.height > MAP_TILE_HEIGHT);
		
		int MoveYNum = map.height - MAP_TILE_HEIGHT;
		
		int MoveXNum = map.width - MAP_TILE_WIDTH;
		
		switch(direction){
            default: break;
			case 0:
				if(BaseActivity.map_action.pos_y > 0){
					BaseActivity.map_action.pos_y--;
					if(screen_y < MAP_TILE_HEIGHT/2 & isHeightMove & MoveY < 0)
						MoveY++;
					else
					screen_y--;
					
					meetenemy();
				}
				break;
			case 1:
				if(BaseActivity.map_action.pos_y < map.height-1){
					BaseActivity.map_action.pos_y++;
					if(screen_y > MAP_TILE_HEIGHT/2 & isHeightMove & MoveY > (-MoveYNum))
						MoveY--;
					else screen_y++;
					
					meetenemy();
				}
				break;
			case 2:
				if(BaseActivity.map_action.pos_x > 0){
					BaseActivity.map_action.pos_x--;
					if(screen_x<MAP_TILE_WIDTH/2&isWidthMove&MoveX<0)
						MoveX++;
					else
						screen_x--;
					meetenemy();
				}
				break;
			case 3:
				if(BaseActivity.map_action.pos_x < map.width-1){
					BaseActivity.map_action.pos_x++;
					if(screen_x>MAP_TILE_WIDTH/2&isWidthMove&MoveX>(-MoveXNum))
						MoveX--;
					else screen_x++;
					meetenemy();
				}
				break;

		}
		
		map.Event_MoveAfter(BaseActivity.map_action.pos_x, BaseActivity.map_action.pos_y);
		
		showmsg();
//        Log.i(TAG, "keyevent: time "+Timing.end());
    }
	
	//初始化地图
	int screen_width;
	int screen_height;
	int wt, ht;
	Bitmap action_pos;
	void init()
	{
		screen_width = getWidth();
		
		screen_height = getHeight();
		
		wt = screen_width / MAP_TILE_WIDTH;
		
		ht = screen_height / MAP_TILE_HEIGHT;
		
		refreshmap();
		
		enemys = new ArrayList<>();
		
		plays = new ArrayList<>();
		
		enemy_draw = new ArrayList<>();
		
		play_draw = new ArrayList<>();
		
		action_pos = resizeImage2(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.role), wt * 2, ht * 2);
		
		initMapPosition();
	}
	
	//初始化地图位置
	void initMapPosition()
	{
		int px=BaseActivity.map_action.pos_x;

		int py=BaseActivity.map_action.pos_y;

		MoveX = 0;

		MoveY = 0;

		boolean isWidthMove=(map.width > MAP_TILE_WIDTH);

		boolean isHeightMove=(map.height > MAP_TILE_HEIGHT);

		int MoveYNum=map.height - MAP_TILE_HEIGHT;

		int MoveXNum=map.width - MAP_TILE_WIDTH;

		if (isWidthMove)
		{
			if (px > MAP_TILE_WIDTH / 2)
			{
				int pxNum=px - ((MAP_TILE_WIDTH / 2) + 1);
				if (pxNum > MoveXNum)pxNum = MoveXNum;
				MoveX = -pxNum;
				int pxOffset=map.width - px;
				int mw=(MAP_TILE_WIDTH / 2);

				screen_x = pxOffset >= mw - 1 ? mw + 1 : MAP_TILE_WIDTH - pxOffset;
			}
			else screen_x = px;
		}
		else screen_x = px;

		if (isHeightMove)
		{
			if (py > MAP_TILE_HEIGHT / 2)
			{
				int pyNum=py - ((MAP_TILE_HEIGHT / 2) + 1);
				if (pyNum > MoveYNum)pyNum = MoveYNum;
				MoveY = -pyNum;
				int pyOffset=map.height - py;
				int mh=(MAP_TILE_HEIGHT / 2);

				screen_y = pyOffset >= mh - 1 ? mh + 1 : MAP_TILE_HEIGHT - pyOffset;
			}
			else screen_y = py;

		}
		else screen_y = py;
	}
	
	Bitmap resizeImage2(Bitmap bitmap, int w, int h)  {
		Bitmap BitmapOrg = bitmap;
		int width = BitmapOrg.getWidth();
		int height = BitmapOrg.getHeight();
		int newWidth = w;
		int newHeight = h;
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// if you want to rotate the Bitmap  // matrix.postRotate(45);
		Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,  height, matrix, true);
		return resizedBitmap;
	}
	
	
	//方向按键事件
	Handler runkey=new Handler();

	int direction = 0;

	public void onKeyEvent(int key, int en){
		if(isfight){
			ma.mapkeyhide();
			ma.showfightkey2();
			runkey.removeCallbacks(keyrun);
			return;}
		if(en == MotionEvent.ACTION_DOWN)
		{
			move = true;

            isAutoMove = false;

			direction = key;

			keyevent();

			runkey.postDelayed(keyrun, MOVE_SPEED);
		}else if(en == MotionEvent.ACTION_UP)
		{
			StopMove();
		}
		
	}
	
	//boolean ismove;

	boolean move;

	Runnable keyrun=new Runnable(){
		@Override
		public void run()
		{
			if(!isfight & move){
				keyevent();
			}
			runkey.postDelayed(keyrun, MOVE_SPEED);
		}
	};
	
	//显示位置信息
	void showmsg(){
		pos.setText(map.name+"\n位置"+BaseActivity.map_action.pos_x+"."+BaseActivity.map_action.pos_y);
//		for(MyMap.MapItem m : map.item){
//			if(m.pos_x==BaseActivity.map_action.pos_x&m.pos_y==BaseActivity.map_action.pos_y){
//				pos.append("\n"+m.name);
//				if(m.flag==MyMap.MapItem.EXIT)
//					if(BaseActivity.mapmanager.getNextMapExpainl(m)!=null)pos.append("\n"+BaseActivity.mapmanager.getNextMapExpainl(m));
//				break;
//			}
//		}
	}
	
	List<Enemy> enemys;
	List<Player> plays;
	List<EnemyHealthDraw> enemy_draw;
	List<HealthDraw> play_draw;
	public PlayerAll pAll;
	TextItemDraw tid;
	//Rect[] enemys_rect;
	MyMap.MapItem now_ene_mmi;
	//是否为固定遇敌
	boolean is_map_ene;
	//遇敌
    void meetenemy() {
        if (!isene) return;

        List<Enemy> c = new ArrayList<>();

        now_ene_mmi = null;

        is_map_ene = false;

        if (map.now_enemy != null)
            for (MyMap.MapItem mene : map.now_enemy) {

                int istigger = MainActivity.event.get("map_ene_" + map.id + "_" + mene.pos_x + "_" + mene.pos_y, 0);

                if (istigger < 1)
                    if (mene.flag == MyMap.MapItem.ENEMY)
                        if (mene.tig_x != null)
                            for (int i = 0; i < mene.tig_x.length; i++) {
                                int x = mene.tig_x[i];

                                int y = mene.tig_y[i];

                                if (BaseActivity.map_action.pos_x == x & BaseActivity.map_action.pos_y == y) {
                                    now_ene_mmi = mene;

                                    is_map_ene = true;

                                    for (String name : mene.names) {
                                        c.add(BaseActivity.ScanEnemy(name));
                                    }
                                    break;
                                }
                            }
            }

        if (map.isenemy) {
            if (c.size() < 1) {
                if (map.odds(map.encounter))
                    for (String name : map.enes) {
                        Enemy e = BaseActivity.ScanEnemy(name);
                        if (e != null)
                            if (map.odds(e.appearodds))
                                c.add(e);
                    }

                //避免频繁遇到敌人
                if (isEnenFight){
                    if (MoveQuanlity > 3){
                        MoveQuanlity = 0;
                        isEnenFight = false;
                        return;
                    } else {
                        MoveQuanlity++;
                        return;
                    }
                }
            } else
                MainActivity.event.Alert(now_ene_mmi.name, now_ene_mmi.word, 0);

            if (c.size() > 0) {
                runkey.removeCallbacks(keyrun);

                isfight = true;

                state = false;

                attack_target_index = 0;

                ma.showmapkey();

//                ma.showfightkey();

                enemys.clear();

                plays.clear();

                enemy_draw.clear();

                play_draw.clear();

                for (Enemy ee : c) {
                    int ene_num = (new Random(System.currentTimeMillis()).nextInt(ee.appearodds_num) + 1);

                    if (ene_num < 1) ene_num = 1;

                    if (is_map_ene) ene_num = 1;

                    Enemy e = null;

                    for (int i = 0; i < ene_num; i++) {
                        ee.base = null;

                        e = ee.CopyEnemy();

                        e.base = null;

                        e.NewBuffer();

                        e.base.attack_num = 1;

                        e.base.now_health = e.base.max_health;

                        e.base.now_mp = e.base.max_mp;

                        for (Weapon wp : e.equipment) {
                            if (wp != null) wp.MaxDura();
                        }
                        enemys.add(e);
                    }
                }

                BaseActivity.base.NewBuffer();

                BaseActivity.base.base.attack_num = 1;

                if (BaseActivity.base.base.now_health > BaseActivity.base.base.max_health)
                    BaseActivity.base.base.now_health = BaseActivity.base.base.max_health;

                if (BaseActivity.base.base.now_mp > BaseActivity.base.base.max_mp)
                    BaseActivity.base.base.now_mp = BaseActivity.base.base.max_mp;

                plays.add(BaseActivity.base);

                pAll = new PlayerAll();
                pAll.my_atk_num = 1;
                pAll.addPlay(BaseActivity.base);

                //for(Enemy ee : enemys)
                pAll.addEnemy(enemys);

                pAll.setTextItemDraw(tid = new TextItemDraw(this));

                pAll.onFightStart();

                tid.setPosition(0, 200);

                tid.setTextSize(50);

                tid.setTextItemMax(10);

                int hd = FIGHTVIEW_HEIGHT;

                EnemyHealthDraw h = null;

                for (Enemy e : enemys) {
                    h = new EnemyHealthDraw(e);

                    h.setPosition(getWidth() - (HealthDraw.hwidth + 10), hd);

                    hd += h.getRect().height() + 40;

                    pAll.addAttackAnimationListener(h);

                    enemy_draw.add(h);
                }

                hd = FIGHTVIEW_HEIGHT;

                HealthDraw h2 = null;

                for (Player p : plays) {
                    h2 = new HealthDraw(p);

                    h2.setPosition(10, hd);

                    hd += h2.getRect().height() + 10;

                    play_draw.add(h2);
                }

                pAll.addTurnListener(ma);

                ma.FightStart();

                MainActivity.tabHost.getTabWidget().setVisibility(View.GONE);
            }
        }
    }

    /*******  以下为Event调用  *******/
	
	//进入战斗
	public void Fight(List<Enemy> c, boolean isc)
	{
		now_ene_mmi = null;
		
		is_map_ene = isc;
		
		if (c == null)return;
		
		if (c.size() < 1)return;
		
		//停止人物行走定时器
		runkey.removeCallbacks(keyrun);
		
		//战斗进行中标识
		isfight = true;
		
		//战斗界面绘制标识
		state = false;
		
		//敌人选中位置
		attack_target_index = 0;
		
		//显示战斗的按钮
		ma.showmapkey();
//		ma.showfightkey();
		
		//清除实体
		enemys.clear();
		
		plays.clear();
		
		enemy_draw.clear();
		
		play_draw.clear();
		
		Enemy e = null;
		
		for (Enemy ee : c)
		{
			ee.base = null;
			
			e = ee.CopyEnemy();
			
			e.NewBuffer();
			
			e.base.attack_num = 1;
			
			e.base.now_health = e.base.max_health;
			
			e.base.now_mp = e.base.max_mp;
			
			enemys.add(e);
		}

		//自己实体初始化
		BaseActivity.base.NewBuffer();
		
		BaseActivity.base.base.attack_num = 1;
		
		Player play = BaseActivity.base;

		if (play.base.now_health > play.base.max_health)
			play.base.now_health = play.base.max_health;
			
		if (play.base.now_mp > play.base.max_mp)
			play.base.now_mp = play.base.max_mp;

		plays.add(play);

		//初始化攻击管理器
		pAll = new PlayerAll();
		//我方攻击次数为1
		pAll.my_atk_num = 1;
		//pAll.enemy_atk_num = 1;
		
		pAll.addPlay(play);

		//for (Enemy ee : enemys)
		//把敌人添加进攻击管理器
		pAll.addEnemy(enemys);

		//设置战斗消息提示
		pAll.setTextItemDraw(tid = new TextItemDraw(this));
		
		//调用攻击管理器战斗开始
		pAll.onFightStart();
		
		//初始化战斗消息提示
		tid.setPosition(0, FIGHTVIEW_HEIGHT + 200);
		tid.setTextSize(50);
		tid.setTextItemMax(10);

		int hd = FIGHTVIEW_HEIGHT;
		
		//enemys_rect = new Rect[enemys.size()];
		
		EnemyHealthDraw h = null;
		
		for (Enemy ene : enemys)
		{
			h = new EnemyHealthDraw(ene);

			h.setPosition(getWidth() - (HealthDraw.hwidth + 10), hd);

			hd += h.getRect().height() + 40;

			//enemys_rect[i] = h.getRect();
			pAll.addAttackAnimationListener(h);

			enemy_draw.add(h);
		}
		hd = FIGHTVIEW_HEIGHT;
		
		HealthDraw h2 = null;
		
		for (Player p : plays)
		{
			h2 = new HealthDraw(p);
			h2.setPosition(10, hd);
			hd += h2.getRect().height() + 10;
			play_draw.add(h2);
		}
		
		pAll.addTurnListener(ma);
		
		ma.FightStart();
		
		MainActivity.tabHost.getTabWidget().setVisibility(View.GONE);
	}

    public void Fight(List<Enemy> c)
    {
        this.Fight(c, false);
    }
	
	private boolean is_test_fight;
	
	private int save_hp, save_mp;
	
	//测试战斗，不计算得失
	public void TestFight(List<Enemy> c){
		this.is_test_fight = true;
		save_hp = BaseActivity.base.base.now_health;
		save_mp = BaseActivity.base.base.now_mp;
		this.Fight(c, true);
	}
	
	//移动到指定地图
	public void MoveRoleMap(int id, int x, int y){
		map = BaseActivity.mapmanager.LoadMap(id);
		BaseActivity.map_action.map_id=id;
		BaseActivity.map_action.pos_x=x;
		BaseActivity.map_action.pos_y=y;
		initMapPosition();
		showmsg();
	}
	
	//移动到指定位置
	public void MoveRole(int x, int y){
		BaseActivity.map_action.pos_x=x;
		BaseActivity.map_action.pos_y=y;
		initMapPosition();
		showmsg();
	}
	
	//停止移动
	public void StopMove(){
		move = false;
		runkey.removeCallbacks(keyrun);
	}
	
	/*******  结束  ********/
	
	ListDialog repair_dialog;
	List<Weapon> repair_list;
	//ok键事件
	public void okevent(){
		for(MyMap.MapItem m : map.item){
			if(m.pos_x==BaseActivity.map_action.pos_x&m.pos_y==BaseActivity.map_action.pos_y){
				boolean flag = map.Event_Select(m, BaseActivity.map_action.pos_x, BaseActivity.map_action.pos_y);
				if(flag)
				switch(m.flag){
					case MyMap.MapItem.NPC_ORDINARY:
						dialogue(m);
						break;
					case MyMap.MapItem.NPC_SHOP_BUY:
						buy_goods(m);
						break;
					case MyMap.MapItem.NPC_SHOP_SELL:
						msgdialog("把精神探入你的储物空间吧(会有出售选项)");
						break;
						
					case MyMap.MapItem.EXIT:
						MyMap tmpmap = BaseActivity.mapmanager.ExitLoad(m, BaseActivity.map_action);
						if(tmpmap==null)break;
						this.map = tmpmap;
						BaseActivity.map_action.pos_x=m.next_map_pos_x;
						BaseActivity.map_action.pos_y=m.next_map_pos_y;
						initMapPosition();
						showmsg();
						break;
					
					case MyMap.MapItem.BUILD:
						if(m.word!=null && !m.word.equals(""))
							msgdialog(m.name, m.word);
					break;
					
					case MyMap.MapItem.REST:
					new MyDialog(getContext())
					.setMessage("是否要休息？")
							.setSelect("休息", new MyDialog.OnClickListener(){
								@Override
								public void onClick(MyDialog dialog)
								{
									msgdialog("休息好了，身体恢复如初！");
									BaseActivity.base.NewBuffer();
									BaseActivity.base.base.now_health=BaseActivity.base.base.max_health;
									BaseActivity.base.base.now_mp=BaseActivity.base.base.max_mp;
								}
							})
					.setCancel("算了",null)
					.show();
					break;
				
				case MyMap.MapItem.REPAIR:
					CreateReapairDialog();
					break;
				case MyMap.MapItem.GOODS:
				goods(m);
				break;
				
				}
				
				return;
			}
		}
	}
	
	//处理对话
	MyMap.MapItem now_dia_mi;
	void dialogue(MyMap.MapItem m){
		this.now_dia_mi = m;
		if(m.words != null){
			RoleDialog rd = new RoleDialog(getContext());
			for(RoleDialog.Content c : m.words)
				rd.addsay(repName((String)c.title), repName((String)c.message));
			rd.Show();
			return ;
		}
		
		String word=repName(m.word);
		new MyDialog(getContext())
			.SetTitle(m.name)
			.setMessage(word)
			//.setCancel("关闭",null)
			.show();
	}
	
	String repName(String str){
		if(str.indexOf("play") != -1)
			str = str.replace("play", BaseActivity.base.name);
		if(str.indexOf("role") != -1)
			str = str.replace("role", now_dia_mi.name);
		
		return str;
	}
	
	//宝箱查看
	int goods_num;
	MyMap.MapItem now_mi;
	void goods(MyMap.MapItem mi){
		now_mi=mi;
		final String key = "map_box_"+map.id+"_"+mi.pos_x+"_"+mi.pos_y;
		int istigger = MainActivity.event.get(key, 0);
		
		if(istigger > 0){
			msgdialog("这里已经查看过了");
			return;
		}
		List<Item> items=getgoodslist(mi);
		goods_num=0;
		for(Item it : items)
		goods_num+=it.quantity;
		
		new MyDialog(getContext())
		.SetTitle(mi.name)
		.setMessage("是否查看？")
			.setSelect("查看", new MyDialog.OnClickListener(){
				@Override
				public void onClick(MyDialog dialog)
				{
					if(goods_num<=0){
						msgdialog("什么也没有发现");
						return;
					}
					if(goods_num>BaseActivity.space.getSurplusNumber()){
						msgdialog("空间不足");
						return;
					}
					StringBuilder sb=new StringBuilder(now_mi.word);
					for(Item it : getgoodslist(now_mi))
						sb.append("\n获得 "+it.name+"x"+it.quantity);
						msgdialog(sb.toString());
						//now_mi.istigger=true;
						MainActivity.event.put(key, 1);
					for(MyMap.Goods gods : now_mi.goods)
						switch(gods.kind)
						{
							case Item.KIND_EQUIP:
								Weapon wp=BaseActivity.ScanWeapon(gods.name);
								if(wp == null)break;
								wp = wp.NewObject();
								wp.quality=gods.quality;
								wp.MaxDura();
								BaseActivity.space.addWeapon(wp);
								break;
							case Item.KIND_CONSUNABLES:
								Consunables con=BaseActivity.ScanConsunables(gods.name);
								if(con == null)break;
								con = con.NewObject();
								con.quantity=gods.quantity;
								BaseActivity.space.addConsunables(con);
								break;
							case Item.KIND_MATERIAL:
								Material mat=BaseActivity.ScanMaterial(gods.name);
								if(mat == null)break;
								mat = mat.NewObject();
								mat.quantity=gods.quantity;
								BaseActivity.space.addMaterail(mat);
						}
				}
			})
		.setCancel("算了",null)
		.show();
	}
	
	//商店购买
	List<MyMap.Goods> goods;

	MyMap.MapItem mmi;

	ListDialog buy_listdialog;

	ListDialog buy_choice_listdialog;

	List<Item> buy_now_items;

	void buy_goods(final MyMap.MapItem m){
		mmi = m;
		goods = m.goods;
		
		final List<MyImageAdapter.Item> items = new ArrayList<>();
		
		//final MyDialog md = com.xiuxian.chen.util.Loader.LoadDialog(getContext());

        final MyImageAdapter adapter = new MyImageAdapter(getContext());

		buy_listdialog = new ListDialog(getContext())
			.setTitle(m.name+"\n金币 "+BaseActivity.space.getMoney())
//			.setAdapter(adapter)
			.setHeight(MyILoadResource.SCREEN_HEIGHT/2)
			.setOnItemClickListener(buy_itemclick)
			.show();

        rx.Observable.create(new Observable.OnSubscribe<MyImageAdapter.Item>() {
            @Override
            public void call(Subscriber<? super MyImageAdapter.Item> subscriber) {
                for(Item i : (buy_now_items = getgoodslist(m)))
                    items.add(MyImageAdapter.toItem(i.icon, i.name+"(🔥"+i.buy_price+")"));
//                    subscriber.onNext(MyImageAdapter.toItem(i.icon, i.name+"(🔥"+i.buy_price+")"));
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MyImageAdapter.Item>() {
                    @Override
                    public void onCompleted() {
                        buy_listdialog.setImageListItem(items);
                    }
                    @Override
                    public void onError(Throwable throwable) {}
                    @Override
                    public void onNext(MyImageAdapter.Item item) {
//                        adapter.add(item);
                    }
                });

	}
	
	int buy_itemclick_index;

	NumberOperateDialog buy_numberoperatedialog;

	Item now_buy_goods;
	OnItemClickListener buy_itemclick = new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4)
		{
			buy_itemclick_index = p3;
			//Log.i("物品类型", buy_now_items.get(buy_itemclick_index).name+" - "+buy_now_items.get(buy_itemclick_index).kind);
			buy_choice_listdialog=new ListDialog(getContext())
			.setListItem(new String[]{"购买","详情"})
				.setOnItemClickListener(new OnItemClickListener(){
					@Override
					public void onItemClick(AdapterView<?> p1, View p2, int index, long p4)
					{
						switch(index){
							case 0:
							//MyMap.Goods gods=goods.get(buy_itemclick_index);
							
							Item now=buy_now_items.get(buy_itemclick_index);
								//Log.i("物品类型2级", now.name+" - "+now.kind);
								switch(now.kind)
								{
									case Item.KIND_EQUIP:
										if(BaseActivity.space.getSurplusNumber()<1){
											//Toast.makeText(getContext(), "空间已满！", 500).show();
											buy_sp_f();
											return;
										}
										Weapon wp=BaseActivity.ScanWeapon(now.name).NewObject();
										if(BaseActivity.space.getMoney()>=wp.buy_price){
											wp.quality=Weapon.QUALITY_INFELIOR;
											BaseActivity.space.addWeapon(wp);
											BaseActivity.space.addMoney(-wp.buy_price);
											buy_choice_listdialog.dismiss();
											//Toast.makeText(getContext(), "购买成功！", 500).show();
											buy_s_toast();
										}else {
											//Toast.makeText(getContext(), "金币不足！", 500).show();
											buy_f_toast();
											buy_choice_listdialog.dismiss();
										}
										
										break;
									case Item.KIND_CONSUNABLES:
									case Item.KIND_MATERIAL:
										buy_choice_listdialog.dismiss();
										if(BaseActivity.space.getSurplusNumber()<1){
											//Toast.makeText(getContext(), "空间已满！", 500).show();
											buy_sp_f();
											return;
										}
										now_buy_goods=BaseActivity.ScanConsunables(now.name);
										buy_numberoperatedialog=new NumberOperateDialog(getContext())
											.setInputNumberMax(BaseActivity.space.getSurplusNumber()<99 ? BaseActivity.space.getSurplusNumber() : 99)
											.setMessage("价格 "+now_buy_goods.buy_price)
											.setcancel("算了",null)
											.setok("购买", new MyDialog.OnClickListener(){
												@Override
												public void onClick(MyDialog dialog)
												{
													if(buy_now_items.get(buy_itemclick_index).kind==Item.KIND_CONSUNABLES){
													Consunables con=BaseActivity.ScanConsunables(buy_now_items.get(buy_itemclick_index).name).NewObject();
													int num=buy_numberoperatedialog.getInputNumber();
													num=(num<=0 ? 1 : num);
													con.quantity=num;
													int money=(con.buy_price*num);
													if(BaseActivity.space.getMoney()>=money){
													BaseActivity.space.addMoney(-money);
													BaseActivity.space.addConsunables(con);
													//Toast.makeText(getContext(), "购买成功！", 500).show();
													buy_s_toast();
													}else //Toast.makeText(getContext(), "金币不足！", 500).show();
													buy_f_toast();
													}else{
														Material mat=BaseActivity.ScanMaterial(buy_now_items.get(buy_itemclick_index).name).NewObject();
														int num=buy_numberoperatedialog.getInputNumber();
														num=(num<=0 ? 1 : num);
														mat.quantity=num;
														int money=(mat.price*num);
														if(BaseActivity.space.getMoney()>=money){
															BaseActivity.space.addMoney(-money);
															BaseActivity.space.addMaterail(mat);
															//Toast.makeText(getContext(), "购买成功！", 500).show();
															buy_s_toast();
														}else //Toast.makeText(getContext(), "金币不足！", 500).show();
														buy_f_toast();
													}
												}
											})
											.setInputWatcher(new NumberOperateDialog.InputTextWatcher(){
												@Override
												public void afterTextChanged(int i)
												{
													buy_numberoperatedialog.setMessage("价格 "+(now_buy_goods.buy_price*i));
												}
											})
										.show();
										break;
								}
							
							break;
							case 1:
							msgdialog(getExplain(buy_now_items.get(buy_itemclick_index)));
							buy_choice_listdialog.dismiss();
							break;
						}
					}
				})
			.show();
		}
	};
	
	void buy_s_toast(){
		toast("购买成功！");
		buy_listdialog.setTitle(mmi.name+"\n金币 "+BaseActivity.space.getMoney());
	}
	void buy_f_toast(){
		toast("金币不足！");
	}

	void buy_sp_f(){toast("空间已满");}
	
	String getExplain(Item g){
		
		switch(g.kind)
		{
			case Item.KIND_EQUIP:
				return BaseActivity.ScanWeapon(g.name).getExplain();
			case Item.KIND_CONSUNABLES:
				//Log.i("详细信息",g.name+" - "+g.kind);
				return BaseActivity.ScanConsunables(g.name).explain;
			case Item.KIND_MATERIAL:
				return BaseActivity.ScanMaterial(g.name).explain;
			default: return null;
		}
	}
	
	List<Item> getgoodslist(MyMap.MapItem m){
		List<Item> items=new ArrayList<>();
		for(MyMap.Goods g : m.goods){
			Item i=null;
			switch(g.kind)
			{
				case Item.KIND_EQUIP:
					i = BaseActivity.ScanWeapon(g.name);
				break;
				case Item.KIND_CONSUNABLES:
					i = BaseActivity.ScanConsunables(g.name);
					break;
				case Item.KIND_MATERIAL:
					i = BaseActivity.ScanMaterial(g.name);
					break;
			}
			if(i!=null){
				items.add(i);
				items.get(items.size()-1).quality=g.quality;
				items.get(items.size()-1).quantity=g.quantity;
			}
			}
			
		return items;
	}
	
	void CreateReapairDialog(){
		repair_dialog=new ListDialog(getContext())
			.setTitle("金币 "+BaseActivity.space.getMoney())
			//.setListItem(item)
			.setOnItemClickListener(repair_rp)
			.show();
		refreshRepairDialog();
	}
	
	//修理装备
	void refreshRepairDialog(){
		String[] item;
		repair_list=new ArrayList<>();
		for(Weapon wp : BaseActivity.base.equipment)
			if(wp!=null && wp.dura!=wp.getDuraMax())
			{
				repair_list.add(wp);
			}

		if(repair_list.size()>=1){
			item=new String[repair_list.size()+1];
			item[repair_list.size()]="全部修理";
			int i=0;
			for(Weapon wp : repair_list){
				int cost=getRepairCost(wp);
				item[i]=wp.name+"(费用："+cost+")";
				i++;
			}
			repair_dialog.setListItem(item);
		}else{
				repair_dialog.dismiss();
				msgdialog("没有装备可以修理！");
			}
	}
	
	int repair_cost;
	OnItemClickListener repair_rp=new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4)
		{
			if(p3==repair_list.size()){
				repair_cost=0;
				for(Weapon wp : repair_list){
					repair_cost+=getRepairCost(wp);
				}
				
				new MyDialog(getContext())
				.SetTitle("提示")
				.setMessage("是否全部修理？"+"\n费用："+repair_cost)
					.setSelect("全部修理", new MyDialog.OnClickListener(){
						@Override
						public void onClick(MyDialog dialog)
						{
							if(BaseActivity.space.getMoney()>=repair_cost){
								for(Weapon wp : repair_list)
								wp.MaxDura();
								repair_dialog.dismiss();
								//refreshRepairDialog();
								BaseActivity.space.addMoney(-repair_cost);
								BaseActivity.base.NewBuffer();
								MyToast.makeText(ma, "全部修理完成，欢迎再来！", Toast.LENGTH_SHORT).show();
							}else MyToast.makeText(ma, "小子，貌似你的钱不够啊！", Toast.LENGTH_SHORT).show();
						}
					})
				.setCancel("算了",null)
				.show();
			}else{
				Weapon wp=repair_list.get(p3);
				repair_cost=getRepairCost(wp);
				if(BaseActivity.space.getMoney()>=repair_cost){
					wp.MaxDura();
					//repair_dialog.dismiss();
					BaseActivity.space.addMoney(-repair_cost);
					BaseActivity.base.NewBuffer();
					refreshRepairDialog();
					MyToast.makeText(ma, "修理完成！", Toast.LENGTH_SHORT).show();
				}else MyToast.makeText(ma, "小子，貌似你的钱不够啊！", Toast.LENGTH_SHORT).show();
				
			}
			
		}
	};
	
	//获取装备修理费用
	int getRepairCost(Weapon wp){
		return
		(wp.getDuraMax()-wp.dura)
		*((wp.applylv/3 < 1 ? 1 : wp.applylv/3)+wp.quality+1);
	}
	
	//刷新当前地图
	public void refreshmap(){
		map = BaseActivity.mapmanager.getActionNowMap(BaseActivity.map_action);
	}
	
	//战斗界面
	public boolean state;
	
	//public boolean state2;
	
	int ss = 0;
	
	void fightDraw(Canvas c)
	{
		Paint p = new Paint();

			for (HealthDraw h : play_draw)
			{
				h.onDraw(c);
				String text = "";
				for (Buffer buf : h.getBuffers())
				{
					if(!buf.getEffect())continue;
					String buf_name = buf.name;
					text += ("[" + buf_name + buf.rounds + "]");
				}
				p.setTextSize(25);
				p.setColor(Color.YELLOW);
				Rect r = h.getTextWH(text, p);
				c.drawText(text, 10, (h.getY() + (h.getRect().height())) + r.height(), p);
			}
			
			int index = 0;
			
			for (HealthDraw h : enemy_draw)
			{
				if (index == attack_target_index)
				{
					h.ShowCursor(0);
				}
				else h.HideCursor();
				h.onDraw(c);

				String text="";
				for (Buffer buf : h.getBuffers())
				{
					if(!buf.getEffect())continue;
					String buf_name=buf.name;
					text += ("[" + buf_name + buf.rounds + "]");
				}
				p.setTextSize(25);
				p.setColor(Color.YELLOW);
				Rect r = h.getTextWH(text, p);
				c.drawText(text, (getWidth() - r.width()) - 10, (h.getY() + (h.getRect().height())) + r.height(), p);
				index++;
			}

			if (tid != null)tid.onDraw(c);

			if ((ss = pAll.FightState()) > 0)
			{
				if (!state)
				{
				state = true;
				new Handler().postDelayed(
						new Runnable(){
							@Override
							public void run()
							{
								fightresult();
							}
						}, 500);
				}
			}
			if (!isfightend && ss == 0)
			pAll.eneListAttack();
	}
	
	//测试战斗结果
	boolean testfightresult(){
		if(!is_test_fight) return false;
		
//		is_test_fight = false;

        isfightend = true;

        if (ss == 1) {
            pAll.SendMessage("战斗失败！", Color.RED);

        } else {
            pAll.SendMessage("战斗胜利！", Color.GREEN);
        }

        pAll.SendMessage("[点击退出]", Color.YELLOW);

		return true;
	}
	
	//战斗结束
	void fightresult()
	{
		if (testfightresult()) return;

        isfightend = true;

		StringBuffer sb = new StringBuffer("战斗胜利！");

		BaseActivity.base.CleafBuffer();

		for (Weapon wp : BaseActivity.base.equipment)
			if (wp != null)
				wp.dura -= wp.dura > 0 ? 1 : 0;

		int exp = 0;

		int money = 0;

		map.Event_Fightend(ss);

		//ss等于2，为战斗胜利
		if (ss == 2)
		{
			List<Item> items=new ArrayList<>();

			List<Enemy.GoodsAll> gods = new ArrayList<>();

			for (Enemy es : enemys)
			{
				exp += es.getexp;

				money += es.getMoney();

				Enemy.GoodsAll ga = es.Redult();

				gods.add(ga);

				for (Item i : ga.getList())
					items.add(i);
			}

			for (Enemy ene : pAll.enemys)
				BaseActivity.gtmanager.UpdateKillTask(ene.name, 1);

			for (Weapon wp : BaseActivity.base.equipment)
				if (wp != null && wp.buffer != null)
				{
					int[] exp_m = wp.buffer.fightend(new int[]{exp, money});

					exp = exp_m[0];

					money = exp_m[1];
				}
			sb.append("\n获得经验 ");

            sb.append(exp);

			sb.append(money <= 0 ? "" : "\n获得金钱 ");

            sb.append(money);

			BaseActivity.base.exp += exp;

			BaseActivity.space.addMoney(money);

			boolean islvup = BaseActivity.base.UpdateExp();

			if (islvup)sb.append("\n等级提升 LV " + BaseActivity.base.level);

			if (items.size() > 0)
			{
				sb.append("\n[获得物品]");

				for (Item i : items) {
                    sb.append("\n");
                    sb.append(i.name);
                    sb.append("x");
                    sb.append(i.quantity);
                }
				for (Enemy.GoodsAll g : gods)
					g.InSpace();
			} }
		else
		{
			//战斗失败，扣除经验。
			exp = (int)(BaseActivity.base.max_exp * 0.2f);

            money = (int) (BaseActivity.space.getMoney() * 0.1f);
			
			for (Weapon wp : BaseActivity.base.equipment)
				if (wp != null && wp.buffer != null)
				{
					int[] exp_m = wp.buffer.death(new int[]{exp, money});

					exp = exp_m[0];

					money = exp_m[1];
				}

		}

        if (ss == 1) {
            pAll.SendMessage("您已死亡！将被送往安全地带！", Color.RED);

        } else {
            pAll.SendMessage(sb.toString(), Color.GREEN);
        }

        pAll.SendMessage("[点击退出]", Color.YELLOW);
	}


	
	//脱离战斗状态
	void fightModelRelieve(){
		isfight = false;

        isfightend = false;

        isEnenFight = true;

		ma.showmapkey();

//		ma.showfightkey();

		refreshmap();

		MainActivity.tabHost.getTabWidget().setVisibility(View.VISIBLE);
	}
	
	//普通攻击
	int attack_target_index;
	public void attack(){
            if (pAll.my_atk_num > 0) {
                pAll.my_atk_num--;
                play_draw.get(0).right();
                Enemy ene = pAll.enemys.get(attack_target_index);
                if (ene.base.now_health <= 0) {
                    int i = 0;
                    for (Enemy e : pAll.enemys) {
                        if (e.base.now_health > 0) {
                            ene = e;
                            break;
                        }
                        i++;
                    }
                    attack_target_index = i;
                }
                //pAll.setAttackType(PlayerAll.ATTACK_COMMONLY);
                pAll.attack(pAll.players.get(0).base.atk,
                        pAll.players.get(0),
                        ene,
                        AttackType.getInstance(PlayerAll.ATTACK_COMMONLY, 0));
                pAll.enemy_atk_num += 1;
            }
	}
	
	//施放技能
	public void skill_attack(int index){
		if(pAll.my_atk_num < 1)return;
		final Skill sk=BaseActivity.base.skillment[index];
		if(sk==null)return ;
		sk.space=BaseActivity.space;
		int num=-1;
		final NumberOperateDialog nedit = 
			new NumberOperateDialog(getContext());
		switch(sk.type){
			default: break;
			case Skill.ACTIVE:
			skill_attack_response(sk);
			break;
			case Skill.ACTIVE_SPECIAL_SPECIFIED_NUMBER:
			nedit
			.setTitle("技能消耗物品数量")
			.setInputNumberMin(1)
					.setok("确定", new MyDialog.OnClickListener(){
						@Override
						public void onClick(MyDialog dialog)
						{
							sk.use_num=nedit.getInputNumber();
							skill_attack_response(sk);
						}
					})
			.setcancel("取消", null);
			num=skill_logic(sk);
			if(num>0){
				nedit.setInputNumberMax(num);
				nedit.show();
			}
			break;
		}
	}
	
	int skill_logic(final Skill sk){
		int num=0;
		switch(sk.consume_type){
			case Skill.USE_MONEY:
			if(sk.space.getMoney()>0)
				return sk.space.getMoney();
			break;
			case Skill.USE_CONSUNABLES:
			num=sk.space.getConsunablesNumber(sk.use_name);
			if(num>0)return num;
			break;
			case Skill.USE_MATERIAL:
			num=sk.space.getMaterialNumber(sk.use_name);
			if(num>0)return num;
			break;
		}
		return -1;
	}
	
	void skill_attack_response(final Skill sk){
		Enemy ene = pAll.enemys.get(attack_target_index);
		
		if(ene.base.now_health<=0){
			int i=0;
			for(Enemy e : pAll.enemys){
				if(e.base.now_health>0){ene=e; break;}
				i++;}
			attack_target_index=i;
		}
		if(!sk.satk.Judge(sk, pAll.players.get(0), ene)){
			pAll.SendMessage("无法发动！",Color.RED);
			return;
		}

		pAll.skill_attack(sk,
                pAll.players.get(0),
                ene,
                AttackType.getInstance(PlayerAll.ATTACK_SKILL, sk.hurt_type));
		
		sk.setNowCd(sk.getCd());
		
		pAll.my_atk_num--;
		
		pAll.enemy_atk_num++;
	}
	
	//计算敌我数值
	int fracion(Buffer b){
		int f=0;
		f+=b.now_health/10;
		f+=b.now_mp/15;
		f+=b.atk/2;
		f+=b.speed/2;
		f+=b.def;
		return f;
	}
	//逃跑
	public void escape(){
		if(is_map_ene){
			MyToast.makeText(ma, "无法逃跑！", Toast.LENGTH_SHORT).show();
			return;
		}
		float at=0.5f;
		int my=fracion(pAll.players.get(0).base);
		int ene=0;
		for(Enemy e : pAll.enemys)
		ene+=fracion(e.base);
		if(my!=ene){
			if(my>ene)
				at+=(((float)(my-ene))/100);
			else if(ene>my)
				at-=(((float)(ene-my))/100);
		}
		if(at<=0)at=0;  if(at>=1)at=1;
		//Toast.makeText(getContext(), ""+at, Toast.LENGTH_SHORT).show();
		if(new Random(System.currentTimeMillis()).nextInt(1001)<(at*1000)){
			fightModelRelieve();
			//Toast.makeText(getContext(), "逃跑成功！", Toast.LENGTH_SHORT).show();
		}else{
			pAll.enemy_atk_num+=1;
			//pAll.RoundBase();
            MyToast.makeText(ma, "逃跑失败!", Toast.LENGTH_SHORT).show();
			//Toast.makeText(getContext(), "逃跑失败！", Toast.LENGTH_SHORT).show();
		}
		
	}
	
	
	ListView fight_space_list;

	MyDialog fight_space_dialog;
	//战斗中打开空间消耗栏
	List<Integer> indexs = new ArrayList<>();
	public void fight_open_space(){
		//String[] item=new String[BaseActivity.space.consunables.size()];
		List<String> item = new ArrayList<>();
		
		indexs.clear();
		
		int i=0;
		
		for(Consunables con : BaseActivity.space.consunables)
		{
			if(con.road != Consunables.REST){
				
			item.add(con.name+"["+con.quantity+"]");
			
			indexs.add(i);
			}
			i++;
		}
		
		fight_space_list=new ListView(getContext());
		
		ButtonAdapter adapter=new ButtonAdapter(getContext(), item.toArray(new String[item.size()]), "使用");
		
		fight_space_list.setAdapter(adapter);
		
		adapter.setOnclickListener(new com.xiuxian.chen.adapter.OnClickListener(){
				@Override
				public void OnClick(int index)
				{
					Consunables con =
					BaseActivity.space
					.consunables.get(indexs.get(index));
					if(con.road == Consunables.REST){
						MyToast.makeText(ma, "无法在战斗中使用！", Toast.LENGTH_SHORT).show();
						return;
					}
					Enemy ene = pAll.enemys.get(attack_target_index);
					if(ene.base.now_health<=0){
						int i=0;
						for(Enemy e : pAll.enemys){
							if(e.base.now_health>0){ene=e; break;}
							i++;}
						attack_target_index = i;
					}

					//物品使用异常捕获
					try {
                        if (con.conuse.use(BaseActivity.space, pAll.players.get(0), ene, pAll)) {
                            fight_space_dialog.dismiss();
                            BaseActivity.space.takeout_consunables(indexs.get(index), 1);
                            pAll.my_atk_num -= 1;
                            pAll.enemy_atk_num += 1;
                        }
                    }catch (Exception e){
                        ErrorHandle.doError(e);
                    }
				}
			});
			
		adapter.setOnItemclickListener(new com.xiuxian.chen.adapter.OnClickListener(){
				@Override
				public void OnClick(int index)
				{
					//Log.i(LoadResources.TAG, "");
					msgdialog(
					BaseActivity.space
					.consunables.get(indexs.get(index)).explain);
				}
			});
			
		
		fight_space_dialog=
		new MyDialog(getContext())
		.SetTitle("空间")
		.setinitView(true)
		.setView(fight_space_list)
		.setCancel("关闭",null)
		.Show();
	}

	//点击事件
	int event_x, event_y;
	boolean is_click;
	int click_index;
	//long time;
	EventPost ep = new EventPost(550);
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		event_x = (int)event.getX();

		event_y = (int)event.getY();

        if (isfight & isfightend){
            FightHandle();
            return true;
        }
		
		if(!isfight & (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE)){
			int x = event_x/wt;

			int y = event_y/ht;

			if((x + (-MoveX)) == BaseActivity.map_action.pos_x
                    && (y + (-MoveY)) == BaseActivity.map_action.pos_y
                    && event.getAction() == MotionEvent.ACTION_DOWN){
				okevent();
			}else if (x < map.width && y < map.height){
                isAutoMove = true;
                auto_x = x + (-MoveX);
                auto_y = y + (-MoveY);
            }

			return true;
		}
		else
		if (event.getAction() == MotionEvent.ACTION_DOWN & isfight)
		{
			for (int i=0, length = enemy_draw.size();i < length;i++)
				if (enemy_draw.get(i).getRect().contains(event_x, event_y) & enemys.get(i).base.now_health > 0)
				{
                    timer2.reset();

					is_click = true;

					click_index = i;

					break;
				}
		}
		else
		if (event.getAction() == MotionEvent.ACTION_UP & isfight & is_click)
		{
			if (attack_target_index == click_index)
				attack();
			else
				attack_target_index = click_index;
		}

		if (event.getAction() == MotionEvent.ACTION_UP){
            is_click = false;
        }

		return true;
	}

	void FightHandle(){
        if (is_test_fight){
            is_test_fight = false;

            Buffer buf = BaseActivity.base.NewBuffer();

            BaseActivity.base.CleafBuffer();

            buf.now_health = save_hp;

            buf.now_mp = save_mp;
        }
         else
        if (ss == 1)
        {
            BaseActivity.map_action.setPosition(0, 0);

            BaseActivity.map_action.map_id = map.afresh_map;

            BaseActivity.base.base.now_health = 1;

            initMapPosition();
        }
        else
        if (is_map_ene & now_ene_mmi != null)
        {
            //储存固定遭遇战斗
            MainActivity.event.put("map_ene_" + map.id + "_" + now_ene_mmi.pos_x + "_" + now_ene_mmi.pos_y, 1);
        }

        fightModelRelieve();
    }
	
	void toast(String text){
		MyToast.makeText(ma, text, Toast.LENGTH_SHORT, Gravity.CENTER).show();
	}
	
}
