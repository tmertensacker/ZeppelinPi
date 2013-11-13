package pi;

import java.net.*;
import java.io.*;

public class Listener extends Thread
{
	private ServerSocket serverSocket;
	private Pi pi;
	
	public Listener(int port, Pi pi) throws IOException
	{
		this.pi = pi;
		serverSocket = new ServerSocket(port);
		serverSocket.setSoTimeout(10000);
	}
	public synchronized void run()
	{
		while(true){
			try{
				System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "...");
				Socket server = serverSocket.accept();
				System.out.println("Just connected to " + server.getRemoteSocketAddress());
				DataInputStream in = new DataInputStream(server.getInputStream());
				String inMsg = in.readUTF();
				OutputStream out = server.getOutputStream();
				DataOutputStream outData = new DataOutputStream(out);
				//TODO: alle commando's uitwerken!
				if(inMsg.equals("get height")){ //TODO: commando's aaneen schrijven
					float height = pi.getHeight();
					outData.writeUTF(Float.toString(height));
				}
				else if(inMsg.equals("get picture")){
					pi.takePicture();
					InputStream inFile = new FileInputStream("picture.jpg");                        
					copy(inFile, out);
					inFile.close();
				}
				else if(inMsg.equals("turnrightstart"))
					pi.turnRightStart();
				else if(inMsg.equals("turnrightstop"))
					pi.turnRightStop();
				else if(inMsg.equals("turnleftstart"))
					pi.turnLeftStart();
				else if(inMsg.equals("turnleftstop"))
					pi.turnLeftStop();
				else if(inMsg.contains("stayatheight")){
					String[] split = inMsg.split("\\s+");
					if(split.length == 2 ){
						System.out.println("stayatheight "+split[1]);
						double height = Double.parseDouble(split[1]);
						pi.goToHeight(height);
					}
					else{
						//onbekend commando
					}
				}
				/*else if(inMsg.contains("turnright")){
					String[] split = inMsg.split("\\s+");
					if(split.length > 2 )
						System.out.println(split[2]); //TODO: wordt 1.
					pi.turnRight(Integer.parseInt(split[2]));   
				}*/
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

}
