
package com.zhanglin.practice;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.os.Build.VERSION;
import android.util.Log;

public class DataBase extends SQLiteOpenHelper
{
	private String TAG="test";
	private static final int VERSION=1;
	public DataBase(Context context,String name,CursorFactory factory,int version)
	{
		super(context,name,factory,version);
		// TODO Auto-generated constructor stub
		Log.i(TAG,"DataBase is going");
	}
	public DataBase(Context context,String name)
	{
		this(context,name,VERSION);
	}
	public DataBase(Context context,String name,int version)
	{
		this(context,name,null,VERSION);
	}
	@Override
	public void onCreate(SQLiteDatabase db)
	{
		// TODO Auto-generated method stub
		Log.i(TAG,"onCreat is going");
		db.execSQL("create table music(path char(100),title char(100),artist char(100),album char(100),duration long)");
	}

	@Override
	public void onOpen(SQLiteDatabase db)
	{
		// TODO Auto-generated method stub
		Log.i(TAG,"onOpen is going");
		super.onOpen(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0,int arg1,int arg2)
	{
		// TODO Auto-generated method stub
		Log.i(TAG,"onUpgrade is going");
	}

}
