package com.xiuxian.chen.map;

public class Action
{
	public int map_id;
	public int pos_x;
	public int pos_y;
	
	
	public Action(int id){
		this.map_id=id;
	}
	
	public void setPosition(int x, int y){
		this.pos_x=x;
		this.pos_y=y;
	}
	
	
	
}
