
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
		final int FLING_MIN_DISTANCE=100;// X����y�����ƶ��ľ���(����)
		final int FLING_MIN_VELOCITY=200;// x����y���ϵ��ƶ��ٶ�(����/��)
		if((e1.getX()-e2.getX())>FLING_MIN_DISTANCE&&Math.abs(velocityX)>FLING_MIN_VELOCITY)
		{
			//activity.last();//��һ��������ͨ��activity�о�̬��������
		}
		else if((e2.getX()-e1.getX())>FLING_MIN_DISTANCE&&Math.abs(velocityX)>FLING_MIN_VELOCITY)
		{
			//activity.next();//��һ������
		}
		PlayingActivity.gesture="true";
		return super.onFling(e1,e2,velocityX,velocityY);
	}

}
