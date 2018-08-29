package com.xiuxian.chen.set;

//物品
public class Item
{
	//物品类型
	public transient static final int
	KIND_NULL = -1,//无
	KIND_EQUIP=0,//装备
	KIND_CONSUNABLES=1,//消耗品
	KIND_MATERIAL=2,//材料
	KIND_SKILL=3,//技能
	KIND_ENEMY=4,//敌人
    KIND_MONEY = 5;//金币

    //品质字符
    public transient static final String[] QUAILTY = {"下品", "中品", "上品", "极品"};

	//品质
	public transient static final int
	QUALITY_INFELIOR=0,//下品
	QUALITY_MEDIUM=1,//中品
	QUALITY_FIRST_CLASS=2,//上品
	QUALITY_NONSUCH=3;//极品
	
	//品质质量，数值百分比
	private transient static final float
	A=1.7f,
	B=1.5f,
	C=1.2f,
	D=1.0f;
	
	//名称
	public String name;
	//图标
	public transient int icon;
	//说明
	public transient String explain;
	//类型
	public int kind;
	//品质
	public int quality;
	//使用等级
	public int applylv;
	//耐久
	public int dura;
	//总耐久
	public int duramax;
	//数量
	public int quantity;
	//出售价格
	public int price;
	//购买价格
	public int buy_price;
	
	public Item(){
		this.quantity=1;
	}
	
	public Item(String name, int kind){
		this(kind);
		this.name = name;
	}
	
	public Item(int kind){
		this();
		this.kind = kind;
	}
	
	public String getExplain(){
		return this.explain;
	}

    public static String getQualityString(int quality)
    {
        if(quality > QUAILTY.length - 1) return "?";
        return QUAILTY[quality];
    }

    //获取品质字符
	public String getQualityString()
    {
        if(quality > QUAILTY.length - 1) return "?";
        return QUAILTY[this.quality];
    }
	
	//获取品质数值
	public int getQuality(int n){
        if (n < 1) return 0;
		switch(this.quality)
		{
			default: return (int)((float)n*C);

			case QUALITY_INFELIOR: return (int)((float)n*D);

			case QUALITY_FIRST_CLASS: return (int)((float)n*B);

			case QUALITY_NONSUCH: return (int)((float)n*A);
		}

	}
	
	
}
