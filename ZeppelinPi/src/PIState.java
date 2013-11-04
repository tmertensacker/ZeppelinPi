/**
* Class to keep track of all states of the elements of the Pi:
*	- The motor used to control the propellor in order to descend and ascend: bottom motor.
*	- The motor used to control the propellor in order to turn left, move forward or backward: left motor.
*	- The motor used to control the propellor in order to turn right, move forward or backward: right motor.
*	- Last measured distance (blabedkbalbalbalbalbalbalblblablablablablabla)
*
*/
public class PIState{

	/**
	* Variable to keep track of the state of the bottom motor. Either 0 , off, or 1, on. 
	* OPMERKING nog aanpassen want deze motor kan een variabele waarde (voltage) gegeven worden.
	*/
	private int bottomMotorState = 0;
	
	/**
	* Variable to keep track of the state of the left motor. Either 0, off, or 1, on.
	*/
	private int leftMotorState = 0;
	
	/**
	* Variable to keep track of the state of the right motor. Either 0, off, or 1, on.
	*/
	private int rightMotorState = 0;
	
	/**
	* Variable to keep track of the latest distance measured by the discante monitor.
	*/
	private float latestDistanceMeasured = 0;
	
	public PIState(){
		
	}
	
	/**
	* Toggles the state of the bottom motor.
	* If the current state is 0, the state is set to 1.
	* If the current state is 1, the state is set to 0.
	*/
	public void toggleBottomMotorState(){
		if(bottomMotorState == 0){
			bottomMotorState = 1;
		}
		else{
			bottomMotorState = 0;
		}
	}
	
	/**
	* Toggles the state of the left motor.
	* If the current state is 0, the state is set to 1.
	* If the current state is 1, the state is set to 0.
	*/
	public void toggleStateLeftMotor(){
		if(leftMotorState == 0){
			leftMotorState = 1;
		}
		else{
			leftMotorState = 0;
		}
	}
	
	/**
	* Toggles the state of the right motor.
	* If the current state is 0, the state is set to 1.
	* If the current state is 1, the state is set to 0.
	*/
	public void toggleStateRightMotor(){
		if(rightMotorState == 0){
			rightMotorState = 1;
		}
		else{
			rightMotorState = 0;
		}
	}
	
	/**
	* Sets the latest distance measured to the given distance.
	*/
	public void setLatestDistanceMeasured(float newDist){
		latestDistanceMeasured = newDist;
	}
	
	/**
	* Returns the state of the bottom motor.
	*/
	public int getBottomMotorState() {
		return bottomMotorState;
	}

	/**
	* Returns the state of the left motor.
	*/
	public int getLeftMotorState() {
		return leftMotorState;
	}

	/**
	* Returns the state of the right motor.
	*/
	public int getRightMotorState() {
		return rightMotorState;
	}

	/**
	* Returns the latest distance measured.
	*/
	public float getLatestDistanceMeasured() {
		return latestDistanceMeasured;
	}

	
	
}