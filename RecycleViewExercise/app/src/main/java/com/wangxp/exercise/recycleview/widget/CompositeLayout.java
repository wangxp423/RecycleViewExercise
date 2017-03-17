package com.wangxp.exercise.recycleview.widget;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.wangxp.exercise.recycleview.R;

/**
 * Created by Wangxp on 2017/3/2  复合RecyclerView和其他View
 */

public class CompositeLayout extends LinearLayout{
    private View headerView,mainView;
    public CompositeLayout(Context context) {
        super(context);
        init();
    }

    public CompositeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CompositeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        setOrientation(VERTICAL);
        initHeaderView();
        initRecyclerView();
    }

    private void initHeaderView(){
        headerView = View.inflate(getContext(), R.layout.item_title_category,null);
        addView(headerView);
    }

    private void initRecyclerView(){
        mainView = View.inflate(getContext(), R.layout.include_recyclerview,null);
        addView(mainView);
    }

    public View getHeaderView(){
        return headerView;
    }
    public View getMainView(){
        return mainView;
    }

}
