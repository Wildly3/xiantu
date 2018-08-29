package com.xiuxian.chen.set;
import java.util.*;
import android.util.*;
import com.xiuxian.chen.include.*;
import android.graphics.*;
import android.os.*;
import com.xiuxian.chen.*;

public class PlayerAll implements Runnable
{
	public List<Player> players;
	
	public List<Enemy> enemys;
	
	private TextItemDraw tid;
	
	private List<AttackAnimationListener> anims;
	
	private List<TurnListener> Turns;
	
	//private Handler handle;
	
	//攻击延迟
	private static final int ATTACK_DELAY = 350;
	
	//我方攻击次数
	public int my_atk_num=0;
	//敌人攻击次数
	public int enemy_atk_num=0;
	
	private Bundle tag;
	//攻击类型状态标识
	public static final String ATTACK_TYPE_FLAG="attack_type";
	
	//攻击类型
	public static final String
	ATTACK_NULL="",
	
	ATTACK_COMMONLY="commonly",  //普通攻击
	
	ATTACK_CONSUNABLES="consunables",  //消耗品的攻击
	
	ATTACK_SKILL="skill",  //技能攻击
	
	ATTACK_BUFF="buffer";//buff减益伤害
	
	private int Before_Atk;
	
	private int After_Atk;
	
	/***过滤效果标识***/
	
	//过滤自己的装备技能效果
	public static final String FILTRATION_MY = "<FILTRATION_MY>";
	
	//过滤对方的装备技能效果
	public static final String FILTRATION_ENE = "<FILTRATION_ENE>";
	
	//过滤防御力,,,造成真实伤害
	public static final String FILTRATION_REAL = "<FILTRATION_REAL>";

    //无法闪避
    public static final String FILTRATION_ACCURATE = "<FILTRATION_ACCURATE>";
	
	public PlayerAll(){
		this.players=new ArrayList<Player>();
		this.enemys=new ArrayList<Enemy>();
		this.tag=new Bundle();
		//this.handle = new Handler();
	}

	public void setAfterAtk(int after_Atk)
	{
		After_Atk = after_Atk;
	}

	public int getAAtk()
	{
		return After_Atk;
	}

	private void setBeforeAtk(int before_Atk)
	{
		Before_Atk = before_Atk;
	}

	//获取没有经过防御装逼等计算前的攻击力
	public int getBAtk()
	{
		return Before_Atk;
	}
	
	public void addPlay(Player p){
		p.base.index=players.size();
		for(int i=0;i<p.skillment.length;i++){
			Skill sk = p.skillment[i];
			if(sk != null)
				sk.setNowCd(0);
		}
		this.players.add(p);
	}
	
	public void addEnemy(Enemy e){
		e.base.index=enemys.size();
		
		for(int i=0;i<e.skillment.length;i++){
			Skill sk = e.skillment[i];
			if(sk != null)
				sk.setNowCd(0);
		}
		
		this.enemys.add(e);
	}
	
	public List<Enemy> getEnemys(){
		return this.enemys;
	}
	
	public void addEnemy(List<Enemy> e){
		for(Enemy ee : e)
		for(int i=0;i<ee.skillment.length;i++){
			Skill sk = ee.skillment[i];
			if(sk != null)
				sk.setNowCd(0);
		}
		
		this.enemys.addAll(e);
	}
	
	public void setTextItemDraw(TextItemDraw tid){
		this.tid=tid;
	}
	
	public void addAttackAnimationListener(AttackAnimationListener anim){
		if(anims == null) anims = new ArrayList<>();
		this.anims.add(anim);
	}
	
	//添加回合监听
	public void addTurnListener(TurnListener turn){
		if(Turns == null) Turns = new ArrayList<>();
		Turns.add(turn);
	}

