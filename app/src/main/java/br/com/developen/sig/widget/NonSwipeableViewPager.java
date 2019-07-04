package br.com.developen.sig.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

public class NonSwipeableViewPager extends ViewPager {


    private boolean enabled;


    public NonSwipeableViewPager(Context context, AttributeSet attrs) {

        super(context, attrs);

        this.enabled = false;

    }


    public boolean onTouchEvent(MotionEvent event) {

        if (this.enabled)

            return super.onTouchEvent(event);

        return false;

    }


    public boolean onInterceptTouchEvent(MotionEvent event) {

        if (this.enabled)

            return super.onInterceptTouchEvent(event);

        return false;

    }


    public void setPagingEnabled(boolean enabled) {

        this.enabled = enabled;

    }


}