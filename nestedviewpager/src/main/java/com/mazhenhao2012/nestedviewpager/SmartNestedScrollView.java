package com.mazhenhao2012.nestedviewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;

public class SmartNestedScrollView extends NestedScrollView {

    private int[] mLocationInWindow = new int[2];
    private int[] mChildLocationInWindow = new int[2];

    public SmartNestedScrollView(@NonNull Context context) {
        super(context);
    }

    public SmartNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SmartNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        getLocationInWindow(mLocationInWindow);
        target.getLocationInWindow(mChildLocationInWindow);

        int childY = mChildLocationInWindow[1] - mLocationInWindow[1];
        int dyConsumed = 0;
        if (dy != 0) {
            if (canScrollVertically(dy)) {
                if (dy > 0) { // scroll down childY变小 吸住bottom
                    int childBottomOffset = childY + target.getHeight() - getHeight();
                    if (childBottomOffset > 0) {
                        dyConsumed = Math.min(dy, childBottomOffset);
                    }
                } else { // scroll up childY变大 吸住top
                    if (childY < 0) {
                        dyConsumed = Math.max(dy, childY);
                    }
                }
            }
        }
        if (dyConsumed != 0) {
            scrollBy(0, dyConsumed);
        }
        int dyUnconsumed = dy - dyConsumed;
        if (dyUnconsumed != 0) {
            if (hasNestedScrollingParent(type) || startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL, type)) {
                onNestedPreScroll(target, 0, dyUnconsumed, consumed, type);
            }
        }
        consumed[1] += dyConsumed;
    }

}
