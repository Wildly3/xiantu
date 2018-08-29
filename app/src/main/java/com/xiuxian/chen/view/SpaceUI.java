package com.xiuxian.chen.view;

import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;
import com.xiuxian.chen.*;
import com.xiuxian.chen.adapter.*;
import com.xiuxian.chen.include.*;
import com.xiuxian.chen.map.*;
import com.xiuxian.chen.set.*;
import java.util.*;
import android.os.*;
import com.google.gson.*;
import com.xiuxian.chen.data.*;

public class SpaceUI extends Fragment implements OnItemClickListener
{
	RadioGroup group;

	TextView size_text, attrs_num;

	RadioButton[] radiobtns;

	ListView mListView;

	MyImageAdapter adapter;
	
	ListDialog wliatdia;
	
	//当前空间类型
	int now_type;
	
	final int resid[]={
		R.id.rolespaceRadioButton1_equip,
		R.id.rolespaceRadioButton1_consunables,
		R.id.rolespaceRadioButton1_materials
	};
	
	Player base;
	
	com.xiuxian.chen.set.Space space;
	
	//是否为物品选择模式
	boolean isShelf;

    //是否为上架物品模式
    boolean isSell;

	ItemOnClick itemclick;
	
	RoleActivity role;
	
	public SpaceUI(RoleActivity role){
		this.role = role;
	}
	
	@Override
	public void onCreate(Bundle bundle)
	{
		super.onCreate(bundle);
		
		base = BaseActivity.base;
		
		space = BaseActivity.space;
		
		isShelf = bundle.getBoolean("shelf", false);

        isSell = bundle.getBoolean("sell", false);
		
		setContentView(R.layout.role_space);
		
		mListView = (ListView)findViewById(R.id.rolespaceListView1);
		
		adapter = new MyImageAdapter(getContext());
		
		mListView.setAdapter(adapter);
		
		mListView.setOnItemClickListener(this);
		
		group = (RadioGroup)findViewById(R.id.rolespaceRadioGroup1);
		
		size_text = (TextView)findViewById(R.id.rolespaceTextView1);
		
		radiobtns = new RadioButton[resid.length];
		
		for(int i=0;i<resid.length;i++)
			radiobtns[i]=(RadioButton)findViewById(resid[i]);

		group.setOnCheckedChangeListener(
		new android.widget.RadioGroup.OnCheckedChangeListener(){
				@Override
				public void onCheckedChanged(RadioGroup p1, int c)
				{
					radioClick(c);
				}
			});
			
		radiobtns[0].setChecked(true);
		
		radioClick(resid[0]);
		
		itemclick = new ItemOnClick();
	}

	//空间界面切换初始化
	void radioClick(int id){
		switch(id){
			case R.id.rolespaceRadioButton1_equip:
				goodinit(Item.KIND_EQUIP);
				break;
			case R.id.rolespaceRadioButton1_consunables:
				goodinit(Item.KIND_CONSUNABLES);
				break;
			case R.id.rolespaceRadioButton1_materials:
				goodinit(Item.KIND_MATERIAL);
				break;
		}
	}
	
	//选中
	void shelf(final int index){
		final Bundle bundle = getBundle();
		
		final Gson gson = new GsonBuilder().create();
		
		final SelectGoods sg = new SelectGoods();
		
		switch(now_type){
			case Item.KIND_EQUIP:
			sg.name = items[index].name;
				
			sg.index = index;
			
			sg.type = now_type;
			
			sg.number = 1;
			
			sg.quality = items[index].quality;
			
			sg.dura = items[index].dura;
			
			bundle.putString("select", gson.toJson(sg));
			
			exit();
			break;
			
			case Item.KIND_CONSUNABLES:
			case Item.KIND_MATERIAL:
			final NumberOperateDialog nodia = new NumberOperateDialog(getContext());
			
			final Item item = items[index];
			
			nodia.setTitle(item.name)
			.setInputNumberMin(1)
			.setInputNumberMax(item.quantity)
					.setok("确定", new MyDialog.OnClickListener(){
						@Override
						public void onClick(MyDialog dialog)
						{
							sg.name = item.name;

							sg.index = index;

							sg.type = now_type;

							sg.number = nodia.getInputNumber();
							
							bundle.putString("select", gson.toJson(sg));

							exit();
						}
					})
					.setcancel("取消", null)
					.show();
			break;
		}
	}

