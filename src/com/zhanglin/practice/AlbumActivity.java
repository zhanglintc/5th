package com.zhanglin.practice;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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

public class AlbumActivity extends Activity
{
	private ListView mListView;
	private MyAdapter mMyAdapter;
	private String[] albums;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.album);
		//setListData();
		setupViews();
	}
	public void setupViews()
	{
			Cursor c=this.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,new String[]
			{
					MediaStore.Audio.Media.TITLE,
					MediaStore.Audio.Media.DURATION,
					MediaStore.Audio.Media.ARTIST,
					MediaStore.Audio.Media.ALBUM,
					MediaStore.Audio.Media._ID,
					MediaStore.Audio.Media.DISPLAY_NAME
			},
			null,null,MediaStore.Audio.Media.ALBUM);
			c.moveToFirst();
			int num=c.getCount();
			HashSet<String> set=new HashSet<String>();
			for(int i=0;i<num;i++)
			{
				set.add(c.getString(3));
				c.moveToNext();
			}
			num=set.size();
			Iterator<String> it=set.iterator();
			albums=new String[num];
			int i=0;
			while(it.hasNext())
			{
				albums[i]=it.next().toString();
				i++;
			}
			String album="";
			for(int j=0;j<num;j++)
			{
				if(j<num-1)
				{
					album=album+"'"+albums[j]+"',";
				}
				else
				{
					album=album+"'"+albums[j]+"'";
				}
			}

			Cursor c1=this.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,new String[]
			{
					MediaStore.Audio.Media.TITLE,
					MediaStore.Audio.Media.DURATION,
					MediaStore.Audio.Media.ARTIST,
					MediaStore.Audio.Media.ALBUM,
					MediaStore.Audio.Media._ID,
					MediaStore.Audio.Media.DISPLAY_NAME,
			},
					null,null,MediaStore.Audio.Media.ALBUM);
			c1.moveToFirst();
			HashMap<String,String> map=new HashMap<String,String>();
			int num1=c1.getCount();
			for(int j=0;j<num1;j++)
			{
				map.put(c1.getString(3),c1.getString(2));
				c1.moveToNext();
			}
		mMyAdapter=new MyAdapter(this,albums,map);
		mListView=(ListView)findViewById(R.id.listview_album);
		mListView.setAdapter(mMyAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg0,View view,int position,long id)
			{
				Intent intent=new Intent();
				intent.setClass(AlbumActivity.this,alb.class);
				intent.putExtra("albums",albums[position]);
				startActivity(intent);
			}
		});
		
	}
	private class MyAdapter extends BaseAdapter
	{		
		private Context myCon;
		private String[] albums;
		private HashMap<String,String> myMap;

		public MyAdapter(Context con,String[] str1,HashMap<String,String> map)
		{
			myCon=con;
			albums=str1;
			myMap=map;
		}

		@Override
		public int getCount()
		{
			return albums.length;
		}

		@Override
		public Object getItem(int position)
		{
			return position;
		}

		@Override
		public long getItemId(int position)
		{

			return position;
		}

		@Override
		public View getView(int position,View convertView,ViewGroup parent)
		{
			convertView=LayoutInflater.from(myCon).inflate(R.layout.doublerowitem,null);
			/**
			 * 设置专辑名
			 */
			TextView album=(TextView)convertView.findViewById(R.id.doublerow_textView1);
			if(albums[position].length()>15)
			{
				album.setText(albums[position].substring(0,12)+"...");
			}
			else
			{
				album.setText(albums[position]);
			}
			/**
			 * 设置艺术家姓名
			 */
			TextView artist=(TextView)convertView.findViewById(R.id.doublerow_textView2);
			if(albums[position].equals("sdcard"))
			{
				artist.setText("未知艺术家");
			}
			else
			{
				artist.setText(myMap.get(albums[position]));
			}

			ImageView Albumsitem=(ImageView)convertView.findViewById(R.id.doublerow_image);
			Albumsitem.setImageResource(R.drawable.item);
			return convertView;
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
			Intent intent1=new Intent(AlbumActivity.this,PlayListActivity.class);
			startActivity(intent1);
			break;
		case 2:
			Intent playing=new Intent();
			playing.setClass(AlbumActivity.this,PlayingActivity.class);
			startActivity(playing);
			break;
		case 3:
			Intent intent2=new Intent(AlbumActivity.this,About.class);
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
}

