package pi;

import onPi.Distance;


public class HeightManager implements Runnable {
	
	private double targetHeight;
	private DistanceMonitor myDistance;
	private boolean stop = false;
	private double balancePower;
	private double lastHeight;
	private double currentPower;
	private double maxPower;
	private double minPower;
	private boolean hasBalance;
	private MotorPwm heightmotor;
	
	public HeightManager(MotorPwm heightMotor){
		myDistance = new DistanceMonitor();
		maxPower = 1024;
		minPower = 500;
		hasBalance = false;
		balancePower = 800;
		lastHeight = myDistance.getDistance();
		targetHeight = 50;
		this.heightmotor = heightMotor;
		
		
	}
	public HeightManager(double hoogte){
		targetHeight = hoogte;
	}
	
	
	
	public void terminate() {
		stop = true;
	}
	
	
	//momenteel zal de evenwichtstoestand ingeschakeld blijven als de zeppelin zich op minder van 5cm van de targethoogte bevindt.
	//indien in evenwichtstoestand de zeppelin meer als 2cm/sec daalt/stijgt dan zal het evenwichtsvoltage voor de onderste motor aangepast worden.
	//als de zep dus trager stijgt daalt als 2cm/sec maar wel niet perfect op de zelfde hoogte blijft, zal er vanaf 5 cm afwijking,
	//tijdelijk meer/minder voltage gegeven worden aan de onderste motor, zonder aan het evenwichtsvoltage te raken.
	//vanaf 40 cm afwijking van de targethoogte draait het onderste motor op max/min voltage.
	//indien minder als 40 cm afwijking van targethoogte zal het voltage uitgemiddeld worden tussen evenwichtsvoltage en max-/minvoltage,
	//afhankelijk van de preciese afwijking tussen 0 en 40cm.
	//de parameters hier gebruikt kunnen naar believe aangepast worden, maar principieel zou dit wel moeten werken..?
	
	public synchronized void run(){
		System.out.println("run method in HM");
		while(! stop){
			//System.out.println("start run in HM");
			// als targetHeight ongeveer bereikt is (5 cm afwijking), evenwichtstoestand inschakelen. eventueel balanceVoltage aanpassen.
			while (Math.abs(targetHeight-myDistance.getDistance()) < 5) 
				{
				System.out.println("in lus grootteverschil < 5, grootteverschil = " + (targetHeight-myDistance.getDistance()));
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
				while (Math.abs(myDistance.getDistance() - lastHeight) > 2) {		
					double currentHeight = myDistance.getDistance();
					//
					setBalancePower(currentPower + maxPower*(currentHeight-lastHeight)/200);
					applyBalance();
					lastHeight = currentHeight;
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
					}	
				}
				System.out.println(this.currentPower);
				hasBalance = true;
				
			}
			
			//uit de evenwichtslus indien groot stuk stijgen/dalen.
			double newDistance = myDistance.getDistance();
			//de zep moet stijgen, afhankelijk van hoe ver verwijderd van targetHoogte, vol vermogen, of uitgemiddeld vermogen.
			if (newDistance < targetHeight) {
				//System.out.println("target groter dan huidig");
				System.out.println("huidig = " + myDistance.getDistance());
				if (Math.abs(newDistance - targetHeight) > 40) {
					currentPower = maxPower;
				}
				
				else {
					currentPower = balancePower + (Math.abs(newDistance - targetHeight) / 40)*(maxPower - balancePower);
				}
				System.out.println(this.getCurrentPower());
				applyPower();
			}
			// de zep moet dalen, omgekeerd als hierboven.
			else {
				System.out.println("huidig = " + myDistance.getDistance());
				if (Math.abs(newDistance - targetHeight) > 40) {
					currentPower = minPower;
				}
				
				else {
					
					currentPower = balancePower - (Math.abs(newDistance - targetHeight) / 30)*(balancePower - minPower);
				}
				System.out.println(this.getCurrentPower());
				applyPower();
			}
		}
		
	}
	
	public void setTargetHeight(double newTargetHeight){
		targetHeight = newTargetHeight;
	}
	
	private void setBalancePower(double voltage) {
		if (voltage > maxPower) {
			balancePower = maxPower;
		}
		
		else if (voltage < minPower) {
			balancePower = minPower;
		}
		
		else {
			balancePower = voltage;
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
}
