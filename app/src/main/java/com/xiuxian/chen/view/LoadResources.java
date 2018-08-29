package com.xiuxian.chen.view;

import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import com.xiuxian.chen.include.*;
import com.xiuxian.chen.map.*;
import com.xiuxian.chen.set.*;
import java.io.*;
import java.util.*;
import java.util.zip.*;
import com.xiuxian.chen.*;
import com.xiuxian.chen.data.*;

public class LoadResources
{
    public static final String TAG = "com.xiuxian.chen";
	
	public static String JAVASCRIPT_BASE;
	
	public static String JAVASCRIPT_FUNCTION;
	
	public static int SCREEN_WIDTH;
	
	public static int SCREEN_HEIGHT;
	
	private Activity app;
	
	File paths[];
	
	Runnable run;
	
	File FirstUnZipFile;
	
	public static String MAP_PATH;
	
	private static String CACHE_PATH;
	
	public static float DP;
	
	public LoadResources(Activity app, Runnable run){
		this.app=app;
		this.run=run;
		String Path= CACHE_PATH = app.getExternalCacheDir().getAbsolutePath();
		MAP_PATH = Path+"/map/";
		FirstUnZipFile=new File(app.getExternalCacheDir().getAbsolutePath()+"/.NOTFIRST");
		paths=new File[]{
			new File(Path+"/equip/"),
			new File(Path+"/consunables/"),
			new File(Path+"/material/"),
			new File(Path+"/map/"),
			new File(Path+"/item/"),
			new File(Path+"/enemy/"),
			new File(Path+"/skill/")
		};
		BaseActivity.cache = ItemCache.getInstance(Path);

		DP = app.getResources().getDisplayMetrics().density;

		try
		{
			JAVASCRIPT_BASE = ReadAssetsString("javascript/base.js");
			
			JAVASCRIPT_FUNCTION = ReadAssetsString("javascript/function.js");
		}
		catch (IOException e)
		{}
	}
	
	String ReadAssetsString(String filename) throws IOException{
		String result = null;
		InputStream in = app.getAssets().open(filename);

		byte[] buf = new byte[in.available()];

		in.read(buf);

		result = new String(buf);

		in.close();
		
		return result;
	}
	
	MyDialog md;
	ProgressBar prog;
	public void start(){
		View v=View.inflate(app, R.layout.prog, null);

		prog=(ProgressBar)v.findViewById(R.id.progProgressBar1);

		md=new MyDialog(app)
			.setCancel(false)
			.setView(v)
			.Show();

		new Thread(new Runnable(){
				@Override
				public void run()
				{
					for(File f : paths)
						f.mkdir();
					init();
				}
			}).start();
	}
	
