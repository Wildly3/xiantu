package com.xiuxian.chen.include;

import android.content.*;
import android.util.*;
import android.view.*;
import android.widget.*;

public class SlideCutListView extends ListView
{
	/**
	 * ��ǰ������ListView��position
	 */
	private int slidePosition;
	/**
	 * ��ָ����X�����
	 */
	private int downY;
	/**
	 * ��ָ����Y�����
	 */
	private int downX;
	/**
	 * ��Ļ���
	 */
	private int screenWidth;
	/**
	 * ListView��item
	 */
	private View itemView;
	/**
	 * ������
	 */
	private Scroller scroller;

	private static final int SNAP_VELOCITY = 600;
	/**
	 * �ٶ�׷�ٶ���
	 */
	private VelocityTracker velocityTracker;
	/**
	 * �Ƿ���Ӧ������Ĭ��Ϊ����Ӧ
	 */
	private boolean isSlide = false;
	/**
	 * ��Ϊ���û���������С����
	 */
	private int mTouchSlop;
	/**
	 *  �Ƴ�item��Ļص�ӿ�
	 */
	private RemoveListener mRemoveListener;
	/**
	 * ���ָʾitem������Ļ�ķ���,�����������,��һ��ö��ֵ����
	 */
	private RemoveDirection removeDirection;

	// ����ɾ�����ö��ֵ
	public enum RemoveDirection {
		RIGHT, LEFT;
	}


	public SlideCutListView(Context context) {
		this(context, null);
	}

