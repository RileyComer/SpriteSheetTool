package Graphics;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JFrame;

public class Game {
	
	public static void main(String[] args) throws IOException  {
		//Sprite stuff
		boolean edit=true;
		String fileName="Test";
		
		int spriteWidth=20;
		int spriteHeight=20;
		int sheetWidth=3;
		int sheetHeight=1;
		int inputWidth=20;//27
		int inputHeight=20;//27
		
		File editFile=new File("C:\\Users\\Riley\\Desktop\\Sprites\\"+fileName+".png");
		if(!edit) {
			editFile=null;
		}
		
		// Creates Window
		Gui window = new Gui ( spriteWidth, spriteHeight, sheetWidth, sheetHeight,inputWidth,inputHeight,editFile,fileName);
		window.setExtendedState(JFrame.MAXIMIZED_BOTH); window.setUndecorated(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
		
		while(true) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException ie){
				System.out.println("Scanning...");
			}
			window.redraw();
		}
	}
	
	private static void playSound(File Sound) {
		try {
			/*
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(Sound));
			clip.start();
			*/
		}catch(Exception e) {
			
		}
	}

}
