
package server;

import java.io.*; 
import java.util.*; 
import java.net.*; 


public class Server { 


	static ArrayList<HandleUsers> clientList = new ArrayList<HandleUsers>(); 
	
	
	static int usersCounter = 0; 

	public static void main(String[] args) throws IOException { 
		
		ServerSocket serverSocket = new ServerSocket(1000); 
		
		Socket socket; 
		
		
		while (usersCounter < 2) { 
			
			socket = serverSocket.accept(); 

			//System.out.println("New client request received : " + socket); 
			
		
			DataInputStream input = new DataInputStream(socket.getInputStream()); 
			DataOutputStream output = new DataOutputStream(socket.getOutputStream());  
			HandleUsers newUser = new HandleUsers(socket,"client " + usersCounter, input, output); 

			
			Thread thread = new Thread(newUser); 
			
			System.out.println("new client added"); 
			clientList.add(newUser); 
			thread.start(); 
			usersCounter++; 

		} 
	} 
} 


class HandleUsers implements Runnable { 
	Scanner messageInput = new Scanner(System.in); 
	private String userName; 
	final DataInputStream input; 
	final DataOutputStream output; 
	Socket socket; 
	boolean status; 
	
	// constructor 
	public HandleUsers(Socket socket, String userName, DataInputStream input, DataOutputStream output) { 
		this.status = true; 
		this.userName = userName; 
		this.socket = socket; 
                this.input = input; 
		this.output = output;
		
	} 

	@Override
	public void run() { 

		String message; 
		while (true) 
		{ 
			try
			{ 
				 
				message = input.readUTF(); 

				String sendTo = (this.userName.equals("client 0") ? "client 1" : "client 0");
                                
				System.out.println(message + " - " + this.userName ); 
				
				if(message.equals("logout")){ 
					this.status=false; 
					this.socket.close(); 
					break; 
				} 
				

                                for(int i = 0; i < Server.clientList.size(); i++){
                                    if(Server.clientList.get(i).status && Server.clientList.get(i).userName.equals(sendTo)){
                                        Server.clientList.get(i).output.writeUTF(message);
                                    }
                                }
			} catch (IOException e) { 
				
				e.printStackTrace(); 
			} 
			
		} 
		try
		{ 
			
			this.input.close(); 
			this.output.close(); 
			
		}catch(IOException e){ 
			e.printStackTrace(); 
		} 
	} 
} 
