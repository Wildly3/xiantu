package com.xiuxian.chen.view;
import android.content.Context;
import android.os.*;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import cn.bmob.v3.*;
import cn.bmob.v3.exception.*;
import cn.bmob.v3.listener.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xiuxian.chen.*;
import com.xiuxian.chen.adapter.*;
import com.xiuxian.chen.data.SelectGoods;
import com.xiuxian.chen.include.*;
import com.xiuxian.chen.other.*;
import com.xiuxian.chen.set.Consunables;
import com.xiuxian.chen.set.Item;
import com.xiuxian.chen.set.Material;
import com.xiuxian.chen.set.Weapon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ShoppingUI extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener
{
    private static final String TAG = "ShoppingUI";

	private static final String USER_OBJECTID = "664bf87412";

    public static final String
    MONEYDF = "金币不足！",
    SPACEDF = "空间不足",
    BUYSUCCESS = "购买成功";
	
	TextView title;
	
	Spinner mSpinner;

    Button btn_shlf;

    ListView ListView_search;
	
	static final String[] type = {
		"装备",
		"消耗品",
		"材料"
	};

	EditText edit_search;

    int selectType;

    List<EquipGoods> equips;

    List<ConsunablesGoods> cons;

    List<MaterialGoods> mats;

    TitleAdapter adapter;

    String search;

	@Override
	protected void onCreate(Bundle bundle)
	{
		super.onCreate(bundle);
		
		MainActivity.hideBottom();
		
		setContentView(R.layout.shopping);
		
		title = (TextView)findViewById(R.id.shoppingTextView1_myid);
		
		mSpinner = (Spinner)findViewById(R.id.shoppingSpinner1_type);

        adapter = new TitleAdapter(getContext());

        mSpinner.setOnItemSelectedListener(this);

        btn_shlf = (Button)findViewById(R.id.shoppingButton1_shelf);

        edit_search = (EditText) findViewById(R.id.shoppingEditText1_search);

        ListView_search = (ListView) findViewById(R.id.shoppingListView1_list);

        ListView_search.setAdapter(adapter);

        ListView_search.setOnItemClickListener(this);

        edit_search.setOnEditorActionListener(new MyEditor());

        btn_shlf.setOnClickListener(this);
		
		mSpinner.setAdapter(new MyAdapter(getContext(), type));
		
		MyIdInit();
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		
		MainActivity.showBottom();
	}
	
	//id获取
	void MyIdInit(){
		final Shoping shop = Shoping.getInstance();
		
		final int id = shop.getId();
		
		if(id == -1){
			final BmobQuery<MyUser> userQuery = new BmobQuery<>();
			
			userQuery.getObject(USER_OBJECTID, new QueryListener<MyUser>(){
					@Override
					public void done(final MyUser user, BmobException e)
					{
						if(e == null){
							user.setId(user.getId()+1);
							
							user.update(USER_OBJECTID, new UpdateListener(){
									@Override
									public void done(BmobException e)
									{
										if(e == null){
                                            shop.setId(user.getId() - 1);

											title.setText("我的id："+shop.getId());
											
											MainActivity.MainPressenter.SaveArchive();
										}else
										{
											MyToast.makeText(getActivity(), "id更新失败！", Toast.LENGTH_SHORT).show();
										}
									}
								});
							
						}else
						{
							MyToast.makeText(getActivity(), "id获取失败！", Toast.LENGTH_SHORT).show();
                        }
					}
				});
		}else
		//反之直接显示已存储的id
		{
			title.setText("我的id："+shop.getId());
		}
		
		
	}

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectType = position;
        adapter.clear();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //购买
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (selectType){
            case 0:
                buyEquip(position);
                break;
            case 1:
                buyConsunables(position);
                break;
            case 2:
                buyMaterial(position);
                break;
        }
    }

    //购买装备
    void buyEquip(final int position){
        final EquipGoods goods = equips.get(position);

        new MyDialog(getContext())
                .SetTitle("购买")
                .setMessage(goods.name+" "+goods.price+"金币 是否购买？")
                .setSelect("是", new MyDialog.OnClickListener() {
                    @Override
                    public void onClick(MyDialog dialog) {
                        if(BaseActivity.space.getMoney() < goods.price){
                            toast(MONEYDF);
                            return;
                        }

                        if(BaseActivity.space.getSurplusNumber() < 1){
                            toast(SPACEDF);
                            return;
                        }

                        SelectGoods goodmsg = new SelectGoods();
                        goodmsg.name = goods.name;
                        goodmsg.type = Item.KIND_EQUIP;

                        sendMoneyMail(goods.id, goods.price, goodmsg, new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if(e == null){
                                    BaseActivity.space.addMoney(-goods.price);
                                    goods.delete(new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            if(e == null){
                                                Weapon wp = BaseActivity.ScanWeapon(goods.name);
                                                if(wp != null){
                                                    wp = wp.NewObject();
                                                    wp.quality = goods.quality;
                                                    BaseActivity.space.addWeapon(wp);
                                                    search(search);
                                                    toast(BUYSUCCESS);
                                                    MainActivity.MainPressenter.SaveArchive();
                                                }
                                            }
                                        }
                                    });
                                }
                            }
                        });

                    }
                })
                .setCancel("否", null)
                .show();
    }

    //购买消耗品
    void buyConsunables(final int position){
        final ConsunablesGoods goods = cons.get(position);
        InputNumberDialog(goods.number, new MyDialog.OnClickListener() {
            @Override
            public void onClick(MyDialog dialog) {
                final NumberOperateDialog nodia = (NumberOperateDialog) dialog.getTag();
                final int number = nodia.getInputNumber();
                new MyDialog(getContext())
                        .SetTitle("购买")
                        .setMessage(goods.name+"\n数量："+number + "\n合计：" + (goods.price * number) + "金币"
                        + "\n是否购买？"
                        )
                        .setSelect("确定", new MyDialog.OnClickListener() {
                            @Override
                            public void onClick(MyDialog dialog) {
                                final int money = goods.price * number;

                                if(BaseActivity.space.getMoney() < money){
                                    toast(MONEYDF);
                                    return;
                                }

                                if(BaseActivity.space.getSurplusNumber() < number){
                                    toast(SPACEDF);
                                    return;
                                }

                                SelectGoods goodmsg = new SelectGoods();
                                goodmsg.name = goods.name;
                                goodmsg.type = Item.KIND_CONSUNABLES;
                                goodmsg.number = number;

                                sendMoneyMail(goods.id, money, goodmsg, new SaveListener<String>() {
                                    @Override
                                    public void done(String s, BmobException e) {
                                        if(e == null){
                                            BaseActivity.space.addMoney(-money);
                                            final UpdateListener update =  new UpdateListener() {
                                                @Override
                                                public void done(BmobException e) {
                                                    if(e == null){
                                                        Consunables con = BaseActivity.ScanConsunables(goods.name);
                                                        if(con != null){
                                                            con = con.NewObject();
                                                            con.quantity = number;
                                                            BaseActivity.space.addConsunables(con);
                                                            search(search);
                                                            toast(BUYSUCCESS);
                                                            MainActivity.MainPressenter.SaveArchive();
                                                        }
                                                    }
                                                }
                                            };

                                            if(number == goods.number){
                                                goods.delete(update);
                                            }else{
                                                goods.number -= number;
                                                goods.update(update);
                                            }

                                        }
                                    }
                                });

                            }
                        })
                        .setCancel("取消", null)
                        .show();

            }
        });
    }

    //购买材料
    void buyMaterial(final int position){
        final MaterialGoods goods = mats.get(position);
        InputNumberDialog(goods.number, new MyDialog.OnClickListener() {
            @Override
            public void onClick(MyDialog dialog) {
                final NumberOperateDialog nodia = (NumberOperateDialog) dialog.getTag();
                final int number = nodia.getInputNumber();
                new MyDialog(getContext())
                        .SetTitle("购买")
                        .setMessage(goods.name+"\n数量："+number + "\n合计：" + (goods.price * number) + "金币"
                                + "\n是否购买？"
                        )
                        .setSelect("确定", new MyDialog.OnClickListener() {
                            @Override
                            public void onClick(MyDialog dialog) {
                                final int money = goods.price * number;

                                if(BaseActivity.space.getMoney() < money){
                                    toast(MONEYDF);
                                    return;
                                }

                                if(BaseActivity.space.getSurplusNumber() < number){
                                    toast(SPACEDF);
                                    return;
                                }

                                SelectGoods goodmsg = new SelectGoods();
                                goodmsg.name = goods.name;
                                goodmsg.type = Item.KIND_MATERIAL;
                                goodmsg.number = number;

                                sendMoneyMail(goods.id, money, goodmsg, new SaveListener<String>() {
                                    @Override
                                    public void done(String s, BmobException e) {
                                        if(e == null){
                                            BaseActivity.space.addMoney(-money);
                                            final UpdateListener update =  new UpdateListener() {
                                                @Override
                                                public void done(BmobException e) {
                                                    if(e == null){
                                                        Material material = BaseActivity.ScanMaterial(goods.name);
                                                        if(material != null){
                                                            material = material.NewObject();
                                                            material.quantity = number;
                                                            BaseActivity.space.addMaterail(material);
                                                            search(search);
                                                            toast(BUYSUCCESS);
                                                            MainActivity.MainPressenter.SaveArchive();
                                                        }
                                                    }
                                                }
                                            };

                                            if(number == goods.number){
                                                goods.delete(update);
                                            }else{
                                                goods.number -= number;
                                                goods.update(update);
                                            }

                                        }
                                    }
                                });

                            }
                        })
                        .setCancel("取消", null)
                        .show();

            }
        });
    }

    //输入数量
    void InputNumberDialog(int inputmax, MyDialog.OnClickListener listener){
        final NumberOperateDialog nodia = new NumberOperateDialog(getContext());
        nodia.setTitle("购买数量")
                .setInputNumberMax(inputmax)
                .setok("确定", listener)
                .setcancel("取消", null)
                .show();
    }

    //发送金币邮件
    void sendMoneyMail(final int id, final int money, SelectGoods goodmsg, final SaveListener<String> listener){
        final Gson gson = new GsonBuilder().create();
        Mail mail = new Mail();
        mail.pid = Shoping.ADMINISTRATOR;
        mail.id = id;
        mail.msg = "您上架的物品 "+goodmsg.name+
                "已售出"+(goodmsg.type != Item.KIND_EQUIP ? (" ："+goodmsg.number+" ") : "！")+"感谢您的使用！";
        SelectGoods sg = new SelectGoods();
        sg.type = Item.KIND_MONEY;
        sg.number = money;
        mail.goods = gson.toJson(sg, SelectGoods.class);
        mail.save(listener);
    }

    //搜索
    private class MyEditor implements android.widget.TextView.OnEditorActionListener
    {
        @Override
        public boolean onEditorAction(TextView tv, int actionId, KeyEvent event) {
            if (!(actionId == EditorInfo.IME_ACTION_SEARCH
                    || event.getKeyCode() == KeyEvent.ACTION_DOWN)) {
                return true;
            }

            closeKey();

            final String content = search = edit_search.getText().toString();

            search(content);

            return true;
        }

    }

    void search(final String content){
        switch (selectType){
            case 0:
                BmobQuery<EquipGoods> query = new BmobQuery<>();
                query.addWhereEqualTo("name", content);
                query.findObjects(new FindListener<EquipGoods>() {
                    @Override
                    public void done(List<EquipGoods> list, BmobException e) {
                        if(e == null && list != null && list.size() > 0){
                            Collections.sort(list, new Comparator<EquipGoods>() {
                                @Override
                                public int compare(EquipGoods o1, EquipGoods o2) {
                                    return o1.price - o2.price;
                                }
                            });
                            equips = list;

                            List<TitleAdapter.InfoMation> item = new ArrayList<>(list.size());

                            TitleAdapter.InfoMation info = null;

                            for(EquipGoods eg : list){
                                info = new TitleAdapter.InfoMation();
                                info.title = eg.publisher;
                                StringBuilder sb = new StringBuilder(eg.name);
                                sb.append(" [");
                                sb.append(Item.getQualityString(eg.quality));
                                sb.append("]");
                                sb.append(" 价格："+eg.price);
                                info.msg = sb.toString();
                                item.add(info);
                            }

                            adapter.set(item);
                        }else failshow();
                    }
                });
                break;
            case 1:
                BmobQuery<ConsunablesGoods> query2 = new BmobQuery<>();
                query2.addWhereEqualTo("name", content);
                query2.findObjects(new FindListener<ConsunablesGoods>() {
                    @Override
                    public void done(List<ConsunablesGoods> list, BmobException e) {
                        if(e == null && list != null && list.size() > 0){
                            Collections.sort(list, new Comparator<ConsunablesGoods>() {
                                @Override
                                public int compare(ConsunablesGoods o1, ConsunablesGoods o2) {
                                    return o1.price - o2.price;
                                }
                            });
                            cons = list;

                            List<TitleAdapter.InfoMation> item = new ArrayList<>(list.size());

                            TitleAdapter.InfoMation info = null;

                            for(ConsunablesGoods eg : list){
                                info = new TitleAdapter.InfoMation();
                                info.title = eg.publisher;
                                StringBuilder sb = new StringBuilder(eg.name);
                                sb.append(" [");
                                sb.append(eg.number);
                                sb.append("]");
                                sb.append(" 价格："+eg.price);
                                info.msg = sb.toString();
                                item.add(info);
                            }

                            adapter.set(item);
                        }else failshow();
                    }
                });
                break;
            case 2:
                BmobQuery<MaterialGoods> query3 = new BmobQuery<>();
                query3.addWhereEqualTo("name", content);
                query3.findObjects(new FindListener<MaterialGoods>() {
                    @Override
                    public void done(List<MaterialGoods> list, BmobException e) {
                        if(e == null && list != null && list.size() > 0){
                            Collections.sort(list, new Comparator<MaterialGoods>() {
                                @Override
                                public int compare(MaterialGoods o1, MaterialGoods o2) {
                                    return o1.price - o2.price;
                                }
                            });
                            mats = list;

                            List<TitleAdapter.InfoMation> item = new ArrayList<>(list.size());

                            TitleAdapter.InfoMation info = null;

                            for(MaterialGoods eg : list){
                                info = new TitleAdapter.InfoMation();
                                info.title = eg.publisher;
                                StringBuilder sb = new StringBuilder(eg.name);
                                sb.append(" [");
                                sb.append(eg.number);
                                sb.append("]");
                                sb.append(" 价格："+eg.price);
                                info.msg = sb.toString();
                                item.add(info);
                            }

                            adapter.set(item);
                        }else failshow();
                    }
                });
                break;

        }
    }

    //搜索失败提示
    void failshow(){
        //toast("搜索失败！");
        adapter.clear();
    }

    void closeKey(){
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(edit_search.getWindowToken(), 0);
    }

    @Override
    public void onClick(View v) {
        startFragment(new SellUI());
    }
}
