package com.xiuxian.chen.view;
import android.animation.*;
import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.util.*;

//一个布局的简单栈

public class LayoutStack extends Activity
{
    //默认最小动画时长
    private static final int ANIMACTION_MIN_SPEED = 500;

	//栈最大数
	public static final int LAYOUT_STACK_MAX = 6;
	
	private List<Surface> stack;
	
	private View BaseView = null;
	
	//是否处于动画中
	private boolean isanimation;

    private int animaction_speed = ANIMACTION_MIN_SPEED;
	
	//顶层View
	private RelativeLayout DecorView;
	
	public LayoutStack(){
		this.stack=new ArrayList<Surface>(LAYOUT_STACK_MAX);
	}
	
	@Override
    protected void onCreate(Bundle savedInstanceState)
    {
		super.onCreate(savedInstanceState);

		DecorView = new RelativeLayout(this);

		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(-1, -1);

		DecorView.setLayoutParams(lp);

		super.setContentView(this.DecorView);
	}
	
	public RelativeLayout getDecorView(){
		((ViewGroup) this.DecorView.getParent()).removeAllViews();
		return this.DecorView;
	}
	
	//获取当前view
	public View getCurrentView(){
		return BaseView;
	}
	
    /**
     * 设置动画动画时长
     * @param t 时长
     */
    public void setAnimactionSpeed(int t)
    {
        this.animaction_speed = t < ANIMACTION_MIN_SPEED ? ANIMACTION_MIN_SPEED : t;
    }


    /**
     * 判断是否处于页面跳转动画中
     * @return 动画状态
     */
    public boolean isAnimation()
    {
        return this.isanimation;
    }

    public void DirectlyView(int id){
        View temp = LayoutInflater.from(this).inflate(id, DecorView, false);
        if(temp != null)
            DirectlyView(temp);
    }

    /**
     * 显示一个视图，但是会清除所有视图
     * @param contentview 视图
     */
    public void DirectlyView(View contentview)
    {
        if(isanimation) return;

        if(stack.size() > 0){
            Iterator<Surface> iterator = stack.iterator();

            while (iterator.hasNext()){
                Surface surface = iterator.next();

                ViewListener vl = surface.getViewListener();

                if(vl != null) vl.onDestroy();

                iterator.remove();
            }
        }

        Goin(contentview, null);
    }

    public boolean RemoveListView(ViewListener vl){
        for (int i = 0, len = stack.size(); i<len; i++){
            Surface s = stack.get(i);
            if (s.getViewListener() != null){
                if (s.getViewListener().equals(vl)){
                    if (i == stack.size() - 1){
                        GoBack();
                    }else {
                        s.getViewListener().onDestroy();

                        stack.remove(i);
                    }
                    return true;
                }
            }
        }

        return false;
    }

	public boolean Goin(int id, ViewListener vl){
		if(stack.size() > 0 && id == stack.get(stack.size()-1).getId()) return false;
		
		View temp = LayoutInflater.from(this).inflate(id, DecorView, false);
		
		return Goin(temp, vl);
	}

    /**
     * 压进 显示一个视图
     * @param contentview 要显示的视图
     * @param vl 视图状态接口
     * @return 是否显示成功
     */
    public boolean Goin(View contentview, ViewListener vl){
		if(isanimation) return false;

		Surface s = new Surface();

		s.setViewListener(vl);

		ViewListener vl_before = stack.size() > 0 ?  stack.get(stack.size()-1).getViewListener() : null;

		if(vl_before != null) vl_before.onPause();

		s.setContentView(contentview);

		addView(contentview, 0);
		
		if(vl != null) vl.onLoad(this);

		this.stack.add(s);

		Action(stack.size());

        return true;
	}
	
	//移除栈顶布局并显示下一个
	public boolean GoBack(){
		if(stack.size() <= 1 || isanimation)return false;

		Surface now = getLast();

		Bundle callback = null;
		
		if(now.getViewListener() != null)
		callback = now.getViewListener().getCallback();
		
		if(now.getViewListener() != null)
			now
				.getViewListener()
				.onDestroy();

		stack.remove(stack.size()-1);
		
		Surface last = getLast();
		
		if(last.getViewListener() != null && callback != null)
		last.getViewListener().Callback(callback);
		
		addView(last.getContentView(), 1);

		Action(stack.size());

		return true;
	}
	
	private Surface getLast(){
        int index = stack.size() - 1;
        if (index < 0) return null;
		return stack.get(index);
	}
	
	private Surface getTop(){
		return this.stack.get(0);
	}
	
	private void addView(View v){
		this.DecorView.removeAllViews();
		this.DecorView.addView(v);
	}
	
	private void addView(View v, View v2){
		this.DecorView.removeAllViews();
		this.DecorView.addView(v);
		this.DecorView.addView(v2);
	}
	
	/*
	private View addView(int id, final int anim_type)
	{
		View temp = LayoutInflater.from(this).inflate(id, DecorView, false);
		
		addView(temp, anim_type);
		
		return temp;
	}
	*/
	
