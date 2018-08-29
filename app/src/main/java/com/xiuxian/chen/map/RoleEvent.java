package com.xiuxian.chen.map;

/**
 * Created by Wildly on 2018/8/28.20:29
 * 人物事件接口
 */

public interface RoleEvent {
    /**拦路
     * @return 返回一个长度为4的整型数组，分别为上下左右，为0时不拦截，不为零时表示拦路
     */
    public int[] BlockTW();
}
