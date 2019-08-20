package model;


public class Karaoke {

	public static void main(String[] args) 
	{
		SongBook songBook = new SongBook();
		songBook.importFrom("songs.txt");
		System.out.println(System.getProperty("java.class.path"));
		
		
		KaraokeMachine machine = new KaraokeMachine(songBook);
		machine.run();
		System.out.println("Saving song book....");
		songBook.exportTo("songs.txt");
		
		
//*****************//Testing**************************************************************************************************
//
		
	}
}
