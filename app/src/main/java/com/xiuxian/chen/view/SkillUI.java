package com.xiuxian.chen.view;

import android.text.*;
import android.text.style.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;
import com.xiuxian.chen.*;
import com.xiuxian.chen.include.*;
import com.xiuxian.chen.set.*;
import android.content.*;
import android.os.*;

public class SkillUI extends Fragment
{
	ListView skill_list;
	
	Button[] now_skill_btn;
	
	final int[] now_skill_btnid={
		R.id.skillButton2_skill0,
		R.id.skillButton1_skill1,
		R.id.skillButton1_skill2,
		R.id.skillButton1_skill3
	};
	ListDialog skillments_item_click;
	
	int skillments_item_index;
	
	int textcolor;

	Player base;
	
	@Override
	protected void onCreate(Bundle bundle)
	{
		super.onCreate(bundle);
		
		setContentView(R.layout.skill);
		
		final Context app = getContext();
		
		base = BaseActivity.base;
		
		textcolor = app.getResources().getColor(R.color.color_alltext);

		now_skill_btn=new Button[now_skill_btnid.length];

		for(int i=0;i<now_skill_btnid.length;i++){
			Skill sk=BaseActivity.base.skillment[i];

			Button btn=(Button)findViewById(now_skill_btnid[i]);

			now_skill_btn[i]=btn;

			btn.setText(sk== null ? "..." : sk.name);

			btn.setOnClickListener(now_skill_click);
		}
		skill_list=(ListView)findViewById(R.id.skillListView1);

		skill_list.setOnItemClickListener(new OnItemClickListener(){
				@Override
				public void onItemClick(AdapterView<?> p1, View p2, int index, long p4)
				{
					skillments_item_index=index;
					skillments_item_click=new ListDialog(app)
						.setTitle("选择")
						.setListItem(new String[]{
										 "查看",
										 "替换1",
										 "替换2",
										 "替换3",
										 "替换4"
									 })
						.setOnItemClickListener(new OnItemClickListener(){
							@Override
							public void onItemClick(AdapterView<?> p1, View p2, int index, long p4)
							{
								skillments_item_click.dismiss();
								if(index==0){
									msgdialog(base.skillments.get(skillments_item_index).explain);
									return;
								}
								if(base.skillment[index-1]!=null)
									base.skillments.add(base.skillment[index-1]);
								base.skillment[index-1]=base.skillments.get(skillments_item_index);
								base.skillments.remove(skillments_item_index);
								refresh_skillbtn();
							}
						})
						.show();

				}
			});
		refresh_skillbtn();
	}
	
	//刷新技能按钮
	void refresh_skillbtn(){
		for(int i=0;i<now_skill_btnid.length;i++){
			Skill sk=base.skillment[i];
			//Button btn=(Button)app.findViewById(now_skill_btnid[i]);
			now_skill_btn[i].setText(sk== null ? "..." : sk.name);
			now_skill_btn[i].setOnClickListener(now_skill_click);
		}

		CharSequence[] list=new CharSequence[base.skillments.size()];
		
		for(int i=0;i<base.skillments.size();i++){
			
			list[i] = getColorText(base.skillments.get(i).name, textcolor);
		}
		
		ArrayAdapter<CharSequence> array = new ArrayAdapter<CharSequence>(getContext(), android.R.layout.simple_list_item_1, list);
		
		skill_list.setAdapter(array);
	}
	
	public SpannableString getColorText(String str, int color){
		SpannableString sps=new SpannableString(str);
		ForegroundColorSpan fcs = new ForegroundColorSpan(color);
		sps.setSpan(fcs, 0, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return sps;
	}

	int btnid1, itemid1;
	View.OnClickListener now_skill_click=new View.OnClickListener(){
		@Override
		public void onClick(View v)
		{
			btnid1=v.getId();
			if(getmentskill(v.getId())==null)return;
			switch(v.getId()){
				case R.id.skillButton2_skill0:

					break;
				case R.id.skillButton1_skill1:

					break;
				case R.id.skillButton1_skill2:

					break;
				case R.id.skillButton1_skill3:

					break;
			}

			skilldiament();
		}
	};

	Skill getmentskill(int id){
		switch(id){
			case R.id.skillButton2_skill0:
				return BaseActivity.base.skillment[0];
			case R.id.skillButton1_skill1:
				return BaseActivity.base.skillment[1];
			case R.id.skillButton1_skill2:
				return BaseActivity.base.skillment[2];
			case R.id.skillButton1_skill3:
				return BaseActivity.base.skillment[3];
		}

		return null;
	}

	int getskillindex(int id){
		switch(id){
			default:
			case R.id.skillButton2_skill0:
				return 0;
			case R.id.skillButton1_skill1:
				return 1;
			case R.id.skillButton1_skill2:
				return 2;
			case R.id.skillButton1_skill3:
				return 3;
		}
	}

	ListDialog skilldiament_dia;
	void skilldiament(){
		Skill sk=getmentskill(btnid1);
		skilldiament_dia=new ListDialog(getContext())
			.setTitle(sk.name)
			.setListItem(new String[]{
							 "查看",
							 "卸下"
						 })
			.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener(){
				@Override
				public void onItemClick(AdapterView<?> p1, View p2, int index, long p4)
				{
					itemid1=index;
					switch(index){
						case 0:
							msgdialog(getmentskill(btnid1).explain);
							break;
						case 1:
							base.skillments.add(getmentskill(btnid1));
							base.skillment[getskillindex(btnid1)]=null;
							refresh_skillbtn();
							break;
					}
					
					skilldiament_dia.dismiss();
				}
			})
			.show();
	}
	
}
