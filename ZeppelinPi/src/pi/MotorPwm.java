package pi;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.GpioPinPwmOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

public class MotorPwm {
	
	/*public static void main( String[] args ) {
		try{
			System.out.println("Hello");
			//Motor 4
			Pin forw = RaspiPin.GPIO_12;
			Pin back = RaspiPin.GPIO_14;
			MotorPwm motor = new MotorPwm(forw, back);
			System.out.println("Current power = "+ motor.getPower());
			motor.setPower(1024);
			System.out.println("Set power to: "+motor.getPower());
			Thread.sleep(3000);
			motor.setPower(800);
			System.out.println("Set power to: "+motor.getPower());
			Thread.sleep(3000);
			motor.setPower(500);
			System.out.println("Set power to: "+motor.getPower());
			Thread.sleep(3000);
			motor.setPower(200);
			System.out.println("Set power to: "+motor.getPower());
			Thread.sleep(3000);
			motor.setPower(0);
			System.out.println("Set power to: "+motor.getPower());
		}
		catch(InterruptedException ex){
			Thread.currentThread().interrupt();
		}
	}*/
	
	private final GpioController gpio = GpioFactory.getInstance();
	//pwmPin final houden?
	private final GpioPinPwmOutput pwmPin = gpio.provisionPwmOutputPin(RaspiPin.GPIO_01);
	private final GpioPinDigitalOutput trigPinForward;
	private final GpioPinDigitalOutput trigPinBackward;

	public MotorPwm(Pin trigPinForward, Pin trigPinBackward){
		pwmPin.setPwm(0);	
		this.trigPinForward = gpio.provisionDigitalOutputPin(trigPinForward);
		this.trigPinBackward = gpio.provisionDigitalOutputPin(trigPinBackward);
		this.trigPinForward.low();
		this.trigPinBackward.low();
		
	}
	
	public void setPower(int x){
		if(x >= 0  && x <=1024){	
		pwmPin.setPwm(x);
		}
		else{
			System.err.println("Tried to set the power of Pwm motor to a value out of range.");
		}
	}
	
	public int getPower(){
		return pwmPin.getPwm();
	}
	public void triggerForwardOn(){
		trigPinForward.high();
	}
	
	public void triggerForwardOff(){
		trigPinForward.low();
	}

	public void triggerBackwardOn(){
		trigPinBackward.high();
	}
	
	public void triggerBackwardOff(){
		trigPinBackward.low();
	}
}

