package pi;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

public class TestHeightManager {
		public static void main(String[] args) throws InterruptedException {
			Pin forw1 = RaspiPin.GPIO_05;
			Pin back1 = RaspiPin.GPIO_07;
			MotorPwm myMotor = new MotorPwm(forw1, back1);
			HeightManager myHM = new HeightManager(myMotor);
			Thread tmyHM = new Thread(myHM);
			tmyHM.start();
		}
	}

