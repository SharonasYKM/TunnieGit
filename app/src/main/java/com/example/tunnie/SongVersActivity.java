
package com.example.tunnie;


import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import lists.SongList;


// when activity given flyoutcontainer the whole chord show thing get fucked up - so for now theres no menu on activity
// the casting in the transposition functions here and in util class is unessesery. i can just sent the arraylist insted of String[].
// "noTranspositional" transposition to chords

public class SongVersActivity extends Activity
{
	public String resultFromLuncher;
    private ArrayList<TextView> songVersChordsTV = new ArrayList<TextView>();
    private ArrayList<String> songVersChordsList = new ArrayList<String>();
    private final ArrayList<String> originalSongVersChordsList = new ArrayList<String>();

	String urlParsed;
	int textSize = 20;
    private int transed = 0;
    private int halfTones;

    class Layout
	{
		public Layout()
		{
			scrollView1 = (ScrollView)findViewById(R.id.scrollView1);
			getDownWithTheBoogie2 = (ScrollDowner)findViewById(R.id.getDownWithTheBoogie2);
			
			mainLayout = (LinearLayout)findViewById(R.id.mainLayout);
			chordDisplayCustomView = (ChordDisplayCustomView)findViewById(R.id.chordDisplayCustomView);
		
			waitAnimation = (ProgressBar)findViewById(R.id.progressBar1);
			
			mainContainer = (FrameLayout)findViewById(R.id.mainContainer);
			replaceWithZoomable= (FrameLayout)findViewById(R.id.replaceWithZoomable);

            plusTransBtn = (Button)findViewById(R.id.plusTrans);
            minusTransBtn = (Button)findViewById(R.id.minusTrans);
            bestTransBtn = (Button)findViewById(R.id.bestTrans);
            add2FavoriteBtn = (Button)findViewById(R.id.add2Favorite);
		}
		ProgressBar waitAnimation;
		
		ScrollDowner getDownWithTheBoogie2;
		ScrollView scrollView1;
		/**/
		LinearLayout mainLayout;
		ChordDisplayCustomView chordDisplayCustomView;
		
		FrameLayout mainContainer, replaceWithZoomable;

        Button plusTransBtn, minusTransBtn, bestTransBtn, add2FavoriteBtn;
	}
	
	class Events
	{
		public Events()
		{
			l.mainLayout.setOnTouchListener(new OnTouchListener() {	
				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					l.chordDisplayCustomView.setVisibility(View.GONE);
					return false;
				}
			});

