package com.wangxp.exercise.recycleview.activity;

import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iwangfan.foundationlibary.utils.ToastUtils;
import com.jcodecraeer.xrecyclerview.BaseRefreshHeader;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.wangxp.exercise.recycleview.BaseNormalActivity;
import com.wangxp.exercise.recycleview.R;
import com.wangxp.exercise.recycleview.listener.NormalHeaderCallBack;
import com.wangxp.exercise.recycleview.widgetdivider.ItemDivider;

/**
 * @类描述：xRecyclerView 下拉刷新样式
 * @创建人：Wangxiaopan
 * @创建时间：2017/3/13 0013 14:15
 * @修改人：
 * @修改时间：2017/3/13 0013 14:15
 * @修改备注：
 */

public class PullRecyclerViewActivity extends BaseNormalActivity{
    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_xrecycler;
    }

    @Override
    protected void initView() {
        final XRecyclerView re = (XRecyclerView) findViewById(R.id.re);
        re.setLayoutManager(new LinearLayoutManager(this));
        re.addItemDecoration(new ItemDivider().setDividerWith(2).setDividerColor(Color.MAGENTA));
        re.setAdapter(new MyAdapter());
        re.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                re.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        re.onHeaderRefreshComplete(BaseRefreshHeader.PULL_STATE_FINISH);
                    }
                },2000);
            }

            @Override
            public void onLoadMore() {
                re.loadMoreComplete();
            }
        });
        NormalHeaderCallBack callBack = new NormalHeaderCallBack(this);
        callBack.setmTipText("航美引擎提供，更新10条数据");
        re.setHeaderViewCallBack(callBack);
    }

    @Override
    protected void initData() {

    }

    private class MyAdapter extends RecyclerView.Adapter{

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new RecyclerView.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1,parent,false)) {
            };
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            TextView tv = (TextView) holder.itemView.findViewById(android.R.id.text1);
            tv.setText(String.valueOf(position));
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtils.showShortToast("item = " + position);
                }
            });
//            holder.itemView.setBackgroundColor(Color.BLUE);
        }

        @Override
        public int getItemCount() {
            return 100;
        }
    }
}
