package pi;
import java.io.IOException;



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
		myHeightManager = new HeightManager(myDistance);
		myCamera = new Camera();
		myLeftMotor = new MotorFixed();
		myRightMotor = new MotorFixed();
		myHeightMotor = new MotorPwm();
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
		
		//myLeftMotor.setOn(richting 1)
		//myRightMotor.setOn(richting 1)
		myPiState.toggleStateLeftMotor();
		myPiState.toggleStateRightMotor();
	}
	
	public void forwardStop() {
		// methodes in PiState kunnen nog verandern.
		
		//myLeftMotor.setOff
		//myRightMotor.setOff
		myPiState.toggleStateLeftMotor();
		myPiState.toggleStateRightMotor();
	}
	
	public void forward(int time) {
		//double currentTime = System.currentTimeMillis();
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
		
		//myLeftMotor.setOn(richting 2)
		//myRightMotor.setOn(richting 2)
		myPiState.toggleStateLeftMotor();
		myPiState.toggleStateRightMotor();
	}
	
	public void backwardStop() {
		// methodes in PiState kunnen nog verandern.
		
		//myLeftMotor.setOff
		//myRightMotor.setOff
		myPiState.toggleStateLeftMotor();
		myPiState.toggleStateRightMotor();
	}
	
	public void backward(int time) {
		//double currentTime = System.currentTimeMillis();
		backwardStart();
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
		}
		//System.out.println(System.currentTimeMillis() - currentTime);
		backwardStop();
	}
	
	public void turnLeftStart(){
		// methodes in PiState kunnen nog veranderen.
		
		//myLeftMotor.setOn(richting 2)
		//myRightMotor.setOn(richting 1)
		myPiState.toggleStateLeftMotor();
		myPiState.toggleStateRightMotor();
	}
	
	public void turnLeftStop() {
		// methodes in PiState kunnen nog verandern.
		
		//myLeftMotor.setOff
		//myRightMotor.setOff
		myPiState.toggleStateLeftMotor();
		myPiState.toggleStateRightMotor();
	}
	
	public void turnLeft(int time) {
		//double currentTime = System.currentTimeMillis();
		turnLeftStart();
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
		}
		//System.out.println(System.currentTimeMillis() - currentTime);
		turnLeftStop();
	}
	
	public void turnRightStart(){
		// methodes in PiState kunnen nog veranderen.
		
		//myLeftMotor.setOn(richting 1)
		//myRightMotor.setOn(richting 2)
		myPiState.toggleStateLeftMotor();
		myPiState.toggleStateRightMotor();
	}
	
	public void turnRightStop() {
		// methodes in PiState kunnen nog verandern.
		
		//myLeftMotor.setOff
		//myRightMotor.setOff
		myPiState.toggleStateLeftMotor();
		myPiState.toggleStateRightMotor();
	}
	
	public void turnRight(int time) {
		//double currentTime = System.currentTimeMillis();
		turnLeftStart();
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
		}
		//System.out.println(System.currentTimeMillis() - currentTime);
		turnRightStop();
	}
	
	public void climbStart(){
		// methodes in PiState kunnen nog veranderen.
		
		//myHeightMotor -> harder
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
	
	public void goToHeight(double height) {
		myHeightManager.setTargetHeight(height);
	}
	
	public String getPiState() {
		return myPiState.toString();
	}
}

