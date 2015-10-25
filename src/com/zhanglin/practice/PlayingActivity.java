
package com.zhanglin.practice;

import java.io.IOException;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils.TruncateAt;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class PlayingActivity extends Activity
{
	
	static String completion="false";
	static String gesture="false";
	static String currentname;//歌曲名,title
	static SeekBar seekbar;
	Handler handler;
	Button like;
	ImageButton last,stop,start,next;
	TextView showingname,time,fulltime,play_artist,play_album;	
	private int speed;//2 back , 3 forward
	int isfirst=0;	
	private GestureDetector gestureDetector;
	formatTime formatTime=new formatTime();
	String playlistFlag="nothas";
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		
		//removeNotify();
		isfirst=1;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.playing);
		
		seekbar=(SeekBar)findViewById(R.id.seekbar);
		last=(ImageButton)findViewById(R.id.last);
		stop=(ImageButton)findViewById(R.id.stop);
		start=(ImageButton)findViewById(R.id.start);
		next=(ImageButton)findViewById(R.id.next);
		like=(Button)findViewById(R.id.like);
		showingname=(TextView)findViewById(R.id.songname);
		time=(TextView)findViewById(R.id.time);
		fulltime=(TextView)findViewById(R.id.fulltime);
		play_artist=(TextView)findViewById(R.id.play_artist);
		play_album=(TextView)findViewById(R.id.play_album);
		
		gestureDetector=new GestureDetector(new ChangeGestureDetector(this)); // 手势识别

		showingname.setText(PublicList.title[PublicList.currentItem]);

		if(PublicList.artist[PublicList.currentItem].length()>16)
			play_artist.setText(PublicList.artist[PublicList.currentItem].substring(0,15));
		else 
			play_artist.setText(PublicList.artist[PublicList.currentItem]);
		if(PublicList.album[PublicList.currentItem].length()>16)
			play_album.setText(PublicList.album[PublicList.currentItem].substring(0,15));
		else
			play_album.setText(PublicList.album[PublicList.currentItem]);
		
		seekbar.setMax(PlayerServices.myMediaPlayer.getDuration());
		final DelayThread delaythread=new DelayThread(1000);
		delaythread.start();
		
		time.setText(formatTime.formatTime(PlayerServices.myMediaPlayer.getCurrentPosition()));
		fulltime.setText(formatTime.formatTime(PlayerServices.myMediaPlayer.getDuration()));
		
		CheckLikeButton();
		
		@SuppressWarnings("unused")
		final OnTouchListener TouchLight=new OnTouchListener()// 更改button亮度的模板
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
		/*************************** 按钮的各种操作开始开始 ***********************************/
		//last.setOnTouchListener(TouchDark);
		//last.setOnTouchListener(TouchLight);
		stop.setOnTouchListener(TouchDark);
		//stop.setOnTouchListener(TouchLight);
		//start.setOnTouchListener(TouchDark);
		//next.setOnTouchListener(TouchDark);
		//next.setOnTouchListener(TouchLight);
		if(PlayerServices.myMediaPlayer.isPlaying()==true)
		{
			start.setImageResource(R.drawable.pause);
		}
		else
		{
			start.setImageResource(R.drawable.start);
		}
		last.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				PlayerServices.lastMusic();
				CheckLikeButton();
			}
		});
		last.setOnLongClickListener(new OnLongClickListener()
		{

			@Override
			public boolean onLongClick(View v)
			{
				// TODO Auto-generated method stub
				speed=2;
				return true;
			}
		});
		last.setOnTouchListener(new OnTouchListener()
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
				if(event.getAction()==MotionEvent.ACTION_UP)
				{
					speed=0;
				}
				return false;
			}
		});
		next.setOnLongClickListener(new OnLongClickListener()
		{
			@Override
			public boolean onLongClick(View v)
			{
				// TODO Auto-generated method stub
				speed=3;
				return true;
			}
		});
		next.setOnTouchListener(new OnTouchListener()
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
				// TODO Auto-generated method stub
				if(event.getAction()==MotionEvent.ACTION_UP)
				{
					speed=0;
				}
				return false;
			}
		});
		{
			;
		}
		next.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				PlayerServices.nextMusic();
				CheckLikeButton();
			}
		});

		stop.setOnClickListener(new OnClickListener()// stop按钮操作
				{

					@Override
					public void onClick(View v)
					{
						// TODO Auto-generated method stub

						try
						{
							PlayerServices.myMediaPlayer.stop();
							PlayerServices.myMediaPlayer.reset();
							PlayerServices.myMediaPlayer.setDataSource(PublicList.path[PublicList.currentItem]);
							PlayerServices.myMediaPlayer.prepare();
						}
						catch(IllegalArgumentException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						catch(IllegalStateException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						catch(IOException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
		start.setOnClickListener(new OnClickListener()// star按钮
				{

					@Override
					public void onClick(View v)
					{
						// TODO Auto-generated method stub
						if(PlayerServices.myMediaPlayer.isPlaying())
						{
							PlayerServices.myMediaPlayer.pause();
						}
						else
						{
							try
							{
								PlayerServices.myMediaPlayer.prepare();
								PlayerServices.myMediaPlayer.start();
							}
							catch(IllegalStateException e)
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							catch(IOException e)
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							PlayerServices.myMediaPlayer.start();
						}
					}
				});

		like.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				DataBase dataBase=new DataBase(PlayingActivity.this,"Player_db");
				SQLiteDatabase db=dataBase.getWritableDatabase();
				Cursor cursor=db.query("music",null,null,null,null,null,null);
				while(cursor.moveToNext())
				{
					if(PublicList.title[PublicList.currentItem].equals(cursor.getString(cursor.getColumnIndex("title"))))
					{
						playlistFlag="has";
						
					}	
				}
				if(playlistFlag=="has")
				{
					db.delete("music","title=?",new String[] {PublicList.title[PublicList.currentItem]});
					Toast.makeText(PlayingActivity.this,"取消收藏成功",Toast.LENGTH_SHORT).show();
				}
				else
				{
					ContentValues values=new ContentValues();
					values.put("path",PublicList.path[PublicList.currentItem]);
					values.put("title",PublicList.title[PublicList.currentItem]);
					values.put("artist",PublicList.artist[PublicList.currentItem]);
					values.put("album",PublicList.album[PublicList.currentItem]);
					values.put("duration",PublicList.duration[PublicList.currentItem]);
					db.insert("music",null,values);
					Toast.makeText(PlayingActivity.this,"收藏成功",Toast.LENGTH_SHORT).show();
				}
				playlistFlag="nothas";
				CheckLikeButton();
			}
		});

		seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
		{
			@Override
			public void onProgressChanged(SeekBar arg0,int arg1,boolean arg2)
			{
				// TODO Auto-generated method stub
				delaythread.stop();
			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0)
			{
				// 开始拖动进度条
				// TODO Auto-generated method stub
				delaythread.stop();
			}

			@Override
			public void onStopTrackingTouch(SeekBar arg0)
			{
				// 停止拖动进度条
				// TODO Auto-generated method stub
				PlayerServices.myMediaPlayer.seekTo(seekbar.getProgress());// 将media进度设置为当前seekBar的进度
				PlayerServices.myMediaPlayer.start();
				if(delaythread.isAlive()==false)
				{
					delaythread.start();
				}

			}
		});
		/******************************按钮操作结束 *************************************/
		/*******************************************************************************/
		handler=new Handler()// 动态刷新一些数据
		{
			public void handleMessage(Message msg)
			{
				seekbar.setProgress(PlayerServices.myMediaPlayer.getCurrentPosition());
				isfirst=0;
				showingname.setText(PublicList.title[PublicList.currentItem]);
				showingname.setEllipsize(TruncateAt.MARQUEE);
				showingname.setMarqueeRepeatLimit(10);
				seekbar.setMax(PlayerServices.myMediaPlayer.getDuration());
				if(speed==3)
				{
					PlayerServices.myMediaPlayer.seekTo(PlayerServices.myMediaPlayer.getCurrentPosition()+PlayerServices.myMediaPlayer.getDuration()/20);
				}
				if(speed==2)
				{
					PlayerServices.myMediaPlayer.seekTo(PlayerServices.myMediaPlayer.getCurrentPosition()-PlayerServices.myMediaPlayer.getDuration()/20);
				}
				if(PlayerServices.myMediaPlayer.isPlaying()==true)// 设置start按钮的变化
				{
					start.setImageResource(R.drawable.pause);
				}
				else
				{
					start.setImageResource(R.drawable.start);
				}
				time.setText(formatTime.formatTime(PlayerServices.myMediaPlayer.getCurrentPosition()));
				fulltime.setText(formatTime.formatTime(PlayerServices.myMediaPlayer.getDuration()));
				if(PublicList.artist[PublicList.currentItem].length()>16)
					play_artist.setText(PublicList.artist[PublicList.currentItem].substring(0,15));
				else
					play_artist.setText(PublicList.artist[PublicList.currentItem]);
				if(PublicList.album[PublicList.currentItem].length()>16)
					play_album.setText(PublicList.album[PublicList.currentItem].substring(0,15));
				else
					play_album.setText(PublicList.album[PublicList.currentItem]);
				
				//如果一首歌完成或者手势上下曲，也需要判断当前like按钮的状态
				//last和next也刷新了状态，OnCreat时也刷新了
				//方法很笨拙,很无奈
				if(completion=="true"||gesture=="true")
				{
					CheckLikeButton();
					completion="false";
					gesture="false";
				}
			}
		};
	}// end of onCreat 方法

	@Override
	protected void onStart()
	{
		// TODO Auto-generated method stub
		super.onStart();
		//removeNotify();
	}

	@Override
	protected void onPause()
	{
		// TODO Auto-generated method stub
		super.onPause();
		//addNotify();
	}

	@Override
	protected void onDestroy()
	{
		// TODO Auto-generated method stub
		super.onDestroy();
		//addNotify();
	}

	class DelayThread extends Thread // 读取音乐频率的线程
	{
		int milliseconds;

		public DelayThread(int i)
		{
			milliseconds=i;
		}

		public void run()
		{
			while(true)
			{
				try
				{
					sleep(milliseconds);
					// 设置音乐进度读取频率
				}
				catch(InterruptedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				handler.sendEmptyMessage(0);
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)// Menu产生
	{
		// TODO Auto-generated method stub
		menu.add(0,1,Menu.NONE,"收藏列表");
		menu.add(0,2,Menu.NONE,"播放模式");
		menu.add(0,3,Menu.NONE,"关于");
		menu.add(0,4,Menu.NONE,"返回");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)// 点击Menu的具体操作
	{
		// TODO Auto-generated method stub
		switch(item.getItemId())
		{
		case 1:
			Intent intent1=new Intent(PlayingActivity.this,PlayListActivity.class);
			startActivity(intent1);
			break;
		case 2:
			Intent setIntent=new Intent(PlayingActivity.this,SetModeActivity.class);
			startActivity(setIntent);
			break;
		case 3:
			Intent intent2=new Intent(PlayingActivity.this,About.class);
			startActivity(intent2);
			break;
		case 4:
			finish();
			break;
		default:
			break;
		}
		return true;
	}

	public void next()
	{
		PlayerServices.nextMusic();
	}
	public void last()
	{
		PlayerServices.lastMusic();
	}
	
	@Override //手势操作
	public boolean onTouchEvent(MotionEvent event)
	{
		return gestureDetector.onTouchEvent(event);
	}
	
	public void CheckLikeButton()
	{
		DataBase dataBase=new DataBase(PlayingActivity.this,"Player_db");
		SQLiteDatabase db=dataBase.getWritableDatabase();
		Cursor cursor=db.query("music",null,null,null,null,null,null);
		while(cursor.moveToNext())
		{
			if(PublicList.title[PublicList.currentItem].equals(cursor.getString(cursor.getColumnIndex("title"))))
			{
				playlistFlag="has";				
			}	
		}
		if(playlistFlag=="has")
		{
			like.setText("取消");
			like.setTextColor(Color.RED);
		}
		else
		{
			like.setText("收藏");
			like.setTextColor(Color.WHITE);
		}
		playlistFlag="nothas";
	}
	
	public void addNotify()
	{
		String ns=Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager=(NotificationManager)getSystemService(ns);
		int icon=R.drawable.android;
		CharSequence tickerText="播放器正在后台运行";//PublicList.title[PublicList.currentItem];
		long when=System.currentTimeMillis();
		
		Notification notification=new Notification(icon,tickerText,when);
		Context context=getApplicationContext();
		CharSequence contentTitle="MusicPlayer";
		CharSequence contentText="点击快速进入播放器";//+PublicList.title[PublicList.currentItem];//"打开正在播放曲目";
		Intent notificationIntent=new Intent(this,PlayingActivity.class);
		PendingIntent contentIntent=PendingIntent.getActivity(this,0,notificationIntent,Intent.FLAG_ACTIVITY_NEW_TASK);
		
		notification.setLatestEventInfo(context,contentTitle,contentText,contentIntent);
		notification.defaults|=Notification.DEFAULT_LIGHTS;
		
		mNotificationManager.notify(3,notification);
		
	}
	
	public void  removeNotify()
	{
		String ns=Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager=(NotificationManager)getSystemService(ns);
		mNotificationManager.cancel(3);
	}
	
}
