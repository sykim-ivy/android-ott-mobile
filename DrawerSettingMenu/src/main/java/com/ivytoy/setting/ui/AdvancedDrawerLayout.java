package com.ivytoy.setting.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import androidx.core.view.GravityCompat;
import androidx.core.view.MotionEventCompat;
import androidx.core.view.ViewCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import org.jetbrains.annotations.NotNull;

import kotlin.jvm.functions.Function0;

/**
 * 增强DrawerLayout
 * 1. 支持右侧双层Drawer, 原本的DrawerLayout会报错
 * 2. 支持第一层Drawer点击收起
 * Created by assistne on 16/10/25.
 */

public class AdvancedDrawerLayout extends DrawerLayout {

    private float mInitialMotionX;
    private float mInitialMotionY;
    private View mRightBelowView;
    private View mRightAboveView;
    private int mTouchSlop;
    public AdvancedDrawerLayout(Context context) {
        this(context, null);
    }

    public AdvancedDrawerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AdvancedDrawerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        final ViewConfiguration vc = ViewConfiguration.get(context);
        mTouchSlop = vc.getScaledTouchSlop();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int childCount = getChildCount();
        // 双层Drawer, 原生会抛出异常
        try {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } catch (IllegalStateException e) {
            /** 默认最后一个子View是第二层Drawer */
            // 因为抛出异常, 所以手动测量第二层Drawer
            final float density = getResources().getDisplayMetrics().density;
            int minDrawerMargin = (int) (64 * density + 0.5f);
            final View child = getChildAt(childCount - 1);
            final LayoutParams lp = (LayoutParams) child.getLayoutParams();
            final int drawerWidthSpec = getChildMeasureSpec(widthMeasureSpec,
                    minDrawerMargin + lp.leftMargin + lp.rightMargin,
                    lp.width);
            final int drawerHeightSpec = getChildMeasureSpec(heightMeasureSpec,
                    lp.topMargin + lp.bottomMargin,
                    lp.height);
            child.measure(drawerWidthSpec, drawerHeightSpec);
        }

        // 找出两个Drawer, 用来处理点击收起Drawer问题
        if (mRightBelowView == null || mRightAboveView == null) {
            mRightBelowView = null;
            mRightAboveView = null;
            for (int i = 0; i < childCount; i ++) {
                final View child = getChildAt(i);
                if (checkDrawerViewAbsoluteGravity(child, Gravity.RIGHT)) {
                    if (mRightBelowView == null) {
                        mRightBelowView = child;
                    } else {
                        mRightAboveView = child;
                    }
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        /** 双层Drawer时, 不能正常通过点击收起第一层Drawer, 所以在这里自己处理 */
        final int action = ev.getAction();
        switch (action & MotionEventCompat.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {
                final float x = ev.getX();
                final float y = ev.getY();
                mInitialMotionX = x;
                mInitialMotionY = y;
                break;
            }

            case MotionEvent.ACTION_UP: {
                if (mRightAboveView != null && mRightBelowView != null) {
                    final float x = ev.getX();
                    final float y = ev.getY();
                    if (x < mRightBelowView.getLeft()) {
                        final float dx = x - mInitialMotionX;
                        final float dy = y - mInitialMotionY;
                        final int slop = mTouchSlop;
                        if (dx * dx + dy * dy < slop * slop) {
                            // 当第二层Drawer没有打开而第一层Drawer打开时, 收起第一层Drawer
                            if (!isDrawerOpen(mRightAboveView) && isDrawerOpen(mRightBelowView)) {
                                closeDrawer(mRightBelowView);
                                return true;
                            }
                        }
                    }
                }
                break;
            }
        }
        // 其他情况使用默认代码
        return super.onTouchEvent(ev);
    }

    int getDrawerViewAbsoluteGravity(View drawerView) {
        final int gravity = ((LayoutParams) drawerView.getLayoutParams()).gravity;
        return GravityCompat.getAbsoluteGravity(gravity, ViewCompat.getLayoutDirection(this));
    }

    boolean checkDrawerViewAbsoluteGravity(View drawerView, int checkFor) {
        final int absGravity = getDrawerViewAbsoluteGravity(drawerView);
        return (absGravity & checkFor) == checkFor;
    }

}