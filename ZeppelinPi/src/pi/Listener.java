package pi;

import java.net.*;
import java.io.*;

public class Listener extends Thread
{
	private ServerSocket serverSocket;
   
	public Listener(int port) throws IOException
	{
		serverSocket = new ServerSocket(port);
		serverSocket.setSoTimeout(10000);
	}
	public void run()
	{
		while(true){
			try{
				System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "...");
				Socket server = serverSocket.accept();
				System.out.println("Just connected to " + server.getRemoteSocketAddress());
				DataInputStream in = new DataInputStream(server.getInputStream());
				String inMsg = in.readUTF();
				OutputStream out = server.getOutputStream();
				if(inMsg.equals("get distance")){
					DistanceMonitor monitor = new DistanceMonitor();
					// Zoek de mediaan van N measurements...
					float distance = monitor.measureDistance();
					//out.writeUTF(Float.toString(distance));
				}
				else if(inMsg.equals("get picture")){
					Camera camera = new Camera();
					camera.makePicture();
					InputStream inFile = new FileInputStream("picture.jpg");                        
					copy(inFile, out);
					inFile.close();
				}
				else if(inMsg.equals("start motor up")){
					
					//out.writeUTF("starting motor...");          		
				}
				else{
					//out.writeUTF("error: unknown command");				
				}
				server.close();
			}
			catch(SocketTimeoutException s){
				System.out.println("Socket timed out!");
				break;
			}
			catch(IOException e){
				e.printStackTrace();
				break;
			}
		}
	}

	public static void copy(InputStream in, OutputStream out) throws IOException {
		byte[] buf = new byte[8192];
		int len = 0;
		while ((len = in.read(buf)) != -1) {
			out.write(buf, 0, len);
		}
	}

	public static void main(String [] args)
	{
		int port = Integer.parseInt(args[0]); 	
		try{
	 		Thread t = new Listener(port);
			t.start();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
}
