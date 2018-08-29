package com.xiuxian.chen.other;
import com.xiuxian.chen.set.*;
import java.util.*;

public class GOODS
{
	public static List<ExChangeGoodsAll> ECGS;
	
	static{
		ECGS=new ArrayList<ExChangeGoodsAll>();
		ExChangeGoods ec;
			ECGS.add(new ExChangeGoodsAll("长亭外", new ExChangeGoods("破败血刃",Item.KIND_EQUIP,Item.QUALITY_NONSUCH)));
			
			ec=new ExChangeGoods(ExChangeGoods.EXP);
			ec.exp=1500;
			ECGS.add(new ExChangeGoodsAll("古道边", ec));
			
		ec=new ExChangeGoods(ExChangeGoods.EXP);
		ec.exp=1000000;
		ECGS.add(new ExChangeGoodsAll("exp666", ec));
	}
	
	
}
