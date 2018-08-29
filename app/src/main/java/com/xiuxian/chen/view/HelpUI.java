package com.xiuxian.chen.view;

import android.graphics.Color;
import android.os.Bundle;

import com.xiuxian.chen.MainActivity;
import com.xiuxian.chen.R;
import com.xiuxian.chen.include.AllLayout;

/**
 * Created by Wildly on 2018/8/4.5:42
 */

public class HelpUI extends Fragment {

    AllLayout layout;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        MainActivity.hideBottom();

        setContentView(R.layout.helpview);

        layout = (AllLayout) findViewById(R.id.helpview_layout);

        init();
    }

    void init(){
        final float TitleSize = 18;
        final float MsgSize = 14f;
        final int TitleColor = Color.GREEN;
        final int MsgColor = Color.WHITE;

        layout.addText("战斗相关", TitleSize, TitleColor);
        layout.addText("战斗界面键盘操作、点击当前选中敌人（黄色小箭头）执行普通攻击，长按敌人血条查看详细信息。", MsgSize, MsgColor);

        layout.addText("地图界面操作", TitleSize, TitleColor);
        layout.addText("除键盘操作外，点击地图任意地点自动进行移动，点击人物当前位置执行确定键操作。", MsgSize, MsgColor);

        layout.addText("人物属性介绍", TitleSize, TitleColor);
        layout.addText("攻击 普通攻击为100%攻击力加成的一次攻击", MsgSize, MsgColor);
        layout.addText("防御 抵御掉100%防御点伤害", MsgSize, MsgColor);
        layout.addText("速度 这会影响自身的命中和闪避", MsgSize, MsgColor);
        layout.addText("气运 该属性现在暂时没有任何作用", MsgSize, MsgColor);
        layout.addText("五行属性 进行属性攻击时会根据相应的五行属性加成伤害（每点属性增加0.2%伤害）", MsgSize, MsgColor);
        layout.addText("五行属性抗性 该属性被属性攻击 攻击时会根据相应五行属性减免伤害（每点属性减少0.2%伤害）", MsgSize, MsgColor);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MainActivity.showBottom();
    }
}
