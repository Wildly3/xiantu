package com.xiuxian.chen.util;

/**
 * Created by Wildly on 2018/7/30.1:11
 */

public final class Timing {
    private static long time;

    public static void start(){
        time = System.currentTimeMillis();
    }

    public static long end(){
        return System.currentTimeMillis() - time;
    }

    private long ntime;

    private int interval;

    private long btime;

    public Timing(){}

    public Timing(int interval){
        this.interval = interval;
    }

    public void Nstart(){
        ntime = System.currentTimeMillis();
    }

    public long Nend(){
        long result = System.currentTimeMillis() - ntime;

        result = (result < 1 ? 0 : result);

//        btime += result;

        return result;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public void reset(){
        this.btime = System.currentTimeMillis();
    }

    public boolean isInterval(){
        if ((System.currentTimeMillis() - btime) >= interval){
            btime = System.currentTimeMillis();
            return true;
        }

        return false;
    }
}
