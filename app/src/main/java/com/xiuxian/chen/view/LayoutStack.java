package com.xiuxian.chen.view;

import android.animation.Animator;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Wildly on 2018/8/29.20:49
 */

//一个布局的简单栈

public class LayoutStack extends AppCompatActivity
{
    private static final String TAG = "LayoutStack";

    //默认最小动画时长
    private static final int ANIMACTION_MIN_SPEED = 300;

    //栈最大数
    public static final int LAYOUT_STACK_MAX = 6;

    private List<Surface> stack;

    private View BaseView = null;

    private MyLayout LevitationView;

    private Drawable LevitationBackground;

    //是否有动画
    private boolean animation;

    //是否处于动画中
    private boolean isanimation;

    private int animaction_speed = ANIMACTION_MIN_SPEED;

    //顶层View
    private RelativeLayout DecorView;

    private int ContentTop;

    private int WindowBackgroundColor;

    public LayoutStack(){
        this.stack=new ArrayList<Surface>(LAYOUT_STACK_MAX);
        this.animation = true;
        this.LevitationBackground = new ColorDrawable(Color.WHITE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        DecorView = new RelativeLayout(this);

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(-1, -1);

        DecorView.setLayoutParams(lp);

        super.setContentView(this.DecorView);

        ContentTop = getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();

        Drawable drawable = getWindow().getDecorView().getBackground();

        if (drawable != null && drawable instanceof ColorDrawable){
            WindowBackgroundColor = ((ColorDrawable)drawable).getColor();
        }
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

    public View ShowLevitationView(int id){
        View view = LayoutInflater.from(this).inflate(id, null);
        ShowLevitationView(view);
        return view;
    }

    /**显示一个悬浮视图，不可重复显示，必须关闭上一个视图才可以显示（底部上划关闭、返回键关闭）
     * @param view 视图
     */
    public void ShowLevitationView(View view){
        if (LevitationView != null) return;
        final MyLayout frameLayout = new MyLayout(this);
        frameLayout.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        frameLayout.setBackground(this.LevitationBackground);
        frameLayout.addView(view);

        final MyLayout background = new MyLayout(this);

        background.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));

        background.setVisibility(View.INVISIBLE);

        background.addView(frameLayout);

        background.setOnTouchEvent(new View.OnTouchListener() {
            boolean isTouchBottom;
            int starty;
            int offsety;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int height = DecorView.getHeight() - (5 * DecorView.getHeight() / 100);
                int y = (int) event.getY();
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        if (y >= height) {
                            isTouchBottom = true;
                            offsety = y;
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                       offsety = y;
                        break;
                    case MotionEvent.ACTION_UP:
                        if (!isTouchBottom) break;
                        isTouchBottom = false;
                        if (offsety < height - (5 * DecorView.getHeight() / 100)){
                            CloseLevitationView();
                        }
                        break;
                }
                return false;
            }
        });

