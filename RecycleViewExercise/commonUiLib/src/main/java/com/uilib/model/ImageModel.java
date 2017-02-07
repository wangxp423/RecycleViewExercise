package com.uilib.model;


/**
 * 图片加载单元的数据模型，对于ListView则是一行的图片，可能会有多列，都属于统一个model
 * @author wangwenguan
 *
 */
public class ImageModel {
	
	public static final int MAX_TRY_TIMES	= 3;

	public static final short MASK_STATE		= 0x0F;
	public static final short MASK_ERROR_COUNT	= 0xF0;
	public static final short BIT_OFFSET_ERROR_COUNT	= 4;
	public static final byte STATE_NONE		= 0;
	public static final byte STATE_LOADING	= 1;
	public static final byte STATE_LOADED	= 2;
	public static final byte STATE_ERROR	= 3;
	
	byte[] mStates;
	
	public ImageModel (int itemCount) {
		mStates = new byte[itemCount];
	}

	public void setState (int newstate, int itemindex) {
		mStates[itemindex] = (byte)newstate;
	}
	public byte getState (int itemindex) {
		return mStates[itemindex];
	}
	
	public void recycle () {
		mStates = null;
	}
}
