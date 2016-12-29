package lists;

import java.util.LinkedList;

import logic.Song;
import logic.SongVers;

//make Quotetion mark within the html string possible to put in a string (now i put \ before each mark manually to make this work)


public class SongList extends LinkedList<Song>
{
	/***
	 **
	 */
	public static SongList myList;
	private static boolean active = false;
	public static SongVers choosenSongVers;
	public static void init() 
	{
		if (active==false)
		{
			myList = new SongList();
			active = true;
			
			//Song allUneedIsLove = new Song(001, "All you nees is love", "The Beatles");
			//allUneedIsLove.getSongVersList().add(2, new SongVers("All you nees is love", 4, "file:///android_asset/pre.html" ,0012));
			//myList.add(allUneedIsLove);
			
			/*
	        String html = "<i></i>*******************<br>Banana Co <br>Radiohead<br><br> <br>Street Spirit CD <br>*******************<br><br><span>Em</span>        <span>C</span><br>Oh Banana Co<br>          <span>G</span>    <span>G/F#</span>       <span>G</span>    <span>G</span>/<span>F#</span><br>We really love you and we need you<br><br>    <span>Em</span>        <span>C</span><br>And oh Banana Co<br>            <span>G</span>    <span>G/F#</span>   <span>G</span>     <span>G</span>/<span>F#</span><br>We'd really love to believe you but<br><span>Cm</span>                     <span>G</span><br>Everything's burning down<br><span>Cm</span><br>We've got to put it out <br><br><span>C</span>     <span>C</span>   <span>C</span>/B  <span>Am7</span>  <span>G</span>   <br><br><br>****************<br>Comentarios a: jorgemp55@hotmail.com";
	        SongVers songVesr1 = new SongVers("banana co", html , 5);
	        Song song1 = new Song(1, "Banana Co", "Radiohead");
	        song1.getSongVersList().add(songVesr1);
	        SongList.myList.add(song1);
	        
	        String html2 = "<i></i>The Beatles - Elenor Rigby<br><br>www.Moment44.org<br><br>Like Our Band On Facebook! :) <br>www.Facebook.com/Moment44<br><br>And Check Us Out On Youtube! :)<br>www.youtube.com/Moment44<br><br>Don't Forget Twitter! :)<br>www.Twitter.com/Moment_44<br><br>Notes*: <br>   -When me and my band play we strum<br>   VERY staccato, short and muted quickly<br>   during the verses, with up strums<br><br>   -During the Pre-Chorus and Chorus<br>   we play nice and open.<br><br>   -A lot people like the \"Beatles Way\" <br>   better. In our band we have a keys player<br>   who hits the higher ends and the lower ends.<br><br><br>This is way my band plays it| Beatles way |<br>____________________________|_____________|<br>Chords   - <span>E</span>  <span>A</span>  <span>D</span>  <span>G</span>  <span>B</span>  <span>E</span> | <span>E</span> <span>A</span> <span>D</span> <span>G</span> <span>B</span> <span>E</span> |<br>C        - x  x  5  5  5  3 | X 3 2 0 1 0 |<br>Em       - x  x  5  4  5  3 | 0 2 2 0 0 0 |<br>Em7      - x  x  0  4  5  3 | 0 2 2 0 3 0 |<br>Em6      - x  4  x  4  5  3 | 0 2 2 0 2 0 |<br>Emaug    - x  3  x  4  5  3 | 0 2 2 0 1 0 |<br>Em*      - x  2  x  4  5  3 | 0 2 2 0 0 0 |<br>----------------------------|-------------|<br><br>Chorus:<br><span>C</span>                           <span>Em</span><br>Ahhh look at all the lonley people <br><span>C</span>                           <span>Em</span><br>Ahhh look at all the lonley people <br><br>Verse 1:<br><span>Em</span>           <br>Elenor Rigby picks up the rice in a church<br>                    <span>C</span> <br>Where a wedding has been been<br>            <span>Em</span><br>Living in a dream<br><br>Waits by the window, wearing the face<br>                            <span>C</span><br>She keeps in the jar by her door<br>          <span>Em</span><br>Who is it for?<br><br>Pre-Chorus:<br>Em7            Em6           Emaug            Em*<br>All the lonley peo-ple where do they all come from? <br>Em7            Em6           Emaug          Em*<br>All the lonley peo-ple where do they all be-long? <br><br>Verse 2:<br><span>Em</span><br>Father McKenzie writing the words of a sermon<br>                 <span>C</span><br>that no one will hear<br>             <span>Em</span><br>No one comes near<br><br>Look at him working. <br>                                                   <span>C</span><br>Darning his socks in the night when there's nobody there<br>             <span>Em</span><br>What does he care?<br><br>Pre-Chorus:<br>Em7            Em6           Emaug            Em*<br>All the lonley peo-ple where do they all come from? <br>Em7            Em6           Emaug          Em*<br>All the lonley peo-ple where do they all be-long? <br><br>Chorus:<br><span>C</span>                           <span>Em</span><br>Ahhh look at all the lonley people <br><span>C</span>                           <span>Em</span><br>Ahhh look at all the lonley people <br><br>Verse 3:<br><span>Em</span><br>Eleanor Rigby died in the church and <br>                               <span>C</span>        <span>Em</span><br>was buried along with her name   Nobody came<br><br>Father McKenzie wiping the dirt from his hands<br>                     <span>C</span><br>as he walks from the grave<br>           <span>Em</span><br>No one was saved<br><br>Pre-Chorus:<br>Em7            Em6           Emaug            Em*<br>All the lonley peo-ple where do they all come from? <br>Em7            Em6           Emaug          Em*<br>All the lonley peo-ple where do they all be-long? ";
	        SongVers songVesr2 = new SongVers("Elenor Rigby", html2 , 5);
	        Song song2 = new Song(2, "Elenor Rigby", "The Beatles");
	        song2.getSongVersList().add(songVesr2);
	        SongList.myList.add(song2);
	        */
		}
	}
	/***
	 **
	 */
}