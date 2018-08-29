package com.xiuxian.chen.include;
import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import com.xiuxian.chen.*;
import com.xiuxian.chen.data.*;

public class MyDialog extends Dialog implements OnClickListener
{
	private LinearLayout pack;
	
	private FrameLayout frame = null;
	
	private TextView title, message;
	
	private CharSequence title_text = null, message_text = null;
	
	private Button Select, Cancel;
	
	private RelativeLayout TitleTab;
	
	private ImageButton Close;
	
	private String SelectText=null, CancelText=null;
	
	private OnClickListener SelectClick, CancelClick;
	
	private View countView=null;
	
	private boolean isinit;
	
	private boolean iscancel;
	
	private boolean isremovetitle;
	
	private boolean isclick_close;
	
	private boolean select_is_close_dialog;
	
	private boolean cancel_is_close_dialog;
	
	private int width = -1;

	private int height = -1;

    private Bundle bundle;

    private Object tag;
	
	public MyDialog(Context cx){
		super(cx, R.style.diastyle);
		select_is_close_dialog = true;
		cancel_is_close_dialog = true;
		iscancel = true;
        this.bundle = new Bundle();
	}
	
	public MyDialog(Context cx, int width, int height){
		this(cx);
		this.width = width;
		this.height = height;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialogview);
		pack=(LinearLayout)findViewById(R.id.dialogviewLinearLayout1);
		frame=(FrameLayout)findViewById(R.id.dialogviewFrameLayout1);
		TitleTab = (RelativeLayout)findViewById(R.id.dialogviewRelativeLayout1);
		title=(TextView)findViewById(R.id.title);
		Select=(Button)findViewById(R.id.select);
		Cancel=(Button)findViewById(R.id.cancel);
		Close = (ImageButton)findViewById(R.id.close);
		message=(TextView)findViewById(R.id.message);
		Select.setOnClickListener(this);
		Cancel.setOnClickListener(this);
		Close.setOnClickListener(this);
		Close.setVisibility(iscancel ? View.VISIBLE : View.GONE);
		TitleTab.setVisibility(isremovetitle ? View.GONE : View.VISIBLE);
		if(SelectText!=null){
			Select.setVisibility(View.VISIBLE);
			Select.setText(SelectText);
			}
		if(CancelText!=null){
			Cancel.setVisibility(View.VISIBLE);
			Cancel.setText(CancelText);
			}
		if(title_text!=null)
		title.setText(title_text);
		//else title.setVisibility(View.GONE);
		if(message_text!=null){
			message.setVisibility(View.VISIBLE);
			message.setText(message_text);
			}
			
		ViewGroup.LayoutParams lp=pack.getLayoutParams();
		if(this.width == -1)
		lp.width = MyILoadResource.SCREEN_WIDTH-50;
		else lp.width = this.width;
		if(this.height != -1)
		lp.height = this.height;
		pack.setLayoutParams(lp);
		if(isinit)initView();
		if(countView!=null){
			frame.removeAllViews();
			frame.addView(countView);
			}
	}
	
	public MyDialog setView(View v){
		if(frame==null)
			countView=v;
		else
		{
			//frame.setPadding(0,0,0,0);
			frame.removeAllViews();
			frame.addView(v);
		}
		
		return this;
	}
	
	
	public MyDialog SetTitle(CharSequence str){
		if(title!=null){
			title.setVisibility(View.VISIBLE);
			title.setText(str);
		}
		else title_text=str;
		return this;
	}
	
	public MyDialog setMessage(String str){
		if(message!=null){
			message.setVisibility(View.VISIBLE);
			message.setText(str);
		}else message_text=str;
		
		return this;
	}
	
	public MyDialog setMessage(CharSequence str){
		if(message!=null){
			message.setVisibility(View.VISIBLE);
			message.setText(str);
		}else message_text=str.toString();

		return this;
	}
	
	public MyDialog setSelect(String text, OnClickListener o){
		if(Select==null)
		this.SelectText=text;
		else {
			Select.setVisibility(View.VISIBLE);
			Select.setText(text);
			}
		this.SelectClick=o;
		return this;
	}
	
	public MyDialog setSelect(String text, OnClickListener o, boolean isc){
		this.select_is_close_dialog = isc;
		return this.setSelect(text, o);
	}
	
	public MyDialog setCancel(String text, OnClickListener o){
		if(Cancel==null)
		this.CancelText=text;
		else {
			Cancel.setVisibility(View.VISIBLE);
			Cancel.setText(text);
			}
		this.CancelClick=o;
		return this;
	}
	
	public MyDialog setCancel(String text, OnClickListener o, boolean isc)
	{
		this.cancel_is_close_dialog = isc;
		return this.setCancel(text, o);
	}
	
	public MyDialog setinitView(boolean is){
		this.isinit=is;
		return this;
	}
	
	public MyDialog Center(){
		
		return this;
	}
	
	public MyDialog Show(){
		super.show();
		return this;
	}
	
	//去除标题栏
	public void RemoveTitle(){
		if(TitleTab != null)
		TitleTab.setVisibility(View.GONE);
		else isremovetitle = true;
	}
	
	//显示标题栏
	public void ShowTitle(){
		if(TitleTab != null)
		TitleTab.setVisibility(View.VISIBLE);
		else isremovetitle = false;
	}
	
	private void initView(){
		if(frame==null)return;
		frame.setPadding(10, 0, 10, 0);
	}
	
	public MyDialog setCancel(boolean flag){
		super.setCanceledOnTouchOutside(flag);
		super.setCancelable(iscancel = flag);
		if(Close!=null){
			Close.setVisibility(iscancel ? View.VISIBLE : View.GONE);
		}
		return this;
	}
	
	//是否点击内框关闭
	public MyDialog setTouchDownDissmis(boolean is){
		this.isclick_close = is;
		return this;
	}
	
	@Override
	public void onClick(View v)
	{
		//Log.i(LoadResources.TAG, "" + cancel_is_close_dialog);
		switch (v.getId())
		{
			case R.id.select:
				if (select_is_close_dialog) dismiss();
				if (SelectClick != null) SelectClick.onClick(this);
				break;
			case R.id.cancel:
				if (cancel_is_close_dialog) dismiss();
				if (CancelClick != null) CancelClick.onClick(this);
				break;
			case R.id.close:
				dismiss();
				break;
		}
		//dismiss();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if(event.getAction() == MotionEvent.ACTION_DOWN & isclick_close)dismiss();
		return super.onTouchEvent(event);
	}

	//
	public Bundle getBundle(){
        return this.bundle;
    }

    public void setTag(Object object){
        this.tag = object;
    }

    public Object getTag(){
        return this.tag;
    }

	public static interface OnClickListener{
		public void onClick(MyDialog dialog);
	}
	
	
}
