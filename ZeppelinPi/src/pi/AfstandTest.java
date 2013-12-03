package pi;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

public class AfstandTest implements Runnable {
	int aantal = 30;
	MotorFixed myLeftMotor;
	MotorFixed myRightMotor;
	MotorPwm myBottomMotor;
	PiState myPiState;
	final double maxPower = 1024;
	final double minPower = 824;
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
	public AfstandTest() {
		myPiState = new PiState(minPower, maxPower);
		myBottomMotor = new MotorPwm(forw1, back1);
		myLeftMotor = new MotorFixed(forw4, back4);
		myRightMotor = new MotorFixed(forw2, back2);
	}
	public void main( String[] args) {
		AfstandTest af = new AfstandTest();
		Thread t = new Thread(af);
		t.start();
	}
	
	public void run(){
		int i = 0;
		while (i < aantal) {
			try {
				forwardStart();			
				Thread.sleep(100);
				forwardStop();
				Thread.sleep(200);
			}
			catch (InterruptedException e) {
				
			}
			i++;
		}
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
}
