package com.xiuxian.chen.map;

/**
 * Created by Wildly on 2018/8/28.20:51
 */

public class RoleManager {
    public static Role ScanRole(String id){
        Role role = new Role();

        role.name = "村长";
        role.explain = "新宿村的村长，据说年轻时还参过军！";
//        role.event = new RoleEvent() {
//            @Override
//            public int[] BlockTW() {
//                return new int[]{0,1,0,0};
//            }
//        };

        role.returns = "你现在还不能去哦！";

        return role;
    }
}
