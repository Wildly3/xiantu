package com.xiuxian.chen.view;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import com.xiuxian.chen.*;
import com.xiuxian.chen.include.*;
import com.xiuxian.chen.other.*;
import com.xiuxian.chen.set.*;
import android.view.MenuItem.*;
import android.view.inputmethod.*;
import android.util.*;
import android.content.*;

public class ConsoleActivity extends Fragment
{
	//获取装备
	static final String GETEQUIP="getequip-";

	//获取消耗品，格式： name-number
	static final String GETCONSUNABLES="getcons-";

	//获取技能
	static final String GETSKILL="getskill-";

	//获取金币
	static final String GETMONEY = "getmoney-";

	//获取经验
	static final String GETEXP = "getexp-";

	CustomizeCmd cmd;
	
	EditText consoleEdit;

	TextView consoleText;
	
	SharedPreferences sp;
	
	Context cx;
	
	@Override
	protected void onCreate(Bundle bundle)
	{
		super.onCreate(bundle);
		
		cx = getContext();
		
		//sp = cx.getSharedPreferences("set", Context.MODE_PRIVATE);
		
		MainActivity.hideBottom();
		
		setContentView(R.layout.console);
		
		cmd = CustomizeCmd.getInstance();
		
		cmd.Context(cx);
		
		consoleEdit=(EditText)findViewById(R.id.consoleEditText1);

		consoleText = (TextView)findViewById(R.id.consoleTextView1);

		consoleText.setText(cmd.getInfoMation());

		//consoleEdit.setCursorVisible(false);
		
		consoleEdit.setOnEditorActionListener(new MyEditor());
	}

	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		cmd.setInfo(consoleText.getText().toString());
		MainActivity.showBottom();
	}
	
	
	private class MyEditor implements android.widget.TextView.OnEditorActionListener
	{
		@Override
		public boolean onEditorAction(TextView tv, int actionId, KeyEvent event)
		{
			//Log.i(LoadResources.TAG, "调用 action="+actionId + "event="+(event == null ? "null" : "gd"));
			
			if (!(actionId == EditorInfo.IME_ACTION_SEARCH
                || event.getKeyCode() == KeyEvent.ACTION_DOWN))
			{
				return true;
			}
			
			String pw = consoleEdit.getText()
			.toString().replaceAll("\n", "");

			if(pw.equals("")){
				if(cmd.getLastTimeInput() != null){
					pw = cmd.getLastTimeInput();
				}else return true;
			}
			else
				cmd.setLastTimeInput(pw);

			consoleEdit.setText("");

			boolean iscmd = cmd.enter(pw);

			if (iscmd)
			{
				String msg = cmd.getMessage();
				if(msg == null) return false;
				consoleText.append(msg);
				consoleText.append("\n");
				return true;
			}

			String result=null;

			String name = null;

			ExChangeGoods goods = null;

			boolean sv_cheats = cmd.Environment().getBoolean("sv_cheats", false);

			if(sv_cheats)
				if(pw.indexOf(GETEQUIP) != -1){
					name = pw.substring(pw.indexOf(GETEQUIP)+GETEQUIP.length());
					goods = new ExChangeGoods(name,Item.KIND_EQUIP,Item.QUALITY_NONSUCH);
					result = goods.ExChange();
				}
				else
				if(pw.indexOf(GETCONSUNABLES) != -1){
					name = pw.substring(pw.indexOf(GETCONSUNABLES)+GETCONSUNABLES.length());
					int index = name.indexOf("-");
					int num = 1;
					String name2 = "";
					if(index != -1){
						String[] arr=name.split("-");
						name2=arr[0];
						num = Integer.parseInt(arr[1]);
					}else {
						name2 = name;
					}

					goods = new ExChangeGoods(name2, Item.KIND_CONSUNABLES, 0, num);
					result=goods.ExChange();
				}
				else
				if(pw.indexOf(GETSKILL) != -1){
					name = pw.substring(pw.indexOf(GETSKILL)+GETSKILL.length());
					goods = new ExChangeGoods(name,ExChangeGoods.SKILL);
					result = goods.ExChange();
				}
				else
				if(pw.indexOf(GETMONEY) != -1)
				{
					name = pw.substring(pw.indexOf(GETMONEY)+GETMONEY.length());
					goods = new ExChangeGoods(ExChangeGoods.MONEY);
					goods.money = Integer.parseInt(name);
					result = goods.ExChange();
				}
				else
				if(pw.indexOf(GETEXP) != -1)
				{
					name = pw.substring(pw.indexOf(GETEXP)+GETEXP.length());
					goods = new ExChangeGoods(ExChangeGoods.EXP);
					goods.exp = Integer.parseInt(name);
					result = goods.ExChange();
				}
				else
					for(ExChangeGoodsAll ecg : GOODS.ECGS)
						if(ecg.password.equals(pw)){
							result=ecg.Exchange();
							break;
						}

			if(result == null)
				consoleText.append("无效输入！\n");
			else consoleText.append(result+"\n");
			
			return true;
		}
	}

	
}
