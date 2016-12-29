package logic;

import android.widget.TextView;

import com.example.tunnie.Util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;

public class SongVers
{
	
////////////////	Properties		///////////////////////////////
	
	private String name;
	private int rating;
	private int pplRate;
	private String url;
	private boolean isFavorite = false;
	private int id;
	private Document doc;
	private String songString;
	LinkedList<JSONObject> chordList = new LinkedList<JSONObject>();
	
	
	private String htmlPlusSpaces;
	
////////////////	Contractors		///////////////////////////////
	
	public SongVers(String name, String rating, String pplRate, String url) 
	{
			this.setName(name);
			
			if(rating.equals("")) this.rating=0;
			else this.rating=Integer.parseInt(rating);
			
			if(pplRate.equals("")) this.pplRate=0;
			else this.pplRate=Integer.parseInt(pplRate);
			
			this.url = url;
	}
	
	
	public SongVers(String name, int rating, int pplRate, String url)
	{
		
	}
	
	
	public SongVers(String name, String htmlPlusSpaces, int rating)
	{
		this.setName(name);
		this.rating=rating;
		this.htmlPlusSpaces=htmlPlusSpaces;
		getChordListFromHtmlPre();
	}
	
	public SongVers(String name, int rating, String url,int id, LinkedList<JSONObject> chordList)
	{
		this.setName(name);
		this.rating=rating;
		this.url=url;
		this.id=id;
		this.setChordList(chordList);
	}
	
	public SongVers(String name, int rating, String url,int id)
	{
		this.setName(name);
		this.rating=rating;
		this.url=url;
		this.id=id;
	}
	
////////////////	Methods		///////////////////////////////	
	



	void getChordListFromHtmlPre()
    {
    	String[] htmlSplit2br = htmlPlusSpaces.split("<br>");//spilt to br
    	
    	for (int i = 0; i < htmlSplit2br.length; i++)   // turn span into chordCustomeViews-TextView
    	{
    		if(htmlSplit2br[i].contains("<span>"))
    		{
    			createChordsfromSongLine(htmlSplit2br[i]);
    		}
    		else
    		{
    			//ll.addView(createLyricsSongLine(htmlSplit2br[i]));  /// if i will ever need lyrics in songvers before lunch to view
    		}	
		}
    }	
	
	void createChordsfromSongLine(String chordsLine)
	{
		chordsLine = chordsLine.replace("<span>", "");
		chordsLine = chordsLine.replace("</span>", "");// remove span tag
		
		String currentFound = "";
		for (int i = 0; i < chordsLine.length(); i++)     			
		{
			if(chordsLine.charAt(i) == ' ')
			{
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
						addChordToSongVers(currentFound);
						currentFound="";
						break;
					}
				}
			}
		}
		addChordToSongVers(currentFound);
	}
	
	private void addChordToSongVers(String currentFound)
	{
		JSONObject currentFoundChord = Util.getCurrentChord(currentFound);
		if(!chordList.contains(currentFoundChord))
		{
			chordList.add(currentFoundChord);
		}
	}

	//////////////////////////
	
    public void getHtmlFromUrl(final String url)
    {
		new Thread(new Runnable() {
	        @Override
	        public void run() {
	            while (true) {
	                try {
	                	DefaultHttpClient httpclient = new DefaultHttpClient(); // Create HTTP Client
	                	HttpGet httpget = new HttpGet(url); // Set the action you want to do
	                	HttpResponse response = httpclient.execute(httpget); // Executeit
	                	HttpEntity entity = response.getEntity(); 
	                	InputStream is = entity.getContent(); // Create an InputStream with the response
	                	BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
	                	final StringBuilder sb = new StringBuilder();
	                	String line = null;
	                	while ((line = reader.readLine()) != null) {sb.append(line + "\n");}// Read line by line
	                	String htmlString = sb.toString(); // Result is here
						setSongString(getSongFromUGHtml(htmlString));
						
						if(htmlString != null) is.close(); // Close the stream

	                } catch (Exception e)
	                {
	                	e.printStackTrace();
	                }
	            }
	        }
	    }).start();
	}

	public String getSongFromUGHtml(String html)
	{
	    this.doc = Jsoup.parse(html);
	    String s = doc.select("#cont > pre:eq(2)").outerHtml();
	    s = s.substring(6);
	    
		return s;
	}
	
	String makeString(InputStream result)
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(result));
        StringBuilder out = new StringBuilder();
        String line;
        String print = null;

        try
        {
			while ((line = reader.readLine()) != null)
			{
			    out.append(line);
			}
			 print = out.toString();
			 reader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
        return print;
    }
	
	
	
////////////////	GetSet		///////////////////////////////
	
	public TextView setChordable(String possibleChord)
	{
		return null;
	}

	public boolean isFavorite() {
		return isFavorite;
	}

	public void setFavorite(boolean isFavorite) {
		this.isFavorite = isFavorite;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LinkedList<JSONObject> getChordList() {
		return chordList;
	}

	public void setChordList(LinkedList<JSONObject> chordList) {
		this.chordList = chordList;
	}

	public String getSongString() {
		return songString;
	}

	public void setSongString(String songString) {
		this.songString = songString;
	}

	public String getHtmlPlusSpaces() {
		return htmlPlusSpaces;
	}

	public void setHtmlPlusSpaces(String htmlPlusSpaces) {
		this.htmlPlusSpaces = htmlPlusSpaces;
	}


	public int getPplRate() {
		return pplRate;
	}


	public void setPplRate(int pplRate) {
		this.pplRate = pplRate;
	}
}