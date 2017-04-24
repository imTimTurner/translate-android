package com.github.imtimturner.translate.ui.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * ViewPager у которого реализована возможность отчлючать/подключать swipe
 * */
public class TRViewPager extends ViewPager {

    private boolean paging;

    public TRViewPager(Context context) {
        super(context);
    }

    public TRViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return paging && super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return paging && super.onTouchEvent(ev);
    }

    public void setPagingEnabled(boolean enabled){
        this.paging = enabled;
    }

}