            OnClickListener transpositionListener = new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(v == l.minusTransBtn){
                        transpositionChordTextViewBy(songVersChordsTV ,-1);
                        transed--;
                    }
                    if(v == l.plusTransBtn){
                        transpositionChordTextViewBy(songVersChordsTV ,1);
                        transed++;
                    }
                    if(v == l.bestTransBtn){
                        /*    // cuzOfTranspo
                        int bestTrans = Util.getBestTranspositionByHalftones(getOriginalSongVersChordsArr());

                        transpositionChordTextViewBy(songVersChordsTV ,bestTrans-transed);
                        transed = bestTrans;
                        */
                    }
                }
            };
            l.minusTransBtn.setOnClickListener(transpositionListener);
            l.plusTransBtn.setOnClickListener(transpositionListener);
            l.bestTransBtn.setOnClickListener(transpositionListener);
		}
	}
	
	Layout l;
	Events e;
	
	Context ctx;
	
	@Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.songvers_activity);
        ctx = this;
        
        l = new Layout();
        e = new Events();
        
        new SetViewInSongActivityByUrl().execute();
        l.getDownWithTheBoogie2.setHandlerForAutoScroll(l.scrollView1);
    }
	
	
	// Song Launcher // 
	
	public class SetViewInSongActivityByUrl extends AsyncTask<String, String, String>
    {
    	final StringBuilder sb = new StringBuilder();
    	String url;

		@Override
		protected void onPreExecute() {
			url = SongList.choosenSongVers.getUrl();
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {

			HttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
			HttpConnectionParams.setSoTimeout(httpParams, 10000);
			DefaultHttpClient httpclient = new DefaultHttpClient(); // Create HTTP Client
			
        	HttpGet httpget = new HttpGet(url); // Set the action you want to do
        	HttpResponse response = null;
			try {
				response = httpclient.execute(httpget);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // Executeit
        	HttpEntity entity = response.getEntity(); 
        	InputStream is = null;
			try {
				is = entity.getContent();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // Create an InputStream with the response
        	BufferedReader reader = null;
			try {
				reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	String line = null;
        	try {
				while ((line = reader.readLine()) != null) // Read line by line
				    sb.append(line + "\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			resultFromLuncher = sb.toString(); // Result is here
			resultFromLuncher = resultFromLuncher.replace("\n", "<br>").replace("\r", "<br>");
			
			Document doc = Jsoup.parse(resultFromLuncher);
		    String s = doc.select("#cont > pre").outerHtml();
		    getViewFromParse(s, l.mainLayout);
            setSongVersChordsList();

			super.onPostExecute(result);
			
		}
    }
	
	// parser //
	
	void getViewFromParse(String htmlPlusSpaces, LinearLayout ll)
    {
		htmlPlusSpaces = htmlPlusSpaces.substring(573);
		htmlPlusSpaces = htmlPlusSpaces.replace("<pre>", "");
		htmlPlusSpaces = htmlPlusSpaces.replace("</pre>", "");
		htmlPlusSpaces = htmlPlusSpaces.replaceAll("<i>", "");
		htmlPlusSpaces = htmlPlusSpaces.replaceAll("</i>", "");
		
    	String[] htmlSplit2br = htmlPlusSpaces.split("<br>");//spilt to br
    	
		l.waitAnimation.setVisibility(View.GONE);
	    l.mainLayout.invalidate();

    	for (int i = 0; i < htmlSplit2br.length; i++)   // turn span into chordCostumeViews-TextView
    	{
    		if(htmlSplit2br[i].contains("<span>"))
    		{
    			ll.addView(createChordsSongLine(htmlSplit2br[i]));
    		}
    		else
    		{
    			ll.addView(createLyricsSongLine(htmlSplit2br[i]));
    		}
		}
    }	
	
	LinearLayout createChordsSongLine(String chordsLine)
	{
		chordsLine = chordsLine.replace("<span>", "");
		chordsLine = chordsLine.replace("</span>", "");// remove span tag
		
		LinearLayout ll = new LinearLayout(this);
		LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		ll.setLayoutParams(layoutParams); //create the layout where the chords will be in
		
		String currentFound = "";
		for (int i = 0; i < chordsLine.length();)     			
		{
			if(chordsLine.charAt(i) == ' ')
			{
				for(int j=i; i<chordsLine.length(); j++)
				{
					if(chordsLine.charAt(j) == ' ')
					{
						currentFound+= " ";
						i++;
					}
					else
					{
						ll.addView(createBlank(currentFound));
						currentFound="";
						break;
					}
				}
				
			}
			else
			{				
				for (int j=i; j <chordsLine.length(); j++)
				{
					if(chordsLine.charAt(j) != ' ')
					{
						currentFound+= chordsLine.charAt(j);
						i++;
					}
					else
					{
                        /*if(Util.getCurrentChord(currentFound)!=null)
                        {

						}*/
						ll.addView(createChord(currentFound));
						currentFound="";
						break;
					}
				}
			}
		}
		ll.addView(createChord(currentFound));
		return ll;
	}

	TextView createLyricsSongLine(String lyricsLine)
	{
		TextView tv = new TextView(ctx);
		tv.setText(lyricsLine);
		
		Typeface tf = Typeface.createFromAsset(getAssets(), "CourierNewRegular.ttf");
		tv.setTypeface(tf);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
	
    	return tv;
	}
	
	TextView createBlank(String blank)
	{
    	return createLyricsSongLine(blank);
	}
	
	TextView createChord(String chordsLine)
	{
		TextView cctv = new TextView(ctx);
		
		cctv.setText(chordsLine);
		
		Typeface tf = Typeface.createFromAsset(getAssets(),
		        "CourierNewRegular.ttf");
		cctv.setTypeface(tf);
		cctv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
		cctv.setTextColor(Color.RED);
		giveTextViewListener(cctv);

        songVersChordsTV.add(cctv);
		
		return cctv;
	}
	
	private void giveTextViewListener(final TextView chordCustumeTextView)
	{
		chordCustumeTextView.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
	    		String noteNtypeChord = (String) chordCustumeTextView.getText();
	    		l.chordDisplayCustomView.searchNDisplayCurrentChord(SongVersActivity.this, noteNtypeChord);
	    		
	    		runOnUiThread(new Runnable() 
	    		{
					public void run() 
					{
						new Handler().postDelayed(new Runnable() 
						{
							@Override
							public void run() 
							{	
								View chordCusomParent = (View) chordCustumeTextView.getParent();
								int y = (int) chordCusomParent.getY() - l.scrollView1.getScrollY();
								int x = (int) chordCustumeTextView.getX()- l.scrollView1.getScrollX();
								
					    		l.chordDisplayCustomView.setX(x);
					    		l.chordDisplayCustomView.setY(y);
					    		l.chordDisplayCustomView.setVisibility(View.VISIBLE);
							}
						}, 100);
					}
				});
			}
		});	
	}



    // transposition //

    void transpositionChordTextViewBy(ArrayList<TextView> arrayListChords, int halfTones)
    {
        for (int j = 0; j < arrayListChords.size(); j++) {
            String beforeTransChord = (String)arrayListChords.get(j).getText();

            //String afterTransChord = Util.traspositionBy(beforeTransChord, halfTones); // cuzOfTranspo

            //arrayListChords.get(j).setText(afterTransChord);
        }
    }
    void setSongVersChordsList() //from songVersChordsTV
    {
        for (int i = 0; i < songVersChordsTV.size(); i++)
        {
            if(songVersChordsList.contains((String)songVersChordsTV.get(i).getText()))
            {
                continue;
            }
            else
            {
                songVersChordsList.add((String)songVersChordsTV.get(i).getText());
                originalSongVersChordsList.add((String)songVersChordsTV.get(i).getText());
            }
        }
    }
    public String[] getOriginalSongVersChordsArr()
    {
        String[] songVersChordsArr = new String[originalSongVersChordsList.size()];
        for (int i = 0; i < originalSongVersChordsList.size(); i++)
        {
            songVersChordsArr[i] = originalSongVersChordsList.get(i);
        }
        return songVersChordsArr;
    }
}
