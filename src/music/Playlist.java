package music;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Playlist extends HashMap<Song, Integer> {
	private String playlistName;
	private File rootDirectory = new File(System.getProperty("user.dir") + "/SampleLibrary");
//	private HashMap<Song, Integer> playlist;
	
	public Playlist() {
		super();
		createPlaylist();
	}
	
	public Playlist(File directory) {
		super();
		rootDirectory = directory;
		createPlaylist();
	}
	
	private void createPlaylist() {
		playlistName = rootDirectory.getName();
		int counter = 1;
				
		for (File potentialSong : rootDirectory.listFiles()) {
			try {
				this.put(new Song(potentialSong), counter);
				counter++;
			} catch (UnsupportedAudioFileException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
		
	
	public String getTitle() {
		return playlistName;
	}
	
	public File getDirectory() {
		return rootDirectory;
	}
	
	public void renamePlaylist(String name) {
		String newDirectory = rootDirectory.getAbsolutePath().replace(playlistName, name);
		rootDirectory.renameTo(new File(newDirectory));
		playlistName = name;
		
	}
	
//	public HashMap<Song, Integer> getPlaylist() {
//		return playlist;
//	}
	
	public void addSong(File song) {
		//by default this method will add a song to the end of a playlist
		
		try {
			this.put(new Song(song), this.size() + 1);
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void changeSongPosition(String title, int newLocation) throws FileNotFoundException {
		Song song_ = null;
		
		for (Entry<Song, Integer> song : this.entrySet()) {
			if (song.getKey().getTitle().equals(title))  {
				song_ = song.getKey();
				break;
			}
		}
			
		if (song_ == null) return; //throw new FileNotFoundException(title + " doesn't exist!");
		
		
		for (Entry<Song, Integer> song : this.entrySet())
			if (song.getValue() >= newLocation) song.setValue(song.getValue() + 1);
		this.put(song_, newLocation);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Playlist)) return false;
		
		Playlist p = (Playlist) obj;
		
		if (p.getDirectory() != rootDirectory) return false;
		
		return true;
	}
}
