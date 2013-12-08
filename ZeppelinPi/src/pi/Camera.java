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
	    Runtime.getRuntime().exec("raspistill -o picture.jpg -t 0");
		// met -n kan je instellen dat er geen preview getoond wordt en dat er dus meteen een foto genomen wordt
	}
	catch(IOException ieo){
		ieo.printStackTrace();
	}
}

}