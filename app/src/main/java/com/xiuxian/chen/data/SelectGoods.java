package com.xiuxian.chen.data;

public final class SelectGoods
{
	//类型
	public int type;
	
	//名称
	public String name;
	
	//位置
	public int index;
	
	//数量
	public int number;

	//质量
	public int quality;
	
	//耐久
	public int dura;
	
	@Override
	public int hashCode()
	{
		return type + index;
	}

	@Override
	public boolean equals(Object obj)
	{
		if(!(obj instanceof SelectGoods)){
			return false;
		}
		
		SelectGoods sg = (SelectGoods)obj;
		
		if(type == sg.type
                & index == sg.index
                & name != null
                && sg.name !=null
                && name.equals(sg.name))
			return true;

		return false;
	}
	
	
}
