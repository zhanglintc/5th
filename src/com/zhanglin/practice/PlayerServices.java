
package com.zhanglin.practice;

import java.util.Random;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.IBinder;
import android.util.Log;

public class PlayerServices extends Service
{
	public static MediaPlayer myMediaPlayer=new MediaPlayer();// 播放对象
	private int isfirst=0;
	static Random random=new Random();

	@Override
	public void onCreate()
	{
		super.onCreate();
		isfirst=1;
		try
		{
			myMediaPlayer.reset();
			myMediaPlayer.setDataSource(PublicList.path[PublicList.currentItem]);
			myMediaPlayer.prepare();
		}
		catch(Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
			Log.i("playing","Err");
		}
	}

	@Override
	public void onStart(Intent intent,int startId)
	{
		super.onStart(intent,startId);		
		if(isfirst!=1)
		{
			//playMusic(PublicList.path[PublicList.currentItem]);
		}
		isfirst=0;
	}


	@Override
	public void onDestroy()
	{
		myMediaPlayer.stop();
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent arg0)
	{
		return null;
	}
	
	static void playMusic(String path)
	{
		Log.i("playing","playMusic");
		Log.i("playing",path);
		try
		{
			myMediaPlayer.reset();
			myMediaPlayer.setDataSource(path);
			Log.i("playing","playMusic-->try");
			myMediaPlayer.prepare();
			myMediaPlayer.start();
			myMediaPlayer.setOnCompletionListener(new OnCompletionListener()
			{

				@Override
				public void onCompletion(MediaPlayer mp)
				{
					// TODO Auto-generated method stub
					switch(SetModeActivity.mode)
					{
					case 1:
						if(++PublicList.currentItem>PublicList.size)//if 里面--已经使currentItem变化了
						{
							PublicList.currentItem=0;
						}
						playMusic(PublicList.path[PublicList.currentItem]);
						break;
					case 2:
						playMusic(PublicList.path[PublicList.currentItem]);
						break;
					case 3:
						PublicList.currentItem=random.nextInt(PublicList.size);
						playMusic(PublicList.path[PublicList.currentItem]);
						break;
					default:
						break;
					}					
					//nextMusic();
					PlayingActivity.seekbar.setMax(PlayerServices.myMediaPlayer.getDuration());
					PlayingActivity.completion="true";
					
					
				}
			});
		}
		catch(Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
			Log.i("playing","Err");
		}
	}

	static void lastMusic()
	{
		switch(SetModeActivity.mode)
		{
		case 1:
			if(--PublicList.currentItem<0)//if 里面--已经使currentItem变化了
			{
				PublicList.currentItem=PublicList.size;
			}
			playMusic(PublicList.path[PublicList.currentItem]);
			break;
		case 2:
			playMusic(PublicList.path[PublicList.currentItem]);
			break;
		case 3:
			PublicList.currentItem=random.nextInt(PublicList.size);
			playMusic(PublicList.path[PublicList.currentItem]);
			break;

		default:
			break;
		}
	}

	static void nextMusic()
	{
		switch(SetModeActivity.mode)
		{
		case 1:
			if(++PublicList.currentItem>PublicList.size)
			{
				PublicList.currentItem=0;
			}
			playMusic(PublicList.path[PublicList.currentItem]);
			break;
		case 2:
			playMusic(PublicList.path[PublicList.currentItem]);
			break;
		case 3:
			PublicList.currentItem=random.nextInt(PublicList.size);
			playMusic(PublicList.path[PublicList.currentItem]);
			break;

		default:
			break;
		}
	}
	
	public void addNotify()
	{
		String ns=Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager=(NotificationManager)getSystemService(ns);
		int icon=R.drawable.android;
		CharSequence tickerText=PublicList.title[PublicList.currentItem];
		long when=System.currentTimeMillis();
		
		Notification notification=new Notification(icon,tickerText,when);
		Context context=getApplicationContext();
		CharSequence contentTitle="MusicPlayer";
		CharSequence contentText="正在播放："+PublicList.title[PublicList.currentItem];//"打开正在播放曲目";
		Intent notificationIntent=new Intent(this,PlayingActivity.class);
		PendingIntent contentIntent=PendingIntent.getActivity(this,0,notificationIntent,Intent.FLAG_ACTIVITY_NEW_TASK);
		
		notification.setLatestEventInfo(context,contentTitle,contentText,contentIntent);
		notification.defaults|=Notification.DEFAULT_LIGHTS;
		
		mNotificationManager.notify(3,notification);
		
	}
	
}
