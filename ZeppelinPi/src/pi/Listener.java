package pi;

import java.net.*;
import java.util.Arrays;
import java.util.List;
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
				if(inMsg.equals("getheight")){
					float height = pi.getHeight();
					outData.writeUTF(Float.toString(height));
				}
				else if(inMsg.equals("takepicture")){
					pi.takePicture();
					InputStream inFile = new FileInputStream("picture.jpg");                        
					copy(inFile, out);
					inFile.close();
				}
				else if(inMsg.equals("getpistate")) {
					String result = pi.getPiState();
					outData.writeUTF(result);
				}
				else if (inMsg.contains("forward ")) {
					List<String> strings = Arrays.asList(inMsg.split("\\s+"));
					pi.forward(Integer.parseInt(strings.get(1)));
				}
				else if (inMsg.contains("backward ")) {
					List<String> strings = Arrays.asList(inMsg.split("\\s+"));
					pi.backward(Integer.parseInt(strings.get(1)));
				}
				else if (inMsg.contains("climb ") || inMsg.contains("descend ")) {
					List<String> strings = Arrays.asList(inMsg.split("\\s+"));
					pi.goToHeight(Integer.parseInt(strings.get(1)));
				}
				else if(inMsg.equals("forwardstart"))
					pi.forwardStart();
				else if(inMsg.equals("forwardstop"))
					pi.forwardStop();
				else if(inMsg.equals("backwardstart"))
					pi.backwardStart();
				else if(inMsg.equals("backwardstop"))
					pi.backwardStop();
				else if(inMsg.equals("climbstart"))
					pi.climbStart();
				else if(inMsg.equals("climbstop"))
					pi.climbStop();
				else if(inMsg.equals("descendstart"))
					pi.descendStart();
				else if(inMsg.equals("descendstop"))
					pi.descendStop();
				else if(inMsg.equals("turnrightstart"))
					pi.turnRightStart();
				else if(inMsg.equals("turnrightstop"))
					pi.turnRightStop();
				else if(inMsg.equals("turnleftstart")){
					pi.turnLeftStart();
					System.out.println("turnleft start received");
				}
				else if(inMsg.equals("turnleftstop")){
					pi.turnLeftStop();
					System.out.println("turnleft start received");
				}
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