	/**
	 * 获取本地软件版本号名称
	 */
	public static String getLocalVersionName(Context ctx) {
		String localVersion = "";
		try {
			PackageInfo packageInfo = ctx.getApplicationContext()
				.getPackageManager()
				.getPackageInfo(ctx.getPackageName(), 0);
			localVersion = packageInfo.versionName;
			//LogUtil.d("TAG", "本软件的版本号。。" + localVersion);
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return localVersion;
	}
	
	//解压文件
	private static boolean upZipFileDir(File zipFile, String folderPath) {
        ZipFile zfile= null;
        try {
            //转码为GBK格式，支持中文
            zfile = new ZipFile(zipFile);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        Enumeration zList=zfile.entries();
        ZipEntry ze=null;
        byte[] buf=new byte[1024];
        while(zList.hasMoreElements()){
            ze=(ZipEntry)zList.nextElement();
            //列举的压缩文件里面的各个文件，判断是否为目录
            if(ze.isDirectory()){
                String dirstr = folderPath + ze.getName();
                dirstr.trim();
                File f=new File(dirstr);
                f.mkdir();
                continue;
            }
            OutputStream os= null;
            FileOutputStream fos = null;
            // ze.getName()会返回 script/start.script这样的，是为了返回实体的File
            File realFile = getRealFileName(folderPath, ze.getName());
            try {
                fos = new FileOutputStream(realFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return false;
            }
            os = new BufferedOutputStream(fos);
            InputStream is= null;
            try {
                is = new BufferedInputStream(zfile.getInputStream(ze));
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            int readLen=0;
            //进行一些内容复制操作
            try {
                while ((readLen=is.read(buf, 0, 1024))!=-1) {
                    os.write(buf, 0, readLen);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            try {
                is.close();
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        try {
            zfile.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 给定根目录，返回一个相对路径所对应的实际文件名.
     * @param baseDir 指定根目录
     * @param absFileName 相对路径名，来自于ZipEntry中的name
     * @return java.io.File 实际的文件
     */
    private static File getRealFileName(String baseDir, String absFileName){
        String[] dirs=absFileName.split("/");
        File ret = new File(baseDir);
        String substr = null;

        if(dirs.length>1){
            for (int i = 0; i < dirs.length-1;i++) {
                substr = dirs[i];
                ret=new File(ret, substr);
            }

            if(!ret.exists())
                ret.mkdirs();
            substr = dirs[dirs.length-1];
            ret=new File(ret, substr);
            return ret;
        }else{
            ret = new File(ret,absFileName);
        }
        return ret;
    }

	//把assets资源写入sd卡
	private void WriteAssetsSD(String assets, String filename){
		InputStream in=null;
		FileOutputStream fos=null;
		byte[] buf=new byte[1024];
		try
		{
			in = app.getAssets().open(assets);
			fos=new FileOutputStream(filename);
			int len=0;
			while((len=in.read(buf))!=-1){
				fos.write(buf, 0, len);
			}

		}
		catch (IOException e)
		{}finally{
			try
			{
				if(in!=null)
					in.close();
				if(fos!=null)
					fos.close();
			}
			catch (IOException e)
			{}
		}
	}

	private void init(){
		if(!VerificationVersion()){
			DeleteJs();

			File res=new File(app.getExternalCacheDir().getAbsolutePath()+"/resources.zip");

			WriteAssetsSD("resources.zip", res.getAbsolutePath());

			upZipFileDir(res, app.getExternalCacheDir().getAbsolutePath()+"/");

			res.delete();

			try
			{
				FirstUnZipFile.createNewFile();

				VersionWrite();
			}
			catch (IOException e)
			{
                exception(e);
            }
		}
		
		app.runOnUiThread(new Runnable(){
				@Override
				public void run()
				{
					SCREEN_WIDTH = app.getWindowManager().getDefaultDisplay().getWidth();

					SCREEN_HEIGHT = app.getWindowManager().getDefaultDisplay().getHeight();

					try
					{
						run.run();
					}
					catch (Exception e)
					{exception(e);}
					md.dismiss();
				}

			});
		
	}
	
	//验证脚本版本是否一致
	boolean VerificationVersion(){
		//FirstUnZipFile
		FileInputStream fin = null;
		String localVersion = getLocalVersionName(app);
		String version = "";
		if(!FirstUnZipFile.isFile())return false;
		try
		{
			fin = new FileInputStream(FirstUnZipFile);
			byte[] buf = new byte[fin.available()];
			fin.read(buf);
			version = new String(buf);
			fin.close();
		}
		catch (IOException e)
		{
			return false;
		}
		if(!version.equals(localVersion)) return false;
		
		return true;
	}
	
	//写入当前版本到脚本配置
	void VersionWrite(){
		FileOutputStream fos = null;
		String version = getLocalVersionName(app);
		try
		{
			fos = new FileOutputStream(FirstUnZipFile);
			fos.write(version.getBytes());
			fos.close();
		}
		catch (IOException e)
		{}
	}
	
	void DeleteJs(){
		File f = new File(CACHE_PATH+"/");
		for(File ff : FileList(f))
		DeleteFile(ff);
	}
	
	//删除一个文件，或者删除一个文件夹(包括里面所有内容)
	void DeleteFile(File f){
		if(f.isFile())
			f.delete();
			else
		if(f.isDirectory()){
			File[] fs = FileList(f);
			if(fs != null && fs.length > 0){
				for(File ff : fs)DeleteFile(ff);
				f.delete();
			}else f.delete();
		}
	}
	
	File[] FileList(File f){
		return f.listFiles();
	}
	
	void exception(final Exception e){
		app.runOnUiThread(new Runnable(){
				@Override
				public void run()
				{
					EditText edit=new EditText(app);
					edit.setText(e.getMessage());
					edit.setBackgroundResource(R.drawable.shape_corner);
					new MyDialog(app)
						.SetTitle("游戏异常")
						.setView(edit)
						.show();
				}
			});
	}
	
	void exception(final String e){
		Exception ex = new Exception(e);
		this.exception(ex);
	}

	File[] listFile(String path){
		return new File(path)
			.listFiles(new FileFilter(){
				@Override
				public boolean accept(File f)
				{
					if(f.isFile()&f.getName().endsWith(".js"))
						return true;
					return false;
				}
			});
	}

	/*
	void msgdialog(String t){
		EditText edit=new EditText(app);
		edit.setBackgroundResource(R.drawable.shape_corner);
		edit.setText(t);
		new MyDialog(app)
			.setTitle("程序文件错误")
			.setCancel(false)
			.setCancel("退出", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					app.finish();
				}
			})
			.setView(edit)
			.show();
	}
*/
}
