package music;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;

import player.audio.Output;

public class Queue {
	private ArrayList<Entry<Song, Integer>> queue;
	
	private Output audio;
	private int currentTrack = 0;
	private int nextTrack;
	private int previousTrack;
	private boolean shuffle = false;
	
	public static enum loop {
		NONE,
		TRACK,
		QUEUE
	}
	
	private loop currentLoop = loop.QUEUE;
	
	
	public Queue() {
		//creates an empty queue
		queue = new  ArrayList<>();
		audio = Output.getOutput();
	}
	
	public Queue(Playlist p) {
		this();
		setQueue(p);
	}
	
	public ArrayList<Entry<Song, Integer>> getQueue() {
		return queue;
	}
	
	public int getCurrentTrack() {
		return currentTrack;
	}
	
	private void setQueue(Playlist p) {
		//sort
		
		for (int i = 1; i <= p.size(); i++) {
			for (Entry<Song, Integer> song : p.entrySet()) {
				if (song.getValue() == i) {
					queue.add(song);
					break;
				}
			}
		}
		
		currentTrack = 0;
		updateTrackPosition();
	}
	
	public void selectTrack(int index) {
		if (index - 1 >= 0) currentTrack = index - 1;
		updateTrackPosition();
//		play();
	}
	
	private void updateTrackPosition() {
		previousTrack = currentTrack - 1;
		nextTrack = currentTrack + 1;
	}
	
	private void sortPlaylist(int order[]) {
		//an order of 1 4 2 3 should play tracks with the labels in that order
		
		//pass in 'null' to sort it in its normal order
		if (order == null) {
			order = new int[queue.size()];
			for (int i = 0; i < queue.size(); i++) {
				order[i] = i + 1;
			}
		}
		
		ArrayList<Entry<Song, Integer>> newQueue = new ArrayList<>();
		while (newQueue.size() != queue.size()) {
			for (int i : order) {
				for (Entry<Song, Integer> song : queue) 
					if (song.getValue() == i) newQueue.add(song);
			}
		}
		
		queue = newQueue;
	}
	
	public void play() {
		try {
			audio.play(queue.get(currentTrack).getKey());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void resume() {
		try {
			audio.resume();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void pause() {
		audio.pause();
	}
	
	public void skipForward() {
		if (nextTrack > queue.size() - 1 && (getLoopStatus() == loop.QUEUE || getLoopStatus() == loop.TRACK)) {
			pause();
			selectTrack(1);
			play();
			return;
		} else if (nextTrack > queue.size() - 1) return;
	
		pause();
		currentTrack = nextTrack;
		updateTrackPosition();
		play();
	}
	
	public void skipBackward() {
		if (previousTrack < 0 && (getLoopStatus() == loop.QUEUE || getLoopStatus() == loop.TRACK)) {
			pause();
			selectTrack(queue.size());
			play();
			return;
		} else if (previousTrack < 0) return;
		
		pause();
		currentTrack = previousTrack;
		updateTrackPosition();
		play();
	}
	
	public void toggleShuffle() {
		shuffle = !shuffle;
		if (shuffle) sortPlaylist(generateRandomOrder());
		else sortPlaylist(null);
	}
	
	public boolean getShuffleStatus() {
		return shuffle;
	}
	
	private int[] generateRandomOrder() {
		Random rand = new Random();
		int order[] = new int[queue.size()];
		for (int i : order) i = 0;
		
		for (int i = 0; i < order.length;) {
			int next = rand.nextInt(order.length) + 1;
			if (hasDuplicate(next, order)) continue;
			order[i] = next;
			i++;
		}
		
		return order;
	}
	
	private boolean hasDuplicate(int potential, int list[]) {
		for (int i : list) 
			if (i == potential) return true;
		
		return false;
	}
	
	public void toggleLoop() {
		if (currentLoop == loop.TRACK) currentLoop = loop.NONE;
		else if (currentLoop == loop.NONE) currentLoop = loop.QUEUE;
		else if (currentLoop == loop.QUEUE) currentLoop = loop.TRACK;
	}
	
	public loop getLoopStatus() {
		return currentLoop;
	}
	
	public void clear() {
		queue = null;
		audio = null;
	}
}