    /**
     * 攻击
     * @param atk 攻击力
     * @param my 攻击者对象
     * @param ene 被攻击者对象
     * @param type 攻击类型
     * @return 被攻击者收到的真正伤害
     */
    private int base_attack(int atk, Player my, Player ene, AttackType type)
	{
        String atktype = type.flag;

        try {
            if (atktype == null || atktype.equals(""))
                return 0;

            boolean filtration_my = false;

            boolean filtration_ene = false;

            boolean filtration_real = false;

            boolean filtration_accurate = false;

            if (atktype.indexOf(FILTRATION_MY) != -1) {
                atktype = atktype.replace(FILTRATION_MY, "");
                filtration_my = true;
            }

            if (atktype.indexOf(FILTRATION_ENE) != -1) {
                atktype = atktype.replace(FILTRATION_ENE, "");
                filtration_ene = true;
            }

            if (atktype.indexOf(FILTRATION_REAL) != -1) {
                atktype = atktype.replace(FILTRATION_REAL, "");
                filtration_real = true;
            }

            if (atktype.indexOf(FILTRATION_ACCURATE) != -1) {
                atktype = atktype.replace(FILTRATION_ACCURATE, "");
                filtration_accurate = true;
            }

            int atk_temp = atk;

            int attr_temp = 0;

            switch (type.hurt_type){
                default:break;
                case Player.METAL:
                    attr_temp = my.base.metal - ene.base.metal_resist;
                    break;
                case Player.WOOD:
                    attr_temp = my.base.wood - ene.base.wood_resist;
                    break;
                case Player.WATER:
                    attr_temp = my.base.water - ene.base.water_resist;
                    break;
                case Player.FIRE:
                    attr_temp = my.base.fire - ene.base.fire_resist;
                    break;
                case Player.SOIL:
                    attr_temp = my.base.soil - ene.base.soil_resist;
                    break;
            }

            if (attr_temp > 0){
                float bl = (float)attr_temp * 0.002f;
                atk = (atk_temp + (int)(atk_temp * bl) );
            } else if (attr_temp < 0){
                attr_temp = Math.abs(attr_temp);
                float bl = (float)attr_temp * 0.002f;
                atk = (atk_temp - (int)(atk_temp * bl) );
                atk = atk < 1 ? 1 : atk;
            }

            setBeforeAtk(atk);

            setAfterAtk((atk - ene.base.def) < 1 ? 1 : atk - ene.base.def);

            if (!filtration_accurate && atktype != null && !atktype.equals(ATTACK_BUFF))
                if (my.base != null & ene.base != null) {
                    boolean is_dodge = dodge(my.base.speed, ene.base.speed);
                    if (!is_dodge) {
                        SendMessage(ene.name + "闪避了" + my.name + "的攻击", Color.WHITE);

                        return -1;
                    }
                }

            if (!filtration_my) {
                for (Skill sk : my.skillment)
                    if (sk != null && sk.spa != null) {
                        //Log.i("技能攻击调用次数", ""+(++num));
                        sk.setPall(this);

                        atk = sk.spa.attack(atk, sk, my, ene, atktype);
                    }

                for (Weapon wp : my.equipment)
                    if (wp != null && wp.buffer != null) {
                        if (wp.dura > 0)
                            atk = wp.buffer.attack(atk, my, ene, this, atktype);
                    }

            }

            if (!filtration_ene) {

                for (Weapon wp : ene.equipment)
                    if (wp != null && wp.buffer != null)
                        if (wp.dura > 0)
                            atk = wp.buffer.beattack(atk, my, ene, this, atktype);

                for (Skill sk : ene.skillment)
                    if (sk != null && sk.spa != null) {
                        sk.setPall(this);
                        atk = sk.spa.beingattacked(atk, sk, my, ene, atktype);
                    }

            }

            if (!filtration_real) {
                atk = (atk - ene.base.def) < 1 ? 1 : atk - ene.base.def;

                atk = ene.base.SubtractHealth(atk);
            } else {
                ene.now_health = (ene.now_health - atk < 0 ? 0 : ene.now_health - atk);
            }

            return atk;
        }catch (Exception e){
            ErrorHandle.doError(e);
            return -1;
        }
	}
	
	//速度闪避计算
	private boolean dodge(int a, int b){
		int c = 100 - (a>=b ? 0 : Math.abs(a-b)) / 5;
		return Util.odds((float)c/100);
		//return false;
	}
	
	private void buffrefresh(Player my)
	{
		if(my.skillment != null && my.skillment.length > 0)
		for(Skill sk : my.skillment){
			if(sk != null && sk.getNowCd() > 0)
				sk.setNowCd(sk.getNowCd() - 1);
		}
		
		try
		{
			Iterator<Buffer> it = my.buffer.iterator();

			Buffer buf = null;

			while (it.hasNext())
			{
				buf = it.next();

				buf.Rounds(this);
			}
		}
		catch (ConcurrentModificationException e)
		{
			SendMessage("警告：集合迭代出错！！！", Color.RED);
			buffrefresh(my);
		}

	}
	
	public int attack(int atk, Player my, Player ene, AttackType atktype){
		return this.attack(atk, my, ene, Color.WHITE, atktype);
	}
	
	public int attack(int atk, Player my, Player ene, int color, AttackType atktype){
		int hurt = this.base_attack(atk, my, ene, atktype);
		if(hurt != -1 && tid!=null){
			TextItemDraw.Text t = new TextItemDraw.Text();
			t.text=my.name+"→"+ene.name+"造成["+hurt+"]点伤害";
			t.color=color;
			tid.addText(t);
			}
		return hurt;
	}
	
