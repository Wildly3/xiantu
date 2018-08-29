package com.xiuxian.chen.util;
import android.os.*;
import com.xiuxian.chen.include.*;
import android.content.*;
import rx.subscriptions.*;
import rx.*;
import rx.observers.*;

public final class Loader
{
//	public static void Load(final LoaderEvent<?> load){
//		final Handler handle = new Handler(Looper.getMainLooper());
//
//		final LoaderEvent<?> event = new LoaderEvent(){
//			@Override
//			public void call(LoaderEvent<?> load){}
//
//			@Override
//			public void next(final Object n)
//			{
//				handle.post(new Runnable(){
//						@Override
//						public void run()
//						{
//							load.next(n);
//						}
//					});
//			}
//
//			@Override
//			public void complete()
//			{
//				handle.post(new Runnable(){
//						@Override
//						public void run()
//						{
//							load.complete();
//						}
//					});
//			}
//
//		};
//
//		new Thread(){
//			@Override
//			public void run(){
//				load.call(event);
//			}
//		}.start();
//	}
	
	public static MyDialog LoadDialog(Context cx){
		return new MyDialog(cx)
		.SetTitle("提示")
		.setMessage("加载中...")
		.setCancel(false)
		.Show();
	}
	
	
}
