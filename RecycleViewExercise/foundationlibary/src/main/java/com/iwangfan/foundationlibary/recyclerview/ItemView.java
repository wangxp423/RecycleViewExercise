package com.iwangfan.foundationlibary.recyclerview;


/**
 * 判断是否显示可用此接口
 * @param <T>
 */
public interface ItemView<T>
{

    int getItemViewLayoutId();

    /**
     * 是否是此类型的view
     * @param item
     * @param position
     * @return
     */
    boolean isForViewType(T item, int position);

    void convert(ViewHolder holder, T t, int position);

}
