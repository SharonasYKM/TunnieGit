package com.example.tunnie;


import lists.SongList;
import logic.Song;
import logic.SongVers;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;




public class FavoriteActivity extends Activity {

    public int cartAmount = 0;

    class Layout
    {
        public Layout()
        {
            songDisplayLayout2 = (LinearLayout)findViewById(R.id.songDisplayLayout2);
            imageView2 = (ImageView)findViewById(R.id.imageView2);

        }
        LinearLayout songDisplayLayout2;
        ImageView imageView2;
    }

    class Events
    {
        public Events()
        {
            l.imageView2.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent i = new Intent(ctx, MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);// flipflop stop
                    ctx.startActivity(i);
                }
            });
        }
    }

    Layout l;
    Events e;

    Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorite_activity);

        final Context context = this;
        ctx = context;

        l = new Layout(); e = new Events();


        //createSongViews();
    }



    private void createSongViews()
    {
        for(int i=0; i<SongList.myList.size();i++ )
        {
            Song currentSong = SongList.myList.get(i);

            SongVersCustomView[] songVersCustomViewArr = new SongVersCustomView[currentSong.getSongVersList().size()];
            for(int j=0; j<songVersCustomViewArr.length;j++ )
            {
                SongVers currentSongVers = currentSong.getSongVersList().get(j);
                if(currentSongVers.isFavorite())
                {
                    songVersCustomViewArr[j] = new SongVersCustomView(this, null,currentSongVers);
                    songVersCustomViewArr[j].setSongVers(currentSongVers);

                    l.songDisplayLayout2.addView(songVersCustomViewArr[j]);
                }
            }
        }
    }


	/*
	private void createSongViews()
	{
		SongVersCustomView[] songVersCustomViewArr = new SongVersCustomView[FavoriteSongVersList.myList.size()];
		for(int i=0; i<FavoriteSongVersList.myList.size();i++ )
		{
			SongVers currentSongVers = FavoriteSongVersList.myList.get(i);
			songVersCustomViewArr[i] = new SongVersCustomView(this, null,currentSongVers);
			songVersCustomViewArr[i].setSongVers(currentSongVers);

			l.songDisplayLayout2.addView(songVersCustomViewArr[i]);
		}
	}
	*/

	/*
	private void createViews()
	{
		SongCustomeView[]  songCustomeViewArr = new SongCustomeView[FavoriteSongList.myList.size()];

		for(int i=0; i<songCustomeViewArr.length;i++ )
		{
			songCustomeViewArr[i] = new SongCustomeView(this, null);

			songCustomeViewArr[i].setSong(FavoriteSongList.myList.get(i));
			songCustomeViewArr[i].l.textView1.setText(FavoriteSongList.myList.get(i).getSongName());
			songCustomeViewArr[i].l.textView2.setText(FavoriteSongList.myList.get(i).getSongArtist());
			songCustomeViewArr[i].l.textView3.setText(FavoriteSongList.myList.get(i).getSongLyrics());


			l.songDisplayLayout2.addView(songCustomeViewArr[i]);
		}
	}
	*/
}