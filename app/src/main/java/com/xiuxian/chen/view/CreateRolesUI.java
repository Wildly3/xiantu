package com.xiuxian.chen.view;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.xiuxian.chen.MainActivity;
import com.xiuxian.chen.R;
import com.xiuxian.chen.include.Util;
import com.xiuxian.chen.map.Action;
import com.xiuxian.chen.set.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Wildly on 2018/8/27.21:49
 */

public class CreateRolesUI extends Fragment implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    RadioGroup radioGroup;

    RadioButton people;

    TextView explain;

    Button btncreate;

    EditText editName;

    int type = Player.RACE_PEOPLE;

    String attrexplain;

    int[] aptitude;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.createroles);

        radioGroup = (RadioGroup) findViewById(R.id.createroles_group);

        explain = (TextView) findViewById(R.id.createroles_explain);

        btncreate = (Button) findViewById(R.id.createroles_create);

        editName = (EditText) findViewById(R.id.createroles_edit);

        btncreate.setOnClickListener(this);

        radioGroup.setOnCheckedChangeListener(this);

        people = (RadioButton) findViewById(R.id.createroles_people);

        MainActivity.hideBottom();

        init();
    }

    void init(){
        Attribute one = new Attribute();
        one.weight = 45;

        Attribute two = new Attribute();
        two.weight = 25;

        Attribute three = new Attribute();
        three.weight = 15;

        Attribute four = new Attribute();
        four.weight = 10;

        Attribute five = new Attribute();
        five.weight = 5;

        List<Attribute> arr = new ArrayList<>(5);

        arr.add(one);
        arr.add(two);
        arr.add(three);
        arr.add(four);
        arr.add(five);

        int index = getAttrIndex(arr);

        int[] arrs = aptitude = getAttr(index + 1);

        StringBuilder sb = new StringBuilder("");

        for (int a : arrs) {
            sb.append(getString(a));
            sb.append(" ");
        }
        sb.append("灵根\n");

        attrexplain = sb.toString();

        people.setChecked(true);
    }

    int[] getAttr(int num){
        int[] arr = new int[num];

        for (int i=0;i<num;i++){
            con : while (true){
                int random = new Random().nextInt(499);
                int index = (random / 100) + 1;
                for (int a : arr){
                    if (a == index) {
                        continue con;
                    }
                }
                if (arr[i] == 0){
                    arr[i] = index;
                    break;
                }

            }
        }

        return arr;
    }

    String getString(int index){
        switch (index){
            default: return "null";
            case 1: return "金";
            case 2: return  "木";
            case 3: return  "水";
            case 4: return  "火";
            case 5: return  "土";
        }
    }

    int getAttrIndex(List<Attribute> attrs){
        double sumWeight = 0;

        for (Attribute attr : attrs){
            sumWeight += attr.weight;
        }

        double randomNumber = Math.random();

        double d1 = 0;

        double d2 = 0;

        int index = -1;

        for (int i=0,len = attrs.size();i<len;i++){
            d2 += Double.parseDouble(String.valueOf(attrs.get(i).weight)) / sumWeight;

            if (i == 0){
                d1 = 0;
            } else {
                d1 += Double.parseDouble(String.valueOf(attrs.get(i - 1).weight)) / sumWeight;
            }

            if (randomNumber >= d1 && randomNumber <= d2){
                index = i;
                break;
            }
        }

        return index;
    }


    class Attribute
    {
        int type;

        int weight;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        MainActivity.showBottom();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        String explain = "";
        switch (checkedId){
            case R.id.createroles_people:
                type = Player.RACE_PEOPLE;
                explain = "人族各方面属性比较均衡，比较适合新手玩家！";
                break;
            case R.id.createroles_beast:
                type = Player.RACE_BEAST;
                explain = "妖族皮糙肉厚，但伤害较低！";
                break;
            case R.id.createroles_monster:
                type = Player.RACE_MONSTER;
                explain = "魔族伤害高，但自身比较脆弱！";
                break;
            case R.id.createroles_ghost:
                type = Player.RACE_GHOST;
                explain = "鬼族以速度为傲，但自身较为脆弱！";
                break;
        }

        this.explain.setText(attrexplain  + explain);
    }

    @Override
    public void onClick(View v) {
        String name = editName.getText().toString().trim();

        name = name.replaceAll("\n", "");

        name = name.replaceAll(" ", "");

        if ("".equals(name)){
            toast("道友尊姓大名？");
            return;
        }

        Player play = new Player();

        play.race = type;

        play.Initattr();

        play.name = name;

        play.health += 15;
        play.atk += 6;
        play.mp += 10;
        play.def += 2;
        play.speed += 2;

        int value = 6 - aptitude.length;

        for (int i : aptitude){
            play.aptitude[i-1] = value;
        }

        BaseActivity.space = new com.xiuxian.chen.set.Space();

        BaseActivity.space.space_max = 100;

        BaseActivity.map_action = new Action(1);

        BaseActivity.mapmanager.CacheMap(1);

        BaseActivity.map_action.pos_x = 0;

        BaseActivity.map_action.pos_y = 0;

        play.NewBuffer();

        BaseActivity.base = play;

        RoleActivity.isloadarchive = true;

        RoleActivity.activity.startFragment(new RoleUI());

        finish();
    }
}
