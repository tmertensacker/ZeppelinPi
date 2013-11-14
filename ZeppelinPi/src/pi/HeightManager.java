package onPi;

import onPi.DistanceMonitor;
import onPi.MotorPwm;


public class HeightManager implements Runnable {
	
	private double targetHeight;
	private DistanceMonitor myDistance;
	private boolean running = true;
	private double balancePower;
	//private double lastHeight;
	private double currentPower;
	private double maxPower;
	private double minPower;
	private MotorPwm heightmotor;
	private int direction; // 0 = uit; 1 = forward; 2 = backward
	
	public HeightManager(MotorPwm heightMotor){
		myDistance = new DistanceMonitor();
		maxPower = 1024;
		minPower = 400;
		balancePower = 800;
		//lastHeight = myDistance.getDistance();
		targetHeight = 50;
		this.heightmotor = heightMotor;
		direction = 0;
		
		
	}
	public HeightManager(double hoogte){
		targetHeight = hoogte;
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
	
	
	//momenteel zal de evenwichtstoestand ingeschakeld blijven als de zeppelin zich op minder van 5cm van de targethoogte bevindt.
	//indien in evenwichtstoestand de zeppelin meer als 2cm/sec daalt/stijgt dan zal het evenwichtsvoltage voor de onderste motor aangepast worden.
	//als de zep dus trager stijgt daalt als 2cm/sec maar wel niet perfect op de zelfde hoogte blijft, zal er vanaf 5 cm afwijking,
	//tijdelijk meer/minder voltage gegeven worden aan de onderste motor, zonder aan het evenwichtsvoltage te raken.
	//vanaf 40 cm afwijking van de targethoogte draait het onderste motor op max/min voltage.
	//indien minder als 40 cm afwijking van targethoogte zal het voltage uitgemiddeld worden tussen evenwichtsvoltage en max-/minvoltage,
	//afhankelijk van de preciese afwijking tussen 0 en 40cm.
	//de parameters hier gebruikt kunnen naar believe aangepast worden, maar principieel zou dit wel moeten werken..?
	
	/*public synchronized void run(){
		while(running){
			// als targetHeight ongeveer bereikt is (2.5 cm afwijking), evenwichtstoestand inschakelen. eventueel balanceVoltage aanpassen.
			while (Math.abs(targetHeight-myDistance.getDistance()) < 2.5 && running) 
				{
				// schakel gemeten balanceVoltage in
				applyBalance();
				// initialiseer lastHeight en wacht 1000 ms
				lastHeight = myDistance.getDistance();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
				}	
				// vergelijk laatse hoogte met nieuwe hoogte, indien te groot (voorlopig groter als 2cm) balanceVoltage aanpassen.
				// nauwkeurigheid distancemeter nog testen.	
				// Hoe juist aanpassen, voorlopig heel eenvoudig, experimenteel voorlopige factor 200 bepalen!				
				if (Math.abs(myDistance.getDistance() - lastHeight) > 1.5) {		
					double currentHeight = myDistance.getDistance();
					//
					setBalancePower(currentPower + maxPower*(currentHeight-lastHeight)/200);
					applyBalance();
				}
				
			}
			
			//uit de evenwichtslus indien groot stuk stijgen/dalen.
			double newDistance = myDistance.getDistance();
			//de zep moet stijgen, afhankelijk van hoe ver verwijderd van targetHoogte, vol vermogen, of uitgemiddeld vermogen.
			if (newDistance < targetHeight) {
				if (Math.abs(newDistance - targetHeight) > 30) {
					currentPower = maxPower;
				}
				
				else {
					setCurrentPower(balancePower + (Math.abs(newDistance - targetHeight) / 30)*(maxPower - balancePower));
				}
				applyPower();
			}
			// de zep moet dalen, omgekeerd als hierboven.
			else {
				if (Math.abs(newDistance - targetHeight) > 30) {
					currentPower = minPower;
				}
				
				else {
					
					setCurrentPower(balancePower - (Math.abs(newDistance - targetHeight) / 30)*(balancePower - minPower));
				}
				applyPower();
			}
		}
		
	}*/
	
	public synchronized void run() {
		while(running){
			// als targetHeight ongeveer bereikt is (2.5 cm afwijking), evenwichtstoestand inschakelen. eventueel balanceVoltage aanpassen.
			while (Math.abs(targetHeight-myDistance.getDistance()) < 3 && running) 
				{
				// schakel gemeten balanceVoltage in
				applyBalance();				
			}
			//uit de evenwichtslus indien groot stuk stijgen/dalen.
			double newDistance = myDistance.getDistance();
			//de zep moet stijgen, afhankelijk van hoe ver verwijderd van targetHoogte, vol vermogen, of uitgemiddeld vermogen.
			if (newDistance < targetHeight) {
				if (Math.abs(newDistance - targetHeight) > 30) {
					if (!(direction == 1)) {
						heightmotor.triggerBackwardOff();
						heightmotor.triggerForwardOn();	
						direction = 1;
					}
					
					currentPower = maxPower;
				}
				
				else {
					setCurrentPower(minPower + (Math.abs(newDistance - targetHeight) / 30)*(maxPower - minPower));
				}
				applyPower();
			}
			// de zep moet dalen, omgekeerd als hierboven.
			else {
				if (!(direction == 2)) {
					heightmotor.triggerForwardOff();
					heightmotor.triggerBackwardOn();
					direction = 2;
				}
				
				if (Math.abs(newDistance - targetHeight) > 30) {
					currentPower = maxPower;
				}
				
				else {
					
					setCurrentPower(minPower + (Math.abs(newDistance - targetHeight) / 30)*(maxPower - minPower));
				}
				applyPower();
			}
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
	
	private void setCurrentPower(double voltage) {
		if (voltage > maxPower) {
			currentPower = maxPower;
		}
		
		else if (voltage < minPower) {
			currentPower = minPower;
		}
		
		else {
			currentPower = voltage;
		}
	}
	
	public double getCurrentPower() {
		return currentPower;
	}
	
	private void applyBalance() {
		currentPower = balancePower;
		applyPower();
	}
	
	public void applyPower() {
		this.heightmotor.setPower((int)currentPower);
	}
	
	public double getBalance() {
		return balancePower;
	}
	
	public void setRunning(boolean bool){
		running = bool;		
	}
}