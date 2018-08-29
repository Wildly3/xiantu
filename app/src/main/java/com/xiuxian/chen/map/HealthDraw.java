package com.xiuxian.chen.map;
import com.xiuxian.chen.set.*;
import android.graphics.*;
import android.util.*;
import java.util.*;
import java.util.concurrent.*;
import com.xiuxian.chen.view.*;
import com.xiuxian.chen.*;

public class HealthDraw
{
	private Player p;
	
	private Paint mPaint;
	
	private int px, py;
	
	private int tw, th, curw, curh;
	
	private int nth;
	
	private int max_hp;
	
	private String cursor;
	
	public static final int hwidth=200;
	
	static final int CURSORSIZE = 70;
	
	private int now_hp;
	
	private Rect rect;

    private List<AttackEffect> effects;

    //保存血量判定是否收到伤害
    private int save_health;

	public HealthDraw(Player p){
		this.p = p;
        save_health = p.base.now_health;
		this.mPaint = new Paint();
		mPaint.setTextSize(30);
		Rect r = getTextWH(p.name+"LV·"+p.level, mPaint);
		tw = r.width();
		th = r.height()+10;
		mPaint.setTextSize(20);
		nth = getTextWH("100/100", mPaint).height();
		mPaint.setTextSize(CURSORSIZE);
		Rect r2 = getTextWH(">",mPaint);
		curw = r2.width()+(int)(MainActivity.DP*2);
		curh = r2.height();
		//Log.i("health",""+p.health);
		max_hp = p.base.max_health;
		cursor = "";
		this.now_hp = getnumat(hwidth, p.base.now_health * 100 / max_hp);
		rect();
        effects = new ArrayList<>(6);
	}

	public void setX(int px)
	{
		this.px = px;
	}

	public int getX()
	{
		return px;
	}

	public void setY(int py)
	{
		this.py = py;
	}

	public int getY()
	{
		return py;
	}
	
	public void ShowCursor(int fx){
		cursor=fx==0 ? ">" : "<";
	}
	
	public void HideCursor(){
		cursor="";
	}
	
	public List<Buffer> getBuffers(){
		return p.buffer;
	}
	
	public void setPosition(int x, int y){
		this.px=x;
		this.py=y;
		rect();
	}
	
	private boolean isoffset;
	
	private int original_position_x;
	
	private static final int offset_x = hwidth / 15;
	
	public void left(){
		isoffset = true;
		this.original_position_x = px;
		this.px -= offset_x;
	}
	
	public void right(){
		isoffset = true;
		this.original_position_x = px;
		this.px += offset_x;
	}

	public void atkeffect(int atk){
        AttackEffect ae = new AttackEffect();
        ae.distance = th * 2;
        ae.atk = atk;
        ae.setColor(Color.YELLOW);
        ae.setTextSize(MainActivity.DP * 15);
        ae.setPosition(px, py + th);
        effects.add(ae);
    }
	
	public Rect getRect(){
		return this.rect;
	}
	
	private Rect rect(){
		this.rect = new Rect(px, py, px+hwidth, py+(th+35)+(th/2));
		return this.rect;
	}
	
	
	public void onDraw(Canvas c)
	{
		mPaint.setColor(Color.YELLOW);
		//绘制等级
		mPaint.setTextSize(30);

		c.drawText(p.name + "LV·" + p.level, (tw > hwidth & px > 100) ? px - (tw - hwidth) : px, py + th, mPaint);

		//绘制光标
		mPaint.setTextSize(CURSORSIZE);

		c.drawText(cursor, px - curw, (py + th) + curh, mPaint);
		//绘制血条背景
		mPaint.setColor(Color.rgb(128, 128, 128));

		drawRect(c, px, (py + th) + 10, px + hwidth, (py + th) + 35, mPaint);
		//绘制血条
		int hpat = p.base.now_health * 100 / max_hp;

		int hpat2=getnumat(hwidth, hpat);

		mPaint.setColor(0x82FF003B);

		drawRect(c, px, (py + th) + 10, px + (now_hp <= 0 ? 0 : now_hp), (py + th) + 35, mPaint);

		mPaint.setColor(Color.RED);

		drawRect(c, px, (py + th) + 10, px + (hpat2 <= 0 ? 0 : hpat2), (py + th) + 35, mPaint);

		int shield = p.base.getShield() < 1 ? 0 : getnumat(hwidth, p.base.getShield() * 100 / p.base.getShieldMax());

		if (shield != 0)
		{
			mPaint.setColor(0xFFFF9425);

			drawRect(c, px, (py + th) + 10, px + shield, (py + th) + 30, mPaint);

			int line = p.base.getShieldMax() / 50;

			line = line < 1 ? 1 : line;

			mPaint.setColor(Color.BLACK);

			if (line < 3)
			{
				//if (shield >= (hwidth / 2))
				c.drawLine(px + (hwidth / 2), (py + th) + 10, px + (hwidth / 2), (py + th) + 35, mPaint);
			}
			else
			{
				line--;
				line = line > 30 ? 30 : line;
				int linew = hwidth / (line + 1);
				int now = 0;
				for (int i=1;i < line + 1;i++)
				{
					now = i * linew;
					c.drawLine(px + (now), (py + th) + 10, px + (now), (py + th) + 35, mPaint);
				}
			}
		}

		//绘制血量信息
		mPaint.setColor(Color.WHITE);

		mPaint.setTextSize(20);

		String msg = shield == 0 ? (p.base.now_health + "/" + max_hp) : String.valueOf(p.base.getShield());

		c.drawText(msg, px+5, (py + th) + 10 + (nth), mPaint);

		mPaint.setColor(Color.BLUE);

		drawRect(c, px, (py + th) + 35, px + getnumat(hwidth, p.base.now_mp * 100 / p.base.max_mp), (py + th) + 45, mPaint);

		if (now_hp > hpat2)now_hp -= ((now_hp - hpat2) / 10) < 1 ? 1 : (now_hp - hpat2) / 10;

        if (p.base.now_health != save_health){
            int atk = save_health - p.base.now_health;

            atkeffect(atk);

            save_health = p.base.now_health;
        }

		if (isoffset)
		{
			if (px == original_position_x) isoffset = false;
			if (px > original_position_x) px--;
			else 
			if (px < original_position_x) px++;
		}

		if (effects.size() > 0){
            Iterator<AttackEffect> iterator = effects.iterator();

            while (iterator.hasNext()){
                AttackEffect ae = iterator.next();
                if (ae.isLife())
                    ae.onDraw(c);
                else
                    iterator.remove();
            }
        }
	}

	private void drawRect(Canvas c, float left, float top, float right, float bottom, Paint p){
		RectF r = new RectF(left, top, right, bottom);
		c.drawRoundRect(r, 2, 2, p);
		//c.drawRect(r, p);
	}
	
	//getat(p.base.now_mp, p.base.max_mp)
	
	int getnumat(int a, int b){
		float c=((float)b/100);
		return (int)((float)a*c);
	}
	
	public Rect getTextWH(String str, Paint mp){
		Rect r=new Rect();
		mp.getTextBounds(str,0,str.length(),r);
		return r;
	}
	
}
