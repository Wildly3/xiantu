package com.xiuxian.chen.set;

import java.util.*;

//回合监听器，每回合结束都会调用
public interface TurnListener
{
	public void Turn(List<Player> play);
}
