package com.hysia.edgeswipe.common.base;

import android.support.v4.app.Fragment;

import com.hysia.edgeswipe.R;

/**
 * Fragment의 Base Class
 *
 * Created by hysia on 2017. 7. 17..
 */

public class BaseFragment extends Fragment{

    /**
     * 해당 Container에 Fragment를 추가 한다.
     * with Animation
     *
     * @param container
     * @param fragment
     */
    protected void addFragmentStackWithAnimation(int container, Fragment fragment) {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, 0,
                        0, R.anim.slide_out_right)
                .add(container, fragment, fragment.getClass().getName())
                .addToBackStack(fragment.getClass().getName())
                .commit();
    }

    /**
     * 해당 Container에 Fragment를 추가 한다.
     *
     * @param fragment
     */
    protected void addFragmentStack(int container, Fragment fragment) {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .add(container, fragment, fragment.hashCode() + "")
                .addToBackStack(fragment.hashCode() + "")
                .commit();
    }

    /**
     * Back 버튼 클릭
     * @return
     */
    public boolean onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
            return false;
        }

        return true;
    }

}
