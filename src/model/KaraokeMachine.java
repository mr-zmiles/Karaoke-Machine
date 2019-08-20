package model;
import java.io.*;
import java.util.*;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class KaraokeMachine 
{
	private SongBook songBook;
	private BufferedReader reader;
	private Map<String, String> menu;
	private Queue<Song> songQueue;
	
	public KaraokeMachine(SongBook songBook)
	{
		this.songBook = songBook;
		reader = new BufferedReader(new InputStreamReader
				(System.in));
		songQueue = new ArrayDeque<Song>();
		menu = new HashMap<String, String>();
		menu.put("add", "Add a new song to the song book");
		menu.put("play", "Play the next song in the queue");
		menu.put("choose", "Choose a song to sing!");
		menu.put("quit", "Give up. Exit the program");
	}
	
	//Using throws we bubble up exception for another method
	//to handle
	private String promptAction() throws IOException
	{
		if(songBook.getSongCount() == 1)
			System.out.printf("There is %d song available and %d songs queued."
					+ "Your options are: %n", songBook.getSongCount(), songQueue.size());
		else
		{
			System.out.printf("There are %d songs available and %d songs queued."
				+ "Your options are: %n", songBook.getSongCount(), songQueue.size());
		}
		for(Map.Entry<String, String> entry: menu.entrySet())
			System.out.printf("%s - %s %n", entry.getKey(), entry.getValue());
	
		System.out.print("What do you want to do:  ");
		String choice = reader.readLine();
		return choice.trim().toLowerCase();
	}
	
	//Loop until we get the value that we want out here
	public void run()
	{
		String choice = "";
		//runs as long as choice is not equal to quit
		do
		{
			try
			{
			//promptAction() throws IOException if there is a problem
			choice = promptAction();
			switch(choice)
			{
				case "add":
					Song song = promptNewSong();
					songBook.addSong(song);
					System.out.printf("%s added! %n%n", song);
					break;
				case "choose":
					String artist = promptArtist();
					Song artistSong = promptSongForArtist(artist);
					songQueue.add(artistSong);
					System.out.printf("You chose: %s, %n", artistSong);
					break;
				case "play":
					playNext();
					break;
				case "quit":
					System.out.println("Thanks for playing!");
					break;
				default:
					System.out.printf("Unknown choice: %s. Try again. %n%n", choice);
			}
			}
			catch(IOException ioe)
			{
				System.out.println("Problem with input");
				ioe.printStackTrace();
			}
		}while(!choice.equals("quit"));
	}
	
	private Song promptNewSong() throws IOException
	{
		System.out.print("Enter the artist's name:   ");
		String artist = reader.readLine();
		System.out.print("Enter the title:   ");
		String title = reader.readLine();
		System.out.print("Enter the video URL:   ");
		String videoURL = reader.readLine();
		return new Song(artist, title, videoURL);
	}
	
	private String promptArtist() throws IOException
	{
		System.out.println("Available artists:");
		List<String> artists = new ArrayList<>(songBook.getArtists());
		int index = promptForIndex(artists);
		return artists.get(index);
	}
	
	private Song promptSongForArtist(String artist) throws IOException
	{
		System.out.println("\nAvailable songs for " + artist);
		List<Song> songs = new ArrayList<>(songBook.getSongsForArtist(artist));
		List<String> songTitles = new ArrayList<>();
		for(Song song: songs)
			songTitles.add(song.getTitle());
		int index = promptForIndex(songTitles);
		return songs.get(index);
	}
	
	//Format options list for user and return the option that they user chose
	private int promptForIndex(List<String> options) throws IOException
	{
		int counter = 1;
		for(String option : options)
		{
			System.out.printf("%d.) %s %n", counter, option);
			counter++;
		}
		System.out.print("Please choose the number of one of the available options:");
		String optionAsString = reader.readLine();
		int choice = Integer.parseInt(optionAsString.trim());
		return choice - 1;
	}
	
	//Play next song in songQueue
	public void playNext()
	{
		Song song = songQueue.poll();
		if(song == null)
		{
			System.out.println("\n\nSorry there are no songs in the queue. Use "
					+ "choose from the menu to add some!");
		}
		else
		{
			System.out.printf("%n%n%nOpen %s to hear %s by %s %n%n%n", song.getVideoURL(),
					song.getTitle(), song.getArtist());
			playYouTubeVideo(song.getVideoURL());
		}
		
	}
	
	private void playYouTubeVideo(String url)
	{
        if(Desktop.isDesktopSupported())
        {
            Desktop desktop = Desktop.getDesktop();
            try 
            {
                desktop.browse(new URI(url));
            } 
            catch (IOException | URISyntaxException e) 
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        else
        {
            Runtime runtime = Runtime.getRuntime();
            try 
            {
                runtime.exec("xdg-open " + url);
            } catch (IOException e) 
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
	}	
}
