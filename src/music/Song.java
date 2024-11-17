package music;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import player.audio.Output;

public class Song {
	private String songName;
	private File songFile = null;
	private AudioInputStream audio = null;
	private Clip track = null;
	
	private int trackPositionInSeconds;
	private int trackLengthInSeconds;
	
	public Song(File song) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		fileToSong(song);
		getTitle();
	}
	
//	public Song(File song, String name) {
//		String path = song.getParent();
//	}
	
	private void fileToSong(File song) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		if (isAudio(song)) {
			songFile = song;
			audio = AudioSystem.getAudioInputStream(songFile);
			track = AudioSystem.getClip();
			return;
		} //else throw new UnsupportedAudioFileException(song.getName() + " in " + song.getParent() + " is not a supported audio file!");
	}
	
//	private int getTrackLengthInSeconds() {
//		return (int) track.getMicrosecondLength() / 1000000;
//	}
	
//	public void setTrackPositionInSeconds(Output output) {
//		output.getTrack().getMicrosecondPosition();
//	}
	
	public Clip getTrack() {
		return track;
	}
	
	public File getFile() {
		return songFile;
	}
	
	public AudioInputStream getAudio() {
		return audio;
	}
	
	public String getTitle() {
		songName = songFile.getName().replace(".wav", "");
		songName = songName.replace("_", " ");
		return songName;
	}
	
	public void renameSong(String title) {
		songName = title;
		songFile.renameTo(new File(songFile.getParent() + "/" + title));
	}
	
	private boolean isAudio(File song) {
		//only supporting .wav files at the moment
		if (song.getName().endsWith(".wav")) return true;
		else return false;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Song)) return false;
		
		Song song = (Song) obj;
		
		if (!getTitle().equals(song.getTitle())) return false;
		
		return true;
	}
}
