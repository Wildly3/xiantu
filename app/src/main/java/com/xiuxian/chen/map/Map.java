package com.xiuxian.chen.map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by Wildly on 2018/8/28.14:45
 */

public final class Map {
    public String name;

    private MapTile[] tiles;

    public Map(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /** 将tile转换为数组
     * @param tile 要转换得tile
     */
    public void toList(MapTile tile){
        if (tile == null) return;

        Set<MapTile> arr = tile.toList();

        tiles = new MapTile[arr.size()];

        int i = 0;

        Iterator<MapTile> iterator = arr.iterator();

        while (iterator.hasNext()){
            MapTile t = iterator.next();

            tiles[i] = t;

            i++;
        }
    }

    /**获取转换后得tile数组
     * @return tiles
     */
    public MapTile[] getTiles(){
        return this.tiles;
    }

    /**将转换后得数组转换为tile
     * @return tile
     */
    public MapTile toTile(){
        MapTile tile = null;

        if (tiles == null) return null;

        for (int i=0;i<tiles.length;i++){
            MapTile t = tiles[i];
            if (i == 0) tile = t;
            for (int j=0;j<4;j++){
                String id = t.aroundID[j];
                if (id != null){
                    t.around[j] = FindTile(id);
                }
            }
        }

        return tile;
    }

    /**根据id查找已经转换的数组里的tile
     * @param id 要查找的tile id
     * @return tile
     */
    public MapTile FindTile(String id){
        MapTile tile = null;

        for (int i=0, len = tiles.length;i<len;i++){
            if (tiles[i].id.equals(id)){
                tile = tiles[i];
                break;
            }
        }

        return tile;
    }


}