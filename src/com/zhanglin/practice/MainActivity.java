package com.zhanglin.practice;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;

public class MainActivity extends Activity
{
	/** Called when the activity is first created. */
	private ImageButton artist,album,playlist,songs=null;
	private int[] _ids;
	private String[] _titles;
	private String[] _path; // 音乐文件的路径
	private String[] _artists;
	private String[] _album;
	private long[] _duration;
	private Cursor c;
	private AlertDialog ad=null;
	private AlertDialog.Builder builder=null;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		addNotify();
		ButtonBonder();
		setListData();
		Log.i("test",_path[0]);
		// 创建数据库
		DataBase dataBase=new DataBase(MainActivity.this,"Player_db");
		SQLiteDatabase db=dataBase.getWritableDatabase();

		Intent startserviceIntent=new Intent(MainActivity.this,PlayerServices.class);
		startService(startserviceIntent);

		// 添加歌曲到播放列表
		for(int i=0;i<c.getCount();i++)
		{
			PublicList.list(_path[i],_titles[i],_artists[i],_album[i],_duration[i],i);
		}
		if(PlayerServices.myMediaPlayer.isPlaying()==true)
		{
			Intent intent=new Intent();
			intent.setClass(MainActivity.this,PlayingActivity.class);
			startActivity(intent);
		}
		else
		{
			;
		}

