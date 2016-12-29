package com.example.tunnie;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ToggleButton;


// pass to the custume view the responsability of menu butoon. make it dont beloge to each activity seperately

public class MenuCustomeView extends LinearLayout {
	
	class Layout
	{
		public Layout()
		{
			myChordsActivityBtn = (Button)findViewById(R.id.myChordsActivityBtn);
			favoriteActivityBtn = (Button)findViewById(R.id.favoriteActivityBtn);
		}
		Button myChordsActivityBtn, favoriteActivityBtn;
	}
	
	class Events
	{
		public Events()
		{
			l.favoriteActivityBtn.setOnClickListener(new OnClickListener() 
			{
				@Override
				public void onClick(View v) 
				{
					Intent i = new Intent(ctx, FavoriteActivity.class);
					i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	                ctx.startActivity(i); 
					getContext().startActivity(i);
				}
			});
			l.myChordsActivityBtn.setOnClickListener(new OnClickListener() 
			{
				@Override
				public void onClick(View v) 
				{
					Intent i = new Intent(ctx, ChordsActivity.class);
					i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	                ctx.startActivity(i); 
					getContext().startActivity(i);
				}
			});
		}
	}
	Layout l;
	Events e;
	
	Context ctx;
	
	public MenuCustomeView(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
        ctx = context;
		
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.menu_custome_view, this);
		
		l = new Layout(); e = new Events();
		
		this.isInEditMode();
		
	}
}
