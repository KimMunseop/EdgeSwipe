package com.hysia.edgeswipe.component.detail;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.hysia.edgeswipe.R;
import com.hysia.edgeswipe.common.base.BaseFragment;

import java.util.Random;

/**
 * 상세화면 Fragment
 * Created by hysia on 2017. 7. 17..
 */

public class DetailFragment extends BaseFragment {
    private Button newFragmentButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        view.setBackgroundColor(color);

        newFragmentButton = (Button) view.findViewById(R.id.detail_new_fragment_button);
        newFragmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFragmentStackWithAnimation(R.id.detail_fragment_container, new DetailFragment());
            }
        });
    }
}