    //上架物品
    void SellGoods(final int index){
        final View view = LayoutInflater.from(getContext()).inflate(R.layout.editor_number, null);

        final EditText editText = (EditText)view.findViewById(R.id.EditorNumber_edittext);

        new MyDialog(getContext())
                .SetTitle("上架单价")
                .setView(view)
                .setSelect("上架", new MyDialog.OnClickListener() {
                    @Override
                    public void onClick(final MyDialog dialog) {
                        final String content = editText.getText().toString();
                        if(content.length() > 9){
                            toast("您输入的价格超出上限！");
                            return;
                        }
                        if(content.equals("")){
                            toast("请输入上架的单价！");
                            return;
                        }
                        final Item item = items[index];

                        if(item.dura != item.duramax && now_type == Item.KIND_EQUIP){
                            toast("未修理的装备无法上架！");
                            return;
                        }
                        final Gson gson = new GsonBuilder().create();
                        final SelectGoods sg = new SelectGoods();
                        final Bundle bundle = getBundle();
                        if(now_type == Item.KIND_EQUIP) {
                            sg.name = item.name;
                            sg.index = index;
                            sg.type = now_type;
                            sg.number = 1;
                            sg.quality = Integer.parseInt(content);
                            bundle.putString("select", gson.toJson(sg));
                            dialog.dismiss();
                            exit();
                        }else{
                            dialog.dismiss();
                            final NumberOperateDialog nodia = new NumberOperateDialog(getContext());
                            nodia.setTitle("输入上架数量")
                                    .setInputNumberMin(1)
                                    .setInputNumberMax(item.quantity)
                                    .setok("上架", new MyDialog.OnClickListener() {
                                        @Override
                                        public void onClick(MyDialog dia) {
                                            sg.name = item.name;
                                            sg.index = index;
                                            sg.type = now_type;
                                            sg.number = nodia.getInputNumber();
                                            sg.quality = Integer.parseInt(content);
                                            bundle.putString("select", gson.toJson(sg));
                                            exit();
                                        }
                                    })
                                    .setcancel("取消", null)
                                    .show();
                        }
                    }
                }, false)
                .setCancel("取消", null)
                .Show();
    }

	@Override
	public void onItemClick(AdapterView<?> p1, View v, int position, long p4)
	{
		if(isShelf){
			shelf(position);
			return ;
		}else
		    if(isSell)
            {
                SellGoods(position);
                return;
            }
		
		wliatdia = new ListDialog(getContext())
			.setOnItemClickListener(itemclick);
			
			itemclick.setType(now_type);
			
			index = position;
			
		switch(now_type){
			case Item.KIND_EQUIP:
				wliatdia
				.setListItem(isSellPosition()
				? new String[]{"查看","装备","出售","丢弃"}
				: new String[]{"查看","装备","丢弃"});
				break;

			case Item.KIND_CONSUNABLES:
					wliatdia.setListItem(
					isSellPosition()
					? new String[]{"使用","查看","出售","丢弃"} 
					: new String[]{"使用","查看","丢弃"});
				break;

			case Item.KIND_MATERIAL:
					wliatdia.setListItem(
					isSellPosition()
					? new String[]{"查看","出售","丢弃"}
					: new String[]{"查看","丢弃"});
				break;
		}
		
		wliatdia.show();
	}
	
	int index;
	
	Item[] items;
	
	void goodinit(int type)
	{
		List<MyImageAdapter.Item> item;
		
		now_type = type;
		
		switch (type)
		{
				//初始化装备
			case Item.KIND_EQUIP:
				if (BaseActivity.space.weapons.size() > 0)
				{
					//mListView.setOnItemClickListener(this);
					
					item = new ArrayList<>(BaseActivity.space.weapons.size());
					
					items = new Item[BaseActivity.space.weapons.size()];
					
					for (int i=0;i < BaseActivity.space.weapons.size();i++)
					{
						Weapon wp = BaseActivity.space.weapons.get(i);
						
						String qua="";
						
						switch (wp.quality)
						{
							case Weapon.QUALITY_INFELIOR:
								qua = "下品";
								break;
							case Weapon.QUALITY_MEDIUM:
								qua = "中品";
								break;
							case Weapon.QUALITY_FIRST_CLASS:
								qua = "上品";
								break;
							case Weapon.QUALITY_NONSUCH:
								qua = "极品";
								break;
						}
						
						item.add(MyImageAdapter.toItem(wp.icon, wp.name + "[" + qua + "]"));
						
						items[i] = wp;
					}
					adapter.setItem(item);
				}
				else adapter.clear();
				
				break;
				//初始化消耗品
			case Item.KIND_CONSUNABLES:
				if (BaseActivity.space.consunables.size() > 0)
				{
					//mListView.setOnItemClickListener(this);
					
					item = new ArrayList<>(BaseActivity.space.consunables.size());
					
					items = new Item[BaseActivity.space.consunables.size()];
					
					for (int i=0;i < BaseActivity.space.consunables.size();i++)
					{
						Consunables cb = BaseActivity.space.consunables.get(i);
						
						item.add(MyImageAdapter.toItem(cb.icon, cb.name + "[" + cb.quantity + "]"));
						
						//cb.name+" ["+cb.quantity+"]";
						items[i] = cb;
					}
					adapter.setItem(item);
				}
				else adapter.clear();
				
				break;
				//初始化材料
			case Item.KIND_MATERIAL:
				if (BaseActivity.space.materials.size() > 0)
				{
					//mListView.setOnItemClickListener(this);
					
					item = new ArrayList<>(BaseActivity.space.materials.size());
					
					items = new Item[BaseActivity.space.materials.size()];
					
					for (int i=0;i < BaseActivity.space.materials.size();i++)
					{
						Material mt = BaseActivity.space.materials.get(i);
						
						item.add(MyImageAdapter.toItem(mt.icon, mt.name + " [" + mt.quantity + "]"));
						
						items[i] = mt;
					}
					adapter.setItem(item);
				}
				else adapter.clear();
				break;
		}

		spacesize_refresh();
	}
	
