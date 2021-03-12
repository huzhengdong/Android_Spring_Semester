package com.example.chapter3.homework;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;

import java.util.ArrayList;

public class PlaceholderFragment extends Fragment {
    private ListView lvItems;
    private LottieAnimationView animationView;
    private int mAnimationTime;
    private View view;

    private String[] data = {"抖音", "头条", "字节",  "西瓜", "大力"};
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO ex3-3: 修改 fragment_placeholder，添加 loading 控件和列表视图控件



        view = inflater.inflate(R.layout.fragment_placeholder, container, false);
        ArrayAdapter<String>  adapterItems = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_activated_1, data);
        lvItems = (ListView) view.findViewById(R.id.lvItems);
        lvItems.setAdapter(adapterItems);
        lvItems.setVisibility(View.GONE);

        animationView = (LottieAnimationView) view.findViewById(R.id.animation_view1);
        mAnimationTime = getResources().getInteger(
                android.R.integer.config_shortAnimTime);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getView().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 这里会在 5s 后执行
                // TODO ex3-4：实现动画，将 lottie 控件淡出，列表数据淡入
                lvItems.setAlpha(0f);
                lvItems.setVisibility(View.VISIBLE);
                lvItems.animate().alpha(1f).setDuration(1000)
                        .setListener(null);


                animationView.animate().alpha(0f).setDuration(1000)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                animationView.setVisibility(View.GONE);
                            }
                        });



            }
        }, 5000);
    }
}
