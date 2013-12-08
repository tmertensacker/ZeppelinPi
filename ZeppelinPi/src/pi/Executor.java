


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
	private final double forwardTreshold = 150; // = afstand vanaf waar snelheid constant blijft.
	private double forwardStopParam; // = parameter om tot stilstand te komen, voorwaarts vliegen
	private final double backwardTreshold = 150; // = afstand vanaf waar snelheid constant blijft.
	private double backwardStopParam; // = parameter om tot stilstand te komen, achterwaarts vliegen
	private final double turnTreshold = 90; // = hoek vanaf waar hoeksnelheid constant blijft.
	private double turnStopParam; // = parameter om tot stilstand te komen, draaien.
	private double extraBackwardParam = 0.4;
	

	public Executor(Pi pi) {
		queue = new LinkedList<String>();
		executing = false;
		this.pi = pi;
		forwardOn = 100;
		forwardOff = 200;
		backwardOn = 200;
		backwardOff = 100;
		turnforwardOn = 80;
		turnbackwardOnExtraTime = 50;
		turnOff = 200;
		forwardStopParam = 23.3333;
		backwardStopParam = 10;
		turnStopParam = 10;
	}
	
	public synchronized void run(){
		executing = true;
			while (executing) {
				if (! queue.isEmpty()) {
					String command = queue.poll();

					if (command.contains("goforward ")) {
						List<String> strings = Arrays.asList(command.split("\\s+"));
						double distance = Integer.parseInt(strings.get(1).toString());
						//nieuwe manier:
						//omrekenen naar aantal pulsen:
						double a = 0; // = ???
						double b = 0.10;
						double c = 0;
						double amount = a * Math.pow(Integer.parseInt(strings.get(1).toString()), 2) + b * Integer.parseInt(strings.get(1).toString()) + c;
						forwardPulse((int)amount);
						backward(getForwardStopTime(distance));
					}
					else if (command.contains("gobackward ")) {
						List<String> strings = Arrays.asList(command.split("\\s+"));
						double distance = Integer.parseInt(strings.get(1).toString());
						//nieuwe manier:
						//omrekenen naar aantal pulsen:
						double a = 0; // = ???
						double b = 0.10;
						double c = 0;
						double amount = a * Math.pow(Integer.parseInt(strings.get(1).toString()), 2) + b * Integer.parseInt(strings.get(1).toString()) + c;
						backwardPulse((int)amount);
						forward(getForwardStopTime(distance));
					}
					else if (command.contains("turnleft ")) {
						List<String> strings = Arrays.asList(command.split("\\s+"));
						double angle = Integer.parseInt(strings.get(1).toString());
						//nieuwe manier:
						//omrekenen naar aantal pulsen:
						double a = 0; // = ???
						double b = 0.10;
						double c = 0;
						double amount = a * Math.pow(Integer.parseInt(strings.get(1).toString()), 2) + b * Integer.parseInt(strings.get(1).toString()) + c;
						turnLeftPulse((int)amount);
						turnRightPulse(getTurnStopTime(angle));
					}
					else if (command.contains("turnright ")) {
						List<String> strings = Arrays.asList(command.split("\\s+"));
						double angle = Integer.parseInt(strings.get(1).toString());
						//nieuwe manier:
						//omrekenen naar aantal pulsen:
						double a = 0; // = ???
						double b = 0.10;
						double c = 0;
						double amount = a * Math.pow(Integer.parseInt(strings.get(1).toString()), 2) + b * Integer.parseInt(strings.get(1).toString()) + c;
						turnRightPulse((int)amount);
						turnLeftPulse(getTurnStopTime(angle));
					}
					
					//vanaf hier: wordt allemaal opgelost in Listener!!! (oude code)
					
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
					}
					else if(command.equals("forwardstart"))
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
	
	public void turnRightPulse(int amount) {
		for (int i = 0; i < amount; i++) {
			pi.getRightMotor().triggerBackwardOn();
			pi.getPiStateObject().setRightMotorState(2);
			waitForXMillis(turnbackwardOnExtraTime);
			pi.getLeftMotor().triggerForwardOn();
			pi.getPiStateObject().setLeftMotorState(1);
			waitForXMillis(turnforwardOn);
			pi.getLeftMotor().triggerForwardOff();
			pi.getPiStateObject().setLeftMotorState(0);
			waitForXMillis(turnbackwardOnExtraTime);
			pi.getRightMotor().triggerBackwardOff();
			pi.getPiStateObject().setRightMotorState(0);
			waitForXMillis(turnOff);
		}
	}
	
	public void turnLeftPulse(int amount) {
		for (int i = 0; i < amount; i++) {
			pi.getLeftMotor().triggerBackwardOn();
			pi.getPiStateObject().setLeftMotorState(2);
			waitForXMillis(turnbackwardOnExtraTime);
			pi.getRightMotor().triggerForwardOn();
			pi.getPiStateObject().setRightMotorState(1);
			waitForXMillis(turnforwardOn);
			pi.getRightMotor().triggerForwardOff();
			pi.getPiStateObject().setRightMotorState(0);
			waitForXMillis(turnbackwardOnExtraTime);
			pi.getLeftMotor().triggerBackwardOff();
			pi.getPiStateObject().setLeftMotorState(0);
			waitForXMillis(turnOff);
		}
	}
	
	private void waitForXMillis(int number) {
		long referentionTime = System.currentTimeMillis();
		while (System.currentTimeMillis() < referentionTime + (long)number) {
			
		}
	}
	
	public int getForwardStopTime(double distance) {
		if (distance > forwardTreshold) {
			return (int)(forwardTreshold * forwardStopParam);
		}
		else {
			return (int)(distance * forwardStopParam);
		}		
	}
	
	public int getBackwardStopTime(double distance) {
		if (distance > backwardTreshold) {
			return (int)(backwardTreshold * backwardStopParam);
		}
		else {
			return (int)(distance * backwardStopParam);
		}
	}
	
	public int getTurnStopTime(double angle) {
		if (angle > turnTreshold) {
			return (int)(turnTreshold * turnStopParam);
		}
		else {
			return (int)(angle * turnStopParam);
		}
	}
	
	public void forward(int time) {
		pi.forwardStart();
		waitForXMillis(time);
		pi.forwardStop();
	}
	
	public void backward(int time) {
		pi.backwardStart();
		waitForXMillis(time);
		pi.backwardStop();
	}
	
	public void turnLeft(int time) {
		pi.getLeftMotor().triggerBackwardOn();
		pi.getPiStateObject().setLeftMotorState(2);
		waitForXMillis((int)(time * extraBackwardParam));
		pi.getRightMotor().triggerForwardOn();
		pi.getPiStateObject().setRightMotorState(1);
		waitForXMillis(time);
		pi.getRightMotor().triggerForwardOff();
		pi.getPiStateObject().setRightMotorState(0);
		waitForXMillis((int)(time * extraBackwardParam));
		pi.getLeftMotor().triggerBackwardOff();
		pi.getPiStateObject().setLeftMotorState(0);
	}
	
	public void turnRight(int time) {
		pi.getRightMotor().triggerBackwardOn();
		pi.getPiStateObject().setRightMotorState(2);
		waitForXMillis((int)(time * extraBackwardParam));
		pi.getLeftMotor().triggerForwardOn();
		pi.getPiStateObject().setLeftMotorState(1);
		waitForXMillis(time);
		pi.getLeftMotor().triggerForwardOff();
		pi.getPiStateObject().setLeftMotorState(0);
		waitForXMillis((int)(time * extraBackwardParam));
		pi.getRightMotor().triggerBackwardOff();
		pi.getPiStateObject().setRightMotorState(0);
	}
}