		// 控制按钮按下是的明暗效果
		final OnTouchListener TouchLight=new OnTouchListener()
		{
			public final float[] BT_SELECTED=new float[]
			{1,0,0,0,50,0,1,0,0,50,0,0,1,0,50,0,0,0,1,0};
			public final float[] BT_NOT_SELECTED=new float[]
			{1,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,1,0};

			@Override
			public boolean onTouch(View v,MotionEvent event)
			{
				if(event.getAction()==MotionEvent.ACTION_DOWN)
				{
					v.getBackground().setColorFilter(new ColorMatrixColorFilter(BT_SELECTED));
					v.setBackgroundDrawable(v.getBackground());
				}
				else if(event.getAction()==MotionEvent.ACTION_UP)
				{
					v.getBackground().setColorFilter(new ColorMatrixColorFilter(BT_NOT_SELECTED));
					v.setBackgroundDrawable(v.getBackground());
				}
				return false;
			}
		};
		// 同上
		final OnTouchListener TouchDark=new OnTouchListener()
		{
			public final float[] BT_SELECTED=new float[]
			{1,0,0,0,-50,0,1,0,0,-50,0,0,1,0,-50,0,0,0,1,0};
			public final float[] BT_NOT_SELECTED=new float[]
			{1,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,1,0};

			@Override
			public boolean onTouch(View v,MotionEvent event)
			{
				if(event.getAction()==MotionEvent.ACTION_DOWN)
				{
					v.getBackground().setColorFilter(new ColorMatrixColorFilter(BT_SELECTED));
					v.setBackgroundDrawable(v.getBackground());
				}
				else if(event.getAction()==MotionEvent.ACTION_UP)
				{
					v.getBackground().setColorFilter(new ColorMatrixColorFilter(BT_NOT_SELECTED));
					v.setBackgroundDrawable(v.getBackground());
				}
				return false;
			}
		};
		// 按钮的效果
		artist.setOnTouchListener(TouchLight);
		artist.setOnTouchListener(TouchDark);
		album.setOnTouchListener(TouchLight);
		album.setOnTouchListener(TouchDark);
		playlist.setOnTouchListener(TouchLight);
		playlist.setOnTouchListener(TouchDark);
		songs.setOnTouchListener(TouchLight);
		songs.setOnTouchListener(TouchDark);
		album.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				Intent intent=new Intent(MainActivity.this,AlbumActivity.class);
				startActivity(intent);
			}
		});
		artist.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				Intent artiIntent=new Intent(MainActivity.this,ArtistActivity.class);
				startActivity(artiIntent);
			}
		});
		songs.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				Intent intent=new Intent(MainActivity.this,SongsActivity.class);
				startActivity(intent);
			}
		});
		playlist.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				Intent intent=new Intent();
				intent.setClass(MainActivity.this,PlayListActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode,KeyEvent event)
	{
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK && event.getRepeatCount()==0)
		{
			System.exit(0);
			return true;
		}

		return super.onKeyDown(keyCode,event);
	}

	// 绑定按钮的函数
	public void ButtonBonder()
	{
		artist=(ImageButton)findViewById(R.id.artist);
		album=(ImageButton)findViewById(R.id.album);
		playlist=(ImageButton)findViewById(R.id.playlist);
		songs=(ImageButton)findViewById(R.id.songs);
	}

	// 创建Menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// TODO Auto-generated method stub
		menu.add(0,1,Menu.NONE,"收藏列表");
		menu.add(0,2,Menu.NONE,"正在播放");
		menu.add(0,3,Menu.NONE,"关于");
		menu.add(0,4,Menu.NONE,"退出");
		return true;
	}

	// Menu按键效果
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// TODO Auto-generated method stub
		switch(item.getItemId())
		{
			case 1:
				Intent intent1=new Intent(MainActivity.this,PlayListActivity.class);
				startActivity(intent1);
				break;
			case 2:
				Intent playing=new Intent();
				playing.setClass(MainActivity.this,PlayingActivity.class);
				startActivity(playing);
				break;
			case 3:
				Intent intent2=new Intent(MainActivity.this,About.class);
				startActivity(intent2);
				break;
			case 4:
				Intent close=new Intent();
				close.setClass(MainActivity.this,PlayerServices.class);
				stopService(close);
				removeNotify();
				finish();
				break;
			default:
				break;
		}
		return true;
	}

	private void setListData()
	{
		c=this.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,new String[]
		{MediaStore.Audio.Media.TITLE,MediaStore.Audio.Media.DURATION,MediaStore.Audio.Media.ARTIST,MediaStore.Audio.Media._ID,MediaStore.Audio.Media.DISPLAY_NAME,MediaStore.Audio.Media.DATA,MediaStore.Audio.Media.ALBUM},null,null,null);
		if(c==null||c.getCount()==0)
		{
			builder=new AlertDialog.Builder(this);
			builder.setMessage("存储列表为空...").setPositiveButton("确定",null);
			ad=builder.create();
			ad.show();
		}
		c.moveToFirst();
		_ids=new int[c.getCount()];
		_titles=new String[c.getCount()];
		_artists=new String[c.getCount()];
		_path=new String[c.getCount()];
		_album=new String[c.getCount()];
		_duration=new long[c.getCount()];
		for(int i=0;i<c.getCount();i++)
		{
			_ids[i]=c.getInt(3);
			_titles[i]=c.getString(0);
			_artists[i]=c.getString(2);
			_path[i]=c.getString(5).substring(4);
			_album[i]=c.getString(6);
			_duration[i]=c.getInt(1);
			c.moveToNext();
		}
	}

	public void addNotify()
	{
		String ns=Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager=(NotificationManager)getSystemService(ns);
		int icon=R.drawable.fifth;
		CharSequence tickerText="播放器正在后台运行";// PublicList.title[PublicList.currentItem];
		long when=System.currentTimeMillis();

		Notification notification=new Notification(icon,tickerText,when);
		Context context=getApplicationContext();
		CharSequence contentTitle="Fifth Player";
		CharSequence contentText="点击快速进入播放器";// +PublicList.title[PublicList.currentItem];//"打开正在播放曲目";
		Intent notificationIntent=new Intent(this,MainActivity.class);
		PendingIntent contentIntent=PendingIntent.getActivity(this,0,notificationIntent,Intent.FLAG_ACTIVITY_NEW_TASK);

		notification.setLatestEventInfo(context,contentTitle,contentText,contentIntent);
		notification.defaults|=Notification.DEFAULT_LIGHTS;

		mNotificationManager.notify(623,notification);

	}

	public void removeNotify()
	{
		String ns=Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager=(NotificationManager)getSystemService(ns);
		mNotificationManager.cancel(623);
	}
}
