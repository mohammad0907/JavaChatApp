/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;


import java.io.*; 
import java.net.*; 
import java.util.Scanner; 

public class Client { 
	final static int server = 1000; 

	public static void main(String args[]) throws UnknownHostException, IOException { 
		Scanner inputMessage = new Scanner(System.in); 
		InetAddress IPAddress = InetAddress.getByName("localhost"); 
		Socket socket = new Socket(IPAddress, server); 
		DataInputStream input = new DataInputStream(socket.getInputStream()); 
		DataOutputStream output = new DataOutputStream(socket.getOutputStream()); 

		Thread messageSend = new Thread(new Runnable() { 
			@Override
			public void run() { 
				while (true) { 

					
					String sendMessage = inputMessage.nextLine(); 
					
					try { 
						// write on the output stream 
						output.writeUTF(sendMessage); 
                                                System.out.println("You: " + sendMessage);
					} catch (IOException e) { 
						e.printStackTrace(); 
					} 
				} 
			} 
		}); 
		
		
		Thread messageRecieve = new Thread(new Runnable() 
		{ 
			@Override
			public void run() { 

				while (true) { 
					try { 
                                            System.out.println("Friend: " + input.readUTF()); 
					} catch (IOException e) { 

						e.printStackTrace(); 
					} 
				} 
			} 
		}); 

		messageSend.start(); 
		messageRecieve.start(); 

	} 
} 
