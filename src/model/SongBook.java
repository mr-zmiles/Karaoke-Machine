package model;
import java.util.*;
import java.io.*;

public class SongBook 
{
	private List<Song> mSongs;
	
	public SongBook()
	{
		mSongs = new ArrayList<Song>();
	}
	
	//Add ability to export our SongBook to an external file
	public void exportTo(String fileName)
	{
		try(
				FileOutputStream fos = new FileOutputStream(fileName);
				PrintWriter writer = new PrintWriter(fos);
					)
		{
			//Iterate through all the current songs in the songBook, and export
			//them to file
			for(Song song: mSongs)
			{
				writer.printf("%s|%s|%s%n", song.getArtist(), 
						song.getTitle(), song.getVideoURL());
			}
		}
		catch(IOException ioe)
		{
			System.out.printf("Problems saving %s %n", fileName);
			ioe.printStackTrace();
		}
	}
	
	//Custom Serialization 
	public void importFrom(String fileName)
	{
		try(
				FileInputStream fis = new FileInputStream(fileName);
				BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
				)
		{
			String line = "";
			while((line = reader.readLine()) != null)
			{
				String[] args = line.split("\\|");
				addSong(new Song(args[0], args[1], args[2]));
			}
		}
		catch(IOException ioe)
		{
			System.out.printf("Problems loading %s %n", fileName);
			ioe.printStackTrace();
		}
	}
	
	
	public void addSong(Song song)
	{
		mSongs.add(song);
	}

	public int getSongCount()
	{
		return mSongs.size();
	}
	
	// FIXME: This should be cached!
	private Map<String, List<Song>> byArtist()
	{
		Map<String, List<Song>> byArtist = new TreeMap<String, List<Song>>();
		for(Song song : mSongs)
		{
			//See if artist is already a key in the map, if so we will get the list
			//rep of their songs
			List<Song> artistSongs = byArtist.get(song.getArtist());
			//if artist wasn't previously in byArtist Map
			if(artistSongs == null)
			{
				artistSongs = new ArrayList<>();
				//put in artist's name and new list of songs
				byArtist.put(song.getArtist(), artistSongs);
			}
			//To new list we're going to add the song, or to the existing list
			artistSongs.add(song);
		}
		return byArtist;
	}
	
	public Set<String> getArtists()
	{
		return byArtist().keySet();
	}
	
	public List<Song> getSongsForArtist(String artist)
	{
		List<Song> songs = byArtist().get(artist);
		//Anon in-line class example
		songs.sort(new Comparator<Song>() 
		{
			@Override
			public int compare(Song song1, Song song2)
			{
				if(song1.equals(song2))
					return 0;
				return song1.title.compareTo(song2.title);
			}
		});
		return songs;
	}
}
