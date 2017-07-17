package com.hysia.edgeswipe.component.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.hysia.edgeswipe.R;
import com.hysia.edgeswipe.common.base.BaseFragment;
import com.hysia.edgeswipe.component.detail.DetailActivity;

/**
 * 메인화면 Fragment
 * Created by hysia on 2017. 7. 17..
 */

public class MainFragment extends BaseFragment {

    private Button newActivityButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        newActivityButton = (Button) view.findViewById(R.id.main_new_activity_button);
        newActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detailIntent = DetailActivity.createIntent(getActivity());
                getActivity().startActivity(detailIntent);
            }
        });
    }
}
