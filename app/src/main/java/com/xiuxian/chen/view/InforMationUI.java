package com.xiuxian.chen.view;

import android.content.Context;
import android.graphics.*;
import android.view.*;
import android.widget.*;
import com.xiuxian.chen.*;
import com.xiuxian.chen.include.*;
import com.xiuxian.chen.set.*;
import com.xiuxian.chen.data.*;
import android.os.*;

public class InforMationUI extends Fragment implements View.OnClickListener
{
	TextView size_text, attrs_num;
	
	final int resid[]={
		R.id.roleattrButton2_ppower,
		R.id.roleattrButton2_power,
		R.id.roleattrButton2_def,
		R.id.roleattrButton2_speed
	};
	
	final int colors[] ={
		R.color.weapon,
		R.color.head,
		R.color.earrings,
		R.color.necklace,
		R.color.clothes,
		R.color.gloves,
		R.color.shoes
	};
	
	final int[] resid_button={
		R.id.roleattrButton2_weapon,//武器
		R.id.roleattrButton2_head,//头部
		R.id.roleattrButton2_earrings,//耳环
		R.id.roleattrButton2_necklace,//项链
		R.id.roleattrButton2_clothes,//衣服
		R.id.roleattrButton2_gloves,//手套
		R.id.roleattrButton2_shoes//鞋子
	};
	
	Player base;
	
	com.xiuxian.chen.set.Space space;
	
	@Override
	public void onCreate(Bundle bundle)
	{
		super.onCreate(bundle);
		
		base = BaseActivity.base;

		space = BaseActivity.space;
		
		setContentView(R.layout.role_attr);
		
		attr_text=(AllLayout)findViewById(R.id.roleattrTextView1);
		
		attrs_num=(TextView)findViewById(R.id.roleattrTextView2_attr);
		
		for(int id : resid)
			((Button)findViewById(id))
			.setOnClickListener(new View.OnClickListener(){
					@Override
					public void onClick(View v)
					{
						addattr(v);
					}
				});
		
		equip_buttons=new Button[resid_button.length];
		for(int i=0;i<resid_button.length;i++){
			equip_buttons[i]=(Button)findViewById(resid_button[i]);
			equip_buttons[i].setOnClickListener(this);
			//equip_buttons[i].setOnLongClickListener(equip_button_longclick);
		}
		attr_refresh();
	}

	AllLayout attr_text;
	Button[] equip_buttons;
	
	//获取人物属性
	void attr_refresh(){
		equip_scan();
		base.NewBuffer();
		Buffer real=base.base;
		int white = Color.WHITE;
		int size = 16;
		attr_text.removeAllViews();

        LinearLayout line = new LinearLayout(getContext());

        line.setOrientation(LinearLayout.HORIZONTAL);

		attr_text.addText(base.name, size, white, line);

        String race[] = Util.getRaceString(base.race, getContext());

        attr_text.addText("  [" + race[0] + "]", size, Integer.parseInt(race[1]), line);

        attr_text.addText(getAptitudeString(base.aptitude), size, white, line);

        attr_text.addLayout(line);
		//attr_text.addText("经验 "+base.exp+"/"+base.max_exp, size, white);
		AllLayout.ProgressBar progress = new AllLayout.ProgressBar();
		progress.setTextSize(size);
		
		progress.setTitle("经验");
		progress.setAllColor(getContext().getResources().getColor(R.color.exp));
		progress.setProgress(base.exp*100/base.max_exp);
		attr_text.addProgres(progress);
		
		progress.setTitle("气血");
		progress.setAllColor(getContext().getResources().getColor(R.color.health));
		progress.setProgress(real.now_health*100/real.max_health);
		progress.setMsg(real.now_health+"/"+real.max_health);
		attr_text.addProgres(progress);
		
		
		progress.setTitle("灵力");
		progress.setAllColor(getContext().getResources().getColor(R.color.mp));
		progress.setProgress(real.now_mp*100/real.max_mp);
		progress.setMsg(real.now_mp+"/"+real.max_mp);
		attr_text.addProgres(progress);

        LinearLayout layout1 = attr_text.CreateLayout();

        LinearLayout layout2 = attr_text.CreateLayout();

        attr_text.addText("攻击 "+real.atk, size, getContext().getResources().getColor(R.color.atk), layout1);
		
		attr_text.addText("防御 "+real.def, size, getContext().getResources().getColor(R.color.def), layout1);
		
		attr_text.addText("速度 "+real.speed, size, getContext().getResources().getColor(R.color.speed), layout1);
		
		attr_text.addText("气运 "+real.luck, size, getContext().getResources().getColor(R.color.luck), layout1);

        attr_text.addText("金属性 "+real.metal + "   金属性抗性 "+real.metal_resist, size, getColor(R.color.metal), layout2);

        attr_text.addText("木属性 "+real.wood + "   木属性抗性 "+real.wood_resist, size, getColor(R.color.wood), layout2);

        attr_text.addText("水属性 "+real.water + "   水属性抗性 "+real.water_resist, size, getColor(R.color.water), layout2);

        attr_text.addText("火属性 "+real.fire + "   火属性抗性 "+real.fire_resist, size, getColor(R.color.fire), layout2);

        attr_text.addText("土属性 "+real.soil + "   土属性抗性 "+real.soil_resist, size, getColor(R.color.soil), layout2);

        LinearLayout layout3 = new LinearLayout(getContext());
        layout3.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);

