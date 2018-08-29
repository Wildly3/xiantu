package com.xiuxian.chen.include;
import android.os.*;

public class EventPost extends Handler implements Runnable
{
	long time;
	
	Runnable runnable;
	
	boolean isLoop;
	
	public EventPost(long time){
		this.time = time;
	}

	public void setLoop(boolean isLoop)
	{
		this.isLoop = isLoop;
	}

	public boolean isLoop()
	{
		return isLoop;
	}

	public void setRunnable(Runnable runnable)
	{
		this.runnable = runnable;
	}

	public Runnable getRunnable()
	{
		return runnable;
	}
	
	public void post(){
		if(runnable == null) return ;
		this.postDelayed(this, time);
	}
	
	public void stop(){
		//this.removeCallbacks(this);
		this.runnable = null;
	}
	
	@Override
	public void run()
	{
		if(runnable != null)
			runnable.run();
		
		if(isLoop) this.postDelayed(this, time);
	}
	
}
