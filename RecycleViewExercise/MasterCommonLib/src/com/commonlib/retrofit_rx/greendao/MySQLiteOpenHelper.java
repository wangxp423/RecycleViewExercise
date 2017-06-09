package com.commonlib.retrofit_rx.greendao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.commonlib.retrofit_rx.http.cache.DaoMaster;

/**
 * @类描述：SQLiteOpenHelper
 * @创建人：Wangxiaopan
 * @创建时间：2017/6/8 0008 18:29
 * @修改人：
 * @修改时间：2017/6/8 0008 18:29
 * @修改备注：
 */

public class MySQLiteOpenHelper extends DaoMaster.DevOpenHelper {
    public MySQLiteOpenHelper(Context context, String name) {
        super(context, name);
    }

    public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
    }
}
