package com.xiuxian.chen.util;
import java.lang.reflect.*;

public interface LoaderEvent<T>
{
	public void call(LoaderEvent<T> load);
	
	public void next(T n);
	
	public void complete();
}
