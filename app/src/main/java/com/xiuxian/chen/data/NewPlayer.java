package com.xiuxian.chen.data;
import android.content.*;
import android.view.*;
import android.widget.*;
import com.xiuxian.chen.*;
import com.xiuxian.chen.include.*;
import com.xiuxian.chen.map.*;
import com.xiuxian.chen.presenter.*;
import com.xiuxian.chen.set.*;
import com.xiuxian.chen.view.*;

public class NewPlayer
{
	Context cx;
	
	MainView main;
	
	public NewPlayer(Context cx, MainView main){
		this.cx = cx;
		this.main = main;
	}
	
	public MyDialog Window(){
		final EditText edit1=new EditText(cx);
		edit1.setTextColor(cx.getResources().getColor(R.color.color_alltext));
		//edit1.setBackground(getResources().getDrawable(R.drawable.shape_corner));
		edit1.setBackgroundResource(R.drawable.shape_corner);
		edit1.setCursorVisible(false);
		MyDialog md = new MyDialog(cx)
			.SetTitle("道友您的名字？")
			.setView(edit1)
			.setSelect("确定", new MyDialog.OnClickListener(){
				@Override
				public void onClick(MyDialog dialog)
				{
					if(!edit1.getText().toString().equals("")){
						Intent i=new Intent("show");
						i.putExtra("vis", View.VISIBLE);
						i.putExtra("end", true);
						cx.sendBroadcast(i);
						BaseActivity.base = NewPlayer(edit1.getText().toString());
						dialog.dismiss();
                        main.Complete(IMainActivityPresenter.FLAG_LOADARCHIVE);
					}
					else main.ShowMessage("道友尊姓大名？");
				}
			}, false)
			.setCancel("离开", new MyDialog.OnClickListener(){
				@Override
				public void onClick(MyDialog dialog)
				{
					System.exit(0);
				}
			})
			.setCancel(false);
			
			return md;
	}
	
	//任务初始属性
	Player NewPlayer(String name){
		Player play = new Player();
        play.Initattr();
		play.name = name;
		play.health += 15;
		play.atk += 6;
		play.mp += 10;
		play.def += 2;
		play.speed += 2;

		BaseActivity.space=new com.xiuxian.chen.set.Space();

		BaseActivity.space.space_max=100;

		BaseActivity.map_action=new Action(1);

        BaseActivity.mapmanager.CacheMap(1);

		BaseActivity.map_action.pos_x=0;

		BaseActivity.map_action.pos_y=0;

		play.NewBuffer();

		return play;
	}
	
}
