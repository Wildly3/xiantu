package com.xiuxian.chen.set;
import com.xiuxian.chen.view.*;
import java.util.*;
import java.util.concurrent.*;

public class Enemy extends Player implements Cloneable
{
	//出现几率
	public float appearodds;
	//出现数量
	public int appearodds_num;
	
	public int getexp;
	
	public int money_low;
	
	public int money_hight;
	
	public List<Chance> gods;
	
	public Enemy(){
		super();
		enemy = 1;
		gods=new ArrayList<>();
		super.isrebuff = true;
		super.Initattr();
		eminit();
	}

	public void setRace(int race){
        this.race = race;
        super.Initattr();
    }

    public void setAptitude(int a, int b, int c, int d, int e){
        aptitude[0] = a;
        aptitude[1] = b;
        aptitude[2] = c;
        aptitude[3] = d;
        aptitude[4] = e;
    }

	//自动计算怪物掉落
	public void eminit(){
		this.getexp = fracion(this)*2;
		int money = fracion(this);
		this.money_low = money/2;
		this.money_hight = money + 1;
	}
	
	//计算人物战斗数值
	private int fracion(Player b){
		int f=0;
		f+=b.now_health/10;
		f+=b.now_mp/15;
		f+=b.atk/2;
		f+=b.speed/2;
		f+=b.def;
		return f;
	}
	
	//设置等级，这会自动根据等级分配属性
	public void IncareaseLv(int lv){
		super.exp=0;
		super.attrs+=5*lv;
		super.health+=getfi(APTITUDE_RACE[race][0])*lv;
		super.mp+=getfi(APTITUDE_RACE[race][1])*lv;
		super.atk+=getfi(APTITUDE_RACE[race][2])*lv;
		super.def+=getfi(APTITUDE_RACE[race][3])*lv;
		super.speed+=getfi(APTITUDE_RACE[race][4])*lv;
		level+=lv;

        AptitudeUp(lv);

        switch (race){
            case RACE_PEOPLE:
                add(lv * 1, lv * 2, lv * 1, lv * 1);
                break;
            case RACE_BEAST:
                add(lv * 2, lv * 1, lv * 1, lv * 1);
                break;
            case RACE_MONSTER:
                add(0, lv * 3, lv * 1, lv * 1);
                break;
            case RACE_GHOST:
                add(lv * 1, lv * 1, 0, lv * 3);
                break;
        }

        //废弃该计算方式
//		 for(int i=0;i<lv;i++){
//			 int rand=(new Random(System.currentTimeMillis()).nextInt(1001));
//			 if(rand>0&rand<=250){
//				 super.health+=3;
//			 }else if(rand>250&rand<=500){
//				 super.atk+=2;
//				 super.mp+=2;
//			 }else if(rand>500&rand<=750){
//				 super.health+=1;
//				 super.def+=1;
//			 }else if(rand>750){
//				 super.speed+=2;
//			 }
//
//		 }
		 eminit();
	}

	private void add(int power, int mp, int def, int speed){
        this.health += (power * 3) + def;
        this.mp += mp * 2;
        this.atk += mp * 2;
        this.def += def;
        this.speed += speed * 2;
    }

	List<String> WearEquipNamse;
	
	List<String> WearSkillNmaes;
	
	//穿戴装备
	public void WearEquip(String name){
		if(WearEquipNamse == null) WearEquipNamse = new ArrayList<>();
		this.WearEquipNamse.add(name);
	}
	
	//穿戴技能
	public void WearSkill(String name){
		if(WearSkillNmaes == null) WearSkillNmaes = new ArrayList<>();
		this.WearSkillNmaes.add(name);
	}
	
	public List<String> getWearEquip(){
		return this.WearEquipNamse;
	}
	
	public List<String> getWearSkill(){
		return this.WearSkillNmaes;
	}
	
    @Override
    protected Enemy clone() throws CloneNotSupportedException {
        return (Enemy) super.clone();
    }

