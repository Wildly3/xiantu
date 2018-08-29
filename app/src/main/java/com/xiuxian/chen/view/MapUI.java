package com.xiuxian.chen.view;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xiuxian.chen.MainActivity;
import com.xiuxian.chen.R;
import com.xiuxian.chen.map.Map;
import com.xiuxian.chen.map.MapTile;
import com.xiuxian.chen.map.Role;
import com.xiuxian.chen.map.RoleManager;

import java.util.Iterator;

/**
 * Created by Wildly on 2018/8/28.13:00
 */

public class MapUI extends Fragment implements View.OnClickListener {

    private static final String TAG = "MapUI";

    Button center, up, down, left, right;

    TextView line_up, line_down, line_left, line_right;

    TextView maptitle, info;

    ScrollView infoScrollview;

    LinearLayout hereButtonView;

    MapTile nowmap;

    Role[] nowroles;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.newmap);

        center = (Button) findViewById(R.id.map_way_center);
        up = (Button) findViewById(R.id.map_way_up);
        down = (Button) findViewById(R.id.map_way_down);
        left = (Button) findViewById(R.id.map_way_left);
        right = (Button) findViewById(R.id.map_way_right);
        up.setOnClickListener(this);
        down.setOnClickListener(this);
        left.setOnClickListener(this);
        right.setOnClickListener(this);

        line_up = (TextView) findViewById(R.id.map_wayline_up);
        line_down = (TextView) findViewById(R.id.map_wayline_down);
        line_left = (TextView) findViewById(R.id.map_wayline_left);
        line_right = (TextView) findViewById(R.id.map_wayline_right);

        maptitle = (TextView) findViewById(R.id.map_nowflag);
        info = (TextView) findViewById(R.id.map_info_textview);
        infoScrollview = (ScrollView) findViewById(R.id.map_info_scrollview);

        hereButtonView = (LinearLayout) findViewById(R.id.map_here_button);

        center.setClickable(false);

        mapinit();
    }

    void mapinit(){
        MapTile main = new MapTile("新宿村");

        main.addRole("111");

        main.setUp(new MapTile("后山"));

        main.setLeft(new MapTile("转职殿"));

        main.setRight(new MapTile("试炼场"));

        main.setDown(new MapTile("新宿村外"))
                .setDown(new MapTile("河边"))
                .setRight(new MapTile("木桥"));

        nowmap = main;

        mapshow();
    }

    void mapshow(){
        center.setText(nowmap.name);

        hereButtonView.removeAllViews();

        nowroles = null;

        if (nowmap.roles.size() > 0) {
            nowroles = new Role[nowmap.roles.size()];
            for (int i = 0, len = nowmap.roles.size(); i < len; i++) {
                Role role = RoleManager.ScanRole(nowmap.roles.get(i));
                if (role == null) continue;
                nowroles[i] = role;
                hereButtonView.addView(CreateButton(role));
            }
        }
        MapTile road_up = nowmap.getUp();

        MapTile road_down = nowmap.getDown();

        MapTile road_left = nowmap.getLeft();

        MapTile road_right = nowmap.getRight();

        if (road_up != null){
            line_up.setVisibility(View.VISIBLE);
            up.setVisibility(View.VISIBLE);
            up.setText(road_up.name);
        } else {
            line_up.setVisibility(View.GONE);
            up.setVisibility(View.GONE);
        }

        if (road_down != null){
            line_down.setVisibility(View.VISIBLE);
            down.setVisibility(View.VISIBLE);
            down.setText(road_down.name);
        } else {
            line_down.setVisibility(View.GONE);
            down.setVisibility(View.GONE);
        }

        if (road_left != null){
            line_left.setVisibility(View.VISIBLE);
            left.setVisibility(View.VISIBLE);
            left.setText(road_left.name);
        } else {
            line_left.setVisibility(View.GONE);
            left.setVisibility(View.GONE);
        }

        if (road_right != null){
            line_right.setVisibility(View.VISIBLE);
            right.setVisibility(View.VISIBLE);
            right.setText(road_right.name);
        } else {
            line_right.setVisibility(View.GONE);
            right.setVisibility(View.GONE);
        }

        maptitle.setText(nowmap.name);
    }

    Button CreateButton(final Role role){
        final Button btn = new Button(getContext());
        btn.setText(role.name);
        btn.setTextColor(getContext().getResources().getColor(R.color.color_alltext));
        btn.setBackgroundResource(R.drawable.selector_button);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -1);

        lp.setMargins(0, 0, (int) MainActivity.DP * 6, 0);

        btn.setLayoutParams(lp);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast(role.explain);
            }
        });
        return btn;
    }

    @Override
    public void onClick(View v) {

        boolean isref = false;

        MapTile tile = null;

        int direction = 0;

        switch (v.getId()){
            case R.id.map_way_up:
                if (nowmap.getUp() != null){
                    isref = true;
                    direction = 0;
                    tile = nowmap.getUp();
                }
                break;
            case R.id.map_way_down:
                if (nowmap.getDown() != null){
                    isref = true;
                    direction = 1;
                    tile = nowmap.getDown();
                }
                break;
            case R.id.map_way_left:
                if (nowmap.getLeft() != null){
                    isref = true;
                    direction = 2;
                    tile = nowmap.getLeft();
                }
                break;
            case R.id.map_way_right:
                if (nowmap.getRight() != null){
                    isref = true;
                    direction = 3;
                    tile = nowmap.getRight();
                }
                break;
        }
        if (isref) {

            boolean blocktw = false;

            int length = nowroles == null ? 0 : nowroles.length;

            if (length > 0)
            for (int i=0;i<length;i++){
                Role role = nowroles[i];
                if (role != null && role.event != null)
                    if (role.event.BlockTW()[direction] != 0){
                        blocktw = true;
                        append(role.name);
                        append(role.returns == null ? "拦住了你！" : "：" + role.returns);
                        append("\n");
                    }
            }

            if (blocktw) return;

            nowmap = tile;
            append("你进入了 ");
            append(nowmap.name);
            append("\n");
            mapshow();
        }
    }

    void append(String text){
        info.append(text);
        infoScrollview.fullScroll(View.FOCUS_DOWN);
    }

}
