
package com.zhanglin.practice;

public class PublicList
{
	//最大数量1000首歌
	static String path[]=new String[1000];
	static String title[]=new String[1000];
	static String artist[]=new String[1000];
	static String album[]=new String[1000];
	static long duration[]=new long[1000];
	static int currentItem=0;
	static int size=0;

	public static void list(String pat,String tit,String art,String alb,long dur,int pos)
	{
		path[pos]=pat;
		title[pos]=tit;
		artist[pos]=art;
		album[pos]=alb;
		duration[pos]=dur;
		size=pos;
	}
}