        layoutParams.weight = 0.7f;

        layoutParams.setMargins((int) (MainActivity.DP * 8), 0, 0, 0);

        layout2.setLayoutParams(layoutParams);

        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-2, -1);

        layoutParams2.weight = 0.3f;

        layout1.setLayoutParams(layoutParams2);

        layout3.addView(layout1);
        layout3.addView(layout2);
        attr_text.addView(layout3);

		attrs_num.setText("属性点："+base.attrs);
	}

	String getAptitudeString(int[] ap){
        StringBuilder sb = new StringBuilder(" ");
        int i = 0;
        for (int a : ap){
            if (a != 0)
            switch (i){
                case 0:
                    sb.append("金 ");
                    break;
                case 1:
                    sb.append("木 ");
                    break;
                case 2:
                    sb.append("水 ");
                    break;
                case 3:
                    sb.append("火 ");
                    break;
                case 4:
                    sb.append("土 ");
                    break;
            }
            i++;
        }

        sb.append("灵根");

        return sb.toString();
    }

	int getColor(int id){
        return getContext().getResources().getColor(id);
    }
	
	String explains[];
	void equip_scan(){
		final int[] types={Weapon.HEAD,Weapon.NECKLACE,Weapon.CLOTHES,Weapon.GLOVES,Weapon.SHOES,Weapon.WEAPON,Weapon.EARRINGS};
		explains=new String[7];
		Weapon[] ws=base.equipment;
		Weapon wp=null;
		Button btn;
		for(int i=0;i<types.length;i++){
			if(ws[types[i]]!=null){
				wp=ws[types[i]];
				btn=equip_buttons[types[i]];
				btn.setText(wp.name);
				if(wp.dura==0)btn.setTextColor(Color.rgb(100,100,100)); else
					btn.setTextColor(getContext().getResources().getColor(colors[types[i]]));
				explains[types[i]]=wp.getExplain();
			}else equip_buttons[types[i]].setText(getName(types[i]));

		}
	}

	String getName(int t){
		final String[] n={"武器","头部","耳环","项链","衣服","手套","鞋子",};
		return n[t];
	}
	
	ListDialog itemdia;
	
	int id1;
	
	@Override
	public void onClick(View v)
	{
		id1=v.getId();
		int type=IdtoType(id1);
		if(type==-1)return ;
		String explain=explains[type];
		if(explain==null || explain.equals("")) return ;
		itemdia=new ListDialog(getContext())
			.setListItem(new String[]{"查看","卸下"})
			.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener(){
				@Override
				public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4)
				{
					switch(p3){
						case 0:
							equip_scan();
							lookattr();
							itemdia.dismiss();
							break;
						case 1:
							unequip();
							equip_scan();
							itemdia.dismiss();
							break;
					}

				}
			})
			.show();
	}
	
	//查看装备详情
	void lookattr(){
		if(base.base.now_health>base.base.max_health)
			base.base.now_health=base.base.max_health;
		if(base.base.now_mp>base.base.max_mp)
			base.base.now_mp=base.base.max_mp;
		int type=IdtoType(id1);
		if(type==-1)return ;
		String explain=explains[type];
		//toast(""+base.equipment[type].dura);
		if(explain==null || explain.equals("")) return ;
		new MyDialog(getContext())
			.SetTitle(base.equipment[type].name)
			.setMessage(explain)
			//.setCancel("返回",null)
			.Show();
	}
	
	//卸下装备
	void unequip(){
		int type=IdtoType(id1);
		if(space.getSurplusNumber()>0){
			space.addWeapon(base.equipment[type]);
			base.equipment[type]=null;
		}else toast("空间已满，无法卸下装备！");
		attr_refresh();
	}
	
	NumberOperateDialog attr_nodialog;
	
	void addattr(final View v){
		String title="";
		
		if(base.attrs<1)return;
		
		switch(v.getId()){
			case R.id.roleattrButton2_ppower:
				title="每点增加3点生命";
				break;
			case R.id.roleattrButton2_power:
				title="每点增加2点攻击力和2点灵力";
				break;
			case R.id.roleattrButton2_def:
				title="每点增加1点防御力和1点生命";
				break;
			case R.id.roleattrButton2_speed:
				title="每点增加2点速度";
				break;
		}
		attr_nodialog=new NumberOperateDialog(getContext())
			.setTitle(title)
			.setText(base.attrs>0 ? "1" : "0")
			.setMessage("剩余"+(base.attrs>0 ? (base.attrs-1) : 0)+"点")
			.setInputNumberMax(base.attrs)
			.setInputWatcher(new NumberOperateDialog.InputTextWatcher(){
				@Override
				public void afterTextChanged(int i)
				{
					attr_nodialog.setMessage("剩余"+(base.attrs-attr_nodialog.getInputNumber())+"点");
				}
			})
			.setok("确认加点", new MyDialog.OnClickListener(){
				@Override
				public void onClick(MyDialog dialog)
				{
					switch(v.getId()){
						case R.id.roleattrButton2_ppower:
							base.health+=(attr_nodialog.getInputNumber()*3);
							break;
						case R.id.roleattrButton2_power:
							base.atk+=(attr_nodialog.getInputNumber()*2);
							base.mp+=(attr_nodialog.getInputNumber()*2);
							break;
						case R.id.roleattrButton2_def:
							base.def+=(attr_nodialog.getInputNumber());
							base.health+=(attr_nodialog.getInputNumber());
							break;
						case R.id.roleattrButton2_speed:
							base.speed+=(attr_nodialog.getInputNumber()*2);
							break;
					}
					base.attrs-=attr_nodialog.getInputNumber();
					attr_refresh();
				}
			})
			.setcancel("算了",null)
			.show();


	}
	
	//转换id
	int IdtoType(int id){
		switch(id)
		{
			default: return -1;
			case R.id.roleattrButton2_weapon: return Weapon.WEAPON;
			case R.id.roleattrButton2_head: return Weapon.HEAD;
			case R.id.roleattrButton2_necklace: return Weapon.NECKLACE;
			case R.id.roleattrButton2_clothes: return Weapon.CLOTHES;
			case R.id.roleattrButton2_earrings: return Weapon.EARRINGS;
			case R.id.roleattrButton2_gloves: return Weapon.GLOVES;
			case R.id.roleattrButton2_shoes: return Weapon.SHOES;
		}
	}
}
