package com.iwangfan.foundationlibary.recyclerview;

import android.support.v4.util.SparseArrayCompat;

/**
 * @param <T>
 */
public class ItemViewManager<T> {
    /**
     * SparseArrayCompat()其实是一个map容器,它使用了一套算法优化了hashMap,可以节省至少50%的缓存.
     * 缺点但是有局限性只针对下面类型.
     * key: Integer; value: object
     */
    SparseArrayCompat<ItemView<T>> delegates = new SparseArrayCompat();

    public int getItemViewDelegateCount() {
        return delegates.size();
    }

    /**
     * 添加一个view类型
     *
     * @param delegate
     * @return
     */
    public ItemViewManager<T> addDelegate(ItemView<T> delegate) {
        int viewType = delegates.size();
        if (delegate != null) {
            delegates.put(viewType, delegate);
            viewType++;
        }
        return this;
    }

    public ItemViewManager<T> addDelegate(int viewType, ItemView<T> delegate) {
        if (delegates.get(viewType) != null) {
            throw new IllegalArgumentException(
                    "An ItemView is already registered for the viewType = "
                            + viewType
                            + ". Already registered ItemView is "
                            + delegates.get(viewType));
        }
        delegates.put(viewType, delegate);
        return this;
    }

    /**
     * 删除一个类型
     *
     * @param delegate
     * @return
     */
    public ItemViewManager<T> removeDelegate(ItemView<T> delegate) {
        if (delegate == null) {
            throw new NullPointerException("ItemView is null");
        }
        int indexToRemove = delegates.indexOfValue(delegate);

        if (indexToRemove >= 0) {
            delegates.removeAt(indexToRemove);
        }
        return this;
    }

    /**
     * 根据type删除
     *
     * @param itemType
     * @return
     */
    public ItemViewManager<T> removeDelegate(int itemType) {
        int indexToRemove = delegates.indexOfKey(itemType);

        if (indexToRemove >= 0) {
            delegates.removeAt(indexToRemove);
        }
        return this;
    }

    public int getItemViewType(T item, int position) {
        int delegatesCount = delegates.size();
        for (int i = delegatesCount - 1; i >= 0; i--) {
            ItemView<T> delegate = delegates.valueAt(i);
            if (delegate.isForViewType(item, position)) {
                return delegates.keyAt(i);
            }
        }
        throw new IllegalArgumentException(
                "No ItemView added that matches position=" + position + " in data source");
    }

    public void convert(ViewHolder holder, T item, int position) {
        int delegatesCount = delegates.size();
        for (int i = 0; i < delegatesCount; i++) {
            ItemView<T> delegate = delegates.valueAt(i);

            if (delegate.isForViewType(item, position)) {
                delegate.convert(holder, item, position);
                return;
            }
        }
        throw new IllegalArgumentException(
                "No ItemViewManager added that matches position=" + position + " in data source");
    }


    public ItemView getItemViewDelegate(int viewType) {
        return delegates.get(viewType);
    }

    public int getItemViewLayoutId(int viewType) {
        return getItemViewDelegate(viewType).getItemViewLayoutId();
    }

    public int getItemViewType(ItemView itemView) {
        return delegates.indexOfValue(itemView);
    }
}
