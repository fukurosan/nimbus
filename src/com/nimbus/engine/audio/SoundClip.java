package com.nimbus.engine.audio;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundClip {
	
	private Clip clip = null;
	private FloatControl gainControl;

	public SoundClip(String path) {
		try {
			InputStream audioSource = SoundClip.class.getResourceAsStream(path);
			InputStream bufferedIn = new BufferedInputStream(audioSource);
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(bufferedIn);
			AudioFormat baseFormat = audioInputStream.getFormat();
			AudioFormat decodeFormat = new AudioFormat(
					AudioFormat.Encoding.PCM_SIGNED, 
					baseFormat.getSampleRate(), 
					16, 
					baseFormat.getChannels(), 
					baseFormat.getChannels() * 2, 
					baseFormat.getSampleRate(), 
					false
					);
			AudioInputStream decodedAudioInputStream = AudioSystem.getAudioInputStream(decodeFormat, audioInputStream);
			
			clip = AudioSystem.getClip();
			clip.open(decodedAudioInputStream);
			
			gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			
		} 
		catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			e.printStackTrace();
		}

	}
	
	public void play() {
		//Clip should never be able to be null, but just for good measure.
		if(clip == null) {
			return;
		}
		stop();
		clip.setFramePosition(0);
		//Strange, but sometimes the sound won't play. This just forces it to happen.
		while(!clip.isRunning()) {
			clip.start();
		}
	}
	
	public void loop() {
		clip.loop(Clip.LOOP_CONTINUOUSLY);
		play();
	}
	
	public void stop() {
		if(clip.isRunning()) {
			clip.stop();
		}
	}
	
	public void close() {
		stop();
		clip.drain();
		clip.close();
	}
	
	//The volume is actually gain in this case, so the output is described in Db
	public void setVolume(float volumeDecibels) {
		gainControl.setValue(volumeDecibels);
	}
	
	public boolean isRunning() {
		return clip.isRunning();
	}

}
