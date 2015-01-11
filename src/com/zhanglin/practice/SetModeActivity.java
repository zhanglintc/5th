
package com.zhanglin.practice;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class SetModeActivity extends Activity
{
	private RadioGroup radioGroup=null;
	private RadioButton radioButton1,radioButton2,radioButton3=null;
	private Button apply=null;
	static int mode=1;//播放模式,1全曲,2单曲,3随机

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setmode);

		radioGroup=(RadioGroup)findViewById(R.id.radioGroup1);
		radioButton1=(RadioButton)findViewById(R.id.radio1);
		radioButton2=(RadioButton)findViewById(R.id.radio2);
		radioButton3=(RadioButton)findViewById(R.id.radio3);
		apply=(Button)findViewById(R.id.apply);

		switch(mode)
		{
		case 1:
			radioButton1.setChecked(true);
			break;
		case 2:
			radioButton2.setChecked(true);
			break;
		case 3:
			radioButton3.setChecked(true);
			break;
		}

		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{

			@Override
			public void onCheckedChanged(RadioGroup group,int checkedId)
			{
				// TODO Auto-generated method stub
				switch(checkedId)
				{
				case R.id.radio1:
					mode=1;
					break;

				case R.id.radio2:
					mode=2;
					break;

				case R.id.radio3:
					mode=3;
					break;
				default:
					break;
				}
			}
		});

		apply.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
}
