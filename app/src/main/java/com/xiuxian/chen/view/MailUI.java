package com.xiuxian.chen.view;
import android.os.*;
import android.view.*;
import android.widget.*;
import cn.bmob.v3.*;
import cn.bmob.v3.exception.*;
import cn.bmob.v3.listener.*;
import com.xiuxian.chen.*;
import com.xiuxian.chen.adapter.*;
import com.xiuxian.chen.include.*;
import com.xiuxian.chen.other.*;
import java.util.*;
import com.xiuxian.chen.data.*;
import com.google.gson.*;
import com.xiuxian.chen.set.*;
import android.graphics.*;

public class MailUI extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener
{
	Button btn_send;
	
	Button btn_refresh;
	
	ListView mListView;
	
	List<Mail> mails;
	
	@Override
	protected void onCreate(Bundle bundle)
	{
		super.onCreate(bundle);
		
		MainActivity.hideBottom();
		
		setContentView(R.layout.mail);
		
		btn_send = (Button)findViewById(R.id.mailButton1_send);
		
		btn_refresh = (Button)findViewById(R.id.mailButton1_ref);
		
		mListView = (ListView)findViewById(R.id.mailListView1);
		
		btn_send.setOnClickListener(this);
		
		btn_refresh.setOnClickListener(this);
		
		mListView.setOnItemClickListener(this);
		
		MailShow();
	}
	
	@Override
	public void onItemClick(AdapterView<?> p1, View v, int position, long p4)
	{
		final Mail mail = mails.get(position);
		
		ClickTextView textview = new ClickTextView(getContext());
		
		final int aColor = Color.WHITE;
		
		final float aSize = 25f;

		textview.addText("发件人：" + (mail.pid == Shoping.ADMINISTRATOR ? "系统管理员" :  mail.publisher), null, aSize, aColor);

        if(mail.pid != Shoping.ADMINISTRATOR)
		textview.addText("发件人id：" + mail.pid, null, aSize, aColor);
		
		textview.addText("内容：", null, aSize, aColor);
		
		textview.addText(mail.msg, null, 18f, Color.YELLOW);
		
		final List<SelectGoods> sgoods = new ArrayList<>();

		if(!mail.goods.equals("null")){
			
			textview.addText("附件：", null, aSize, aColor);

			String[] goods = mail.goods.split("\n");

			Gson gson = new GsonBuilder().create();

			final int gColor = Color.RED;
			
			final float gSize = 18f;
			
			for(String json : goods){
				SelectGoods sg = gson.fromJson(json, SelectGoods.class);

				switch(sg.type){
					case Item.KIND_EQUIP:
						Weapon wp = BaseActivity.ScanWeapon(sg.name);
						if(wp != null){
                            wp = wp.NewObject();
                            wp.dura = sg.dura;
                            wp.quality = sg.quality;
							textview.addText(sg.name, wp.getExplain(), gSize, gColor);
						}
						else
						{
							textview.addText(sg.name+"[无效]", null, gSize, gColor);
						}

						break;
					case Item.KIND_CONSUNABLES:
						Consunables con = BaseActivity.ScanConsunables(sg.name);
						if(con != null) {
                            String explain = sg.name + "\n数量；" + sg.number + "\n";
                            explain += con.getExplain();
                            textview.addText(sg.name, explain, gSize, gColor);
                        }
						else
						{
							textview.addText(sg.name+"[无效]", null, gSize, gColor);
						}
						break;
					case Item.KIND_MATERIAL:
						Material mat = BaseActivity.ScanMaterial(sg.name);
						if(mat != null) {
                            String explain = sg.name + "\n数量；" + sg.number + "\n";
                            explain += mat.getExplain();
                            textview.addText(sg.name, mat.getExplain(), gSize, gColor);
                        }
						else
						{
							textview.addText(sg.name+"[无效]", null, gSize, gColor);
						}
						break;
                    case Item.KIND_MONEY:
                            textview.addText("金币 "+sg.number, null, gSize, gColor);
                        break;
				}

				sgoods.add(sg);
			}
		}
		
		
		new MyDialog(getContext())
			.SetTitle("邮件")
			.setView(textview)
			.setSelect("收取", new MyDialog.OnClickListener(){
				@Override
				public void onClick(MyDialog dialog)
				{
					mail.delete(new UpdateListener(){
							@Override
							public void done(BmobException e)
							{
								if (e == null)
								{
									if (!mail.goods.equals("null"))
									{
										for (SelectGoods sg : sgoods)
											switch (sg.type)
											{
												case Item.KIND_EQUIP:
													Weapon wp = BaseActivity.ScanWeapon(sg.name);
													if (wp != null)
													{
														wp = wp.NewObject();
														wp.quality = sg.quality;
														wp.dura = sg.dura;
														BaseActivity.space.addWeapon(wp);
													}
													break;
												case Item.KIND_CONSUNABLES:
													Consunables con = BaseActivity.ScanConsunables(sg.name);
													if (con != null)
													{
														con = con.NewObject();
														con.quantity = sg.number;
														BaseActivity.space.addConsunables(con);
													}
													break;
												case Item.KIND_MATERIAL:
													Material mat = BaseActivity.ScanMaterial(sg.name);
													if (mat != null)
													{
														mat = mat.NewObject();
														mat.quantity = sg.number;
														BaseActivity.space.addMaterail(mat);
													}
													break;
                                                case Item.KIND_MONEY:
                                                    BaseActivity.space.addMoney(sg.number);
                                                    break;
											}

									}

									MailShow();

									MainActivity.MainPressenter.SaveArchive();
								}else
								toast("邮件收取失败！");
							}
						});
				}
			})
		.setCancel("关闭", null)
		.Show();
	}
	
