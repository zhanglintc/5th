
package com.zhanglin.practice;


import java.util.HashSet;
import java.util.Iterator;

import android.app.Activity;
import android.app.AlertDialog;
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

public class ArtistActivity extends Activity
{
	private ListView mListView;
	private MyAdapter mMyAdapter;
	
	private int[] _ids;
	private String[] _titles;
	private String[] _path; // 音乐文件的路径
	private String[] _artists;
	private long[] _duration;
	private Cursor c;
	private AlertDialog ad=null;
	private AlertDialog.Builder builder=null;
	private String[] artists;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.artist);
		setListData();
		setupViews();
	}
	public void setupViews()
	{
		c.moveToFirst();
		int num=c.getCount();
		HashSet<Object> set=new HashSet<Object>();
		for(int i=0;i<num;i++)
		{
			if(c.getString(2).equals("<unknown>"))
			{
				set.add("未知艺术家");
			}
			else
			{
				set.add(c.getString(2));
			}
			c.moveToNext();
		}
		num=set.size();
		Iterator<Object> it=set.iterator();
		artists=new String[num];
		int i=0;
		while(it.hasNext())
		{
			artists[i]=it.next().toString();
			i++;
		}
		/* 计算每个歌手拥有的歌曲数 */
		int counts[]=new int[num];
		int n=0;
		c.moveToFirst();
		for(int j=0;j<num;j++)
		{
			c.moveToFirst();
			String name=artists[j];//c.getString(2);
			for(int k=n;k<c.getCount();k++)
			{
				if(name.equals(c.getString(2)))
				{
					counts[j]++;					
				}
				c.moveToNext();
			}
		}


		mMyAdapter=new MyAdapter(this, artists, counts);
		mListView=(ListView)findViewById(R.id.listview_artist);
		mListView.setAdapter(mMyAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> arg0,View arg1,int arg2,long arg3)
			{
				
				Intent intent=new Intent();
				intent.setClass(ArtistActivity.this,art.class);
				intent.putExtra("artist",artists[(int)arg3]);
				startActivity(intent);
			}
		});
	}
	private class MyAdapter extends BaseAdapter
	{		
		private Context myCon;
		private String[] artists;
		private int[] counts;
		
		public MyAdapter(Context con,String[] str1,int[] counts)
		{
			myCon=con;
			artists=str1;
			this.counts=counts;
		}
		
		@Override
		public int getCount()
		{
			return artists.length;
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
			convertView=LayoutInflater.from(myCon).inflate(R.layout.doublerowitem,null);

			// 设置艺术家姓名
			TextView artist=(TextView)convertView.findViewById(R.id.doublerow_textView1);
			if(artists[position].length()>15)
				artist.setText(artists[position].substring(0,12)+"...");
			else
				artist.setText(artists[position]);

			// 设置歌手拥有的歌曲数
			TextView musicCounts=(TextView)convertView.findViewById(R.id.doublerow_textView2);
			musicCounts.setText("共有"+counts[position]+"首歌曲");
			// 设置列表项图标
			ImageView Artistsitem=(ImageView)convertView.findViewById(R.id.doublerow_image);
			Artistsitem.setImageResource(R.drawable.item);
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
				MediaStore.Audio.Media.DATA
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
		for(int i=0;i<c.getCount();i++)
		{
			_duration[i]=c.getLong(1);
			_ids[i]=c.getInt(3);
			_titles[i]=c.getString(0);
			_artists[i]=c.getString(2);
			_path[i]=c.getString(5).substring(4);
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
			Intent intent1=new Intent(ArtistActivity.this,PlayListActivity.class);
			startActivity(intent1);
			break;
		case 2:
			Intent playing=new Intent();
			playing.setClass(ArtistActivity.this,PlayingActivity.class);
			startActivity(playing);
			break;
		case 3:
			Intent intent2=new Intent(ArtistActivity.this,About.class);
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
