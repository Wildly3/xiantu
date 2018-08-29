package com.xiuxian.chen.map;

import android.util.Log;

import com.xiuxian.chen.include.IDUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Wildly on 2018/8/28.15:01
 */

public final class MapTile {
    private static final String TAG = "MapTile";

    public String name;

    public String id;

    public transient MapTile[] around;

    public String[] aroundID;

    public List<String> roles;

    public MapTile(){
        roles = new ArrayList<>(10);
        around = new MapTile[4];
        aroundID = new String[4];
        id = IDUtils.createID();
    }

    public MapTile(String name){
        this();
        this.name = name;
    }


    /**添加人物
     * @param id 添加人物的id
     */
    public void addRole(String id){
        this.roles.add(id);
    }

    public MapTile setUp(MapTile tile){
        if (tile == null) return null;
        around[0] = tile;
        aroundID[0] = tile.id;
        tile.around[1] = this;
        tile.aroundID[1] = this.id;
        return tile;
    }

    public MapTile setDown(MapTile tile){
        if (tile == null) return null;
        around[1] = tile;
        aroundID[1] = tile.id;
        tile.around[0] = this;
        tile.aroundID[0] = this.id;
        return tile;
    }

    public MapTile setLeft(MapTile tile){
        if (tile == null) return null;
        around[2] = tile;
        aroundID[2] = tile.id;
        tile.around[3] = this;
        tile.aroundID[3] = this.id;
        return tile;
    }

    public MapTile setRight(MapTile tile){
        if (tile == null) return null;
        around[3] = tile;
        aroundID[3] = tile.id;
        tile.around[2] = this;
        tile.aroundID[2] = this.id;
        return tile;
    }


    public MapTile getUp(){
        return around[0];
    }

    public MapTile getDown(){
        return around[1];
    }

    public MapTile getLeft(){
        return around[2];
    }

    public MapTile getRight(){
        return around[3];
    }

    /**遍历tile比较器返回true时返回tile对象
     * @param base 要遍历得tile
     * @param skip 要跳过得tile（避免死循环）
     * @param comparator 比较器
     * @return tile
     */
    private MapTile sort(MapTile base, MapTile skip, Comparator comparator){
        MapTile tile = null;

        if (comparator == null) return null;

        if (comparator.compar(this)) return this;

        for (int i = 0; i < 4; i++){
            MapTile round = base.around[i];
            if (round != null && !skip.equals(round)){
                boolean c = comparator.compar(round);
                if (c) {
                    tile = round;
                    break;
                }
                MapTile returns = sort(round, base, comparator);
                if (returns != null){
                    tile = returns;
                    break;
                }
            }
        }
        return tile;
    }

    /**根据名称查找tile
     * @param name 要查找得tile名称
     * @return tile
     */
    public MapTile FindNameTile(final String name){
         return sort(this, this, new Comparator() {
            @Override
            public boolean compar(MapTile tile) {
                if (tile.name.equals(name)){
                    return true;
                }
                return false;
            }
        });
    }

    /**根据id查找tile
     * @param id 要查找得tile id
     * @return tile
     */
    public MapTile FindIDTile(final String id){
        return sort(this, this, new Comparator() {
            @Override
            public boolean compar(MapTile tile) {
                if (tile.id.equals(id)){
                    return true;
                }
                return false;
            }
        });
    }

    /**转换为tile集合
     * @return tiles
     */
    public Set<MapTile> toList(){
        final Set<MapTile> arr = new HashSet<>();

        sort(this, this, new Comparator() {
//            int i=0;
            @Override
            public boolean compar(MapTile tile) {
                arr.add(tile);
                //Log.i(TAG, "compar: " + (++i));
                return false;
            }
        });

        return arr;
    }


    public static interface Comparator
    {
        public boolean compar(MapTile tile);
    }

}
