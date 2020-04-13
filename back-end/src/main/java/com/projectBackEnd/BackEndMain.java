package main.java.com.projectBackEnd;
import io.micronaut.runtime.Micronaut;

/**
 * Main method : runs the server
 */
public class BackEndMain {

	/**
	 * When invoked, this method will cause the Micronaut endpoints to be deployed.
	 * @param args Arguments for command line running.
	 */
	public static void main(String []args){
		Micronaut.run(BackEndMain.class);
	}
}
