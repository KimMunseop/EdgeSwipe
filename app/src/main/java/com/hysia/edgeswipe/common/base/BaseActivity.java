package com.hysia.edgeswipe.common.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;

import com.hysia.edgeswipe.R;
import com.hysia.edgeswipe.common.manager.ActivityManager;
import com.hysia.edgeswipe.component.main.MainActivity;

/**
 * Activity의 Base Class
 * Created by hysia on 2017. 7. 17..
 */

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            if(this instanceof MainActivity) {
                savedInstanceState.remove("android:viewHierarchyState");
                savedInstanceState.remove("android:support:fragments");

            } else {
                finish();
            }
        }

        super.onCreate(savedInstanceState);

        // Activity 추가
        ActivityManager.getInstance().addActivityToStack(this);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // Edge Swipe 이벤트 전달.
        if(ActivityManager.getInstance().onEdgeSwipeEvent(ev)) {
            return super.dispatchTouchEvent(ev);
        }

        return false;
    }

    /**
     * Start Slide Animation
     * @param intent
     * @param reqestCode
     */
    public void startActivityForResultWithSlideAnimation(@NonNull Intent intent, int reqestCode) {
        startActivityForResult(intent, reqestCode);
        overridePendingTransition(R.anim.slide_in_right, android.R.anim.fade_out);
    }

    /**
     * Finish Slide Animation
     */
    public void finishActivityWithSlideAnimation() {
        finish();
        overridePendingTransition(android.R.anim.fade_in, R.anim.slide_out_right);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Activity 삭제
        ActivityManager.getInstance().removeActivityFromStack(this);
    }
}
