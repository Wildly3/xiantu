package com.xiuxian.chen.view;
import android.os.*;
import android.view.*;
import android.widget.*;
import cn.bmob.v3.exception.*;
import cn.bmob.v3.listener.*;
import com.google.gson.*;
import com.xiuxian.chen.*;
import com.xiuxian.chen.adapter.*;
import com.xiuxian.chen.data.*;
import com.xiuxian.chen.include.*;
import com.xiuxian.chen.other.*;
import com.xiuxian.chen.set.*;
import com.xiuxian.chen.set.Space.*;
import java.util.*;
import android.util.*;

public class SendMailUI extends Fragment implements View.OnClickListener, SlideCutListView.RemoveListener
{
	EditText edit_id;
	
	EditText edit_msg;
	
	Button send;
	
	Button btn_addgoods;

    Button btn_addmoney;
	
	List<SelectGoods> selects;
	
	SlideCutListView mListView;
	
	boolean isSend;
	
	@Override
	protected void onCreate(Bundle bundle)
	{
		super.onCreate(bundle);
		
		setContentView(R.layout.sendmail);
		
		edit_id = (EditText)findViewById(R.id.sendmailEditText1_id);
		
		edit_msg = (EditText)findViewById(R.id.sendmailEditText1_msg);
		
		send = (Button)findViewById(R.id.sendmailButton1_send);
		
		btn_addgoods = (Button)findViewById(R.id.sendmailButton1_addgoods);

        btn_addmoney = (Button)findViewById(R.id.sendmailButton1_addmoney);
		
		mListView = (SlideCutListView)findViewById(R.id.sendmailListView1_goods);
		
		mListView.setRemoveListener(this);
		
		send.setOnClickListener(this);
		
		btn_addgoods.setOnClickListener(this);

        btn_addmoney.setOnClickListener(this);
		
		selects = new ArrayList<>();
	}
	
	@Override
	public void onClick(View v)
	{
		switch(v.getId()){
			case R.id.sendmailButton1_send:
				sendMail();
			break;
			
			case R.id.sendmailButton1_addgoods:
				Bundle bundle = new Bundle();
				
				bundle.putBoolean("shelf", true);
				
				startFragment(new SpaceUI(null), bundle);
			break;

            case R.id.sendmailButton1_addmoney:
                addmoney();
                break;
		}
		
	}

	//添加金币
	void addmoney(){
        final NumberOperateDialog nodia = new NumberOperateDialog(getContext());

        nodia.setTitle("添加金币")
                .setInputNumberMin(1)
                .setInputNumberMax(BaseActivity.space.getMoney())
                .setok("确定", new MyDialog.OnClickListener() {
                    @Override
                    public void onClick(MyDialog dialog) {
                        int number = nodia.getInputNumber();
                        SelectGoods sg = new SelectGoods();
                        sg.type = Item.KIND_MONEY;
                        sg.number = number;

                        selects.add(sg);
                        ShowAddGoods();
                    }
                })
                .setcancel("取消", null)
                .show();

    }
	
	//发送邮件
	void sendMail(){
		if(isSend) return ;
		
		String input_id = edit_id.getText().toString();

		String input_msg = edit_msg.getText().toString();

		if(input_id.equals("")){
			MyToast.makeText(getActivity(), "请输入对方id！", Toast.LENGTH_SHORT).show();
			return ;
		}

		if(input_msg.equals("")){
			MyToast.makeText(getActivity(), "请输入邮件内容！", Toast.LENGTH_SHORT).show();
			return ;
		}

		int myid = Shoping.getInstance().getId();

		String name = BaseActivity.base.name;

		if(myid == -1){
			MyToast.makeText(getActivity(), "您未获取id，请打开寄卖行！", Toast.LENGTH_SHORT).show();
			return ;
		}

		int id = Integer.parseInt(input_id);

		Mail mail = new Mail();

		mail.id = id;

		mail.pid = myid;

		mail.publisher = name;

		mail.msg = input_msg;
		
		StringBuilder sb = new StringBuilder("");
		
		if(selects.size() > 0)
		{
			Gson gson = new GsonBuilder().create();
			
			for(SelectGoods sg : selects){
				
				sb.append(gson.toJson(sg, SelectGoods.class));
				
				sb.append("\n");
			}
			
			mail.goods = sb.toString();
		}
		else
			mail.goods = "null";

		isSend = true;
		
		mail.save(new SaveListener<String>(){
				@Override
				public void done(String objectid, BmobException e)
				{
					isSend = false;
					
					if(e == null){
						MyToast.makeText(getActivity(), "发送成功！", Toast.LENGTH_SHORT).show();

						edit_id.setText("");

						edit_msg.setText("");
						
						boolean isselect = selects.size() > 0;
						
						List<Position> equip = new ArrayList<>(selects.size());
						
						List<Position> con = new ArrayList<>(selects.size());
						
						List<Position> mat = new ArrayList<>(selects.size());
						
						Position pos = null;
						
						for(SelectGoods sg : selects){
							switch(sg.type){
								case Item.KIND_EQUIP:
								pos = new Position();
								pos.index = sg.index;
								equip.add(pos);
								break;
								case Item.KIND_CONSUNABLES:
									pos = new Position();
									pos.index = sg.index;
									pos.quantity = sg.number;
									con.add(pos);
								break;
								case Item.KIND_MATERIAL:
									pos = new Position();
									pos.index = sg.index;
									pos.quantity = sg.number;
									mat.add(pos);
								break;
                                case Item.KIND_MONEY:
                                    BaseActivity.space.addMoney(-sg.number);
                                    break;
							}
							
						}
						
						BaseActivity.space.takeout_weapon(equip);
						
						BaseActivity.space.takeout_consunables(con);
						
						BaseActivity.space.takeout_material(mat);
						
						selects.clear();
						
						if(adapter != null)
						adapter.clear();
						
						if(isselect){
							MainActivity.MainPressenter.SaveArchive();
						}
					}
					else{
							//Log.i("err", e.getMessage());
							MyToast.makeText(getActivity(), "发送失败！", Toast.LENGTH_SHORT).show();
						}
				}
			});
	}

	@Override
	public void Callback(Bundle bundle)
	{
		super.Callback(bundle);
		
		String obj = bundle.getString("select", null);
		
		if(obj == null) return ;
		
		Gson gson = new GsonBuilder().create();
		
		SelectGoods sg = gson.fromJson(obj, SelectGoods.class);
		
		for(SelectGoods sg1 : selects){
			if(sg1.equals(sg)){
				toast("已经选择过该物品！");
				return ;
				}
		}
		
		selects.add(sg);
		
		ShowAddGoods();
	}
	
	MyAdapter adapter;
	
	void ShowAddGoods(){
		String[] item = new String[selects.size()];
		int i = 0;
		for(SelectGoods sg : selects){
			if(sg.type == Item.KIND_CONSUNABLES
			|| sg.type == Item.KIND_MATERIAL)
			item[i] = sg.name+"["+sg.number+"]";
			else if(sg.type == Item.KIND_EQUIP)
			item[i] = sg.name;
            else item[i] = "金币 "+sg.number;
			i++;
		}
		mListView.setAdapter(adapter = new MyAdapter(getContext(), item));
	}
	
	@Override
	public void removeItem(SlideCutListView.RemoveDirection direction, int position)
	{
		adapter.remove(position);
		selects.remove(position);
	}
	
}