	class ItemOnClick implements OnItemClickListener
	{
		public int type;

		public void setType(int type)
		{
			this.type = type;
		}

		public int getType()
		{
			return type;
		}
		
		@Override
		public void onItemClick(AdapterView<?> p1, View p2, int position, long p4)
		{
			wliatdia.dismiss();
			
			switch(position){
				case 0:
					if(type == Item.KIND_EQUIP || type == Item.KIND_MATERIAL){
						Look(index, type);
					}else Use(index, Item.KIND_CONSUNABLES);
				break;
				
				case 1:
					if(type == Item.KIND_EQUIP){
						Use(index, Item.KIND_EQUIP);
					}
					else
					if(type == Item.KIND_CONSUNABLES){
						Look(index, Item.KIND_CONSUNABLES);
					}
					else
					if(type == Item.KIND_MATERIAL){
						if(isSellPosition())
							Sell(index, Item.KIND_MATERIAL);
						else Discard(index, Item.KIND_MATERIAL);
					}
					break;
					
				case 2:
					if(type == Item.KIND_EQUIP || type == Item.KIND_CONSUNABLES){
						if(isSellPosition())
							Sell(index, type);
						else Discard(index, type);
					}else
					if(type == Item.KIND_MATERIAL){
						Discard(index, type);
					}
					break;
					
				case 3:
					Discard(index, type);
					break;
				
			}
		}
	}
	
	//查看详情
	void Look(int index, int type){
		String msg = null;
		switch(type){
			case Item.KIND_EQUIP:
			msg = ((Weapon)items[index]).getExplain();
			break;
			
			case Item.KIND_CONSUNABLES:
			case Item.KIND_MATERIAL:
			msg = items[index].explain;
			break;
		}
		
		new MyDialog(getContext())
			.SetTitle(items[index].name)
			.setMessage(msg)
			.setCancel("返回",null)
			.show();
	}

	//使用
	void Use(int index, int type){
		switch (type)
		{
			//装备
			case Item.KIND_EQUIP:
				Weapon sw = space.weapons.get(index);
				
				if (sw.applylv <= base.level)
				{
					if (base.equipment[sw.type] == null)
						base.equipment[sw.type] = space.takeout_weapon(index);
					else
					{
						Weapon wp = base.equipment[sw.type];

						base.equipment[sw.type] = space.takeout_weapon(index);

						space.addWeapon(wp);
					}}
				else toast("等级过低，无法装备！");
				goodinit(Item.KIND_EQUIP);
				break;
				
				//消耗品
			case Item.KIND_CONSUNABLES:
				final Consunables con = (Consunables)items[index];
				
				if(con.road != Consunables.ENTIRE && con.road != Consunables.REST){
					toast("只能在战斗中使用！");
					break;
				}
				if(base.level < con.applylv){
					toast("等级过低无法使用！");
					break;
				}
				useCon();
				break;

				//材料
			case Item.KIND_MATERIAL:
				
				break;
		}
		
		if(role != null)
			role.showTitle();
	}
	
	//出售
	void Sell(final int index, final int type){
		final NumberOperateDialog nodialog = new NumberOperateDialog(getContext());
		final Item item = items[index];
		nodialog
			.setInputNumberMax(item.quantity)

			.setMessage("售价：" + item.price)

			.setInputWatcher(new NumberOperateDialog.InputTextWatcher(){
				@Override
				public void afterTextChanged(int i)
				{
					nodialog.setMessage("售价：" + item.price * i);
				}
			})
			
			.setok("出售", new MyDialog.OnClickListener(){
				@Override
				public void onClick(MyDialog dialog)
				{
					int num = nodialog.getInputNumber();
					
					if(num<1)return;
					
					int money = item.price * num;
					
					switch (type)
					{
						case Item.KIND_EQUIP:
							space.takeout_weapon(index);
							goodinit(Item.KIND_EQUIP);
							break;

						case Item.KIND_CONSUNABLES:
							space.takeout_consunables(index, num);
							goodinit(Item.KIND_CONSUNABLES);
							break;

						case Item.KIND_MATERIAL:
							space.takeout_material(index, num);
							goodinit(Item.KIND_MATERIAL);
							break;
					}
					space.addMoney(money);
					
					if(role != null)
					role.showTitle();
					
					toast("卖出"+money+"金币");
				}
			})
			.setcancel("算了",null)
			.show();
	}
	
