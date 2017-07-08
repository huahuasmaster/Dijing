package com.example.administrator.lalala.assistant;

import android.view.MotionEvent;
import android.view.VelocityTracker;

/**
 * Created by Administrator on 2017/4/30.
 */

public class SwipeBackHelper {
    public static final int YSPEED_MIN = 1000;
    public static final int XDISTANCE_MIN = 200;
    public static final int YDISTANCE_MIN = 100;

    /**
     *
     * @return 滑动速度，以每秒钟移动了多少像素值为单位。
     */
    public static int getScrollVelocity(VelocityTracker mVelocityTracker) {
        mVelocityTracker.computeCurrentVelocity(1000);
        int velocity = (int) mVelocityTracker.getYVelocity();
        return Math.abs(velocity);
    }
    /**
     * 创建VelocityTracker对象，并将触摸界面的滑动事件加入到VelocityTracker当中。
     *
     * @param event
     *
     */
    public static VelocityTracker createVelocityTracker(MotionEvent event,VelocityTracker mVelocityTracker) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
        return mVelocityTracker;
    }
    /**
     * 回收VelocityTracker对象。
     */
    public static void recycleVelocityTracker(VelocityTracker mVelocityTracker) {
        if(mVelocityTracker != null) {
            try {
                mVelocityTracker.recycle();
                mVelocityTracker = null;
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }





}