	void MailDelete(Mail mail){
		mail.delete(new UpdateListener(){
				@Override
				public void done(BmobException e)
				{
					if(e == null){
						MailShow();
						MainActivity.MainPressenter.SaveArchive();
					}
				}
			});
	}
	
	//刷新邮件
	void MailShow(){
		final int id = Shoping.getInstance().getId();
		
		if(id == -1){
			MyToast.makeText(getActivity(), "您未获取id，请打开寄卖行！", Toast.LENGTH_SHORT).show();
			
			return ;
		}
		
		BmobQuery<Mail> bm = new BmobQuery<>();
		
		bm.addWhereEqualTo("id", id);
		
		bm.findObjects(new FindListener<Mail>(){
				@Override
				public void done(List<Mail> mails, BmobException e)
				{
					if(e != null){
						MyToast.makeText(getActivity(), "获取邮件失败！！！", Toast.LENGTH_SHORT).show();
					}else
						ShowList(mails);
					
				}
			});
	}
	
	TitleAdapter adapter;
	
	void ShowList(final List<Mail> mails){
		this.mails = mails;
		
		if(mails == null && adapter != null){
			adapter.clear();
			
			return ;
		}
		
		if(mails.size() < 1) {
			if(adapter != null)
			adapter.clear();
			return ;
		}

        List<TitleAdapter.InfoMation> item = new ArrayList<>(mails.size());

        TitleAdapter.InfoMation info = null;
		
		for(Mail m : mails){
            info = new TitleAdapter.InfoMation();
            info.title = m.pid == Shoping.ADMINISTRATOR ? "系统管理员" : (m.publisher + " id："+m.pid);
            info.msg = m.msg;
            item.add(info);
		}

        adapter = new TitleAdapter(getContext());

        adapter.set(item);
		
		mListView.setAdapter(adapter);
	}
	
	Timer task;
	
	class ButtonDelay extends TimerTask
	{
		int time;

		Handler handle;
		
		public ButtonDelay(){
			handle = new Handler(Looper.getMainLooper());
		}
		
		public void setTime(int time)
		{
			this.time = time;
		}

		public int getTime()
		{
			return time;
		}
		
		@Override
		public void run()
		{
			if(time > 0) time--;
			
			handle.post(new Runnable(){
					@Override
					public void run()
					{
						if(time > 0)
						btn_refresh.setText("刷新("+time+")");
						else{
							btn_refresh.setText("刷新");
							btn_refresh.setEnabled(true);
							task.cancel();
							task = null;
						}
					}
				});
		}
	}
	
	@Override
	public void onClick(View v)
	{
		switch(v.getId()){
			case R.id.mailButton1_send:
			startFragment(new SendMailUI());
			break;
			
			case R.id.mailButton1_ref:
			task = new Timer();
			ButtonDelay bd = new ButtonDelay();
			bd.setTime(6);
			task.schedule(bd, 1, 1000);
			btn_refresh.setEnabled(false);
			MailShow();
			break;
		}
	}
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();

		MainActivity.showBottom();
		
		if(task != null)
			task.cancel();
		
		task = null;
	}
	
	
}
