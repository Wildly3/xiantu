package com.xiuxian.chen.map;

import android.renderscript.Script;
import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xiuxian.chen.data.MyILoadResource;
import com.xiuxian.chen.include.Util;

import java.io.UnsupportedEncodingException;

/**
 * Created by Wildly on 2018/8/31.1:31
 * 资源管理器
 */

public class AssetsManager {

    public static Map LoadMap(String id){
        String filename = MyILoadResource.MAP_PATH + id + ".json";
        String json = Util.ReadStringFile(filename);
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json, Map.class);
    }

    public static Role LoadRole(String id){
        String filename = MyILoadResource.CACHE_PATH + "/role/" + id + ".json";
        String json = Util.ReadStringFile(filename);
        Gson gson = new GsonBuilder().create();
        Role role = gson.fromJson(json, Role.class);
        if (role.script != null && "".equals(role.script)){
            String script = Util.Base64ToString(role.script);
            if (script != null){
                role.event = Util.fromJs(script, RoleEvent.class);
            }
        }
        return role;
    }

}
