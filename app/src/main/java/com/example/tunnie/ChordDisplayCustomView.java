package com.example.tunnie;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.tunnie.R;

import java.util.LinkedList;


// TODO "i Hate this shit remark on the grid display where the place on the fret is given is completly unessesery
public class ChordDisplayCustomView extends LinearLayout {

    JSONObject currentChord;
	private int currentChordVers = 0;
	private LinkedList<int[]> currentFinferingList = new LinkedList<int[]>();
	private int[] currentFinfering = new int[]{-1,-1,-1,-1,-1,-1};
	Context context;
	
	
	class Layout
	{
		public Layout()
		{
			layoutGuitarChordDiagrams = (LinearLayout)findViewById(R.id.layoutGuitarChordDiagrams);
			leftArrow = (TextView)findViewById(R.id.leftArrow);
			rightArrow = (TextView)findViewById(R.id.rightArrow);
			fretStartAndChordName = (TextView)findViewById(R.id.fretStartAndChordName);
			
			main = (FrameLayout)findViewById(R.id.main);
			
			dot1 = (ImageView)findViewById(R.id.dot1);
			dot2 = (ImageView)findViewById(R.id.dot2);
			dot3 = (ImageView)findViewById(R.id.dot3);
			dot4 = (ImageView)findViewById(R.id.dot4);
			dot5 = (ImageView)findViewById(R.id.dot5);
			dot6 = (ImageView)findViewById(R.id.dot6);
		}
		FrameLayout main;
		LinearLayout layoutGuitarChordDiagrams;
		TextView leftArrow, rightArrow, fretStartAndChordName;
		ImageView dot1, dot2, dot3, dot4, dot5, dot6;
	}
	
	class Events
	{
		public Events()
		{	
			l.leftArrow.setOnClickListener(new OnClickListener() 
			{
				@Override
				public void onClick(View v) 
				{
					if(currentChord != null)
					{
						if(currentChordVers<=0){currentChordVers = currentFinferingList.size()-1;}
						else{currentChordVers--;}
						getCurrentChordVersNDisplay(parent);
					}
					else
					{
						Toast.makeText(context, "there is no choosen chord", Toast.LENGTH_SHORT).show();
					}
				}
			});
			l.rightArrow.setOnClickListener(new OnClickListener() 
			{
				@Override
				public void onClick(View v) 
				{
					if(currentChord != null)
					{
						if(currentChordVers>=currentFinferingList.size()-1){currentChordVers=0;}
						else{currentChordVers++;}
						getCurrentChordVersNDisplay(parent);
					}
					else
					{
						Toast.makeText(context, "there is no choosen chord", Toast.LENGTH_SHORT).show();
					}
				}
			});
		}
	}
	Layout l;
	Events e;
	Activity parent;
	
	public ChordDisplayCustomView(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
		
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.chord_display_custume_view, this);
		
