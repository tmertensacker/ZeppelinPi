package pi;


public class HeightManager3 implements Runnable {
	
	private double targetHeight;
	private DistanceMonitor myDistance;
	private boolean running = true;
	private double maxPower;
	private double minPower;
	private MotorPwm heightmotor;
	private PiState state;
	private int direction; // 0 = uit; 1 = forward; 2 = backward
	private double accumulator;
	
	private double [] error;
	
	public HeightManager3(MotorPwm heightMotor, PiState pistate, DistanceMonitor distanceMonitor, double minPower, double maxPower){
		myDistance = distanceMonitor;
		this.maxPower = maxPower;
		this.minPower = minPower;
		this.targetHeight = 50;
		this.heightmotor = heightMotor;
		this.direction = 0;
		this.state = pistate;
		this.error = new double[10];
		this.accumulator = 0;
	}
	
	private double pTerm = 1;
	private double iTerm = 0;
	private double dTerm = 0;
	
	public synchronized void run() {
		while(running){
			double pid;
			calcError();
			state.setCurrentHeight((float)error[0]);
			
			pid = pTerm * error[0];
			accumulator += error[0];
			pid += iTerm * accumulator;
			pid += dTerm * (error[0] - error[3]);
			//System.out.println("pre_pid= "+pid);
			double pre_pid = pid;
			if(Math.abs(pid) > (maxPower - minPower)){
				pid = maxPower - minPower;
				accumulator -= (pre_pid-pid);
			}
			//System.out.println("pid= "+pid);
			int power = (int) Math.round(Math.abs(pid)+minPower);
			if(pid < 0){
				System.out.println("pid < 0");
				if(!(direction==2)){
					//System.out.println("startdownward");
					startDownward(power);
				}
				else{
					setPower(power);
				}
			}
			else{
				if(!(direction==1)){
					//System.out.println("startupward");
					startUpward(power);
				}
				else{
					setPower(power);
				}
			}
			//System.out.println("direction:" +direction);
			//System.out.println("accumulator= "+accumulator);
		}
	}
	
	private void calcError(){
		for(int i=(error.length-1); i > 0; i--)
			error[i] = error[i-1];
		double newDistance = myDistance.getDistance();
		error[0] = targetHeight - newDistance;
		System.out.println("newDistance: "+newDistance);
		//System.out.println("new error: "+error[0]);
	}
	
	public void terminate(){
		running = false;
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		heightmotor.setPower(0);
		state.setBottomMotorPower(0);
	}
	
	private void startUpward(int power){
		heightmotor.triggerBackwardOff();
		heightmotor.triggerForwardOn();
		direction = 1;
		setPower(power);
		state.setBottomMotorState(1);
		state.setBottomMotorPower(power);
	}
	
	private void startUpward(){
		startUpward((int)minPower);
	}
	
	private void startDownward(){
		startDownward((int)minPower);
	}
	
	private void startDownward(int power){
		heightmotor.triggerForwardOff();
		heightmotor.triggerBackwardOn();
		direction = 2;
		setPower(power);
		state.setBottomMotorState(2);
		state.setBottomMotorPower(power);
	}
	
	public void setTargetHeight(double newTargetHeight){
		targetHeight = newTargetHeight;
	}
	
	private void setPower(double voltage) {
		if(voltage>maxPower)
			voltage = maxPower;
		if(voltage<minPower)
			voltage = minPower;
		heightmotor.setPower((int)voltage);
		state.setBottomMotorPower((int)voltage);
	}
}