	public SlideCutListView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SlideCutListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		screenWidth =
			((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))
			.getDefaultDisplay().getWidth();
		scroller = new Scroller(context);
		mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
	}

	/**
	 * ���û���ɾ��Ļص�ӿ�
	 * @param removeListener
	 */
	public void setRemoveListener(RemoveListener removeListener) {
		this.mRemoveListener = removeListener;
	}

	/**
	 * �ַ��¼�����Ҫ������жϵ������Ǹ�item, �Լ�ͨ��postDelayed�������Ӧ���һ����¼�
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN: {
					addVelocityTracker(event);

					// ����scroller���û�н�������ֱ�ӷ���
					if (!scroller.isFinished()) {
						return super.dispatchTouchEvent(event);
					}
					downX = (int) event.getX();
					downY = (int) event.getY();

					slidePosition = pointToPosition(downX, downY);

					// ��Ч��position, �����κδ���
					if (slidePosition == AdapterView.INVALID_POSITION) {
						return super.dispatchTouchEvent(event);
					}

					// ��ȡ���ǵ���item view
					itemView = getChildAt(slidePosition - getFirstVisiblePosition());
					break;
				}
			case MotionEvent.ACTION_MOVE: {
					if (Math.abs(getScrollVelocity()) > SNAP_VELOCITY
						|| (Math.abs(event.getX() - downX) > mTouchSlop && Math
						.abs(event.getY() - downY) < mTouchSlop)) {
						isSlide = true;
					}
					break;
				}
			case MotionEvent.ACTION_UP:
				recycleVelocityTracker();
				break;
		}

		return super.dispatchTouchEvent(event);
	}

	/**
	 * ���һ�����getScrollX()���ص������Ե�ľ��룬������View���ԵΪԭ�㵽��ʼ�����ľ��룬�������ұ߻���Ϊ��ֵ
	 */
	private void scrollRight() {
		removeDirection = RemoveDirection.RIGHT;
		final int delta = (screenWidth + itemView.getScrollX());
		// ����startScroll���������һЩ��Ĳ���������computeScroll()�����е���scrollTo���item
		scroller.startScroll(itemView.getScrollX(), 0, -delta, 0,
							 Math.abs(delta));
		postInvalidate(); // ˢ��itemView
	}

	/**
	 * ���󻬶��������������֪�����󻬶�Ϊ��ֵ
	 */
	private void scrollLeft() {
		removeDirection = RemoveDirection.LEFT;
		final int delta = (screenWidth - itemView.getScrollX());
		// ����startScroll���������һЩ��Ĳ���������computeScroll()�����е���scrollTo���item
		scroller.startScroll(itemView.getScrollX(), 0, delta, 0,
							 Math.abs(delta));
		postInvalidate(); // ˢ��itemView
	}

	/**
	 * �����ָ��itemView�ľ�����ж��ǹ����ʼλ�û�������������ҹ�
	 */
	private void scrollByDistanceX() {
		// ��������ľ��������Ļ�����֮һ��������ɾ��
		if (itemView.getScrollX() >= screenWidth / 3) {
			scrollLeft();
		} else if (itemView.getScrollX() <= -screenWidth / 3) {
			scrollRight();
		} else {
			// ��ص�ԭʼλ��,Ϊ��͵����������ֱ�ӵ���scrollTo��
			itemView.scrollTo(0, 0);
		}

	}

	/**
	 * ���������϶�ListView item���߼�
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (isSlide && slidePosition != AdapterView.INVALID_POSITION) {
			addVelocityTracker(ev);
			final int action = ev.getAction();
			int x = (int) ev.getX();
			switch (action) {
				case MotionEvent.ACTION_MOVE:
					int deltaX = downX - x;
					downX = x;

					// ��ָ�϶�itemView��, deltaX����0������С��0���ҹ�
					itemView.scrollBy(deltaX, 0);
					break;
				case MotionEvent.ACTION_UP:
					int velocityX = getScrollVelocity();
					if (velocityX > SNAP_VELOCITY) {
						scrollRight();
					} else if (velocityX < -SNAP_VELOCITY) {
						scrollLeft();
					} else {
						scrollByDistanceX();
					}

					recycleVelocityTracker();
					// ��ָ�뿪��ʱ��Ͳ���Ӧ���ҹ�
					isSlide = false;
					break;
			}

			return true; // �϶���ʱ��ListView����
		}

		//����ֱ�ӽ���ListView�����onTouchEvent�¼�
		return super.onTouchEvent(ev);
	}

	@Override
	public void computeScroll() {
		// ����startScroll��ʱ��scroller.computeScrollOffset()����true��
		if (scroller.computeScrollOffset()) {
			// ��ListView item��ݵ�ǰ�Ĺ�ƫ������й�
			itemView.scrollTo(scroller.getCurrX(), scroller.getCurrY());

			postInvalidate();

			// ����������ʱ����ûص�ӿ�
			if (scroller.isFinished()) {
				if (mRemoveListener == null) {
					throw new NullPointerException("RemoveListener is null, we should called setRemoveListener()");
				}

				itemView.scrollTo(0, 0);
				mRemoveListener.removeItem(removeDirection, slidePosition);
			}
		}
	}

	/**
	 * ����û����ٶȸ�����
	 * 
	 * @param event
	 */
	private void addVelocityTracker(MotionEvent event) {
		if (velocityTracker == null) {
			velocityTracker = VelocityTracker.obtain();
		}

		velocityTracker.addMovement(event);
	}

	/**
	 * �Ƴ��û��ٶȸ�����
	 */
	private void recycleVelocityTracker() {
		if (velocityTracker != null) {
			velocityTracker.recycle();
			velocityTracker = null;
		}
	}

	/**
	 * ��ȡX����Ļ����ٶ�,����0���һ�������֮����
	 * 
	 * @return
	 */
	private int getScrollVelocity() {
		velocityTracker.computeCurrentVelocity(1000);
		int velocity = (int) velocityTracker.getXVelocity();
		return velocity;
	}

	/**
	 * 
	 * ��ListView item������Ļ���ص����ӿ�
	 * ������Ҫ�ڻص��removeItem()���Ƴ��Item,Ȼ��ˢ��ListView
	 * 
	 * @author xiaanming
	 *
	 */
	public interface RemoveListener {
		public void removeItem(RemoveDirection direction, int position);
	}

}
