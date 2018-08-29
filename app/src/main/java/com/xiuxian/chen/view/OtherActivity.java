package com.xiuxian.chen.view;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xiuxian.chen.MainActivity;
import com.xiuxian.chen.R;
import com.xiuxian.chen.adapter.MyAdapter;
import com.xiuxian.chen.data.MyILoadResource;
import com.xiuxian.chen.include.MyDialog;
import com.xiuxian.chen.include.MyToast;
import com.xiuxian.chen.other.Update;
import com.xiuxian.chen.other.perfromLongClickTextView;

import java.io.File;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.QueryListener;

public class OtherActivity extends BaseActivity implements OnItemClickListener, View.OnClickListener
{
    private static final String TAG = "OtherActivity";

	static final int[] btnid = {
            R.id.other_save,
            R.id.other_console,
            R.id.other_deteatver,
            R.id.other_help,
            R.id.other_about,
            R.id.other_mail,
            R.id.other_goods,
            R.id.other_maptest
    };

	ListView list;

//    android.support.v7.widget.RecyclerView listview;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Goin(R.layout.otherview, null);
        for (int i=0;i<btnid.length;i++){
            ((Button)findViewById(btnid[i])).setOnClickListener(this);
        }
	}

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.other_save:
                new MyDialog(this)
                        .SetTitle("提示")
                        .setMessage("是否保存游戏进度?")
                        .setCancel("否", null)
                        .setSelect("是", new MyDialog.OnClickListener(){
                            @Override
                            public void onClick(MyDialog dialog)
                            {
                                MainActivity.MainPressenter.SaveArchive(new Runnable() {
                                    @Override
                                    public void run() {
                                        toast("保存成功");
                                    }
                                });
                            }
                        })
                        .show();
                break;
            case R.id.other_console:
                startFragment(new ConsoleActivity());
                break;
            case R.id.other_deteatver:
                update();
                break;
            case R.id.other_about:
                about();
                break;
            case R.id.other_help:
                startFragment(new HelpUI());
                break;
            case R.id.other_mail:
                startFragment(new MailUI());
                break;
            case R.id.other_goods:
                startFragment(new ShoppingUI());
                break;
            case R.id.other_maptest:
                startFragment(new MapUI());
                break;
        }
    }

	@Override
	public void onItemClick(AdapterView<?> p1, View p2, int index, long p4)
	{
		//LibTo.Verify(BaseActivity.sss);
		switch(index){
			case 0:
				startFragment(new ShoppingUI());
			break;
			case 1:
				startFragment(new MailUI());
			break;
            case 2:
                about();
                break;
            case 3:
                update();
                break;
			case 4:
			//startActivity(new Intent(this, ConsoleActivity.class));
			startFragment(new ConsoleActivity());
			break;
			case 5:
			new MyDialog(this)
			.SetTitle("提示")
			.setMessage("是否保存游戏进度?")
			.setCancel("否", null)
					.setSelect("是", new MyDialog.OnClickListener(){
						@Override
						public void onClick(MyDialog dialog)
						{
							MainActivity.MainPressenter.SaveArchive(new Runnable() {
                                @Override
                                public void run() {
                                    toast("保存成功");
                                }
                            });
						}
					})
			.show();
			break;
		}
	}

    public static final String UPDATEID = "JLVlBBBF";

    public static final String UPDATE_DATA_FILENAME = "resources.zip";

    public static final String UPDATE_DATE_APK_FILENAM = "xiantu.apk";

    //更新作用
    void update(){
        BmobQuery<Update> query = new BmobQuery<>();
        query.getObject(UPDATEID, new QueryListener<Update>() {
            @Override
            public void done(Update update, BmobException e) {
                if (e == null){
                    int type = update.type;

                    float upv = Float.parseFloat(update.version);

                    float localv = MyILoadResource.getJsVersion();

                    switch (type){
                        case 1:
                            File apk = new File(MyILoadResource.UPDATE_PATH + "/" + UPDATE_DATE_APK_FILENAM);

                            if (upv > localv){
                                apk.delete();

                                dataUpdate(update);
                            } else
                                if (apk.isFile())
                                    install(apk.getAbsolutePath());
                            else updatenull();
                            break;
                        case 2:
                            if (upv > localv){
                                new File(MyILoadResource.UPDATE_PATH + "/" + UPDATE_DATA_FILENAME).delete();
                                dataUpdate(update);
                            }else updatenull();
                            break;
                    }
                } else MyToast.makeText(OtherActivity.this, "更新检查失败！", Toast.LENGTH_SHORT).show();
            }
        });

    }

    //无更新提示
    void updatenull(){
        MyToast.makeText(OtherActivity.this, "暂无更新！", Toast.LENGTH_SHORT).show();
    }

    //更新
    void dataUpdate(final Update update){
        new MyDialog(this)
                .SetTitle("更新")
                .setMessage(update.msg)
                .setSelect("更新", new MyDialog.OnClickListener() {
                    @Override
                    public void onClick(MyDialog dialog) {
                        DownDialog(update, update.url,
                                MyILoadResource.UPDATE_PATH  + "/"
                                        + (update.type == 1 ? UPDATE_DATE_APK_FILENAM : UPDATE_DATA_FILENAME));
                    }
                })
                .setCancel("取消", null)
                .show();
    }

    //下载对话框
    void DownDialog(final Update update, String url, final String filename){
        LinearLayout linearLayout = new LinearLayout(this);

        linearLayout.setOrientation(LinearLayout.HORIZONTAL);

        final TextView textView = new TextView(this);

        textView.setTextColor(getResources().getColor(R.color.color_alltext));

        linearLayout.addView(textView);

        final MyDialog myDialog = new MyDialog(this)
                .SetTitle("正在下载更新，请勿关闭！")
                .setView(linearLayout)
                .setCancel(false)
                .Show();

        BmobFile bmobFile = new BmobFile("", "", url);

        bmobFile.download(new File(filename), new DownloadFileListener() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void done(String s, BmobException e) {

                myDialog.setCancel(true);
                if (e != null)
                textView.append("\n更新失败；" + e.getMessage());
                else
                {
                    if (update.type == 1) {
                        install(filename);
                        return;
                    }

                    final String version = update.version;

                    MyILoadResource.DeleteJs();

                    boolean isunzip =  MyILoadResource.upZipFileDir(new File(filename), MyILoadResource.CACHE_PATH + "/");

                    if (!isunzip){
                        textView.append("\n数据文件解压失败！");
                        return;
                    }

                    MyILoadResource.VersionWrite(version);

                    textView.append("\n更新完成，已为您自动保存游戏，请关闭游戏重启！");

                    new File(filename).delete();

                    MainActivity.MainPressenter.SaveArchive();
                }
            }

            @Override
            public void onProgress(Integer value, long l) {
                textView.setText("正在下载；" + String.valueOf(value) + "%");
            }
        });

    }

    private void install(String filePath) {
//        Log.i(TAG, "开始执行安装: " + filePath);
        File apkFile = new File(filePath);

        Intent intent = new Intent(Intent.ACTION_VIEW);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            Log.w(TAG, "版本大于 N ，开始使用 fileProvider 进行安装");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            Uri contentUri = FileProvider.getUriForFile(
                    this
                    , "com.xiuxian.chen.fileprovider"
                    , apkFile);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
//            Log.w(TAG, "正常进行安装");
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        }

        startActivity(intent);
    }

	//关于
    private void about()
    {
        View v = getLayoutInflater().inflate(R.layout.about, null);
        SpannableString qqtext = new SpannableString("319965592");
        qqtext.setSpan(new ClickableSpan() {
            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(Color.BLUE);
                ds.setUnderlineText(true);
            }

            @Override
            public void onClick(View widget) {
                ClipboardManager cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                cm.setPrimaryClip(ClipData.newPlainText(null, "319965592"));
                toast("已复制到粘贴版");
				final String qqUrl = "mqqwpa://im/chat?chat_type=wpa&uin=319965592&version=1";
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(qqUrl)));
            }
        }, 0, qqtext.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        perfromLongClickTextView text = (perfromLongClickTextView) v.findViewById(R.id.about_text1);
        text.setText("");
        text.append("Wildly 制作 QQ ");
        text.append(qqtext);
        text.setMovementMethod(LinkMovementMethod.getInstance());
        new MyDialog(this)
                .SetTitle("关于")
                .setinitView(true)
                .setView(v)
                .Show();
    }
}
