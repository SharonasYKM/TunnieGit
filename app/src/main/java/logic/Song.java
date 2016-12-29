package logic;


import java.util.LinkedList;

public class Song {

	private int id;
	private String songName;
	private String songArtist;
	private int versAmount = 1;
	private LinkedList<SongVers> songVersList = new LinkedList<SongVers>();
	


	
	public Song(int id, String songName, String songArtist)
	{
		this.setId(id);
		this.songName = songName;
		this.songArtist = songArtist;
	}
	
	public Song(String songName, String songArtist)
	{
		this.songName = songName;
		this.songArtist = songArtist;
	}
	
	
	public String getSongName() {
		return songName;
	}

	public void setSongName(String songName) {
		this.songName = songName;
	}

	public String getSongArtist() {
		return songArtist;
	}

	public void setSongArtist(String songArtist) {
		this.songArtist = songArtist;
	}

	public LinkedList<SongVers> getSongVersList() {
		return songVersList;
	}

	public void setSongVersList(LinkedList<SongVers> songVersList) {
		this.songVersList = songVersList;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}

	public int getVersAmount() {
		return versAmount;
	}

	public void setVersAmount(int versAmount) {
		this.versAmount = versAmount;
	}
}
