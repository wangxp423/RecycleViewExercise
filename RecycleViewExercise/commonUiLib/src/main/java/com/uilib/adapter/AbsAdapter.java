package com.uilib.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.widget.BaseAdapter;

public abstract class AbsAdapter<T> extends BaseAdapter {
	
	private final static int DEFUALT_COLUMN_COUNT = 1;
	private int mColumnOfRow;
	private Context mContext;
	private List<T> mDataSet;
	
	public AbsAdapter(Context context) {
		this(context, DEFUALT_COLUMN_COUNT);
	}
	
	public AbsAdapter(Context context,int columnOfRow){
		if(columnOfRow < DEFUALT_COLUMN_COUNT) throw new IllegalArgumentException("column of row must be above zero !");
		mContext = context;
		setCoulmnOfRow(columnOfRow);
		mDataSet = new ArrayList<T>(1);
	}
	
	public Context getContext(){
		return mContext;
	}
	
	public List<T> getDataSet(){
		return mDataSet;
	}
	
	public int getCoulmnOfRow(){
		return mColumnOfRow;
	}
	
	public void setCoulmnOfRow(int coulmn){
		mColumnOfRow = coulmn;
		notifyDataSetChanged();
	}
	
	public void clear(){
		mDataSet.clear();
		notifyDataSetChanged();
	}
	
	public void remove(T data){
		mDataSet.remove(data);
		notifyDataSetChanged();
	}
	
	public void remove(int position){
		mDataSet.remove(position);
		notifyDataSetChanged();
	}
	
	public void updateDataSet(List<T> dataSet){
		setDataSet(dataSet,true);
		notifyDataSetChanged();
	}
	
	public void addDataSet(List<T> dataSet){
		if(dataSet != null){
			setDataSet(dataSet, false);
			notifyDataSetChanged();
		}
	}
	
	public void setDataSet(List<T> dataSet){
		setDataSet(dataSet, true);
	}
	
	private void setDataSet(List<T> dataSet,boolean clean){
		if(clean) mDataSet.clear();
		if(dataSet != null) mDataSet.addAll(dataSet);
	}
	
	@Override
	public int getCount() {
		int totalCount = getRawCount();
		int count = totalCount/mColumnOfRow;
		if(totalCount % mColumnOfRow != 0) count ++;
		return count;
	}

	@Override
	public final T getItem(int position) {
		return getItem(position, 0);
	}

	public final T getItem(int row,int coumn){
		int index = mColumnOfRow * row + coumn;
		if(index>=getRawCount() || index<0) return null;
		else return mDataSet.get(index);
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public boolean isEmpty() {
		return (mDataSet==null || mDataSet.isEmpty());
	}
	
	private int getRawCount(){
		return (mDataSet==null ? 0 : mDataSet.size());
	}
}