        this.DecorView.addView(background);
        frameLayout.setTranslationY(-this.DecorView.getHeight());
        background.setVisibility(View.VISIBLE);
        frameLayout.animate()
                .translationY(0)
                .setDuration(animaction_speed);
        LevitationView = background;
    }

    /**设置悬浮视图的背景
     * @param background
     */
    public void setLevitationBackground(Drawable background){
        if (LevitationViewExist())
            this.LevitationView.setBackground(background);

        this.LevitationBackground = background;
    }

    /**关闭一个悬浮视图
     * @param pattern 是否有关闭动画
     */
    public void CloseLevitationView(boolean pattern){
        if (LevitationView != null) {
            if (pattern)
            LevitationView.animate()
                    .translationY(-this.DecorView.getHeight())
                    .setDuration(animaction_speed)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {}
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            LayoutStack.this.DecorView.removeView(LevitationView);
                            LevitationView = null;
                        }
                        @Override
                        public void onAnimationCancel(Animator animation) {}
                        @Override
                        public void onAnimationRepeat(Animator animation) {}
                    });
            else {
                LayoutStack.this.DecorView.removeView(LevitationView);
                LevitationView = null;
            }
        }
    }

    public void CloseLevitationView(){
        CloseLevitationView(true);
    }

    public boolean LevitationViewExist(){
        return LevitationView != null;
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

    /**根据listener来删除视图
     * @param vl 要删除的视图
     * @return 删除成功返回true
     */
    public boolean RemoveListView(ViewListener vl){
        if (stack.size() < 2) return false;
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

        FrameLayout background = new FrameLayout(this);

        background.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));

        background.setBackground(new ColorDrawable(WindowBackgroundColor));

        background.addView(contentview);

        Surface s = new Surface();

        s.setViewListener(vl);

        invalidateOptionsMenu();

        ViewListener vl_before = stack.size() > 0 ?  stack.get(stack.size()-1).getViewListener() : null;

        if(vl_before != null) vl_before.onPause();

        s.setContentView(background);

        ShowView(background, animation ? 0 : -1);

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

        ShowView(last.getContentView(), animation ? 1 : -2);

        Action(stack.size());

        invalidateOptionsMenu();

        return true;
    }

    /**获取末尾视图
     * @return
     */
    private Surface getLast(){
        int index = stack.size() - 1;
        if (index < 0) return null;
        return stack.get(index);
    }

    /**获取顶部视图
     * @return
     */
    private Surface getTop(){
        if (stack.size() < 1) return null;
        return this.stack.get(0);
    }

    /**是否只有一个视图
     * @return
     */
    public boolean isFinally(){
        return stack.size() == 1;
    }

    /**清除之前的视图进行视图的添加
     * @param v 视图
     */
    private void addView(View v){
        this.DecorView.removeAllViews();
        this.DecorView.addView(v);
    }

    /**清除之前的视图进行视图的添加（由于布局特性v视图会被v2视图覆盖）
     * @param v 视图1
     * @param v2 视图2
     */
    private void addView(View v, View v2){
        this.DecorView.removeAllViews();
        this.DecorView.addView(v);
        this.DecorView.addView(v2);
    }

    /**显示一个视图
     * @param contentView 要显示的视图
     * @param pattern 显示模式 0为进入并且显示动画 -1不显示动画， 1为退出并且显示动画 -2不显示动画
     */
    private void ShowView(final View contentView, final int pattern){
        if (pattern == 0 || pattern == -1){
            final View tempView = BaseView;
            BaseView = contentView;
            isanimation = true;

            this.DecorView.addView(contentView);
            if (pattern == -1 || stack.size() < 1){
                DecorView.removeView(tempView);
                isanimation = false;
                return;
            }
            contentView.setVisibility(View.INVISIBLE);
            contentView.post(new Runnable() {
                @Override
                public void run() {
                    contentView.setTranslationX(-contentView.getWidth());
                    contentView.setVisibility(View.VISIBLE);
                    contentView.animate()
                            .translationX(0)
                            .setDuration(animaction_speed)
                            .setListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animation) {}
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    if (tempView != null)
                                        DecorView.removeView(tempView);
                                    isanimation = false;
                                }
                                @Override
                                public void onAnimationCancel(Animator animation) {}
                                @Override
                                public void onAnimationRepeat(Animator animation) {}
                            });
                }
            });

        } else
            if (pattern == 1 || pattern == -2){
                isanimation = true;
                final Surface surface = getLast();
                final boolean isCall = surface != null && surface.getViewListener() != null;
                if (pattern == -2){
                    this.addView(contentView);
                    isanimation = false;
                    BaseView = contentView;
                    if (isCall){
                        surface
                                .getViewListener()
                                .onResume();
                    }
                    return;
                }

                this.addView(contentView, BaseView);
                BaseView.post(new Runnable() {
                    @Override
                    public void run() {
                        BaseView.animate()
                                .translationX(-BaseView.getWidth())
                                .setDuration(animaction_speed)
                                .setListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animation) {}
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        DecorView.removeView(BaseView);
                                        BaseView = contentView;
                                        isanimation = false;
                                        if (isCall){
                                            surface
                                                    .getViewListener()
                                                    .onResume();
                                        }
                                    }
                                    @Override
                                    public void onAnimationCancel(Animator animation) {}
                                    @Override
                                    public void onAnimationRepeat(Animator animation) {}
                                });
                    }
                });
            }
    }

    private final static class Surface
    {
        private View contentview;

        public ViewListener viewlistener;

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

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        Surface surface = getLast();

        if (surface.getViewListener() != null){
            return surface.getViewListener().onCreateOptionsMenu(menu);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Surface surface = getLast();

        if (surface.getViewListener() != null){
            return surface.getViewListener().onOptionsItemSelected(item);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (onKeyDownPage(keyCode, event))
            return true;

        Surface s = getLast();

        if (s == null) return false;

        if (s.getViewListener() != null && s.getViewListener().onKeyDown(keyCode, event))
            return true;

        if (keyCode == KeyEvent.KEYCODE_BACK && !isanimation) {
            if (LevitationViewExist()){
              CloseLevitationView();
            } else
            if (!(s.getViewListener() != null && s.getViewListener().onBack()))
                if (!this.GoBack())
                    OnEndLayout();
        }
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

    /**
     * 在最后一个视图按下back键时调用（不覆盖默认调用父类onBackPressed方法，即退出）
     */
    public void OnEndLayout(){
        super.onBackPressed();
    }

}