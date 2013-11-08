package pi;
import java.io.IOException;
import com.pi4j.io.gpio.*;



public class Pi {
	HeightManager myHeightManager;
	DistanceMonitor myDistance;
	Camera myCamera;
	MotorFixed myLeftMotor;
	MotorFixed myRightMotor;
	MotorPwm myHeightMotor;
	PIState myPiState;
	
	public Pi() {
		myDistance = new DistanceMonitor();
		myCamera = new Camera();
		//aanpassen naar juiste pinnnen!
		Pin pin1 = RaspiPin.GPIO_10;
		Pin pin2 = RaspiPin.GPIO_11;
		Pin pin3 = RaspiPin.GPIO_12;
		Pin pin4 = RaspiPin.GPIO_13;
		Pin pin5 = RaspiPin.GPIO_14;
		Pin pin6 = RaspiPin.GPIO_15;
		myLeftMotor = new MotorFixed(pin1,pin2);
		myRightMotor = new MotorFixed(pin3,pin4);
		myHeightMotor = new MotorPwm(pin5,pin6);
		myHeightManager = new HeightManager(myHeightMotor);
	}
	
	public static void main(String [] args)
	{
		int port = Integer.parseInt(args[0]);
		Pi pi = new Pi();
		try{
	 		Thread t = new Listener(port, pi);
	 		Thread hm = new Thread(pi.getHM());
			t.start();
			hm.start();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}	
	public HeightManager getHM() {
		return myHeightManager;
	}
	public float getHeight() {
		return myDistance.getDistance();
	}
	
	public void takePicture() {
		try {
			myCamera.makePicture();
		} catch (IOException e) {
			
		}
	}
	
	public void forwardStart(){
		// methodes in PiState kunnen nog veranderen.
		
		myLeftMotor.triggerForwardOn();
		myRightMotor.triggerForwardOn();
		myPiState.toggleStateLeftMotor();
		myPiState.toggleStateRightMotor();
	}
	
	public void forwardStop() {
		// methodes in PiState kunnen nog verandern.
		
		myLeftMotor.triggerForwardOff();
		myRightMotor.triggerForwardOff();
		myPiState.toggleStateLeftMotor();
		myPiState.toggleStateRightMotor();
	}
	
	public void forward(int time) {
		forwardStart();
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
		}
		//System.out.println(System.currentTimeMillis() - currentTime);
		forwardStop();
	}
	
	public void backwardStart(){
		// methodes in PiState kunnen nog veranderen.
		
		myLeftMotor.triggerBackwardOn();
		myRightMotor.triggerBackwardOn();
		myPiState.toggleStateLeftMotor();
		myPiState.toggleStateRightMotor();
	}
	
	public void backwardStop() {
		// methodes in PiState kunnen nog verandern.
		
		myLeftMotor.triggerBackwardOff();
		myRightMotor.triggerBackwardOff();
		myPiState.toggleStateLeftMotor();
		myPiState.toggleStateRightMotor();
	}
	
	public void backward(int time) {
		backwardStart();
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
		}
		backwardStop();
	}
	
	public void turnLeftStart(){
		// methodes in PiState kunnen nog veranderen.
		
		myLeftMotor.triggerBackwardOn();
		myRightMotor.triggerForwardOn();
		myPiState.toggleStateLeftMotor();
		myPiState.toggleStateRightMotor();
	}
	
	public void turnLeftStop() {
		// methodes in PiState kunnen nog verandern.
		
		myLeftMotor.triggerBackwardOff();
		myRightMotor.triggerForwardOff();
		myPiState.toggleStateLeftMotor();
		myPiState.toggleStateRightMotor();
	}
	
	public void turnLeft(int time) {
		turnLeftStart();
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
		}
		turnLeftStop();
	}
	
	public void turnRightStart(){
		// methodes in PiState kunnen nog veranderen.
		
		myLeftMotor.triggerForwardOn();
		myRightMotor.triggerBackwardOn();
		myPiState.toggleStateLeftMotor();
		myPiState.toggleStateRightMotor();
	}
	
	public void turnRightStop() {
		// methodes in PiState kunnen nog verandern.
		
		myLeftMotor.triggerForwardOff();
		myRightMotor.triggerBackwardOff();
		myPiState.toggleStateLeftMotor();
		myPiState.toggleStateRightMotor();
	}
	
	public void turnRight(int time) {
		turnLeftStart();
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
		}
		turnRightStop();
	}
	
	/**
	public void climbStart(){
		// methodes in PiState kunnen nog veranderen.
		
		myHeightMotor.setPower(1024);
		myHeightMotor.triggerForwardOn();
		myPiState.toggleBottomMotorState();
	}
	
	public void climbStop() {
		// methodes in PiState kunnen nog verandern.
		
		myHeightManager.setTargetHeight(myDistance.getDistance());
		myPiState.toggleBottomMotorState();
	}
	
	public void descendStart(){
		// methodes in PiState kunnen nog veranderen.
		
		//myHeightmotor -> zachter
		myPiState.toggleBottomMotorState();
	}
	
	public void descendStop() {
		// methodes in PiState kunnen nog verandern.
		
		myHeightManager.setTargetHeight(myDistance.getDistance());
		myPiState.toggleBottomMotorState();
	}
	*/
	
	public void goToHeight(double height) {
		myHeightManager.setTargetHeight(height);
	}
	
	public String getPiState() {
		return myPiState.toString();
	}
}

