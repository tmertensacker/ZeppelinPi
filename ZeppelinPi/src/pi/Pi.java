package pi;
import java.io.IOException;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;



public class Pi {
	HeightManager2 myHeightManager;
	DistanceMonitor myDistance;
	Camera myCamera;
	MotorFixed myLeftMotor;
	MotorFixed myRightMotor;
	MotorPwm myBottomMotor;
	final double maxPower = 1024;
	final double minPower = 824;
	PiState myPiState;
	Executor executor;
	
	//Motor1
	Pin forw1 = RaspiPin.GPIO_07;
	Pin back1 = RaspiPin.GPIO_05;
	//Motor2
	Pin forw2 = RaspiPin.GPIO_00;
	Pin back2 = RaspiPin.GPIO_04;
	//Distance Monitor pins:.
	//Pin 1 = RaspiPin.GPIO_13;
	//Pin 2 = RaspiPin.GPIO_11;
	//Motor4
	Pin forw4 = RaspiPin.GPIO_14;
	Pin back4 = RaspiPin.GPIO_12;
	
	public Pi() {
		myPiState = new PiState(minPower, maxPower);
		myDistance = new DistanceMonitor();
		myCamera = new Camera();
		myBottomMotor = new MotorPwm(forw1, back1);
		//myPiState.setBottomMotorState(1);
		myLeftMotor = new MotorFixed(forw4, back4);
		myRightMotor = new MotorFixed(forw2, back2);
		myHeightManager = new HeightManager2(myBottomMotor,myPiState, myDistance, minPower, maxPower);
		executor = new Executor(this);
	}
	
	public static void main(String [] args)
	{
		int port = Integer.parseInt(args[0]);
		Pi pi = new Pi();
		try{
	 		Thread t = new Thread(new Listener(port, pi));
			Thread hm = new Thread(pi.getHeightManager());
			Thread ex = new Thread(pi.getExecutor());
	 		t.start();
			hm.start();
			ex.start();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}	
	
	public HeightManager2 getHeightManager(){
		return myHeightManager;
	}
	
	public void addCommand(String command) {
		executor.addCommand(command);
	}
	
	private Executor getExecutor() {
		return executor;
	}
	public float getHeight() {
		float returnHeight = myDistance.getDistance();
		myPiState.setCurrentHeight(returnHeight);
		return returnHeight;
		
	}
	
	public void takePicture() {
		try {
			myCamera.makePicture();
		} catch (IOException e) {
			System.out.println("Couldn't make a picture. (IOException)");
		}
	}
	
	public void stop() { //deze methode laat de zeppelin ogenblikkelijk stoppen met commandos uit te voeren, waarnaa hij terug bestuurbaar is door de pijltjestoetsen.
		//executor.stopExecuting();
		executor.clearQueue(); // al dan niet noodzakelijk..?
		myLeftMotor.triggerForwardOff();
		myLeftMotor.triggerBackwardOff();
		myPiState.setLeftMotorState(0);
		myRightMotor.triggerBackwardOff();
		myRightMotor.triggerForwardOff();
		myPiState.setRightMotorState(0);
	}
	public void forwardStart(){
		myLeftMotor.triggerForwardOn();
		myPiState.setLeftMotorState(1);
		myRightMotor.triggerForwardOn();
		myPiState.setRightMotorState(1);
	}
	
	public void forwardStop() {		
		myLeftMotor.triggerForwardOff();
		myPiState.setLeftMotorState(0);
		myRightMotor.triggerForwardOff();
		myPiState.setRightMotorState(0);
	}
	
	/*public void forward(int time) {
		forwardStart();
		try {
			Thread.sleep(time);
		} catch(InterruptedException ex) {
		    Thread.currentThread().interrupt();
		}
		forwardStop();
	}*/
	
	public void backwardStart(){
		myLeftMotor.triggerBackwardOn();
		myPiState.setLeftMotorState(2);
		myRightMotor.triggerBackwardOn();
		myPiState.setRightMotorState(2);
	}
	
	public void backwardStop() {
		myLeftMotor.triggerBackwardOff();
		myPiState.setLeftMotorState(0);
		myRightMotor.triggerBackwardOff();
		myPiState.setRightMotorState(0);
	}
	
	/*public void backward(int time) {
		backwardStart();
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		backwardStop();
	}*/
	
	public void turnLeftStart(){		
		myLeftMotor.triggerBackwardOn();
		myPiState.setLeftMotorState(2);
		myRightMotor.triggerForwardOn();
		myPiState.setRightMotorState(1);
	}
	
	public void turnLeftStop() {
		myLeftMotor.triggerBackwardOff();
		myPiState.setLeftMotorState(0);
		myRightMotor.triggerForwardOff();
		myPiState.setRightMotorState(0);
	}
	
	/*public void turnLeft(int time) {
		turnLeftStart();
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		turnLeftStop();
	}*/
	
	public void turnRightStart(){
		myLeftMotor.triggerForwardOn();
		myPiState.setLeftMotorState(1);
		myRightMotor.triggerBackwardOn();
		myPiState.setRightMotorState(2);
	}
	
	public void turnRightStop() {
		myLeftMotor.triggerForwardOff();
		myPiState.setLeftMotorState(0);
		myRightMotor.triggerBackwardOff();
		myPiState.setRightMotorState(0);
	}
	
	/*public void turnRight(int time) {
		turnRightStart();
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		turnRightStop();
	}*/
	
	public void climbStart(){
		myHeightManager.setRunning(false);
		myBottomMotor.setPower(1024);
		myPiState.setBottomMotorPower(1024);
		myBottomMotor.triggerForwardOn();
		myPiState.setBottomMotorState(1);
	}
	
	public void climbStop() {
		myBottomMotor.setPower(0);
		myPiState.setBottomMotorPower(0);
		myBottomMotor.triggerForwardOff();
		myPiState.setBottomMotorState(0);
		myHeightManager.setTargetHeight(myDistance.getDistance());
		myHeightManager.setRunning(true);
		
	}
	
	public void descendStart(){
		myHeightManager.setRunning(false);
		myBottomMotor.setPower(1024);
		myPiState.setBottomMotorPower(1024);
		myBottomMotor.triggerBackwardOn();
		myPiState.setBottomMotorState(2);
	}
	
	public void descendStop() {
		myBottomMotor.setPower(0);
		myPiState.setBottomMotorPower(0);
		myBottomMotor.triggerBackwardOff();
		myPiState.setBottomMotorState(0);
		myHeightManager.setTargetHeight(myDistance.getDistance());
		myHeightManager.setRunning(true);
	}
	
	public void goToHeight(double newTargetHeight) {
		myHeightManager.setTargetHeight(newTargetHeight);
	}
	
	public String getPiState(){
		return myPiState.toString();
	}
	
	public void terminate() {
		executor.stopExecuting();
		myHeightManager.stopRunning();
		System.exit(0);
	}
	public double getTargetHeight() {
		return myHeightManager.getTargetHeight();
	}
	
	public MotorFixed getLeftMotor() {
		return myLeftMotor;
	}
	
	public MotorFixed getRightMotor() {
		return myRightMotor;
	}
}
