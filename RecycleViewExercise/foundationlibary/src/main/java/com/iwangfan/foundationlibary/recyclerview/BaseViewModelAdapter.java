package com.iwangfan.foundationlibary.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.HashMap;

/**
 * Author : wangxp
 * Date : 2017/2/16
 * Desc :Base ViewModel Adapter for AbsListView to handle M-V-Vm struck logics
 */

public class BaseViewModelAdapter<T> extends BaseListRecyclerAdapter<T> {
    private HashMap<Integer, IViewModel<T>> mViewModels;
    private HashMap<Integer, String> mViewModelsConfig;

    public BaseViewModelAdapter() {
        mViewModels = new HashMap<Integer, IViewModel<T>>();
        mViewModelsConfig = new HashMap<Integer, String>();
    }

    public void addViewModelClass(int index, Class<? extends IViewModel<T>> cls) {
        if (cls == null) return;
        mViewModelsConfig.put(index, cls.getName());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        IViewModel<T> viewModel = getViewModelByIndex(viewType);
        if (null == viewModel){
            return null;
        } else {
            return viewModel.onCreateViewHolder(parent,viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final int type = getItemViewType(position);
        IViewModel<T> viewModel = getViewModelByIndex(type);
        T model = getList().get(position);
        if (null != viewModel) viewModel.onBindViewHolder(model,holder,position);
    }


    protected IViewModel<T> getViewModelByIndex(int index) {
        IViewModel<T> viewModel = mViewModels.get(index);
        if (viewModel == null) {
            try {
                final String className = mViewModelsConfig.get(index);
                Class<?> cls = Class.forName(className);
                Object obj = cls.newInstance();
                if (obj instanceof IViewModel) {
                    viewModel = (IViewModel<T>) obj;
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
}