	//丢弃
	void Discard(final int index, final int type)
	{
        final Item item = items[index];

		switch (type)
		{
			case Item.KIND_EQUIP:
			    new MyDialog(getContext())
                        .SetTitle("提示")
                        .setMessage("是否要丢弃 " + item.name + "该装备？")
                        .setCancel("丢弃", new MyDialog.OnClickListener() {
                            @Override
                            public void onClick(MyDialog dialog) {
                                space.takeout_weapon(index);
                                goodinit(Item.KIND_EQUIP);
                            }
                        })
                        .setSelect("取消", null)
                        .show();
				break;

			case Item.KIND_CONSUNABLES:
            case Item.KIND_MATERIAL:
                final NumberOperateDialog nodia = new NumberOperateDialog(getContext());
                nodia.setTitle("丢弃数量")
                        .setInputNumberMax(item.quantity)
                        .setok("丢弃", new MyDialog.OnClickListener() {
                            @Override
                            public void onClick(MyDialog dialog) {
                                if (type == Item.KIND_CONSUNABLES){
                                    space.takeout_consunables(index, nodia.getInputNumber());
                                    goodinit(Item.KIND_CONSUNABLES);
                                } else {
                                    space.takeout_material(index, nodia.getInputNumber());
                                    goodinit(Item.KIND_MATERIAL);
                                }

                            }
                        })
                        .setcancel("取消", null)
                        .show();
				break;
		}
	}

	//消耗品的使用
	void useCon()
	{
		final Consunables con = space.consunables.get(index);

		final PlayerAll pall = new PlayerAll();

		pall.setGood(con);

		switch (con.usetype)
		{
			default:
			case 0:
				final TextButtonDialog use_textbuttondialog=new TextButtonDialog(getContext());
				use_textbuttondialog
					.setText(getattr())
					.setButtonText("使用(" + con.quantity + ")")
					.setNegativeButton("关闭", new MyDialog.OnClickListener(){
						@Override
						public void onClick(MyDialog dialog)
						{
							goodinit(Item.KIND_CONSUNABLES);
						}
					})
					.setOnclickListener(new View.OnClickListener(){
						@Override
						public void onClick(View p1)
						{
							int num = 0;

							num = con.quantity;

                            try {
                                if (!con.conuse.use(space, base, base, pall)) return;
                            }catch (Exception e){
                                ErrorHandle.doError(e);
                                return;
                            }
							
							space.takeout_consunables(index, 1);
							
							use_textbuttondialog.setButtonText("使用(" + (num - 1) + ")");
							
							use_textbuttondialog.setText(getattr());
							
							if ((num - 1) <= 0)
							{
								use_textbuttondialog.dismiss();
								
								goodinit(Item.KIND_CONSUNABLES);
							}
						}
					})
					.show();
				break;

			case 1:
			    try {
                    if (!con.conuse.use(space, base, base, pall)) break;
                }catch (Exception e){
                    ErrorHandle.doError(e);
                    return;
                }
				
				space.takeout_consunables(index, 1);
				
				goodinit(Item.KIND_CONSUNABLES);
				break;

		}

	}

	String getattr(){
		String msg=
			"等级 "+base.level+
			"\n经验 "+base.exp+"/"+base.max_exp+
			"\n气血 "+base.base.now_health+"/"+base.base.max_health+
			"\n灵力 "+base.base.now_mp+"/"+base.base.max_mp+
			"\n攻击 "+base.base.atk+
			"\n防御 "+base.base.def+
			"\n速度 "+base.base.speed+
			"\n气运 "+base.base.luck;
		return msg;
	}
	
	//刷新
	void spacesize_refresh(){
		if(size_text!=null)
			size_text.setText(""+space.getNumbers()+"/"+space.space_max);
	}
	
	//判断当前是否为出售点
	boolean isSellPosition(){
		MyMap.MapItem mi = BaseActivity.mapmanager
		
		.getActionNowMap(BaseActivity.map_action)
		
		.getNowPositionMapItem(BaseActivity.map_action);
		
		if(mi==null)return false;
		
		if(mi.flag==MyMap.MapItem.NPC_SHOP_SELL)return true;
		
		return false;
	}
	
}
