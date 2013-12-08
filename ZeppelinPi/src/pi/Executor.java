package pi;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Executor implements Runnable{
	
	private LinkedList<String> queue;
	private boolean executing;
	private Pi pi;

	public Executor(Pi pi) {
		queue = new LinkedList<String>();
		executing = false;
		this.pi = pi;
	}
	
	public synchronized void run(){
		executing = true;
			while (executing) {
				if (! queue.isEmpty()) {
					String command = queue.poll();

					if (command.contains("goforward ")) {
						int constant = 100;
						List<String> strings = Arrays.asList(command.split("\\s+"));
						//for(int i = 0; i < strings.get(1)/100; i++);
						pi.forward(100);
						
						/*pi.forwardStart();
						try {
							Thread.sleep(Integer.parseInt(strings.get(1)));
						} catch(InterruptedException ex) {
						    Thread.currentThread().interrupt();
						}
						pi.forwardStop();*/
					}
					else if (command.contains("gobackward ")) {
						List<String> strings = Arrays.asList(command.split("\\s+"));
						pi.backwardStart();
						try {
							Thread.sleep(Integer.parseInt(strings.get(1)));
						} catch(InterruptedException ex) {
						    Thread.currentThread().interrupt();
						}
						pi.backwardStop();
					}
					else if (command.contains("turnleft ")) {
						List<String> strings = Arrays.asList(command.split("\\s+"));
						pi.turnLeftStart();
						try {
							Thread.sleep(Integer.parseInt(strings.get(1)));
						} catch(InterruptedException ex) {
						    Thread.currentThread().interrupt();
						}
						pi.turnRightStop();
					}
					else if (command.contains("turnright ")) {
						List<String> strings = Arrays.asList(command.split("\\s+"));
						pi.turnLeftStart();
						try {
							Thread.sleep(Integer.parseInt(strings.get(1)));
						} catch(InterruptedException ex) {
						    Thread.currentThread().interrupt();
						}
						pi.turnRightStop();
					}
					/*else if (command.contains("climb ")) {
						List<String> strings = Arrays.asList(command.split("\\s+"));
						pi.climbStart();
						try {
							Thread.sleep(Integer.parseInt(strings.get(1)));
						} catch(InterruptedException ex) {
						    Thread.currentThread().interrupt();
						}
						pi.climbStop();
					}
					else if ( command.contains("descend ")) {
						List<String> strings = Arrays.asList(command.split("\\s+"));
						pi.descendStart();
						try {
							Thread.sleep(Integer.parseInt(strings.get(1)));
						} catch(InterruptedException ex) {
						    Thread.currentThread().interrupt();
						}
						pi.descendStop();
					}*/
					/*else if(command.equals("forwardstart"))
						pi.forwardStart();
					else if(command.equals("forwardstop"))
						pi.forwardStop();
					else if(command.equals("backwardstart"))
						pi.backwardStart();
					else if(command.equals("backwardstop"))
						pi.backwardStop();
					else if(command.equals("climbstart"))
						pi.climbStart();
					else if(command.equals("climbstop"))
						pi.climbStop();
					else if(command.equals("descendstart"))
						pi.descendStart();
					else if(command.equals("descendstop"))
						pi.descendStop();
					else if(command.equals("turnrightstart"))
						pi.turnRightStart();
					else if(command.equals("turnrightstop"))
						pi.turnRightStop();
					else if(command.equals("turnleftstart"))
						pi.turnLeftStart();
					else if(command.equals("turnleftstop"))
						pi.turnLeftStop();
					else if(command.contains("stayatheight")){
						String[] split = command.split("\\s+");
						if(split.length == 2 ){
							System.out.println("stayatheight "+split[1]);
							double height = Double.parseDouble(split[1]);
							pi.goToHeight(height);
						}
						else{
							//onbekend commando
						}
					}*/
					else {
						// commando niet gevonden/verwerkt!
					}
				}
			}
		
	}
	
	public void addCommand(String command) {
		queue.offer(command);
	}
	
	public void stopExecuting() {
		this.executing = false;
	}
	
	public void startExecuting() {
		this.executing = true;
	}
	
	public void clearQueue() {
		queue.clear();
	}
}