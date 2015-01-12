package com.zhanglin.practice;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class PlayListActivity extends Activity
{
	private ListView mListView;
	private MyAdapter mMyAdapter;
	
	private String[] _titles;
	private String[] _path; // 音乐文件的路径
	private String[] _artists;
	private String[] _album;
	private long[] _duration;
	private Cursor c;
	private AlertDialog ad=null;
	private AlertDialog.Builder builder=null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.playlist);
	}
	@Override
	protected void onStart()
	{
		// TODO Auto-generated method stub
		super.onStart();
		setListData();
		setupViews();
	}

	public void setupViews()
	{
		mMyAdapter=new MyAdapter();
		mListView=(ListView)findViewById(R.id.listview_playlist);
		mListView.setAdapter(mMyAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> arg0,View arg1,int arg2,long arg3)
			{
				// TODO Auto-generated method stub
				for(int i=0;i<c.getCount();i++)
				{
					PublicList.list(_path[i],_titles[i],_artists[i],_album[i],_duration[i],i);
				}
				if(PublicList.currentItem==(int)arg3&&PlayerServices.myMediaPlayer.isPlaying()==true)
				{
					Intent intent=new Intent(PlayListActivity.this,PlayingActivity.class);
					startActivity(intent);
				}
				else 
				{
					PublicList.currentItem=(int)arg3;
					PlayerServices.playMusic(PublicList.path[PublicList.currentItem]);
					Intent intent=new Intent(PlayListActivity.this,PlayingActivity.class);
					startActivity(intent);
				}	
			}
		});
	}
	// 定义自己的适配器,注意getCount和getView方法
	private class MyAdapter extends BaseAdapter
	{
		@Override
		public int getCount()
		{
			return c.getCount();
		}

		@Override
		public Object getItem(int arg0)
		{
			return arg0;
		}

		@Override
		public long getItemId(int position)
		{
			return position;
		}

		@Override
		public View getView(int position,View convertView,ViewGroup parent)
		{
			convertView=LayoutInflater.from(getApplicationContext()).inflate(R.layout.musicitem,null);

			TextView mTextView=(TextView)convertView.findViewById(R.id.musicitem_textView);
			ImageView imageView=(ImageView)convertView.findViewById(R.id.musicitem_imageView);
			TextView duration=(TextView)convertView.findViewById(R.id.musicitem_duration);
			TextView artTextView=(TextView)convertView.findViewById(R.id.musicitem_textView2);
			artTextView.setText(_artists[position]);
			mTextView.setText(_titles[position]);
			formatTime totime=new formatTime();
			duration.setText(totime.formatTime(_duration[position]));
			if(PlayerServices.myMediaPlayer.isPlaying()==true)
			{
				if(_titles[position].equalsIgnoreCase(PublicList.title[PublicList.currentItem]))
				{
					imageView.setImageResource(R.drawable.isplaying);
				}
			}
			mTextView.setTextColor(Color.WHITE);
			return convertView;
		}

	}
	private void setListData()
	{
		DataBase dataBase=new DataBase(PlayListActivity.this,"Player_db");
		SQLiteDatabase db=dataBase.getWritableDatabase();
		c=db.query("music",null,null,null,null,null,null);
		if(c==null||c.getCount()==0)
		{
			builder=new AlertDialog.Builder(this);
			builder.setMessage("存储列表为空...").setPositiveButton("确定",null);
			ad=builder.create();
			ad.show();
		}
		c.moveToFirst();
		_titles=new String[c.getCount()];
		_artists=new String[c.getCount()];
		_path=new String[c.getCount()];
		_duration=new long[c.getCount()];
		_album=new String[c.getCount()];
		for(int i=0;i<c.getCount();i++)
		{
			_path[i]=c.getString(c.getColumnIndex("path"));
			_titles[i]=c.getString(c.getColumnIndex("title"));
			_artists[i]=c.getString(c.getColumnIndex("artist"));
			_album[i]=c.getString(c.getColumnIndex("album"));
			_duration[i]=c.getLong(c.getColumnIndex("duration"));
			c.moveToNext();
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// TODO Auto-generated method stub
		// mainMenu=menu;
		menu.add(0,1,Menu.NONE,"收藏列表");
		menu.add(0,2,Menu.NONE,"正在播放");
		menu.add(0,3,Menu.NONE,"关于");
		menu.add(0,4,Menu.NONE,"返回");
		// return super.onCreateOptionsMenu(menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// TODO Auto-generated method stub
		switch(item.getItemId())
		{
		case 1:
			Toast.makeText(PlayListActivity.this,"当前已经是播放列表",Toast.LENGTH_LONG).show();
			break;
		case 2:
			Intent playing=new Intent();
			playing.setClass(PlayListActivity.this,PlayingActivity.class);
			startActivity(playing);
			break;
		case 3:
			Intent aboutIntent=new Intent(PlayListActivity.this,About.class);
			startActivity(aboutIntent);
			break;
		case 4:
			finish();
			break;
		default:
			break;
		}
		return true;
	}
}
