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
	}
	
	private void startUpward(){
		heightmotor.triggerBackwardOff();
		heightmotor.triggerForwardOn();
		direction = 1;
		setPower(minPower);
	}
	
	private void startDownward(){
		heightmotor.triggerForwardOff();
		heightmotor.triggerBackwardOn();
		direction = 2;
		setPower(minPower+100);
	}
	
	public synchronized void run() {
		while(running){
			double old_vel = vel;
			double newDistance = myDistance.getDistance();
			System.out.println("new distance: "+newDistance);
			time = System.currentTimeMillis();
			vel = (newDistance - lastHeight)/(time - prevTime);
			state.setCurrentHeight((float)newDistance);
			System.out.println("new vel: "+vel);
			double diff = Math.abs(targetHeight-newDistance);
			
			if (diff < 5 && Math.abs(vel) < 0.002) {
				// evenwicht
			}
			else{
				if(diff < 5){
					if( vel > 0.002){
						if(direction == 1){ //omhoog
							int newPower = heightmotor.getPower() - 3;
							if(newPower < minPower){
								startDownward();
							}
							else{
								setPower(newPower);
							}
						}
						else{
							setPower(heightmotor.getPower()+2);
						}
					}
					else{
						if(direction == 2){
							int newPower = heightmotor.getPower() - 1;
							if(newPower < minPower){
								startUpward();
							}
							else{
								setPower(newPower);
							}
						}
						else{
							setPower(heightmotor.getPower()+1);
						}
					}
				}
				else{
					if(newDistance < targetHeight){ // ge moet naar boven
						if( vel > 0.002 ){
							if(direction == 1){ // ge gaat naar boven
								int newPower = heightmotor.getPower() - 3;
								if(newPower < minPower){
									startDownward();
								}
								else{
									setPower(newPower);
								}
							}
							else{
								setPower(heightmotor.getPower()+2);
							}
						}
						else if(vel > 0){
							if(! (direction == 1))
								startUpward();
							else
								setPower(heightmotor.getPower() + 1);
						}
						else{
							if(! (direction == 1))
								startUpward();
							else
								setPower(heightmotor.getPower()+2);
						}
					}
					else{
						if( vel < -0.002){
							if(direction == 2){
								int newPower = heightmotor.getPower() - 3;
								if(newPower < minPower){
									startUpward();
								}
								else{
									setPower(newPower);
								}
							}
							else{
								setPower(heightmotor.getPower()+1);
							}
						}
						else if( vel < 0 ){
							if(! (direction == 2))
								startDownward();
							else
								setPower(heightmotor.getPower() + 2);
						}
						else{
							if(! (direction == 2))
								startDownward();
							else
								setPower(heightmotor.getPower()+1);
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
		if(voltage>maxPower)
			voltage = maxPower;
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
