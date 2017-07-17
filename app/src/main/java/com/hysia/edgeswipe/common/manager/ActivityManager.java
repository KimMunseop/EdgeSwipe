package com.hysia.edgeswipe.common.manager;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.Pair;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.hysia.edgeswipe.component.main.MainActivity;
import com.hysia.edgeswipe.common.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 실행중인 Activity를 관리하는 Manager
 *
 * Created by hysia on 2017. 7. 17..
 */

public class ActivityManager {
    /**
     * instance 객체
     */
    private static ActivityManager mInstance;

    /**
     * activity stack
     */
    private List<Pair<BaseActivity, EdgeSwipeController>> mActivityStack;


    public static ActivityManager getInstance() {
        synchronized (ActivityManager.class) {
            if(mInstance == null) {
                mInstance = new ActivityManager();
            }
        }

        return mInstance;
    }

    private ActivityManager() {
        mActivityStack = new ArrayList<>();
    }


    /**
     * 엣지 스와이프를 사용하려면 dispatchTouchEvent의 이벤트를 전달해야 한다.
     * ex)
     * @Override
     * public boolean dispatchTouchEvent(MotionEvent ev) {
     *   if(ActivityManager.getInstance().onEdgeSwipeEvent(ev)) {
     *       return super.dispatchTouchEvent(ev);
     *   }
     *   return false;
     * }
     * @param ev
     * @return
     */
    public boolean onEdgeSwipeEvent(MotionEvent ev) {
        if(mActivityStack == null) {
            return true;
        }

        Pair<BaseActivity, EdgeSwipeController> topStack =  mActivityStack.get(mActivityStack.size() - 1);
        EdgeSwipeController controller = topStack.second;

        if(controller != null) {
            return controller.onInterceptTouchEvent(ev);
        }

        return true;
    }


    /**
     * activity를 stack에 추가한다.
     * (activity의 onCreate에 정의)
     *
     * @param activity
     */
    public void addActivityToStack(@NonNull BaseActivity activity) {
        if(mActivityStack == null) {
            mActivityStack = new ArrayList<>();
        }

        mActivityStack.add(new Pair<>(activity, new EdgeSwipeController(activity)));
    }


    /**
     * activity를 stack에 추가한다.
     * (activity의 onDestroy에 정의)
     *
     * @param activity
     */
    public void removeActivityFromStack(@NonNull BaseActivity activity) {
        if(mActivityStack == null) {
            return;
        }

        for(Pair<BaseActivity, EdgeSwipeController> stack : mActivityStack) {
            if(stack == null) {
                continue;
            }

            if(stack.first == activity) {
                mActivityStack.remove(stack);
                break;
            }
        }
    }


    /**
     *
     * @return activty stack size
     */
    public int getActivityStackCount() {
        if(mActivityStack == null) {
            return 0;
        }

        return mActivityStack.size();
    }

    /**
     *
     * @return stack의 최상단 activity
     * @throws Exception
     */
    public BaseActivity getTopActivityFromStack() throws Exception {
        if(mActivityStack == null) {
            throw new NullPointerException();
        }

        Pair<BaseActivity, EdgeSwipeController> topStack =  mActivityStack.get(mActivityStack.size() - 1);
        if(topStack != null) {
            return topStack.first;
        }

        return null;
    }


    public void release() {
        mActivityStack.clear();
        mInstance = null;
    }


    /**
     * 엣지 스와이프를 위한 Controller 정의
     */
    public class EdgeSwipeController {
        private static final float SWIPE_START_MAX_DX = 30.0f;
        private static final float SWIPE_START_MIN_DY = 200.0f;
        private static final float SWIPE_AVAILABLE_VELOCITY = 0.5f;

        private BaseActivity mActivity;
        private GestureDetector mDetector;

        private boolean isAvailableEdgeSwipe = false;
        private boolean isExecuteEdgeSwipe = false;

        private boolean isEnabled = true;


        public EdgeSwipeController(BaseActivity activity) {
            this.mActivity = activity;

            mDetector = new GestureDetector(mActivity, mGestureListener);
        }

        /**
         * EdgeSwipe 사용 여부
         * @param isEnabled
         */
        public void setEnabledEdgeSwipe(boolean isEnabled) {
            this.isEnabled = isEnabled;
        }


        /**
         * 이벤트 intercept
         * @param event
         * @return
         */
        public boolean onInterceptTouchEvent(@NonNull MotionEvent event) {
            if(!isEnabled) {
                return true;
            }


            int fragmentStackCount = mActivity.getSupportFragmentManager().getBackStackEntryCount();
            if(mActivity instanceof MainActivity &&  fragmentStackCount == 0) {
                return true;
            }
            // GestureDetector 이벤트 전달.
            mDetector.onTouchEvent(event);

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if(event.getX() < SWIPE_START_MAX_DX && event.getY() > SWIPE_START_MIN_DY){
                        isAvailableEdgeSwipe = true;
                        return false;
                    }
                    break;

                case MotionEvent.ACTION_MOVE:
                    if(!isAvailableEdgeSwipe) {
                        return true;
                    }

                    if(fragmentStackCount > 0) {
                        Fragment fragment = getFragment(fragmentStackCount - 1);
                        fragment.getView().setTranslationX(event.getX());

                    } else {
                        mActivity.getWindow().getDecorView().setTranslationX(event.getX());
                    }
                    return false;

                case MotionEvent.ACTION_UP:
                    if(!isExecuteEdgeSwipe) {
                        if(fragmentStackCount > 0) {
                            Fragment fragment = getFragment(fragmentStackCount - 1);
                            fragment.getView().setTranslationX(0);

                        } else {
                            mActivity.getWindow().getDecorView().setTranslationX(0);
                        }
                    }
                    isExecuteEdgeSwipe = false;
                    isAvailableEdgeSwipe = false;
                    break;

            }
            return true;
        }


        /**
         * 해당 Activity의 Fragment를 가져온다.
         *
         * @param position
         * @return
         */
        private Fragment getFragment(int position) {
            FragmentManager.BackStackEntry backStackEntry = mActivity.getSupportFragmentManager().getBackStackEntryAt(position);
            String tag = backStackEntry.getName();

            return mActivity.getSupportFragmentManager().findFragmentByTag(tag);
        }


        /**
         * EdgeSwipe 수행
         */
        private void executeSwipeBack() {
            isExecuteEdgeSwipe = true;

            // Fragment stack이 존재 할 경우 Fragment pop
            if (mActivity.getSupportFragmentManager().getBackStackEntryCount() > 0) {
                mActivity.getSupportFragmentManager().popBackStack();
                return;
            }

            // 그 이외의 경우 activity pop
            int activityCount = getActivityStackCount();
            if(activityCount > 1) {
                try {
                    BaseActivity topActivity = getTopActivityFromStack();
                    if(topActivity != null) {
                        topActivity.finishActivityWithSlideAnimation();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


        private GestureDetector.OnGestureListener mGestureListener = new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                float AVAILABLE_SWIPE_DISTANCE = mActivity.getWindow().getDecorView().getWidth() * SWIPE_AVAILABLE_VELOCITY;

                if(e1.getX() < SWIPE_START_MAX_DX && e1.getY() > SWIPE_START_MIN_DY){
                    if(e2.getX() - e1.getX() > AVAILABLE_SWIPE_DISTANCE) {
                        executeSwipeBack();
                        return true;
                    }
                }
                return false;
            }
        };
    }
}
