package com.xiuxian.chen;

import android.Manifest;
import android.animation.*;
import android.app.*;
import android.content.*;
import android.content.pm.PackageManager;
import android.os.*;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.*;
import android.widget.*;
import com.xiuxian.chen.data.*;
import com.xiuxian.chen.include.*;
import com.xiuxian.chen.map.*;
import com.xiuxian.chen.other.*;
import com.xiuxian.chen.presenter.*;
import com.xiuxian.chen.set.*;
import com.xiuxian.chen.view.*;
import cn.bmob.v3.*;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

public class MainActivity extends TabActivity implements MainView
{
	public static TabHost tabHost;
	
	//事件
	public static Event event;
	
	public static float DP;
	
	public static LinearLayout Bottom;
	
	public static IMainActivityPresenter MainPressenter;
	
	static MainActivity main;
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
                );
		
		DP = getResources().getDisplayMetrics().density;
		
		main = this;
		
		MainPressenter = new IMainActivityPresenter(this);
		
		MainPressenter.addViewListener(this);
		
		LoadingView();
		
		MainPressenter.InitResource();
		
		ShopInit();

        checkPremission();

        MainUiInit();
	}

	void checkPremission(){
        int write = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        int read = ActivityCompat.checkSelfPermission(this, Manifest.permission.WAKE_LOCK);

        if (write != PackageManager.PERMISSION_GRANTED || read != PackageManager.PERMISSION_GRANTED){
            if (Build.VERSION.SDK_INT < 23){
                MyToast.makeText(this, "未获取游戏相关权限，无法运行！", Toast.LENGTH_LONG).show();
                premissionDia();
            } else{
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.WAKE_LOCK
                }, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1){
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED
                    || grantResults[1] != PackageManager.PERMISSION_GRANTED){
                MyToast.makeText(this, "拒绝授权，无法运行！", Toast.LENGTH_LONG).show();
                premissionDia();
            }
        }

    }

    void premissionDia(){
        new MyDialog(this)
                .SetTitle("提示")
                .setMessage("未获取游戏相关权限，无法运行！")
                .setCancel("退出游戏", new MyDialog.OnClickListener() {
                    @Override
                    public void onClick(MyDialog dialog) {
                        System.exit(0);
                    }
                })
                .setCancel(false)
                .Show();
    }

	//bmob初始化
	void ShopInit()
	{
		Bmob.initialize(this, LibTo.getAppId());
	}
	
	MyDialog loaddialog;
	
	void LoadingView(){
		loaddialog = new MyDialog(this)
		.SetTitle("提示信息")
		.setMessage("加载中...")
		.setCancel(false)
		.Show();
	}
	
	@Override
	public void Complete(int flag)
	{
        switch (flag){
            default:break;
            case IMainActivityPresenter.FLAG_LOADRESORECE:
                MainPressenter.LoadArchive();
                loaddialog.dismiss();
                break;
            case IMainActivityPresenter.FLAG_LOADARCHIVE:
                RoleActivity.activity.startFragment(new RoleUI());
                RoleActivity.isloadarchive = true;
                break;
        }
	}

		//初始化页面
	void MainUiInit()
	{
		setContentView(R.layout.main);
		
		Bottom = (LinearLayout)findViewById(R.id.mainLinearLayout1);
		
		Bottom.setVisibility(View.GONE);
		
		tabHost = getTabHost();
		
		TabHost.TabSpec tab;
		
		Intent it;

		it = new Intent(this, RoleActivity.class);
		tab = buildTagSpec("", "人物", it);
		tabHost.addTab(tab);

		it = new Intent(this, MapActivity.class);
		tab = buildTagSpec("", "地图", it);
		tabHost.addTab(tab);

		it = new Intent(this, OtherActivity.class);
		tab = buildTagSpec("", "系统", it);
		tabHost.addTab(tab);
		tabHost.setCurrentTab(0);
	}
	
	//显示底部
	public static void showBottom()
	{
		final TabWidget tab = tabHost.getTabWidget();
		
		tab.setTranslationY(Bottom.getHeight()+10);
		tab.setVisibility(View.VISIBLE);
		Bottom.setVisibility(View.GONE);
		Bottom.animate()
			.translationY(tab.getHeight()+10)
			.setDuration(300)
			.setListener(new android.animation.Animator.AnimatorListener(){
				@Override
				public void onAnimationStart(Animator p1)
				{
					// TODO: Implement this method
				}

				@Override
				public void onAnimationEnd(Animator p1)
				{
					tab.animate()
						.translationY(0)
						.setDuration(200);
				}

				@Override
				public void onAnimationCancel(Animator p1)
				{
					// TODO: Implement this method
				}

				@Override
				public void onAnimationRepeat(Animator p1)
				{
					// TODO: Implement this method
				}
			});
	}
	
	@Override
	public void ShowDialog(final MyDialog dialog)
	{
		runOnUiThread(new Runnable(){

				@Override
				public void run()
				{
					dialog.Show();
				}
			});
	}

	@Override
	public void ShowMessage(final String msg)
	{
		runOnUiThread(new Runnable(){

				@Override
				public void run()
				{
					MyToast.makeText(MainActivity.this, msg, 1000).show();
				}
			});
	}
	
	//隐藏底部
	public static void hideBottom()
	{
		TabWidget tab = tabHost.getTabWidget();
		
		Bottom.setTranslationY(Bottom.getHeight()+10);
		tab.setVisibility(View.INVISIBLE);
		Bottom.setVisibility(View.VISIBLE);
		tab.animate()
		.translationY(tab.getHeight()+10)
		.setDuration(300)
			.setListener(new android.animation.Animator.AnimatorListener(){
				@Override
				public void onAnimationStart(Animator p1)
				{
					// TODO: Implement this method
				}

				@Override
				public void onAnimationEnd(Animator p1)
				{
					Bottom.animate()
					.translationY(0)
					.setDuration(200);
				}

				@Override
				public void onAnimationCancel(Animator p1)
				{
					// TODO: Implement this method
				}

				@Override
				public void onAnimationRepeat(Animator p1)
				{
					// TODO: Implement this method
				}
			});
		
	}
	
	private TabHost.TabSpec buildTagSpec(String tagName, String tagLable, Intent content) {
		return tabHost
			.newTabSpec(tagName)
			.setIndicator(getView(tagLable))
			.setContent(content);
	}
	
	View getView(String str){
		View v=LayoutInflater.from(this).inflate(R.layout.tabwidget_btn, null);
		TextView tv=(TextView)v.findViewById(R.id.tabwidgetbtnTextView1);
		tv.setText(str);
		return v;
	}
}
