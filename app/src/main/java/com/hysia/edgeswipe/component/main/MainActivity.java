package com.hysia.edgeswipe.component.main;

import android.os.Bundle;

import com.hysia.edgeswipe.R;
import com.hysia.edgeswipe.common.base.BaseActivity;


/**
 * 메인 화면
 */
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initMainFragment();
    }

    private void initMainFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment_container, new MainFragment())
                .commit();
    }
}
