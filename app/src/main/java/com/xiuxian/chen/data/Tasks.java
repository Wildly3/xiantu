package com.xiuxian.chen.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xiuxian.chen.set.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class Tasks
{
	//查找一个任务
	public static GameTask ScanTask(String name){

//		for(GameTask gt : list)
//			if(name.equals(gt.name))return gt;

        String json = readString(MyILoadResource.CACHE_PATH + "/task/" + name + ".json");

        if (json == null) return null;

        Gson gson = new GsonBuilder().create();

        GameTask gameTask = gson.fromJson(json, GameTask.class);

		return gameTask;
	}

    private static String readString(String filename){
        FileInputStream fileInputStream = null;
        String str = null;
        try {
            fileInputStream = new FileInputStream(filename);

            byte[] buf = new byte[fileInputStream.available()];

            fileInputStream.read(buf);

            str = new String(buf, "UTF-8");
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        }finally {
            if (fileInputStream != null)
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

        return str;
    }

}
