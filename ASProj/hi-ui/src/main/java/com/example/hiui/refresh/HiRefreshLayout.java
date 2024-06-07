package com.example.hiui.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.Scroller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.hilibrary.log.HiLog;

import java.util.ArrayList;
import java.util.List;

public class HiRefreshLayout extends FrameLayout implements HiRefresh {
    private static final String TAG = HiRefreshLayout.class.getSimpleName();
    private HiOverView.HiRefreshState mState;
    private GestureDetector mGestureDetector;
    private AutoScroller mAutoScroller;
    private HiRefresh.HiRefreshListener mHiRefreshListener;
    private HiOverView mHiOverView;
    //最后下拉的Y轴坐标
    private int mLastY;
    //刷新时是否禁止滚动
    private boolean disableRefreshScroll;

    public HiRefreshLayout(@NonNull Context context) {
        super(context);
        init();
    }

    public HiRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HiRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mGestureDetector = new GestureDetector(getContext(), hiGestureDetector);
        mAutoScroller = new AutoScroller();
        HiLog.e(TAG,"init....");
    }


    HiGestureDetector hiGestureDetector = new HiGestureDetector() {
        @Override
        public boolean onScroll(@NonNull MotionEvent e1, @NonNull MotionEvent e2, float distanceX, float distanceY) {
            HiLog.i(TAG, "disY" + distanceY);
            if (Math.abs(distanceX) > Math.abs(distanceY) || mHiRefreshListener != null && !mHiRefreshListener.enableRefresh()) {
                //横向滑动，或刷新被禁止则不处理
                HiLog.i(TAG, "横向滑动，或刷新被禁止则不处理");
                return false;
            }
            if (disableRefreshScroll && mState == HiOverView.HiRefreshState.STATE_REFRESH) {
                //刷新时禁止滑动
                HiLog.i(TAG, "刷新时 禁止滑动");
                return true;
            }

            View head = getChildAt(0);
            View child = HiScrollUtil.findScrollableChild(HiRefreshLayout.this);
            if (HiScrollUtil.childScrolled(child)) {
                //如果列表发生了滚动则不处理
                HiLog.i(TAG, "列表发生了滚动 不处理");
                return false;
            }
            //没有刷新或没有达到可以刷新的距离，且头部已经划出或下拉，没有达到刷新的最小距离
            if ((mState != HiOverView.HiRefreshState.STATE_REFRESH
                    || head.getBottom() <= mHiOverView.mPullRefreshHeight)
                    && (head.getBottom() > 0 || distanceY <= 0.0F)) {
                //还在手动滑动中并没有释放
                if (mState != HiOverView.HiRefreshState.STATE_OVER_RELEASE) {
                    int dis;
                    //阻尼计算 根据下拉的距离
                    if (child.getTop() < mHiOverView.mPullRefreshHeight) {
                        dis = (int) (mLastY / mHiOverView.minDamp);
                    } else {
                        dis = (int) (mLastY / mHiOverView.maxDamp);
                    }
                    //如果是正在刷新状态，则不允许在滑动的时候改变状态
                    boolean bool = moveDown(dis, true);
                    mLastY = (int) (-distanceY);
                    return bool;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    };

    /**
     * 根据偏移量移动header与child
     * @param offsetY
     * @param nonAuto
     * @return
     */
    private boolean moveDown(int offsetY, boolean nonAuto) {
        View head = getChildAt(0);
        View child = getChildAt(1);
        int childTop = child.getTop() + offsetY;
        if (childTop <= 0) {//异常情况的补充 手指向上滑动
            offsetY = -childTop;
            //移动head与child的位置，到原始位置
            head.offsetTopAndBottom(offsetY);
            child.offsetTopAndBottom(offsetY);
            if (mState != HiOverView.HiRefreshState.STATE_REFRESH) {
                mState = HiOverView.HiRefreshState.STATE_INIT;
            }
        } else if (mState == HiOverView.HiRefreshState.STATE_REFRESH
                && childTop > mHiOverView.mPullRefreshHeight) {
            //如果正在下拉刷新中，禁止继续下拉
            return false;
        } else if (childTop <= mHiOverView.mPullRefreshHeight) {
            //下拉高度 还没超出设定的刷新距离
            if (mHiOverView.getState() != HiOverView.HiRefreshState.STATE_VISIBLE
                    && nonAuto) {
                mHiOverView.onVisible();
                mHiOverView.setState(HiOverView.HiRefreshState.STATE_VISIBLE);
                mState = HiOverView.HiRefreshState.STATE_VISIBLE;
            }
            head.offsetTopAndBottom(offsetY);
            child.offsetTopAndBottom(offsetY);
            if (childTop == mHiOverView.mPullRefreshHeight &&
                    mState == HiOverView.HiRefreshState.STATE_OVER_RELEASE) {
                HiLog.i(TAG, "refresh,childTop: " + childTop);
                refresh();
            }
        } else {
            //超出最小刷新位置
            if (mHiOverView.getState() != HiOverView.HiRefreshState.STATE_OVER && nonAuto) {
                //超出刷新位置
                mHiOverView.onOver();
                //设置状态
                mHiOverView.setState(HiOverView.HiRefreshState.STATE_OVER);
            }
            head.offsetTopAndBottom(offsetY);
            child.offsetTopAndBottom(offsetY);
        }
        if (mHiOverView != null) {
            mHiOverView.onScroll(head.getBottom(), mHiOverView.mPullRefreshHeight);
        }
        return true;
    }

    /**
     * 刷新
     */
    private void refresh() {
        if (mHiRefreshListener != null) {
            mState = HiOverView.HiRefreshState.STATE_REFRESH;
            mHiOverView.onRefresh();
            mHiOverView.setState(HiOverView.HiRefreshState.STATE_REFRESH);
            mHiRefreshListener.onRefresh();
        }
    }

    @Override
    public void setDisableRefreshScroll(boolean disableRefreshScroll) {
        this.disableRefreshScroll = disableRefreshScroll;
    }

    @Override
    public void refreshFinished() {
        final View head = getChildAt(0);
        HiLog.i(this.getClass().getSimpleName(), "refreshFinished head-bottom:" +
                head.getBottom());
        mHiOverView.onFinish();
        mHiOverView.setState(HiOverView.HiRefreshState.STATE_INIT);
        final int bottom = head.getBottom();
        if (bottom > 0) {
            recover(bottom);
        }
        mState = HiOverView.HiRefreshState.STATE_INIT;
    }

    @Override
    public void setRefreshOverView(HiOverView hiOverView) {
        if (this.mHiOverView != null) {
            removeView(mHiOverView);
        }
        this.mHiOverView = hiOverView;
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        addView(mHiOverView, 0, params);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        HiLog.e(TAG,event.toString());
        //事件分发处理
        if (!mAutoScroller.isFinished()) {
            return false;
        }

        View head = getChildAt(0);
        //松开手后的处理
        if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL
                || event.getAction() == MotionEvent.ACTION_POINTER_INDEX_MASK) {
            //松开手
            if (head.getBottom() > 0) {
                //头部已下拉，
                if (mState != HiOverView.HiRefreshState.STATE_REFRESH) {
                    recover(head.getBottom());
                    return false;
                }
            }
            mLastY = 0;
        }
        //一直下拉没有松开手
        boolean consumed = mGestureDetector.onTouchEvent(event);
        if ((consumed || (mState != HiOverView.HiRefreshState.STATE_INIT &&
                mState != HiOverView.HiRefreshState.STATE_REFRESH))
                && head.getBottom() != 0) {
            //让父类接收不到真实的事件
//            event.setAction(MotionEvent.ACTION_CANCEL);
            return super.dispatchTouchEvent(event);
        }

        if (consumed) {
            return true;
        } else {
            return super.dispatchTouchEvent(event);
        }
    }

    /**
     * 恢复到原位置
     *
     * @param dis
     */
    private void recover(int dis) {
        if (mHiRefreshListener != null && dis > mHiOverView.mPullRefreshHeight) {
            //滑动到刷新位置 HiOverView的底部到达最小刷新距离位置
            mAutoScroller.recover(dis - mHiOverView.mPullRefreshHeight);
            mState = HiOverView.HiRefreshState.STATE_OVER_RELEASE;
            HiLog.i(TAG, "超过最小刷新距离后，恢复到刷新位置");
            List<Integer> req = new ArrayList<>();
            req.stream().mapToInt(Integer::intValue).toArray();
        } else {
            mAutoScroller.recover(dis);
            HiLog.i(TAG, "没达到最小刷新距离，恢复到原始");
        }
    }

    @Override
    public void setRefreshListener(HiRefreshListener hiRefreshListener) {
        this.mHiRefreshListener = hiRefreshListener;
    }

    /**
     * 借助Scroller实现视图的自动滚动
     */
    public class AutoScroller implements Runnable {
        private Scroller mScroller;
        private int mLastY;
        private boolean mIsFinished;

        AutoScroller() {
            mScroller = new Scroller(getContext(), new LinearInterpolator());
            mIsFinished = true;
        }

        @Override
        public void run() {
            if (mScroller.computeScrollOffset()) {//还未滚动完成
                HiLog.e(TAG, "Scroller计算的偏移量" + (mScroller.getCurrY()));
                HiLog.e(TAG, "自动滚动偏移量" + (mLastY - mScroller.getCurrY()));
                moveDown(mLastY - mScroller.getCurrY(), false);
                //当前偏移量Y
                //最终mLastY的值就是要移动的距离 dis
                mLastY = mScroller.getCurrY();
                HiLog.e(TAG, "mLastY" + mLastY);
                post(this);
            } else {
                removeCallbacks(this);
                mIsFinished = true;
            }
        }

        void recover(int dis) {
            if (dis <= 0) {
                return;
            }
            HiLog.e(TAG, "要恢复到的位置" + (dis));
            removeCallbacks(this);
            mLastY = 0;
            mIsFinished = false;
            mScroller.startScroll(0, 0, 0, dis, 300);
            post(this);
        }

        boolean isFinished() {
            return mIsFinished;
        }
    }



    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        //定义head和child的排列位置
        View head = getChildAt(0);
        View child = getChildAt(1);
        if (head != null && child != null) {
            int childTop = child.getTop();
            if (mState == HiOverView.HiRefreshState.STATE_REFRESH) {
                head.layout(0, mHiOverView.mPullRefreshHeight - head.getMeasuredHeight(),
                        right, mHiOverView.mPullRefreshHeight);
                child.layout(0, mHiOverView.mPullRefreshHeight, right,
                        mHiOverView.mPullRefreshHeight + child.getMeasuredHeight());
            } else {
                head.layout(0, childTop - head.getMeasuredHeight(), right, childTop);
                child.layout(0, childTop, right, childTop + child.getMeasuredHeight());
            }
            View other;
            for (int i = 2; i < getChildCount(); ++i) {
                other = getChildAt(i);
                other.layout(0, top, right, bottom);
            }
        }
    }
}