		this.context = context;
		l = new Layout(); e = new Events();
		
		
	}
	
	public JSONObject getCurrentChord(String choosenNoteGeted, String choosenTypeNoteGeted)
	{
        String chordName = choosenNoteGeted+choosenTypeNoteGeted;
        return getCurrentChord(chordName);
    }
	
	public JSONObject getCurrentChord(String noteNtypeChord, Context ctx)
	{
        currentChordVers=0;
        for(int i=0; i<MainActivity.chordsDB.length(); i++)
        {
            try {
                if(MainActivity.chordsDB.getJSONObject(i).getString("symbol").contains(noteNtypeChord)
                        || MainActivity.chordsDB.getJSONObject(i).getJSONArray("alternativeSymbols").getString(0).contains(noteNtypeChord))
                {
                    currentChord = MainActivity.chordsDB.getJSONObject(i);
                    return currentChord;
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
        l.main.setVisibility(View.GONE);
        Toast.makeText(ctx, "Cant find this chord :(", Toast.LENGTH_LONG).show();
        return null;
	}

    public JSONObject getCurrentChord(String noteNtypeChord)
    {
        currentChordVers=0;
        for(int i=0; i<MainActivity.chordsDB.length(); i++)
        {
            try {
                if(MainActivity.chordsDB.getJSONObject(i).getString("symbol").contains(noteNtypeChord)
                        || MainActivity.chordsDB.getJSONObject(i).getJSONArray("alternativeSymbols").getString(0).contains(noteNtypeChord))
                {
                    currentChord = MainActivity.chordsDB.getJSONObject(i);
                    return currentChord;
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }
	
	
	public void displayCurrentChord(Activity activity, JSONObject currentChord)
	{
		parent = activity;
		if(currentChord!=null){getCurrentChordVersNDisplay(activity);}
	}
	
	public void searchNDisplayCurrentChord(Activity activity, String choosenNoteGeted, String choosenTypeNoteGeted)
	{
		getCurrentChord(choosenNoteGeted, choosenTypeNoteGeted);
		if(currentChord!=null){displayCurrentChord(activity, currentChord);}
	}
	
	public void searchNDisplayCurrentChord(Activity activity, String noteNTypeChord)
	{
		getCurrentChord(noteNTypeChord);
		if(currentChord!=null){displayCurrentChord(activity, currentChord);}
	}
	
	public void getCurrentChordVersNDisplay(Activity activity)
	{
		activity.runOnUiThread(new Runnable()
		{
			@Override
			public void run() {
				if(currentChord!=null)
				{
                    currentFinferingList = new LinkedList<int[]>();
                    try {
                        for(int i=0; i<currentChord.getJSONArray("shapes").length() ; i++)
                        {
                            int[] chordVers = new int[]{
                            currentChord.getJSONArray("shapes").getJSONArray(i).getInt(0),
                            currentChord.getJSONArray("shapes").getJSONArray(i).getInt(1),
                            currentChord.getJSONArray("shapes").getJSONArray(i).getInt(2),
                            currentChord.getJSONArray("shapes").getJSONArray(i).getInt(3),
                            currentChord.getJSONArray("shapes").getJSONArray(i).getInt(4),
                            currentChord.getJSONArray("shapes").getJSONArray(i).getInt(5)};
                            currentFinferingList.add(chordVers);
                        }
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
					currentFinfering = currentFinferingList.get(currentChordVers);
					createChordDiagram();
				}
			}
		});
	}
	
	
	private void createChordDiagram()
	{
		ImageView[] dots = {l.dot1, l.dot2, l.dot3, l.dot4, l.dot5, l.dot6};
		
		int maxFretInChord=4;
		for(int i=0; i<currentFinfering.length; i++)
		{
			if(currentFinfering[i]>maxFretInChord&&currentFinfering[i]!=-1){maxFretInChord=currentFinfering[i];}
		}

        try {
            l.fretStartAndChordName.setText(currentChord.getString("symbol") + String.valueOf(maxFretInChord - 4));
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        for(int i=0; i<currentFinfering.length; i++)
		{
			LayoutParams imgParams = (LayoutParams) dots[i].getLayoutParams();;
			ImageView iv = dots[i]; 
			
			if(currentFinfering[i]==-1)
			{
				iv.setImageResource(R.drawable.ex);
				imgParams.setMargins(0, 0, 8, 0);
				//imgParams.topMargin = 0;
			}
			else if(currentFinfering[i]==0)
			{
				imgParams.setMargins(0, 15, 8, 0);
				iv.setImageResource(R.drawable.round);
				//imgParams.topMargin = 0;
			}
			else
			{				
				imgParams.setMargins(0, (currentFinfering[i]-maxFretInChord+3)*35 +35 , 8, 0);
				iv.setImageResource(R.drawable.round);
				//imgParams.topMargin = (currentFinfering[i]-maxFretInChord+4)*70;
			}			
			
			iv.setLayoutParams(imgParams);
			
			
			/*
			LayoutParams imgParams = (LayoutParams) dots[i].getLayoutParams();
			switch (currentFinfering[i]-maxFretInChord+5)
				{
					case -1:
						dots[i].setImageResource(R.drawable.ex);
						imgParams.topMargin = 0;
						break;
					case 0:
						imgParams.topMargin = 10; //15//5.3	
						dots[i].setImageResource(R.drawable.round);
						break;
					case 1:
						imgParams.topMargin = 113;  //38//12
						dots[i].setImageResource(R.drawable.round);
						break;
					case 2:
						imgParams.topMargin = 234; //79//25.3
						dots[i].setImageResource(R.drawable.round);
						break;
					case 3:
						imgParams.topMargin = 355; //120//38.6
						dots[i].setImageResource(R.drawable.round);
						break;
					case 4:
						imgParams.topMargin = 476; //161//52
						dots[i].setImageResource(R.drawable.round);
						break;
					default:
						//dots[i].setImageResource(R.drawable.ex);
						//imgParams.topMargin = 0;
						break;

				}
				
				l.layoutGuitarChordDiagrams.invalidate();
				//dots[i].setTop(top); //not work
				//dots[i].setPadding(0, 10, 10, 0);	//make circles smaller

				//dot.setPadding(0, 10, 10, 0);
				//dot.setTop(10);
				//dots[i].setLayoutParams(imgParams);
				 * */
			}						
		}
	}


