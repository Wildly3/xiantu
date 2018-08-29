package com.xiuxian.chen.map;

/**
 * Created by Wildly on 2018/8/28.20:51
 */

public class RoleManager {
    public static Role ScanRole(String id){
        Role role = new Role();

        role.name = "村长";
        role.explain = "新宿村的村长，据说年轻时还参过军！";

        return role;
    }
}
