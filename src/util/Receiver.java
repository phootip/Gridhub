package util;

import java.io.BufferedReader;
import java.io.IOException;

import util.InputManager;

public class Receiver extends Thread{
	BufferedReader in;
	String temp;
	String[] arr;
	
	public Receiver(BufferedReader in){
		this.in = in;
	}
	
	@Override
	public void run(){
		try {
			while(true){
				System.out.println("in loop");
				temp = in.readLine();
				System.out.println("from server: " + temp);
				
				arr = temp.split(" ");
				if(arr[0].equals("keypress")){
					System.out.println("adding key press: " + arr[1]);
					InputManager.getInstance().addKeyPress(Integer.parseInt(arr[1]));
				}else if(arr[0].equals("keyrelease")){
					System.out.println("adding key release: " + arr[1]);
					InputManager.getInstance().addKeyRelease(Integer.parseInt(arr[1]));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
