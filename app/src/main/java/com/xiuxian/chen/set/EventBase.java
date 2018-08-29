package com.xiuxian.chen.set;

public abstract class EventBase implements EventImplements
{
	private PlayerAll pall;
	
	private Space space;
	
	private String name;
	
	public EventBase(String name){
		this.name = name;
	}
	
	@Override
	public void onCreate(PlayerAll pall, Space space)
	{
		this.pall = pall;
		this.space = space;
	}
	
	public String getMame(){
		return this.name;
	}
	
	public PlayerAll getpAll(){
		return this.pall;
	}
	
	public Space getSpace(){
		return this.space;
	}
	
}
