package pi;
import java.io.*;

/**
* Class to control a camera attached to a Raspberry PI.
*/
public class Camera{

public Camera(){}

/**
* Takes a picture and saves it to the main file location of the PI under the name "picture.jpg".
*/
public void makePicture() throws IOException{
	try{
		File file = new File("picture.jpg");
		file.delete();
	    Process p = Runtime.getRuntime().exec("raspistill -o picture.jpg -t 0");
	    try {
			p.waitFor();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//	    long prevTime = System.currentTimeMillis();
//		long currTime = System.currentTimeMillis();
//		while(currTime - prevTime > 3000){
//			currTime = System.currentTimeMillis();
//		}
			// met -n kan je instellen dat er geen preview getoond wordt en dat er dus meteen een foto genomen wordt
	}
	catch(IOException ieo){
		ieo.printStackTrace();
	}
}

}