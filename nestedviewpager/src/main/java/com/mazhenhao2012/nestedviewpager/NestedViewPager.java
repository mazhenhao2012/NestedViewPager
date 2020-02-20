package com.mazhenhao2012.nestedviewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class NestedViewPager extends ViewPager {

    public NestedViewPager(@NonNull Context context) {
        super(context);
    }

    public NestedViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    private float mLastTouchX = 0;
    private float mLastTouchY = 0;
    private int mTargetPointerId = 0;
    private int[] mLocationInWindow = new int[2];

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        getLocationInWindow(mLocationInWindow);
        switch (e.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mTargetPointerId = e.getPointerId(0);
                mLastTouchX = e.getX(0) + mLocationInWindow[0];
                mLastTouchY = e.getY(0) + mLocationInWindow[1];
                break;
            case MotionEvent.ACTION_MOVE:
                int index = e.findPointerIndex(mTargetPointerId);
                if (index < 0) break;
                float currentX = e.getX(index) + mLocationInWindow[0];
                float currentY = e.getY(index) + mLocationInWindow[1];
                float dx = currentX - mLastTouchX;
                float dy = currentY - mLastTouchY;

                if (Math.abs(dy) > Math.abs(dx)) {
                    return false;
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                int actionIndex = e.getActionIndex();
                if (e.getPointerId(actionIndex) == mTargetPointerId) {
                    final int newPointerIndex = actionIndex == 0 ? 1 : 0;
                    mTargetPointerId = e.getPointerId(newPointerIndex);
                    mLastTouchX = e.getX(newPointerIndex) + mLocationInWindow[0];
                    mLastTouchY = e.getY(newPointerIndex) + mLocationInWindow[1];
                }
                break;
        }
        return super.onInterceptTouchEvent(e);
    }


}
