package com.xiuxian.chen.map;

/**
 * Created by Wildly on 2018/8/28.20:12
 * 地图人物
 */

public class Role {
    public String name;

    public String id;

    //简介
    public String explain;

    //当阻挡阻挡通过时，返回的信息
    public String returns;

    //死亡后是否还会复活刷新
    public boolean isre;

    //事件接口
    public transient RoleEvent event;

    //脚本
    public String script;

    public Role(){}
}
