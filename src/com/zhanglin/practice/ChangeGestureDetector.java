
package com.zhanglin.practice;

import android.view.MotionEvent;
import android.view.GestureDetector.SimpleOnGestureListener;

public class ChangeGestureDetector extends SimpleOnGestureListener
{
	PlayingActivity activity;

	public ChangeGestureDetector(PlayingActivity activity)
	{
		this.activity=activity;
	}

	@Override
	public boolean onFling(MotionEvent e1,MotionEvent e2,float velocityX,float velocityY)
	{
		final int FLING_MIN_DISTANCE=100;// X或者y轴上移动的距离(像素)
		final int FLING_MIN_VELOCITY=200;// x或者y轴上的移动速度(像素/秒)
		if((e1.getX()-e2.getX())>FLING_MIN_DISTANCE&&Math.abs(velocityX)>FLING_MIN_VELOCITY)
		{
			//activity.last();//上一曲操作，通过activity中静态函数调用
		}
		else if((e2.getX()-e1.getX())>FLING_MIN_DISTANCE&&Math.abs(velocityX)>FLING_MIN_VELOCITY)
		{
			//activity.next();//下一曲操作
		}
		PlayingActivity.gesture="true";
		return super.onFling(e1,e2,velocityX,velocityY);
	}

}
