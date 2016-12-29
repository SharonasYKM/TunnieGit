package com.example.tunnie;

import logic.Song;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


public class SongCustomView extends LinearLayout {

	private boolean isPressed = false;
	private Song song;
	
	class Layout
	{
		public Layout()
		{
			songNameTV = (TextView)findViewById(R.id.SongNameTV);
			songArtistTV = (TextView)findViewById(R.id.SongArtistTV);	
			songVersLayout = (LinearLayout)findViewById(R.id.songVersLayout);
			songIdLayout = (LinearLayout)findViewById(R.id.songIdLayout);
			openSongVersLayoutBtn = (Button)findViewById(R.id.button1);
		}
		Button openSongVersLayoutBtn;
		TextView songNameTV, songArtistTV, textView3;
		LinearLayout songVersLayout, songIdLayout;
	}
	
	class Events
	{
		public Events()
		{
			l.openSongVersLayoutBtn.setOnClickListener(new OnClickListener() 
			{
				@Override
				public void onClick(View v) 
				{
						if (isPressed)
					{
						l.songVersLayout.setVisibility(GONE);
						isPressed =false;
					}
					else
					{
						l.songVersLayout.setVisibility(VISIBLE);
						isPressed =true;
					}
				}
			});
		}
	}
	Layout l;
	Events e;	
	
	public SongCustomView(Context context, AttributeSet attrs, Song s) 
	{
		super(context, attrs);		
		
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.song_custome_view, this);
		
		l = new Layout(); e = new Events();
		
		setSong(s);
		displaySongDetails();
	}

	public Song getSong() {
		return song;
	}

	public void setSong(Song song) {
		this.song = song;
		displaySongDetails();
	}
	
	private void displaySongDetails()
	{
		l.songArtistTV.setText(song.getSongName());
		l.songNameTV.setText(song.getSongArtist());
	}
}
