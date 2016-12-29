package com.example.tunnie;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;

import JsonDB.JsonUtil;
import lists.FavoriteSongVersList;
import lists.SongList;
import logic.Song;
import logic.SongVers;

// delete internet connection toasting
// if there are 0 result stop search

public class MainActivity extends Activity {

    public static JSONArray chordsDB;

	int minSongOn1Search = 5;
	
	public static String resString;
	
	static String url;
	
	public static int lastSearchSongsNum = 0;
	public static int pageNum = 1;
	public static String searchQueryForUrl;
	LinkedList<Elements> tableItemsList = new LinkedList<Elements>();
	
	SetTableViewByUrlasy searchAsync = new SetTableViewByUrlasy();
	
	class Layout
	{
		public Layout()
		{
			songDisplayLayout = (LinearLayout)findViewById(R.id.songDisplayLayout);
			menuButton = (ImageView)findViewById(R.id.menuButton);
			menu1 = (MenuCustomeView)findViewById(R.id.menu1);
			searchView1 = (SearchView)findViewById(R.id.searchView0);
			root = (FlyOutContainer) findViewById(R.id.main);
			
			progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
			stopSearchBtn = (Button) findViewById(R.id.stopSearchBtn);
			
			moreSongsBtn = (Button) findViewById(R.id.moreSongsBtn);
			progressBarMoreSongs = (ProgressBar) findViewById(R.id.progressBarMoreSongs);
			
			listView = (ListView) findViewById(R.id.list);
		}
		Button stopSearchBtn, moreSongsBtn;
		ProgressBar progressBar1, progressBarMoreSongs;
		SearchView searchView1;
		MenuCustomeView menu1;
		LinearLayout songDisplayLayout;
		ImageView menuButton;
		FlyOutContainer root;
		
