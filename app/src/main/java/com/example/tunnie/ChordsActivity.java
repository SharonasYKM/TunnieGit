package com.example.tunnie;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import org.json.JSONException;
import org.json.JSONObject;

public class ChordsActivity extends Activity
{
  public static String[] chordTypeStringArr = { "", "m", "aug", "dim", "sus2", "sus4", "5", "6", "m6", "6/9", "7", "m7", "m(maj7)", "maj7", "º7", "m7b5", "7#5", "7b5", "7sus2", "7sus4", "9", "m9", "maj9", "9#5", "9b5", "9sus2", "9sus4", "m11", "13", "m13", "maj13" };
  public static String[] toneStringArr = { "A", "A#", "Ab", "B", "Bb", "C", "C#", "D", "D#", "Db", "E", "Eb", "F", "F#", "G", "G#", "Gb" };
  private String choosenNote = "";
  private String choosenTypeNote = "";
  private LinkedList<CheckedTextView> chordTypeCheckedTextViewList = new LinkedList();
  private LinkedList<CheckedTextView> toneCheckedTextViewList = new LinkedList();
  JSONObject currentChord;


  class Layout
  {
    public Layout()
    {
      chordDisplayCustumeView1 = (ChordDisplayCustomView)findViewById(R.id.chordDisplayCustumeView1);
      getChordDisplayBtn =  (Button) findViewById(R.id.getChordDisplayBtn);
      iKnowBtn = (Button)findViewById(R.id.iKnowBtn);

      knownChordsLayout = (LinearLayout)findViewById(R.id.knownChordsLayout);
      menuButton = (ImageView)findViewById(R.id.menuButton);
      root = (FlyOutContainer)findViewById(R.id.main);
      toneDisplayLayout = (LinearLayout)findViewById(R.id.toneDisplayLayout);
      typeChordDisplayLayout = (LinearLayout)findViewById(R.id.typeChordDisplayLayout);
    }
    ChordDisplayCustomView chordDisplayCustumeView1;
    Button getChordDisplayBtn, iKnowBtn;
    LinearLayout knownChordsLayout, toneDisplayLayout, typeChordDisplayLayout;
    ImageView menuButton;
    FlyOutContainer root;
  }
  class Events
  {
    public Events()
    {
    	l.getChordDisplayBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (((choosenNote!="") && (choosenTypeNote!="")) || (choosenNote!=""))
		          {
					currentChord = l.chordDisplayCustumeView1.getCurrentChord(choosenNote, choosenTypeNote);
		            l.chordDisplayCustumeView1.displayCurrentChord(ChordsActivity.this, currentChord);
		            return;
		          }
		          Toast.makeText(ChordsActivity.this.getApplicationContext(), "Choose Tone and chord type to display chord diagram", 1).show();
			}
		});
    	l.iKnowBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if ((choosenNote != null) && (choosenTypeNote != null))
		          {
					currentChord = l.chordDisplayCustumeView1.getCurrentChord(choosenNote, choosenTypeNote);

					for(int i=0; i<Util.knownChordsList.size() ; i++)
					{
						if (currentChord.equals(Util.knownChordsList.get(i)))
			            {
			            	l.iKnowBtn.setText("X");
			            	Util.knownChordsList.add(currentChord);
			            	getknownChordsList();
			            	break;
			            }
					}
		              l.iKnowBtn.setText("V");
		              Util.knownChordsList.remove(currentChord);
		              getknownChordsList();
		          }
				else{Toast.makeText(ctx, "Choose Tone and chord type to display chord diagram", 1).show();}
		        }
			});
    }

  }


  Context ctx;
  Events e;
  Layout l;

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(R.layout.chords_activity);

    ctx = this;
    l = new Layout();
    e = new Events();

    getToneList();
    getTypeChordList();
    getknownChordsList();
  }

  protected void onStop()
  {
    super.onStop();
    l.root.toggleMenu();
  }

  protected void onPause()
  {
    super.onPause();
    Object localObject1 = "";
    for (int i = 0;i < Util.knownChordsList.size();i++)
    {
      Object localObject2 = localObject1;
      try
      {
        if (!((String)localObject1).contains(Util.knownChordsList.get(i).getString("symbol") + "&")) {
          localObject2 = localObject1 + Util.knownChordsList.get(i).getString("symbol") + "&";
        }
        localObject1 = localObject2;
      }
      catch (JSONException localJSONException)
      {
        localJSONException.printStackTrace();
        Object localObject3 = localObject1; /// wasent mogdar
      }
    }
    Object localObject3 = null;
    try
    {
      FileOutputStream localFileOutputStream = openFileOutput("KnownChords.txt", 2);
      localObject3 = localFileOutputStream;
    }
    catch (FileNotFoundException localFileNotFoundException)
    {
      try
      {
        ((OutputStreamWriter)localObject3).write((String)localObject1);
        ((OutputStreamWriter)localObject3).flush();
        ((OutputStreamWriter)localObject3).close();
        return;
      }
      catch (IOException localIOException)
      {
        localIOException.printStackTrace();
      }
      localFileNotFoundException = localFileNotFoundException;
      localFileNotFoundException.printStackTrace();
    }
    localObject3 = new OutputStreamWriter((OutputStream)localObject3);
  }

  public void toggleMenu(View paramView)
  {
    l.root.toggleMenu();
  }
  public boolean onCreateOptionsMenu(Menu paramMenu) {
    getMenuInflater().inflate(2131230721, paramMenu);
    return true;
  }

  private void getToneList() {
    int i = 0;
    while (i < toneStringArr.length)
    {
      CheckedTextView localCheckedTextView = new CheckedTextView(this);
      localCheckedTextView.setText(toneStringArr[i]);
      giveACheckedTextViewListener(localCheckedTextView, true);
      toneCheckedTextViewList.add(localCheckedTextView);
      l.toneDisplayLayout.addView(localCheckedTextView);
      i += 1;
    }
  }
  private void getTypeChordList() {
    int i = 0;
    while (i < chordTypeStringArr.length)
    {
      CheckedTextView localCheckedTextView = new CheckedTextView(this);
      localCheckedTextView.setText(chordTypeStringArr[i]);
      giveACheckedTextViewListener(localCheckedTextView, false);
      chordTypeCheckedTextViewList.add(localCheckedTextView);
      l.typeChordDisplayLayout.addView(localCheckedTextView);
      i += 1;
    }
  }
  private void getknownChordsList() {
    this.l.knownChordsLayout.removeAllViews();
    for (int i = 0;i < Util.knownChordsList.size();i++)
    {
	    TextView localTextView = new TextView(this);
	    try
	    {
	      localTextView.setText(Util.knownChordsList.get(i).getString("symbol"));
	      l.knownChordsLayout.addView(localTextView);
	    }
	    catch (JSONException localJSONException){localJSONException.printStackTrace();}
    }
  }
  private void giveACheckedTextViewListener(final CheckedTextView checkedTextView, final boolean isToneOrType) {
	  checkedTextView.setOnClickListener(new View.OnClickListener() {
          public void onClick(View paramAnonymousView) {
              if (isToneOrType) {
                  if (choosenNote == null) {
                      choosenNote = (String) checkedTextView.getText(); // choosenNote=(String) cb.getText();
                  } else {
                      setNewChoosenFromLists(isToneOrType);
                      choosenNote = (String) checkedTextView.getText();
                      checkedTextView.setChecked(true);
                  }
                  checkedTextView.setBackgroundColor(Color.YELLOW);
              } else {// if i don't want recode(code hozer) here, i need to know how to get String by reference
                  if (choosenTypeNote == null) {
                      choosenNote = (String) checkedTextView.getText();
                  } else {
                      setNewChoosenFromLists(isToneOrType);
                      choosenNote = (String) checkedTextView.getText();
                      checkedTextView.setChecked(true);
                  }
                  checkedTextView.setBackgroundColor(Color.YELLOW);
              }
          }
      });
  }
  private void setNewChoosenFromLists(boolean toneOrType) {


      // give all not-choosen from the list a transparent background
    if(toneOrType)
    {
        for(int i = 0 ; i< toneCheckedTextViewList.size() ; i++)
        {
            toneCheckedTextViewList.get(i).setBackgroundColor(0);
        }
    }
      else
    {
        for(int i = 0 ; i< chordTypeCheckedTextViewList.size() ; i++)
        {
            chordTypeCheckedTextViewList.get(i).setBackgroundColor(0);
        }
    }
  }
}





/* Location:           C:\Users\SharonasYKM\Desktop\classes-dex2jar.jar

 * Qualified Name:     com.example.miaooo.ChordsActivity

 * JD-Core Version:    0.7.0.1

 */