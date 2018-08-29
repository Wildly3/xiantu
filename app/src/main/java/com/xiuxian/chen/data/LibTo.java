package com.xiuxian.chen.data;

public final class LibTo
{
	static{
		System.loadLibrary("xiantu");
	}

	public static native boolean Verify(String str);

	public static native String getAppId();
	
}
