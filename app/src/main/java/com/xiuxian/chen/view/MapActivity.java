package com.xiuxian.chen.view;
import android.animation.Animator;
import android.app.Activity;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import com.xiuxian.chen.*;
import com.xiuxian.chen.include.MyDialog;
import com.xiuxian.chen.set.*;
import java.util.*;
import android.util.*;
import com.xiuxian.chen.data.*;
import com.xiuxian.chen.util.Timing;

public class MapActivity extends BaseActivity implements OnClickListener, OnTouchListener, TurnListener, MapViewListener
{
    private static final String TAG = "MapActivity";

	MapView mv;
	
	Button[] btns;
	
	int resid[]={
		R.id.mapButton1_up,
		R.id.mapButton1_down,
		R.id.mapButton1_left,
		R.id.mapButton1_right,
		R.id.mapButton2_ok
	};
	
	int resid2[]={
		R.id.mapButton1_attack,
		R.id.mapButton1_skill1,
		R.id.mapButton1_skill2,
		R.id.mapButton1_skill3,
		R.id.mapButton1_escape,
		R.id.mapButton1_space
	};
	Button[] fightbtn;
	
	TextView pos;
	
	List<Button> skillbtn;

    Player base;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

//        if (!MapDetect()) {
//            setContentView(R.layout.err);
//            return;
//        }
//        else
//		setContentView(R.layout.map);
//
//        base = BaseActivity.base;
//
//		mv = (MapView)findViewById(R.id.mapMapView1);
//
//		mv.ma = this;
//
//		MainActivity.event.setMapView(mv);
//
//		mv.pos = pos=(TextView)findViewById(R.id.mapTextView1_pos);
//
//		Button btn = null;
//
//		btns=new Button[resid.length];
//
//		for(int i=0;i<resid.length;i++){
//			btns[i]=(Button)findViewById(resid[i]);
//			if(i!=resid.length-1)
//			btns[i].setOnTouchListener(this);
//		}
//
//		btns[resid.length - 1].setOnClickListener(new OnClickListener(){
//				@Override
//				public void onClick(View p1)
//				{
//					mv.okevent();
//				}
//			});
//
//		fightbtn=new Button[resid2.length];
//
//		skillbtn = new ArrayList<>(3);
//
//		for (int i=0;i < resid2.length;i++)
//		{
//			btn =  (Button)findViewById(resid2[i]);
//
//			if (i > 0 & i < 4)
//			{
//				btn.setText(base.skillment[i - 1] == null ? "..." : base.skillment[i - 1].name);
//				skillbtn.add(btn);
//			}
//
//			btn.setOnClickListener(this);
//
//			fightbtn[i] = btn;
//		}
//
////        Log.i(TAG, "onCreate: time "+Timing.end());
        startFragment(new MapUI());
    }

	//检测地图是否存在
	boolean MapDetect(){
//        Log.i(TAG, "MapDetect: " + BaseActivity.map_action.map_id);
        if(!BaseActivity.mapmanager.isMap(BaseActivity.map_action.map_id)) {
            MainActivity.event.Alert("地图加载异常", BaseActivity.mapmanager.getError(), 1);
            return false;
        }

        return true;
    }

	//刷新技能冷却
	void btnSkillTitle(Player play){
		for(int i=0;i<3;i++){
			Button btn = skillbtn.get(i);

			Skill sk = play.skillment[i];

			if(sk != null){
			if(sk.getNowCd() > 0){
				btn.setText(sk.name+"("+sk.getNowCd()+")");
				btn.setClickable(false);
			}else {
				btn.setText(sk.name);
				btn.setClickable(true);
			} }
		}
	}
	
	@Override
	public void onClick(View v)
	{
		if(mv.state) return ;
		
		if(mv.pAll.my_atk_num < 1 || mv.isfightend) return ;
		
		switch(v.getId()){
			case R.id.mapButton1_attack:
				mv.attack();
				break;
			case R.id.mapButton1_skill1:
				mv.skill_attack(0);
				break;
			case R.id.mapButton1_skill2:
				mv.skill_attack(1);
				break;
			case R.id.mapButton1_skill3:
				mv.skill_attack(2);
				break;
			case R.id.mapButton1_escape:
				mv.escape();
				break;
			case R.id.mapButton1_space:
				mv.fight_open_space();
				break;
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
		switch(v.getId()){
			case R.id.mapButton1_up:
				mv.onKeyEvent(0, event.getAction());
				break;
			case R.id.mapButton1_down:
				mv.onKeyEvent(1, event.getAction());
				break;
			case R.id.mapButton1_left:
				mv.onKeyEvent(2, event.getAction());
				break;
			case R.id.mapButton1_right:
				mv.onKeyEvent(3, event.getAction());
				break;
			case R.id.mapButton2_ok:
				mv.onKeyEvent(4, event.getAction());
				break;
		}
		return false;
	}
	
	public void showmapkey(){
		for(Button b : btns)
			if(b.getVisibility()==View.VISIBLE)
				b.setVisibility(View.GONE);
			else if(b.getVisibility()==View.GONE)
				b.setVisibility(View.VISIBLE);
				
		if(pos.getVisibility()==View.VISIBLE)
			pos.setVisibility(View.GONE);
		else if(pos.getVisibility()==View.GONE)
			pos.setVisibility(View.VISIBLE);
	}
	
	public void mapkeyhide(){
		for(Button b : btns)
				b.setVisibility(View.GONE);
			pos.setVisibility(View.GONE);
	}
	
	public void showfightkey2(){
		for(Button b : fightbtn)
				b.setVisibility(View.VISIBLE);
	}
	
//	public void showfightkey(){
//		int type = -1;
//		for(Button b : fightbtn)
//			if(b.getVisibility()==View.VISIBLE)
//				b.setVisibility(View.GONE);
//			else if(b.getVisibility() == View.GONE)
//				b.setVisibility(type = View.VISIBLE);
//
//			if(type != -1)
//				btnSkillTitle(BaseActivity.base);
//	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		//mv.onPause();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		//onResume();
	}
	
	@Override
	public void Turn(List<Player> play)
	{
		btnSkillTitle(play.get(0));
	}
	
	@Override
	public void FightEnd()
	{
		// TODO: Implement this method
	}

	@Override
	public void FightStart()
	{
		btnSkillTitle(BaseActivity.base);
	}

    @Override
    public void onBackPressed() {
        new MyDialog(this)
                .SetTitle("提示")
                .setMessage("是否不保存退出？")
                .setCancel("否", null)
                .setSelect("是", new MyDialog.OnClickListener(){
                    @Override
                    public void onClick(MyDialog dialog)
                    {
                        MainActivity.hideBottom();
                        getWindow().getDecorView().animate()
                                .scaleX(0)
                                .scaleY(0)
                                .setDuration(300)
                                .setListener(new android.animation.Animator.AnimatorListener(){
                                    @Override
                                    public void onAnimationStart(Animator p1) {}

                                    @Override
                                    public void onAnimationEnd(Animator p1)
                                    {
                                        System.exit(0);
                                    }

                                    @Override
                                    public void onAnimationCancel(Animator p1) {}

                                    @Override
                                    public void onAnimationRepeat(Animator p1) {}
                                });

                    }
                }).show();
    }
}
