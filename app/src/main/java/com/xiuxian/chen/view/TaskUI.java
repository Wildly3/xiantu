package com.xiuxian.chen.view;
import android.os.*;
import android.widget.*;
import com.xiuxian.chen.*;
import com.xiuxian.chen.adapter.*;
import com.xiuxian.chen.data.*;
import com.xiuxian.chen.include.*;
import com.xiuxian.chen.map.*;
import com.xiuxian.chen.set.*;
import java.util.*;

public class TaskUI extends Fragment implements RadioGroup.OnCheckedChangeListener, OnClickListener
{
	RadioGroup group;
	
	RadioButton rbtn_run, rbtn_canrun;
	
	ListView mListView;
	
	ButtonAdapter adapter;
	
	int action;
	
	@Override
	protected void onCreate(Bundle bundle)
	{
		super.onCreate(bundle);
		
		setContentView(R.layout.role_task);
		
		group = (RadioGroup)findViewById(R.id.roletaskRadioGroup1);
		
		rbtn_run = (RadioButton)findViewById(R.id.roletaskRadioButton1);
		
		rbtn_canrun = (RadioButton)findViewById(R.id.roletaskRadioButton2);
		
		mListView = (ListView)findViewById(R.id.roletaskListView1);
		
		group.setOnCheckedChangeListener(this);

		mListView.setAdapter(adapter = new ButtonAdapter(getContext()));
		
		rbtn_run.setChecked(true);
	}
	
	@Override
	public void onCheckedChanged(RadioGroup group, int index)
	{
		switch (index)
		{
			case R.id.roletaskRadioButton1:
				RunTaskViewInit();
				break;
			case R.id.roletaskRadioButton2:
				FromTaskViewInit();
				break;
		}
	}
	
	//进行中任务视图初始化
	void RunTaskViewInit(){
		adapter.clear();
		adapter.setOnclickListener(null);
		adapter.setOnItemclickListener(null);
		List<String> list = new ArrayList<>();
		List<GameTask> gts = BaseActivity.gtmanager.getTasks();
		if(gts.size() < 1) return;
		gs.clear();
		for(GameTask gt : gts){
			list.add(gt.name);
			gs.add(gt);
			}
		
		adapter.setItem(list.toArray(new String[list.size()]), "完成任务");
		
		for(int i=0;i<gs.size();i++){
			if(!gs.get(i).isComplate()){
				adapter.setButtonText("未完成", i);
				adapter.setButtonClickable(i, false);
			}else adapter.setButtonClickable(i, true);
		}
		
		action = 2;
		
		adapter.setOnclickListener(this);
		
		adapter.setOnItemclickListener(new OnClickListener(){

				@Override
				public void OnClick(int index)
				{
					GameTask task = gs.get(index);
					showTaskInfoMation(task, true);
				}
			});
	}
	
	
	List<GameTask> gs = new ArrayList<>();
	//初始化接受任务视图
	void FromTaskViewInit(){
		adapter.clear();
		adapter.setOnclickListener(null);
		adapter.setOnItemclickListener(null);
		MyMap map = BaseActivity.mapmanager.getActionNowMap(BaseActivity.map_action);
		
		if (map == null){
			adapter.clear();
			return ;}
		
		if (map.getTasks().size() < 1){
			adapter.clear();
			return ;}
		
		gs.clear();
		//adapter.setButtonText("接受任务");
		List<String> list = new ArrayList<>();
		//Log.i(LoadResources.TAG, map.getTasks().size()+"");
		for (String task : map.getTasks())
		{
			GameTask gt = Tasks.ScanTask(task);
			if (gt != null && !BaseActivity.gtmanager.isEmptyAll(gt.name)){
				gs.add(gt);
				list.add(gt.from_name + "→" + gt.name);
				}
		}
		if (list.size() > 0){
			adapter.setItem(list.toArray(new String[list.size()]), "接受任务");
			adapter.setOnclickListener(this);
			action = 1;
			adapter.setOnItemclickListener(new OnClickListener(){
					@Override
					public void OnClick(int index)
					{
						GameTask task = gs.get(index);
						showTaskInfoMation(task, false);
					}
				});
		}
			
	}
	
	void showTaskInfoMation(GameTask task, boolean isv){
		StringBuilder sb = new StringBuilder("");
		sb.append("任务发布者："+task.from_name+"\n");
		sb.append(task.explain);
		for(GameTask.TaskInfoMation tm : task.getTaskInfoMation())
			sb.append(tm.name+":"+(isv ? tm.getNumber()+"/" : "")+tm.enough+"\n");

		sb.append("\n");
		sb.append("任务奖励：");
		if(task.exp > 0) sb.append("\n经验 "+task.exp);
		if(task.money > 0) sb.append("\n金币 "+task.money);
		if(task.getGoods().size() > 0)
			for(Item item : task.getGoods()){
				sb.append("\n"+item.name);
			}
		msgdialog(sb.toString());
	}
	
	
	@Override
	public void OnClick(int index)
	{
		switch(action){
			case 1:
			BaseActivity.gtmanager
			.addTask(Tasks.ScanTask(
			gs.get(index).name));
			toast("已接受任务！");
			FromTaskViewInit();
			break;
			
			case 2:
			CompletedTask(gs.get(index));
			RunTaskViewInit();
			break;
		}
	}
	
	void CompletedTask(GameTask gt){
		String result = BaseActivity.gtmanager.CompletedTask(gt.name);
		new MyDialog(getContext())
		.setMessage("任务完成")
		.setMessage(result)
		.setCancel("关闭", null)
		.Show();
		
		
	}

}
