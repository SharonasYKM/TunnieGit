package com.example.tunnie;

import android.widget.LinearLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

public class Util
{
  public static LinkedList<JSONObject> knownChordsList = new LinkedList();
  
  private static int compareTonesToKnown(String[] paramArrayOfString)
  {
    String[] arrayOfString = getKnownChords();
    int j = paramArrayOfString.length;
    int i = 0;
    if (i < paramArrayOfString.length)
    {
      int n = 0;
      int m = 0;
      for (;;)
      {
        int k = n;
        if (m < arrayOfString.length)
        {
          if (arrayOfString[m].equals(paramArrayOfString[i])) {
            k = 1;
          }
        }
        else
        {
          m = j;
          if (k != 0) {
            m = j - 1;
          }
          i += 1;
          j = m;
          break;
        }
        m += 1;
      }
    }
    return j;
  }
  
  public static JSONObject getCurrentChord(String paramString)
  {
    int i = 0;
    while (i < MainActivity.chordsDB.length()) {
      try
      {
        if ((MainActivity.chordsDB.getJSONObject(i).getString("symbol").contains(paramString)) || (MainActivity.chordsDB.getJSONObject(i).getJSONArray("alternativeSymbols").getString(0).contains(paramString)))
        {
          JSONObject localJSONObject = MainActivity.chordsDB.getJSONObject(i);
          return localJSONObject;
        }
      }
      catch (JSONException localJSONException)
      {
        localJSONException.printStackTrace();
        i += 1;
      }
    }
    return null;
  }
  
  public static String[] getKnownChords()
  {
    String[] arrayOfString = new String[knownChordsList.size()];
    for (int i = 0 ;i < arrayOfString.length; i++)
    {
        try
        {
          arrayOfString[i] = ((JSONObject)knownChordsList.get(i)).getString("symbol").trim();
        }
        catch (JSONException localJSONException){localJSONException.printStackTrace();}
    }
    return arrayOfString;
  }
  
  static String[] splitSymbolToToneNType(String paramString)
  {
    paramString.replaceAll(" ", "");
    String[] arrayOfString = new String[2];
    if (paramString.substring(1, 2) == "#")
    {
      arrayOfString[0] = paramString.substring(0, 2);
      return arrayOfString;
    }
    if (paramString.substring(0, 2) == "b")
    {
      arrayOfString[1] = paramString.substring(2);
      return arrayOfString;
    }
    arrayOfString[0] = paramString.substring(0, 1);
    arrayOfString[1] = paramString.substring(1);
    return arrayOfString;
  }
  
  void setTranspositionedStringArr(String[] paramArrayOfString, LinearLayout paramLinearLayout)
  {
    int i = 0;
    while (i < paramArrayOfString.length) {
      i += 1;
    }
  }
}



/* Location:           C:\Users\SharonasYKM\Desktop\classes-dex2jar.jar

 * Qualified Name:     com.example.miaooo.Util

 * JD-Core Version:    0.7.0.1

 */