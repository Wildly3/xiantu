package com.xiuxian.chen.map;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

/**
 * Created by Wildly on 2018/7/30.6:03
 */

public final class AttackEffect
{
    private static final String TAG = "AttackEffect";

    int x, y;

    //移动量
    int offset;

    //可移动的距离
    int distance;

    //要显示的攻击数值
    int atk;

    Paint paint;

    //
    private int pdistance;

    AttackEffect(){
        paint = new Paint();
        offset = 1;
    }

    public void setPosition(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void setColor(int color){
        this.paint.setColor(color);
    }

    public void setTextSize(float textSize){
        this.paint.setTextSize(textSize);
    }

    void onDraw(Canvas canvas){
        if (!isLife()) return;

        String text = (atk < 1 ? "+" + Math.abs(atk) : String.valueOf(-atk));

        canvas.drawText(text, x, y + (-pdistance), paint);

        pdistance += offset;
    }

    //生命是否结束
    public boolean isLife(){
        return !(pdistance > distance);
    }
}
