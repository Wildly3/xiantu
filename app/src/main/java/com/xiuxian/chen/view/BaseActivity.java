package com.xiuxian.chen.view;
import android.animation.*;
import android.content.*;
import android.content.pm.*;
import android.view.*;
import android.widget.*;
import com.xiuxian.chen.*;
import com.xiuxian.chen.data.*;
import com.xiuxian.chen.include.*;
import com.xiuxian.chen.map.*;
import com.xiuxian.chen.other.*;
import com.xiuxian.chen.set.*;
import java.math.*;
import java.security.*;
import java.util.*;
import android.util.*;
import android.os.*;

public class BaseActivity extends FragmentActivity
{
	public static Player base;

	public static com.xiuxian.chen.set.Space space;

	public static Action map_action;

	public static MapManager mapmanager;

	public static GameTaskManager gtmanager;
	
	public static List<Weapon> WEAPONS;
	public static List<Consunables> CONSUNABLES;
	public static List<Material> MATERAIALS;
	
	//public static List<MyMap> MAPS;
	public static List<Enemy> ENEMYS;
	public static List<Skill> SKILLS;
	
	public static ItemCache cache;
	
	public static String sss;
	
	private Bundle bundle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getsing();
		//LibTo.Verify(sss);
		Shoping.getInstance();
		
		bundle = new Bundle();
	}

    @Override
    public void OnEndLayout() {
		//getsing();
        new MyDialog(this)
                .SetTitle("提示")
                .setMessage("是否退出游戏？")
                .setCancel("否", null)
			.setSelect("是", new MyDialog.OnClickListener(){
                    @Override
                    public void onClick(MyDialog dialog)
                    {
                        Exit();
						MainActivity.hideBottom();
						
                        getWindow().getDecorView().animate()
						.scaleX(0)
						.scaleY(0)
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
									System.exit(0);
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
							//MainActivity.exit();
						
                    }
                }).show();
    }

	
	private void getsing(){
		try
		{
			PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
			android.content.pm.Signature[] sig = info.signatures;
			String str = sig[0].toCharsString();
			str = getMD5(str.getBytes());
			sss = str;
		}
		catch (Exception e)
		{}
	}
	
	private String getMD5(byte[] buffer) throws NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance("MD5");
		digest.update(buffer);
		BigInteger bigInt = new BigInteger(1, digest.digest());
		return bigInt.toString(16);
	}
	
    public void toast(String str){
		MyToast.makeText(this, str, Toast.LENGTH_SHORT, Gravity.CENTER).show();
	}

	 public void msgdialog(String t){
		new MyDialog(this)
			.setMessage(t)
			//.setCancel("关闭", null)
			.show();
	}
	
	public void msgdialog(Context cx, String t){
		new MyDialog(cx)
			.setMessage(t)
			//.setCancel("关闭", null)
			.show();
	}
	
	public static Weapon ScanWeapon(String name){
		return cache.ScanEquip(name);
	}

	public static Consunables ScanConsunables(String name){
		
		return cache.ScanConsunables(name);
	}

	public static Material ScanMaterial(String name){
		return cache.ScanMaterial(name);
	}
	
	public static Enemy ScanEnemy(String name){
		return cache.ScanEnemy(name);
	}
	
	public static Skill ScanSkill(String name){
		return cache.ScanSkill(name);
	}
	
	public void Exit(){}

	@Override
	protected void onStart()
	{
		super.onStart();
		//getsing();
	}
	
}
