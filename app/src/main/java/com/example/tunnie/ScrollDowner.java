package com.example.tunnie;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

// give the stop button play and pause: when the scroll done manually while the scroller scroll, the scroller will stop scroll.
public class ScrollDowner extends LinearLayout {

    int maxSpeed = 50;
    static int ticker =0;
	static boolean isPause = true;
	int screenHeight;

	class Layout
	{
		public Layout()
		{
			seekBar = (SeekBar)findViewById(R.id.seekBar);
			stopBtn = (Button)findViewById(R.id.buttonStop);
		}
		Button stopBtn;
		SeekBar seekBar;
	}
	
	class Events
	{
		public Events()
		{
			l.seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
				
				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
				}

				@Override
				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser) {
					isPause=false;
					ticker = progress;
                    if(progress==0)
                        {
                            isPause = true;
                        }
				}

				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {
				}
			});
			
			l.stopBtn.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					isPause=true;
					return false;
				}
			});
		}
	}
	Layout l;
	Events e;
	
	Context ctx;
	
	public ScrollDowner(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
        ctx = context;
		
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.get_down_with_the_boogie, this);
		
		l = new Layout(); e = new Events();
		
		init();
		
	}
	
	private void init()
	{
		l.seekBar.setMax(maxSpeed);
		l.seekBar.setProgress(0);
		isPause = true;
		
		WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
        Point size = new Point();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			display.getSize(size);   //--- is this realy nessesery?, cuz if it is...the app wont me competable with 11-12 api
		}
		screenHeight = size.y;
	}
	
	public void setHandlerForAutoScroll(final View wv) //create the ticker for the auto scroll
	{
		final Handler mHandler = new Handler();
		
		new Thread(new Runnable() {
	        @Override
	        public void run() {
	            while (true) {
	                try {
	                    Thread.sleep(maxSpeed-ticker+10); //for some reason, when the seekbar peak, the whole program was shut down. the +20 somehow manage it.
	                    mHandler.post(new Runnable() {
	                        @Override
	                        public void run()
	                        {
                                int gsY = wv.getScrollY();
                                int gmh = wv.getMeasuredHeight();
								//if(gsY+gmh >591+618 ){isPause=true;}  // try to stop the scroll when reach rhe bottom unsuccessfully
	                        	if(!isPause){wv.scrollBy(0, 1);}
	                        }
	                    });
	                } catch (Exception e)
	                {
	                }
	            }
	        }
	    }).start();
	}
}