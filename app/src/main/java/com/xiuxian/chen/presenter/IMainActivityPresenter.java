package com.xiuxian.chen.presenter;
import android.app.*;
import android.content.*;
import android.os.*;
import com.xiuxian.chen.*;
import com.xiuxian.chen.data.*;
import com.xiuxian.chen.include.*;
import com.xiuxian.chen.map.*;
import com.xiuxian.chen.set.*;
import com.xiuxian.chen.view.*;

public class IMainActivityPresenter
{
	Context cx;
	
	MainView main;
	
	ILoadResource loadresource;
	
	IArchive archive;
	
	NewPlayer newPlayer;

    //加载资源标识
    public static final int FLAG_LOADRESORECE = 1;

    //加载存档标识
    public static final int FLAG_LOADARCHIVE = 2;

    //保存存档标识
    public static final int FLAG_SAVEARCHIVE = 3;
	
	public IMainActivityPresenter(Activity cx){
		this.cx = cx;
		
		this.archive = new MyIArchive(cx);
		
		this.loadresource = new MyILoadResource(cx);
		
		MainActivity.event = Event.New(cx);
	}
	
	public void addViewListener(MainView main){
		this.main = main;
		this.newPlayer = new NewPlayer(cx, main);
	}

	//初始化资源
	public void InitResource()
	{
		final Handler handle = new Handler(Looper.getMainLooper());

		new Thread(){
			@Override
			public void run()
			{
				loadresource.init();

				handle.post(new Runnable(){
						@Override
						public void run()
						{
							main.Complete(FLAG_LOADRESORECE);
						}
					});
			}
		}.start();
	}

	//读取存档
	public void LoadArchive()
	{
		final Handler handle = new Handler(Looper.getMainLooper());

		new Thread(){
			@Override
			public void run()
			{
				final boolean isread = archive.LoadArchive();
				
				handle.post(new Runnable(){
						@Override
						public void run()
						{
							if (!isread)
							{
                                RoleActivity.activity.startFragment(new CreateRolesUI());
							} else {
                                main.Complete(FLAG_LOADARCHIVE);
                            }
						}
					});
			}
		}.start();
	}

	//保存存档
	public void SaveArchive(){
		SaveArchive(null);
	}

    public void SaveArchive(final Runnable completedListener) {
        final Handler handle = new Handler(Looper.getMainLooper());

        new Thread() {
            @Override
            public void run() {
                archive.SaveArchive();

                if (completedListener != null)
                    handle.post(completedListener);

                handle.post(new Runnable() {
                    @Override
                    public void run() {
                        main.Complete(FLAG_SAVEARCHIVE);
                    }
                });
            }
        }.start();
    }

}
