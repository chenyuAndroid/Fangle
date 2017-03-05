package com.chenyu.fangle.utils;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2016/9/19.
 */
public class FabBehavior extends CoordinatorLayout.Behavior {

    int offsetTotal = 0;
    int childTotalHeight = 0;
    boolean isHiding = false;

    public FabBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        return true;
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        offset(child,dyConsumed);
    }

    @Override
    public boolean onNestedFling(CoordinatorLayout coordinatorLayout, View child, View target, float velocityX, float velocityY, boolean consumed) {
        return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);
    }

    private void offset(View child,int dy){
        //向下滑，dy为正；向上滑，dy为负
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
        childTotalHeight = child.getHeight() + layoutParams.bottomMargin;

        if(dy > 0){
            if(offsetTotal + dy >= childTotalHeight){
                dy = childTotalHeight - offsetTotal;
            }else if(offsetTotal >= childTotalHeight){
                return;
            }
        }else {
            if(offsetTotal + dy <= 0){
                dy = -offsetTotal;
            }else if(offsetTotal <= 0){
                return;
            }
        }

        offsetTotal += dy;

        Log.d("cylog","offsetTotal:"+offsetTotal+"  childTotalHeight:"+childTotalHeight);
        child.offsetTopAndBottom(dy);


    }
}