	/*
	 添加布局
	 @contentview 要显示的view
	 @anim_type 动画 -1不显示 0进入动画 1退出动画
	 */
	private void addView(final View contentview, final int anim_type)
	{
		if (anim_type == 0)
		{
			final View be = BaseView;

			BaseView = contentview;

			isanimation = true;

			if (be == null)
			{
				this.addView(BaseView);

				isanimation = false;

				return;
			}	
			else
			{
				BaseView.setAlpha(0.0f);

				this.addView(be, BaseView);
			}

			BaseView.post(new Runnable(){
					@Override
					public void run()
					{
						if (be != null)
							be.animate()
								.alpha(0.0f)
								.setDuration(animaction_speed);

						BaseView.setTranslationX(-BaseView.getWidth());
						BaseView.animate()
							.alpha(1.0f)
							.translationX(0)
							.setDuration(animaction_speed / 2)
							.setListener(new android.animation.Animator.AnimatorListener(){
								@Override
								public void onAnimationStart(Animator p1)
								{
									// TODO: Implement this method
								}

								@Override
								public void onAnimationEnd(Animator p1)
								{
									DecorView.removeView(be);

									isanimation = false;
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
				});
		}
		else
		{
			final View v = contentview;

			isanimation = true;

			if (anim_type < 0)
			{
				this.addView(v);

				isanimation = false;

				DecorView.removeView(BaseView);

				BaseView = v;

				isanimation = false;

				if (stack.get(stack.size() - 1).getViewListener() != null)
					stack.get(stack.size() - 1)
						.getViewListener()
						.onResume();
				
				return;
			}
			
			this.addView(v, BaseView);

			v.setAlpha(0.0f);

			BaseView.setAlpha(1.0f);

			BaseView.post(new Runnable(){
					@Override
					public void run()
					{
						v.animate()
							.alpha(1.0f)
							.setDuration(animaction_speed / 2)
							.setListener(null);

						BaseView.animate()
							.alpha(0.0f)
							.translationX(-BaseView.getWidth())
							.setDuration(animaction_speed / 2)
							.setListener(
							new android.animation.Animator.AnimatorListener(){
								@Override
								public void onAnimationStart(Animator p1)
								{
									// TODO: Implement this method
								}

								@Override
								public void onAnimationEnd(Animator p1)
								{
									DecorView.removeView(BaseView);
									
									BaseView = v;
									
									isanimation = false;
									
									if (stack.get(stack.size() - 1).getViewListener() != null)
										stack.get(stack.size() - 1)
										.getViewListener()
										.onResume();
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
				});
		}
	}
	
	private final static class Surface
	{
		private View contentview;
		
		public ViewListener viewlistener;
		
		public boolean isLeftMoveBack;
		
		private int id;
		
		public Surface(View contentview, ViewListener vl)
		{
			this.contentview = contentview;
			
			this.viewlistener = vl;
		}

		public Surface(){}
		
		public void setContentView(View contentview)
		{
			this.contentview = contentview;
		}

		public View getContentView()
		{
			return contentview;
		}

		public void setViewListener(ViewListener viewlistener)
		{
			this.viewlistener = viewlistener;
		}

		public ViewListener getViewListener()
		{
			return viewlistener;
		}

		public Surface setId(int id)
		{
			this.id = id;
			
			return this;
		}

		public int getId()
		{
			return id;
		}
		
		public Surface setLeftMoveBackable(boolean is)
		{
			this.isLeftMoveBack = is;
			return this;
		}

	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
        if(onKeyDownPage(keyCode, event))
            return true;

        Surface s = getLast();

        if (s == null) return false;

        if(s.getViewListener() != null && s.getViewListener().onKeyDown(keyCode, event))
            return true;

		if(keyCode == KeyEvent.KEYCODE_BACK && !isanimation && !this.GoBack())
			OnEndLayout();
		
		return false;
	}

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(onKeyUpPage(keyCode, event))
            return true;

        Surface s = getLast();

        if (s == null) return false;

        if(s.getViewListener() != null && s.getViewListener().onKeyUp(keyCode, event))
            return true;

        return true;
    }

    @Override
    public void onBackPressed() {}

    @Override
    protected void onStop() {
        super.onStop();
        Surface s = getLast();
        if(s != null && s.getViewListener() != null)
            s.getViewListener().onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Surface s = getLast();
        if(s != null && s.getViewListener() != null)
            s.getViewListener().onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Surface s = getLast();
        if(s != null && s.getViewListener() != null)
            s.getViewListener().onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Surface s = getLast();
        if(s != null && s.getViewListener() != null)
            s.getViewListener().onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Surface s = getLast();
        if(s != null && s.getViewListener() != null)
            s.getViewListener().onDestroy();
    }

    //继承时应该重写该方法
    public boolean onKeyDownPage(int KeyCode, KeyEvent event) {return false;}

    public boolean onKeyUpPage(int KeyCode, KeyEvent event) {return false;}

    //栈的进出调用
	public void Action(int count){}
	
	public void OnEndLayout(){
        super.onBackPressed();
    }
	
}
