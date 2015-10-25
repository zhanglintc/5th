
package com.zhanglin.practice;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.widget.AdapterView.OnItemClickListener;

public class SongsActivity extends Activity
{
	private ListView mListView;
	private MyAdapter mMyAdapter;
	
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
		setContentView(R.layout.songs);
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
		mListView=(ListView)findViewById(R.id.listview_songs);
		mListView.setAdapter(mMyAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> arg0,View arg1,int position,long id)
			{
				// TODO Auto-generated method stub
				for(int i=0;i<c.getCount();i++)
				{
					PublicList.list(_path[i],_titles[i],_artists[i],_album[i],_duration[i],i);
				}
				if(PublicList.currentItem==position&&PlayerServices.myMediaPlayer.isPlaying()==true)
				{
					Intent intent=new Intent(SongsActivity.this,PlayingActivity.class);
					startActivity(intent);
				}
				else 
				{
					PublicList.currentItem=position;
					PlayerServices.playMusic(PublicList.path[PublicList.currentItem]);
					Intent intent=new Intent(SongsActivity.this,PlayingActivity.class);
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
			TextView artsitTextView=(TextView)convertView.findViewById(R.id.musicitem_textView2);
			ImageView imageView=(ImageView)convertView.findViewById(R.id.musicitem_imageView);
			TextView duration=(TextView)convertView.findViewById(R.id.musicitem_duration);
			mTextView.setText(_titles[position]);
			artsitTextView.setText(_artists[position]);
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
		c=this.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,new String[]
		{
				MediaStore.Audio.Media.TITLE,
				MediaStore.Audio.Media.DURATION,
				MediaStore.Audio.Media.ARTIST,
				MediaStore.Audio.Media._ID,
				MediaStore.Audio.Media.DISPLAY_NAME,
				MediaStore.Audio.Media.DATA,
				MediaStore.Audio.Media.ALBUM
		},
		null,null,null);
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
		_duration=new long[c.getCount()];
		_album=new String[c.getCount()];
		for(int i=0;i<c.getCount();i++)
		{
			_duration[i]=c.getLong(1);
			_ids[i]=c.getInt(3);
			_titles[i]=c.getString(0);
			_artists[i]=c.getString(2);
			_path[i]=c.getString(5).substring(4);
			_album[i]=c.getString(6);
			c.moveToNext();
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// TODO Auto-generated method stub
		menu.add(0,1,Menu.NONE,"收藏列表");
		menu.add(0,2,Menu.NONE,"正在播放");
		menu.add(0,3,Menu.NONE,"关于");
		menu.add(0,4,Menu.NONE,"返回");
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// TODO Auto-generated method stub
		switch(item.getItemId())
		{
		case 1:
			Intent intent=new Intent(SongsActivity.this,PlayListActivity.class);
			startActivity(intent);
			break;
		case 2:
			Intent playing=new Intent();
			playing.setClass(SongsActivity.this,PlayingActivity.class);
			startActivity(playing);
			break;
		case 3:
			Intent about=new Intent(SongsActivity.this,About.class);
			startActivity(about);
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