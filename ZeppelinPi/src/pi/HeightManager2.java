package pi;


public class HeightManager2 implements Runnable {
	
	private double targetHeight;
	private DistanceMonitor myDistance;
	private boolean running = true;
	private double balancePower;
	private double lastHeight;
	private double currentPower;
	private double maxPower;
	private double minPower;
	private MotorPwm heightmotor;
	private PiState state;
	private int direction; // 0 = uit; 1 = forward; 2 = backward
	private double vel;
	private double prevTime;
	private double time;
	
	public HeightManager2(MotorPwm heightMotor, PiState pistate, DistanceMonitor distanceMonitor, double minPower, double maxPower){
		myDistance = distanceMonitor;
		this.maxPower = maxPower;
		this.minPower = minPower;
		balancePower = 0;
		//lastHeight = myDistance.getDistance();
		targetHeight = 100;
		this.heightmotor = heightMotor;
		direction = 0;
		state = pistate;
		prevTime = System.currentTimeMillis();
	}
	
	public void terminate(){
		running = false;
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		currentPower = 0;
		applyPower();
	}
	
	public synchronized void run() {
		while(running){
			double old_vel = vel;
			double newDistance = myDistance.getDistance();
			time = System.currentTimeMillis();
			double new_vel = (newDistance - lastHeight)/(time - prevTime);
			state.setCurrentHeight((float)newDistance);
			
			double diff = Math.abs(targetHeight-newDistance);
			
			if (diff < 5 && Math.abs(vel) < 1) {
				// evenwicht
			}
			else{
				if(diff < 5){
					if( vel > 1){
						if(! (direction == 2)){
							heightmotor.triggerForwardOff();
							heightmotor.triggerBackwardOn();
							direction = 2;
							setPower(minPower);
						}
						else{
							setPower(heightmotor.getPower()+10);
						}
					}
					else{
						if(! (direction == 1)){
							heightmotor.triggerBackwardOff();
							heightmotor.triggerForwardOn();
							direction = 1;
							setPower(minPower);
						}
						else{
							setPower(heightmotor.getPower()+10);
						}
					}
				}
				if(newDistance < targetHeight){
					if( vel > 2 ){
						// we gaan naar boven, maar te snel, zet motor naar beneden
						if(! (direction == 2)){
							heightmotor.triggerForwardOff();
							heightmotor.triggerBackwardOn();
							direction = 2;
							setPower(minPower);
						}
						else{
							setPower(heightmotor.getPower()+10);
						}
					}
					else if(vel > 0){
						// we gaan naar boven, kan nog sneller
						if(! (direction == 1)){
							heightmotor.triggerBackwardOff();
							heightmotor.triggerForwardOn();
							direction = 1;
							setPower(minPower);
						}
						else{
							setPower(heightmotor.getPower() + 10);
						}
					}
					else{
						// we gaan naar onder
						if(! (direction == 1)){
							heightmotor.triggerBackwardOff();
							heightmotor.triggerForwardOn();
							direction = 1;
							setPower(minPower);
						}
						else{
							setPower(heightmotor.getPower()+10);
						}
					}
				}
				else{
					if( vel < -2){
						// we gaan naar onder, maar te snel, zet motor naar boven
						if(! (direction == 1)){
							heightmotor.triggerBackwardOff();
							heightmotor.triggerForwardOn();
							direction = 1;
							setPower(minPower);
						}
						else{
							setPower(heightmotor.getPower()+10);
						}
					}
					else if( vel < 0 ){
						// we gaan naar onder, kan sneller
						if(! (direction == 2)){
							heightmotor.triggerForwardOff();
							heightmotor.triggerBackwardOn();
							direction = 2;
							setPower(minPower);
						}
						else{
							setPower(heightmotor.getPower() + 10);
						}
					}
					else{
						// we gaan naar onder
						if(! (direction == 2)){
							heightmotor.triggerForwardOff();
							heightmotor.triggerBackwardOn();
							direction = 2;
							setPower(minPower);
						}
						else{
							setPower(heightmotor.getPower()+10);
						}
					}
				}
			}
			lastHeight = newDistance;
			prevTime = time;
		}
	}
	public void setTargetHeight(double newTargetHeight){
		targetHeight = newTargetHeight;
	}
	
	/*private void setBalancePower(double voltage) {
		if (voltage > maxPower) {
			balancePower = maxPower;
		}
		
		else if (voltage < minPower) {
			balancePower = minPower;
		}
		
		else {
			balancePower = voltage;
		}
	}*/
	
	private void setPower(double voltage) {
		currentPower = voltage;
		heightmotor.setPower((int)voltage);
	}
	
	public double getCurrentPower() {
		return currentPower;
	}
	
	public void setRunning(boolean bool){
		running = bool;		
	}
}
