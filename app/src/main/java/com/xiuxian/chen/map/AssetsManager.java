package com.xiuxian.chen.map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xiuxian.chen.data.MyILoadResource;
import com.xiuxian.chen.include.Util;

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

}
