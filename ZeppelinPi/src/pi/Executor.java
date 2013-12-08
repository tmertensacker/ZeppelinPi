package pi;


import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Executor implements Runnable{
	
	private LinkedList<String> queue;
	private boolean executing;
	private Pi pi;
	private int forwardOn;
	private int forwardOff;
	private int backwardOn;
	private int backwardOff;
	private int turnforwardOn;
	private int turnbackwardOnExtraTime;
	private int turnOff;

	public Executor(Pi pi) {
		queue = new LinkedList<String>();
		executing = false;
		this.pi = pi;
		forwardOn = 100;
		forwardOff = 200;
		backwardOn = 200;
		backwardOff = 100;
		turnforwardOn = 80;
		turnbackwardOn = 180;
		turnOff = 200;
	}
	
	public synchronized void run(){
		executing = true;
			while (executing) {
				if (! queue.isEmpty()) {
					String command = queue.poll();

					if (command.contains("goforward ")) {
						List<String> strings = Arrays.asList(command.split("\\s+"));
						pi.forwardStart();
						try {
							Thread.sleep(Integer.parseInt(strings.get(1)));
						} catch(InterruptedException ex) {
						    Thread.currentThread().interrupt();
						}
						pi.forwardStop();
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
					else if (command.contains("climb ")) {
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
					}
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
	
	public void forwardPulse(int amount) {
		for (int i = 0; i < amount; i++) {
			pi.forwardStart();
			waitForXMillis(forwardOn);
			pi.forwardStop();
			waitForXMillis(forwardOff);
		}
	}
	
	public void backwardPulse(int amount) {
		for (int i = 0; i < amount; i++) {
			pi.backwardStart();
			waitForXMillis(backwardOn);
			pi.backwardStop();
			waitForXMillis(backwardOff);
		}
	}
	
	public void turRightPulse(int amount) {
		for (int i = 0; i < amount; i++) {
			pi.getRightMotor().triggerBackwardOn();
			waitForXMillis(turnbackwardOnExtraTime);
			pi.getLeftMotor().triggerForwardOn();
			waitForXMillis(turnforwardOn);
			pi.getLeftMotor().triggerForwardOff();
			waitForXMillis(turnbackwardOnExtraTime);
			pi.getRightMotor().triggerBackwardOff();
			waitForXMillis(turnOff);
		}
	}
	
	public void turLeftPulse(int amount) {
		for (int i = 0; i < amount; i++) {
			pi.getLeftMotor().triggerBackwardOn();
			waitForXMillis(turnbackwardOnExtraTime);
			pi.getRightMotor().triggerForwardOn();
			waitForXMillis(turnforwardOn);
			pi.getRightMotor().triggerForwardOff();
			waitForXMillis(turnbackwardOnExtraTime);
			pi.getLeftMotor().triggerBackwardOff();
			waitForXMillis(turnOff);
		}
	}
	
	public void waitForXMillis(int number) {
		try {
			Thread.sleep(number);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}