package com.zhanglin.practice;

public class formatTime
{
	public formatTime()
	{
		super();
	}
	public String formatTime(int time)
	{
		time/=1000;
		int minute=time/60;
		//int hour=minute/60;
		int second=time%60;
		minute%=60;
		return String.format("%02d:%02d",minute,second);
	}
	public String formatTime(long time)
	{
		time/=1000;
		long minute=time/60;
		//int hour=minute/60;
		long second=time%60;
		minute%=60;
		return String.format("%02d:%02d",minute,second);
	}
}