		ListView listView; // TODO change name
	}
	class Events
	{
		public Events()
		{
			l.listView.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	                 // ListView Clicked item index
	                 int itemPosition     = position;
	                 
	                 // ListView Clicked item value
	                 String  itemValue    = (String) l.listView.getItemAtPosition(position);
	                    
	                  // Show Alert 
	                  Toast.makeText(getApplicationContext(),
	                    "Position :"+itemPosition+"  ListItem : " +itemValue , Toast.LENGTH_LONG).show();
                }
			});    
			l.stopSearchBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					searchAsync.cancel(true);				//TODO not so working
					l.stopSearchBtn.setVisibility(View.GONE);
					l.progressBar1.setVisibility(View.GONE);
				}
			});
			l.moreSongsBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					l.moreSongsBtn.setVisibility(View.GONE);
					l.progressBarMoreSongs.setVisibility(View.VISIBLE);
					loadSongOfAnotherPage();
				}
			});
			l.searchView1.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
	            @Override
	            public boolean onQueryTextSubmit(String query) {
	            	l.searchView1.clearFocus();
	            	l.root.invalidate();
	            	
		            callSearch(query);
	                return true;
	            }
	            public void callSearch(String query)
	            {
	            	String searchQueryForUrlTwiceTest = query.replace(" ", "+");
	            	l.searchView1.setQuery("", false);
	            	if(searchQueryForUrlTwiceTest != searchQueryForUrl)
	            	{          	
	            		searchQueryForUrl = searchQueryForUrlTwiceTest; 
		            	url = "http://www.ultimate-guitar.com/search.php?search_type=title&value=" + searchQueryForUrl;
		            	
		            	refreshSearch();

		            	if(!ishaveNetworkConnection()){
							Toast.makeText(MainActivity.this, "no internet connection", Toast.LENGTH_LONG).show();
						}
						else{
							l.moreSongsBtn.setVisibility(View.GONE);
							l.stopSearchBtn.setVisibility(View.VISIBLE);
							l.progressBar1.setVisibility(View.VISIBLE);
							searchAsync = new SetTableViewByUrlasy();
							searchAsync.execute();  // put in resString the url html;
						}
	            	}					
	            }
				@Override
				public boolean onQueryTextChange(String newText) {
					return false;
				}
	        });
		}
	}


	Layout l;
	Events e;
	
	public static Context ctx;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity); 
        
        final Context context = this;
        ctx = context;
        
        l = new Layout();
        e = new Events();

        OnFirstOpening();

        chordsDBInit();
        SongList.init();
        FavoriteSongVersList.init();


        
        if(Util.knownChordsList.size()==0)
        {knownChordsInit();} //in order for this to work i need to already have knowChord.txt in file system

        createListViewAdapter();



    }
	@Override
    protected void onStop() {
    	// TODO Auto-generated method stub
    	super.onStop();
    	
    	//l.root.toggleMenu();
    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sample, menu);
		return true;
	}

	public void toggleMenu(View v){
		l.root.toggleMenu();
	}

	private void OnFirstOpening() {

		boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isFirstRun", true);
		if (isFirstRun){

			// add-write text into file
			try {
				FileOutputStream fileout=openFileOutput("chordsDB.txt", MODE_PRIVATE);
				OutputStreamWriter outputWriter=new OutputStreamWriter(fileout);
				JsonUtil jsonUtil = new JsonUtil();
				outputWriter.write(jsonUtil.chordDbString1 + jsonUtil.chordDbString2);
				outputWriter.close();

				//display file saved message
				Toast.makeText(getBaseContext(), "File saved successfully!",
						Toast.LENGTH_SHORT).show();

			} catch (Exception e) {
				e.printStackTrace();
			}
			FileOutputStream fou = null;
			try {
				fou = openFileOutput("KnownChords.txt", MODE_WORLD_WRITEABLE);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			OutputStreamWriter osw = new OutputStreamWriter(fou);
			try {

				osw.flush();
				osw.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			getSharedPreferences("PREFERENCE", MODE_PRIVATE)
					.edit()
					.putBoolean("isFirstRun", false)
					.apply();
		}
	}
    private void knownChordsInit() {
        FileInputStream fis = null;
        try {
            fis = openFileInput("KnownChords.txt");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            //
            e.printStackTrace();
        }
        InputStreamReader isr = new InputStreamReader(fis);

        int data_block = 100;
        char[] data = new char[data_block];
        String final_data = "";
        int size;

        try {
            while((size = isr.read(data))>0)
            {
                String read_data = String.copyValueOf(data, 0, size);
                final_data+=read_data;
                data = new char[data_block];
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //l.result.setText(final_data);    ///////////////////////////////// to test!
        if(final_data=="") return;
        String[] splitedChordData= final_data.split("&");

        for (int i = 0; i < splitedChordData.length; i++)
        {
                Util.knownChordsList.add(Util.getCurrentChord(splitedChordData[i]));
        }
    }

    private void chordsDBInit() {
        FileInputStream fis = null;
        try {
            fis = openFileInput("chordsDB.txt");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            //
            e.printStackTrace();
        }
        InputStreamReader isr = new InputStreamReader(fis);

        int data_block = 100;
        char[] data = new char[data_block];
        String final_data = "";
        int size;

        try {
            while((size = isr.read(data))>0)
            {
                String read_data = String.copyValueOf(data, 0, size);
                final_data+=read_data;
                data = new char[data_block];
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //l.result.setText(final_data);    ///////////////////////////////// to test!
        if(final_data=="") return;
        try {
            chordsDB = new JSONArray(final_data);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
    }

    class SetTableViewByUrlasy extends AsyncTask<String, String, String>
    {
    	final StringBuilder sb = new StringBuilder();
    	String urlAsy;
    	
    	// get html content from url
		@Override
		protected String doInBackground(String... params) {
			DefaultHttpClient httpclient = new DefaultHttpClient(); // Create HTTP Client
        	HttpGet httpget = new HttpGet(urlAsy); // Set the action you want to do
        	HttpResponse response = null;
			try {
				response = httpclient.execute(httpget);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				try {
					this.finalize();
				} catch (Throwable e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				e.printStackTrace();
			} // Executeit
        	HttpEntity entity = response.getEntity(); 
        	InputStream is = null;
			try {
				is = entity.getContent();
			} catch (IllegalStateException e) {
				e.printStackTrace();
				this.cancel(false);
			} catch (IOException e) {
				e.printStackTrace();
				this.cancel(false);
			} // Create an InputStream with the response
        	BufferedReader reader = null;
			try {
				reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				this.cancel(false);
			}
        	String line = null;
        	try {
				while ((line = reader.readLine()) != null) // Read line by line
				    sb.append(line + "\n");
			} catch (IOException e) {
				e.printStackTrace();
				this.cancel(false);
			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			urlAsy = url;
			url = "";
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(String result) {
        	resString = sb.toString(); // Result is here
        	
			Document doc = Jsoup.parse(resString);
		    for (int i = 0; i < 100; i++) // seperate html page to each table row
		    {
					Elements element = doc.select("#page-wrapper > div > div > table > tbody> tr> td > table> tbody > tr:eq("+ i +")");
					if (element.outerHtml()==""){   //this is the breaker of the loop. i<100 has no meaning
						Toast.makeText(MainActivity.this, "there are" + tableItemsList.size() + "items", Toast.LENGTH_SHORT).show();
						break;
					}
					else{
						boolean b = element.outerHtml().contains(">chords<");
						if(b) // make sure only the guitar chord info get inside the list) 
						{
							tableItemsList.add(element);
						}
					}
			}
		    
		    elementListToSongs();
		    
		    if(SongList.myList.size()<minSongOn1Search)
		    {
		    	loadSongOfAnotherPage();
		    }
		    else
		    {
				// on enough songs
				createSongViews();
				l.songDisplayLayout.invalidate();
		    }
			super.onPostExecute(result);
		}
    }
    
    private boolean ishaveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected()){
                    haveConnectedWifi = true;
            		Toast.makeText(MainActivity.this, "wifi yes", Toast.LENGTH_SHORT).show();}
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected()){
                    haveConnectedMobile = true;
            		Toast.makeText(MainActivity.this, "mobile yes", Toast.LENGTH_SHORT).show();}
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

	private void refreshSearch() {
		lastSearchSongsNum = 0;
    	SongList.myList.clear();
    	tableItemsList.clear();
    	resString="";
    	pageNum = 1;
    	l.songDisplayLayout.removeAllViews();
	}
	
	private void loadSongOfAnotherPage() {
		pageNum++;
		tableItemsList.clear();
    	url = "http://www.ultimate-guitar.com/search.php?title=" + searchQueryForUrl + "&page=" + pageNum + "&tab_type_group=text&app_name=ugt&order=myweight";
    	searchAsync = new SetTableViewByUrlasy();
    	searchAsync.execute();
	}
    
    private void elementListToSongs() {
	    for (int i = 0; i < tableItemsList.size(); i++)
	    {
	    	Elements currentTableLine = tableItemsList.get(i);

	    	//get Song Details From Html of current table line
			String songNameOuterHtml = currentTableLine.select("td:eq(1) > div > a").text();
			String songUrlOuterHtml = currentTableLine.select("td:eq(1) > div > a").attr("href");
            if(songUrlOuterHtml.isEmpty())
            {
                songNameOuterHtml = currentTableLine.select("td:eq(2) > div > a").text();
                songUrlOuterHtml = currentTableLine.select("td:eq(2) > div > a").attr("href");
            }
			if(songUrlOuterHtml.charAt(0)=='/') // wierd case handling
			{
				songNameOuterHtml = currentTableLine.select("td:eq(2) > a").text();
				songUrlOuterHtml = currentTableLine.select("td:eq(2) > a").attr("href");
			}
			String ratingPPlOuterHtml = currentTableLine.select("td:eq(2) > span:eq(1) > b").text();
			
			String ratingOuterHtml = "" + 0;
			try {	// wierd case handling
				ratingOuterHtml = currentTableLine.select("td:eq(2) > span > span").attr("class").substring(2);
			} catch (Exception e) {
				try {
					ratingOuterHtml = currentTableLine.select("td:eq(3) > span > span").attr("class").substring(2);
				} catch (Exception e1) {
					ratingPPlOuterHtml = "" + 0;
				}
			}
			String[] splitedsongUrlOuterHtml = songUrlOuterHtml.split("/");
			String artistOuterHtml = splitedsongUrlOuterHtml[4].replace("_", " ");
			

			if(SongList.myList.size()==0)
			{
                Song song = new Song(i, songNameOuterHtml, artistOuterHtml);
                //TODO: they have chagend the rating representation on the HTML...intead of writing how many stars given they put 1-5 stars icons. will need to adjust meanwhile give 4 to all
                ///SongVers songVers = new SongVers(songNameOuterHtml + " (vers 1)", ratingOuterHtml, ratingPPlOuterHtml, songUrlOuterHtml);
                SongVers songVers = new SongVers(songNameOuterHtml + " (vers 1)", "4", ratingPPlOuterHtml, songUrlOuterHtml);
				song.getSongVersList().add(songVers);
		        SongList.myList.add(song);
			}
			
			else
			{
				Song lastSongAdded = SongList.myList.get(SongList.myList.size()-1);
				int versAmountOfLastSong = lastSongAdded.getVersAmount();
				
				// make sure all vers are inside a song
				String songNameWIthoutVersWord;
				if(songNameOuterHtml.contains(" (ver "))
				{
					songNameWIthoutVersWord = songNameOuterHtml.substring(0, songNameOuterHtml.length()-8);
					if(songNameOuterHtml.charAt(songNameOuterHtml.length()-3) == '1')
					{
						songNameWIthoutVersWord = songNameOuterHtml.substring(0, songNameOuterHtml.length()-9);
					}
				}
				else { songNameWIthoutVersWord = songNameOuterHtml;}
				
				
				if(		lastSongAdded.getSongArtist().equals(artistOuterHtml) &&
						lastSongAdded.getSongName().equals(songNameWIthoutVersWord))
				{
                    //TODO: they have chagend the rating representation on the HTML...intead of writing how many stars given they put 1-5 stars icons. will need to adjust meanwhile give 4 to all
                    //SongVers songVers = new SongVers(songNameOuterHtml, ratingOuterHtml, ratingPPlOuterHtml, songUrlOuterHtml);
                    SongVers songVers = new SongVers(songNameOuterHtml, "4", ratingPPlOuterHtml, songUrlOuterHtml);
					lastSongAdded.getSongVersList().add(songVers);
					
					lastSongAdded.setVersAmount(versAmountOfLastSong+1);
				}
				else
				{
					Song song = new Song(i, songNameOuterHtml, artistOuterHtml);
                    //TODO: they have chagend the rating representation on the HTML...intead of writing how many stars given they put 1-5 stars icons. will need to adjust meanwhile give 4 to all
                    //SongVers songVers = new SongVers(songNameOuterHtml, ratingOuterHtml, ratingPPlOuterHtml, songUrlOuterHtml);
                    SongVers songVers = new SongVers(songNameOuterHtml, "4", ratingPPlOuterHtml, songUrlOuterHtml);
					song.getSongVersList().add(songVers);
			        SongList.myList.add(song);
				}
			}
		}
    }




	private void createSongViews()	// create and display view by songlist
		{
			l.stopSearchBtn.setVisibility(View.GONE);
			l.progressBar1.setVisibility(View.GONE);
			
			SongCustomView[] songCustomViewArr = new SongCustomView[SongList.myList.size()];
			
			for(int i=lastSearchSongsNum; i<songCustomViewArr.length;i++ )
			{
				Song currentSong = SongList.myList.get(i);
				
				songCustomViewArr[i] = new SongCustomView(this, null, currentSong);
				songCustomViewArr[i].setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

				songCustomViewArr[i].setSong(currentSong);
				songCustomViewArr[i].l.songNameTV.setText(SongList.myList.get(i).getSongName());
				songCustomViewArr[i].l.songArtistTV.setText(SongList.myList.get(i).getSongArtist());
				
				SongVersCustomView[] songVersCustomViewArr = new SongVersCustomView[currentSong.getSongVersList().size()];
				for(int j=0; j<songVersCustomViewArr.length;j++ )
				{	
					SongVers currentSongVers = currentSong.getSongVersList().get(j);
					songVersCustomViewArr[j] = new SongVersCustomView(this, null, currentSongVers);
					songVersCustomViewArr[j].setSongVers(currentSong.getSongVersList().get(j));
					
					songCustomViewArr[i].l.songVersLayout.addView(songVersCustomViewArr[j]);
				}		
				l.songDisplayLayout.addView(songCustomViewArr[i]);
			}
			lastSearchSongsNum = songCustomViewArr.length;
			
			l.moreSongsBtn.setVisibility(View.VISIBLE);
			l.progressBarMoreSongs.setVisibility(View.GONE);
			
			searchAsync.cancel(true);
		}

	private void createListViewAdapter()
	{
		 // Defined Array values to show in ListView
        String[] values = new String[] { "Android List View", 
                                         "Adapter implementation",
                                         "Simple List View In Android",
                                         "Create List View Android", 
                                         "Android Example", 
                                         "List View Source Code", 
                                         "List View Array Adapter", 
                                         "Android Example List View" 
                                        };

        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
          android.R.layout.simple_list_item_1, android.R.id.text1, values);


        // Assign adapter to ListView
        l.listView.setAdapter(adapter); 
	}
}
























/*
public class SetViewInSongActivityByUrl extends AsyncTask<String, String, String>
{
	final StringBuilder sb = new StringBuilder();
	String urlAsy;
	
	@Override
	protected String doInBackground(String... params) {
		DefaultHttpClient httpclient = new DefaultHttpClient(); // Create HTTP Client
    	HttpGet httpget = new HttpGet(urlAsy); // Set the action you want to do
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
	protected void onPreExecute() {
		urlAsy = MainActivity.url;
		super.onPreExecute();
	}
	
	@Override
	protected void onPostExecute(String result) {
    	resString = sb.toString(); // Result is here
    	resString = resString.replace("\n", "<br>").replace("\r", "<br>");
    	if(resString.length()>2000 && changed==false)
    	{
    		changed=true;
    	} 
		super.onPostExecute(result);
	}
}*/
