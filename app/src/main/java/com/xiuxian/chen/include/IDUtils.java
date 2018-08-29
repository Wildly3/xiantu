package com.xiuxian.chen.include;

/**
 * Created by Wildly on 2018/8/28.17:38
 * 根据时间戳和随机数生成唯一id
 */

public final class IDUtils {
    private static byte[] lock = new byte[0];

    private static long w = 10000000;

    public static String createID(){
        long r = 0;
        synchronized (lock){
            r = (long) ( (Math.random() + 1) * w);
        }

        return System.currentTimeMillis() + String.valueOf(r).substring(1);
    }

}
