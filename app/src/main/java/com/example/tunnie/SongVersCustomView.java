package com.example.tunnie;

import lists.FavoriteSongVersList;
import lists.SongList;
import logic.SongVers;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


//essential to do setSongVers...if not the is favorite will make the program collapse

public class SongVersCustomView extends LinearLayout {

	private SongVers SVCVsongVers;
	
	class Layout
	{
		public Layout()
		{
			favoriteCheckBox = (CheckBox)findViewById(R.id.favoriteCheckBox);
			name = (TextView)findViewById(R.id.name);
			chordable = (TextView)findViewById(R.id.chordable);
			
			mainLayout = (LinearLayout)findViewById(R.id.mainLayout);
			ratingLayout = (LinearLayout)findViewById(R.id.ratingLayout);
			
			ratingStarsTV = (TextView)findViewById(R.id.ratingStars);
		}
		LinearLayout mainLayout, ratingLayout;
		CheckBox favoriteCheckBox;
		TextView name, chordable, ratingStarsTV;
	}
	
	class Events
	{
		public Events()
		{
			l.mainLayout.setOnClickListener(new OnClickListener() 
			{
				@Override
				public void onClick(View v) 
				{
					SongList.choosenSongVers = SVCVsongVers;
					Intent i = new Intent(ctx, SongVersActivity.class);
					i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	                ctx.startActivity(i); 
					getContext().startActivity(i);
				}
			});
			l.favoriteCheckBox.setOnClickListener(new OnClickListener() 
			{
				@Override
				public void onClick(View v) 
				{
					if (l.favoriteCheckBox.isChecked())
					{
						FavoriteSongVersList.myList.add(SVCVsongVers);
						SVCVsongVers.setFavorite(true);
					}
					else
					{
						FavoriteSongVersList.myList.remove(SVCVsongVers);
						SVCVsongVers.setFavorite(false);
					}
				}
			});
		}
	}
	Layout l;
	Events e;
	
	Context ctx;
	
	public SongVersCustomView(Context context, AttributeSet attrs, SongVers currentSongVers) 
	{
		super(context, attrs);
		ctx = context;
		
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.song_vers_custome_view, this);
		
		l = new Layout(); e = new Events();
		SVCVsongVers = currentSongVers;
		isSongVersFavorite();
	}

	
	private void isSongVersFavorite()
	{
		if(SVCVsongVers.isFavorite())
		{
			l.favoriteCheckBox.setChecked(true);
		}
	}
	
	public SongVers getSongVers() {
		return SVCVsongVers;
	}

	public void setSongVers(SongVers songVers) {
		this.SVCVsongVers = songVers;
		l.name.setText(SVCVsongVers.getName()); //set name
		
		l.ratingStarsTV.setText("Rating :");
		for(int i=0; i<(int)songVers.getRating(); i++) //set stars
		{
			ImageView iv = new ImageView(ctx);
			iv.setImageResource(R.drawable.star0);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(25, 25);
			iv.setLayoutParams(layoutParams);
			l.ratingLayout.addView(iv);
		}
		TextView tv = new TextView(ctx);
		tv.setText("[" + songVers.getPplRate() + " people rate]"); // set how mant people rate
		l.ratingLayout.addView(tv);
	}
}

