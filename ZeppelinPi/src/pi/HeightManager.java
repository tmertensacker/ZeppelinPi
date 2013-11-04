package pi;



public class HeightManager extends Thread{
	double targetHeight;
	DistanceMonitor myDistance;
	public HeightManager(DistanceMonitor distance){
		targetHeight = 0;
		myDistance = distance;
	}
	public HeightManager(double hoogte){
		targetHeight = hoogte;
	}
	
	public void stayOnHeight(){
		while(true){
			if (myDistance.getDistance() < targetHeight) {
				
			}
			
			else {}
		}
	}
	
	public void setTargetHeight(double newTargetHeight){
		targetHeight = newTargetHeight;
	}
}