	public int attack(int atk, Player my, Player ene, String msg, int color, AttackType atktype){
		int hurt = this.base_attack(atk, my, ene, atktype);
		if(hurt != -1 && tid!=null){
			TextItemDraw.Text t = new TextItemDraw.Text();
			t.text=msg.indexOf("atk")!=-1 ? msg.replace("atk",""+hurt) : msg;
			t.color=color;
			tid.addText(t);
		}
		return hurt;
	}
	
	//技能攻击
	public void skill_attack(Skill sk, Player my, Player ene, AttackType atktype){
		sk.setPall(this);
		int atk=sk.satk.attack(sk,my,ene);
		if(atk == -1){
			//BuffRefresh();
			//RoundPlay(my);
			return ;
		}
		int hurt = this.base_attack(atk, my, ene, atktype);
		if(hurt != -1 && tid!=null){
			TextItemDraw.Text t = new TextItemDraw.Text();
			t.text=my.name+" ["+sk.name+"]→"+ene.name+"造成["+hurt+"]点伤害";
			t.color=Color.BLUE;
			tid.addText(t);
		}
	}
	
	//效果伤害
	public void buff_attack(Player target, Buffer holder, int atk){
		int hurt = base_attack(atk, new Player(), target, AttackType.getInstance(ATTACK_BUFF, holder.hurt_type));
		
		if(tid!=null){
			TextItemDraw.Text t = new TextItemDraw.Text();
			t.text = holder.getTargetName()+"受到"+holder.name+"效果["+hurt+"]点伤害";
			if(holder.color == -1)
			t.color=Color.rgb(255,192,203);
			else t.color = holder.color;
			tid.addText(t);
		}
	}
	
	public void SendMessage(String str, int color){
		if(tid!=null){
			TextItemDraw.Text t = new TextItemDraw.Text();
			t.text = str;
			t.color = color;
			tid.addText(t);
		}
	}
	
	//战斗开始触发装备效果
	public void onFightStart()
	{
		//int i=0;
		for (Player p : players)
		{
			p.setAll(this);
			
			for (Skill sk : p.skillment)
				if (sk != null)
				{
					//初始化所有技能的cd
					//Log.i("技能数量", sk.name+":"+(++i));
					if (sk.spa != null)
						sk.spa.onstartfight(p, (List)enemys);
				}

			for (Weapon wp : p.equipment)
				if (wp != null && wp.buffer != null)
					wp.buffer.onstartfight(p, (List)enemys);
		}

		for (Enemy e : enemys)
		{
			e.setAll(this);
			
			for (Skill sk : e.skillment)
			if(sk != null){
				//sk.setNowCd(0);
				
				if (sk.spa != null)
					sk.spa.onstartfight(e, (List)enemys);
				}


			for (Weapon wp : e.equipment)
				if (wp != null && wp.buffer != null)
					wp.buffer.onstartfight(e, players);
		}
	}
	
	//属性刷新
	public void BuffRefresh()
	{
		Buffer buf = null;

		for (Player p : players)
		{
			if (p.base.now_health > 0)
			{
				Iterator<Buffer> it = p.buffer.iterator();

				while (it.hasNext())
				{
					buf = it.next();

					if (buf.rounds <= 0 & buf.getEffect())
					{
						switch (buf.type)
						{
							default: break;
							case Buffer.PROMOTE:
							case Buffer.REDUCE:
								buf.onDestory(p);
								break;}
						buf.setEffect(false);
						it.remove();
					}
				}
			}
		}

		for (Enemy e : enemys)
		{
			if (e.base.now_health > 0)
			{
				Iterator<Buffer> it = e.buffer.iterator();

				buf = null;

				while (it.hasNext())
				{
					buf = it.next();

					if (buf.rounds <= 0 & buf.getEffect())
					{
						switch (buf.type)
						{
							default: break;
							case Buffer.PROMOTE:
							case Buffer.REDUCE:
								buf.onDestory(e);
								break;}
						buf.setEffect(false);
						it.remove();
					}
				}
			}
		}
	}
	
	//当前战斗状态，0为正常战斗状态，1为己方全部死亡，2为敌方全部死亡
	public int FightState(){
		int a=0;
		for(Player p : players)
		if(p.base.now_health>0)a++;
		if(a<=0)return 1;
		
		a=0;
		for(Enemy e : enemys)
		if(e.base.now_health>0)a++;
		if(a<=0)return 2;
		
		return 0;
	}
	
	int numm;
	
