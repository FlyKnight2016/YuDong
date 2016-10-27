package com.example.vedioviewcompat;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.vedioviewcompat.MediaController.MediaPlayerControl;
import com.example.vedioviewcompat.MediaController.onClickIsFullScreenListener;


public class MainActivity extends Activity implements onClickIsFullScreenListener {

	private MediaController mController;
	private boolean fullscreen=false;
	private VideoView viv;
	private ProgressBar progressBar;
	private RelativeLayout rlDD;
	private RelativeLayout rlTop;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.activity_main);
		viv = (VideoView) findViewById(R.id.videoView);
		rlDD=(RelativeLayout) findViewById(R.id.rl_dd);
//		progressBar=(ProgressBar) findViewById(R.id.progressBar1);
		mController = new MediaController(this);
		mController.setClickIsFullScreenListener(this);
	
		viv.setMediaController(mController);
//		progressBar.setVisibility(View.VISIBLE);
		viv.setVideoURI(Uri.parse("android.resource://" + getPackageName()
		+ "/" + R.raw.apple));
		viv.requestFocus();
		viv.start();
		
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return super.onTouchEvent(event);
	}
	
	@Override
	public void setOnClickIsFullScreen() {
		// TODO Auto-generated method stub
		if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){//设置RelativeLayout的全屏模式
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}else{
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}    
	}
	public void onConfigurationChanged(Configuration newConfig) {
		if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
			Log.e("info", "横屏");
			rlDD.setVisibility(View.GONE);
		}else{
			Log.e("info", "竖屏");
			rlDD.setVisibility(View.VISIBLE);
		}
		super.onConfigurationChanged(newConfig);
		   viv.refreshDrawableState();
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