    //复制一个enemy对象
	public Enemy CopyEnemy(){
		Enemy ene=null;
		
        try {
            ene = this.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
		
		
		if(WearEquipNamse != null)
		for(String equip : WearEquipNamse){
			Weapon wp = BaseActivity.ScanWeapon(equip);
			if(wp != null)
				ene.equipment[wp.type] = wp.NewObject().MaxDura();
		}
		
		
		if(WearSkillNmaes != null)
		for(String skill : WearSkillNmaes){
			addSkill2(skill);
		}
		
		ene.buffer = new Vector<>();
		
		ene.getexp = getexp;
		
		ene.money_low = money_low;
		
		ene.money_hight = money_hight;
		
		ene.gods = gods;
		
		return ene;
	}
	
	//设置技能空位，这个设置会清空所有原有技能
	public void setSkillMax(int max){
		super.skillment = new Skill[max];
	}
	
	//添加一个技能，已经有的技能则不会添加
	private void addSkill2(String name){
		if(SkillisEmpty(name))return;
		Skill sk = BaseActivity
		.ScanSkill(name)
		.newObject();
		if(sk==null)return ;
		for(int i=0;i<skillment.length;i++)
			if(skillment[i] == null){
				skillment[i] = sk;
				break;
			}
	}
	
	//判断一个技能是否存在
	private boolean SkillisEmpty(String name){
		for(Skill sk : skillment)
		if(sk != null)
			if(name.equals(sk.name)) return true;
		
		return false;
	}
	
	//获取掉落金币
	public int getMoney(){
		return getInt(money_low, money_hight);
	}
	
	private static int getInt(int l, int h){
		int h2=(h-l)+1;
		h2=h2<1 ? 1 : h2;
		return (l+new Random().nextInt(h2));
	}
	
	//添加掉落物品
	public void addGod(Chance c){
		gods.add(c);
	}
	
	//
	public GoodsAll Redult(){
		GoodsAll ga = new GoodsAll();
		
		for(Chance c : gods)
		ga.addGoods(c);
		
		return ga;
	}
	
	public static class Chance
	{
		//几率1000分之
		public float chance;
		
		public String name;
		
		public int type;
		
		public int quanlity_low;
		
		public int quanlity_hight;
		
		public Chance(String name, int type){
			this.name=name;
			this.type=type;
		}
	}
	
	public static boolean odds(float o){
		int a=(new Random().nextInt(1000));
		if(a<(o*1000))return true;
		return false;
	}
	
	public static class GoodsAll{
		public List<Weapon> weapons;
		
		public List<Consunables> cons;
		
		public List<Material> mats;
		
		public GoodsAll(){
			weapons=new ArrayList<>();
			cons=new ArrayList<>();
			mats=new ArrayList<>();
		}
		
		public void addGoods(Chance c)
		{
			if (odds(c.chance))
				switch (c.type)
				{
					case Item.KIND_EQUIP:
						Weapon wp=ScanWeapon(c.name);
						
						final float[] at={
							0.40f,
							0.40f,
							0.15f,
							0.05f
						};

						boolean[] is = new boolean[at.length];
						
						//float at2 = 0;
						
						int ind = 0;
						
						for (int i=0;i < at.length;i++)
						{
							is[i] = odds(at[i]);
						}
						
						for (int i=0;i < at.length;i++)
						if(is[i])
						ind = i;
						
						/*
						原来的爆率算法
						for (int i=0;i < at.length;i++)
							if (is[i] && at[i] >= at2)
							{
								ind = i;
								at2 = at[i];
							}
						*/
						
						wp.quality = ind;
						
						weapons.add(wp);
						
						break;
					case Item.KIND_CONSUNABLES:
						Consunables con = ScanConsunables(c.name);
						
						con.quantity = getInt(c.quanlity_low, c.quanlity_hight);
						
						cons.add(con);
						break;
					case Item.KIND_MATERIAL:
						Material mat=ScanMaterial(c.name);
						
						mat.quantity = getInt(c.quanlity_low, c.quanlity_hight);
						
						mats.add(mat);
						break;
				}

		}
		
		//掉落物品添加至空间
		public void InSpace()
		{
			for (Weapon wp :  weapons)
				if (BaseActivity.space.getSurplusNumber() > 0)
				{
					wp.MaxDura();
					BaseActivity.space.addWeapon(wp.NewObject());
				}
			for (Consunables con : cons)
				if (BaseActivity.space.getSurplusNumber() >= con.quantity)
					BaseActivity.space.addConsunables(con.NewObject());
					
			for (Material mat : mats)
				if (BaseActivity.space.getSurplusNumber() >= mat.quantity)
					BaseActivity.space.addMaterail(mat.NewObject());
		}
		
		public List<Item> getList(){
			List<Item> item=new ArrayList<>();
			
			for(Weapon wp :  weapons)
			item.add(wp);
			
			for(Consunables con : cons)
			item.add(con);
			
			for(Material mat : mats)
			item.add(mat);
			
			return item;
		}
		
		Weapon ScanWeapon(String name){
			return BaseActivity.ScanWeapon(name);
		}
		
		Consunables ScanConsunables(String name){
			return BaseActivity.ScanConsunables(name);
		}
		
		Material ScanMaterial(String name){
			return BaseActivity.ScanMaterial(name);
		}
		
		
	}
	
	
	
}