	//敌人攻击
	public void eneListAttack()
	{
		if (enemy_atk_num > 0)
		{
			if (!isrepat)
			{
				time = System.currentTimeMillis();
				
				isrepat = true;

				index = 0;
			}
			
			DelayAttack();
		}
	}
	
	private boolean isrepat;
	
	private void DelayAttack(){
		run();
	}
	
	private int index;
	
	private long time;
	
	@Override
	public void run()
	{
		if ((System.currentTimeMillis() - time) >= ATTACK_DELAY & isrepat)
		{
			time = System.currentTimeMillis();
			
			Enemy e = enemys.get(index);

			if (e.base.now_health > 0 & e.base.attack_num > 0)
			{
				if (anims != null)
					if (index <= anims.size() - 1)
						anims.get(index).anim(index);
						
				EnemyAttack(e);
			}
			//else if (e.base.attack_num < 1) RoundEne(e);

			index ++;

			if (index > enemys.size() - 1)
			{
				Turn();
				
				isrepat = false;

				if (players.get(0).base.attack_num > 0)
				{
					enemy_atk_num--;

					my_atk_num++;
				}
				//else RoundBase();

				return ;
			}
		}
	}
	
	//回合
	public void Turn(){
		buffrefresh(players.get(0));
		
		Iterator<Enemy> iterator = enemys.iterator();
		while(iterator.hasNext()){
			Enemy e = iterator.next();
			if(e.base.now_health > 0)
			buffrefresh(e);
		}
		
		BuffRefresh();
		
		if(Turns != null && Turns.size() > 0)
			for(TurnListener turn : Turns)
			turn.Turn(players);
	}
	
	private void EnemyAttack(Enemy e)
	{
		//boolean attack = Enemy.odds(0.5f);
		boolean skill = Enemy.odds(0.35f);

		if (skill)
		{
			List<Skill> sk = getPassiveSkill(e);
			if (sk.size() < 1)
			{
				CommonAttack(e);
				return ;
			}
			int rand = new Random().nextInt((sk.size() * 100) + 100);
			int now = rand / 100;
			if (now > sk.size() - 1) now = sk.size() - 1;
			Skill sk1 = sk.get(now);
			Player play = players.get(0);
			if (sk1.satk != null && sk1.satk.Judge(sk1, e, play) && sk1.getNowCd() < 1)
			{
				sk1.setNowCd(sk1.getCd());
				
				skill_attack(sk1, e, play, AttackType.getInstance(ATTACK_SKILL, sk1.hurt_type));
			}
			else CommonAttack(e);

		}
		else CommonAttack(e);
	}
	
	private void CommonAttack(Enemy e){
		attack(e.base.atk, e, players.get(0), Color.RED, AttackType.getInstance(PlayerAll.ATTACK_COMMONLY, 0));
	}
	
	private List<Skill> getPassiveSkill(Enemy e){
		List<Skill> sk = new ArrayList<>();
		for(Skill s : e.skillment){
			if(s != null && s.type == Skill.ACTIVE){
				sk.add(s);
			}
		}
		
		return sk;
	}
	
	/*
	public void RoundBase(){
		for(Skill sk : players.get(0).skillment){
			if(sk.getNowCd() > 0)
				sk.setNowCd(sk.getNowCd() - 1);
		}
		
		RoundPlay(players.get(0));
	}
	
	//敌人回合循环buff以及cd的计算
	private void RoundEne(Enemy ene){
		for(Skill sk : ene.skillment){
			if(sk.getNowCd() > 0)
				sk.setNowCd(sk.getNowCd() - 1);
		}
		
		RoundPlay(ene);
	}
	
	private void RoundPlay(Player p){
		buffrefresh(p);
		BuffRefresh();
	}
	*/
	
	//获取当前包装数据
	public Bundle getTag(){
		return this.tag;
	}
	
	//以下插入获取数据
	public void put(String key, String value){
		this.tag.putString(key, value);
	}
	
	public void put(String key, int value){
		this.tag.putInt(key, value);
	}
	
	public String get(String key){
		return this.tag.getString(key, "");
	}
	
	public int getInt(String key){
		return this.tag.getInt(key, 0);
	}
	
	public void setAttackType(String action){
		put(ATTACK_TYPE_FLAG, action);
	}
	
	public String getAttackType(){
		return get(ATTACK_TYPE_FLAG);
	}
	
	//获取事件
	public Event getEvent(){
		return MainActivity.event;
	}
	
	private Item tmp_item;
	
	public void setGood(Item item){
		this.tmp_item = item;
	}
	
	public Item getGood(){
		return tmp_item;
	}
	
	public static interface AttackAnimationListener
	{
		public void anim(int index);
	}
	
}
