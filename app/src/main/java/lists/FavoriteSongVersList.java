package lists;

import java.util.LinkedList;

import logic.SongVers;


public class FavoriteSongVersList extends LinkedList<SongVers>
{
	/***
	 **
	 */
	public static FavoriteSongVersList myList;
	private static boolean active = false;
	
	public static void init() 
	{
		if (active==false)
		{
			myList = new FavoriteSongVersList();
			active = true;
		}
	}
	
	
	 @Override
	  public boolean add(SongVers songVers) {
	    
		 for(int i=0; i<myList.size(); i++)
		 {
			 if (songVers.getId() == myList.get(i).getId()){
				 return false;
			 }
		 }
		 super.add(songVers);
		 return true;
	  }
	 
	 public void delete(SongVers songVers){
		 for(int i=0; i<myList.size(); i++)
		 {
			 if (songVers.getId() == myList.get(i).getId()){
				 myList.remove(i);
			 }
		 }
	 }
	/***
	 **
	 */
}
