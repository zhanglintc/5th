
package com.zhanglin.practice;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MusicListAdapter extends BaseAdapter
{

	private Context myCon;
	private Cursor myCur;
	private int pos=-1;
	ImageView imageView;

	public MusicListAdapter(Context con,Cursor cur)
	{
		myCon=con;
		myCur=cur;
	}

	@Override
	public int getCount()
	{
		return myCur.getCount();
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
		convertView=LayoutInflater.from(myCon).inflate(R.layout.musiclist,null);
		myCur.moveToPosition(position);
		TextView tv_music=(TextView)convertView.findViewById(R.id.music1);
		if(myCur.getString(0).length()>15)
		{
			try
			{
				String musicTitle=myCur.getString(0).trim().substring(0,12)+"...";
				tv_music.setText(musicTitle);
			}
			catch(Exception e)
			{

				e.printStackTrace();
			}
		}
		else
		{
			tv_music.setText(myCur.getString(0).trim());
		}
		TextView tv_singer=(TextView)convertView.findViewById(R.id.singer);
		if(myCur.getString(2).equals("<unknown>"))
		{
			tv_singer.setText("未知艺术家");
		}
		else
		{
			tv_singer.setText(myCur.getString(2));
		}
		TextView tv_time=(TextView)convertView.findViewById(R.id.time);
		tv_time.setText(toTime(myCur.getInt(1)));
		ImageView img=(ImageView)convertView.findViewById(R.id.listitem);
		if(position==pos)
		{
			img.setImageResource(R.drawable.isplaying);
		}
		else
		{
			img.setImageResource(R.drawable.item);
		}
		
		if(PlayerServices.myMediaPlayer.isPlaying()==true)
		{
			if(myCur.getString(0).equalsIgnoreCase(PublicList.title[PublicList.currentItem]))
			{
				imageView=(ImageView)convertView.findViewById(R.id.listitem);
				imageView.setImageResource(R.drawable.isplaying);
			}
		}
		//Log.i("CC",myCur.getString(0));
		return convertView;
	}

	public void setItemIcon(int position)
	{
		pos=position;
	}

	/**
	 * 时间格式转换
	 * 
	 * @param time
	 * @return
	 */
	public String toTime(int time)
	{

		time/=1000;
		int minute=time/60;
		int second=time%60;
		minute%=60;
		return String.format("%02d:%02d",minute,second);
	}

}
