package com.zhanglin.practice;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;

public class alb extends Activity
{
	private Cursor c;
	private int[] _ids;
	private String[] _titles;
	private String[] _path; // 音乐文件的路径
	private String[] _artists;
	private String[] _album;
	private long[] _duration;
	private ListView listview;
	private int pos;
	private String albumName;
	private MusicListAdapter adapter;
	/* 上下文菜单项 */
	private static final int PLAY_ITEM=Menu.FIRST;
	private static final int DELETE_ITEM=Menu.FIRST+1;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		Log.i("test","AlbumActivity");
		super.onCreate(savedInstanceState);
		Intent intent=this.getIntent();
		albumName=intent.getExtras().getString("albums");
		listview=new ListView(this);
	
		listview.setOnItemClickListener(new ListItemClickListener());
		listview.setOnCreateContextMenuListener(new ContextMenuListener());
		LinearLayout list=new LinearLayout(this);
		list.setBackgroundResource(R.drawable.background);
		LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
		list.addView(listview,params);
		setContentView(list);
	}

	@Override
	protected void onStart()
	{
		// TODO Auto-generated method stub
		super.onStart();
		setListData();
	}

	/* 播放选中的音乐 */
	private void playMusic(int position)
	{
		Intent intent=new Intent(alb.this,PlayingActivity.class);
		intent.putExtra("_ids",_ids);
		intent.putExtra("_titles",_titles);
		intent.putExtra("position",position);
		startActivity(intent);
		finish();
	}

	/* 从列表中删除选中的音乐 */
	private void deleteMusic(int position)
	{
		this.getContentResolver().delete(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,MediaStore.Audio.Media._ID+"="+_ids[position],null);
	}

	/* 从sdcard中删除选中的音乐 */
	private void deleteMusicFile(int position)
	{
		File file=new File(_path[pos]);
		file.delete();
	}

	class ListItemClickListener implements OnItemClickListener
	{

		@Override
		public void onItemClick(AdapterView<?> arg0,View view,int position,long id)
		{
			// TODO Auto-generated method stub
			//playMusic(position);
			for(int i=0;i<c.getCount();i++)
			{
				PublicList.list(_path[i],_titles[i],_artists[i],_album[i],_duration[i],i);
			}
			if(PublicList.currentItem==position&&PlayerServices.myMediaPlayer.isPlaying()==true)
			{
				Intent intent=new Intent(alb.this,PlayingActivity.class);
				startActivity(intent);
			}
			else 
			{
				PublicList.currentItem=position;
				PlayerServices.playMusic(PublicList.path[PublicList.currentItem]);
				Intent intent=new Intent(alb.this,PlayingActivity.class);
				startActivity(intent);
			}	
		}

	}

	/* 创建上下文菜单监听器 */
	class ContextMenuListener implements OnCreateContextMenuListener
	{
		@Override
		public void onCreateContextMenu(ContextMenu menu,View view,ContextMenuInfo info)
		{
			menu.setHeaderTitle("操作");
			menu.add(0,PLAY_ITEM,0,"播放");
			menu.add(0,DELETE_ITEM,0,"删除");
			final AdapterView.AdapterContextMenuInfo menuInfo=(AdapterView.AdapterContextMenuInfo)info;
			pos=menuInfo.position;
		}
	}

	/* 上下文菜单的某一项被点击时回调该方法 */
	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
		case PLAY_ITEM: // 开始播放
			playMusic(pos);
			break;

		case DELETE_ITEM: // 删除一首歌曲
			AlertDialog.Builder builder=new AlertDialog.Builder(this);
			builder.setMessage("真的要删除这首歌曲吗").setPositiveButton("是",new DialogInterface.OnClickListener()
			{

				@Override
				public void onClick(DialogInterface dialog,int which)
				{
					deleteMusic(pos); // 从列表中删除音乐
					deleteMusicFile(pos); // 从sdcard中删除音乐
					setListData(); // 从新获得列表中药显示的数据
					adapter.notifyDataSetChanged(); // 更新列表UI
				}
			}).setNegativeButton("否",null);
			AlertDialog ad=builder.create();
			ad.show();
			break;
		}
		return true;
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
				MediaStore.Audio.Media.ALBUM+"='"+albumName+"'",null,null);
		c.moveToFirst();
		_ids=new int[c.getCount()];
		_titles=new String[c.getCount()];
		_path=new String[c.getCount()];
		_album=new String[c.getCount()];
		_duration=new long[c.getCount()];
		_artists=new String[c.getCount()];
		for(int i=0;i<c.getCount();i++)
		{
			_ids[i]=c.getInt(3);
			_titles[i]=c.getString(0);
			_path[i]=c.getString(5);
			_album[i]=c.getString(6);
			_duration[i]=c.getLong(1);
			_artists[i]=c.getString(2);
			c.moveToNext();
		}
		adapter=new MusicListAdapter(this,c);
		listview.setAdapter(adapter);
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
			//Toast.makeText(MainActivity.this,"播放列表",Toast.LENGTH_LONG).show();
			Intent intent1=new Intent(alb.this,PlayListActivity.class);
			startActivity(intent1);
			break;
		case 2:
			Intent playing=new Intent();
			playing.setClass(alb.this,PlayingActivity.class);
			startActivity(playing);
			break;
		case 3:
			//Toast.makeText(MainActivity.this,"关于",Toast.LENGTH_LONG).show();
			Intent intent2=new Intent(alb.this,About.class);
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
