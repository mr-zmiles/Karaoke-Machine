package model;

public class Song 
{
	protected String artist;
	protected String title;
	protected String videoURL;
	
	public Song(String artist, String title, String videoURL)
	{
		this.artist = artist;
		this.title = title;
		this.videoURL = videoURL;
	}

	public String getArtist() {
		return artist;
	}

	public String getTitle() {
		return title;
	}

	public String getVideoURL() {
		return videoURL;
	}
	
	@Override
	public String toString()
	{
		return String.format(" %s by %s", 
				title, artist);
	}
	
}
