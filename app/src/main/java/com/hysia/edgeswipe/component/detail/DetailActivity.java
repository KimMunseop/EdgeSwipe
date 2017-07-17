package com.hysia.edgeswipe.component.detail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.hysia.edgeswipe.R;
import com.hysia.edgeswipe.common.base.BaseActivity;

/**
 * 상세 화면 Activity
 * Created by hysia on 2017. 7. 17..
 */

public class DetailActivity extends BaseActivity {

    public static Intent createIntent(Activity activity) {
        Intent intent = new Intent(activity, DetailActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);

        initMainFragment();
    }

    private void initMainFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.detail_fragment_container, new DetailFragment())
                .commit();
    }
}
