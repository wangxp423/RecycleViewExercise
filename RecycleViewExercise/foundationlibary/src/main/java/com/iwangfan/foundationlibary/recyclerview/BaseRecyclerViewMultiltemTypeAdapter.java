package com.iwangfan.foundationlibary.recyclerview;

import android.content.Context;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by wuyulong on 2017/3/3.
 * RecyclerView 多类型Adapter封装
 */

public class BaseRecyclerViewMultiltemTypeAdapter<T> extends BaseListRecyclerAdapter<T> {
    protected Context mContext;
    private final SparseArrayCompat<IRecyclerViewItemTypeModle<T>> mViewModels = new SparseArrayCompat<>();
    private final SparseArrayCompat<String> mViewModelsConfig = new SparseArrayCompat<>();
    protected IModleListener iModleListener;

    public BaseRecyclerViewMultiltemTypeAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        IRecyclerViewItemTypeModle<T> viewModel = getViewModelByIndex(viewType);
        int layoutId = viewModel.getItemViewLayoutId();
        ViewHolder holder = ViewHolder.createViewHolder(mContext, parent, layoutId);
        initViewHolderParam(holder);
        setListener(parent, holder, viewType);
        return holder;
    }

    protected void initViewHolderParam(ViewHolder holder) {

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final int type = getItemViewType(position);
        IRecyclerViewItemTypeModle<T> viewModel = getViewModelByIndex(type);
        T model = getList().get(position);
        if (null != viewModel && holder instanceof ViewHolder) {
            ViewHolder mViewHolder = (ViewHolder) holder;
            viewModel.onBindViewHoler(mViewHolder, model, position);
        }

    }

    public void addViewModelClass(int index, Class<? extends IRecyclerViewItemTypeModle> cls) {
        if (cls == null) return;
        mViewModelsConfig.put(index, cls.getName());
    }

    public void removeViewModelClass(int index) {
        mViewModelsConfig.remove(index);
        notifyDataSetChanged();
    }

    public IRecyclerViewItemTypeModle<T> getViewModelByIndex(int index) {
        IRecyclerViewItemTypeModle<T> viewModel = mViewModels.get(index);
        if (viewModel == null) {
            try {
                final String className = mViewModelsConfig.get(index);
                Class<?> cls = Class.forName(className);
                Object obj = cls.newInstance();
                if (obj instanceof IRecyclerViewItemTypeModle) {
                    viewModel = (IRecyclerViewItemTypeModle<T>) obj;
                    if (iModleListener != null) {
                        iModleListener.OnViewModleListener(viewModel);
                    }
                    mViewModels.put(index, viewModel);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return viewModel;
    }

    /**
     * 是否开启点击事件
     *
     * @param viewType
     * @return
     */
    protected boolean isEnabled(int viewType) {
        return true;
    }

    protected void setListener(final ViewGroup parent, final ViewHolder viewHolder, int viewType) {
        if (!isEnabled(viewType)) return;
        viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    int position = viewHolder.getAdapterPosition();
                    mOnItemClickListener.onItemClick(v, viewHolder, position);
                }
            }
        });

        viewHolder.getConvertView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemClickListener != null) {
                    int position = viewHolder.getAdapterPosition();
                    return mOnItemClickListener.onItemLongClick(v, viewHolder, position);
                }
                return false;
            }
        });
    }

    /**
     * 设置viewmodle 监听器
     *
     * @param iModleListener
     */
    public void setViewModleListener(IModleListener iModleListener) {
        this.iModleListener = iModleListener;

    }
}
